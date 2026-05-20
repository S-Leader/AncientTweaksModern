/*package com.keletu.ancienttweaks.packet;

import com.keletu.ancienttweaks.item.ItemWafflesIron;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketLaunchWaffle implements IMessage {
    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    public static class Handler implements IMessageHandler<PacketLaunchWaffle, IMessage> {

        @Override
        public IMessage onMessage(PacketLaunchWaffle message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.server.addScheduledTask(() -> ((ItemWafflesIron) ItemRegister.wafflesIron).onLeftClick(player));
            return null;
        }
    }

}*/