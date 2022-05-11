package com.github.kr328.clash.service.expose;

import android.app.Service;
import android.content.Context;

import java.util.UUID;

public interface IStateUpdater {
    Notification create(Service service);

    public interface Notification {
        void onServiceCreated(Service service);
        void updateNow(Context context, String rx, String tx);
        void updateTotal(Context context, String rx, String tx);
        void flushChanges(Context context);
        void updateProfile(Context context, UUID uuid, String profile);
    }
}
