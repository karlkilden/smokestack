package se.bm.server;

import org.hjson.JsonValue;
import org.hjson.Stringify;
import org.junit.Test;

import se.bm.server.config.SystemConfig;

import se.bm.server.ServerConfig;
import se.bm.server.SystemConfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;


public class ServerConfigTest {

	
	@Test
	public void generate() throws JsonProcessingException {
		
		ObjectMapper mapper = new ObjectMapper();
		ServerConfig config = new ServerConfig();

		SystemConfig conf = new SystemConfig();
		conf.setHost("host");
		conf.setUsers(Sets.newHashSet("user", "user2"));
		conf.setWars(Sets.newHashSet("war", "war2"));
		String hjsonString = JsonValue.readHjson(mapper.writeValueAsString(config)).toString(Stringify.HJSON);
		System.out.println(hjsonString);
	}
	
}
