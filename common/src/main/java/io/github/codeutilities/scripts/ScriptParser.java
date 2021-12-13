package io.github.codeutilities.scripts;

import io.github.codeutilities.scripts.action.ScriptAction;
import io.github.codeutilities.scripts.action.ScriptActionArgument;
import io.github.codeutilities.scripts.action.ScriptActionArgument.ScriptActionArgumentType;
import io.github.codeutilities.scripts.action.ScriptActionCategory;
import io.github.codeutilities.scripts.action.ScriptActionType;
import io.github.codeutilities.scripts.event.ScriptEvent;
import io.github.codeutilities.scripts.event.ScriptEventType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScriptParser {

    public static List<ScriptEvent> parseFile(String source, Script script) {
        ScriptEvent event = null;
        String[] lines = source.split("\n");
        Pattern actionP = Pattern.compile("^(\\w+)\\.(\\w+)\\((.*)\\) ?(\\{?)$");
        Pattern eventP = Pattern.compile("^Event\\.(\\w+):$");
        List<ScriptEvent> events = new ArrayList<>();
        List<ScriptAction> trace = new ArrayList<>();

        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("#")) {
                continue;
            }
            if (line.isEmpty()) {
                continue;
            }

            Matcher eventMatcher = eventP.matcher(line);
            if (eventMatcher.matches()) {
                String name = eventMatcher.group(1);
                if (ScriptEventType.exists(name)) {
                    if (event != null) {
                        events.add(event);
                    }
                    if (trace.size() > 0) {
                        throw new IllegalArgumentException("Invalid brackets");
                    }
                    event = new ScriptEvent(name);
                } else {
                    throw new IllegalArgumentException("Invalid script (Unknown event)");
                }
            } else {
                Matcher actionMatcher = actionP.matcher(line);
                if (actionMatcher.matches()) {
                    if (event == null) {
                        throw new IllegalArgumentException("Invalid (No Event present)");
                    }
                    String type = actionMatcher.group(1);
                    String name = actionMatcher.group(2);

                    ScriptActionCategory cat = ScriptActionCategory.get(type);
                    if (cat == null) {
                        throw new IllegalArgumentException("Invalid script (Unknown action category)");
                    }
                    ScriptActionType t = ScriptActionType.get(cat, name);
                    if (t == null) {
                        throw new IllegalArgumentException("Invalid script (Unknown action in category)");
                    }

                    ScriptAction action = new ScriptAction(t, parseArguments(actionMatcher.group(3), script));

                    if (trace.size() > 0) {
                        trace.get(trace.size()-1).inner.add(action);
                    } else {
                        event.actions.add(action);
                    }

                    if (action.getType().isCurly()) {
                        if (actionMatcher.group(4).equals("{")) {
                            trace.add(action);
                        } else {
                            throw new IllegalArgumentException("Invalid script (Missing curly bracket)");
                        }
                    } else if (actionMatcher.group(4).equals("{")) {
                        throw new IllegalArgumentException("Invalid script (Unexpected curly bracket)");
                    }
                } else if (line.equals("}")) {
                    if (trace.size() > 0) {
                        trace.remove(trace.size() - 1);
                    } else {
                        throw new IllegalArgumentException("Invalid Brackets");
                    }
                } else {
                    throw new IllegalArgumentException("Invalid script (Invalid Format)");
                }
            }
        }
        if (trace.size() > 0) {
            throw new IllegalArgumentException("Missing closing '}'");
        }

        if (event != null) {
            events.add(event);
        }
        return events;
    }

    private static List<ScriptActionArgument> parseArguments(String arguments, Script script) {
        List<String> stringArgs = new ArrayList<>();

        boolean escaped = false;
        boolean inString = false;

        StringBuilder sb = new StringBuilder();

        for (char c : arguments.toCharArray()) {
            if (escaped) {
                if (c == 'n') {
                    sb.append('\n');
                } else {
                    sb.append(c);
                }
                escaped = false;
            } else if (c == '\\') {
                escaped = true;
            } else if (c == '"') {
                inString = !inString;
                if (inString) {
                    sb.append(c);//add a quote at the beginning to mark it as a string
                }
            } else if (c == ',' && !inString) {
                stringArgs.add(sb.toString());
                sb = new StringBuilder();
            } else if (c != ' ' || inString) {
                sb.append(c);
            }
        }
        stringArgs.add(sb.toString());

        List<ScriptActionArgument> args = new ArrayList<>();

        for (String arg : stringArgs) {
            if (arg.startsWith("\"")) {
                args.add(new ScriptActionArgument(ScriptActionArgumentType.TEXT, arg.substring(1), script));
            } else {
                try {
                    args.add(new ScriptActionArgument(ScriptActionArgumentType.NUMBER, Double.parseDouble(arg), script));
                } catch (Exception err) {
                    args.add(new ScriptActionArgument(ScriptActionArgumentType.VARIABLE, arg, script));
                }
            }
        }

        return args;
    }


}
