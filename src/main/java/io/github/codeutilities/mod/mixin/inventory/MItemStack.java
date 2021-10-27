package io.github.codeutilities.mod.mixin.inventory;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.mod.features.keybinds.Keybinds;
import java.util.List;
import java.util.Set;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.InputUtil.Key;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class MItemStack {

    @Shadow
    private NbtCompound tag;

    @Shadow
    @Nullable
    public abstract NbtCompound getSubTag(String key);

    @Inject(method = "getTooltip", at = @At("RETURN"), cancellable = true)
    private void getTooltip(PlayerEntity player, TooltipContext context,
        CallbackInfoReturnable<List<Text>> cir) {

        if (player == null) return;

        try {

            String cname = FabricLoader.getInstance().isDevelopmentEnvironment() ? "boundKey" : "field_1655";

            int keycode = ((Key) FieldUtils.getField(KeyBinding.class,cname,true).get(Keybinds.showTags)).getCode();

            if (keycode == -1) return;

            if (InputUtil.isKeyPressed(CodeUtilities.MC.getWindow().getHandle(),keycode)) {
                List<Text> t = cir.getReturnValue();

                NbtCompound tags = getSubTag("PublicBukkitValues");

                if (tags != null) {
                    Set<String> keys = tags.getKeys();
                    if (keys.size() != 0) {
                        t.add(new LiteralText(""));

                        for (String key : keys) {
                            String value = tags.get(key).asString();
                            if (value.length()>20) value = value.substring(0,30)+"...";
                            key = key.replaceFirst("hypercube:","");

                            t.add(new LiteralText("§a" + key + " §7= §f" + value));
                        }
                    }
                }

                cir.setReturnValue(t);
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}
