package io.github.codeutilities.config.internal.settings;

import io.github.codeutilities.config.types.IConfigEnum;

public enum OverlayVisibility implements IConfigEnum {
    NONE,
    ALWAYS,
    MENU,
    CHAT;

    @Override
    public String getKey() {
        return "projectaudio_overlay_visability";
    }
}
