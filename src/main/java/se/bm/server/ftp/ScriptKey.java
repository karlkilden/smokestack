package se.bm.server.ftp;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ScriptKey {
	public static final String BROKER_FETCH = "broker.fetch";
	public static final String BROKER_PUT = "broker.put";

	private boolean dynamic;
	private String name;

	public ScriptKey(boolean dynamic, String name) {
		super();
		this.dynamic = dynamic;
		this.name = name + ".txt";
	}



	public boolean isDynamic() {
		return dynamic;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public boolean equals(Object other) {
		ScriptKey script = (ScriptKey) other;
		EqualsBuilder eb = new EqualsBuilder();
		return eb.append(script.name, this.name).build();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder eb = new HashCodeBuilder();
		return eb.append(this.name).build();
	}

}
