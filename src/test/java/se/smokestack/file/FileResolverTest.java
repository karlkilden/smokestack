package se.smokestack.file;

import java.io.File;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;

import static org.junit.Assert.*;

public class FileResolverTest {
	@Rule
    public final StandardOutputStreamLog console = new StandardOutputStreamLog();
	
	private static final Logger log = LogManager.getLogger();


	@Test
	public void file_resolver_get_instance_should_return_an_instance() {
		assertNotNull(FileResolver.getInstance());
	}
	
	@Test
	public void file_resolver_get_instance_should_return_an_new_instance() {
		assertNotSame(FileResolver.getInstance(), FileResolver.getInstance());
	}
	
	@Test
	public void file_should_be_created_using_file_name_and_relative_path() {
		File file = FileResolver.getInstance().fromRelativePath("testfile.txt").build();
		assertNotNull(file);
	}
	
	@Test
	public void file_not_found_should_be_caught_and_logged_specifing_used_path() {
	}
	
	@Test
	public void test() {

		String test = "C:\\Users\\Kalle\\Desktop\\Docs\\asd.txt";
		Stream<String> file = FileResolver.getInstance().streamFromFile(test);
		file.forEach(s -> log.info(s));
	}
	
	
}
