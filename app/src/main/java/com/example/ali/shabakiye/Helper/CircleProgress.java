package com.example.ali.shabakiye.Helper;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by ali on 2/23/18.
 */

public class CircleProgress extends View {
    private static final String TAG = "CircleProgress";

    private static final long ANIMATION_DURATION = 2000;

    private int min = 0;
    private int max = 100;//defualt
    private int progress = 0;
    private int color = Color.RED;
    private float storkeWidth = 15;
    private boolean remainDay = false;
    String percent = "";

    private Paint backgroundPaint;
    private Paint foregroundPaint;
    private Paint percentPaint;

    private Rect percentTextBound;

    private RectF rectF;//for draw roundRect


    public CircleProgress(Context context) {
        super(context);
        Log.d(TAG, "CircleProgress: enter");
        init(context);
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setStyle(Paint.Style.STROKE);//storke mishe faghat yek hashiye bedoone inke dayere tooper bashe
        backgroundPaint.setStrokeWidth(storkeWidth);

        foregroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        foregroundPaint.setStyle(Paint.Style.STROKE);//storke mishe faghat yek hashiye bedoone inke dayere tooper bashe
        foregroundPaint.setStrokeWidth(storkeWidth);

        percentPaint = new Paint();
        percentPaint.setTextAlign(Paint.Align.CENTER);
        percentPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        float density = context.getResources().getDisplayMetrics().density;//chegali px haye safe
        Log.d(TAG, "init: density : " + density);
        percentPaint.setTextSize(12 * density);

        rectF = new RectF();

        percentTextBound = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure: widthMeasureSpec : " + widthMeasureSpec);
        Log.d(TAG, "onMeasure: getSuggestedMinimumWidth : " + getSuggestedMinimumWidth());
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);//in miyad har chi ke too layout besh width dadi ro mizare
//        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int min = Math.min(width, height);
        //dar 3 khat bala ma kolle nahiye view ro besh midim
        Log.d(TAG, "onMeasure: width : " + width);
        Log.d(TAG, "onMeasure: height : " + height);
        Log.d(TAG, "onMeasure: min : " + min);
        setMeasuredDimension(min, min);
        //dar khat bala ma andaze khode oon shekl ro ke mikhayem rasm konim ro besh midim
        rectF.set(storkeWidth / 2, storkeWidth / 2, min - storkeWidth / 2, min - storkeWidth / 2);//be andaze nesfe storke az safe fasele migirim ta az safe nazane biroon
        //.set() farghi nadare ba inke hamoon moghe ke dari new mikoni too constructor besh bedi
        //dar vaghe set bayad bashe hatman va andaze oon shekhaie ke bash keshidim ro moshakhas mikone
    }

    @Override
    protected void onDraw(Canvas canvas) {//har vaght view taghir mikone varede in tabe mishe
        int completeArc = max - min;
        int distanceArc = ((progress - min) * 360) / completeArc;

//        int progressPercent = ((progress - min) * 100) / completeArc;

        if (remainDay) {
            percent = progress + " روز مانده";
        } else {
            percent = progress + "%";
        }

        int r = (max - progress) * 255 / max;//chon rang ha beyne 0 ta 255 hastan dar 255/100 oon ro zarb mikonim
        int g = progress * 180 / max;
        int b = 30;
        int newColor = Color.rgb(r, g, b);

        backgroundPaint.setColor(adjustAlphaForColor(newColor, 0.1f));
        foregroundPaint.setColor(newColor);
        percentPaint.setColor(newColor);

        canvas.drawOval(rectF, backgroundPaint);

        canvas.drawArc(rectF, -90, distanceArc, false, foregroundPaint);

        percentPaint.getTextBounds(percent, 0, percent.length(), percentTextBound);
        float x = getPaddingLeft() + (getWidth() - getPaddingLeft() - getPaddingRight()) / 2;//getWidth() width khode view asli ro mide yani hamin custom view
        float y = getPaddingTop() + (getHeight() - getPaddingTop() - getPaddingBottom()) / 2 + (percentTextBound.height() / 2);
        canvas.drawText(percent, x, y, percentPaint);
    }

    public void setMax(int max) {
        this.max = (max > min) ? max : min;
        invalidate();
    }

    public int getMax() {
        return max;
    }

    public void setProgress(int value) {
        if (value > this.max) {
            this.progress = this.max;
        } else if (value < min) {
            this.progress = this.min;
        } else {
            this.progress = value;
        }
        invalidate();
    }


    private int adjustAlphaForColor(int color, float alpha) {

        if (alpha < 0.0f || alpha > 1.0f) {
            return color;
        }

        float colorAlpha = Color.alpha(color) * alpha;//alpha color feli ro migire ke roo kamane has bad dar oon alpha ke ma mikhayem zarbesh mikone
        return Color.argb((int) colorAlpha,
                Color.red(color),
                Color.green(color),
                Color.blue(color));
    }

    public void setProgressWithAnimation(int value) {
        ObjectAnimator animator = ObjectAnimator.ofInt(this, "progress", value);
        long duration = Math.abs(value - progress) * ANIMATION_DURATION / (max - min);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(duration);
        animator.start();
    }

    public void setRemainDay(boolean remainDay) {
        this.remainDay = remainDay;
    }
}
