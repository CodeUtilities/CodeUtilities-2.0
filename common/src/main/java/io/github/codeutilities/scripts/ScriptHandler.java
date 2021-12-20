package io.github.codeutilities.scripts;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.BuiltInExceptionProvider;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.event.Event;
import io.github.codeutilities.event.EventHandler;
import io.github.codeutilities.event.impl.ReloadCommandsEvent;
import io.github.codeutilities.scripts.event.ScriptEventType;
import io.github.codeutilities.util.FileUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundCommandsPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ScriptHandler {

    public static final Logger LOGGER = LogManager.getLogger("CuScripts");
    public static List<Script> scripts = new ArrayList<>();
    private static CommandDispatcher<SharedSuggestionProvider> vanillaDispatcher = null;
    private static CommandDispatcher<SharedSuggestionProvider> scriptCmdDispatcher = null;

    public static void init() {
        EventHandler.register(ReloadCommandsEvent.class, (event) -> {
            vanillaDispatcher = event.vanillaDispatcher();
            scriptCmdDispatcher = new CommandDispatcher<>();
            triggerEvent(ScriptEventType.REGISTER_CMDS, event);
            for (CommandNode<SharedSuggestionProvider> node : scriptCmdDispatcher.getRoot().getChildren()) {
                event.modifiableDispatcher().getRoot().addChild(node);
            }
        });
    }

    public static void init() {

        triggerEvent(ScriptEventType.FINALIZE,null);

        scripts.clear();
        LOGGER.info("Loading scripts...");
        File folder = FileUtil.cuFolder("Scripts").toFile();
        if (!folder.exists()) {
            folder.mkdirs();
        }
        for (File file : folder.listFiles()) {
            if (file.isFile()) {
                String name = file.getName();
                if (name.endsWith(".cus")) {
                    scripts.add(new Script(name, file));
                } else {
                    LOGGER.warn("Ignored file: " + file.getName() + ", Does not end with .cus (CodeUtilitiesScript)");
                }
            }
        }
        LOGGER.info("Loaded scripts!");
        triggerEvent(ScriptEventType.INIT, null);

        if (vanillaDispatcher != null) {
            //reload commands
            CodeUtilities.MC.getConnection().handleCommands(new ClientboundCommandsPacket(vanillaDispatcher.getRoot()));
        }
    }

    public static void  triggerEvent(ScriptEventType scriptEvent, Event cuEvent) {
        triggerEvent(scriptEvent, new ScriptContext(), cuEvent);
    }

    public static void triggerEvent(ScriptEventType scriptEvent, ScriptContext ctx, Event cuEvent) {
        for (Script script : scripts) {
            script.triggerEvent(scriptEvent, ctx, cuEvent);
        }
    }

    public static boolean createScript(String name) {
        try {
            return FileUtil.cuFolder("Scripts").resolve(name + ".cus").toFile().createNewFile();
        } catch (Exception err) {
            err.printStackTrace();
            return false;
        }
    }

    public static boolean deleteScript(String name) {
        try {
            return FileUtil.cuFolder("Scripts").resolve(name).toFile().delete();
        } catch (Exception err) {
            err.printStackTrace();
            return false;
        }
    }

    public static CommandDispatcher<SharedSuggestionProvider> getScriptCmdDispatcher() {
        return scriptCmdDispatcher;
    }

    public static boolean dispatchCmd(StringReader reader) {
        //copied from CommandHandler::dispatch and made it use the scriptCmdDispatcher

        if (CodeUtilities.MC.getConnection() != null) {
            // get the command dispatcher
            try {
                // execute the command
                scriptCmdDispatcher.execute(reader, null);
                return true;
            } catch (CommandSyntaxException e) {
                // if the command doesn't exist an exception will be thrown
                BuiltInExceptionProvider builtInExceptions = CommandSyntaxException.BUILT_IN_EXCEPTIONS;

                if (e.getType() != builtInExceptions.dispatcherUnknownCommand() &&
                    e.getType() != builtInExceptions.dispatcherParseException()) {
                    CodeUtilities.MC.player.sendMessage(new TextComponent(e.getMessage())
                        .withStyle(style -> style.withColor(ChatFormatting.RED)), null);
                    return true;
                }
            }
        }
        return false;
    }
}
