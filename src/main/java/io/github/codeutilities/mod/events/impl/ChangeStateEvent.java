package io.github.codeutilities.mod.events.impl;

import io.github.codeutilities.mod.events.interfaces.HyperCubeEvents;
import io.github.codeutilities.mod.features.StreamerModeHandler;
import io.github.codeutilities.sys.networking.State;
import net.minecraft.util.ActionResult;

public class ChangeStateEvent {

    public ChangeStateEvent() {
        HyperCubeEvents.CHANGE_STATE.register(this::run);
    }

    private ActionResult run(State newstate, State oldstate) {
//        StreamerModeHandler.handleStateChange(oldstate, newstate);

        try {
//            DFDiscordRPC.getInstance().update(newstate); TODO: update discordrpc
//            if(Client.client.isOpen()) Client.client.send("{\"content\":" + newstate.toJson() + ",\"type\":\"state\"}"); TODO: update weboskcet
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ActionResult.PASS;
    }
}
