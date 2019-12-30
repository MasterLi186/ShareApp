package com.lfl.shareapp.shareapp.server;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.lfl.shareapp.shareapp.view.ViewManager;

public class FloatService extends AccessibilityService {
    private static final String TAG = "FloatService";
    private static FloatService mService ;
    private int count = 0;

    public static boolean isReady(){
        return mService != null;
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        mService = this;
        Log.d(TAG, "onServiceConnected: ");

        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + this.getPackageName()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG, "onAccessibilityEvent: ");
        if (count ++ == 1){
            if (Settings.canDrawOverlays(FloatService.this)) {
                ViewManager.getInstance(FloatService.this).showFloatBall();
            }
        }
    }

    @Override
    public void onInterrupt() {

    }
}
