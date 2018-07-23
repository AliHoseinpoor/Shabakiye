package com.example.ali.shabakiye.Helper;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.Window;

public class DialogHelper {

    private Activity activity;

    public DialogHelper(Activity activity) {
        this.activity = activity;
    }

    public void resizeAlertDialog(AlertDialog dialog) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.setLayout((int) (0.9 * metrics.widthPixels), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
