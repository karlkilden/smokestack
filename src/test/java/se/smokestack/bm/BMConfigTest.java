package se.smokestack.bm;

import java.util.HashMap;
import java.util.Map;

import org.hjson.JsonValue;
import org.hjson.Stringify;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;


public class BMConfigTest {
	public static void main(String[] args) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		BMConfig config = new BMConfig();
		config.setFtpBmDir("test");
		config.setFtpURL("test2");
		config.setUsers(Sets.newHashSet("testa", "test2"));
		Map<String, String> systems = new HashMap<>();
		systems.put("trunk", "c:\test\test");
		config.setSystems(systems);
		String hjsonString = JsonValue.readHjson(mapper.writeValueAsString(config)).toString(Stringify.HJSON);
		System.out.println(hjsonString);
	}
}
