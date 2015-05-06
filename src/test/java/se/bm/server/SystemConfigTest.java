package se.bm.server;

import org.hjson.JsonValue;
import org.hjson.Stringify;
import org.junit.Test;

import se.bm.server.SystemConfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;


public class SystemConfigTest {
	@Test
	public void generate() throws JsonProcessingException {
		
		ObjectMapper mapper = new ObjectMapper();
		SystemConfig conf = new SystemConfig();
		conf.setHost("host");
		conf.setUsers(Sets.newHashSet("user", "user2"));
		conf.setWars(Sets.newHashSet("war", "war2"));
		String hjsonString = JsonValue.readHjson(mapper.writeValueAsString(conf)).toString(Stringify.HJSON);
		System.out.println(hjsonString);
	}
}
