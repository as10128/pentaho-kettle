package be.ibridge.kettle.trans.step;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import junit.framework.TestCase;
import be.ibridge.kettle.core.Const;
import be.ibridge.kettle.core.LogWriter;
import be.ibridge.kettle.trans.StepLoader;
import be.ibridge.kettle.trans.Trans;
import be.ibridge.kettle.trans.TransMeta;

public abstract class KettleStepUseCase extends TestCase {
	public static final String REPLAY_DATE = "16032006-051637";

	public LogWriter log;

	public TransMeta meta;

	public Trans trans;

	public String directory;

	protected void setUp() throws Exception {
		super.setUp();
		log = LogWriter.getInstance(Const.SPOON_LOG_FILE, false,
				LogWriter.LOG_LEVEL_ROWLEVEL);
		StepLoader.getInstance().read();
	}

	public abstract String getFileExtension();

	protected void tearDown() throws Exception {
		File[] files = new File(directory).listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return !(name.endsWith("ktr")
						|| name.endsWith(getFileExtension())
						|| name.endsWith(".svn") || name
						.endsWith(REPLAY_DATE + ".line"));
			}
		});
		if (files != null)
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}
		super.tearDown();
	}

	public void expectFiles(String directory, int expected) {
		String[] files = new File(directory).list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return !(name.endsWith(".svn"));
			}
		});
		if (files == null)
			assertEquals(0, expected);
		else
			assertEquals(expected, files.length);
	}

	public void expectContent(String filename, String expectedContent)
			throws IOException {
		FileInputStream stream = new FileInputStream(filename);
		try {
			StringBuffer buffer = new StringBuffer();
			int read = 0;
			while ((read = stream.read()) != -1) {
				buffer.append((char) read);
			}
			assertEquals(expectedContent, buffer.toString());
		} finally {
			stream.close();
		}
	}

}
