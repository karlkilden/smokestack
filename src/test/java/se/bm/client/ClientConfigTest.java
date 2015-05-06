package se.bm.client;

import org.hjson.JsonValue;
import org.hjson.Stringify;
import org.junit.Test;

import se.bm.server.SystemConfig;

import com.beust.jcommander.internal.Lists;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;

public class ClientConfigTest {

	@Test
	public void testGetBrokerDir() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		ClientConfig conf = new ClientConfig();
		conf.setBrokerDir("brokerdir");
		conf.setJenkinsURL("jenkins");
		conf.setWarInfos(Lists.newArrayList(new WarInfo()));
		String hjsonString = JsonValue.readHjson(mapper.writeValueAsString(conf)).toString(Stringify.HJSON);
		System.out.println(hjsonString);	}

}
