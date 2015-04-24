package se.smokestack.boot;

import static se.smokestack.boot.Conf.split;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ConfHolder {
	
	private List<Conf> confs = new ArrayList<>();
	List<String> activeCommands = new ArrayList<>();
	
	Set<String> configuredCommands = new HashSet<>();
	Set<String> configuredSystems = new HashSet<>();
	private Set<String> users = new HashSet<>();
	Map<String, Conf> confMap = new HashMap<>();
	private Properties properties;
	private String winscp;
	
	
	public ConfHolder (Properties properties) {
		this.properties = properties;
		split(this.properties.getProperty("users")).forEach(u -> users.add(u));
		winscp = this.properties.getProperty("winscp");
	}
	
	public ConfHolder build(Iterable<String> targetSystems, String[] params) {
		for (String system : targetSystems) {
			for (int i = 1; i < params.length; i++) {
				String c = params[i].replace("-", "");
				addToActivatedConfs(c, system);
			}
		}
		return this;
	}
	private void addToActivatedConfs(String c, String system) {
		String mapKey = Conf.createKey(c, system);
		confs.add(confMap.get(mapKey));
		
	}
	public void add(String cmd, String sys) {
		configuredCommands.add(cmd);
		configuredSystems.add(sys);
		Conf c = Conf.build(cmd, sys, properties);
		confMap.put(c.toString(), c);
		
	}
	public List<Conf> getConfs() {
		return confs;
	}
	public void setConfs(List<Conf> confs) {
		this.confs = confs;
	}

	public String getWinscp() {
		return winscp;
	}

	public void setWinscp(String winscp) {
		this.winscp = winscp;
	}

	public String getFTP() {
		return properties.getProperty("ftp");
	}

	public Set<String> getUsers() {
		return users;
	}
	
}
