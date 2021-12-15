package io.github.codeutilities.util.templates;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class CodeAction implements CodeElement {

    private final CodeBlock type;
    private final String action, subAction;
    private final CodeArgument[] args;
    private final boolean inverted;

    public CodeAction(CodeBlock type, String action, String subAction, boolean inverted, CodeArgument... args) {
        this.type = type;
        this.action = action;
        this.args = args;
        this.subAction = subAction;
        this.inverted = inverted;
    }

    public CodeAction(CodeBlock type, String action, String subAction, CodeArgument... args) {
        this(type, action, subAction, false, args);
    }

    public CodeAction(CodeBlock type, String action, CodeArgument... args) {
        this(type, action, null, args);
    }

    public CodeAction(CodeBlock type) {
        this(type, null);
    }

    public CodeAction(CodeBlock type, String action, boolean inverted, CodeArgument... args) {
        this(type, action, null, inverted, args);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("id","block");
        json.addProperty("block",type.getName());
        if (inverted) {
            json.addProperty("inverted","NOT");
        }
        if (subAction != null) {
            json.addProperty("subAction",subAction);
        }
        if (action != null) {
            json.addProperty(type.getActionPath(),action);
        }

        JsonArray items = new JsonArray();
        for (CodeArgument arg : this.args) {
            items.add(arg.toJson());
        }
        JsonObject args = new JsonObject();
        args.add("items",items);
        json.add("args",args);

        return json;
    }
}
