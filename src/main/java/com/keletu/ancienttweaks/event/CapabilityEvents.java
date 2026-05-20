package com.keletu.ancienttweaks.event;

import com.keletu.ancienttweaks.cap.IHurtCounter;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityEvents {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IHurtCounter.class);
    }
}