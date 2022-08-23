package com.github.kr328.clash.service.expose;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface IClock {
    void start(@NonNull Context context);
    void stop(@Nullable Context context);

    interface Generator {
        IClock generate(Context context);
    }
}
