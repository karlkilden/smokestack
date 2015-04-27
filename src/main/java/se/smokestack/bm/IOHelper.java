package se.smokestack.bm;

import static java.lang.Integer.parseInt;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.deltaspike.core.util.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
public class IOHelper {
	private static final Logger LOG = LogManager.getLogger();

	private static final String CMD_C = "cmd /c ";
	private static final String SCRIPT_NAME = "%script_name%";
	private static final String WINSPC_DIR = "%winspc_dir%";
	private static final String WINSCP_SCRIPT_TEMPLATE = CMD_C + WINSPC_DIR + "WinSCP.exe /script=" + WINSPC_DIR + SCRIPT_NAME;

	@Inject
	private ProcessRunner processRunner;

	@Inject
	private BMConfig bmConfig;
	
	@Inject
	WinSCPHandler winSCPHandler;

	public static List<String> lines(String fileName) {

		try {
			return FileUtils.readLines(new File(fileName), Charset.defaultCharset());
		} catch (IOException e) {
			ExceptionUtils.throwAsRuntimeException(e);
		}
		return null;
	}

	public void winscp(WinSCPScript script) {
		script.build();
		winSCPHandler.writeScriptToDisk(script);
		String cmd = WINSCP_SCRIPT_TEMPLATE.replace(WINSPC_DIR, bmConfig.getWinscpDir());
		cmd = cmd.replace(SCRIPT_NAME, script.getScriptName());
		processRunner.runProcess(cmd);
	}

	public void startService() {
		serviceCmd("start");
	}
	
	public void serviceCmd(String action) {
		String cmd = "cmd /c start mpservice.bat " + action +" " + bmConfig.getTomcatService();
		processRunner.runProcess(cmd);
	}

	public void stopTomcat() {
		try (Socket socket = new Socket(bmConfig.getTomcatHost(), parseInt(bmConfig.getTomcatShutdownPort()));
				PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);) {
			if (socket.isConnected()) {
				pw.println("SHUTDOWN");// send shut down command
			}
		} catch (NumberFormatException e) {
			ExceptionUtils.throwAsRuntimeException(e);
		} catch (ConnectException e) {
			LOG.info("tomcat already stopped");
		} catch (IOException e) {
			ExceptionUtils.throwAsRuntimeException(e);

		}
		IOHelper.sleep(bmConfig.getTomcatShutdownSleep());
		if (bmConfig.getBMCommand().getOptions().isForceShutdown()) {
			serviceCmd("stop");
		}
	}

	public void startTomcat() {
		startService();
	}

	public static final void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			ExceptionUtils.throwAsRuntimeException(e);
		}
	}

}
