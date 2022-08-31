package com.github.kr328.clash.core.bridge;

import androidx.annotation.Keep;

@Keep
public interface HostApiTestConsumer {
    boolean call(String host, int time, String error, String result);
}
