package com.keletu.ancienttweaks.baubles.client;

import com.keletu.ancienttweaks.AncientTweaks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;

public class ModelAbsorber extends EntityModel<Player> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(AncientTweaks.MODID, "absorber"), "main");

    private final ModelPart backpack;

    public ModelAbsorber(ModelPart root) {
        this.backpack = root.getChild("backpack");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("backpack", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -1.0F, 0.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(-0.75F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(mesh, 48, 32);
    }

    @Override
    public void setupAnim(Player player, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // 动画由 Layer 里复制 PlayerModel body 姿态处理
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.backpack.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    /**
     * 让背包跟随玩家身体旋转。
     */
    public void setupAngles(PlayerModel<?> playerModel) {
        copyProperties(playerModel.body, this.backpack);
    }

    private static void copyProperties(ModelPart source, ModelPart target) {
        target.xRot = source.xRot;
        target.yRot = source.yRot;
        target.zRot = source.zRot;

        target.x = source.x;
        target.y = source.y;
        target.z = source.z;
    }

    /**
     * 对应旧版 render 里的特殊位移。
     */
    public void render(Player player, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float alpha) {
        poseStack.pushPose();

        if (player.isCrouching()) {
            poseStack.translate(0.0F, 0.2F, 0.0F);
        }

        if (!player.getItemBySlot(EquipmentSlot.CHEST).isEmpty()) {
            poseStack.translate(0.0F, 0.0F, 0.1F);
        }

        this.backpack.render(poseStack, vertexConsumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, alpha);

        poseStack.popPose();
    }
}