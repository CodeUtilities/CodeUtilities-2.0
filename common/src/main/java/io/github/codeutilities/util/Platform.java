package io.github.codeutilities.util;

public enum Platform {

    UNKNOWN("Unknown"),
    FORGE("Forge"),
    FABRIC("Fabric");

    private final String displayName;

    Platform(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
