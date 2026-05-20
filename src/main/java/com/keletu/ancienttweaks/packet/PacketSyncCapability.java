/*package com.keletu.ancienttweaks.packet;

import com.keletu.ancienttweaks.cap.ATCapabilities;
import com.keletu.ancienttweaks.cap.IHurtCounter;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncCapability implements IMessage {
    private NBTTagCompound data;
    private int type;

    public PacketSyncCapability() {}

    public PacketSyncCapability(NBTTagCompound data, int type) {
        this.data = data;
        this.type = type;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        data = ByteBufUtils.readTag(buf);
        type = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, data);
        buf.writeInt(type);
    }

    public static class Handler implements IMessageHandler<PacketSyncCapability, IMessage> {
        @Override
        public IMessage onMessage(final PacketSyncCapability message, MessageContext ctx) {
            if (ctx.side.isClient()) {
                Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        EntityPlayer player = Minecraft.getMinecraft().player;
                        if (player != null) {
                            IHurtCounter capability = player.getCapability(ATCapabilities.HURT_COUNTER, null);
                            if (capability != null && message.data != null && message.type == 1) {
                                capability.deserializeNBT(message.data);
                            }
                        }
                    }
                });
            }
            return null;
        }
    }
}*/