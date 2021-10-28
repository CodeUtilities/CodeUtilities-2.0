package io.github.codeutilities.mod.mixin.render;

import io.github.codeutilities.mod.config.Config;
import io.github.codeutilities.mod.features.commands.CodeSearcher;
import io.github.codeutilities.sys.networking.State;
import io.github.codeutilities.sys.player.DFInfo;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SignBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer.SignModel;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.SignType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SignBlockEntityRenderer.class)
public abstract class MSignRenderer {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    @Shadow
    public static SignType getSignType(Block block) {
        return null;
    }

    @Shadow @Final private Map<SignType, SignModel> typeToModel;

    /**
     * @author CodeUtilities
     * @reason yea
     */
    @Overwrite
    public void render(SignBlockEntity signBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        if (!signBlockEntity.getPos().isWithinDistance(mc.cameraEntity.getBlockPos(), Config.getInteger("signRenderDistance")))
            return;

        TextRenderer textRenderer = mc.textRenderer;

        SignModel model = typeToModel.get(getSignType(signBlockEntity.getCachedState().getBlock()));

        if (CodeSearcher.shouldGlow(signBlockEntity) && DFInfo.currentState.getMode() == State.Mode.DEV && mc.player.isCreative()) {
            double distance = Math.sqrt(signBlockEntity.getPos().getSquaredDistance(mc.cameraEntity.getBlockPos()));
            double dist = MathHelper.clamp(distance, 1, 15);

            OutlineVertexConsumerProvider outlineVertexConsumerProvider = mc.getBufferBuilders().getOutlineVertexConsumers();
            outlineVertexConsumerProvider.setColor(255, 255, 255, (int) (dist * 17));
            vertexConsumerProvider = outlineVertexConsumerProvider;
        }

        BlockState blockState = signBlockEntity.getCachedState();
        matrixStack.push();
        float h;
        if (blockState.getBlock() instanceof SignBlock) {
            matrixStack.translate(0.5D, 0.5D, 0.5D);
            h = -((float) (blockState.get(SignBlock.ROTATION) * 360) / 16.0F);
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(h));
            model.stick.visible = true;
        } else {
            matrixStack.translate(0.5D, 0.5D, 0.5D);
            h = -blockState.get(WallSignBlock.FACING).asRotation();
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(h));
            matrixStack.translate(0.0D, -0.3125D, -0.4375D);
            model.stick.visible = false;
        }

        matrixStack.push();
        matrixStack.scale(0.6666667F, -0.6666667F, -0.6666667F);
        SpriteIdentifier spriteIdentifier =  TexturedRenderLayers.getSignTextureId(getSignType(blockState.getBlock()));
        SignBlockEntityRenderer.SignModel var10002 = model;
        var10002.getClass();
        VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumerProvider, var10002::getLayer);
        model.root.render(matrixStack, vertexConsumer, i, j);
        model.stick.render(matrixStack, vertexConsumer, i, j);
        matrixStack.pop();
        matrixStack.translate(0.0D, 0.3333333432674408D, 0.046666666865348816D);
        matrixStack.scale(0.010416667F, -0.010416667F, 0.010416667F);
        int m = signBlockEntity.getTextColor().getSignColor();
        int n = (int) ((double) NativeImage.getRed(m) * 0.4D);
        int o = (int) ((double) NativeImage.getGreen(m) * 0.4D);
        int p = (int) ((double) NativeImage.getBlue(m) * 0.4D);
        int q = NativeImage.packColor(0, p, o, n);

        for (int s = 0; s < 4; ++s) {
            Text text = signBlockEntity.getTextOnRow(s,false);
            if (text != null) {
                float t = (float) (-textRenderer.getWidth(text) / 2);
                textRenderer.draw(text, t, (float) (s * 10 - 20), q, false, matrixStack.peek().getModel(), vertexConsumerProvider, false, 0, i);
            }
        }

        matrixStack.pop();
    }

}
