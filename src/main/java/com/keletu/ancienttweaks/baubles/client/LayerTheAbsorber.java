package com.keletu.ancienttweaks.baubles.client;

import com.keletu.ancienttweaks.AncientTweaks;
import com.keletu.ancienttweaks.init.ATItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import top.theillusivec4.curios.api.CuriosApi;

public class LayerTheAbsorber extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(AncientTweaks.MODID, "textures/layer/the_absorber_model.png");

    private final ModelAbsorber model;

    public LayerTheAbsorber(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent, ModelAbsorber model) {
        super(parent);
        this.model = model;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        if (!hasTheAbsorber(player)) {
            return;
        }

        poseStack.pushPose();

        /*
         * 旧版：
         * model.setModelAttributes(renderer.getMainModel());
         * model.setupAngles(renderer.getMainModel());
         *
         * 新版这里让 ModelAbsorber 跟随玩家 body 姿态。
         */
        this.model.setupAngles(this.getParentModel());

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));

        this.model.render(player, poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F);

        poseStack.popPose();
    }

    private static boolean hasTheAbsorber(AbstractClientPlayer player) {
        return CuriosApi.getCuriosInventory(player).resolve().flatMap(handler -> handler.findFirstCurio(ATItems.theAbsorber.get())).isPresent();
    }
}