package se.smokestack.parse;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Nashorn {

	public void test() {
		ScriptEngineManager m = new ScriptEngineManager();
		ScriptEngine nashorn = m.getEngineByName("nashorn");
		String test ="apa";
		try {
			nashorn.eval("print(test)");
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
