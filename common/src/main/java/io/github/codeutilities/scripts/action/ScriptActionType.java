package io.github.codeutilities.scripts.action;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.event.Event;
import io.github.codeutilities.event.type.Cancellable;
import io.github.codeutilities.util.ComponentUtil;
import org.apache.logging.log4j.util.TriConsumer;

public enum ScriptActionType {

    PRINT(ScriptActionCategory.PLAYER, "Print", "Txt", "Prints the given message to the chat.", false,
        (args, inner, event) -> CodeUtilities.MC.player.displayClientMessage(ComponentUtil.fromString(args[0].getText()), false)
    ),

    INCREASE(ScriptActionCategory.VAR, "Increase", "Var, Num", "Increases the value of the given variable by a given number.", false,
        (args, inner, event) -> args[0].set(args[0].getNumber() + args[1].getNumber())
    ),

    DECREASE(ScriptActionCategory.VAR, "Decrease", "Var, Num", "Decreases the value of the given variable by a given number.", false,
        (args, inner, event) -> args[0].set(args[0].getNumber() - args[1].getNumber())
    ),

    SET(ScriptActionCategory.VAR, "Set", "Var, Val", "Sets one variable to a given value.", false,
        (args, inner, event) -> args[0].set(args[1].get())
    ),

    ADD(ScriptActionCategory.VAR, "Add", "Var, Num, Num", "Sets the variable to the sum of the two numbers.", false,
        (args, inner, event) -> args[0].set(args[1].getNumber() + args[2].getNumber())
    ),

    SUBTRACT(ScriptActionCategory.VAR, "Subtract", "Var, Num, Num", "Sets the variable to the difference of the two numbers.", false,
        (args, inner, event) -> args[0].set(args[1].getNumber() - args[2].getNumber())
    ),

    MULTIPLY(ScriptActionCategory.VAR, "Multiply", "Var, Num, Num", "Sets the variable to the product of the two numbers.", false,
        (args, inner, event) -> args[0].set(args[1].getNumber() * args[2].getNumber())
    ),

    DIVIDE(ScriptActionCategory.VAR, "Divide", "Var, Num, Num", "Sets the variable to the quotient of the two numbers.", false,
        (args, inner, event) -> args[0].set(args[1].getNumber() / args[2].getNumber())
    ),

    MODULO(ScriptActionCategory.VAR, "Modulo", "Var, Num, Num", "Sets the variable to the remainder of the two numbers.", false,
        (args, inner, event) -> args[0].set(args[1].getNumber() % args[2].getNumber())
    ),

    RANDOM(ScriptActionCategory.VAR, "Random", "Var, Min, Max", "Sets the variable to a random decimal number between the two numbers.", false,
        (args, inner, event) -> args[0].set(Math.random() * (args[2].getNumber() - args[1].getNumber()) + args[1].getNumber())
    ),

    RANDOM_INT(ScriptActionCategory.VAR, "RandomInt", "Var, Min, Max", "Sets the variable to a random non decimal number between the two numbers.", false,
        (args, inner, event) -> args[0].set(Math.floor(Math.random() * (args[2].getNumber() - args[1].getNumber()) + args[1].getNumber()))
    ),

    GREATER(ScriptActionCategory.IF, "Greater", "Num, Num", "True if the first variable is greater than the second.", true, (args, inner, event) -> {
        if (args[0].getNumber() > args[1].getNumber()) {
            inner.run();
        }
    }),

    LESS(ScriptActionCategory.IF, "Less", "Num, Num", "True if the first variable is less than the second.", true, (args, inner, event) -> {
        if (args[0].getNumber() < args[1].getNumber()) {
            inner.run();
        }
    }),

    GREATER_EQUAL(ScriptActionCategory.IF, "GreaterEqual", "Num, Num", "True if the first variable is greater than or equal to the second.", true, (args, inner, event) -> {
        if (args[0].getNumber() >= args[1].getNumber()) {
            inner.run();
        }
    }),

    LESS_EQUAL(ScriptActionCategory.IF, "LessEqual", "Num, Num", "True if the first variable is less than or equal to the second.", true, (args, inner, event) -> {
        if (args[0].getNumber() <= args[1].getNumber()) {
            inner.run();
        }
    }),

    EQUAL(ScriptActionCategory.IF, "Equal", "Num, Num", "True if the first variable is equal to the second.", true, (args, inner, event) -> {
        if (args[0].getNumber() == args[1].getNumber()) {
            inner.run();
        }
    }),

    NOT_EQUAL(ScriptActionCategory.IF, "NotEqual", "Num, Num", "True if the first variable is not equal to the second.", true, (args, inner, event) -> {
        if (args[0].getNumber() != args[1].getNumber()) {
            inner.run();
        }
    }),

    TIMES(ScriptActionCategory.REPEAT, "Times", "Num", "Repeats Num times.", true, (args, inner, event) -> {
        double num = args[0].getNumber();
        for (int i = 0; i < num; i++) {
            inner.run();
        }
    }),

    SUDO(ScriptActionCategory.PLAYER, "Sudo", "Txt", "Makes the player type in chat.", false,
        (args, inner, event) -> CodeUtilities.MC.player.chat(args[0].getText())
    ),

    IF_REGEX(ScriptActionCategory.IF, "Regex", "Txt, Txt", "True if the text matches the regex.", true, (args, inner, event) -> {
        if (args[0].getText().matches(args[1].getText())) {
            inner.run();
        }
    }),

    IF_NOT_REGEX(ScriptActionCategory.IF, "NotRegex", "Txt, Txt", "True if the text does not match the regex.", true, (args, inner, event) -> {
        if (!args[0].getText().matches(args[1].getText())) {
            inner.run();
        }
    }),

    IF_CONTAINS(ScriptActionCategory.IF, "Contains", "Txt, Txt", "True if the first text contains the second text.", true, (args, inner, event) -> {
        if (args[0].getText().contains(args[1].getText())) {
            inner.run();
        }
    }),

    IF_NOT_CONTAINS(ScriptActionCategory.IF, "NotContains", "Txt, Txt", "True if the first text does not contain the second text.", true, (args, inner, event) -> {
        if (!args[0].getText().contains(args[1].getText())) {
            inner.run();
        }
    }),

    IF_STARTS_WITH(ScriptActionCategory.IF, "StartsWith", "Txt, Txt", "True if the first text starts with the second text.", true, (args, inner, event) -> {
        if (args[0].getText().startsWith(args[1].getText())) {
            inner.run();
        }
    }),

    IF_NOT_STARTS_WITH(ScriptActionCategory.IF, "NotStartsWith", "Txt, Txt", "True if the first text does not start with the second text.", true, (args, inner, event) -> {
        if (!args[0].getText().startsWith(args[1].getText())) {
            inner.run();
        }
    }),

    IF_ENDS_WITH(ScriptActionCategory.IF, "EndsWith", "Txt, Txt", "True if the first text ends with the second text.", true, (args, inner, event) -> {
        if (args[0].getText().endsWith(args[1].getText())) {
            inner.run();
        }
    }),

    IF_NOT_ENDS_WITH(ScriptActionCategory.IF, "NotEndsWith", "Txt, Txt", "True if the first text does not end with the second text.", true, (args, inner, event) -> {
        if (!args[0].getText().endsWith(args[1].getText())) {
            inner.run();
        }
    }),

    CANCEL_EVENT(ScriptActionCategory.CONTROL, "Cancel", "", "Cancels the execution of the event if possible.", false, (args, inner, event) -> {
        if (event instanceof Cancellable c) {
            c.setCancelled(true);
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
