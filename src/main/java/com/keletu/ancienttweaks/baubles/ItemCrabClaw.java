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

public class ItemCrabClaw extends Item implements ICurioItem {

    private static final UUID ATTACK_DAMAGE_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-ABCDEF100000");

    public ItemCrabClaw(Properties properties) {
        super(properties);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();

        if (!(entity instanceof Player player)) {
            return false;
        }

        return CuriosApi.getCuriosInventory(player).map(handler -> handler.findFirstCurio(ATItems.CRAWCARAPACE.get()).isEmpty()).orElse(true);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();

        modifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_UUID, AncientTweaks.MODID + ":attack_bonus", AncientTweaksConfig.CONFIG_CALAMITY.crabClawAttackBonus.get(), AttributeModifier.Operation.MULTIPLY_TOTAL));

        return modifiers;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.empty());

        tooltip.add(Component.translatable("tooltip.ancienttweaks.crabClaw1", Math.round(AncientTweaksConfig.CONFIG_CALAMITY.crabClawAttackBonus.get() * 100)).withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.translatable("tooltip.ancienttweaks.crabClaw2").withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.translatable("tooltip.ancienttweaks.crabClaw3").withStyle(ChatFormatting.DARK_PURPLE));
    }

    @SubscribeEvent
    public static void onEntityHurtByCrabClaw(LivingHurtEvent event) {
        if (event.getAmount() >= Float.MAX_VALUE) {
            return;
        }

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        boolean hasCrabClaw = CuriosApi.getCuriosInventory(player).map(handler -> handler.findFirstCurio(ATItems.CRAWCARAPACE.get()).isPresent()).orElse(false);

        if (!hasCrabClaw) {
            return;
        }

        DamageSource source = event.getSource();

        if (source.is(DamageTypes.THORNS)) {
            return;
        }

        Entity sourceEntity = source.getEntity();
        Entity directEntity = source.getDirectEntity();

        if (!(sourceEntity instanceof LivingEntity attacker)) {
            return;
        }

        if (directEntity != attacker) {
            return;
        }

        float reflectedDamage = event.getAmount() * AncientTweaksConfig.CONFIG_CALAMITY.crabClawReflectDamage.get().floatValue();

        DamageSource thornsDamage = player.damageSources().thorns(player);

        attacker.hurt(thornsDamage, reflectedDamage);

        attacker.addEffect(new MobEffectInstance(ATEffects.CRUMBLING.get(), AncientTweaksConfig.CONFIG_CALAMITY.crabClawArmorWeaknessTime.get() * 20, 0));
    }
}