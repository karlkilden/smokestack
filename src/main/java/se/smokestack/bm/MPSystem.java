package se.smokestack.bm;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
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
//				sleep(1000L);

			}
			boolean wait = true;
			long lastSize = 0L;
			int checks = 0;
			while (wait) {
//				sleep(WAIT);
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
//			sleep(WAIT);
			log.info("finished");
			List<String> lines = FileUtils.readLines(new File(targetPath+"usr"),Charset.defaultCharset());
			
			if (isAuth(lines)) {
//				
//				stopService(conf);
//				// copy war
//				
//				startService(conf);
				
			}
			else {
// handle no auth
				}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	private boolean isAuth(List<String> lines) {
		return confHolder.getUsers().contains(lines.get(0));
	}


}
