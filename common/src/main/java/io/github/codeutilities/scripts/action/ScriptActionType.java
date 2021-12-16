package io.github.codeutilities.scripts.action;

import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.CommandHandler;
import io.github.codeutilities.event.Event;
import io.github.codeutilities.event.impl.ReloadCommandsEvent;
import io.github.codeutilities.event.type.Cancellable;
import io.github.codeutilities.scripts.ScriptContext;
import io.github.codeutilities.scripts.ScriptHandler;
import io.github.codeutilities.scripts.event.ScriptEventType;
import io.github.codeutilities.scripts.types.ScriptList;
import io.github.codeutilities.scripts.types.ScriptNumber;
import io.github.codeutilities.scripts.types.ScriptText;
import io.github.codeutilities.util.ComponentUtil;
import io.github.codeutilities.util.TriConsumer;
import java.util.ArrayList;
import net.minecraft.network.chat.TextComponent;

public enum ScriptActionType {

    PRINT(ScriptActionCategory.PLAYER, "Print", "Txts", "Prints the given message to the chat without colorcodes applied.", false, (args, inner, event) -> {
        StringBuilder sb = new StringBuilder();

        for (ScriptActionArgument arg : args) {
            sb.append(arg.get().text());
        }

        CodeUtilities.MC.player.displayClientMessage(new TextComponent(sb.toString()), false);
    }),

    PRINT_FORMATTED(ScriptActionCategory.PLAYER, "PrintFormatted", "Txts", "Prints the given message to the chat with colorcodes applied.", false, (args, inner, event) -> {
        StringBuilder sb = new StringBuilder();

        for (ScriptActionArgument arg : args) {
            sb.append(arg.get().text());
        }
        CodeUtilities.MC.player.displayClientMessage(ComponentUtil.fromString(sb.toString().replaceAll("&", "§")), false);
    }),

    ACTIONBAR(ScriptActionCategory.PLAYER, "ActionBar", "Txts", "Sends the given message to the player's action bar with colorcodes applied.", false, (args, inner, event) -> {
       StringBuilder sb = new StringBuilder();

        for (ScriptActionArgument arg : args) {
            sb.append(arg.get().text());
        }

        CodeUtilities.MC.player.displayClientMessage(ComponentUtil.fromString(sb.toString().replaceAll("&", "§")), true);
    }),

    INCREASE(ScriptActionCategory.VAR, "Increase", "Var, Nums", "Increases the value of the given variable by the given number(s).", false, (args, inner, event) -> {
        double result = 0;
        for (ScriptActionArgument arg : args) {
            result += arg.get().number();
        }
        args[0].set(new ScriptNumber(result));
    }),

    DECREASE(ScriptActionCategory.VAR, "Decrease", "Var, Nums", "Decreases the value of the given variable by the given number(s).", false, (args, inner, event) -> {
        double result = args[0].get().number();
        for (int i = 1; i < args.length; i++) {
            result += args[i].get().number();
        }
        args[0].set(new ScriptNumber(result));
    }),

    SET(ScriptActionCategory.VAR, "Set", "Var, Val", "Sets one variable to a given value.", false,
        (args, inner, event) -> args[0].set(args[1].get())
    ),

    ADD(ScriptActionCategory.VAR, "Add", "Var, Nums", "Sets the variable to the sum of the numbers.", false, (args, inner, event) -> {
        double result = 0;
        for (int i = 1; i < args.length; i++) {
            result += args[i].get().number();
        }
        args[0].set(new ScriptNumber(result));
    }),

    SUBTRACT(ScriptActionCategory.VAR, "Subtract", "Var, Num, Num", "Sets the variable to the difference of the two numbers.", false,
        (args, inner, event) -> args[0].set(new ScriptNumber(args[1].get().number() - args[2].get().number()))
    ),

    MULTIPLY(ScriptActionCategory.VAR, "Multiply", "Var, Nums", "Sets the variable to the product of the numbers.", false, (args, inner, event) -> {
        double result = 1;
        for (int i = 1; i < args.length; i++) {
            result *= args[i].get().number();
        }
        args[0].set(new ScriptNumber(result));
    }),

