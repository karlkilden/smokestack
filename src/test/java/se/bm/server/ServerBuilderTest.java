package se.bm.server;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import se.bm.server.ServerBuilder;

@RunWith(CdiTestRunner.class)
public class ServerBuilderTest {
	
	@Inject
	private ServerBuilder configReader;
	@Test
	public void test() {
		configReader.setupServerConfigs();
		assertEquals(2,configReader.systems().size());
	}
}
