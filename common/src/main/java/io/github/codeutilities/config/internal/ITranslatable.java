package io.github.codeutilities.config.internal;

import net.minecraft.network.chat.TextComponent;

public interface ITranslatable {
    default TextComponent getTranslation(String key) {
        return ITranslatable.get(key);
    }

    static TextComponent get(String key) {
        return new TextComponent(key);
    }
}
