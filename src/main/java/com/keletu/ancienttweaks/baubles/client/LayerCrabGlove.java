package com.keletu.ancienttweaks.baubles.client;

import com.keletu.ancienttweaks.init.ATItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

public class LayerCrabGlove extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public LayerCrabGlove(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        poseStack.pushPose();

        if (this.getParentModel().young) {
            poseStack.translate(0.0F, 0.75F, 0.0F);
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }

        boolean hasCrawCarapace = hasCurio(player, ATItems.CRAWCARAPACE.get());
        boolean hasBaroClaw = hasCurio(player, ATItems.BAROCLAW.get());

        if (hasCrawCarapace && hasBaroClaw) {
            this.renderHeldItem(player, new ItemStack(ATItems.BAROCLAW.get(), 1), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, HumanoidArm.RIGHT, poseStack, buffer, packedLight);

            this.renderHeldItem(player, new ItemStack(ATItems.CRAWCARAPACE.get(), 1), ItemDisplayContext.THIRD_PERSON_LEFT_HAND, HumanoidArm.LEFT, poseStack, buffer, packedLight);
        } else if (hasBaroClaw) {
            this.renderHeldItem(player, new ItemStack(ATItems.BAROCLAW.get(), 1), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, HumanoidArm.RIGHT, poseStack, buffer, packedLight);
        } else if (hasCrawCarapace) {
            this.renderHeldItem(player, new ItemStack(ATItems.CRAWCARAPACE.get(), 1), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, HumanoidArm.RIGHT, poseStack, buffer, packedLight);
        }

        poseStack.popPose();
    }

    private void renderHeldItem(AbstractClientPlayer player, ItemStack stack, ItemDisplayContext displayContext, HumanoidArm arm, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (stack.isEmpty()) {
            return;
        }

        poseStack.pushPose();

        if (player.isShiftKeyDown()) {
            poseStack.translate(0.0F, 0.2F, 0.0F);
        }

        this.translateToHand(arm, poseStack);

        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

        boolean leftHand = arm == HumanoidArm.LEFT;
        poseStack.translate((leftHand ? -1.0F : 1.0F) / 16.0F, 0.125F, -0.625F);

        Minecraft.getInstance().getItemRenderer().renderStatic(player, stack, displayContext, leftHand, poseStack, buffer, player.level(), packedLight, OverlayTexture.NO_OVERLAY, player.getId());

        poseStack.popPose();
    }

    protected void translateToHand(HumanoidArm arm, PoseStack poseStack) {
        this.getParentModel().translateToHand(arm, poseStack);
    }

    private static boolean hasCurio(AbstractClientPlayer player, net.minecraft.world.item.Item item) {
        return CuriosApi.getCuriosInventory(player).resolve().flatMap(handler -> handler.findFirstCurio(item)).isPresent();
    }
}