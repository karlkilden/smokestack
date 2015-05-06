package se.smokestack.bm;

import static java.lang.Integer.parseInt;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.deltaspike.core.util.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Strings;

import se.bm.server.ServerConfig;
import se.bm.server.ftp.Script;
import se.bm.server.ftp.ScriptKey;

@ApplicationScoped
public class IOHelper {
	public static final String SLASH = "\\";

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
	ServerConfig serverConfig;

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

	public void winscp(WinSCPScript script, boolean cache) {
		script.build();
		winSCPHandler.writeScriptToDisk(script, cache);
		String cmd = WINSCP_SCRIPT_TEMPLATE.replace(WINSPC_DIR, bmConfig.getWinscpDir());
		cmd = cmd.replace(SCRIPT_NAME, script.getScriptName());
		processRunner.runProcess(cmd);
	}

	public void winscp(WinSCPScript script) {
		winscp(script, true);
	}

	public void startService() {
		serviceCmd("start");
	}

	public void serviceCmd(String action) {
		String cmd = "mpservice.bat " + action + " " + bmConfig.getTomcatService();
		processRunner.runBat(cmd);
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

	public void winscp(Script script) {
		writeScriptToDisk(script);
		String cmd = WINSCP_SCRIPT_TEMPLATE.replace(WINSPC_DIR, serverConfig.getWinscpDir());
		cmd = cmd.replace(SCRIPT_NAME, script.getName());
		processRunner.runProcess(cmd);
	}

	private Set<ScriptKey> savedScripts = new HashSet<>();

	public void writeScriptToDisk(Script script) {

		if (!savedScripts.contains(script.getKey())) {
			String file = script.get();
			try {
				Files.write(Paths.get(serverConfig.getWinscpDir() +SLASH +script.getName()), file.getBytes());
			} catch (IOException e) {
				ExceptionUtils.throwAsRuntimeException(e);
			}
			if (!script.getKey().isDynamic()) {
				savedScripts.add(script.getKey());
			}
		}
	}
	
	public void validatePath(String dir, String msg) {
		String d = Strings.emptyToNull(dir);
		Objects.requireNonNull(d, "required configuration missing: " + msg);
		File test = new File(d);
		if(!test.exists()) {
			throw new IllegalStateException("required path missing: "+ msg);
		}
	}

}
