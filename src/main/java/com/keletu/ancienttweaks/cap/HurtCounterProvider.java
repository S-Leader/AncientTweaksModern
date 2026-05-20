package com.keletu.ancienttweaks.cap;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HurtCounterProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {

    private final IHurtCounter instance = new HurtCounter();

    private final LazyOptional<IHurtCounter> optional = LazyOptional.of(() -> this.instance);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull net.minecraftforge.common.capabilities.Capability<T> cap,
                                                      @Nullable Direction side) {
        if (cap == ATCapabilities.HURT_COUNTER) {
            return this.optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        tag.putDouble("hurtCounter", this.instance.getHurtCounter());
        tag.putInt("damageCounter", this.instance.getHurtSinceLastDamage());

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.instance.setHurtCounter(tag.getDouble("hurtCounter"));
        this.instance.setHurtSinceLastDamage(tag.getInt("damageCounter"));
    }

    public void invalidate() {
        this.optional.invalidate();
    }
}