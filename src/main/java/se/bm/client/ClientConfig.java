package se.bm.client;

import static com.google.common.io.Files.createParentDirs;
import static se.bm.core.Constants.SLASH;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.deltaspike.core.util.ExceptionUtils;

import se.bm.core.ConfigResolver;
import se.bm.core.Constants;

@ApplicationScoped
public class ClientConfig {

	@Inject
	private ConfigResolver configResolver;

	private String brokerDir;
	private String jenkinsURL;
	private List<WarInfo> warInfos;
	private String home;
	private transient String user;
	private transient Map<String, String> fetchDirs = new HashMap<>();
	private transient Map<String, String> putDirs = new HashMap<>();

	public String getBrokerDir() {
		return brokerDir;
	}

	public void setBrokerDir(String brokerDir) {
		this.brokerDir = brokerDir;
	}

	public String getJenkinsURL() {
		return jenkinsURL;
	}

	public void setJenkinsURL(String jenkinsURL) {
		this.jenkinsURL = jenkinsURL;
	}

	public List<WarInfo> getWarInfos() {
		return warInfos;
	}

	public void setWarInfos(List<WarInfo> warInfos) {
		this.warInfos = warInfos;
	}

	@PostConstruct
	public void loadFromConfig() {
		ClientConfig sc = configResolver.readClientConfig();
		this.jenkinsURL = sc.getJenkinsURL();
		this.brokerDir = sc.getBrokerDir();
		this.warInfos = sc.getWarInfos();
		this.home = configResolver.getHome() + Constants.Client.HOME + SLASH;
		this.user = System.getProperty("user.name");

		setupPaths();

	}

	private void setupPaths() {
		for (WarInfo info : warInfos) {
			try {
				setupMaps(info, putDirs, Constants.PUT);
				setupMaps(info, fetchDirs, Constants.FETCH);

			} catch (IOException e) {
				ExceptionUtils.throwAsRuntimeException(e);
			}

		}
	}

	private void setupMaps(WarInfo info, Map<String, String> dirs, String operation) throws IOException {
		String base = operation + info.getSystem() + Constants.SLASH + user + Constants.SLASH;
		dirs.put(info.getSystem(), base);
		createParentDirs(new File(getHome() + base));

	}

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	public String getUser() {
		return user;
	}

	public String getPutDir(String system) {
		return getBrokerDir() + putDirs.get(system);
	}

	public String getFetchDir(String system) {
		return getHome() + fetchDirs.get(system);
	}

	public String getBrokerFetchDir(String system) {
		return getHome() + fetchDirs.get(system);

	}

	public String getBrokerPutDir(String system) {
		return getBrokerDir() +  putDirs.get(system);

	}

}
