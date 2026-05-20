package com.keletu.ancienttweaks.baubles;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.keletu.ancienttweaks.AncientTweaks;
import com.keletu.ancienttweaks.AncientTweaksConfig;
import com.keletu.ancienttweaks.init.ATEffects;
import com.keletu.ancienttweaks.init.ATItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ItemBaroClaw extends Item implements ICurioItem {

    private static final UUID ATTACK_DAMAGE_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-ABCDEF100001");

    private static final UUID ARMOR_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-ABCDEF100004");

    public ItemBaroClaw(Properties properties) {
        super(properties);

        MinecraftForge.EVENT_BUS.register(this);
    }

    /**
     * 控制是否能装备。
     * 这里的逻辑是：玩家身上没有 baroClaw 时才允许装备。
     */
    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();

        if (!(entity instanceof Player player)) {
            return false;
        }

        return !hasBaroClaw(player);
    }

    /**
     * Curios 1.20.1 推荐用这个方法给饰品加属性。
     * 不需要像 1.12 Baubles 那样 onEquipped 里手动 applyAttributeModifiers。
     */
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();

        modifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_UUID, AncientTweaks.MODID + ":attack_bonus", AncientTweaksConfig.CONFIG_CALAMITY.baroclawAttackBonus.get(), AttributeModifier.Operation.MULTIPLY_TOTAL));

        modifiers.put(Attributes.ARMOR, new AttributeModifier(ARMOR_UUID, AncientTweaks.MODID + ":armor_bonus", AncientTweaksConfig.CONFIG_CALAMITY.baroclawArmorBonus.get(), AttributeModifier.Operation.MULTIPLY_TOTAL));

        return modifiers;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.empty());

        tooltip.add(Component.translatable("tooltip.ancienttweaks.baroclaw1", Math.round(AncientTweaksConfig.CONFIG_CALAMITY.baroclawAttackBonus.get() * 100)).withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.translatable("tooltip.ancienttweaks.baroclaw2", Math.round(AncientTweaksConfig.CONFIG_CALAMITY.baroclawArmorBonus.get() * 100)).withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.translatable("tooltip.ancienttweaks.baroclaw3").withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.translatable("tooltip.ancienttweaks.baroclaw4").withStyle(ChatFormatting.DARK_PURPLE));
    }

    @SubscribeEvent
    public void onEntityHurt(LivingHurtEvent event) {
        if (event.getAmount() >= Float.MAX_VALUE) {
            return;
        }

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!hasBaroClaw(player)) {
            return;
        }

        Entity sourceEntity = event.getSource().getEntity();
        Entity directEntity = event.getSource().getDirectEntity();

        if (!(sourceEntity instanceof LivingEntity attacker)) {
            return;
        }

        /*
         * 避免反伤再次触发反伤。
         * 同时要求直接伤害来源就是攻击者本体，避免箭、火球等远程来源也触发。
         */
        if (event.getSource().is(DamageTypes.THORNS)) {
            return;
        }

        if (directEntity != attacker) {
            return;
        }

        float reflectedDamage = event.getAmount() * AncientTweaksConfig.CONFIG_CALAMITY.baroclawReflectDamage.get().floatValue();

        DamageSource thornsDamage = player.damageSources().thorns(player);

        attacker.hurt(thornsDamage, reflectedDamage);

        attacker.addEffect(new MobEffectInstance(ATEffects.CRUMBLING.get(), AncientTweaksConfig.CONFIG_CALAMITY.baroclawArmorWeaknessTime.get() * 20, 0));
    }

    private boolean hasBaroClaw(Player player) {
        return CuriosApi.getCuriosInventory(player).map(handler -> handler.findFirstCurio(ATItems.BAROCLAW.get()).isPresent()).orElse(false);
    }
}