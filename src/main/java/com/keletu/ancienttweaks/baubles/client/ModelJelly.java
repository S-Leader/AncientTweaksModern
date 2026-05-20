package com.keletu.ancienttweaks.baubles.client;

import com.keletu.ancienttweaks.AncientTweaks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;

public class ModelJelly extends EntityModel<AbstractClientPlayer> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(AncientTweaks.MODID, "jelly_belt"), "main");

    private final ModelPart belt;
    private final ModelPart jar;
    private final ModelPart lid;
    private final ModelPart cloud1;
    private final ModelPart cloud2;

    public ModelJelly(ModelPart root) {
        this.belt = root.getChild("belt");
        this.jar = root.getChild("jar");
        this.lid = root.getChild("lid");
        this.cloud1 = root.getChild("cloud1");
        this.cloud2 = root.getChild("cloud2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();

        /*
         * 旧版：
         * belt = new ModelRenderer(this, 0, 0);
         * belt.addBox(-4, 0, -2, 8, 12, 4);
         */
        root.addOrReplaceChild("belt", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F), PartPose.ZERO);

        /*
         * 旧版：
         * jar = new ModelRenderer(this, 0, 16);
         * jar.addBox(0, 0, 0, 7, 9, 7);
         */
        root.addOrReplaceChild("jar", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, 0.0F, 0.0F, 7.0F, 9.0F, 7.0F), PartPose.ZERO);

        /*
         * 旧版：
         * lid = new ModelRenderer(this, 44, 0);
         * lid.addBox(1, -1F, 1, 5, 1, 5);
         */
        root.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(44, 0).addBox(1.0F, -1.0F, 1.0F, 5.0F, 1.0F, 5.0F), PartPose.ZERO);

        /*
         * 旧版：
         * cloud1 = new ModelRenderer(this, 24, 0);
         * cloud1.addBox(-2.5F, -2.5F, -2.5F, 5, 5, 5);
         */
        root.addOrReplaceChild("cloud1", CubeListBuilder.create().texOffs(24, 0).addBox(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F), PartPose.ZERO);

        /*
         * 旧版：
         * cloud2 = new ModelRenderer(this, 24, 10);
         * cloud2.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3);
         */
        root.addOrReplaceChild("cloud2", CubeListBuilder.create().texOffs(24, 10).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F), PartPose.ZERO);

        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    @Override
    public void setupAnim(AbstractClientPlayer entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        /*
         * 这里留空。
         * 因为这个模型需要 player、partialTicks、texture，
         * 所以实际渲染走下面的自定义 render 方法。
         */
    }

    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, AbstractClientPlayer player, float partialTicks, ResourceLocation texture) {
        VertexConsumer opaqueBuffer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(texture));
        VertexConsumer translucentBuffer = bufferSource.getBuffer(RenderType.entityTranslucent(texture));

        boolean hasPants = !player.getItemBySlot(EquipmentSlot.LEGS).isEmpty();

        float time = player.tickCount + partialTicks;

        poseStack.pushPose();

        /*
         * 旧版：
         * GlStateManager.scale(7 / 6F, 7 / 6F, 7 / 6F);
         */
        poseStack.scale(7.0F / 6.0F, 7.0F / 6.0F, 7.0F / 6.0F);

        /*
         * 旧版：
         * GlStateManager.scale(1, 1, hasPants ? 1.2F : 1.1F);
         * belt.render(scale);
         */
        poseStack.pushPose();
        poseStack.scale(1.0F, 1.0F, hasPants ? 1.2F : 1.1F);

        this.belt.render(poseStack, opaqueBuffer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();

        /*
         * 旧版：
         * GlStateManager.scale(1/2F, 1/2F, 1/2F);
         * GlStateManager.translate(0, 1, -2/3F);
         * GlStateManager.translate(1/5F, 0, 0);
         * GlStateManager.rotate(-15, 0, 1, 0);
         */
        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.translate(0.0D, 1.0D, -2.0D / 3.0D);
        poseStack.translate(1.0D / 5.0D, 0.0D, 0.0D);
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-15.0F));

        this.jar.render(poseStack, opaqueBuffer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        this.lid.render(poseStack, opaqueBuffer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        /*
         * 旧版：
         * GlStateManager.color(1, 1, 1, 0.5F);
         * GlStateManager.translate(3.5F/16F, 4.5F/16F, 3.5F/16F);
         */
        poseStack.translate(3.5D / 16.0D, 4.5D / 16.0D, 3.5D / 16.0D);

        /*
         * cloud2 动画。
         */
        poseStack.pushPose();
        poseStack.translate(0.0D, 1.0D / 16.0D * Math.sin(0.05D * time), 0.0D);
        poseStack.scale(1.0F + 0.1F * (float) Math.sin(0.03D * time), 0.8F + 0.1F * (float) Math.sin(0.032D * time), 0.8F + 0.1F * (float) Math.sin(0.034D * time));
        this.cloud2.render(poseStack, translucentBuffer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.5F);

        poseStack.popPose();

        /*
         * cloud1 动画。
         */
        poseStack.translate(0.0D, 1.0D / 16.0D * Math.sin(0.05D * time + 0.5D), 0.0D);

        poseStack.scale(1.0F + 0.1F * (float) Math.sin(0.033D * time), 0.8F + 0.1F * (float) Math.sin(0.031D * time), 0.8F + 0.1F * (float) Math.sin(0.034D * time));

        this.cloud1.render(poseStack, translucentBuffer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.5F);

        poseStack.popPose();
    }
}