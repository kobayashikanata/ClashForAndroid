package com.github.kr328.clash.core.bridge;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;

@SuppressWarnings("unused")
@Keep
public interface String2Func {
    boolean call(@Nullable String o1, @Nullable String o2);
}
