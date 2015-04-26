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
	PropertyHelper propertyHelper;
	
	@Test
		public void testWriteScriptToDisk() throws Exception {
			WinSCPScript script = WinSCPScript.getInstance("test")
					.destination("temp")
					.target(propertyHelper.get(PropertyKeys.FTP_BIRDMAN_DIR))
					.ftpDir(propertyHelper.get(PropertyKeys.FTP_DIR))
					.ftpUrl(propertyHelper.get(PropertyKeys.FTP_ULR)).build();
			handler.writeScriptToDisk(script);
		}

}
