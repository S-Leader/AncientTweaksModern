package com.keletu.ancienttweaks;

import com.keletu.ancienttweaks.event.CapabilityEvents;
import com.keletu.ancienttweaks.init.ATEffects;
import com.keletu.ancienttweaks.init.ATItems;
import com.keletu.ancienttweaks.init.ATTabs;
import com.keletu.ancienttweaks.packet.ModNetwork;
import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(AncientTweaks.MODID)
public class AncientTweaks {
    public static final String MODID = "ancienttweaks";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AncientTweaks() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ATItems.ITEMS.register(modEventBus);
        ATEffects.EFFECTS.register(modEventBus);
        ATTabs.TABS.register(modEventBus);

        modEventBus.addListener(CapabilityEvents::registerCapabilities);
        modEventBus.addListener(this::commonSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AncientTweaksConfig.COMMON_SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModNetwork.register();
    }
}