    DIVIDE(ScriptActionCategory.VAR, "Divide", "Var, Num, Num", "Sets the variable to the quotient of the two numbers.", false,
        (args, inner, event) -> args[0].set(new ScriptNumber(args[1].get().number() / args[2].get().number()))
    ),

    MODULO(ScriptActionCategory.VAR, "Modulo", "Var, Num, Num", "Sets the variable to the remainder of the two numbers.", false,
        (args, inner, event) -> args[0].set(new ScriptNumber(args[1].get().number() % args[2].get().number()))
    ),

    RANDOM(ScriptActionCategory.VAR, "Random", "Var, Min, Max", "Sets the variable to a random decimal number between the two numbers.", false,
        (args, inner, event) -> args[0].set(new ScriptNumber(Math.random() * (args[2].get().number() - args[1].get().number()) + args[1].get().number()))
    ),

    RANDOM_INT(ScriptActionCategory.VAR, "RandomInt", "Var, Min, Max", "Sets the variable to a random non decimal number between the two numbers.", false,
        (args, inner, event) -> args[0].set(new ScriptNumber(Math.floor(Math.random() * (args[2].get().number() - args[1].get().number()) + args[1].get().number())))
    ),

    TIMESTAMP(ScriptActionCategory.VAR, "Timestamp", "Var", "Sets the variable to the current timestamp in seconds with very high accuracy.", false,
        (args, inner, event) -> args[0].set(new ScriptNumber(System.currentTimeMillis() / 1000.0))
    ),

