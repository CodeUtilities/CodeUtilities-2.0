package io.github.codeutilities.scripts.action;

import com.google.gson.JsonElement;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.CommandHandler;
import io.github.codeutilities.event.impl.ReloadCommandsEvent;
import io.github.codeutilities.event.impl.RenderGuiEvent;
import io.github.codeutilities.event.type.Cancellable;
import io.github.codeutilities.scripts.ScriptContext;
import io.github.codeutilities.scripts.ScriptHandler;
import io.github.codeutilities.scripts.ScriptUtil;
import io.github.codeutilities.scripts.event.ScriptEventType;
import io.github.codeutilities.scripts.types.ScriptDictionary;
import io.github.codeutilities.scripts.types.ScriptList;
import io.github.codeutilities.scripts.types.ScriptNumber;
import io.github.codeutilities.scripts.types.ScriptText;
import io.github.codeutilities.scripts.types.ScriptValue;
import io.github.codeutilities.util.ComponentUtil;
import io.github.codeutilities.util.FileUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

public enum ScriptActionType {

    LOG(ScriptActionCategory.SCRIPT, "Log", "Txts", "Prints the given message and the script name to the chat.", false, info -> {
        StringBuilder sb = new StringBuilder();

        sb.append("[")
            .append(info.script().getName())
            .append("] ");

        for (ScriptActionArgument arg : info.args()) {
            sb.append(arg.get().text());
        }

        CodeUtilities.MC.player.displayClientMessage(new TextComponent(sb.toString()).withStyle(ChatFormatting.GRAY), false);
    }),

    PRINT_FORMATTED(ScriptActionCategory.PLAYER, "Print", "Txts", "Prints the given message to the chat with colorcodes applied.", false, info -> {
        StringBuilder sb = new StringBuilder();

        for (ScriptActionArgument arg : info.args()) {
            sb.append(arg.get().text());
        }
        CodeUtilities.MC.player.displayClientMessage(ComponentUtil.fromString(sb.toString().replaceAll("&", "§")), false);
    }),

    ACTIONBAR(ScriptActionCategory.PLAYER, "ActionBar", "Txts", "Sends the given message to the player's action bar with colorcodes applied.", false, info -> {
       StringBuilder sb = new StringBuilder();

        for (ScriptActionArgument arg : info.args()) {
            sb.append(arg.get().text());
        }

        CodeUtilities.MC.player.displayClientMessage(ComponentUtil.fromString(sb.toString().replaceAll("&", "§")), true);
    }),

    INCREASE(ScriptActionCategory.NUMBER, "Increase", "Var, Nums", "Increases the value of the given variable by the given number(s).", false, info -> {
        double result = 0;
        for (ScriptActionArgument arg : info.args()) {
            result += arg.get().number();
        }
        info.args()[0].set(new ScriptNumber(result));
    }),

    DECREASE(ScriptActionCategory.NUMBER, "Decrease", "Var, Nums", "Decreases the value of the given variable by the given number(s).", false, info -> {
        double result = info.args()[0].get().number();
        for (int i = 1; i < info.args().length; i++) {
            result -= info.args()[i].get().number();
        }
        info.args()[0].set(new ScriptNumber(result));
    }),

    SET(ScriptActionCategory.VALUE, "Set", "Var, Val", "Sets one variable to a given value.", false,
        info -> info.args()[0].set(info.args()[1].get().copy())
    ),

    ADD(ScriptActionCategory.NUMBER, "Add", "Var, Nums", "Sets the variable to the sum of the numbers.", false, info -> {
        double result = 0;
        for (int i = 1; i < info.args().length; i++) {
            result += info.args()[i].get().number();
        }
        info.args()[0].set(new ScriptNumber(result));
    }),

    SUBTRACT(ScriptActionCategory.NUMBER, "Subtract", "Var, Num, Num", "Sets the variable to the difference of the two numbers.", false,
        info -> info.args()[0].set(new ScriptNumber(info.args()[1].get().number() - info.args()[2].get().number()))
    ),

