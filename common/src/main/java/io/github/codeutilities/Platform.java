package io.github.codeutilities;

public enum Platform {
    UNKNOWN("Unknown"),
    FORGE("Forge"),
    FABRIC("Fabric");

    final public String displayName;
    Platform(String name) {
        displayName = name;
    }
}
