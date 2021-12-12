package io.github.codeutilities.util;

import io.github.codeutilities.CodeUtilities;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {

    public static Path cuFolder() {
        return CodeUtilities.MC.gameDirectory.toPath().resolve("CodeUtilities");
    }

    public static Path cuFolder(String path) {
        return cuFolder().resolve(path);
    }

    public static String readFile(File f) throws IOException {
        return String.join("\n", Files.readAllLines(f.toPath()));
    }

    public static void writeFile(File f, String text) throws IOException {
        Files.writeString(f.toPath(), text);
    }
}
