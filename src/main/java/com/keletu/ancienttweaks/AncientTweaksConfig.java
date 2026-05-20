package com.keletu.ancienttweaks;

import net.minecraftforge.common.ForgeConfigSpec;

public class AncientTweaksConfig {

    public static final ForgeConfigSpec COMMON_SPEC;

    public static final ForgeConfigSpec.BooleanValue ENABLE_AW_TWEAKS;
    public static final ForgeConfigSpec.BooleanValue ENABLE_BAUBLES;
    public static final ForgeConfigSpec.BooleanValue ENABLE_WAFFLES_IRON;
    public static final ForgeConfigSpec.BooleanValue ENABLE_ARMOR_BREAK_TWEAKS;

    public static final ConfigCalamityBaubles CONFIG_CALAMITY;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("General Options").push("general");

        ENABLE_AW_TWEAKS = builder
                .comment("Enable AWTweaks")
                .define("AWTweaks", false);

        ENABLE_BAUBLES = builder
                .comment("Enable BaubleTweaks")
                .define("BaubleTweaks", true);

        ENABLE_WAFFLES_IRON = builder
                .comment("Enable Waffles Iron")
                .define("EnableWafflesIron", true);

        ENABLE_ARMOR_BREAK_TWEAKS = builder
                .comment("Enable Armor Break Tweaks")
                .define("ArmorBreakTweaks", true);

        builder.pop();

        builder.comment("Config Options For Calamity Baubles")
                .push("Calamity Baubles Config");

        CONFIG_CALAMITY = new ConfigCalamityBaubles(builder);

        builder.pop();

