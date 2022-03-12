package io.github.codeutilities.sys.file;

import io.github.codeutilities.CodeUtilities;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
    public static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }


    public static String readFirstLine(Path path) {
        File file = path.toFile();

        try {
            if(!file.exists()) {
                boolean result = file.createNewFile();
                CodeUtilities.log(Level.INFO, path + " does not exist. creating it.");
                if(!result)
                    CodeUtilities.log(Level.INFO, "failed creating " + path);

            }

            String contents = Files.readString(path);

            return contents == null ? "" : contents;
        } catch(IOException exception) {
            CodeUtilities.log(Level.WARN, "Failed to read " + path);
            exception.printStackTrace();
            return "";
        }
    }

    public static void writeStringToFile(Path path, String content) {
        try {
            Files.writeString(path, content);
        } catch (IOException e) {
            CodeUtilities.log(Level.WARN, "Failed writing to " + path);
            e.printStackTrace();
        }
    }

}
