package com.example.ali.shabakiye.Helper;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by ali on 7/18/18.
 */

public class TransparentToolbar {

    private Activity activity;

    public TransparentToolbar(Activity activity) {
        this.activity = activity;
    }

    public void transparentToolbar() {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void setWindowFlag(Activity activity, int bits, boolean on) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        if (on) {
            windowParams.flags |= bits;
        } else {
            windowParams.flags &= ~bits;
        }
        window.setAttributes(windowParams);
    }
}
