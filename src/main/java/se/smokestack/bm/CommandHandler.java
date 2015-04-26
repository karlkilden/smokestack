package se.smokestack.bm;

import java.io.File;
import java.io.IOException;

import static se.smokestack.bm.PropertyKeys.FTP_DIR;
import static se.smokestack.bm.PropertyKeys.FTP_BIRDMAN_DIR;
import static se.smokestack.bm.PropertyKeys.FTP_ULR;
import static se.smokestack.bm.PropertyKeys.LOCAL_TEMP_DIR;

import javax.inject.Inject;

import org.apache.deltaspike.core.util.ExceptionUtils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class CommandHandler {
	private static final ObjectMapper MAPPER = new ObjectMapper();
	@Inject
	PropertyHelper propertyHelper;

	@Inject
	WinSCPHandler winSCPHandler;

	@Inject
	private IOHelper ioHelper;

	abstract Command command();

	abstract void handle(Metadata data);

	void markHandled(Metadata data) {
		data.setHandled(true);
		String userMetaFile = propertyHelper.getUserDir(data) + Metadata.METADATA;

		try {
			
			MAPPER.writeValue(new File(userMetaFile), data);
			WinSCPScript script = WinSCPScript.getInstance(Metadata.PUT_FILE_NAME).put().destination(data.getUser() + "/").target(userMetaFile)
					.ftpDir(propertyHelper.get(FTP_DIR) + "/" + propertyHelper.get(FTP_BIRDMAN_DIR)).ftpUrl(propertyHelper.get(FTP_ULR));

			winSCPHandler.writeScriptToDisk(script);
			ioHelper.winscp(Metadata.PUT_FILE_NAME);
		} catch (Exception e) {
			ExceptionUtils.throwAsRuntimeException(e);

		}
	}
	
	public String prop(String property) {
		return propertyHelper.get(property);
	}
	
	protected void sleep(long time)  {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			ExceptionUtils.throwAsRuntimeException(e);
		}
	}
	
	public String userDir(Metadata data) {
		return propertyHelper.getUserDir(data);
	}

}
