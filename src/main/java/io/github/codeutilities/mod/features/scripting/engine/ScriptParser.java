package io.github.codeutilities.mod.features.scripting.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ScriptParser {

    public static List<ScriptPart> parse(String source,ScriptContext ctx) throws ScriptParserException {
        List<ScriptPart> parts = new ArrayList<>();
        int index = 0;
        for (String line : source.split("\n")) {
            try {
                line = line.trim();
                if (line.matches("(\\w+#\\w+(:|\\(.*\\)( \\{)?)|})")) {
                    if (line.startsWith("Event#")) {
                        parts.add(ScriptEvent.byName(line.substring(line.indexOf("#") + 1, line.indexOf(":"))));
                    } else if (line.equals("}")) {
                        parts.add(new ScriptActionArgs(ScriptAction.CLOSE_BRACKET, ScriptArguments.EMPTY));
                    } else {
                        parts.add(parseAction(line,ctx));
                    }
                } else if (line.startsWith("#") || line.isEmpty()) {
                    continue;
                } else {
                    throw new Exception();
                }
            } catch (Exception err) {
                err.printStackTrace();
                throw new ScriptParserException(index + 1);
            }
            index++;
        }
        return parts;
    }

    private static ScriptPart parseAction(String line,ScriptContext ctx) {
        String category = line.substring(0, line.indexOf("#"));
        String name = line.substring(line.indexOf("#") + 1, line.indexOf("("));
        for (ScriptAction action : ScriptAction.values()) {
            if (Objects.equals(action.category.label, category)) {
                if (Objects.equals(action.name, name)) {
                    return new ScriptActionArgs(action, parseArgs(line,ctx));
                }
            }
        }
        throw new IllegalArgumentException();
    }

    private static ScriptArguments parseArgs(String line,ScriptContext ctx) {
        line = line.substring(line.indexOf("(") + 1);
        if (line.endsWith("{")) {
            line = line.substring(0, line.length() - 1);
        }
        line = line.trim();
        line = line.substring(0, line.length() - 1);

        StringBuilder current = new StringBuilder();
        List<String> strArgs = new ArrayList<>();
        boolean escape = false;
        for (char c : line.toCharArray()) {
            if (escape) {
                if (c == 'n') c = '\n';
                current.append(c);
                escape = false;
            } else if (c == '\\') {
                escape = true;
            } else if (c == ',' && !current.toString().startsWith("\"")) {
                strArgs.add(current.toString());
                current = new StringBuilder();
            } else if (c == ' ') {
                if (current.toString().startsWith("\"")) {
                    current.append(c);
                }
            } else {
                current.append(c);
            }
        }
        strArgs.add(current.toString());

        List<Object> args = new ArrayList<>();
        for (String arg : strArgs) {
            try {
                args.add(Double.parseDouble(arg));
            } catch (NumberFormatException ignored) {
                if (arg.startsWith("\"") && arg.length()>=2) {
                    args.add(arg.substring(1,arg.length()-1));
                } else args.add(new ScriptVariable(arg,ctx));
            }
        }
        return new ScriptArguments(args.toArray(new Object[]{}));
    }

}
