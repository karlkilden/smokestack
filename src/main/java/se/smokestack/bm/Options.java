package se.smokestack.bm;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Alternative;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.beust.jcommander.Parameter;

@Alternative
public class Options {

	@Parameter(description = "Commands")
	private List<String> commands = new ArrayList<>();

	@Parameter(names = "-force", description = "force tomcat service to stop")
	private boolean forceShutdown;
	
	@Parameter(names = "-resume", description = "Skip ftp calls and resume as if all files needed are downloaded")
	private boolean resume;
	
	@Parameter(names = "-nobuild", description = "skip call to jenkins")
	private boolean noBuild;
	
	@Parameter(names = "-noclean", description = "Skip removing files for debug purposes")
	private boolean noClean;
	
	@Parameter(names = "-cmd", description = "Command of interest")
	private String command;
	
	@Parameter(names = "-nowait", description = "Skip waiting for response")
	private boolean skipWait;

	@Parameter(names = "-keepData", description = "Keeps the data previously fetched. This may give false positives on reciving data")
	private boolean keepData;
	

	public boolean isForceShutdown() {
		return forceShutdown;
	}

	public void setForceShutdown(boolean forceShutdown) {
		this.forceShutdown = forceShutdown;
	}



	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public List<String> getCommands() {
		return commands;
	}

	public void setCommands(List<String> commands) {
		this.commands = commands;
	}

	public boolean isNoBuild() {
		return noBuild;
	}

	public void setNoBuild(boolean noBuild) {
		this.noBuild = noBuild;
	}

	public boolean isResume() {
		return resume;
	}

	public void setResume(boolean resume) {
		this.resume = resume;
	}

	public boolean isNoClean() {
		return noClean;
	}

	public void setNoClean(boolean noClean) {
		this.noClean = noClean;
	}

	public String getCommand() {
		return command;
	}

	public boolean isSkipWait() {
		return skipWait;
	}

	public boolean isKeepData() {		
		return keepData;
	}
}
