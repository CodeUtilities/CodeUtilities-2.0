package io.github.codeutilities;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import io.github.codeutilities.commands.CommandHandler;
import io.github.codeutilities.config.internal.ConfigFile;
import io.github.codeutilities.config.structure.ConfigManager;
import io.github.codeutilities.scripts.ScriptHandler;
import io.github.codeutilities.scripts.event.ScriptEventListeners;
import io.github.codeutilities.util.FileUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.github.codeutilities.util.Platform;
import io.github.codeutilities.util.codeinit.CodeInitializer;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CodeUtilities {

    public static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
    public static final Minecraft MC = Minecraft.getInstance();
    public static final Gson GSON = new Gson();
    public static final Logger LOGGER = LogManager.getLogger();
    public static final JsonParser JSON_PARSER = new JsonParser();

    public static Platform platform = Platform.UNKNOWN;

    public static void init() {
        LOGGER.info("Initializing CodeUtilities for " + platform.getDisplayName() + "...");

        if (!FileUtil.cuFolder().toFile().exists()) {
            FileUtil.cuFolder().toFile().mkdirs();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(CodeUtilities::onClose));

        CodeInitializer initializer = new CodeInitializer();
        initializer.add(new ConfigFile());
        initializer.add(new ConfigManager());


        ScriptHandler.init();

        ScriptEventListeners.init();
        CommandHandler.init();

        LOGGER.info("CodeUtilities initialized!");
    }

    public static void onClose() {
        LOGGER.info("Closed");
        ConfigFile.getInstance().save();
    }
}
