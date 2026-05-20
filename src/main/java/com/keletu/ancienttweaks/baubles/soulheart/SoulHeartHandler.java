package com.keletu.ancienttweaks.baubles.soulheart;

import com.keletu.ancienttweaks.AncientTweaks;
import com.keletu.ancienttweaks.init.ATItems;
import com.keletu.ancienttweaks.packet.ModNetwork;
import com.keletu.ancienttweaks.packet.PacketBubbleShield;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import top.theillusivec4.curios.api.CuriosApi;

public class SoulHeartHandler {

    private static final String COMPOUND = AncientTweaks.MODID;
    private static final String TAG_HP = "bubbleShield";
    private static final String TAG_MAX_HP = "maxBubbleShield";

    public static void addHearts(Player player, int hp) {
        int current = getHP(player);
        int max = getMaxHP(player);

        if (current >= max) {
            return;
        }

        setHP(player, Math.min(max, current + hp));
    }

    /**
     * Returns overflow damage.
     */
    public static int removeHP(Player player, int hp) {
        int current = getHP(player);
        int newHp = current - hp;

        setHP(player, Math.max(0, newHp));

        return Math.max(0, -newHp);
    }

    public static void setHP(Player player, int hp) {
        int max = getMaxHP(player);
        int clamped = Math.max(0, Math.min(max, hp));

        CompoundTag cmp = getCompoundToSet(player, false);
        cmp.putInt(TAG_HP, clamped);

        updateClient(player);
    }

    public static int getHP(Player player) {
        CompoundTag cmp = getCompoundToSet(player, false);
        return cmp.contains(TAG_HP) ? cmp.getInt(TAG_HP) : 0;
    }

    public static int getMaxHP(Player player) {
        if (hasCurio(player, ATItems.theSponge.get())) {
            return 20;
        }

        if (hasCurio(player, ATItems.roverDrive.get())) {
            return 6;
        }

        return 0;
    }

    private static boolean hasCurio(Player player, net.minecraft.world.item.Item item) {
        return CuriosApi.getCuriosInventory(player).map(handler -> handler.findFirstCurio(item).isPresent()).orElse(false);
    }

    private static CompoundTag getCompoundToSet(Player player, boolean max) {
        CompoundTag cmp = player.getPersistentData();

        String tag = !max ? COMPOUND : TAG_MAX_HP;

        if (!cmp.contains(tag)) {
            cmp.put(tag, new CompoundTag());
        }

        return cmp.getCompound(tag);
    }

    public static void updateClient(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            ModNetwork.CHANNEL.send(
                    net.minecraftforge.network.PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new PacketBubbleShield(getHP(player))
            );
        }
    }
}
