package se.smokestack.boot;

import java.util.Properties;

public class Conf {

	private String key;
	private String value;
	private String cmd;
	private String sys;
	
	public Conf(String cmd, String sys, Properties p) {
		this.cmd = cmd;
		this.sys = sys;
		this.key = createKey(cmd, sys);
		this.value = p.getProperty(key);
	}
	
	public String getValue() {
		return value;
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
}
