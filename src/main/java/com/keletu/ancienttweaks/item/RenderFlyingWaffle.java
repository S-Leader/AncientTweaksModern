/*package com.keletu.ancienttweaks.item;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderFlyingWaffle extends Render<EntityWaffle> {
    private final RenderItem itemRenderer;

    public RenderFlyingWaffle(RenderManager renderManagerIn, RenderItem itemRendererIn) {
        super(renderManagerIn);
        this.itemRenderer = itemRendererIn;
    }

    @Override
    public void doRender(EntityWaffle entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        double posX = x, posY = y, posZ = z;
        if (entity.isAirBorne) {
            posX += (entity.motionX * partialTicks);
            posY += (entity.motionY * partialTicks);
            posZ += (entity.motionZ * partialTicks);
        }

        GlStateManager.translate((float) posX, (float) posY + 0.5f, (float) posZ);
        GlStateManager.scale(2.0d, 2.0d, 2.0d);
        GlStateManager.enableRescaleNormal();

        this.doRenderTransformations(entity, partialTicks);

        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        ItemStack weapon = new ItemStack(ItemRegister.waffleItem);
        this.itemRenderer.renderItem(weapon, ItemCameraTransforms.TransformType.GROUND);

        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected void doRenderTransformations(EntityWaffle entity, float partialTicks) {
        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks - 90.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(180.0f, 1.0f, 1.0f, 0.0f);

        GlStateManager.translate(-0.05d, -0.25d, 0.0d);
        if (entity.ticksExisted != 0.0f) {
            float rotation = (entity.ticksExisted + partialTicks) * 30.0f % 360.0f;
            GlStateManager.rotate(rotation, 0.0f, 0.0f, 1.0f);
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityWaffle entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}*/