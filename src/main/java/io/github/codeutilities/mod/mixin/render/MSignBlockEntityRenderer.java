package io.github.codeutilities.mod.mixin.render;

import io.github.codeutilities.mod.features.TemplatePeeker;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory.Context;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SignBlockEntityRenderer.class)
public class MSignBlockEntityRenderer {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void constructor(Context ctx, CallbackInfo ci) {
        TemplatePeeker.berfContext = ctx;
    }

}
