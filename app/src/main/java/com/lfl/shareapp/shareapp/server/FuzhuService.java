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
    private String settings = "com.android.settings";
    private String contacts = "android.contacts";
    private String launcher = "android.app.launcher";
    private String xiaomi_launcher = "miui.home";


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
        performGlobalAction(GLOBAL_ACTION_HOME);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getPackageName() == null) return;
        String s = event.getPackageName().toString();
        if (s.contains("systemui")) return;
        Log.d(TAG, "getPackageName: " + s);
//        if (s.contains("launcher")) count = 0;
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
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED){
            rootInActiveWindow = getRootInActiveWindow();
            if (rootInActiveWindow == null) return;
            Log.d(TAG, "count = " + count);
            if (s.contains(launcher) || s.contains(xiaomi_launcher)){
                if(count ++ == 1){
                    //new LauncherMove().run();
//                checkListView();
                }
            }
            if (s.contains(weChat)){
                if(count ++ == 1){
                    //new LauncherMove().run();
                }
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
            mPath2.moveTo(width/2,height/2 +40);
            mPath2.lineTo(width/2,height/2 -40);
            performScrollBackward();
            GestureDescription.StrokeDescription sd2 ;//= new GestureDescription.StrokeDescription(mPath2, 0, 300);
            try {
                for (int i = 0; i < 150; i++) {
                    Log.d(TAG, "i = " + i);
                    sd2 = new GestureDescription.StrokeDescription(mPath2, 0, 10);
                    moveGesture(sd2);
//                    checkListView();
                    SystemClock.sleep(15);
                }
//                Log.d(TAG, "end: ");
            } catch (Exception e) {
                Log.e(TAG, "onAccessibilityEvent: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    class LauncherMove implements Runnable{

        @Override
        public void run() {
            Log.d(TAG, "run: ");
            Path mPath = new Path();
            mPath.moveTo(width /4 * 3, height /2);
            mPath.lineTo(width /4 , height /2);
            GestureDescription.StrokeDescription sd2 ;
            try {
                for (int i = 0; i < 5; i++) {
                    Log.d(TAG, "i = " + i);
                    sd2 = new GestureDescription.StrokeDescription(mPath, 0, 300);
                    moveGesture(sd2);
//                    checkListView();
                    SystemClock.sleep(500);
                }
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

    /*/
    检查listview
    */
    public void checkListView(){
        AccessibilityNodeInfo info = getRootInActiveWindow();
        if (info== null) return;
        try {
            AccessibilityNodeInfo accessibilityNodeInfo = recycleFindListView(info);
            if (accessibilityNodeInfo != null)accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String listViewClassName = "android.widget.ListView";
    private static final String recyclerView = "android.support.v7.widget.RecyclerView";
    /**根据固定的类名循环查找到listView,查找到目标listView后即退出循环
     * @param node
     * @return
     */
    public AccessibilityNodeInfo recycleFindListView(AccessibilityNodeInfo node) {
        if (node.getChildCount() == 0) {
            return null;
        } else {//listview下面必定有子元素，所以放在此时判断
            for (int i = 0; i < node.getChildCount(); i++) {
                Log.d(TAG, "recycleFindListView: " + node.getClassName());
                if (recyclerView.equals(node.getClassName())) {
                    Log.d(TAG, "recyclerView。。。");
//                    node.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                    return node;
                } else if (node.getChild(i) != null) {
                    AccessibilityNodeInfo result = recycleFindListView(node.getChild(i));
                    if (result == null) {
                        continue;
                    } else {
                        return result;
                    }
                }
            }
        }
        return null;
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
