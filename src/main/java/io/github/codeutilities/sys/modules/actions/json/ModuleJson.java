package io.github.codeutilities.sys.modules.actions.json;

import org.json.JSONArray;
import org.json.JSONObject;

public class ModuleJson extends JSONObject {
    public ModuleJson(JSONObject json) {
        super(json.toString());
    }

    public JSONObject getMeta() {
        return this.getJSONObject("meta");
    }

    public JSONArray getTriggers() {
        return this.getJSONArray("triggers");
    }

    public JSONArray getTasks() {
        return this.getJSONArray("tasks");
    }

    public JSONObject getTranslations() {
        return this.getJSONObject("translations");
    }

    public JSONObject getConfig() {
        return this.getJSONObject("config");
    }

    public String getId() {
        return this.getMeta().getString("id");
    }

}
