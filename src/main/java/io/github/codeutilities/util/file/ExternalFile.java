package io.github.codeutilities.util.file;

import java.io.File;

public enum ExternalFile {
    NBS_FILES(new ExternalFileBuilder()
            .isDirectory(true)
            .setName("NBS Files")
            .buildFile()),
    IMAGE_FILES(new ExternalFileBuilder()
            .isDirectory(true)
            .setName("Images")
            .buildFile()),
    TEMPLATE_DB(new ExternalFileBuilder()
            .isDirectory(false)
            .setName("Templates")
            .setFileType("nbt")
<<<<<<< HEAD
=======
            .buildFile()),
    PLOTS_DB(new ExternalFileBuilder()
            .isDirectory(false)
            .setName("Plots")
            .setFileType("nbt")
>>>>>>> 0bee843 (Initial commit)
            .buildFile());

    private final File file;

    ExternalFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }


}
