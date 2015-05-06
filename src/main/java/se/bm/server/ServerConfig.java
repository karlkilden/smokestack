package se.bm.server;

import static se.bm.core.Constants.FETCH;
import static se.bm.core.Constants.PUT;
import static se.bm.core.Constants.SLASH;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import se.bm.core.ConfigResolver;
import se.bm.core.Constants;

@ApplicationScoped
public class ServerConfig implements Serializable {
	@Inject
	private ConfigResolver configResolver;

	private static final long serialVersionUID = 1L;
	private String ftpURL;
	private String serverTempDir;
	private boolean broker;

	private transient String ftpDir = Constants.FTP.HOME;
	private transient String winscpDir;
	private transient String home;
	private transient String putDir;
	private transient String fetchDir;
	private transient String ftpFetchDir = ftpDir + Constants.FTP.FTP_SLASH + Constants.FETCH + Constants.FTP.FTP_SLASH;
	private transient String ftpPutDir = ftpDir + Constants.FTP.FTP_SLASH + Constants.PUT + Constants.FTP.FTP_SLASH;

	public String getFtpURL() {
		return ftpURL;
	}

	public void setFtpURL(String ftpURL) {
		this.ftpURL = ftpURL;
	}

	public String getServerTempDir() {
		return serverTempDir;
	}

	public void setServerTempDir(String serverTempDir) {
		this.serverTempDir = serverTempDir;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public String getPutDir() {
		return putDir;
	}

	public void setPutDir(String putDir) {
		this.putDir = putDir;
	}

	public String getFtpDir() {
		return ftpDir;
	}

	public void setFtpDir(String ftpDir) {
		this.ftpDir = ftpDir;
	}

	public String getFtpGetDir() {
		return ftpFetchDir;
	}

	public void setFtpGetDir(String ftpInputDir) {
		this.ftpFetchDir = ftpInputDir;
	}

	public String getWinscpDir() {
		return winscpDir;
	}

	public void setWinscpDir(String winscpDir) {
		this.winscpDir = winscpDir;
	}

	@PostConstruct
	public void loadFromConfig() {
		ServerConfig sc = configResolver.readServerConfig();
		ftpURL = sc.getFtpURL();
		serverTempDir = sc.getServerTempDir();
		winscpDir = configResolver.getHome() + Constants.WIN_SCP;
		home = configResolver.getHome() + Constants.Server.HOME + SLASH;
		putDir = home + PUT + SLASH;
		fetchDir = home + FETCH + SLASH;
	}

	public String getFetchDir() {
		return fetchDir;
	}

	public void setFetchDir(String fetchDir) {
		this.fetchDir = fetchDir;
	}

	public String getFtpPutDir() {
		return ftpPutDir;
	}

	public void setFtpPutDir(String ftpPutDir) {
		this.ftpPutDir = ftpPutDir;
	}

	public boolean isBroker() {
		return broker;
	}

	public void setBroker(boolean broker) {
		this.broker = broker;
	}

	public String getFtpFetchDir() {
		return ftpFetchDir;
	}

	public void setFtpFetchDir(String ftpFetchDir) {
		this.ftpFetchDir = ftpFetchDir;
	}

}
