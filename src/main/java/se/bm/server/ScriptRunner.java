package se.bm.server;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import se.bm.server.ftp.Script;
import se.bm.server.ftp.ScriptKey;
import se.smokestack.bm.IOHelper;

@ApplicationScoped
public class ScriptRunner {
	
	@Inject
	private ServerConfig serverConfig;
	
	@Inject
	IOHelper ioHelper;
	
	private static final String PUT = "put ";
	private static final String GET = "get ";
	private static final String OPERATION = "#{operation}";
	public static final String TARGET = "#{target}";
	public static final String CD = "#{cd}";
	public static final String FTPURL = "#{ftpURL}";
	
	
	String SCRIPT_BASE =
	"option batch abort"+ System.lineSeparator()+
	"option confirm off"+System.lineSeparator()+
	"open " + FTPURL+System.lineSeparator()+
	"cd " + CD+System.lineSeparator()+
	"option transfer binary"+System.lineSeparator()+
	OPERATION+System.lineSeparator()+
	"close"+System.lineSeparator();
	
	
	
	public Script put(ScriptKey key, String cd, String target) {
		String put = SCRIPT_BASE;
		put = put.replace(OPERATION, PUT + target).replace(CD, cd);
		Script script = new Script(key, put);
		ioHelper.winscp(script);
		return script;
	}
	
	public Script fetch(ScriptKey key, String cd,  String target, String destination) {
		String get = SCRIPT_BASE;
		get = get.replace(OPERATION, GET + target + " " + destination).replace(CD, cd);
		Script script = new Script(key, get);
		ioHelper.winscp(script);
		return script;
	}
	
	
	
	@PostConstruct
	private void init() {
		SCRIPT_BASE = SCRIPT_BASE.replace(FTPURL, serverConfig.getFtpURL());
	}
	
}
