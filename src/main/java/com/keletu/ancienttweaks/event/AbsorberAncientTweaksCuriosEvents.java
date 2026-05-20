package com.keletu.ancienttweaks.event;

import com.keletu.ancienttweaks.AncientTweaks;
import com.keletu.ancienttweaks.AncientTweaksConfig;
import com.keletu.ancienttweaks.init.ATEffects;
import com.keletu.ancienttweaks.init.ATItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

@Mod.EventBusSubscriber(modid = AncientTweaks.MODID)
public class AbsorberAncientTweaksCuriosEvents {

    @SubscribeEvent
    public static void onEntityKnockBack(LivingKnockBackEvent event) {
        LivingEntity living = event.getEntity();

        if (!(living instanceof Player player)) {
            return;
        }

        if (isCurioEquipped(player, ATItems.theAbsorber.get())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onEntityHurtByAbsorber(LivingHurtEvent event) {
        if (event.getAmount() >= Float.MAX_VALUE) {
            return;
        }

        LivingEntity hurtEntity = event.getEntity();

        if (!(hurtEntity instanceof Player player)) {
            return;
        }

        if (player.level().isClientSide) {
            return;
        }

        if (!isCurioEquipped(player, ATItems.theAbsorber.get())) {
            return;
        }

        if (event.isCanceled()) {
            return;
        }

        Entity directEntity = event.getSource().getDirectEntity();
        Entity sourceEntity = event.getSource().getEntity();

        if (sourceEntity instanceof LivingEntity attacker && directEntity == attacker) {
            if (!event.getSource().is(DamageTypes.THORNS)) {
                float reflectedDamage = Math.max(
                        event.getAmount() * AncientTweaksConfig.CONFIG_CALAMITY.theAbsorberReflectDamage.get().floatValue(),
                        AncientTweaksConfig.CONFIG_CALAMITY.theAbsorberReflectDamageMinimum.get().floatValue()
                );

                attacker.hurt(
                        player.damageSources().thorns(player),
                        reflectedDamage
                );

                attacker.addEffect(new MobEffectInstance(
                        ATEffects.CRUMBLING.get(),
                        AncientTweaksConfig.CONFIG_CALAMITY.theAbsorberWeaknessTime.get() * 20,
                        1
                ));

                attacker.addEffect(new MobEffectInstance(
                        MobEffects.WITHER,
                        AncientTweaksConfig.CONFIG_CALAMITY.theAbsorberWitherTime.get() * 20,
                        AncientTweaksConfig.CONFIG_CALAMITY.theAbsorberWitherLevel.get() - 1
                ));
            }
        }

        if (event.getAmount() >= 0.0F) {
            float heal = event.getAmount()
                    * AncientTweaksConfig.CONFIG_CALAMITY.theAbsorberAbsorb.get().floatValue();

            event.setAmount(event.getAmount()
                    * (1.0F - AncientTweaksConfig.CONFIG_CALAMITY.theAbsorberAbsorb.get().floatValue()));

            player.heal(Math.max(0.0F, heal));
        }
    }

    @SubscribeEvent
    public static void onEntityEatByAbsorber(LivingEntityUseItemEvent.Finish event) {
        LivingEntity living = event.getEntity();

        if (!(living instanceof Player player)) {
            return;
        }

        if (player.level().isClientSide) {
            return;
        }

        ItemStack eatenStack = event.getItem();

        if (!eatenStack.isEdible()) {
            return;
        }

        if (player.getFoodData().needsFood()) {
            return;
        }

        if (!isCurioEquipped(player, ATItems.theAbsorber.get())) {
            return;
        }

        AreaEffectCloud cloud = createBasePotionCloud(
                player,
                3.5F,
                100,
                -0.03F
        );

        cloud.addEffect(new MobEffectInstance(
                MobEffects.REGENERATION,
                AncientTweaksConfig.CONFIG_CALAMITY.theAbsorberRegenerationTime.get() * 20,
                AncientTweaksConfig.CONFIG_CALAMITY.theAbsorberRegenerationLevel.get() - 1
        ));

        cloud.addEffect(new MobEffectInstance(
                ATEffects.CLEANSING.get(),
                1,
                0
        ));

        cloud.addEffect(new MobEffectInstance(
                MobEffects.DAMAGE_BOOST,
                AncientTweaksConfig.CONFIG_CALAMITY.theAbsorberStrengthTime.get() * 20,
                AncientTweaksConfig.CONFIG_CALAMITY.theAbsorberStrengthLevel.get() - 1
        ));

        cloud.addEffect(new MobEffectInstance(
                MobEffects.DAMAGE_RESISTANCE,
                AncientTweaksConfig.CONFIG_CALAMITY.theAbsorberResistanceTime.get() * 20,
                AncientTweaksConfig.CONFIG_CALAMITY.theAbsorberResistanceLevel.get() - 1
        ));

        player.level().addFreshEntity(cloud);
    }

    private static boolean isCurioEquipped(Player player, Item item) {
        return CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.findFirstCurio(item).isPresent())
                .orElse(false);
    }

    private static AreaEffectCloud createBasePotionCloud(
            Player player,
            float radius,
            int duration,
            float radiusPerTick
    ) {
        AreaEffectCloud cloud = new AreaEffectCloud(
                player.level(),
                player.getX(),
                player.getY(),
                player.getZ()
        );

        cloud.setOwner(player);
        cloud.setRadius(radius);
        cloud.setDuration(duration);
        cloud.setRadiusPerTick(radiusPerTick);
        cloud.setParticle(ParticleTypes.ENTITY_EFFECT);

        return cloud;
    }
}