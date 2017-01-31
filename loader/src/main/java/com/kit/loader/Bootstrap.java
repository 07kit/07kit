package com.kit.loader;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 */
public class Bootstrap {
	private static Logger logger = Logger.getLogger(Bootstrap.class);

	public static void main(String[] args) throws Exception {
		String jvmArgs = "-XX:+UseG1GC";
		boolean maxHeapSet = false;
		for (String arg : args) {
			if (arg.startsWith("maxHeap=")) {
				maxHeapSet = true;
				logger.info("Max heap size: " + arg.substring(arg.indexOf('='), arg.length()));
				jvmArgs += " -Xmx" + arg.substring(arg.indexOf('='), arg.length());
			}
		}
		if (!maxHeapSet) {
			jvmArgs += " -Xmx424M";

		}
		String cmd = "java -cp \"" + System.getProperty("java.class.path") + "\" " + jvmArgs + " " + Application.class.getCanonicalName();
		Runtime runtime = Runtime.getRuntime();
		Process proc;
		if (!System.getProperty("os.name").toLowerCase().contains("win")) {
			proc = runtime.exec(new String[]{"/bin/sh", "-c", cmd});
			logger.info("Started process by command " + cmd);
		} else {
			proc = runtime.exec(cmd);
		}
		final Process process = proc;
		new Thread(new InputStreamConsumer(process.getInputStream())).start();
		new Thread(new InputStreamConsumer(process.getErrorStream())).start();
	}

	private static class InputStreamConsumer implements Runnable {
		private final InputStream stream;

		InputStreamConsumer(InputStream stream) {
			this.stream = stream;
		}

		@Override
		public void run() {
			final BufferedReader input = new BufferedReader(new
					InputStreamReader(stream));
			String buf = "";
			try {
				while ((buf = input.readLine()) != null) {
					System.out.println(buf);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
