package io.github.codeutilities.mod.features.scripting.engine;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.sys.player.chat.ChatUtil;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import net.minecraft.text.LiteralText;

public enum ScriptAction {

    RUN_COMMAND(Category.PLAYER,"RunCmd", (args, context) -> {
        CodeUtilities.MC.player.sendChatMessage("/"+args.fullString());
    }),
    SEND_CHAT(Category.PLAYER,"SendChat", (args, context) -> {
        CodeUtilities.MC.player.sendChatMessage(args.fullString());
    }),
    PRINT_CHAT(Category.CHAT,"Print", (args,context) -> {
        ChatUtil.sendMessage(args.fullString());
    }),
    IF_EQUALS(Category.IF,"Equals",(args,context) -> {
        return Objects.equals(args.get(0),args.get(1));
    }),
    IF_REGEX(Category.IF, "Regex", (args,context) -> {
        return args.getString(0).matches(args.getString(1));
    }),
    CLOSE_BRACKET;

    Category category;
    String name;
    BiFunction<ScriptArguments,ScriptContext,Boolean> executor;
    ScriptAction(Category category, String name, BiFunction<ScriptArguments,ScriptContext,Boolean> executor) {
        this.category = category;
        this.name = name;
        this.executor = executor;
    }
    ScriptAction(Category category, String name, BiConsumer<ScriptArguments,ScriptContext> executor) {
        this.category = category;
        this.name = name;
        this.executor = (a,b)->{
            executor.accept(a,b);
            return false;
        };
    }

    ScriptAction() {
        this.category = Category.SPECIAL;
    }

    public enum Category {
        PLAYER("Player"),CHAT("Chat"),IF("If"),SPECIAL("Special");

        String label;
        Category(String label) {
            this.label = label;
        }
    }
}
