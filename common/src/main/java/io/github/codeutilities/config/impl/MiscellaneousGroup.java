package io.github.codeutilities.config.impl;

import io.github.codeutilities.config.internal.settings.OverlayVisibility;
import io.github.codeutilities.config.structure.ConfigGroup;
import io.github.codeutilities.config.structure.ConfigSubGroup;
import io.github.codeutilities.config.types.BooleanSetting;
import io.github.codeutilities.config.types.EnumSetting;
import io.github.codeutilities.config.types.IntegerSetting;

public class MiscellaneousGroup extends ConfigGroup {
    public MiscellaneousGroup(String name) {
        super(name);
    }

    @Override
    public void initialize() {
        ConfigSubGroup projectaudio = new ConfigSubGroup("projectaudio");
        projectaudio.register(new EnumSetting<>("projectaudio_overlay_visability", OverlayVisibility.class, OverlayVisibility.ALWAYS));
        projectaudio.register(new IntegerSetting("projectaudio_volume", 100));

        this.register(projectaudio);
    }
}
