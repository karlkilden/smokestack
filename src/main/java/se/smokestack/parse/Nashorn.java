package se.smokestack.parse;

import java.io.FileReader;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.deltaspike.core.api.exception.control.event.ExceptionToCatchEvent;

public class Nashorn {

	@Inject
	Event<ExceptionToCatchEvent> event;
	private ScriptEngineManager m;
	private ScriptEngine engine;

	public String manipulate(String s) {
		try {
			engine.put("currentLine", s);
			engine.eval(new FileReader("stack.js"));
			Object result;

			result = ((Invocable) engine).invokeFunction("manipulate", s);
			return (String) result;
		} catch (Exception e) {
			event.fire(new ExceptionToCatchEvent(e));
		}
		return s;
	}

	@PostConstruct
	private void init() {
		m = new ScriptEngineManager();
		engine = m.getEngineByName("nashorn");
	}

	public String replace(String s) {
		try {
			engine.put("currentLine", s);
			engine.eval(new FileReader("stack.js"));
			Object result;

			result = ((Invocable) engine).invokeFunction("replace", s);
			return (String) result;
		} catch (Exception e) {
			event.fire(new ExceptionToCatchEvent(e));
		}
		return s;
	}
	
	public ScriptEngine engine() {
		return engine;
	}
}
