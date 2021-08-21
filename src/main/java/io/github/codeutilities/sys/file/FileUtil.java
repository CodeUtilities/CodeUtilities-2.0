package io.github.codeutilities.sys.file;

import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
    private static final long  MEGABYTE = 1024L * 1024L;

    public static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static void createFolder(String fileFolder) {

        Path pathOne = FabricLoader.getInstance().getGameDir().resolve("CodeUtilities/" + fileFolder);
        java.io.File fileOne = pathOne.toFile();

        if (!fileOne.exists()) {
            fileOne.mkdir();
        }

    }

    public static void cleanFolder(String fileFolder) {

        Path pathOne = FabricLoader.getInstance().getGameDir().resolve("CodeUtilities/" + fileFolder);
        File fileOne = pathOne.toFile();

        File[] moduleFiles = fileOne.listFiles();

        if (moduleFiles != null) {

            for (File fileTwo : moduleFiles) {

                fileTwo.delete();

            }
        }

    }

    public static long bytesToMeg(long bytes) {
        return bytes / MEGABYTE ;
    }

    public static long getFolderSize(File dir) {
        long size = 0;
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                size += file.length();
            }
            else
                size += getFolderSize(file);
        }
        return size;
    }
}
