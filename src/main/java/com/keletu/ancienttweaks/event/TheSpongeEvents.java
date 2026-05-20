package com.keletu.ancienttweaks.event;

import com.keletu.ancienttweaks.AncientTweaks;
import com.keletu.ancienttweaks.AncientTweaksConfig;
import com.keletu.ancienttweaks.baubles.soulheart.SoulHeartHandler;
import com.keletu.ancienttweaks.init.ATItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = AncientTweaks.MODID)
public final class TheSpongeEvents {

    private static final String TAG_COMPOUND = AncientTweaks.MODID;
    private static final String TAG_THE_SPONGE_COOLDOWN = "theSpongeCooldown";

    private static final UUID SPONGE_ARMOR_UUID = UUID.fromString("15faf191-bf21-4654-b359-ABCDEF100012");

    private TheSpongeEvents() {
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

        boolean equipped = hasSponge(player);

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
                SoulHeartHandler.setHP(player, AncientTweaksConfig.CONFIG_CALAMITY.theSpongeShieldCount.get());

                setCooldown(player, AncientTweaksConfig.CONFIG_CALAMITY.theSpongeShieldCooldown.get());
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

        if (!hasSponge(player)) {
            return;
        }

        if (SoulHeartHandler.getHP(player) <= 0) {
            setCooldown(player, AncientTweaksConfig.CONFIG_CALAMITY.theSpongeShieldCooldown.get());
        } else {
            float discount = AncientTweaksConfig.CONFIG_CALAMITY.theSpongeDamageDiscount.get().floatValue();
            event.setAmount(event.getAmount() * (1.0F - discount));
        }
    }

    public static void applyBubbleArmor(Player player) {
        AttributeInstance armor = player.getAttribute(Attributes.ARMOR);

        if (armor == null) {
            return;
        }

        if (armor.getModifier(SPONGE_ARMOR_UUID) != null) {
            return;
        }

        AttributeModifier modifier = new AttributeModifier(SPONGE_ARMOR_UUID, AncientTweaks.MODID + ":sponge_bubble_armor", AncientTweaksConfig.CONFIG_CALAMITY.theSpongeShieldArmor.get(), AttributeModifier.Operation.ADDITION);

        armor.addTransientModifier(modifier);
    }

    public static void removeBubbleArmor(Player player) {
        AttributeInstance armor = player.getAttribute(Attributes.ARMOR);

        if (armor == null) {
            return;
        }

        if (armor.getModifier(SPONGE_ARMOR_UUID) != null) {
            armor.removeModifier(SPONGE_ARMOR_UUID);
        }
    }

    private static boolean hasSponge(Player player) {
        return CuriosApi.getCuriosInventory(player).map(handler -> handler.findFirstCurio(ATItems.theSponge.get()).isPresent()).orElse(false);
    }

    private static int getCooldown(Player player) {
        CompoundTag tag = getModTag(player);

        if (!tag.contains(TAG_THE_SPONGE_COOLDOWN)) {
            tag.putInt(TAG_THE_SPONGE_COOLDOWN, AncientTweaksConfig.CONFIG_CALAMITY.theSpongeShieldCooldown.get());
        }

        return tag.getInt(TAG_THE_SPONGE_COOLDOWN);
    }

    private static void setCooldown(Player player, int cooldown) {
        getModTag(player).putInt(TAG_THE_SPONGE_COOLDOWN, cooldown);
    }

    private static CompoundTag getModTag(Player player) {
        CompoundTag persistent = player.getPersistentData();

        if (!persistent.contains(TAG_COMPOUND)) {
            persistent.put(TAG_COMPOUND, new CompoundTag());
        }

        return persistent.getCompound(TAG_COMPOUND);
    }
}
