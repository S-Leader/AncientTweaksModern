package com.keletu.ancienttweaks.init;

import com.keletu.ancienttweaks.AncientTweaks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ATTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AncientTweaks.MODID);

    public static final RegistryObject<CreativeModeTab> TAB = TABS.register("pvz_myh",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .icon(() -> new ItemStack(ATItems.theAbsorber.get()))
                    .title(Component.translatable("creativetab.ancienttweaks"))
                    .displayItems((params, output) -> {
                        ATItems.ITEMS.getEntries().forEach(ro -> output.accept(ro.get()));
                    })
                    .build()
    );
}