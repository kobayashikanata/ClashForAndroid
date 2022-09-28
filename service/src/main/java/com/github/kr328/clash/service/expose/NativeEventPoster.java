package com.github.kr328.clash.service.expose;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.github.kr328.clash.common.Global;
import com.github.kr328.clash.core.bridge.Bridge;
import com.github.kr328.clash.core.bridge.String2Func;
import com.github.kr328.clash.service.util.BroadcastKt;

public class NativeEventPoster implements String2Func {
    public static final String ACTION = "CLASH_NATIVE_EVENT";
    public static final String EXTRA_EVENT = "EVENT";
    public static final String EXTRA_DATA = "DATA";
    public static final NativeEventPoster INSTANCE = new NativeEventPoster();
    static {
        Bridge.INSTANCE.nativeSubscribeEvent(NativeEventPoster.INSTANCE);
    }

    public static void checkInit(){

    }

    @Override
    public boolean call(@Nullable String event, @Nullable String data) {
        send(getContext(), event, data);
        return false;
    }

    private Context getContext(){
        return Global.INSTANCE.getApplication();
    }

    private void send(Context context, String event, String data) {
        if(context == null || event == null) return;
        Intent intent = new Intent(ACTION).putExtra(EXTRA_EVENT, event);
        if(data != null) {
            intent.putExtra(EXTRA_DATA, data);
        }
        BroadcastKt.sendBroadcastSelf(context, intent);
    }
}
