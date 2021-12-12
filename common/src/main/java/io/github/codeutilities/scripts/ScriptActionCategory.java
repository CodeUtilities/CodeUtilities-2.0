package io.github.codeutilities.scripts;

public enum ScriptActionCategory {

    PLAYER("Player"),
    IF("If"),
    REPEAT("Repeat"),
    VAR("Var");

    final String name;
    ScriptActionCategory(String name) {
        this.name = name;
    }

    public static ScriptActionCategory get(String name) {
        for (ScriptActionCategory category : values()) {
            if (category.name.equals(name)) {
                return category;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }
}
