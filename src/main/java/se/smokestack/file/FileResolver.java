package se.smokestack.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.deltaspike.core.util.ExceptionUtils;

public class FileResolver {

	public static FileResolver getInstance() {
		// TODO Auto-generated method stub
		return new FileResolver();
	}

	public FileResolver fromRelativePath(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public File build() {
		// TODO Auto-generated method stub
		return null;
	}

	public Stream<String> streamFromFile(String filePath) {
		Path path = Paths.get(filePath);
		try {
			return Files.lines(path);
		} catch (IOException e) {
			ExceptionUtils.throwAsRuntimeException(e);
		}
		return null;
	}

}
