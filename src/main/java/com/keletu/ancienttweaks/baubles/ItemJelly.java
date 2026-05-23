package com.keletu.ancienttweaks.baubles;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.keletu.ancienttweaks.AncientTweaksConfig;
import com.keletu.ancienttweaks.init.ATItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ItemJelly extends Item implements ICurioItem {

    public static final UUID SPEED_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-ABCDEF100002");

    public ItemJelly(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();

        if (!(entity instanceof Player player)) {
            return false;
        }

        Item item = stack.getItem();

        return CuriosApi.getCuriosInventory(player).map(handler -> handler.findFirstCurio(item).isEmpty()).orElse(true);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();

        if (stack.is(ATItems.greenJelly.get()) || stack.is(ATItems.grandGelatin.get())) {
            modifiers.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(SPEED_UUID, "ancienttweaks:movement_speed", AncientTweaksConfig.CONFIG_CALAMITY.greenJellySpeed.get(), AttributeModifier.Operation.MULTIPLY_TOTAL));
        }

        return modifiers;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.empty());

        if (stack.is(ATItems.blueJelly.get())) {
            tooltip.add(Component.translatable("tooltip.ancienttweaks.blueJelly1").withStyle(ChatFormatting.AQUA));

            tooltip.add(Component.translatable("tooltip.ancienttweaks.blueJelly2").withStyle(ChatFormatting.AQUA));
        }

        if (stack.is(ATItems.pinkJelly.get())) {
            tooltip.add(Component.translatable("tooltip.ancienttweaks.pinkJelly1").withStyle(ChatFormatting.LIGHT_PURPLE));

            tooltip.add(Component.translatable("tooltip.ancienttweaks.pinkJelly2").withStyle(ChatFormatting.LIGHT_PURPLE));
        }

        if (stack.is(ATItems.greenJelly.get())) {
            tooltip.add(Component.translatable("tooltip.ancienttweaks.greenJelly1", Math.round(AncientTweaksConfig.CONFIG_CALAMITY.greenJellySpeed.get() * 100)).withStyle(ChatFormatting.GREEN));
        }

        if (stack.is(ATItems.grandGelatin.get())) {
            tooltip.add(Component.translatable("tooltip.ancienttweaks.grandGelatin1", Math.round(AncientTweaksConfig.CONFIG_CALAMITY.greenJellySpeed.get() * 100)).withStyle(ChatFormatting.DARK_PURPLE));

            tooltip.add(Component.translatable("tooltip.ancienttweaks.grandGelatin2").withStyle(ChatFormatting.DARK_PURPLE));

            tooltip.add(Component.translatable("tooltip.ancienttweaks.grandGelatin3").withStyle(ChatFormatting.DARK_PURPLE));
        }
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        if (stack.is(ATItems.grandGelatin.get())) {
            return Rarity.RARE;
        }

        return Rarity.UNCOMMON;
    }
}