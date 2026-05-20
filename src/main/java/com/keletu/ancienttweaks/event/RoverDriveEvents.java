package com.keletu.ancienttweaks.event;

import com.keletu.ancienttweaks.AncientTweaks;
import com.keletu.ancienttweaks.AncientTweaksConfig;
import com.keletu.ancienttweaks.baubles.soulheart.SoulHeartHandler;
import com.keletu.ancienttweaks.init.ATItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = AncientTweaks.MODID)
public final class RoverDriveEvents {

    private static final String TAG_COMPOUND = AncientTweaks.MODID;
    private static final String TAG_ROVER_DRIVE_COOLDOWN = "roverDriveCooldown";

    private static final UUID BUBBLE_ARMOR_UUID =
            UUID.fromString("15faf191-bf21-4654-b359-ABCDEF100011");

    private RoverDriveEvents() {
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            SoulHeartHandler.updateClient(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Player player = event.player;

        if (player.level().isClientSide) {
            return;
        }

        boolean equipped = hasRoverDrive(player) && !hasSponge(player);

        if (!equipped) {
            removeBubbleArmor(player);
            return;
        }

        int hp = SoulHeartHandler.getHP(player);

        if (hp > 0) {
            applyBubbleArmor(player);
        } else {
            removeBubbleArmor(player);
        }

        if (hp <= 0 && player.tickCount % 20 == 0) {
            int cooldown = getCooldown(player);

            cooldown--;

            if (cooldown <= 0) {
                SoulHeartHandler.setHP(
                        player,
                        AncientTweaksConfig.CONFIG_CALAMITY.roverDriveShieldCount.get()
                );

                setCooldown(
                        player,
                        AncientTweaksConfig.CONFIG_CALAMITY.roverDriveShieldCooldown.get()
                );
            } else {
                setCooldown(player, cooldown);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (player.level().isClientSide) {
            return;
        }

        if (hasRoverDrive(player) && !hasSponge(player) && SoulHeartHandler.getHP(player) <= 0) {
            setCooldown(
                    player,
                    AncientTweaksConfig.CONFIG_CALAMITY.roverDriveShieldCooldown.get()
            );
        }
    }

    /**
     * Soul hearts absorb final damage.
     * <p>
     * LivingDamageEvent runs after armor/enchant reductions,
     * which is similar to your old "Fix shields ignore armor" behavior.
     */
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (player.level().isClientSide) {
            return;
        }

        if (event.getAmount() <= 0) {
            return;
        }

        if (!hasRoverDrive(player) && !hasSponge(player)) {
            return;
        }

        int damage = (int) event.getAmount();

        if (damage <= 0) {
            return;
        }

        int overflow = SoulHeartHandler.removeHP(player, damage);

        event.setAmount(overflow);
    }

    public static void applyBubbleArmor(Player player) {
        AttributeInstance armor = player.getAttribute(Attributes.ARMOR);

        if (armor == null) {
            return;
        }

        if (armor.getModifier(BUBBLE_ARMOR_UUID) != null) {
            return;
        }

        AttributeModifier modifier = new AttributeModifier(
                BUBBLE_ARMOR_UUID,
                AncientTweaks.MODID + ":bubble_armor",
                AncientTweaksConfig.CONFIG_CALAMITY.roverDriveShieldArmor.get(),
                AttributeModifier.Operation.ADDITION
        );

        armor.addTransientModifier(modifier);
    }

    public static void removeBubbleArmor(Player player) {
        AttributeInstance armor = player.getAttribute(Attributes.ARMOR);

        if (armor == null) {
            return;
        }

        if (armor.getModifier(BUBBLE_ARMOR_UUID) != null) {
            armor.removeModifier(BUBBLE_ARMOR_UUID);
        }
    }

    private static boolean hasRoverDrive(Player player) {
        return CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.findFirstCurio(ATItems.roverDrive.get()).isPresent())
                .orElse(false);
    }

    private static boolean hasSponge(Player player) {
        return CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.findFirstCurio(ATItems.theSponge.get()).isPresent())
                .orElse(false);
    }

    private static int getCooldown(Player player) {
        CompoundTag tag = getModTag(player);

        if (!tag.contains(TAG_ROVER_DRIVE_COOLDOWN)) {
            tag.putInt(
                    TAG_ROVER_DRIVE_COOLDOWN,
                    AncientTweaksConfig.CONFIG_CALAMITY.roverDriveShieldCooldown.get()
            );
        }

        return tag.getInt(TAG_ROVER_DRIVE_COOLDOWN);
    }

    private static void setCooldown(Player player, int cooldown) {
        getModTag(player).putInt(TAG_ROVER_DRIVE_COOLDOWN, cooldown);
    }

    private static CompoundTag getModTag(Player player) {
        CompoundTag persistent = player.getPersistentData();

        if (!persistent.contains(TAG_COMPOUND)) {
            persistent.put(TAG_COMPOUND, new CompoundTag());
        }

        return persistent.getCompound(TAG_COMPOUND);
    }
}
