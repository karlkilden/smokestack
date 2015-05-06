package se.smokestack.bm;

import javax.inject.Inject;

import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import se.bm.server.ServerBuilder;

@RunWith(CdiTestRunner.class)
public class BMCommandReaderTest {
	@Inject
	BMCommandReader reader;
	@Inject

	ServerBuilder systemBuilder;

	@Test
	public void test() {
		systemBuilder.setupServerConfigs();
		reader.processCommands(systemBuilder.getSystems().get(0));
	}

}
