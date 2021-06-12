package io.github.codeutilities.mixin.render;

import io.github.codeutilities.features.social.cosmetics.BlazeCosmeticRenderer;
import io.github.codeutilities.features.social.cosmetics.CosmeticRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class MixinPlayerEntityRenderer extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {


    public MixinPlayerEntityRenderer(
        Context ctx,
        PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    //TODO: figure out what method this changed to
//    @Inject(method = "<init>(Lnet/minecraft/client/render/entity/EntityRenderDispatcher;Z)V", at = @At("RETURN"))
    public void init(EntityRenderDispatcher dispatcher, boolean bl, CallbackInfo ci) {
        this.addFeature(new CosmeticRenderer(this));
        this.addFeature(new BlazeCosmeticRenderer(this));
    }

}