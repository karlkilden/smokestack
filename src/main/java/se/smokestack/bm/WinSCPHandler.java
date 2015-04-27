package se.smokestack.bm;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.deltaspike.core.util.ExceptionUtils;

@ApplicationScoped
public class WinSCPHandler {

	private Map<String, String> templates = new HashMap<>();

	@Inject
	private BMConfig bmConfig;

	public void writeScriptToDisk(WinSCPScript script) {
		String file = script.create(templates.get(script.getTemplateKey()));
		try {
			Files.write(Paths.get(bmConfig.getWinscpDir() + script.getScriptName()), file.getBytes());
		} catch (IOException e) {
			ExceptionUtils.throwAsRuntimeException(e);
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
