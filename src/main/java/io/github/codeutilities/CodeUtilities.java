package io.github.codeutilities;

import com.google.gson.JsonParser;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CodeUtilities implements ModInitializer {

    public static final String MOD_ID = "codeutilities";
    public static final String MOD_NAME = "CodeUtilities";
//    public static final String MOD_VERSION = "2.2.2";
//    public static final boolean BETA = true;

    public static final Logger LOGGER = LogManager.getLogger();
//    public static final Random RANDOM = new Random();
//    public static final Gson GSON = new GsonBuilder()
//        .registerTypeAdapter(ConfigInstruction.class, new ConfigSerializer())
//        .registerTypeAdapter(BooleanSetting.class, new BooleanSerializer())
//        .registerTypeAdapter(IntegerSetting.class, new IntegerSerializer())
//        .registerTypeAdapter(DoubleSetting.class, new DoubleSerializer())
//        .registerTypeAdapter(FloatSetting.class, new FloatSerializer())
//        .registerTypeAdapter(LongSetting.class, new LongSerializer())
//        .registerTypeAdapter(StringSetting.class, new StringSerializer())
//        .registerTypeAdapter(StringListSetting.class, new StringListSerializer())
//        .setPrettyPrinting()
//        .create();
    public static final JsonParser JSON_PARSER = new JsonParser();
    public static final MinecraftClient MC = MinecraftClient.getInstance();
//    public static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
//    private static final Path optionsTxtPath = FabricLoader.getInstance().getGameDir().resolve("options.txt");
    public static String OPTIONSTXT = "";
//    public static String CLIENT_LANG = "unknown";

    static {
//        try {
//            OPTIONSTXT = FileUtil.readFile(optionsTxtPath.toString(), Charset.defaultCharset());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static void log(Level level, String message) {
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }

    @Override
    public void onInitialize() {
        log(Level.INFO, "Initializing");
        Runtime.getRuntime().addShutdownHook(new Thread(this::onClose));
//        System.setProperty("java.awt.headless", "false");

        // Get lang
//        Pattern regex = Pattern.compile("\nlang:.*");
//        Matcher m = regex.matcher(OPTIONSTXT);
//        while (m.find()) {
//            CLIENT_LANG = m.group(0).replaceAll("^\nlang:", "");
//        }

        // Load modules
//        Action.cacheActions();
//        Trigger.cacheTriggers();
//        Module.loadModules();

        // Initialize.
//        CodeInitializer initializer = new CodeInitializer();
//        initializer.add(new ConfigFile());
//        initializer.add(new ConfigManager());
//        initializer.add(new TemplateStorageHandler());
//        initializer.add(new CustomHeadMenu());
//        initializer.add(new DFDiscordRPC());
//        initializer.add(new Client());
//        initializer.add(new ActionDump());
//        initializer.add(new EventHandler());
//        initializer.add(new State.Locater());
//        initializer.add(new CommandHandler());

        // Initializes only if the given condition is met. (this case: config value)
        // initializer.addIf(new AudioHandler(), Config.getBoolean("audio"));
//        initializer.addIf(new SocketHandler(), Config.getBoolean("itemApi"));
//        MC.send(CosmeticHandler.INSTANCE::load);

//        ClientTickEvents.START_CLIENT_TICK
//            .register(client -> OtherEvents.TICK.invoker().tick(client));

        log(Level.INFO, "Initialized successfully!");
    }

    public void onClose() {
        System.out.println("CLOSED");
//        ConfigFile.getInstance().save();
//        TemplateStorageHandler.getInstance().save();
//        CosmeticHandler.INSTANCE.shutdownExecutorService();
    }

}