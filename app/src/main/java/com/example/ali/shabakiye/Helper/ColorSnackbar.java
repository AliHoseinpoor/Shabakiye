package com.example.ali.shabakiye.Helper;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ali on 7/15/18.
 */

public class ColorSnackbar {

    private static View getSnackbarView(Snackbar snackbar) {
        if (snackbar != null) {
            return snackbar.getView();
        }
        return null;
    }

    private static Snackbar colorSnackbar(Snackbar snackbar, int color) {
        View snackbarView = getSnackbarView(snackbar);
        if (snackbarView != null) {
            snackbarView.setBackgroundColor(color);
            TextView snackText = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            snackText.setTextColor(Color.WHITE);
            snackText.setTypeface(snackText.getTypeface(), Typeface.BOLD);
            snackText.setTextSize(TypedValue.COMPLEX_UNIT_SP , 15);
        }
        return snackbar;
    }

    public static Snackbar reportSnackbar(Snackbar snackbar) {
        return colorSnackbar(snackbar, Color.parseColor("#e9bd0d"));
    }

    public static Snackbar errorSnackbar(Snackbar snackbar) {
        return colorSnackbar(snackbar, Color.parseColor("#e23333"));
    }
}
