package com.example.newcomerdemo.message.dialog;

import android.content.Context;
import android.widget.Toast;

public class MessageDialog {
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int stringId) {
        Toast.makeText(context, context.getResources().getString(stringId), Toast.LENGTH_SHORT).show();
    }
}