    MULTIPLY(ScriptActionCategory.NUMBER, "Multiply", "Var, Nums", "Sets the variable to the product of the numbers.", false, info -> {
        double result = 1;
        for (int i = 1; i < info.args().length; i++) {
            result *= info.args()[i].get().number();
        }
        info.args()[0].set(new ScriptNumber(result));
    }),

    DIVIDE(ScriptActionCategory.NUMBER, "Divide", "Var, Num, Num", "Sets the variable to the quotient of the two numbers.", false,
        info -> info.args()[0].set(new ScriptNumber(info.args()[1].get().number() / info.args()[2].get().number()))
    ),

    MODULO(ScriptActionCategory.NUMBER, "Modulo", "Var, Num, Num", "Sets the variable to the remainder of the two numbers.", false,
        info -> info.args()[0].set(new ScriptNumber(info.args()[1].get().number() % info.args()[2].get().number()))
    ),

    RANDOM(ScriptActionCategory.NUMBER, "Random", "Var, Min, Max", "Sets the variable to a random decimal number between the two numbers.", false,
        info -> info.args()[0].set(new ScriptNumber(Math.random() * (info.args()[2].get().number() - info.args()[1].get().number()) + info.args()[1].get().number()))
    ),

    RANDOM_INT(ScriptActionCategory.NUMBER, "RandomInt", "Var, Min, Max", "Sets the variable to a random non decimal number between the two numbers.", false,
        info -> info.args()[0].set(new ScriptNumber(Math.floor(Math.random() * (info.args()[2].get().number() - info.args()[1].get().number()) + info.args()[1].get().number())))
    ),

    TIMESTAMP(ScriptActionCategory.NUMBER, "Timestamp", "Var", "Sets the variable to the current timestamp in seconds with very high accuracy.", false,
        info -> info.args()[0].set(new ScriptNumber(System.currentTimeMillis() / 1000.0))
    ),

