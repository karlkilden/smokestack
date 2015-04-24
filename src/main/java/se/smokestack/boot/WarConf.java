package se.smokestack.boot;

import java.util.Properties;

public class WarConf extends Conf {

	private String ftpdir;
	private String ftpfile;
	private String target;
	private String warname;
	private String tomee;
	private String service;

	public WarConf(String cmd, String sys, Properties p) {
		super(cmd, sys, p);
		ftpdir = p.getProperty(key + ".ftpdir");
		ftpfile = p.getProperty(key + ".ftpfile");
		target = p.getProperty(key + ".target");
		warname = p.getProperty(key + ".warname");
		tomee = p.getProperty(key + ".tomee");
		service = p.getProperty(key + ".service");

	}

	public String getFtpdir() {
		return ftpdir;
	}

	public String getFtpfile() {
		return ftpfile;
	}

	public String getTarget() {
		return target;
	}

	public String getWarname() {
		return warname;
	}

	public String getTomee() {
		return tomee;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

}
