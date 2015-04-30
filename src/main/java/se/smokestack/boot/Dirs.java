package se.smokestack.boot;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.Objects;

import org.apache.deltaspike.core.util.ExceptionUtils;

public class Dirs {

	public static void copy(Path from, Path to) throws IOException {
		validate(from);
		Files.walkFileTree(from, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, new CopyDirVisitor(from, to,
				StandardCopyOption.REPLACE_EXISTING));
	}

	private static void validate(Path... paths) {
		for (Path path : paths) {
			Objects.requireNonNull(path);
			if (!Files.isDirectory(path)) {
				throw new IllegalArgumentException(String.format("%s is not a directory", path.toString()));
			}
		}
	}

	public static void cleanIfExists(Path path) {
		try {
			if (Files.exists(path)) {
				validate(path);
				Files.walkFileTree(path, new CleanDirVisitor());
			}
		} catch (Exception e) {
			ExceptionUtils.throwAsRuntimeException(e);
		}
	}

	public static class CopyDirVisitor extends SimpleFileVisitor<Path> {
		private final Path fromPath;
		private final Path toPath;
		private final CopyOption copyOption;

		public CopyDirVisitor(Path fromPath, Path toPath, CopyOption copyOption) {
			this.fromPath = fromPath;
			this.toPath = toPath;
			this.copyOption = copyOption;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			Path targetPath = toPath.resolve(fromPath.relativize(dir));
			if (!Files.exists(targetPath)) {
				Files.createDirectory(targetPath);
			}
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			Files.copy(file, toPath.resolve(fromPath.relativize(file)), copyOption);
			return FileVisitResult.CONTINUE;
		}
	}

	public static void delete(Path path) throws IOException {
		validate(path);
		Files.walkFileTree(path, new DeleteDirVisitor());
	}
}