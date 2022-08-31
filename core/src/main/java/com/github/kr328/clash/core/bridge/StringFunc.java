package com.github.kr328.clash.core.bridge;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;

@SuppressWarnings("unused")
@Keep
public interface StringFunc {
    boolean call(@Nullable String value);
}
