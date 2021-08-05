package io.github.codeutilities.mod.config.impl;

import io.github.codeutilities.mod.config.structure.ConfigGroup;
import io.github.codeutilities.mod.config.structure.ConfigSubGroup;
import io.github.codeutilities.mod.config.types.BooleanSetting;
import io.github.codeutilities.mod.config.types.DynamicStringSetting;
import io.github.codeutilities.mod.config.types.EnumSetting;
import io.github.codeutilities.mod.config.types.TextDescription;
import io.github.codeutilities.mod.features.discordrpc.RPCElapsedOption;

public class DiscordRPCGroup extends ConfigGroup {
    public DiscordRPCGroup(String name) {
        super(name);
    }

    @Override
    public void initialize() {

        this.register(new TextDescription("discordRPCDescription"));

        this.register(new BooleanSetting("discordRPC", true));
        this.register(new BooleanSetting("discordRPCShowSession", true));

        // Spawn RPC
        ConfigSubGroup spawn = new ConfigSubGroup("discordRPCSpawn");
        spawn.register(new DynamicStringSetting("discordRPCSpawnDetails", "At Spawn"));
        spawn.register(new DynamicStringSetting("discordRPCSpawnState", "Node ${node.id}"));

        // Plot RPC
        ConfigSubGroup plot = new ConfigSubGroup("discordRPCPlot");
        plot.register(new DynamicStringSetting("discordRPCPlotDetails", "${plot.name}"));
        plot.register(new DynamicStringSetting("discordRPCPlotState",
                "Plot ID: ${plot.id} - Node ${node.id}"));
        plot.register(new BooleanSetting("discordRPCShowPlotMode", true));

        // Elapsed Behaviour
        ConfigSubGroup elapsed = new ConfigSubGroup("discordRPCElapsed");
        elapsed.register(new BooleanSetting("discordRPCShowElapsed", true));
        elapsed.register(new EnumSetting<>("discordRPCElapsed", RPCElapsedOption.class, RPCElapsedOption.SERVER_JOIN));

        this.register(spawn);
        this.register(plot);
        this.register(elapsed);
    }
}