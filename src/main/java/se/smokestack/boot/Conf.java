package se.smokestack.boot;

import java.util.Properties;

import com.google.common.base.Splitter;

public class Conf {
	
	public static class Cmd {
		public static final String WAR ="w";	
		public static final String PRICELIST ="p";	
		public static final String RESTART ="r";
	}

	protected String key;
	protected String cmd;
	protected String sys;
	
	public Conf(String cmd, String sys, Properties p) {
		this.cmd = cmd;
		this.sys = sys;
		this.key = createKey(cmd, sys);
	}
	
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public String getSys() {
		return sys;
	}
	public void setSys(String sys) {
		this.sys = sys;
	}
	
	@Override
	public String toString() {
		return key;
	}

	public static String createKey(String cmd, String sys) {
		return cmd+"."+sys;
	}
	
	 public static Iterable<String> split(String token) {
		 return Splitter.on(',')
			       .trimResults()
			       .omitEmptyStrings()
			       .split(token);
	 }


	public static Conf build(String cmd, String sys2, Properties properties) {
		switch (cmd) {
		case Conf.Cmd.WAR:
			return new WarConf(cmd, sys2, properties);
			
		case Conf.Cmd.PRICELIST:
			return new WarConf(cmd, sys2, properties);
			
		case Conf.Cmd.RESTART:
			return new WarConf(cmd, sys2, properties);
		default:
			throw new IllegalArgumentException("unkown cmd " + cmd);
		}
	}
}
