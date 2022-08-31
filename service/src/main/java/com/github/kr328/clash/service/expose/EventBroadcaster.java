package com.github.kr328.clash.service.expose;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.github.kr328.clash.common.Global;
import com.github.kr328.clash.core.bridge.Bridge;
import com.github.kr328.clash.core.bridge.StringFunc;
import com.github.kr328.clash.service.util.BroadcastKt;

public class EventBroadcaster implements StringFunc {
    public static final String ACTION = "CLASH_NATIVE_EVENT";
    public static final String EXTRA_EVENT = "EVENT";
    public static final EventBroadcaster INSTANCE = new EventBroadcaster();
    static {
        Bridge.INSTANCE.nativeSubscribeEvent(EventBroadcaster.INSTANCE);
    }

    public static void checkInit(){

    }

    @Override
    public boolean call(@Nullable String value) {
        send(getContext(), value);
        return false;
    }

    private Context getContext(){
        return Global.INSTANCE.getApplication();
    }

    private void send(Context context, String event) {
        if(context == null || event == null) return;
        BroadcastKt.sendBroadcastSelf(context, new Intent(ACTION).putExtra(EXTRA_EVENT, event));
    }
}
