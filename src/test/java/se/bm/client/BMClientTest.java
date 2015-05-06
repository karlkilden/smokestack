package se.bm.client;

import javax.inject.Inject;

import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
@RunWith(CdiTestRunner.class)
public class BMClientTest {
	@Inject
	BMClient client;

	@Test
	public void testStart() throws Exception {
		client.start(new String[0]);
	}

	@Test
	public void testRunNewCommand() throws Exception {
	}

	@Test
	public void testConfigPath() throws Exception {
	}

}
