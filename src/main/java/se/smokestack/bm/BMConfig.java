package se.smokestack.bm;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.enterprise.inject.Alternative;
import javax.security.auth.login.FailedLoginException;

import org.apache.deltaspike.core.util.ExceptionUtils;

@Alternative
public class BMConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	private String warfileName;
	private String ftpURL;
	private String ftpDir;
	private String ftpBmDir;
	private String tomcatService;
	private String tomcatHost;
	private int tomcatPort;
	private String tomcatShutdownPort;
	private String tomcatHome;
	private long tomcatShutdownSleep;
	private String localTempDir;
	private String winscpDir;
	private boolean shutdownWhenNewWar;
	private Set<String> users;
	private String system;
	private boolean client;
	private boolean broker;
	private String warfilesLocation;
	private long loopInterval;
	private String brokerFolder;

	private Map<String, String> systems = new HashMap<>();

	private String jenkinsURL;
	private Map<String, String> jobNames = new HashMap<>();
	private Map<String, String> warNames = new HashMap<>();
	private String brokerParentDir; // For clients

	private String brokerResponseDir;

	private transient BMCommand bmCommand;
	private transient Set<String> userDirs = new HashSet<>();
	private transient String userCommandPath;
	private transient String userDir;
	private transient String userDirTemplate;

	private String serverTempDir;

	public void postConstruct() {
		userDirTemplate = serverTempDir + "\\" + system + "\\";
		
		for (String user : users) {
			String userDir = userDirTemplate + user + "\\";
			userDirs.add(userDir);
		}

	}

	public void addMetadata(BMCommand bmCommand) {
		requireNonNull(bmCommand, "this operation requires bmCommand");
		this.bmCommand = bmCommand;
		userDir = userDirTemplate + bmCommand.getUser() + "\\";

		this.userCommandPath = userDir + BMCommand.BM_COMMAND;

		authenticate();
	}

	private void authenticate() {
		if (!getUsers().contains(bmCommand.getUser())) {
			ExceptionUtils.throwAsRuntimeException(new FailedLoginException("user:" + bmCommand.getUser() + " not found"));
		}
	}

	public String getWarfileName() {
		return warfileName;
	}

	public void setWarfileName(String warfileName) {
		this.warfileName = warfileName;
	}

	public String getFtpURL() {
		return ftpURL;
	}

	public void setFtpURL(String ftpURL) {
		this.ftpURL = ftpURL;
	}

	public String getFtpDir() {
		return ftpDir;
	}

	public void setFtpDir(String ftpDir) {
		this.ftpDir = ftpDir;
	}

	public String getFtpBmDir() {
		return ftpBmDir;
	}

	public void setFtpBmDir(String ftpBmDir) {
		this.ftpBmDir = ftpBmDir;
	}

	public String getTomcatService() {
		return tomcatService;
	}

	public void setTomcatService(String tomcatService) {
		this.tomcatService = tomcatService;
	}

	public String getTomcatHost() {
		return tomcatHost;
	}

	public void setTomcatHost(String tomcatHost) {
		this.tomcatHost = tomcatHost;
	}

	public int getTomcatPort() {
		return tomcatPort;
	}

	public void setTomcatPort(int tomcatPort) {
		this.tomcatPort = tomcatPort;
	}

	public String getTomcatShutdownPort() {
		return tomcatShutdownPort;
	}

	public void setTomcatShutdownPort(String tomcatShutdownPort) {
		this.tomcatShutdownPort = tomcatShutdownPort;
	}

	public String getTomcatHome() {
		return tomcatHome;
	}

	public void setTomcatHome(String tomcatHome) {
		this.tomcatHome = tomcatHome;
	}

	public long getTomcatShutdownSleep() {
		return tomcatShutdownSleep;
	}

	public void setTomcatShutdownSleep(long tomcatShutdownSleep) {
		this.tomcatShutdownSleep = tomcatShutdownSleep;
	}

	public String getLocalTempDir() {
		return localTempDir;
	}

	public void setLocalTempDir(String localTempDir) {
		this.localTempDir = localTempDir;
	}

	public String getWinscpDir() {
		return winscpDir;
	}

	public void setWinscpDir(String winscpDir) {
		this.winscpDir = winscpDir;
	}

	public boolean isShutdownWhenNewWar() {
		return shutdownWhenNewWar;
	}

	public void setShutdownWhenNewWar(boolean shutdownWhenNewWar) {
		this.shutdownWhenNewWar = shutdownWhenNewWar;
	}

	public Set<String> getUsers() {
		return users;
	}

	public void setUsers(Set<String> users) {
		this.users = users;
	}

	public Set<String> getUserDirs() {
		return userDirs;
	}

	public BMCommand getBMCommand() {
		return bmCommand;
	}

	public void setData(BMCommand data) {
		this.bmCommand = data;
	}

	public String getUserCommandPath() {
		return userCommandPath;

	}

	public Path fullWarFilePath() {
		return Paths.get(userDir + "/" + warfileName);
	}

	public Path fullWarFileTargetPath() {
		return Paths.get(tomcatHome + "webapps/" + warfileName);
	}

	public Path fullLogsPath() {
		return Paths.get(tomcatHome + "logs/");

	}

	public Path fullLogsTargetPath() {
		return Paths.get(userDir + "logs/");
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public boolean isClient() {
		return client;
	}

	public Map<String, String> getSystems() {
		return systems;
	}

	public void setSystems(Map<String, String> systems) {
		this.systems = systems;
	}

	public String brokerDir() {
		return brokerParentDir + brokerFolder;
	}

	public Map<String, String> getJobNames() {
		return jobNames;
	}

	public void setJobNames(Map<String, String> jobNames) {
		this.jobNames = jobNames;
	}

	public Map<String, String> getWarNames() {
		return warNames;
	}

	public void setWarNames(Map<String, String> warNames) {
		this.warNames = warNames;
	}

	public String getJenkinsURL() {
		return jenkinsURL;
	}

	public void setJenkinsURL(String jenkinsURL) {
		this.jenkinsURL = jenkinsURL;
	}

	public String getWarfilesLocation() {
		return warfilesLocation;
	}

	public void setWarfilesLocation(String warfilesLocation) {
		this.warfilesLocation = warfilesLocation;
	}

	public long getLoopInterval() {
		return loopInterval;
	}

	public void setLoopInterval(long loopInterval) {
		this.loopInterval = loopInterval;
	}

	public String getBrokerResponseDir() {
		return brokerResponseDir;
	}

	public void setBrokerResponseDir(String brokerResponseDir) {
		this.brokerResponseDir = brokerResponseDir;
	}

	public boolean isBroker() {
		return broker;
	}

	public void setBroker(boolean broker) {
		this.broker = broker;
	}

	public String getBrokerFolder() {
		return brokerFolder;
	}

	public void setBrokerFolder(String brokerFolder) {
		this.brokerFolder = brokerFolder;
	}

	public String getBrokerParentDir() {
		return brokerParentDir;
	}

	public void setBrokerParentDir(String brokerParentDir) {
		this.brokerParentDir = brokerParentDir;
	}

	public String getServerTempDir() {
		return serverTempDir;
	}

}
