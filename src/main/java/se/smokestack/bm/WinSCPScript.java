package se.smokestack.bm;

import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.base.Strings;

public class WinSCPScript {

	private String copyDestination;
	private String copyTarget;
	private String ftpDir;
	private String ftpUrl;
	private String scriptName;
	
	private String templateKey;

	public static final String COPY_DESTINATION = "%copy.destination%";
	public static final String COPY_TARGET = "%copy.target%";
	public static final String FTP_DIR = "%ftp.dir%";
	public static final String FTP_URL = "%ftp.url%";
	
	private static final String GET_TEMPLATE = "winspc_get";
	private static final String PUT_TEMPLATE = "winspc_put";
	
	public static final String[] TEMPLATES = new String[] {GET_TEMPLATE, PUT_TEMPLATE};

	private WinSCPScript(String scriptName) {
		this.scriptName = scriptName;
	}

	public static WinSCPScript getInstance(String scriptName) {
		return new WinSCPScript(scriptName+".txt");
	}

	public WinSCPScript build() {
		notEmpty(scriptName, ftpUrl, copyDestination, copyTarget, ftpDir,
				ftpUrl, scriptName);
		return this;
	}

	private void notEmpty(String... fields) {
		for (String s : fields) {
			String field = Strings.emptyToNull(s);
			Objects.requireNonNull(field,
					"WinSCPScript cannot handle null values." + toString());
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


}
