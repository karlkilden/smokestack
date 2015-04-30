package se.smokestack.bm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.deltaspike.core.util.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
public class WinSCPHandler {

	private Map<String, String> templates = new HashMap<>();
	private Set<WinSCPScript> savedScripts = new HashSet<>();
	private Set<WinSCPScript> dynamicScripts = new HashSet<>();
	private static final Logger LOG = LogManager.getLogger();

	@Inject
	private BMConfig bmConfig;

	public void writeScriptToDisk(WinSCPScript script, boolean cache) {

		if (!savedScripts.contains(script)) {
			String file = script.create(templates.get(script.getTemplateKey()));
			try {
				Files.write(Paths.get(bmConfig.getWinscpDir() + script.getScriptName()), file.getBytes());
			} catch (IOException e) {
				ExceptionUtils.throwAsRuntimeException(e);
			}
			if (cache) {
				savedScripts.add(script);
			}
			if (cache == true) {
				LOG.debug(script);
			} else {
				debugOnce(script);
			}
		}
	}

	private void debugOnce(WinSCPScript script) {
		dynamicScripts.add(script);
		if (!dynamicScripts.contains(script)) {
			LOG.debug(script);
		}
	}

	@PostConstruct
	private void readFile() {
		for (String template : WinSCPScript.TEMPLATES) {
			templates.put(template, readTemplate(template));

		}
	}

	private String readTemplate(String name) {
		List<String> lines = IOHelper.lines(name);
		return StringUtils.join(lines, System.lineSeparator());
	}
}
