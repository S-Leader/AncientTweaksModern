package com.keletu.ancienttweaks.baubles;

import com.keletu.ancienttweaks.AncientTweaksConfig;
import com.keletu.ancienttweaks.baubles.soulheart.SoulHeartHandler;
import com.keletu.ancienttweaks.event.TheSpongeEvents;
import com.keletu.ancienttweaks.init.ATItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
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

public class ItemTheSponge extends Item implements ICurioItem {

    public ItemTheSponge(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();

        if (!(entity instanceof Player player)) {
            return false;
        }

        boolean hasSponge = CuriosApi.getCuriosInventory(player).map(handler -> handler.findFirstCurio(ATItems.theSponge.get()).isPresent()).orElse(false);

        boolean hasRoverDrive = CuriosApi.getCuriosInventory(player).map(handler -> handler.findFirstCurio(ATItems.roverDrive.get()).isPresent()).orElse(false);

        return !hasSponge && !hasRoverDrive;
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity entity = slotContext.entity();

        if (entity instanceof Player player && !player.level().isClientSide) {
            TheSpongeEvents.removeBubbleArmor(player);

            SoulHeartHandler.setHP(player, Math.min(SoulHeartHandler.getMaxHP(player), SoulHeartHandler.getHP(player)));
        }
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return Rarity.EPIC;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.empty());

        tooltip.add(Component.translatable("tooltip.ancienttweaks.theSponge1", AncientTweaksConfig.CONFIG_CALAMITY.theSpongeShieldCount.get()).withStyle(ChatFormatting.GOLD));

        tooltip.add(Component.translatable("tooltip.ancienttweaks.theSponge2", Math.round(AncientTweaksConfig.CONFIG_CALAMITY.theSpongeDamageDiscount.get() * 100)).withStyle(ChatFormatting.GOLD));

        tooltip.add(Component.empty());

        tooltip.add(Component.translatable("tooltip.ancienttweaks.theSponge3", AncientTweaksConfig.CONFIG_CALAMITY.theSpongeShieldCooldown.get()).withStyle(ChatFormatting.GOLD));

        tooltip.add(Component.translatable("tooltip.ancienttweaks.theSponge4").withStyle(ChatFormatting.GOLD));

        tooltip.add(Component.empty());
    }
}
