package com.lfl.shareapp.shareapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lfl.shareapp.R;

public class ViewManager {
    private static final String TAG = "ViewManager";
    //WindowManager windowManager;
    public static ViewManager manager;
    Context context;
    private static WindowManager.LayoutParams floatBallParams;

    private static WindowManager mWindowManager = null;
//    private static WindowManager.LayoutParams params;
    public static Boolean isShown = false;
    private static View mView = null;

    private ViewManager(Context context) {
        this.context = context;
    }

    public static ViewManager getInstance(Context context) {
        if (manager == null) {
            manager = new ViewManager(context);
        }
        return manager;
    }

    public void showFloatBall() {
        mView = setUpView(context,"showFloatBall");
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (floatBallParams == null) {
            floatBallParams = new WindowManager.LayoutParams();
            floatBallParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            floatBallParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            floatBallParams.gravity = Gravity.TOP | Gravity.LEFT;
            floatBallParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            floatBallParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            floatBallParams.format = PixelFormat.RGBA_8888;
        }
        mWindowManager.addView(mView, floatBallParams);

    }

    public int getScreenWidth() {
        return mWindowManager.getDefaultDisplay().getWidth();
    }


    private static int canTouchFlags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

    private static int notTouchFlags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;


    /**
     * 设置是否可响应点击事件
     *
     * @param isTouchable
     */
    public static void setTouchable(boolean isTouchable) {
        if (isTouchable) {
            floatBallParams.flags = canTouchFlags;
        } else {
            floatBallParams.flags = notTouchFlags;
        }
        mWindowManager.updateViewLayout(mView, floatBallParams);

    }


    /**
     * 隐藏弹出框
     */
    public static void hidePopupWindow() {
        if (isShown && null != mView) {
            mWindowManager.removeView(mView);
            isShown = false;
        }
    }

    public void setShowTxt(String txt) {
        try {
            TextView showTv = (TextView) mView.findViewById(R.id.tv1);
            showTv.setText(txt);
            mWindowManager.updateViewLayout(mView, floatBallParams);
        } catch (Exception e) {
            Log.d(TAG, "setShowTxt: 更新悬浮框错误");
            e.printStackTrace();
            if (e.getMessage().contains("not attached to window manager")) {
                mWindowManager.addView(mView, floatBallParams);
            }
        }
    }


    public static void setShowImg(Bitmap bitmap) {
        try {
            ImageView showImg = (ImageView) mView.findViewById(R.id.iv);
            showImg.setImageBitmap(bitmap);
            mWindowManager.updateViewLayout(mView, floatBallParams);
        } catch (Exception e) {
            Log.d(TAG, "setShowTxt: 更新悬浮框错误");
            e.printStackTrace();
            if (e.getMessage().contains("not attached to window manager")) {
                mWindowManager.addView(mView, floatBallParams);
            }
        }
    }

    //    static RelativeLayout rl_drag_showinpop;
    private LinearLayout ll;

    private static View setUpView(final Context context, String showtxt) {
        View view = LayoutInflater.from(context).inflate(R.layout.float_services,
                null);

        TextView showTv = (TextView) view.findViewById(R.id.tv2);
        showTv.setText(showtxt);

        LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll);
        ll.setOnTouchListener(new View.OnTouchListener() {
            private float lastX; //上一次位置的X.Y坐标
            private float lastY;
            private float nowX;  //当前移动位置的X.Y坐标
            private float nowY;
            private float tranX; //悬浮窗移动位置的相对值
            private float tranY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean ret = false;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        // 获取按下时的X，Y坐标
                        lastX = event.getRawX();
                        lastY = event.getRawY();
                        ret = true;

                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 获取移动时的X，Y坐标
                        nowX = event.getRawX();
                        nowY = event.getRawY();
                        // 计算XY坐标偏移量
                        tranX = nowX - lastX;
                        tranY = nowY - lastY;
                        floatBallParams.x += tranX;
                        floatBallParams.y += tranY;

                        //更新悬浮窗位置
                        mWindowManager.updateViewLayout(mView, floatBallParams);
                        //记录当前坐标作为下一次计算的上一次移动的位置坐标
                        lastX = nowX;
                        lastY = nowY;

                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return ret;
            }
        });

        return view;

    }
}
