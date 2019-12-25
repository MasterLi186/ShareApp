package com.lfl.shareapp.shareapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.lfl.shareapp.R;
import com.lfl.shareapp.shareapp.server.FuzhuService;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         if (!FuzhuService.isReady()) {
                try {
                    MainActivity.this.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
    //                    getResources().getXml(R.xml.fuzhu);
                } catch (Exception e) {
                    MainActivity.this.startActivity(new Intent(Settings.ACTION_SETTINGS));
                    e.printStackTrace();
                }
            }

         bindService(new Intent(MainActivity.this,FuzhuService.class), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "onServiceConnected: ");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "onServiceDisconnected: ");
            }
        },BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }
    
}
