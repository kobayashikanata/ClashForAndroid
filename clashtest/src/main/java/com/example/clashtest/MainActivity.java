package com.example.clashtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.VpnService;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;

import com.github.kr328.clash.service.data.Imported;
import com.github.kr328.clash.service.expose.Broadcasts;
import com.github.kr328.clash.service.expose.CallerKt;
import com.github.kr328.clash.service.expose.ClashKt;
import com.github.kr328.clash.service.model.AccessControlMode;
import com.github.kr328.clash.service.model.Profile;
import com.github.kr328.clash.service.store.ServiceStore;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(android.R.id.text1).setOnClickListener(view -> launch());
    }

    private void launch(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            UUID uuid = UUID.randomUUID();
            String name = "New profile";
            File file = CallerKt.makeConfigFile(this, uuid);
            CallerKt.writeText(file, "mode: global" +
                    "\nloglevel: debug" +
                    "\nproxies: " +
                    "\n  - name: \"ss\"" +
                    "\n    type: ss" +
                    "\n    server: 192.168.1.1" +
                    "\n    port: 1080" +
                    "\n    cipher: aes-256-cfb" +
                    "\n    password: \"password\"" +
                    "\nrules: "+
                    "\n  # Local Area Network" +
                    "\n  - DOMAIN-KEYWORD,announce,DIRECT" +
                    "\n  - DOMAIN-KEYWORD,torrent,DIRECT" +
                    "\n  - DOMAIN-KEYWORD,tracker,DIRECT" +
                    "\n  - DOMAIN-SUFFIX,smpt,DIRECT" +
                    "\n  - DOMAIN-SUFFIX,local,DIRECT" +
                    "\n  - IP-CIDR,192.168.0.0/16,DIRECT" +
                    "\n  - IP-CIDR,10.0.0.0/8,DIRECT" +
                    "\n  - IP-CIDR,172.16.0.0/12,DIRECT" +
                    "\n  - IP-CIDR,127.0.0.0/8,DIRECT" +
                    "\n  - IP-CIDR,100.64.0.0/10,DIRECT" +
                    "\n  - MATCH,ss" +
                    "", StandardCharsets.UTF_8);
            ServiceStore store = new ServiceStore(this);
            store.setActiveProfile(uuid);
            store.setAccessControlMode(AccessControlMode.DenySelected);
            store.setAccessControlPackages(new HashSet<>(Arrays.asList(
                    "com.android.settings", "com.speed.svpn.lite"
            )));
            CallerKt.doImport(new Imported(uuid, name,
                    Profile.Type.File, "", 0L,
                    System.currentTimeMillis()));

            Intent vpnRequest = VpnService.prepare(this);
            if(vpnRequest != null){
                startActivityForResult(vpnRequest, 0);
            }
            Context context = this;
            Handler handler = new Handler(Looper.getMainLooper());
            Broadcasts broadcasts = new Broadcasts(getApplication());
            broadcasts.register();
            broadcasts.addObserver(new Broadcasts.BaseObserver() {
                @Override public void onStarted() {
                    handler.postDelayed(()-> ClashKt.stopClash(context), 2000);
                }
                @Override public void onStopped(@Nullable String cause) {

                }
            });
            ClashKt.startClash(context, true);
//            RemoteServiceConn conn = new RemoteServiceConn();
//            conn.bind(this);
//            IRemoteService service = conn.getRemoteBlocking(TimeUnit.SECONDS.toMillis(100));
//            service.clash().patchOverride(Clash.OverrideSlot.Session, new ConfigurationOverride());
        }
    }

}