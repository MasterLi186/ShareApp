package com.lfl.shareapp.shareapp.server;

import android.accessibilityservice.AccessibilityService;
import android.os.SystemClock;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class FuzhuService extends AccessibilityService {
    private  final String TAG =  getClass().getName();
    private static FuzhuService mService;
    private AccessibilityNodeInfo rootInActiveWindow;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "onServiceConnected: ");
        mService = this;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG, "onAccessibilityEvent: " + event.getEventType());
        /*
        免root自动安装.
         */
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
            && event.getPackageName().toString().contains("packageinstaller")){
            Log.d(TAG, "event.getPackageName =  " + event.getPackageName());
            rootInActiveWindow = getRootInActiveWindow();
            if (rootInActiveWindow == null) return;
            clickButton("安装");
            clickButton("完成");

        }
    }

    /*/
    点击传入字段按钮
     */
    public void clickButton(String string){
        List<AccessibilityNodeInfo> list = rootInActiveWindow.findAccessibilityNodeInfosByText(string);
        if (list != null && !list.isEmpty()){
            for(AccessibilityNodeInfo nodeInfo:list){
                if (nodeInfo != null && nodeInfo.isClickable()){
                    if (string.equals("安装")){
                        Log.d(TAG, "clickButton: sleep");
                        SystemClock.sleep(5000);
                    }
                    nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }

    }
    @Override
    public void onInterrupt() {
        Log.d(TAG, "onInterrupt: ");
        mService = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        mService = null;
    }

    public static boolean isReady(){
        return mService != null;
    }
}
