package se.smokestack.bm;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BirdmanTest {
	@Test
	public void test (){
		BMCommand data = new BMCommand(Command.WAR);
		data.setUser("Kalle");
		data.setHandled(true);
		data.setOptions(new Options());

		ObjectMapper mapper = new ObjectMapper();
		try {
			System.out.println(mapper.writeValueAsString(data));
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		;
	}
}
