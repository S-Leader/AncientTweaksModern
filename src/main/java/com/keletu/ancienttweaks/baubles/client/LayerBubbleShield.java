package com.keletu.ancienttweaks.baubles.client;

import com.keletu.ancienttweaks.AncientTweaks;
import com.keletu.ancienttweaks.baubles.soulheart.SoulHeartClientHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class LayerBubbleShield extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private static final ResourceLocation SHIELD_TEXTURE = new ResourceLocation(AncientTweaks.MODID, "textures/layer/shield.png");

    private final ModelBubbleShield model;

    public LayerBubbleShield(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent, ModelBubbleShield model) {
        super(parent);
        this.model = model;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (SoulHeartClientHandler.clientPlayerHP <= 0) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(this.getParentModel().body.x / 16.0F, this.getParentModel().body.y / 16.0F, this.getParentModel().body.z / 16.0F);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucentEmissive(SHIELD_TEXTURE));
        this.model.render(poseStack, vertexConsumer, packedLight, 1.0F);

        poseStack.popPose();
    }
}