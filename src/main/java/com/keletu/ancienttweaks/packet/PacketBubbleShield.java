package com.keletu.ancienttweaks.packet;

import com.keletu.ancienttweaks.baubles.soulheart.SoulHeartClientHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketBubbleShield {

    private final int hearts;

    public PacketBubbleShield(int hearts) {
        this.hearts = hearts;
    }

    public PacketBubbleShield(FriendlyByteBuf buf) {
        this.hearts = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.hearts);
    }

    public static void handle(PacketBubbleShield message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        context.enqueueWork(() -> {
            SoulHeartClientHandler.clientPlayerHP = message.hearts;
        });

        context.setPacketHandled(true);
    }
}