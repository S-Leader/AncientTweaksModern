package com.keletu.ancienttweaks.baubles;

import com.keletu.ancienttweaks.AncientTweaksConfig;
import com.keletu.ancienttweaks.init.ATItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ItemGiantTurtleShell extends ShieldItem implements ICurioItem {

    public static final UUID ARMOR_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-ABCDEF100006");

    public static final UUID SPEED_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-ABCDEF100007");

    public static final UUID KNOCKBACK_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-ABCDEF100005");

    public ItemGiantTurtleShell(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();

        if (!(entity instanceof Player player)) {
            return false;
        }

        return CuriosApi.getCuriosInventory(player).map(handler -> handler.findFirstCurio(ATItems.giantTurtleShell.get()).isEmpty()).orElse(true);
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return repairCandidate.is(Items.OBSIDIAN) || super.isValidRepairItem(stack, repairCandidate);
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable("item.ancienttweaks.giant_tortoise_shell");
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.empty());

        tooltip.add(Component.translatable("tooltip.ancienttweaks.giantTortoiseShell1", Math.round(AncientTweaksConfig.CONFIG_CALAMITY.giantTurtleShellArmor.get())).withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.translatable("tooltip.ancienttweaks.giantTortoiseShell2").withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.translatable("tooltip.ancienttweaks.giantTortoiseShell3", Math.round(AncientTweaksConfig.CONFIG_CALAMITY.giantTurtleShellDashSlowness.get() * 100)).withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.translatable("tooltip.ancienttweaks.giantTortoiseShell4", Math.round(AncientTweaksConfig.CONFIG_CALAMITY.giantTurtleShellArmor.get() - AncientTweaksConfig.CONFIG_CALAMITY.giantTurtleShellWeaknessArmor.get())).withStyle(ChatFormatting.DARK_PURPLE));

        tooltip.add(Component.empty());
    }
}
