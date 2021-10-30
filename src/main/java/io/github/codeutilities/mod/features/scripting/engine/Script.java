package io.github.codeutilities.mod.features.scripting.engine;

import io.github.codeutilities.mod.features.scripting.engine.ScriptAction.Category;
import io.github.codeutilities.sys.player.chat.ChatType;
import io.github.codeutilities.sys.player.chat.ChatUtil;
import java.io.File;
import java.util.List;
import java.util.Map.Entry;

public class Script {

    ScriptContext ctx;
    List<ScriptPart> parts;
    public String name;
    public String source;
    public File file;

    private Script(String name, String source, File file, List<ScriptPart> parts,ScriptContext ctx) {
        this.name = name;
        this.source = source;
        this.file = file;
        this.parts = parts;
        this.ctx = ctx;
    }

    public static Script of(String name, String source, File file) throws ScriptParserException {
        ScriptContext ctx = new ScriptContext();
        return new Script(name,source,file,ScriptParser.parse(source,ctx),ctx);
    }

    public void triggerEvent(ScriptEvent evn, ScriptContext ctx) {
        for (Entry<String,Object> var : ctx.vars.entrySet()) {
            this.ctx.setVar(var.getKey(),var.getValue());
        }
        boolean executing = false;
        int i = 0;
        try {
            while (i < parts.size()) {
                if (parts.get(i) == evn) {
                    executing = true;
                } else if (executing) {
                    if (parts.get(i) instanceof ScriptActionArgs action) {
                        if (action.type() == ScriptAction.CLOSE_BRACKET) {
                            i++;
                            continue;
                        }
                        boolean result = action.type().executor.apply(action.args(),ctx);
                        if (action.type().category == Category.IF && !result) {
                            int layer = 0;
                            while (i < parts.size()) {
                                if (parts.get(i) instanceof ScriptActionArgs ta) {
                                    if (ta.type().category == Category.IF) {
                                        layer++;
                                    } else if (ta.type() == ScriptAction.CLOSE_BRACKET) {
                                        layer--;
                                        if (layer <= -1) break;
                                    }
                                }
                                i++;
                            }
                        }
                    }
                }
                i++;
            }
        } catch (Exception err) {
            err.printStackTrace();
            ChatUtil.sendMessage("Error executing script " + name + " at line " + (i+1), ChatType.FAIL);
        }
    }
}
