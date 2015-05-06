package se.bm.server.config;

import javax.enterprise.inject.Specializes;

import se.bm.core.Constants;
import se.bm.core.ConfigResolver;

@Specializes
public class CoreConfigMock extends CoreConfig {

	@Override
	public String getConf() {
		return "test/" + super.getConf();
	};
	
	@Override
	public String getHome() {
		return super.getHome() +"test" + Constants.SLASH ;
	};
	
	@Override
	public String getServerConfigPath() {
		return "test/" + super.getServerConfigPath();
	};

}
