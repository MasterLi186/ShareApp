package com.lfl.shareapp.shareapp.server;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.graphics.Path;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.List;

public class FuzhuService extends AccessibilityService {
    private static final String TAG = "FuzhuService";
    private static FuzhuService mService;
    private AccessibilityNodeInfo rootInActiveWindow;
    private static android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
    private int width;
    private int height;
    private String gallery = "gallery";
    private String weChat = "com.tencent.mm";


    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "onServiceConnected: ");
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        Log.d(TAG, "width = " + width + "  ,  height = " + height);
        mService = this;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getPackageName() == null) return;
        String s = event.getPackageName().toString();
        if (s.contains("systemui")) return;
        //Log.d(TAG, "getPackageName: " + s);
        if (s.contains("launcher")) count = 0;
        /*
        免root自动安装.
         */
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
            && s.contains("packageinstaller")){
            rootInActiveWindow = getRootInActiveWindow();
            if (rootInActiveWindow == null) return;
//            clickButton("总是允许");
            clickButton("安装");
            clickButton("确认");
            clickButton("完成");

        }

        /*
        滑动界面
         */
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
                && s.contains(weChat)){
            rootInActiveWindow = getRootInActiveWindow();
            if (rootInActiveWindow == null) return;
            Log.d(TAG, "count = " + count);
            if(count ++ == 1){
                new MoveThread().run();
            }


        }
    }
    class MoveThread implements Runnable{

        @Override
        public void run() {
            Log.d(TAG, "run: ");
            Path mPath = new Path();
            mPath.moveTo(1000, 2200);
            mPath.lineTo(400,2200);
            GestureDescription.StrokeDescription sd1 = new GestureDescription.StrokeDescription(mPath, 0, 100);
            Path mPath2 = new Path();
            mPath2.moveTo(width/2,width);
            mPath2.lineTo(width/2,0);
            performScrollBackward();
//            GestureDescription.StrokeDescription sd2 ;//= new GestureDescription.StrokeDescription(mPath2, 0, 300);
            try {
//                for (int i = 0; i < 10; i++) {
//                    Log.d(TAG, "i = " + i);
//                    sd2 = new GestureDescription.StrokeDescription(mPath2, 1, 100,true);
//                    //moveGesture(sd2);
//                    //SystemClock.sleep(200);
//                }
//                Log.d(TAG, "end: ");
            } catch (Exception e) {
                Log.e(TAG, "onAccessibilityEvent: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    /**
     * 模拟下滑操作
     */
    public void performScrollBackward() {
        try {
            Thread.sleep(500);
            performGlobalAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
            Log.d(TAG, "performScrollBackward: ");
        } catch (Exception e) {
            Log.d(TAG, "performScrollBackward: " + e.getMessage());
            e.printStackTrace();
        }
    }
    /*
    滑动
     */
    private void moveGesture(GestureDescription.StrokeDescription sd) {
        Log.d(TAG, "moveGesture: ");
        mService.dispatchGesture(new GestureDescription.Builder().addStroke(sd).build(),
                null, null);
    }

    private static int count = 0;
    /*/
    点击传入字段按钮
     */
    public void clickButton(String string){
        List<AccessibilityNodeInfo> list = rootInActiveWindow.findAccessibilityNodeInfosByText(string);
        if (list != null && !list.isEmpty()){
            for(AccessibilityNodeInfo nodeInfo:list){
//                Log.d(TAG, "nodeInfo =  " + nodeInfo);
                if (nodeInfo != null && nodeInfo.isClickable()){
                    if (string.equals("安装")){
                        SystemClock.sleep(500);
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
