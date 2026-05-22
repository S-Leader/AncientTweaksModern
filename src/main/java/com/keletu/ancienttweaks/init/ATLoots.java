package com.keletu.ancienttweaks.init;

import com.keletu.ancienttweaks.AncientTweaks;
import com.keletu.ancienttweaks.event.DrownedJellyLootModifier;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ATLoots {

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOTS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, AncientTweaks.MODID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> DROWNED_JELLY = LOOTS.register("drowned_jelly", () -> DrownedJellyLootModifier.CODEC);
}