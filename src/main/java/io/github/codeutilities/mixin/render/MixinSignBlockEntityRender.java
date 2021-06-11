//TODO: Update to 1.17

//package io.github.codeutilities.mixin.render;
//
//import io.github.codeutilities.config.Config;
//import io.github.codeutilities.util.networking.DFInfo;
//import io.github.codeutilities.util.networking.State;
//import io.github.codeutilities.util.templates.SearchUtil;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.SignBlock;
//import net.minecraft.block.WallSignBlock;
//import net.minecraft.block.entity.SignBlockEntity;
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.font.TextRenderer;
//import net.minecraft.client.network.ClientPlayerEntity;
//import net.minecraft.client.render.OutlineVertexConsumerProvider;
//import net.minecraft.client.render.TexturedRenderLayers;
//import net.minecraft.client.render.VertexConsumer;
//import net.minecraft.client.render.VertexConsumerProvider;
//import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
//import net.minecraft.client.texture.NativeImage;
//import net.minecraft.client.util.SpriteIdentifier;
//import net.minecraft.client.util.math.MatrixStack;
//import net.minecraft.entity.Entity;
//import net.minecraft.text.OrderedText;
//import net.minecraft.util.DyeColor;
//import net.minecraft.util.SignType;
//import net.minecraft.util.math.Direction;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.util.math.Vec3d;
//import net.minecraft.util.math.Vec3f;
//import org.spongepowered.asm.mixin.Final;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Overwrite;
//import org.spongepowered.asm.mixin.Shadow;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//
//import static net.minecraft.client.render.block.entity.SignBlockEntityRenderer.*;
//
//@Mixin(SignBlockEntityRenderer.class)
//public class MixinSignBlockEntityRender {
//
//    private final MinecraftClient mc = MinecraftClient.getInstance();
//    private static final int RENDER_DISTANCE = MathHelper.square(16);
//
//    @Shadow
//    @Final
//    private final Map<SignType, SignBlockEntityRenderer.SignModel> typeToModel;
//
//    /**
//     * @author CodeUtilities
//     * @reason yea
//     */
//    @Overwrite
//    public void render(SignBlockEntity signBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
//        if (!signBlockEntity.getPos().isWithinDistance(mc.cameraEntity.getBlockPos(), Config.getInteger("signRenderDistance")))
//            return;
//
//        TextRenderer textRenderer = mc.textRenderer;
//
//        if (SearchUtil.shouldGlow(signBlockEntity) && DFInfo.currentState.getMode() == State.Mode.DEV && mc.player.isCreative()) {
//            double distance = Math.sqrt(signBlockEntity.getPos().getSquaredDistance(mc.cameraEntity.getBlockPos()));
//            double dist = MathHelper.clamp(distance, 1, 15);
//
//            OutlineVertexConsumerProvider outlineVertexConsumerProvider = mc.getBufferBuilders().getOutlineVertexConsumers();
//            outlineVertexConsumerProvider.setColor(255, 255, 255, (int) (dist * 17));
//            vertexConsumerProvider = outlineVertexConsumerProvider;
//        }
//
//        BlockState blockState = signBlockEntity.getCachedState();
//        matrixStack.push();
//        SignType signType = getSignType(blockState.getBlock());
//        SignModel signModel = (SignModel)this.typeToModel.get(signType);
//        float h;
//        if (blockState.getBlock() instanceof SignBlock) {
//            matrixStack.translate(0.5D, 0.5D, 0.5D);
//            h = -((float)((Integer)blockState.get(SignBlock.ROTATION) * 360) / 16.0F);
//            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(h));
//            signModel.stick.visible = true;
//        } else {
//            matrixStack.translate(0.5D, 0.5D, 0.5D);
//            h = -((Direction)blockState.get(WallSignBlock.FACING)).asRotation();
//            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(h));
//            matrixStack.translate(0.0D, -0.3125D, -0.4375D);
//            signModel.stick.visible = false;
//        }
//
//        matrixStack.push();
//        matrixStack.scale(0.6666667F, -0.6666667F, -0.6666667F);
//        SpriteIdentifier spriteIdentifier = TexturedRenderLayers.getSignTextureId(signType);
//        Objects.requireNonNull(signModel);
//        VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumerProvider, signModel::getLayer);
//        signModel.root.render(matrixStack, vertexConsumer, i, j);
//        matrixStack.pop();
//        matrixStack.translate(0.0D, 0.3333333432674408D, 0.046666666865348816D);
//        matrixStack.scale(0.010416667F, -0.010416667F, 0.010416667F);
//        int m = getColor(signBlockEntity);
//        OrderedText[] orderedTexts = signBlockEntity.updateSign(MinecraftClient.getInstance().shouldFilterText(), (text) -> {
//            List<OrderedText> list = textRenderer.wrapLines(text, 90);
//            return list.isEmpty() ? OrderedText.EMPTY : (OrderedText)list.get(0);
//        });
//        int q;
//        boolean bl2;
//        int r;
//        if (signBlockEntity.isGlowingText()) {
//            q = signBlockEntity.getTextColor().getSignColor();
//            bl2 = shouldRender(signBlockEntity, q);
//            r = 15728880;
//        } else {
//            q = m;
//            bl2 = false;
//            r = i;
//        }
//
//        for(int s = 0; s < 4; ++s) {
//            OrderedText orderedText = orderedTexts[s];
//            float t = (float)(-textRenderer.getWidth(orderedText) / 2);
//            if (bl2) {
//                textRenderer.method_37296(orderedText, t, (float)(s * 10 - 20), q, m, matrixStack.peek().getModel(), vertexConsumerProvider, r);
//            } else {
//                textRenderer.draw(orderedText, t, (float)(s * 10 - 20), q, false, matrixStack.peek().getModel(), vertexConsumerProvider, false, 0, r);
//            }
//        }
//
//        matrixStack.pop();
//    }
//
//    private static int getColor(SignBlockEntity sign) {
//        int i = sign.getTextColor().getSignColor();
//        double d = 0.4D;
//        int j = (int)((double)NativeImage.getRed(i) * 0.4D);
//        int k = (int)((double)NativeImage.getGreen(i) * 0.4D);
//        int l = (int)((double)NativeImage.getBlue(i) * 0.4D);
//        return i == DyeColor.BLACK.getSignColor() && sign.isGlowingText() ? -988212 : NativeImage.getAbgrColor(0, l, k, j);
//    }
//
//    private static boolean shouldRender(SignBlockEntity sign, int signColor) {
//        if (signColor == DyeColor.BLACK.getSignColor()) {
//            return true;
//        } else {
//            MinecraftClient minecraftClient = MinecraftClient.getInstance();
//            ClientPlayerEntity clientPlayerEntity = minecraftClient.player;
//            if (clientPlayerEntity != null && minecraftClient.options.getPerspective().isFirstPerson() && clientPlayerEntity.isUsingSpyglass()) {
//                return true;
//            } else {
//                Entity entity = minecraftClient.getCameraEntity();
//                return entity != null && entity.squaredDistanceTo(Vec3d.ofCenter(sign.getPos())) < (double)RENDER_DISTANCE;
//            }
//        }
//    }
//}
