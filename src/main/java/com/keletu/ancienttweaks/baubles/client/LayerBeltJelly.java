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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import top.theillusivec4.curios.api.CuriosApi;

import javax.annotation.Nullable;

public class LayerBeltJelly extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private static final ResourceLocation GREEN_JELLY_TEXTURE = new ResourceLocation(AncientTweaks.MODID, "textures/layer/vital_jelly.png");

    private static final ResourceLocation PINK_JELLY_TEXTURE = new ResourceLocation(AncientTweaks.MODID, "textures/layer/life_jelly.png");

    private static final ResourceLocation BLUE_JELLY_TEXTURE = new ResourceLocation(AncientTweaks.MODID, "textures/layer/cleansing_jelly.png");

    private static final ResourceLocation GRAND_GELATIN_TEXTURE = new ResourceLocation(AncientTweaks.MODID, "textures/layer/grand_gelatin.png");

    private final ModelJelly jellyModel;

    public LayerBeltJelly(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent, ModelJelly jellyModel) {
        super(parent);
        this.jellyModel = jellyModel;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (player.isInvisible()) {
            return;
        }

        ResourceLocation texture = getJellyTexture(player);

        if (texture == null) {
            return;
        }

        poseStack.pushPose();

        /*
         * 让饰品跟随玩家身体。
         * 旧版：
         * modelPlayer.bipedBody.postRender(scale);
         *
         * 新版：
         * getParentModel().body.translateAndRotate(poseStack);
         */
        this.getParentModel().body.translateAndRotate(poseStack);

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(texture));

        this.jellyModel.render(poseStack, bufferSource, packedLight, player, partialTicks, texture);

        poseStack.popPose();
    }

    @Nullable
    private ResourceLocation getJellyTexture(AbstractClientPlayer player) {
        if (hasCurio(player, ATItems.greenJelly.get())) {
            return GREEN_JELLY_TEXTURE;
        }

        if (hasCurio(player, ATItems.pinkJelly.get())) {
            return PINK_JELLY_TEXTURE;
        }

        if (hasCurio(player, ATItems.blueJelly.get())) {
            return BLUE_JELLY_TEXTURE;
        }

        if (hasCurio(player, ATItems.grandGelatin.get())) {
            return GRAND_GELATIN_TEXTURE;
        }

        return null;
    }

    private boolean hasCurio(AbstractClientPlayer player, Item item) {
        return CuriosApi.getCuriosInventory(player).map(handler -> handler.findFirstCurio(item).isPresent()).orElse(false);
    }
}
