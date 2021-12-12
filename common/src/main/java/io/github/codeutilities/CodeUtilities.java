package io.github.codeutilities;

import com.google.gson.Gson;
import io.github.codeutilities.scripts.ScriptHandler;
import io.github.codeutilities.util.FileUtil;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CodeUtilities {

    public static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
    public static final Minecraft MC = Minecraft.getInstance();
    public static final Gson GSON = new Gson();
    public static final Logger LOGGER = LogManager.getLogger();

    public static Platform platform = Platform.UNKNOWN;

    public static void init() {
        LOGGER.info("Initializing CodeUtilities for " + platform.displayName + "...");

        if (!FileUtil.cuFolder().toFile().exists()) {
            FileUtil.cuFolder().toFile().mkdirs();
        }
        ScriptHandler.load();

        LOGGER.info("CodeUtilities initialized!");
    }
}
