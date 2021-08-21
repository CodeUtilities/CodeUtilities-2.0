package io.github.codeutilities.mod.config.impl;

import io.github.codeutilities.mod.config.ConfigSounds;
import io.github.codeutilities.mod.config.structure.ConfigGroup;
import io.github.codeutilities.mod.config.structure.ConfigSubGroup;
import io.github.codeutilities.mod.config.types.*;
import io.github.codeutilities.mod.config.types.list.ListSetting;
import io.github.codeutilities.mod.config.types.list.StringListSetting;

import java.util.ArrayList;

public class MiscellaneousGroup extends ConfigGroup {
    public MiscellaneousGroup(String name) {
        super(name);
    }

    @Override
    public void initialize() {
        // Non sub-grouped
        this.register(new BooleanSetting("itemApi", true));
        this.register(new BooleanSetting("quickVarScope", true));
        this.register(new BooleanSetting("debugMode", false));
        this.register(new SoundSetting("incomingReportSound")
                .setSelected(ConfigSounds.FLUTE));

        // Audio
        ConfigSubGroup audio = new ConfigSubGroup("audio");
        audio.register(new BooleanSetting("audio", false));
        audio.register(new StringSetting("audioUrl", "https://audio.tomoli.me/"));
        this.register(audio);

        // Quick Number Change
        ConfigSubGroup quickNum = new ConfigSubGroup("quicknum");
        quickNum.register(new BooleanSetting("quicknum", true));
        quickNum.register(new BooleanSetting("quicknumSound", true));
        quickNum.register(new DoubleSetting("quicknumPrimaryAmount", 1.0));
        quickNum.register(new DoubleSetting("quicknumSecondaryAmount", 10d));
        quickNum.register(new DoubleSetting("quicknumTertiaryAmount", 0.1));
        this.register(quickNum);

        ConfigSubGroup soundlib = new ConfigSubGroup("soundlib");
        soundlib.register(new BooleanSetting("soundlib", true));
        soundlib.register(new StringSetting("awesdb", "https://aweslib.dfplots.net/"));
        soundlib.register(new IntegerSetting("maxMB", 50));
        soundlib.register(new IntegerSetting("maxAmnt", 50));
        soundlib.register(new StringSetting("consented", "google.com"));
        soundlib.register(new FloatSetting("volume", 0.5f));
        this.register(soundlib);

    }


}
