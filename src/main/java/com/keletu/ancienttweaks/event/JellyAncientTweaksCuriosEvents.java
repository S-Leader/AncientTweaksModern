package com.keletu.ancienttweaks.event;

import com.keletu.ancienttweaks.AncientTweaks;
import com.keletu.ancienttweaks.AncientTweaksConfig;
import com.keletu.ancienttweaks.init.ATEffects;
import com.keletu.ancienttweaks.init.ATItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

@Mod.EventBusSubscriber(modid = AncientTweaks.MODID)
public class JellyAncientTweaksCuriosEvents {

    @SubscribeEvent
    public static void onEntityJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity living = event.getEntity();

        if (!(living instanceof Player player)) {
            return;
        }

        if (hasGreenJellyJumpBoost(player)) {
            double jumpBoost = AncientTweaksConfig.CONFIG_CALAMITY.greenJellyJumpHeight.get();

            player.setDeltaMovement(player.getDeltaMovement().add(0.0D, jumpBoost, 0.0D));

            player.fallDistance -= (float) jumpBoost;
        }
    }

    @SubscribeEvent
    public static void onEntityEat(LivingEntityUseItemEvent.Finish event) {
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

        /*
         * 旧版逻辑：
         *
         * event.getItem().getItem() instanceof ItemFood && !player.getFoodStats().needFood()
         *
         * 1.20.1 对应：
         *
         * eatenStack.isEdible() && !player.getFoodData().needsFood()
         *
         * 即：吃完后如果已经不需要食物，则触发果冻效果。
         */
        if (player.getFoodData().needsFood()) {
            return;
        }

        if (isCurioEquipped(player, ATItems.pinkJelly.get())) {
            spawnPotionCloud(player, 2.0F, 40, -0.03F, MobEffects.REGENERATION, AncientTweaksConfig.CONFIG_CALAMITY.pinkJellyRegenerationTime.get() * 20, AncientTweaksConfig.CONFIG_CALAMITY.pinkJellyRegenerationStrength.get() - 1);
        }

        if (isCurioEquipped(player, ATItems.blueJelly.get())) {
            spawnPotionCloud(player, 2.0F, 100, -0.03F, ATEffects.CLEANSING.get(), 1, 0);
        }

        if (isCurioEquipped(player, ATItems.grandGelatin.get())) {
            AreaEffectCloud cloud = createBasePotionCloud(player, 2.5F, 80, -0.03F);
            cloud.addEffect(new MobEffectInstance(MobEffects.REGENERATION, AncientTweaksConfig.CONFIG_CALAMITY.grandGelatinRegenerationTime.get() * 20, AncientTweaksConfig.CONFIG_CALAMITY.grandGelatinRegenerationLevel.get() - 1));

            cloud.addEffect(new MobEffectInstance(ATEffects.CLEANSING.get(), 1, 0));

            player.level().addFreshEntity(cloud);
        }
    }

    private static boolean hasGreenJellyJumpBoost(Player player) {
        return isCurioEquipped(player, ATItems.greenJelly.get()) || isCurioEquipped(player, ATItems.grandGelatin.get()) || isCurioEquipped(player, ATItems.theAbsorber.get());
    }

    private static boolean isCurioEquipped(Player player, Item item) {
        return CuriosApi.getCuriosInventory(player).map(handler -> handler.findFirstCurio(item).isPresent()).orElse(false);
    }

    private static void spawnPotionCloud(Player player, float radius, int duration, float radiusPerTick, MobEffect effect, int effectDuration, int amplifier) {
        AreaEffectCloud cloud = createBasePotionCloud(player, radius, duration, radiusPerTick);

        cloud.addEffect(new MobEffectInstance(effect, effectDuration, amplifier));

        player.level().addFreshEntity(cloud);
    }

    private static AreaEffectCloud createBasePotionCloud(Player player, float radius, int duration, float radiusPerTick) {
        AreaEffectCloud cloud = new AreaEffectCloud(player.level(), player.getX(), player.getY(), player.getZ());

        cloud.setOwner(player);
        cloud.setRadius(radius);
        cloud.setDuration(duration);
        cloud.setRadiusPerTick(radiusPerTick);
        cloud.setParticle(ParticleTypes.ENTITY_EFFECT);

        return cloud;
    }
}