package se.smokestack.boot;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

public class CopyDir {
	File src = new File("c:/temp/");
	File dest = new File("c:/temp2/");
	Path srcPath = src.toPath();
	Path destPath = dest.toPath();

	public CopyDir() throws IOException {

		Files.walkFileTree(srcPath, new CopyDirVisitor(srcPath, destPath,
				StandardCopyOption.REPLACE_EXISTING));
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
		public FileVisitResult preVisitDirectory(Path dir,
				BasicFileAttributes attrs) throws IOException {
			Path targetPath = toPath.resolve(fromPath.relativize(dir));
			if (!Files.exists(targetPath)) {
				Files.createDirectory(targetPath);
			}
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
				throws IOException {
			Files.copy(file, toPath.resolve(fromPath.relativize(file)),
					copyOption);
			return FileVisitResult.CONTINUE;
		}
	}
}