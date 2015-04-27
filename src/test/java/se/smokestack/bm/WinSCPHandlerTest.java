package se.smokestack.bm;

import javax.inject.Inject;

import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
@RunWith(CdiTestRunner.class)
public class WinSCPHandlerTest {

	@Inject
	WinSCPHandler handler;
	
	@Inject
	BMConfig bmConfig;
	
	@Test
		public void testWriteScriptToDisk() throws Exception {

//			handler.writeScriptToDisk(script);
		}

}
