package io.github.codeutilities.mod.mixin.screen;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.mod.config.Config;
import io.github.codeutilities.sys.util.networking.DFInfo;
import io.github.codeutilities.sys.util.networking.State;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeInventoryScreen.class)
public class MixinCreativeInventoryScreen {
    @Shadow @Nullable private Slot deleteItemSlot;

    @Inject(method = "onMouseClick", at = @At("HEAD"), cancellable = true)
    public void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType, CallbackInfo ci) {
        if (Config.getBoolean("destroyItemReset") && DFInfo.isOnDF() && DFInfo.currentState.getMode() == State.CurrentState.Mode.DEV
                && actionType == SlotActionType.QUICK_MOVE && slot == this.deleteItemSlot) {
            CodeUtilities.MC.setScreen(null);
            CodeUtilities.MC.player.sendChatMessage("/rc");
            ci.cancel();
        }
    }
}
