package com.cinemamod.common.buffer;

import net.minecraft.network.FriendlyByteBuf;

public interface PacketByteBufSerializable<T extends PacketByteBufSerializable> {

    T fromBytes(FriendlyByteBuf buf);

    void toBytes(FriendlyByteBuf buf);

}
