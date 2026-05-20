/*package com.keletu.ancienttweaks.item;

import com.google.common.collect.Multimap;
import com.keletu.ancienttweaks.AncientTweaks;
import com.keletu.ancienttweaks.packet.PacketLaunchWaffle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.vecmath.Vector3f;
import java.util.List;

public class ItemWafflesIron extends Item {

    public ItemWafflesIron() {
        this.setRegistryName(new ResourceLocation(AncientTweaks.MODID, "waffles_iron"));
        this.setTranslationKey(AncientTweaks.MODID + "." + "waffles_iron");
        this.setMaxDamage(888);
        this.setCreativeTab(AncientTweaks.tabAncientTweaks);
        this.setMaxStackSize(1);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(slot);

        if (slot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 7.0F, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4F, 0));
        }

        return multimap;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (attacker.world.rand.nextDouble() < 0.0001) {
            attacker.world.playSound(null, attacker.posX, attacker.posY, attacker.posZ, SoundEvents.BLOCK_ANVIL_HIT, SoundCategory.PLAYERS, 1F, attacker.world.rand.nextFloat() * 0.2F + 0.4F);
        } else {
            attacker.world.playSound(null, attacker.posX, attacker.posY, attacker.posZ, SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.PLAYERS, 0.5F, attacker.world.rand.nextFloat() * 0.1F + 0.45F);
        }

        if (itemRand.nextInt() <= 1 / 3) {
            target.setFire(5);
        }

        stack.damageItem(1, attacker);

        return true;
    }

    public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (state.getBlockHardness(world, pos) != 0.0F) {
            stack.damageItem(2, entityLiving);
        }

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.ITALIC + I18n.format("item.waffles_iron.tooltip"));
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == Items.IRON_INGOT || super.getIsRepairable(toRepair, repair);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        return 23;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return Items.DIAMOND_SWORD.isBookEnchantable(new ItemStack(Items.DIAMOND_SWORD), book);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return Items.DIAMOND_SWORD.canApplyAtEnchantingTable(new ItemStack(Items.DIAMOND_SWORD), enchantment);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Override
    public Entity createEntity(World world, Entity location, ItemStack stack) {
        EntityUnbreakableItem item = new EntityUnbreakableItem(world, location.posX, location.posY, location.posZ, stack);
        item.setPickupDelay(20);
        item.motionX = location.motionX;
        item.motionY = location.motionY;
        item.motionZ = location.motionZ;
        if (location instanceof EntityItem) {
            item.setThrower(((EntityItem) location).getThrower());
            item.setOwner(((EntityItem) location).getOwner());
        }

        return item;
    }

    public void spawnWaffle(EntityPlayer playerEntity) {
        if (!playerEntity.getHeldItemMainhand().isEmpty() && playerEntity.getHeldItemMainhand().getItem() == this && playerEntity.getCooledAttackStrength(0) == 1) {
            ItemStack stack = playerEntity.getHeldItemMainhand();
            final Multimap<String, AttributeModifier> dmg = stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
            double totalDmg = 0D;
            double multiplyBase = 1D;
            double multiply = 1D;
            for (AttributeModifier modifier : dmg.get(SharedMonsterAttributes.ATTACK_DAMAGE.getName())) {
                if (modifier.getOperation() == 0) {
                    totalDmg += modifier.getAmount();
                } else if (modifier.getOperation() == 1) {
                    multiplyBase += modifier.getAmount();
                } else if (modifier.getOperation() == 2) {
                    multiply *= 1D + modifier.getAmount();
                }
            }
            totalDmg *= multiplyBase;
            totalDmg *= multiply;
            EntityWaffle shot = new EntityWaffle(playerEntity.world, playerEntity, totalDmg * 0.85F);
            Vec3d vector3d = playerEntity.getLook(1.0F);
            Vector3f vector3f = new Vector3f((float) vector3d.x, (float) vector3d.y, (float) vector3d.z);
            shot.shoot(vector3f.x, vector3f.y, vector3f.z, 2.0F, 0.5F);
            playerEntity.world.spawnEntity(shot);
            playerEntity.getCooldownTracker().setCooldown(stack.getItem(), 10);
        }
    }

    public void onLeftClick(final EntityPlayer playerEntity) {
        if (playerEntity.getHeldItemMainhand().getItem() == this && !playerEntity.world.isRemote) {
            spawnWaffle(playerEntity);
        }
    }

    @SubscribeEvent
    public void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        if (!event.getItemStack().isEmpty() && event.getItemStack().getItem() == this) {
            AncientTweaks.packetInstance.sendToServer(new PacketLaunchWaffle());
        }
    }
}*/