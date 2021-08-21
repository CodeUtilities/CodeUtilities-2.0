package io.github.codeutilities.sys.aweslib;

import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.nio.file.Path;

public class AWEFile {

    public static void createFolder(String fileFolder) {

        Path pathOne = FabricLoader.getInstance().getGameDir().resolve("CodeUtilities/" + fileFolder);
        java.io.File fileOne = pathOne.toFile();

        if (!fileOne.exists()) {
            fileOne.mkdir();
        }

    }

    public static void cleanFolder(String fileFolder) {

        Path pathOne = FabricLoader.getInstance().getGameDir().resolve("CodeUtilities/" + fileFolder);
        java.io.File fileOne = pathOne.toFile();

        java.io.File[] moduleFiles = fileOne.listFiles();

        if (moduleFiles != null) {

            for (java.io.File fileTwo : moduleFiles) {

                fileTwo.delete();

            }
        }

    }

    private static final long  MEGABYTE = 1024L * 1024L;

    public static long bytesToMeg(long bytes) {
        return bytes / MEGABYTE ;
    }

    public static long getFolderSize(File dir) {
        long size = 0;
        for (java.io.File file : dir.listFiles()) {
            if (file.isFile()) {
                System.out.println(file.getName() + " " + file.length());
                size += file.length();
            }
            else
                size += getFolderSize(file);
        }
        return size;
    }

}
