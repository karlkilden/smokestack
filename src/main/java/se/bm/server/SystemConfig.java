package se.bm.server;

import java.util.Set;

import se.bm.server.ftp.ScriptKey;

public class SystemConfig {
	private String service;
	private int shutdowmPort;
	private String hostPort;
	private String host;
	private Set<String> wars;
	private String tomcatHome;
	private long shutdownWait;
	private boolean hotdeploy;
	private Set<String> users;
	
	private transient String system;
	private transient String inputDir;
	private transient String outPutDir;
	private transient ScriptKey scriptKey;
	private transient String ftpFetchDir;
	private transient String ftpPutDir;
	
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public int getShutdowmPort() {
		return shutdowmPort;
	}
	public void setShutdowmPort(int shutdowmPort) {
		this.shutdowmPort = shutdowmPort;
	}
	public String getHostPort() {
		return hostPort;
	}
	public void setHostPort(String hostPort) {
		this.hostPort = hostPort;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Set<String> getWars() {
		return wars;
	}
	public void setWars(Set<String> wars) {
		this.wars = wars;
	}
	public String getTomcatHome() {
		return tomcatHome;
	}
	public void setTomcatHome(String tomcatHome) {
		this.tomcatHome = tomcatHome;
	}
	public long getShutdownWait() {
		return shutdownWait;
	}
	public void setShutdownWait(long shutdownWait) {
		this.shutdownWait = shutdownWait;
	}
	public boolean isHotdeploy() {
		return hotdeploy;
	}
	public void setHotdeploy(boolean hotdeploy) {
		this.hotdeploy = hotdeploy;
	}
	public Set<String> getUsers() {
		return users;
	}
	public void setUsers(Set<String> users) {
		this.users = users;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getInputDir() {
		return inputDir;
	}
	public void setInputDir(String inputDir) {
		this.inputDir = inputDir;
	}
	public String getOutPutDir() {
		return outPutDir;
	}
	public void setOutPutDir(String outPutDir) {
		this.outPutDir = outPutDir;
	}
	public ScriptKey getScriptKey() {
		return scriptKey;
	}
	public void setScriptKey(ScriptKey scriptKey) {
		this.scriptKey = scriptKey;
	}
	public String getFtpFetchDir() {
		return ftpFetchDir;
	}
	public void setFtpFetchDir(String ftpFetchDir) {
		this.ftpFetchDir = ftpFetchDir;
	}
	public String getFtpPutDir() {
		return ftpPutDir;
	}
	public void setFtpPutDir(String ftpPutDir) {
		this.ftpPutDir = ftpPutDir;
	}

}
