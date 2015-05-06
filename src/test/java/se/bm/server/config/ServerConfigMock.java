package se.bm.server.config;

import javax.enterprise.inject.Specializes;

import se.bm.server.ServerConfig;

@Specializes
public class ServerConfigMock extends ServerConfig {

@Override
public String getWinscpDir() {
	return super.getWinscpDir();
}
	
}
