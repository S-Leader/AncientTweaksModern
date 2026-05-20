package com.keletu.ancienttweaks.init;

import com.keletu.ancienttweaks.AncientTweaks;
import com.keletu.ancienttweaks.baubles.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ATItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AncientTweaks.MODID);
    public static final RegistryObject<Item> BAROCLAW = ITEMS.register("baroclaw", () -> new ItemBaroClaw(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

    public static final RegistryObject<Item> CRAWCARAPACE = ITEMS.register("craw_carapace", () -> new ItemCrabClaw(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> GIANTSHELL = ITEMS.register("giant_shell", () -> new ItemGiantShell(new Item.Properties().stacksTo(1).durability(999).rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> giantTurtleShell = ITEMS.register("giant_tortoise_shell", () -> new ItemGiantTurtleShell(new Item.Properties().stacksTo(1).durability(1500).rarity(Rarity.RARE)));

    public static final RegistryObject<Item> blueJelly = ITEMS.register("cleansing_jelly", () -> new ItemJelly(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> pinkJelly = ITEMS.register("life_jelly", () -> new ItemJelly(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> greenJelly = ITEMS.register("vital_jelly", () -> new ItemJelly(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> grandGelatin = ITEMS.register("grand_gelatin", () -> new ItemJelly(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> theAbsorber = ITEMS.register("the_absorber", () -> new ItemTheAbsorber(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> roverDrive = ITEMS.register("rover_drive", () -> new ItemRoverDrive(new Item.Properties()));

    public static final RegistryObject<Item> theSponge = ITEMS.register("the_sponge", () -> new ItemTheSponge(new Item.Properties()));
}
