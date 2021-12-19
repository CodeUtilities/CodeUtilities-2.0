package io.github.codeutilities.util.templates;

import com.google.gson.JsonObject;

public class CodeBracket implements CodeElement{

    public static CodeBracket NORMAL_OPEN = new CodeBracket("norm","open");
    public static CodeBracket NORMAL_CLOSE = new CodeBracket("norm","close");
    public static CodeBracket REPEAT_OPEN = new CodeBracket("repeat","open");
    public static CodeBracket REPEAT_CLOSE = new CodeBracket("repeat","close");

    private final String type, direct;

    public CodeBracket(String type, String direct) {
        this.type = type;
        this.direct = direct;
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("id","bracket");
        json.addProperty("type",type);
        json.addProperty("direct",direct);
        return json;
    }
}
