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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Optional;

public class LayerShieldBack extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public LayerShieldBack(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        poseStack.pushPose();

        if (this.getParentModel().young) {
            poseStack.translate(0.0F, 0.75F, 0.0F);
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }

        Optional<ItemStack> turtleShell = findCurio(player, ATItems.giantTurtleShell.get());
        Optional<ItemStack> giantShell = findCurio(player, ATItems.GIANTSHELL.get());

        if (turtleShell.isPresent()) {
            this.renderBackItem(player, turtleShell.get(), poseStack, buffer, packedLight);
        } else if (giantShell.isPresent()) {
            this.renderBackItem(player, giantShell.get(), poseStack, buffer, packedLight);
        }

        poseStack.popPose();
    }

    private void renderBackItem(AbstractClientPlayer player, ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (stack.isEmpty()) {
            return;
        }

        poseStack.pushPose();

        boolean armor = !player.getItemBySlot(EquipmentSlot.CHEST).isEmpty();

        if (player.isShiftKeyDown()) {
            poseStack.translate(0.0F, 0.2F, 0.0F);
        }

        this.translateToBody(poseStack);

        poseStack.scale(0.4F, 0.4F, 0.4F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        poseStack.translate(0.0F, -0.8F, armor ? 0.03F : -0.02F);

        Minecraft.getInstance().getItemRenderer().renderStatic(player, stack, ItemDisplayContext.HEAD, false, poseStack, buffer, player.level(), packedLight, OverlayTexture.NO_OVERLAY, player.getId());

        poseStack.popPose();
    }

    protected void translateToBody(PoseStack poseStack) {
        this.getParentModel().body.translateAndRotate(poseStack);
    }

    private static Optional<ItemStack> findCurio(AbstractClientPlayer player, Item item) {
        return CuriosApi.getCuriosInventory(player).resolve().flatMap(handler -> handler.findFirstCurio(item)).map(slotResult -> slotResult.stack());
    }
}