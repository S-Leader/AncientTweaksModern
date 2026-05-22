package com.keletu.ancienttweaks.baubles;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.keletu.ancienttweaks.AncientTweaks;
import com.keletu.ancienttweaks.AncientTweaksConfig;
import com.keletu.ancienttweaks.block.BlockAbsorber;
import com.keletu.ancienttweaks.init.ATBlocks;
import com.keletu.ancienttweaks.init.ATItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ItemTheAbsorber extends BlockItem implements ICurioItem {

    public static final String TAG_DURATION = "uses";
    public static final UUID SPEED_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-ABCDEF100002");
    public static final UUID ARMOR_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-ABCDEF100004");

    public ItemTheAbsorber(Properties properties) {
        super(ATBlocks.ABSORBER.get(), properties);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();

        if (!(entity instanceof Player player)) {
            return false;
        }

        return CuriosApi.getCuriosInventory(player).map(handler -> handler.findFirstCurio(ATItems.theAbsorber.get()).isEmpty()).orElse(true);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();

        modifiers.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(SPEED_UUID, AncientTweaks.MODID + ":movement_speed", AncientTweaksConfig.CONFIG_CALAMITY.greenJellySpeed.get(), AttributeModifier.Operation.MULTIPLY_TOTAL));

        modifiers.put(Attributes.ARMOR, new AttributeModifier(ARMOR_UUID, AncientTweaks.MODID + ":armor_bonus", AncientTweaksConfig.CONFIG_CALAMITY.theAbsorberShellArmor.get(), AttributeModifier.Operation.ADDITION));

        return modifiers;
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return Rarity.EPIC;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.empty());

        tooltip.add(Component.translatable("tooltip.ancienttweaks.theAbsorber1", Math.round(AncientTweaksConfig.CONFIG_CALAMITY.greenJellySpeed.get() * 100)).withStyle(ChatFormatting.DARK_PURPLE));

        tooltip.add(Component.translatable("tooltip.ancienttweaks.theAbsorber2").withStyle(ChatFormatting.DARK_PURPLE));

        tooltip.add(Component.translatable("tooltip.ancienttweaks.theAbsorber3").withStyle(ChatFormatting.DARK_PURPLE));

        tooltip.add(Component.translatable("tooltip.ancienttweaks.theAbsorber4").withStyle(ChatFormatting.DARK_PURPLE));

        tooltip.add(Component.translatable("tooltip.ancienttweaks.theAbsorber5").withStyle(ChatFormatting.DARK_PURPLE));

        tooltip.add(Component.translatable("tooltip.ancienttweaks.theAbsorber6").withStyle(ChatFormatting.DARK_PURPLE));

        tooltip.add(Component.translatable("tooltip.ancienttweaks.theAbsorber7", Math.round(AncientTweaksConfig.CONFIG_CALAMITY.theAbsorberAbsorb.get() * 100)).withStyle(ChatFormatting.DARK_PURPLE));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() == null) {
            return InteractionResult.PASS;
        }

        if (!context.getPlayer().isShiftKeyDown()) {
            return InteractionResult.PASS;
        }

        if (context.getClickedFace() != Direction.UP) {
            return InteractionResult.PASS;
        }

        return super.useOn(context);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains(TAG_DURATION);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int maxDuration = BlockAbsorber.MAX_DURATION;
        int duration = getDuration(stack);

        return Math.round(13.0F * duration / maxDuration);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        int maxDuration = BlockAbsorber.MAX_DURATION;
        int duration = getDuration(stack);

        float ratio = Math.max(0.0F, Math.min(1.0F, (float) duration / maxDuration));

        return net.minecraft.util.Mth.hsvToRgb(ratio / 3.0F, 1.0F, 1.0F);
    }

    public static int getDuration(ItemStack stack) {
        CompoundTag tag = stack.getTag();

        if (tag == null || !tag.contains(TAG_DURATION)) {
            return BlockAbsorber.MAX_DURATION;
        }

        return tag.getInt(TAG_DURATION);
    }

    public static void setDuration(ItemStack stack, int duration) {
        stack.getOrCreateTag().putInt(TAG_DURATION, duration);
    }
}