    CONCAT(ScriptActionCategory.VAR, "Concat", "Var, Vals", "Sets the variable to the concatenation of the strings.", false,(args, inner, event) -> {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            sb.append(args[i].get().text());
        }
        args[0].set(new ScriptText(sb.toString()));
    }),

    ROUND(ScriptActionCategory.VAR, "Round", "Var, Num", "Sets the variable to the number rounded to the nearest integer.", false,
        (args, inner, event) -> args[0].set(new ScriptNumber(Math.round(args[1].get().number())))
    ),

    FLOOR(ScriptActionCategory.VAR, "Floor", "Var, Num", "Sets the variable to the number rounded down to the nearest integer.", false,
        (args, inner, event) -> args[0].set(new ScriptNumber(Math.floor(args[1].get().number())))
    ),

    CEILING(ScriptActionCategory.VAR, "Ceiling", "Var, Num", "Sets the variable to the number rounded up to the nearest integer.", false,
        (args, inner, event) -> args[0].set(new ScriptNumber(Math.ceil(args[1].get().number())))
    ),

    ABSOLUTE(ScriptActionCategory.VAR, "Absolute", "Var, Num", "Sets the variable to the absolute value of the number.", false,
        (args, inner, event) -> args[0].set(new ScriptNumber(Math.abs(args[1].get().number())))
    ),

    REMOVE_FIRST(ScriptActionCategory.VAR, "RemoveFirst", "Var, Text, Num", "Sets the variable to the text with the first num characters removed.", false,
        (args, inner, event) -> args[0].set(new ScriptText(args[1].get().text().substring((int) args[2].get().number())))
    ),

    REMOVE_LAST(ScriptActionCategory.VAR, "RemoveLast", "Var, Text, Num", "Sets the variable to the text with the last num characters removed.", false,
        (args, inner, event) -> args[0].set(new ScriptText(args[1].get().text().substring(0, args[1].get().text().length() - (int) args[2].get().number())))
    ),

    GREATER(ScriptActionCategory.IF, "Greater", "Num, Num", "True if the first variable is greater than the second.", true, (args, inner, event) -> {
        if (args[0].get().number() > args[1].get().number()) {
            inner.run();
        }
    }),

    LESS(ScriptActionCategory.IF, "Less", "Num, Num", "True if the first variable is less than the second.", true, (args, inner, event) -> {
        if (args[0].get().number() < args[1].get().number()) {
            inner.run();
        }
    }),

    GREATER_EQUAL(ScriptActionCategory.IF, "GreaterEqual", "Num, Num", "True if the first variable is greater than or equal to the second.", true, (args, inner, event) -> {
        if (args[0].get().number() >= args[1].get().number()) {
            inner.run();
        }
    }),

    LESS_EQUAL(ScriptActionCategory.IF, "LessEqual", "Num, Num", "True if the first variable is less than or equal to the second.", true, (args, inner, event) -> {
        if (args[0].get().number() <= args[1].get().number()) {
            inner.run();
        }
    }),

    EQUAL(ScriptActionCategory.IF, "Equal", "Val, Val", "True if the first variable is equal to the second.", true, (args, inner, event) -> {
        if (args[0].get().equals(args[1].get())) {
            inner.run();
        }
    }),

    NOT_EQUAL(ScriptActionCategory.IF, "NotEqual", "Num, Num", "True if the first variable is not equal to the second.", true, (args, inner, event) -> {
        if (args[0].get().number() != args[1].get().number()) {
            inner.run();
        }
    }),

    TIMES(ScriptActionCategory.REPEAT, "Times", "Num", "Repeats Num times.", true, (args, inner, event) -> {
        double num = args[0].get().number();
        for (int i = 0; i < num; i++) {
            inner.run();
        }
    }),

    SUDO(ScriptActionCategory.PLAYER, "Sudo", "Txt", "Makes the player type in chat.", false,
        (args, inner, event) -> CodeUtilities.MC.player.chat(args[0].get().text())
    ),

    IF_REGEX(ScriptActionCategory.IF, "Regex", "Txt, Txt", "True if the text matches the regex.", true, (args, inner, event) -> {
        if (args[0].get().text().matches(args[1].get().text())) {
            inner.run();
        }
    }),

    IF_NOT_REGEX(ScriptActionCategory.IF, "NotRegex", "Txt, Txt", "True if the text does not match the regex.", true, (args, inner, event) -> {
        if (!args[0].get().text().matches(args[1].get().text())) {
            inner.run();
        }
    }),

    IF_CONTAINS(ScriptActionCategory.IF, "Contains", "Txt, Txt", "True if the first text contains the second text.", true, (args, inner, event) -> {
        if (args[0].get().text().contains(args[1].get().text())) {
            inner.run();
        }
    }),

    IF_NOT_CONTAINS(ScriptActionCategory.IF, "NotContains", "Txt, Txt", "True if the first text does not contain the second text.", true, (args, inner, event) -> {
        if (!args[0].get().text().contains(args[1].get().text())) {
            inner.run();
        }
    }),

    IF_STARTS_WITH(ScriptActionCategory.IF, "StartsWith", "Txt, Txt", "True if the first text starts with the second text.", true, (args, inner, event) -> {
        if (args[0].get().text().startsWith(args[1].get().text())) {
            inner.run();
        }
    }),

    IF_NOT_STARTS_WITH(ScriptActionCategory.IF, "NotStartsWith", "Txt, Txt", "True if the first text does not start with the second text.", true, (args, inner, event) -> {
        if (!args[0].get().text().startsWith(args[1].get().text())) {
            inner.run();
        }
    }),

    IF_ENDS_WITH(ScriptActionCategory.IF, "EndsWith", "Txt, Txt", "True if the first text ends with the second text.", true, (args, inner, event) -> {
        if (args[0].get().text().endsWith(args[1].get().text())) {
            inner.run();
        }
    }),

    IF_NOT_ENDS_WITH(ScriptActionCategory.IF, "NotEndsWith", "Txt, Txt", "True if the first text does not end with the second text.", true, (args, inner, event) -> {
        if (!args[0].get().text().endsWith(args[1].get().text())) {
            inner.run();
        }
    }),
    
    IF_GUI_OPEN(ScriptActionCategory.IF, "GuiOpen", "", "True if the player has an open GUI.", true, (args, inner, event) -> {
        if (CodeUtilities.MC.screen != null) {
            inner.run();
        }
    }),
    
    IF_NOT_GUI_OPEN(ScriptActionCategory.IF, "NotGuiOpen", "", "True if the player does not have an open GUI.", true, (args, inner, event) -> {
        if (CodeUtilities.MC.screen == null) {
            inner.run();
        }
    }),

    CANCEL_EVENT(ScriptActionCategory.CONTROL, "Cancel", "", "Cancels the execution of the event if possible.", false, (args, inner, event) -> {
        if (event instanceof Cancellable c) {
            c.setCancelled(true);
        }
    }),

    REGISTER_COMMAND(ScriptActionCategory.CONTROL, "RegisterCommand", "Txt", "Registers a command with a specific name. Only works on Event#RegisterCmds!", false, (args, inner, event) -> {
        if (event instanceof ReloadCommandsEvent) {
            ScriptHandler.getScriptCmdDispatcher().register(CommandHandler.literal(args[0].get().text())
                .executes(ctx -> {
                    ScriptContext sCtx = new ScriptContext();
                    sCtx.setVar("command",new ScriptText(ctx.getInput().substring(1)));
                    ScriptHandler.triggerEvent(ScriptEventType.SEND_COMMAND,sCtx,null);
                    return 1;
                })
                .then(CommandHandler.argument("args", StringArgumentType.greedyString())
                    .executes(ctx -> {
                        ScriptContext sCtx = new ScriptContext();
                        sCtx.setVar("command",new ScriptText(ctx.getInput().substring(1)));
                        ScriptHandler.triggerEvent(ScriptEventType.SEND_COMMAND,sCtx,null);
                        return 1;
                    })
                )
            );
        }
    }),

    CREATE_LIST(ScriptActionCategory.VAR, "CreateList", "Var", "Creates a new list.", false,
        (args, inner, event) -> args[0].set(new ScriptList(new ArrayList<>()))
    ),

    APPEND_VALUE(ScriptActionCategory.VAR, "AppendValue", "List, Val", "Appends the value to the list.", false,
        (args, inner, event) -> args[0].get().list().add(args[1].get())
    ),

    GET_VALUE(ScriptActionCategory.VAR, "GetValue", "Var, List, Num", "Gets the value at the specified index from the list and stores it in the variable.", false,
        (args, inner, event) -> args[0].set(args[1].get().list().get((int) args[2].get().number()))
    ),

    LENGTH(ScriptActionCategory.VAR, "Length", "Var, List", "Gets the length of the list and stores it in the variable.", false,
        (args, inner, event) -> args[0].set(new ScriptNumber(args[1].get().list().size()))
    ),

    REMOVE_VALUE(ScriptActionCategory.VAR, "RemoveValue", "List, Num", "Removes the value at the specified index from the list.", false,
        (args, inner, event) -> args[0].get().list().remove((int) args[1].get().number()-1)
    ),

    FOR_EACH(ScriptActionCategory.REPEAT, "ForEach", "Var, Var, List", "Iterates through the list and stores the value in the first and the index in the second variable.", true, (args, inner, event) -> {
        for (int i = 0; i < args[2].get().list().size(); i++) {
            args[0].set(args[2].get().list().get(i));
            args[1].set(new ScriptNumber(i+1));
            inner.run();
        }
    });

    private final String name, args, desc;
    private final boolean curly;
    private final TriConsumer<ScriptActionArgument[], Runnable, Event> code;
    private final ScriptActionCategory cat;

    ScriptActionType(ScriptActionCategory cat, String name, String args, String desc, boolean curly, TriConsumer<ScriptActionArgument[], Runnable, Event> code) {
        this.cat = cat;
        this.name = name;
        this.args = args;
        this.desc = desc;
        this.curly = curly;
        this.code = code;
    }

    public static ScriptActionType get(ScriptActionCategory cat, String name) {
        for (ScriptActionType type : values()) {
            if (type.cat == cat && type.name.equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return desc;
    }

    public ScriptActionCategory getCategory() {
        return cat;
    }

    public String getArguments() {
        return args;
    }

    public boolean isCurly() {
        return curly;
    }

    public TriConsumer<ScriptActionArgument[], Runnable, Event> getCode() {
        return code;
    }
}