    CONCAT(ScriptActionCategory.TEXT, "Concat", "Var, Vals", "Sets the variable to the concatenation of the strings.", false,info -> {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < info.args().length; i++) {
            sb.append(info.args()[i].get().text());
        }
        info.args()[0].set(new ScriptText(sb.toString()));
    }),

    ROUND(ScriptActionCategory.NUMBER, "Round", "Var, Num", "Sets the variable to the number rounded to the nearest integer.", false,
        info -> info.args()[0].set(new ScriptNumber(Math.round(info.args()[1].get().number())))
    ),

    FLOOR(ScriptActionCategory.NUMBER, "Floor", "Var, Num", "Sets the variable to the number rounded down to the nearest integer.", false,
        info -> info.args()[0].set(new ScriptNumber(Math.floor(info.args()[1].get().number())))
    ),

    CEILING(ScriptActionCategory.NUMBER, "Ceiling", "Var, Num", "Sets the variable to the number rounded up to the nearest integer.", false,
        info -> info.args()[0].set(new ScriptNumber(Math.ceil(info.args()[1].get().number())))
    ),

    ABSOLUTE(ScriptActionCategory.NUMBER, "Absolute", "Var, Num", "Sets the variable to the absolute value of the number.", false,
        info -> info.args()[0].set(new ScriptNumber(Math.abs(info.args()[1].get().number())))
    ),

    REMOVE_FIRST(ScriptActionCategory.TEXT, "RemoveFirst", "Var, Text, Num", "Sets the variable to the text with the first num characters removed.", false,
        info -> info.args()[0].set(new ScriptText(info.args()[1].get().text().substring((int) info.args()[2].get().number())))
    ),

    REMOVE_LAST(ScriptActionCategory.TEXT, "RemoveLast", "Var, Text, Num", "Sets the variable to the text with the last num characters removed.", false,
        info -> info.args()[0].set(new ScriptText(info.args()[1].get().text().substring(0, info.args()[1].get().text().length() - (int) info.args()[2].get().number())))
    ),

    GREATER(ScriptActionCategory.NUMBER, "IfGreater", "Num, Num", "True if the first variable is greater than the second.", true, info -> {
        if (info.args()[0].get().number() > info.args()[1].get().number()) {
            info.inner().run();
        }
    }),

    LESS(ScriptActionCategory.NUMBER, "IfLess", "Num, Num", "True if the first variable is less than the second.", true, info -> {
        if (info.args()[0].get().number() < info.args()[1].get().number()) {
            info.inner().run();
        }
    }),

    GREATER_EQUAL(ScriptActionCategory.NUMBER, "IfGreaterEqual", "Num, Num", "True if the first variable is greater than or equal to the second.", true, info -> {
        if (info.args()[0].get().number() >= info.args()[1].get().number()) {
            info.inner().run();
        }
    }),

    LESS_EQUAL(ScriptActionCategory.NUMBER, "IfLessEqual", "Num, Num", "True if the first variable is less than or equal to the second.", true, info -> {
        if (info.args()[0].get().number() <= info.args()[1].get().number()) {
            info.inner().run();
        }
    }),

    EQUAL(ScriptActionCategory.VALUE, "IfEqual", "Val, Val", "True if the first variable is equal to the second.", true, info -> {
        if (info.args()[0].get().equals(info.args()[1].get())) {
            info.inner().run();
        }
    }),

    NOT_EQUAL(ScriptActionCategory.VALUE, "IfNotEqual", "Num, Num", "True if the first variable is not equal to the second.", true, info -> {
        if (info.args()[0].get().number() != info.args()[1].get().number()) {
            info.inner().run();
        }
    }),

    TIMES(ScriptActionCategory.REPEAT, "Times", "Num", "Repeats Num times.", true, info -> {
        double num = info.args()[0].get().number();
        for (int i = 0; i < num; i++) {
            info.inner().run();
        }
    }),

    SUDO(ScriptActionCategory.PLAYER, "Sudo", "Txt", "Makes the player type in chat.", false,
        info -> CodeUtilities.MC.player.chat(info.args()[0].get().text())
    ),

    IF_REGEX(ScriptActionCategory.TEXT, "IfRegex", "Txt, Txt", "True if the text matches the regex.", true, info -> {
        if (info.args()[0].get().text().matches(info.args()[1].get().text())) {
            info.inner().run();
        }
    }),

    IF_NOT_REGEX(ScriptActionCategory.TEXT, "IfNotRegex", "Txt, Txt", "True if the text does not match the regex.", true, info -> {
        if (!info.args()[0].get().text().matches(info.args()[1].get().text())) {
            info.inner().run();
        }
    }),

    IF_CONTAINS(ScriptActionCategory.TEXT, "IfContains", "Txt, Txt", "True if the first text contains the second text.", true, info -> {
        if (info.args()[0].get().text().contains(info.args()[1].get().text())) {
            info.inner().run();
        }
    }),

    IF_NOT_CONTAINS(ScriptActionCategory.TEXT, "IfNotContains", "Txt, Txt", "True if the first text does not contain the second text.", true, info -> {
        if (!info.args()[0].get().text().contains(info.args()[1].get().text())) {
            info.inner().run();
        }
    }),

    IF_STARTS_WITH(ScriptActionCategory.TEXT, "IfStartsWith", "Txt, Txt", "True if the first text starts with the second text.", true, info -> {
        if (info.args()[0].get().text().startsWith(info.args()[1].get().text())) {
            info.inner().run();
        }
    }),

    IF_NOT_STARTS_WITH(ScriptActionCategory.TEXT, "IfNotStartsWith", "Txt, Txt", "True if the first text does not start with the second text.", true, info -> {
        if (!info.args()[0].get().text().startsWith(info.args()[1].get().text())) {
            info.inner().run();
        }
    }),

    IF_ENDS_WITH(ScriptActionCategory.TEXT, "IfEndsWith", "Txt, Txt", "True if the first text ends with the second text.", true, info -> {
        if (info.args()[0].get().text().endsWith(info.args()[1].get().text())) {
            info.inner().run();
        }
    }),

    IF_NOT_ENDS_WITH(ScriptActionCategory.TEXT, "IfNotEndsWith", "Txt, Txt", "True if the first text does not end with the second text.", true, info -> {
        if (!info.args()[0].get().text().endsWith(info.args()[1].get().text())) {
            info.inner().run();
        }
    }),
    
    IF_GUI_OPEN(ScriptActionCategory.PLAYER, "IfGuiOpen", "", "True if the player has an open GUI.", true, info -> {
        if (CodeUtilities.MC.screen != null) {
            info.inner().run();
        }
    }),
    
    IF_NOT_GUI_OPEN(ScriptActionCategory.PLAYER, "IfNotGuiOpen", "", "True if the player does not have an open GUI.", true, info -> {
        if (CodeUtilities.MC.screen == null) {
            info.inner().run();
        }
    }),

    CANCEL_EVENT(ScriptActionCategory.SCRIPT, "CancelEvent", "", "Cancels the execution of the event if possible.", false, info -> {
        if (info.event() instanceof Cancellable c) {
            c.setCancelled(true);
        }
    }),

    REGISTER_COMMAND(ScriptActionCategory.SCRIPT, "RegisterCommand", "Txt", "Registers a command with a specific name. Only works on Event#RegisterCmds!", false, info -> {
        if (info.event() instanceof ReloadCommandsEvent) {
            CommandDispatcher<SharedSuggestionProvider> cd = ScriptHandler.getScriptCmdDispatcher();

            String[] path = info.args()[0].get().text().split(" ");

            if (path.length == 0) {
                return;
            }

            LiteralArgumentBuilder<SharedSuggestionProvider> current = null;

            for (int i = path.length - 1; i >= 0; i--) {
                if (current == null) {
                    current = CommandHandler.literal(path[i]);
                    current.then(CommandHandler.argument("args", StringArgumentType.greedyString()).executes(ctx -> {
                        ScriptContext sCtx = new ScriptContext();
                        sCtx.setVar("command", new ScriptText(ctx.getInput().substring(1)));
                        ScriptHandler.triggerEvent(ScriptEventType.SEND_COMMAND, sCtx, null);
                        return 1;
                    }));
                } else {
                    LiteralArgumentBuilder<SharedSuggestionProvider> newCurrent = CommandHandler.literal(path[i]);
                    newCurrent.then(current);
                    current = newCurrent;
                }
                current.executes(ctx -> {
                    ScriptContext sCtx = new ScriptContext();
                    sCtx.setVar("command", new ScriptText(ctx.getInput().substring(1)));
                    ScriptHandler.triggerEvent(ScriptEventType.SEND_COMMAND, sCtx, null);
                    return 1;
                });
            }

            cd.register(current);
        }
    }),

    CREATE_LIST(ScriptActionCategory.LIST, "Create", "Var", "Creates a new list.", false,
        info -> info.args()[0].set(new ScriptList(new ArrayList<>()))
    ),

    APPEND_VALUE(ScriptActionCategory.LIST, "AppendValue", "List, Val", "Appends the value to the list.", false,
        info -> info.args()[0].get().list().add(info.args()[1].get())
    ),

    GET_LIST_VALUE(ScriptActionCategory.LIST, "GetValue", "Var, List, Num", "Gets the value at the specified index from the list and stores it in the variable.", false,
        info -> info.args()[0].set(info.args()[1].get().list().get((int) info.args()[2].get().number()))
    ),

    LENGTH(ScriptActionCategory.LIST, "Length", "Var, List", "Gets the length of the list and stores it in the variable.", false,
        info -> info.args()[0].set(new ScriptNumber(info.args()[1].get().list().size()))
    ),

    REMOVE_VALUE(ScriptActionCategory.LIST, "RemoveValue", "List, Num", "Removes the value at the specified index from the list.", false,
        info -> info.args()[0].get().list().remove((int) info.args()[1].get().number()-1)
    ),

    FOR_EACH(ScriptActionCategory.REPEAT, "ListForEach", "Var, Var, List", "Iterates through the list and stores the value in the first and the index in the second variable.", true, info -> {
        for (int i = 0; i < info.args()[2].get().list().size(); i++) {
            info.args()[0].set(info.args()[2].get().list().get(i));
            info.args()[1].set(new ScriptNumber(i+1));
            info.inner().run();
        }
    }),

    WRITE_FILE(ScriptActionCategory.FILES, "Write", "Txt, Txt", "Writes the text to the specified file. File access is restricted to a folder.", false, info -> {
        String fileName = info.args()[0].get().text();
        if (!fileName.matches("^[\\w-_ .]{1,100}$")) {
            throw new IllegalArgumentException("Illegal file name!");
        }
        String scriptName = info.script().getName();
        if (scriptName.endsWith(".cus")) {
            scriptName = scriptName.substring(0, scriptName.length() - 4);
        }
        File file = FileUtil.cuFolder().resolve("Scripts").resolve(scriptName).resolve(fileName).toFile();
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            String content = ScriptUtil.valueToJson(info.args()[1].get()).toString();
            FileUtil.writeFile(file, content);
        } catch (Exception err) {
            throw new RuntimeException("Failed to write file!", err);
        }
    }),

    READ_FILE(ScriptActionCategory.FILES, "Read", "Var, Txt", "Reads the text from the specified file. File access is restricted to a folder.", false, info -> {
        String fileName = info.args()[1].get().text();
        if (!fileName.matches("^[\\w-_ .]{1,100}$")) {
            throw new IllegalArgumentException("Illegal file name!");
        }
        String scriptName = info.script().getName();
        if (scriptName.endsWith(".cus")) {
            scriptName = scriptName.substring(0, scriptName.length() - 4);
        }
        File file = FileUtil.cuFolder().resolve("Scripts").resolve(scriptName).resolve(fileName).toFile();
        try {
            JsonElement content = CodeUtilities.JSON_PARSER.parse(FileUtil.readFile(file));
            info.args()[0].set(ScriptUtil.jsonToValue(content));
        } catch (Exception err) {
            throw new RuntimeException("Failed to read file!", err);
        }
    }),

    FILE_EXISTS(ScriptActionCategory.FILES, "IfExists", "Txt", "Returns true if the specified file exists. File access is restricted to a folder.", true, info -> {
        String fileName = info.args()[0].get().text();
        if (!fileName.matches("^[\\w-_ .]{1,100}$")) {
            throw new IllegalArgumentException("Illegal file name!");
        }
        String scriptName = info.script().getName();
        if (scriptName.endsWith(".cus")) {
            scriptName = scriptName.substring(0, scriptName.length() - 4);
        }
        File file = FileUtil.cuFolder().resolve("Scripts").resolve(scriptName).resolve(fileName).toFile();

        if (file.exists()) {
            info.inner().run();
        }
    }),

    JOIN(ScriptActionCategory.TEXT, "Join", "Var, List, Txt", "Joins the list with the specified separator and stores the result in the variable.", false, info -> {
        StringBuilder result = new StringBuilder();

        ArrayList<ScriptValue> list = info.args()[0].get().list();
        for (int i = 0; i < list.size(); i++) {
            result.append(list.get(i).text());
            if (i != list.size() - 1) {
                result.append(info.args()[2].get().text());
            }
        }

        info.args()[0].set(new ScriptText(result.toString()));
    }),

    SPLIT(ScriptActionCategory.TEXT, "Split", "Var, Txt, Txt", "Splits the text with the specified separator and stores the result in the variable.", false, info -> {
        String text = info.args()[1].get().text();
        String separator = info.args()[2].get().text();
        ArrayList<ScriptValue> result = new ArrayList<>();
        for (String part : text.split(separator)) {
            result.add(new ScriptText(part));
        }
        info.args()[0].set(new ScriptList(result));
    }),

    GAMEMODE(ScriptActionCategory.PLAYER, "IfGamemode", "Txt", "True if the player is in the specified gamemode.", true, info -> {
        String input = info.args()[0].get().text();
        GameType type = CodeUtilities.MC.gameMode.getPlayerMode();
        String nameTarget = type.getName();
        String idTarget = String.valueOf(type.getId());

        if (input.equalsIgnoreCase(nameTarget) || input.equalsIgnoreCase(idTarget)) {
            info.inner().run();
        }
    }),

    GET_HELD_ITEM(ScriptActionCategory.PLAYER, "GetHeldItem", "Var", "Sets the variable to the item the player is holding as a dictionary.", false, info -> {
        CompoundTag item = new CompoundTag();
        CodeUtilities.MC.player.getMainHandItem().save(item);
        info.args()[0].set(ScriptUtil.tagToValue(item));
    }),

    SET_HELD_ITEM(ScriptActionCategory.PLAYER, "SetHeldItem", "Dict", "Sets the item the player is holding. Creative mode only.", false, info -> {
        if (CodeUtilities.MC.gameMode.getPlayerMode() == GameType.CREATIVE) {
            ItemStack item = ItemStack.of((CompoundTag) ScriptUtil.valueToTag(info.args()[0].get()));

            CodeUtilities.MC.gameMode.handleCreativeModeItemAdd(item, CodeUtilities.MC.player.getInventory().selected+36);
        }
    }),

    GET_DICT_VALUE(ScriptActionCategory.DICTIONARY, "GetValue", "Var, Dict, Txt", "Sets the variable to the value of the specified key in the dictionary.", false, info -> {
        info.args()[0].set(info.args()[1].get().dictionary().get(info.args()[2].get().text()));
    }),

    SET_DICT_VALUE(ScriptActionCategory.DICTIONARY, "SetValue", "Dict, Txt, Val", "Sets the value of the specified key in the dictionary.", false, info -> {
        info.args()[0].get().dictionary().put(info.args()[1].get().text(), info.args()[2].get());
    }),

    DICT_SIZE(ScriptActionCategory.DICTIONARY, "Size", "Var, Dict", "Sets the variable to the size of the dictionary.", false, info -> {
        info.args()[0].set(new ScriptNumber(info.args()[1].get().dictionary().size()));
    }),

    CREATE_DICT(ScriptActionCategory.DICTIONARY, "Create", "Var", "Creates a new dictionary.", false, info -> {
        info.args()[0].set(new ScriptDictionary(new HashMap<>()));
    }),

    DICT_FOR_EACH(ScriptActionCategory.REPEAT, "DictForEach", "Var, Var, Dict", "Runs the specified action for each key value pair in the dictionary. Sets the vars to the current key/value.", true, info -> {
        for (Entry<String, ScriptValue> entry : info.args()[2].get().dictionary().entrySet()) {
            info.args()[0].set(new ScriptText(entry.getKey()));
            info.args()[1].set(entry.getValue());
            info.inner().run();
        }
    }),

    DICT_KEY(ScriptActionCategory.DICTIONARY, "IfKey", "Dict, Txt", "True if the key is in the dictionary.", true, info -> {
        if (info.args()[0].get().dictionary().containsKey(info.args()[1].get().text())) {
            info.inner().run();
        }
    }),

    REMOVE_DICT_ENTRY(ScriptActionCategory.DICTIONARY, "RemoveEntry", "Dict, Txt", "Removes the specified key from the dictionary.", false, info -> {
        info.args()[0].get().dictionary().remove(info.args()[1].get().text());
    }),

    PARSE_NUM(ScriptActionCategory.NUMBER, "Parse", "Var, Txt", "Parses the text as a number and sets the variable to it.", false, info -> {
        info.args()[0].set(new ScriptNumber(Double.parseDouble(info.args()[1].get().text())));
    }),

    VAL_TO_JSON(ScriptActionCategory.VALUE, "ToJson", "Var, Val", "Sets the variable to the JSON text representation of the value.", false, info -> {
        info.args()[0].set(new ScriptText(ScriptUtil.valueToJson(info.args()[1].get()).toString()));
    }),

    JSON_TO_VAL(ScriptActionCategory.VALUE, "FromJson", "Var, Txt", "Sets the variable to the value represented by the JSON text.", false, info -> {
        info.args()[0].set(ScriptUtil.jsonToValue(CodeUtilities.JSON_PARSER.parse(info.args()[1].get().text())));
    }),

    LIST_FILES(ScriptActionCategory.FILES, "List", "Var", "Sets the variable to a list of saved files.", false, info -> {
        String scriptName = info.script().getName();
        if (scriptName.endsWith(".cus")) {
            scriptName = scriptName.substring(0, scriptName.length() - 4);
        }
        File file = FileUtil.cuFolder().resolve("Scripts").resolve(scriptName).toFile();

        ArrayList<ScriptValue> files = new ArrayList<>();

        for (File f : file.listFiles()) {
            if (f.isFile()) {
                files.add(new ScriptText(f.getName()));
            }
        }

        info.args()[0].set(new ScriptList(files));
    }),

    LEFT_BOUND_TEXT(ScriptActionCategory.RENDER, "LeftBoundText", "Txt, Num, Num", "Renders the text at the specified location. The text will be left-aligned. Only works in the RenderGuiEvent event.", false, info -> {
        if (info.event() instanceof RenderGuiEvent event) {
            PoseStack stack = event.pose();
            String text = info.args()[0].get().text().replaceAll("&","§");
            int x = (int) info.args()[1].get().number();
            int y = (int) info.args()[2].get().number();
            Font f = CodeUtilities.MC.font;

            f.drawShadow(stack,ComponentUtil.fromString(text),x,y,0xFFFFFFFF);
        }
    }),

    RIGHT_BOUND_TEXT(ScriptActionCategory.RENDER, "RightBoundText", "Txt, Num, Num", "Renders the text at the specified location. The text will be right-aligned. Only works in the RenderGuiEvent event.", false, info -> {
        if (info.event() instanceof RenderGuiEvent event) {
            PoseStack stack = event.pose();
            String text = info.args()[0].get().text().replaceAll("&","§");
            int x = (int) info.args()[1].get().number();
            int y = (int) info.args()[2].get().number();
            Font f = CodeUtilities.MC.font;
            TextComponent textComp = ComponentUtil.fromString(text);
            int width = f.width(textComp);

            f.drawShadow(stack,textComp,x - width,y,0xFFFFFFFF);
        }
    }),

    CENTERED_TEXT(ScriptActionCategory.RENDER, "CenteredText", "Txt, Num, Num", "Renders the text at the specified location. The text will be centered. Only works in the RenderGuiEvent event.", false, info -> {
        if (info.event() instanceof RenderGuiEvent event) {
            PoseStack stack = event.pose();
            String text = info.args()[0].get().text().replaceAll("&","§");
            int x = (int) info.args()[1].get().number();
            int y = (int) info.args()[2].get().number();
            Font f = CodeUtilities.MC.font;
            TextComponent textComp = ComponentUtil.fromString(text);
            int width = f.width(textComp);

            f.drawShadow(stack,textComp,x - width / 2,y,0xFFFFFFFF);
        }
    });

    private final String name, args, desc;
    private final boolean curly;
    private final Consumer<ScriptActionInfo> code;
    private final ScriptActionCategory cat;

    ScriptActionType(ScriptActionCategory cat, String name, String args, String desc, boolean curly, Consumer<ScriptActionInfo> code) {
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

    public Consumer<ScriptActionInfo> getCode() {
        return code;
    }
}
