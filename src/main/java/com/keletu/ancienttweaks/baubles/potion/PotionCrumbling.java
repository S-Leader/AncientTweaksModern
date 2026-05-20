package com.keletu.ancienttweaks.baubles.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class PotionCrumbling extends MobEffect {
    public PotionCrumbling() {
        super(MobEffectCategory.HARMFUL, 0x464046);
        this.addAttributeModifier(Attributes.ARMOR, "d88f6930-fefb-4bf7-a418-ABCDEF100003", -0.15F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
