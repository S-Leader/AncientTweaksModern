package com.keletu.ancienttweaks.init;

import com.keletu.ancienttweaks.AncientTweaks;
import com.keletu.ancienttweaks.baubles.potion.PotionCleansing;
import com.keletu.ancienttweaks.baubles.potion.PotionCrumbling;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ATEffects {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, AncientTweaks.MODID);

    public static final RegistryObject<MobEffect> CRUMBLING = EFFECTS.register("crumbling", PotionCrumbling::new);
    public static final RegistryObject<MobEffect> CLEANSING = EFFECTS.register("cleansing", PotionCleansing::new);
}
