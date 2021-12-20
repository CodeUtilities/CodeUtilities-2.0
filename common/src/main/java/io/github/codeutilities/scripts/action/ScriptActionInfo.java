package io.github.codeutilities.scripts.action;

import io.github.codeutilities.event.Event;
import io.github.codeutilities.scripts.Script;

public record ScriptActionInfo(ScriptActionArgument[] args, Runnable inner, Event event, Script script) {

}
