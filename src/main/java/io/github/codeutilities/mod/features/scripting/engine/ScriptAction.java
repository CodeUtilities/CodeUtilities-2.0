package io.github.codeutilities.mod.features.scripting.engine;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.sys.player.chat.ChatUtil;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public enum ScriptAction implements ScriptPart {

    RUN_COMMAND(Category.PLAYER,"RunCmd","Text(s) - Cmd to run", (args, context) -> {
        CodeUtilities.MC.player.sendChatMessage("/"+args.fullString());
    }),
    SEND_CHAT(Category.PLAYER,"SendChat", "Text(s) - Msg to send",(args, context) -> {
        CodeUtilities.MC.player.sendChatMessage(args.fullString());
    }),
    PRINT_CHAT(Category.CHAT,"Print","Text(s) - Text to print", (args,context) -> {
        ChatUtil.sendMessage(args.fullString());
    }),
    IF_EQUALS(Category.IF,"Equals","Value,Value - Values to compare",(args,context) -> {
        return Objects.equals(args.get(0),args.get(1));
    }),
    IF_REGEX(Category.IF, "Regex", "Text, Regex - Checks if 1 matches 2",(args,context) -> {
        return args.getString(0).matches(args.getString(1));
    }),
    CLOSE_BRACKET;

    public Category category;
    public String name;
    public String description;
    BiFunction<ScriptArguments,ScriptContext,Boolean> executor;
    ScriptAction(Category category, String name,String description, BiFunction<ScriptArguments,ScriptContext,Boolean> executor) {
        this.category = category;
        this.name = name;
        this.executor = executor;
        this.description = description;
    }
    ScriptAction(Category category, String name,String description, BiConsumer<ScriptArguments,ScriptContext> executor) {
        this.category = category;
        this.name = name;
        this.executor = (a,b)->{
            executor.accept(a,b);
            return false;
        };
        this.description = description;
    }

    ScriptAction() {
        this.category = Category.SPECIAL;
    }

    public enum Category {
        PLAYER("Player"),CHAT("Chat"),IF("If"),SPECIAL("Special");

        public String label;
        Category(String label) {
            this.label = label;
        }
    }
}
