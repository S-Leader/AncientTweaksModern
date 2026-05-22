package com.keletu.ancienttweaks.init;

import com.keletu.ancienttweaks.AncientTweaks;
import com.keletu.ancienttweaks.block.BlockAbsorber;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ATBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AncientTweaks.MODID);
    public static final RegistryObject<Block> ABSORBER = BLOCKS.register("absorber",
            () -> new BlockAbsorber(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .sound(SoundType.WOOL)));

}
