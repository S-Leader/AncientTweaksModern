package com.keletu.ancienttweaks.cap;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class ATCapabilities {

    public static final Capability<IHurtCounter> HURT_COUNTER = CapabilityManager.get(new CapabilityToken<>() {
    });
}
