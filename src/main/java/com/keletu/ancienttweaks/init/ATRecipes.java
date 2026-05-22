package com.keletu.ancienttweaks.init;

import com.keletu.ancienttweaks.AncientTweaks;
import com.keletu.ancienttweaks.recipe.AbsorberRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ATRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, AncientTweaks.MODID);

    public static final RecipeType<AbsorberRecipe> ABSORBER_RECIPE_TYPE =
            RecipeType.simple(new ResourceLocation(AncientTweaks.MODID, "absorber"));

    public static final RegistryObject<RecipeSerializer<?>> ABSORBER_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("absorber", AbsorberRecipe.Serializer::new);
}