package se.smokestack.boot;

import java.util.logging.Level;

public class JulLogHandler {

	private final java.util.logging.Logger noisylogger1;

	public JulLogHandler() {
		noisylogger1 = java.util.logging.Logger.getLogger("org.apache.webbeans.corespi.scanner.AbstractMetaDataDiscovery");
        noisylogger1.setLevel(Level.SEVERE);
	}
	
}
