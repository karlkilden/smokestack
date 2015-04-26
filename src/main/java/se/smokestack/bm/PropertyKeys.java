package se.smokestack.bm;

import java.lang.reflect.Field;

public class PropertyKeys {
	public static final String WAR_FILE_NAME ="warfile.name";	
	public static final String FTP_ULR ="ftp.url";	
	public static final String FTP_DIR ="ftp.dir";	
	public static final String FTP_BIRDMAN_DIR ="ftp.birdman.dir";	
	public static final String TOMEE_SERVICE ="tomee.service";	
	public static final String TOMEE_HOST ="tomee.host";	
	public static final String TOMEE_PORT ="tomee.port";	
	public static final String TOMEE_HOME ="tomee.home";	
	public static final String TOMEE_SHUTDOWN_PORT = "tomee.shutdown.port";	
	public static final String TOMEE_SHUTDOWN_SLEEP = "tomee.shutdown.sleep";	
	public static final String LOCAL_TEMP_DIR ="local.temp.dir";	
	public static final String META_DATA_READ_TIME_STAMP ="last.read.meta";	
	public static final String WINSCP_DIR ="winscp.dir";
	public static final String USERS = "users";
	public static final String TOMEE_STOP_BEFORE_REPLACE_WAR = "tomee.stop.before.new.war";
	
	private PropertyKeys(){};
	
	public static void main(String[] args) throws Exception {
		printPropertyKeys();
	}

	private static void printPropertyKeys() throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = PropertyKeys.class.getDeclaredFields();

		for ( Field field : fields ) {
		   field.setAccessible(true);
		  System.out.println(field.get(new PropertyKeys()));

		}
	}
}
