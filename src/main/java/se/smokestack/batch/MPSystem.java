package se.smokestack.batch;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.CharEncoding;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import se.smokestack.boot.Conf;
import se.smokestack.boot.ConfHolder;
import se.smokestack.boot.WarConf;

public class MPSystem {
	private static final Logger log = LogManager.getLogger();

	private static final long WAIT = 100L;

	private ConfHolder confHolder;

	public void handleConfs(ConfHolder confs) {
		this.confHolder = confs;

		for (Conf conf : confHolder.getConfs()) {
			if (conf instanceof WarConf) {
				handleWar((WarConf) conf);
			}

		}
	}

	private void handleWar(WarConf conf) {
		try {

			String command = "cmd /c start" + " " + confHolder.getWinscp() + "war.bat" + " " + confHolder.getFTP() + " " + conf.getFtpdir() + " "
					+ conf.getFtpfile() + " " + conf.getTarget();
			System.out.println(command);
			Process process = Runtime.getRuntime().exec(command);
			process.waitFor();
			String targetPath = conf.getTarget() + "\\props\\";
			String filename = targetPath + conf.getWarname();
			File f = new File(filename);
			System.out.println(filename);
			while (!f.canRead()) {
				sleep(1000L);

			}
			boolean wait = true;
			long lastSize = 0L;
			int checks = 0;
			while (wait) {
				sleep(WAIT);
				long size = f.getTotalSpace();
				log.info("size {}, lastSize {}", size, lastSize);
				boolean noChange = lastSize == size;
				checks = noChange ? checks+1 : 0;
				log.info("check {}", checks);
				if (checks == 3) {
					wait = false;
				}
				lastSize = size;
			}
			sleep(WAIT);
			log.info("finished");
			List<String> lines = FileUtils.readLines(new File(targetPath+"usr"),Charset.defaultCharset());
			
			if (isAuth(lines)) {
				
				stopService(conf);
				
				
			}
			else {
// handle no auth
				}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void stopService(WarConf conf) throws Exception {
		try { 
		    Socket socket = new Socket("localhost", 8005); 
		    if (socket.isConnected()) { 
		        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true); 
		        pw.println("SHUTDOWN");//send shut down command 
		        pw.close(); 
		        socket.close(); 
		    } 
		} catch (Exception e) { 
		    e.printStackTrace(); 
		}
		
		
	}

	private boolean isAuth(List<String> lines) {
		return confHolder.getUsers().contains(lines.get(0));
	}

	private void sleep(long time) throws InterruptedException {
		Thread.sleep(time);
	}

}
