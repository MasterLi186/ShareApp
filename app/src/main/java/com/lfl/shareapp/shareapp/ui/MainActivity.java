package com.lfl.shareapp.shareapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

import com.lfl.shareapp.R;
import com.lfl.shareapp.shareapp.server.FloatService;
import com.lfl.shareapp.shareapp.server.FuzhuService;
import com.lfl.shareapp.shareapp.view.ViewManager;

import org.greenrobot.eventbus.EventBus;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //EventBus.getDefault().register(MainActivity.this);

        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + this.getPackageName()));
        startActivity(intent);

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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }
    
}