        COMMON_SPEC = builder.build();
    }

    public static class ConfigCalamityBaubles {

        public final ForgeConfigSpec.DoubleValue crabClawAttackBonus;
        public final ForgeConfigSpec.DoubleValue crabClawReflectDamage;
        public final ForgeConfigSpec.IntValue crabClawArmorWeaknessTime;

        public final ForgeConfigSpec.DoubleValue baroclawAttackBonus;
        public final ForgeConfigSpec.DoubleValue baroclawArmorBonus;
        public final ForgeConfigSpec.DoubleValue baroclawReflectDamage;
        public final ForgeConfigSpec.IntValue baroclawArmorWeaknessTime;

        public final ForgeConfigSpec.DoubleValue giantShellArmor;
        public final ForgeConfigSpec.DoubleValue giantShellWeaknessArmor;
        public final ForgeConfigSpec.DoubleValue giantShellDashSlowness;
        public final ForgeConfigSpec.IntValue giantShellWeaknessTime;

        public final ForgeConfigSpec.DoubleValue giantTurtleShellArmor;
        public final ForgeConfigSpec.DoubleValue giantTurtleShellWeaknessArmor;
        public final ForgeConfigSpec.DoubleValue giantTurtleShellDashSlowness;
        public final ForgeConfigSpec.IntValue giantTurtleShellWeaknessTime;

        public final ForgeConfigSpec.DoubleValue greenJellySpeed;
        public final ForgeConfigSpec.DoubleValue greenJellyJumpHeight;

        public final ForgeConfigSpec.IntValue pinkJellyRegenerationTime;
        public final ForgeConfigSpec.IntValue pinkJellyRegenerationStrength;

        public final ForgeConfigSpec.IntValue grandGelatinRegenerationTime;
        public final ForgeConfigSpec.IntValue grandGelatinRegenerationLevel;

        public final ForgeConfigSpec.DoubleValue theAbsorberReflectDamage;
        public final ForgeConfigSpec.DoubleValue theAbsorberReflectDamageMinimum;
        public final ForgeConfigSpec.IntValue theAbsorberWeaknessTime;
        public final ForgeConfigSpec.IntValue theAbsorberWitherTime;
        public final ForgeConfigSpec.IntValue theAbsorberWitherLevel;
        public final ForgeConfigSpec.DoubleValue theAbsorberShellArmor;

        public final ForgeConfigSpec.IntValue theAbsorberResistanceTime;
        public final ForgeConfigSpec.IntValue theAbsorberResistanceLevel;
        public final ForgeConfigSpec.IntValue theAbsorberStrengthTime;
        public final ForgeConfigSpec.IntValue theAbsorberStrengthLevel;
        public final ForgeConfigSpec.IntValue theAbsorberRegenerationTime;
        public final ForgeConfigSpec.IntValue theAbsorberRegenerationLevel;

        public final ForgeConfigSpec.DoubleValue theAbsorberAbsorb;

        public final ForgeConfigSpec.IntValue bubbleHeartHeight;

        public final ForgeConfigSpec.IntValue roverDriveShieldCount;
        public final ForgeConfigSpec.IntValue roverDriveShieldCooldown;
        public final ForgeConfigSpec.IntValue roverDriveShieldArmor;

        public final ForgeConfigSpec.IntValue theSpongeShieldCount;
        public final ForgeConfigSpec.IntValue theSpongeShieldCooldown;
        public final ForgeConfigSpec.IntValue theSpongeShieldArmor;
        public final ForgeConfigSpec.DoubleValue theSpongeDamageDiscount;

        public ConfigCalamityBaubles(ForgeConfigSpec.Builder builder) {
            crabClawAttackBonus = builder
                    .comment("how much damage will crab carapace add (percentage)")
                    .defineInRange("CrabClaw Attack Bonus", 0.07D, 0.0D, 10.0D);

            crabClawReflectDamage = builder
                    .comment("how much damage reflect to attacker when player worn CrabClaw (percentage)")
                    .defineInRange("CrabClaw Damage Reflection", 0.25D, 0.0D, 1.0D);

            crabClawArmorWeaknessTime = builder
                    .comment("how long 'armor break' effect applies to attacker when player worn CrabClaw (second)")
                    .defineInRange("CrabClaw ArmorWeakness Time", 5, 0, 32676);

            baroclawAttackBonus = builder
                    .comment("how much damage will baroclaw add (percentage)")
                    .defineInRange("Baroclaw Attack Bonus", 0.10D, 0.0D, 10.0D);

            baroclawArmorBonus = builder
                    .comment("how much armor will crab carapace add (percentage)")
                    .defineInRange("CrabClaw Armor Bonus", 0.05D, 0.0D, 10.0D);

            baroclawReflectDamage = builder
                    .comment("how much damage reflect to attacker when player worn Baroclaw (percentage)")
                    .defineInRange("Baroclaw Damage Reflection", 0.35D, 0.0D, 1.0D);

            baroclawArmorWeaknessTime = builder
                    .comment("how long 'armor break' effect applies to attacker when player worn Baroclaw (second)")
                    .defineInRange("Baroclaw ArmorWeakness Time", 5, 0, 32676);

            giantShellArmor = builder
                    .comment("how much armor points will giant shell add")
                    .defineInRange("Giant Shell Armor", 6.0D, 0.0D, 100.0D);

            giantShellWeaknessArmor = builder
                    .comment("how much armor points will giant shell add when player be attacked")
                    .defineInRange("Giant Shell Weakness Armor", 3.0D, 0.0D, 100.0D);

            giantShellDashSlowness = builder
                    .comment("how much dash speed decreased when player worn giant shell (percentage)")
                    .defineInRange("Giant Shell Dash Speed Decrease", 0.1D, 0.0D, 1.0D);

            giantShellWeaknessTime = builder
                    .comment("how long will player armor decrease when worn giant shell (second)")
                    .defineInRange("Giant Shell Weakness Time", 5, 0, 32676);

            giantTurtleShellArmor = builder
                    .comment("how much armor points will giant turtle shell add")
                    .defineInRange("Giant Turtle Shell Armor", 10.0D, 0.0D, 100.0D);

            giantTurtleShellWeaknessArmor = builder
                    .comment("how much armor points will giant turtle shell add when player be attacked")
                    .defineInRange("Giant Turtle Shell Weakness Armor", 5.0D, 0.0D, 100.0D);

            giantTurtleShellDashSlowness = builder
                    .comment("how much dash speed decreased when player worn giant turtle shell (percentage)")
                    .defineInRange("Giant Turtle Shell Dash Speed Decrease", 0.1D, 0.0D, 1.0D);

            giantTurtleShellWeaknessTime = builder
                    .comment("how long will player armor decrease when worn giant turtle shell (second)")
                    .defineInRange("Giant Turtle Shell Weakness Time", 5, 0, 32676);

            greenJellySpeed = builder
                    .comment("how much speed increase will green jelly give (percentage)")
                    .defineInRange("Green Jelly Speed Bonus", 0.12D, 0.0D, 1.0D);

            greenJellyJumpHeight = builder
                    .comment("how much jump height increase will green jelly give (percentage)")
                    .defineInRange("Green Jelly Jump Height Bonus", 0.02D, 0.0D, 1.0D);

            pinkJellyRegenerationTime = builder
                    .comment("how long regeneration get will player get when worn pink jelly (second)")
                    .defineInRange("Pink Jelly Regeneration Time", 5, 0, 32676);

            pinkJellyRegenerationStrength = builder
                    .comment("which level regeneration get will player get when worn pink jelly")
                    .defineInRange("Pink Jelly Regeneration Level", 1, 1, 256);

            grandGelatinRegenerationTime = builder
                    .comment("how long regeneration will player get when worn grand gelatin (second)")
                    .defineInRange("Grand Gelatin Regeneration Time", 5, 0, 32676);

            grandGelatinRegenerationLevel = builder
                    .comment("which level regeneration will player get when worn grand gelatin")
                    .defineInRange("Grand Gelatin Regeneration Level", 1, 1, 256);

            theAbsorberReflectDamage = builder
                    .comment("how much damage will reflection when player worn the absorber (percentage)")
                    .defineInRange("Absorber Damage Reflection", 0.35D, 0.0D, 1.0D);

            theAbsorberReflectDamageMinimum = builder
                    .comment("how much minimum damage will reflection when player worn the absorber")
                    .defineInRange("Absorber Minimum Damage Reflection", 3.5D, 0.0D, 32676.0D);

            theAbsorberWeaknessTime = builder
                    .comment("how long 'armor break' effect applies to attacker when player worn absorber (second)")
                    .defineInRange("Absorber ArmorWeakness Time", 10, 0, 32676);

            theAbsorberWitherTime = builder
                    .comment("how long wither effect applies to attacker when player worn absorber (second)")
                    .defineInRange("Absorber Wither Time", 10, 0, 32676);

            theAbsorberWitherLevel = builder
                    .comment("which level wither effect applies to attacker when player worn absorber")
                    .defineInRange("Absorber Wither Level", 2, 1, 256);

            theAbsorberShellArmor = builder
                    .comment("how much armor points will absorber add")
                    .defineInRange("Absorber Armor", 10.0D, 0.0D, 100.0D);

            theAbsorberResistanceTime = builder
                    .comment("how long resistance will player get when worn the absorber (second)")
                    .defineInRange("The Absorber Resistance Time", 5, 0, 32676);

            theAbsorberResistanceLevel = builder
                    .comment("which level resistance will player get when worn the absorber")
                    .defineInRange("Absorber Resistance Level", 1, 1, 256);

            theAbsorberStrengthTime = builder
                    .comment("how long strength will player get when worn the absorber (second)")
                    .defineInRange("The Absorber Strength Time", 5, 0, 32676);

            theAbsorberStrengthLevel = builder
                    .comment("which level strength will player get when worn the absorber")
                    .defineInRange("Absorber Strength Level", 1, 1, 256);

            theAbsorberRegenerationTime = builder
                    .comment("how long regeneration will player get when worn the absorber (second)")
                    .defineInRange("The Absorber Regeneration Time", 5, 0, 32676);

            theAbsorberRegenerationLevel = builder
                    .comment("which level regeneration will player get when worn the absorber")
                    .defineInRange("Absorber Regeneration Level", 2, 1, 256);

            theAbsorberAbsorb = builder
                    .comment("how many damage will transform to heal when player worn absorber (percentage)")
                    .defineInRange("Absorber Absorb", 0.05D, 0.0D, 1.0D);

            bubbleHeartHeight = builder
                    .comment("The height of the Bubble Shield Heart bar.")
                    .defineInRange("Bubble Heart Height", 39, 0, 256);

            roverDriveShieldCount = builder
                    .comment("how much damage will rover drive absorb")
                    .defineInRange("Rover Drive Reduction Count", 6, 1, 100);

            roverDriveShieldCooldown = builder
                    .comment("how long will rover drive cooldown (second)")
                    .defineInRange("Rover Drive Cooldown", 10, 0, 32676);

            roverDriveShieldArmor = builder
                    .comment("how much armor point when rover shield active")
                    .defineInRange("Rover Drive Armor", 5, 0, 20);

            theSpongeShieldCount = builder
                    .comment("how much damage will the sponge absorb")
                    .defineInRange("The Sponge Reduction Count", 20, 1, 100);

            theSpongeShieldCooldown = builder
                    .comment("how long will the sponge cooldown (second)")
                    .defineInRange("The Sponge Cooldown", 9, 0, 32676);

            theSpongeShieldArmor = builder
                    .comment("how much armor point when the sponge active")
                    .defineInRange("The Sponge Armor", 15, 0, 20);

            theSpongeDamageDiscount = builder
                    .comment("how much damage reduction will give when the sponge active (percentage)")
                    .defineInRange("The Sponge Damage Discount", 0.1D, 0.0D, 1.0D);
        }
    }
}