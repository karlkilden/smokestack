package se.smokestack.bm;

import static org.apache.deltaspike.core.util.ExceptionUtils.throwAsRuntimeException;

import java.io.File;
import java.nio.file.Paths;

import javax.inject.Inject;

import se.bm.core.UserOptions;
import se.smokestack.boot.Dirs;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BMCommandWriter {
	private static final ObjectMapper MAPPER = new ObjectMapper();
	@Inject
	private BMConfig bmConfig;

	@Inject
	private IOHelper ioHelper;

	private UserOptions options;

	public void markHandled(BMCommand data) {
		data.setHandled(true);
		writeMetaData(data);
	}

	public void markInError(BMCommand data) {
		data.setInError(true);
		writeMetaData(data);
	}

	public void writeClientCommand(BMCommand bmCommand) {

		doWrite(bmCommand, bmConfig.getLocalTempDir() + "/" + bmCommand.getSystem() + "/" + BMCommand.BM_COMMAND);

	}

	private void writeMetaData(BMCommand bmCommand) {
		doWrite(bmCommand, bmConfig.getUserCommandPath());
		WinSCPScript script = WinSCPScript.getWriteToBrokerInstance(BMCommand.PUT_FILE_NAME, bmConfig).put().target(bmConfig.getUserCommandPath());
		ioHelper.winscp(script);

	}

	private void doWrite(BMCommand data, String path) {
		try {

			MAPPER.writeValue(new File(path), data);
		} catch (Exception e) {
			throwAsRuntimeException(e);
		}
	}

	public void copyBrokerFilesToServerDir() {

		File f = new File(bmConfig.brokerDir());
		if (f.exists()) {
			WinSCPScript script = WinSCPScript.getInstance(BMCommand.FTP_PUT).put().target(bmConfig.brokerDir()).ftpDir(bmConfig.getFtpDir())
					.destination(bmConfig.getFtpBmDir()).ftpUrl(bmConfig.getFtpURL());
			ioHelper.winscp(script, false);

			if (!options.isNoClean()) {
				Dirs.cleanIfExists(Paths.get(bmConfig.brokerDir()));
			}
		}

	}
}
