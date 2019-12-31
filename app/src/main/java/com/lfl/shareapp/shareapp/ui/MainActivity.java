package com.lfl.shareapp.shareapp.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.lfl.shareapp.R;
import com.lfl.shareapp.shareapp.server.FloatService;
import com.lfl.shareapp.shareapp.server.FuzhuService;

import org.greenrobot.eventbus.EventBus;

import ssui.ui.app.SsActivity;

public class MainActivity extends SsActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        EventBus.getDefault().register(MainActivity.this);


        if (!FuzhuService.isReady() || !FloatService.isReady()) {
                try {
                    MainActivity.this.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                } catch (Exception e) {
                    MainActivity.this.startActivity(new Intent(Settings.ACTION_SETTINGS));
                    e.printStackTrace();
                }
            }

         bindService(new Intent(MainActivity.this,FuzhuService.class), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "onServiceConnected: " + "FuzhuService");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "onServiceDisconnected: ");
            }
        },BIND_AUTO_CREATE);
         startService(new Intent(MainActivity.this,FloatService.class));


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

//    @Subscribe(threadMode = ThreadMode.BACKGROUND)
//    public void onMessageEvent(ShareEvent event) {
//        Log.d(TAG, "onMessageEvent: " + event.updateView);
//        //ViewManager.getInstance(MainActivity.this).setShowTxt("778899 i = " + event.updateView);
//    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
