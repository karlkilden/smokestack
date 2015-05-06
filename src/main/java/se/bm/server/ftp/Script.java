package se.bm.server.ftp;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Script {
	private static final String PARAM_IDENTIFIER = "#{";
	private String script;
	private ScriptKey key;


	public Script(ScriptKey key, String script) {
		this.key = key;
		if (script.contains(PARAM_IDENTIFIER)) {
			throw new IllegalStateException("Script is not complete " + toString());
		}
		this.script = script;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public String get() {
		return script;
	}

	public String getName() {
		return key.getName();
	}

	public ScriptKey getKey() {
		return key;
	}

}
