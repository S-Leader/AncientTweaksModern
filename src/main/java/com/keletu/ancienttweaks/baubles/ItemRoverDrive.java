package com.keletu.ancienttweaks.baubles;

import com.keletu.ancienttweaks.AncientTweaksConfig;
import com.keletu.ancienttweaks.baubles.soulheart.SoulHeartHandler;
import com.keletu.ancienttweaks.event.RoverDriveEvents;
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

public class ItemRoverDrive extends Item implements ICurioItem {

    public ItemRoverDrive(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.RARE));
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();

        if (!(entity instanceof Player player)) {
            return false;
        }

        boolean hasRoverDrive = CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.findFirstCurio(ATItems.roverDrive.get()).isPresent())
                .orElse(false);

        boolean hasSponge = CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.findFirstCurio(ATItems.theSponge.get()).isPresent())
                .orElse(false);

        return !hasRoverDrive && !hasSponge;
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity entity = slotContext.entity();

        if (entity instanceof Player player && !player.level().isClientSide) {
            RoverDriveEvents.removeBubbleArmor(player);

            int hp = SoulHeartHandler.getHP(player);
            int max = SoulHeartHandler.getMaxHP(player);

            SoulHeartHandler.setHP(player, Math.min(max, hp));
        }
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return Rarity.RARE;
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            @Nullable Level level,
            List<Component> tooltip,
            TooltipFlag flag
    ) {
        tooltip.add(Component.empty());

        tooltip.add(Component.translatable(
                "tooltip.ancienttweaks.roverDrive1",
                AncientTweaksConfig.CONFIG_CALAMITY.roverDriveShieldCount.get()
        ).withStyle(ChatFormatting.DARK_GRAY));

        tooltip.add(Component.empty());

        tooltip.add(Component.translatable(
                "tooltip.ancienttweaks.roverDrive2",
                AncientTweaksConfig.CONFIG_CALAMITY.roverDriveShieldCooldown.get()
        ).withStyle(ChatFormatting.DARK_GRAY));

        tooltip.add(Component.translatable(
                "tooltip.ancienttweaks.roverDrive3"
        ).withStyle(ChatFormatting.DARK_GRAY));

        tooltip.add(Component.empty());
    }
}
