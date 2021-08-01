package io.github.codeutilities.mod.features.modules.actions;

import io.github.codeutilities.mod.features.modules.actions.impl.CancelNextMessagesAction;
import io.github.codeutilities.mod.features.modules.actions.impl.ContinueIfEqualAction;
import io.github.codeutilities.mod.features.modules.actions.impl.GrabMessagesAction;
import io.github.codeutilities.mod.features.modules.actions.impl.MessageAction;
import io.github.codeutilities.mod.features.modules.actions.impl.SendMessageAction;
import io.github.codeutilities.mod.features.modules.actions.impl.StopIfEqualAction;
import io.github.codeutilities.mod.features.modules.actions.impl.WaitAction;
import io.github.codeutilities.mod.features.modules.actions.json.ActionJson;
import java.util.HashMap;

public class Action {

    private static final Action[] ACTIONS = new Action[]{
            new MessageAction(),
            new CancelNextMessagesAction(),
            new SendMessageAction(),
            new StopIfEqualAction(),
            new ContinueIfEqualAction(),
            new WaitAction(),
            new GrabMessagesAction()
    };
    // actionId, action
    private static final HashMap<String, Action> ACTION_IDS = new HashMap<>();

    public static void cacheActions() {
        for (Action action : ACTIONS) {
            ACTION_IDS.put(action.getId(), action);
        }
    }

    public String getId() {
        return null;
    }

    public void execute(ActionJson params) {
    }

    public static Action getAction(String id) {
        return ACTION_IDS.get(id);
    }

}
