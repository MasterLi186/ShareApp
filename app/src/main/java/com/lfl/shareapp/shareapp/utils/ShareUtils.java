package com.lfl.shareapp.shareapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.lfl.shareapp.R;

public class ShareUtils {
    private static final String TAG = "ShareUtils";
    /**
     * 简单对话框
     */
    public static void SimpleDialog(final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_launcher_foreground).setTitle("简单对话框")
                .setMessage("设置Dialog 显示的内容")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(context, "点击了确定按钮",
                                Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancle", null).create().show();

    }
}
