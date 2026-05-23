package com.keletu.ancienttweaks.event;

import com.keletu.ancienttweaks.AncientTweaks;
import com.keletu.ancienttweaks.AncientTweaksConfig;
import com.keletu.ancienttweaks.baubles.ItemGiantShell;
import com.keletu.ancienttweaks.init.ATItems;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = AncientTweaks.MODID)
public class GiantShellAncientTweaksCuriosEvents {

    private static final Map<UUID, Long> LAST_HURT_TIME = new HashMap<>();

    @SubscribeEvent
    public static void onLivingHurtByGiantShell(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (player.level().isClientSide) {
            return;
        }

        if (isGiantShellEquipped(player) && !event.isCanceled()) {
            LAST_HURT_TIME.put(player.getUUID(), player.level().getGameTime());
        }
    }

    @SubscribeEvent
    public static void onPlayerTickGiantShell(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Player player = event.player;

        if (player.level().isClientSide) {
            return;
        }

        boolean equipped = isGiantShellEquipped(player);

        if (!equipped) {
            removeGiantShellModifiers(player);
            LAST_HURT_TIME.remove(player.getUUID());
            return;
        }

        boolean weakened = false;

        Long lastHurt = LAST_HURT_TIME.get(player.getUUID());

        int debuffDuration =
                AncientTweaksConfig.CONFIG_CALAMITY.giantShellWeaknessTime.get() * 20;

        if (lastHurt != null) {
            long timeSinceHurt = player.level().getGameTime() - lastHurt;

            weakened = timeSinceHurt <= debuffDuration;

            if (timeSinceHurt > debuffDuration) {
                LAST_HURT_TIME.remove(player.getUUID());
            }
        }

        removeGiantShellModifiers(player);
        applyGiantShellModifiers(player, weakened);
    }

    private static boolean isGiantShellEquipped(Player player) {
        Item giantShell = ATItems.GIANTSHELL.get();

        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();

        if (mainHand.is(giantShell) || offHand.is(giantShell)) {
            return true;
        }

        return CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.findFirstCurio(giantShell).isPresent())
                .orElse(false);
    }

    private static void applyGiantShellModifiers(Player player, boolean weakened) {
        float speedAmount = player.isSprinting() && !weakened
                ? -AncientTweaksConfig.CONFIG_CALAMITY.giantShellDashSlowness.get().floatValue()
                : 0.0F;

        float armorAmount = weakened
                ? AncientTweaksConfig.CONFIG_CALAMITY.giantShellWeaknessArmor.get().floatValue()
                : AncientTweaksConfig.CONFIG_CALAMITY.giantShellArmor.get().floatValue();

        AttributeInstance speedAttribute =
                player.getAttribute(Attributes.MOVEMENT_SPEED);

        if (speedAttribute != null && speedAmount != 0.0F) {
            speedAttribute.addTransientModifier(new AttributeModifier(
                    ItemGiantShell.SPEED_UUID,
                    AncientTweaks.MODID + ":movement_speed",
                    speedAmount,
                    AttributeModifier.Operation.MULTIPLY_TOTAL
            ));
        }

        AttributeInstance armorAttribute =
                player.getAttribute(Attributes.ARMOR);

        if (armorAttribute != null) {
            armorAttribute.addTransientModifier(new AttributeModifier(
                    ItemGiantShell.ARMOR_UUID,
                    AncientTweaks.MODID + ":armor",
                    armorAmount,
                    AttributeModifier.Operation.ADDITION
            ));
        }
    }

    private static void removeGiantShellModifiers(Player player) {
        AttributeInstance armorAttribute =
                player.getAttribute(Attributes.ARMOR);

        if (armorAttribute != null) {
            armorAttribute.removeModifier(ItemGiantShell.ARMOR_UUID);
        }

        AttributeInstance speedAttribute =
                player.getAttribute(Attributes.MOVEMENT_SPEED);

        if (speedAttribute != null) {
            speedAttribute.removeModifier(ItemGiantShell.SPEED_UUID);
        }

        AttributeInstance knockbackAttribute =
                player.getAttribute(Attributes.KNOCKBACK_RESISTANCE);

        if (knockbackAttribute != null) {
            knockbackAttribute.removeModifier(ItemGiantShell.KNOCKBACK_UUID);
        }
    }
}