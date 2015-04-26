package se.smokestack.bm;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.resourceloader.FileResourceProvider;
import org.apache.deltaspike.core.api.resourceloader.InjectableResource;
import org.apache.deltaspike.core.util.ExceptionUtils;

import com.google.common.base.Splitter;

@ApplicationScoped
public class PropertyHelper {

	private static final String PROPERTIES_FILE_NAME = "bm.properties";

	@Inject
	@InjectableResource(resourceProvider = FileResourceProvider.class, location = PROPERTIES_FILE_NAME)
	private Properties properties;

	private Set<String> users = new HashSet<>();

	private Set<String> userDirs = new HashSet<>();

	public String get(String key) {
		return properties.getProperty(key);
	}

	public Set<String> getUsers() {
		return users;
	}
	
	public Set<String> getUserDirs() {
		return userDirs;
	}

	@PostConstruct
	private void init() {
		users.addAll(split(properties.getProperty(PropertyKeys.USERS)));
		
		for (String user : users) {
			String userDir = get(PropertyKeys.LOCAL_TEMP_DIR) + get(PropertyKeys.FTP_BIRDMAN_DIR) + "\\" +user + "\\";
			userDirs.add(userDir);
		}
		
	
		
	}

	public List<String> split(String token) {
		return Splitter.on(',').trimResults().omitEmptyStrings().splitToList(token);
	}

	private FileOutputStream getPropertiesAsOutputStream() throws FileNotFoundException {
		return new FileOutputStream(PROPERTIES_FILE_NAME);
	}
	
	public void saveReadTimeStamp() {

		try (OutputStream output = getPropertiesAsOutputStream()) {

			properties.setProperty(PropertyKeys.META_DATA_READ_TIME_STAMP, String.valueOf(System.currentTimeMillis()));
			properties.store(output, null);

		} catch (Exception e) {
			ExceptionUtils.throwAsRuntimeException(e);
		}

	}

	public String getUserDir(Metadata data) {
	return get(PropertyKeys.LOCAL_TEMP_DIR) + get(PropertyKeys.FTP_BIRDMAN_DIR) + "\\" +data.getUser() + "\\";
		
	}

}
