package io.github.derringersmods.toomanyglyphs.common.network;

import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;

public abstract class AbstractPacketHandler <MSG> implements BiConsumer<MSG, NetworkEvent.Context> {
    public abstract void accept(MSG msg, NetworkEvent.Context context);
}
