package io.github.codeutilities.config.types;

import io.github.codeutilities.config.structure.ConfigSetting;
import net.minecraft.network.chat.TextComponent;

public class TextDescription extends ConfigSetting<TextComponent> {

    public TextDescription(String key) {
        super(key, new TextComponent(""));
    }
}
