package se.smokestack.bm;

import java.util.Objects;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.base.Strings;

public class WinSCPScript {

	private String copyDestination;
	private String copyTarget;
	private String ftpDir;
	private String ftpUrl;
	private String scriptName;
	private String remove = "";
	private String templateKey;

	public static final String COPY_DESTINATION = "%copy.destination%";
	public static final String COPY_TARGET = "%copy.target%";
	public static final String FTP_DIR = "%ftp.dir%";
	public static final String FTP_URL = "%ftp.url%";
	public static final String REMOVE = "%remove%";

	private static final String GET_TEMPLATE = "winspc_get";
	private static final String PUT_TEMPLATE = "winspc_put";

	public static final String[] TEMPLATES = new String[] { GET_TEMPLATE, PUT_TEMPLATE };

	private WinSCPScript(String scriptName) {
		this.scriptName = scriptName;
	}

	public static WinSCPScript getWriteToBrokerInstance(String scriptName, BMConfig bmConfig) {
		return new WinSCPScript(scriptName + ".txt").ftpUrl(bmConfig.getFtpURL())
				.ftpDir(bmConfig.getFtpDir() + "/" + bmConfig.getFtpBmDir() + "/" + "brokerget" + "/" + bmConfig.getBMCommand().getSystem())
				.destination("/" + bmConfig.getBMCommand().getUser() + "/");
	}

	public static WinSCPScript getInstance(String scriptName) {
		return new WinSCPScript(scriptName + ".txt");
	}

	public WinSCPScript build() {
		notEmpty(scriptName, ftpUrl, copyDestination, copyTarget, ftpDir, ftpUrl, scriptName);
		return this;
	}

	private void notEmpty(String... fields) {
		for (String s : fields) {
			String field = Strings.emptyToNull(s);
			Objects.requireNonNull(field, "WinSCPScript cannot handle null values." + toString());
		}
	}

	public WinSCPScript destination(String destination) {
		this.copyDestination = destination;
		return this;
	}

	public WinSCPScript target(String target) {
		this.copyTarget = target;
		return this;
	}

	public WinSCPScript ftpDir(String dir) {
		this.ftpDir = dir;
		return this;
	}

	public WinSCPScript ftpUrl(String url) {
		this.ftpUrl = url;
		return this;
	}

	public WinSCPScript get() {
		templateKey = GET_TEMPLATE;
		return this;
	}

	public WinSCPScript put() {
		templateKey = PUT_TEMPLATE;
		return this;
	}

	public String getCopyDestination() {
		return copyDestination;
	}

	public String getCopyTarget() {
		return copyTarget;
	}

	public String getFtpDir() {
		return ftpDir;
	}

	public String getFtpUrl() {
		return ftpUrl;
	}

	public String create(String template) {
		String generatedScript = template;
		generatedScript = generatedScript.replace(COPY_DESTINATION, copyDestination);
		generatedScript = generatedScript.replace(COPY_TARGET, copyTarget);
		generatedScript = generatedScript.replace(FTP_DIR, ftpDir);
		generatedScript = generatedScript.replace(FTP_URL, ftpUrl);
		generatedScript = generatedScript.replace(REMOVE, remove);
		return generatedScript;
	}

	public String getScriptName() {
		return scriptName;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public String getTemplateKey() {
		return templateKey;
	}

	@Override
	public boolean equals(Object other) {
		WinSCPScript script = (WinSCPScript) other;
		EqualsBuilder eb = new EqualsBuilder();
		return eb.append(script.getScriptName(), this.getScriptName()).build();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder eb = new HashCodeBuilder();
		return eb.append(this.getScriptName()).build();
	}

	public WinSCPScript remove(String file, boolean createAgain) {
		this.remove = "rm " + file;
		if (createAgain) {
			this.remove = this.remove + System.lineSeparator() + "mkdir " + file;
		}
		return this;
	}

	public WinSCPScript removeTarget(boolean createAgain) {
		this.remove(copyTarget, createAgain);
		return this;
	}

}
