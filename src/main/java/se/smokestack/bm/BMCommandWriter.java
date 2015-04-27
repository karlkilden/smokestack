package se.smokestack.bm;

import java.io.File;

import javax.inject.Inject;

import org.apache.deltaspike.core.util.ExceptionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BMCommandWriter {
	private static final ObjectMapper MAPPER = new ObjectMapper();
	@Inject
	private BMConfig bmConfig;

	@Inject
	private WinSCPHandler winSCPHandler;

	@Inject
	private IOHelper ioHelper;

	void markHandled(BMCommand data) {
		data.setHandled(true);
		writeMetaData(data);
	}

	void markInError(BMCommand data) {
		data.setInError(true);
		writeMetaData(data);
	}

	private void writeMetaData(BMCommand data) {
		try {

			MAPPER.writeValue(new File(bmConfig.getUserCommandPath()), data);
			WinSCPScript script = WinSCPScript.getWriteInstance(BMCommand.PUT_FILE_NAME, bmConfig).put().target(bmConfig.getUserCommandPath());
			ioHelper.winscp(script);
		} catch (Exception e) {
			ExceptionUtils.throwAsRuntimeException(e);
		}
	}
}
