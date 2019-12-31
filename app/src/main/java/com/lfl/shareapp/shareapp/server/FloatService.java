package com.lfl.shareapp.shareapp.server;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
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
    private Message obtain;

    public static boolean isReady(){
        return mService != null;
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        mService = this;
        Log.d(TAG, "onServiceConnected: ");
        if (!Settings.canDrawOverlays(FloatService.this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + this.getPackageName()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getPackageName() == null) return;
        String s = event.getPackageName().toString();
        if (s.contains("systemui")) return;
        Log.d(TAG, "count =  " + count);

        /*
        悬浮框
         */
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED){
            rootInActiveWindow = getRootInActiveWindow();
            if (rootInActiveWindow == null) return;
            Log.d(TAG, "count = " + count);
            if (s.contains(contacts)){
                if(count ++ == 1){
                    ViewManager.getInstance(FloatService.this).showFloatBall();
                }
                if (count == 3){
//                    ViewManager.setShowTxt("7788990");
//                    ViewManager.getInstance(FloatService.this).setShowTxt("778899");
                    int i = 0;
                    obtain = Message.obtain();
//                        EventBus.getDefault().post(new ShareEvent(i));
//                        ViewManager.getInstance(FloatService.this).hidePopupWindow();
//                        ViewManager.getInstance(FloatService.this).setShowTxt(" 88 99 00  : " + i);

                        obtain.arg1 = i;
                        obtain.what = 2;
                        handler.sendMessage(obtain);

                }
            }
        }
    }
    private int ag = 0;
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            int arg1 = msg.arg1;
            int i = msg.what;
            switch (i){
                case 1:
                    break;
                case 2:
                    Log.d(TAG, "handleMessage: ");
                    obtain = Message.obtain();
                    obtain.what = 2;
                    ag ++ ;
                    ViewManager.getInstance(FloatService.this).setShowTxt(" 88 99 00  : " + ag);
                    sendMessageDelayed(obtain,1000);
                    break;
            }
        }
    };
    @Override
    public void onInterrupt() {

    }
}
