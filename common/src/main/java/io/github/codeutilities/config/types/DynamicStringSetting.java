package io.github.codeutilities.config.types;

public class DynamicStringSetting extends StringSetting {
    public DynamicStringSetting() {
    }

    public DynamicStringSetting(String key) {
        super(key, "");
    }

    public DynamicStringSetting(String key, String defaultValue) {
        super(key, defaultValue);
    }
}
