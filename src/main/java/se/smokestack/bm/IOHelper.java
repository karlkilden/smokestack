package se.smokestack.bm;

import static se.smokestack.bm.PropertyKeys.TOMEE_HOST;
import static se.smokestack.bm.PropertyKeys.TOMEE_SHUTDOWN_PORT;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.deltaspike.core.util.ExceptionUtils;

@ApplicationScoped
public class IOHelper {

	private static final String CMD_C = "cmd /c ";

	private static final String SCRIPT_NAME = "%script_name%";

	private static final String WINSPC_DIR = "%winspc_dir%";

	private static final String WINSCP_SCRIPT_TEMPLATE = CMD_C + WINSPC_DIR + "WinSCP.exe /script=" + WINSPC_DIR + SCRIPT_NAME;

	@Inject
	private ProcessRunner processRunner;

	@Inject
	private PropertyHelper propertyHelper;

	public static List<String> lines(String fileName) {

		try {
			return FileUtils.readLines(new File(fileName), Charset.defaultCharset());
		} catch (IOException e) {
			ExceptionUtils.throwAsRuntimeException(e);
		}
		return null;
	}

	public void winscp(String scriptName) {
		String winscpDir = propertyHelper.get(PropertyKeys.WINSCP_DIR);
		String cmd = WINSCP_SCRIPT_TEMPLATE.replace(WINSPC_DIR, winscpDir);
		cmd = cmd.replace(SCRIPT_NAME, scriptName);
		processRunner.runProcess(cmd);
	}

	public void startService() {
		// String command = "cmd /c start startmp.bat "+ " " + "";
		// Process process = Runtime.getRuntime().exec(command);
		// process.waitFor();
	}

	public void stopTomcat() {
		try (Socket socket = new Socket(propertyHelper.get(TOMEE_HOST), Integer.parseInt(propertyHelper.get(TOMEE_SHUTDOWN_PORT)));
				PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);) {
			if (socket.isConnected()) {
				pw.println("SHUTDOWN");// send shut down command
			}
		} catch (Exception e) {
			ExceptionUtils.throwAsRuntimeException(e);
		}
	}

}
