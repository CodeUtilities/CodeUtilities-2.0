package io.github.codeutilities.event.impl;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.codeutilities.event.Event;

public record RenderGuiEvent(PoseStack pose, float tickDelta, int scaledWindowWidth, int scaledWindowHeight) implements Event {

}
