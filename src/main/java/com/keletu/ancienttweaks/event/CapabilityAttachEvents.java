package com.keletu.ancienttweaks.event;

import com.keletu.ancienttweaks.AncientTweaks;
import com.keletu.ancienttweaks.cap.ATCapabilities;
import com.keletu.ancienttweaks.cap.HurtCounterProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityAttachEvents {

    public static final ResourceLocation HURT_COUNTER_ID =
            new ResourceLocation(AncientTweaks.MODID, "hurt_counter");

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            HurtCounterProvider provider = new HurtCounterProvider();

            event.addCapability(HURT_COUNTER_ID, provider);
            event.addListener(provider::invalidate);
        }
    }

    @SubscribeEvent
    public static void clonePlayer(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();

        event.getOriginal().getCapability(ATCapabilities.HURT_COUNTER).ifPresent(oldCap -> {
            event.getEntity().getCapability(ATCapabilities.HURT_COUNTER).ifPresent(newCap -> {
                newCap.setHurtCounter(oldCap.getHurtCounter());
                newCap.setHurtSinceLastDamage(oldCap.getHurtSinceLastDamage());
            });
        });

        event.getOriginal().invalidateCaps();
    }
}