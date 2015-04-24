package se.smokestack.boot;

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
	Map<String, Conf> confMap = new HashMap<>();
	
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
	public void add(String cmd, String sys, Properties p) {
		configuredCommands.add(cmd);
		configuredSystems.add(sys);
		Conf c = new Conf(cmd, sys, p);
		confMap.put(c.toString(), c);
		
	}
	
	
}
