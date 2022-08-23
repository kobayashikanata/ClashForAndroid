package com.github.kr328.clash.service.expose;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ClockModule implements IClock {
    public static IClock.Generator GENERATOR = null;
    private IClock currentClock;

    @Override
    public void start(@NonNull Context context) {
        stop(context);
        IClock.Generator generator = ClockModule.GENERATOR;
        if(generator != null) {
            IClock clock = generator.generate(context);
            this.currentClock = clock;
            clock.start(context);
        }
    }

    @Override
    public void stop(@Nullable Context context){
        IClock clock = this.currentClock;
        this.currentClock = null;
        if(clock != null) {
            clock.stop(context);
        }
    }
}
