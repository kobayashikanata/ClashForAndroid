package com.github.kr328.clash.core.bridge;

import androidx.annotation.Keep;

@SuppressWarnings("unused")
@Keep
public interface IGtsPacketFlow {
    void outputPacket(byte[] packet);
    void updateFD(int fd);
    boolean canReconnect();
}
