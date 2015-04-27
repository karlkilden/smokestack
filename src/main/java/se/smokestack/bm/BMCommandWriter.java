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
	private IOHelper ioHelper;

	void markHandled(BMCommand data) {
		data.setHandled(true);
		writeMetaData(data);
	}

	void markInError(BMCommand data) {
		data.setInError(true);
		writeMetaData(data);
	}

	public void writeClientCommand(BMCommand bmCommand) {

		doWrite(bmCommand, bmConfig.getLocalTempDir() + "/" + bmCommand.getSystem()+ "/" + BMCommand.BM_COMMAND);

	}

	private void writeMetaData(BMCommand bmCommand) {
		doWrite(bmCommand, bmConfig.getUserCommandPath());
		WinSCPScript script = WinSCPScript.getWriteInstance(BMCommand.PUT_FILE_NAME, bmConfig).put().target(bmConfig.getUserCommandPath());
		ioHelper.winscp(script);
	}

	private void doWrite(BMCommand data, String path) {
		try {

			MAPPER.writeValue(new File(path), data);
		} catch (Exception e) {
			ExceptionUtils.throwAsRuntimeException(e);
		}
	}
}
