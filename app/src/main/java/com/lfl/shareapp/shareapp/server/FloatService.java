package com.lfl.shareapp.shareapp.server;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.lfl.shareapp.shareapp.view.ViewManager;

public class FloatService extends AccessibilityService {
    private static final String TAG = "FloatService";
    private static FloatService mService ;
    private int count = 0;
    private AccessibilityNodeInfo rootInActiveWindow;

    private String gallery = "gallery";
    private String weChat = "com.tencent.mm";
    private String settings = "com.android.settings";
    private String contacts = "android.contacts";
    private String launcher = "android.app.launcher";
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
        if (event.getPackageName() == null) return;
        String s = event.getPackageName().toString();
        if (s.contains("systemui")) return;
        Log.d(TAG, "count =  " + count);
        //Log.d(TAG, "getPackageName: " + s);
//        if (s.contains("launcher")) count = 0;
        /*
        免root自动安装.
         */
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
                && s.contains(launcher)){
            rootInActiveWindow = getRootInActiveWindow();
            if (rootInActiveWindow == null) return;

        }

        /*
        滑动界面
         */
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED){
            rootInActiveWindow = getRootInActiveWindow();
            if (rootInActiveWindow == null) return;
            Log.d(TAG, "count = " + count);
            if (s.contains(contacts)){
                if(count ++ == 1){
                    ViewManager.getInstance(FloatService.this).showFloatBall();
                }
                if (count == 20){
//                    ViewManager.setShowTxt("7788990");
                    ViewManager.getInstance(FloatService.this).setShowTxt("778899");
                }
            }
        }
    }

    @Override
    public void onInterrupt() {

    }
}
