package se.bm.client;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import se.bm.core.ContextualMapper;
import se.bm.core.UserOptions;
import se.smokestack.bm.BMCommand;
import se.smokestack.bm.Command;

public class ClientCommand {

	@Inject
	ClientConfig clientConfig;

	private Map<Command, ClientHandler> handlers;

	private BMCommand cmd;

	private String currentPutDir;

	private String currentFetchDir;
	private String currentBrokerPutDir;
	private String currentBrokerFetchDir;

	private String brokerDir;

	private String warFileName;

	public String putDir;

	public void handle(String system, String cmdString, UserOptions options) {
		cmd = new BMCommand(Command.valueOf(StringUtils.upperCase(cmdString)));
		cmd.setOptions(options);
		cmd.setUser(clientConfig.getUser());
		cmd.setSystem(system);
		this.currentPutDir = clientConfig.getPutDir(system);
		this.currentFetchDir= clientConfig.getFetchDir(system);
		this.currentBrokerFetchDir = clientConfig.getBrokerFetchDir(system);
		this.currentBrokerPutDir = clientConfig.getBrokerPutDir(system);
		handlers.get(cmd.getCommand()).handle();
	}

	public BMCommand cmd() {
		return cmd;
	}

	public void setCommand(Command cmd) {
		this.cmd.setCommand(cmd);
	}

	public String getWarFileName() {
		return warFileName;
	}

	public void setWarFileName(String warFileName) {
		this.warFileName = warFileName;
	}

	public String getBrokerDir() {
		return brokerDir;
	}

	public void setBrokerDir(String brokerDir) {
		this.brokerDir = brokerDir;
	}

	public String getCurrentFetchDir() {
		return currentFetchDir;
	}

	public void setCurrentFetchDir(String currentFetchDir) {
		this.currentFetchDir = currentFetchDir;
	}

	public String getCurrentPutDir() {
		return currentPutDir;
	}

	public void setCurrentPutDir(String currentPutDir) {
		this.currentPutDir = currentPutDir;
	}

	@PostConstruct
	private void setup() {
		handlers = ContextualMapper.getContextualMap(Command.class, ClientHandler.class);
	}

	public String getCurrentBrokerFetchDir() {
		return currentBrokerFetchDir;
	}

	public void setCurrentBrokerFetchDir(String currentBrokerFetchDir) {
		this.currentBrokerFetchDir = currentBrokerFetchDir;
	}

	public String getCurrentBrokerPutDir() {
		return currentBrokerPutDir;
	}

	public void setCurrentBrokerPutDir(String currentBrokerPutDir) {
		this.currentBrokerPutDir = currentBrokerPutDir;
	}

}
