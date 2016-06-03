package com.silencedut.daynighttogglebutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by SilenceDut on 16/6/3.
 */

public class DayNightToggleButton extends View {
    private static final String TAG = DayNightToggleButton.class.getSimpleName();
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private boolean mChecked ;
    private float mSunCenterX;
    private float mCenterDistance;
    private ValueAnimator mToggleAnimator;
    private Paint mPaint;
    private int mFrameRadius;
    private int mToggleRadius;
    private RectF left;
    private RectF right;
    private Path framePath;
    private Path pathSun;
    private Path pathMoon;
    private int mBackgroundColor;
    private int mToggleColor;
    private ToggleSettings mToggleSettings;
    private boolean mIsAnimating;


    public DayNightToggleButton(Context context) {
        super(context);
        init(null);
    }
    public DayNightToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DayNightToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        ToggleSettings toggleSettings =new ToggleSettings.Builder().buildSettings();
        setClickable(true);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLUE);           // 画笔颜色 - 黑色
        mPaint.setStyle(Paint.Style.FILL);    // 填充模式 - 描边
        mPaint.setStrokeWidth(1);              // 边框宽度 - 10
        pathSun = new Path();
        pathMoon =new Path();
        framePath = new Path();

        if(attrs!=null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.DayNightToggleButton);
            int toggleUnchecked = typedArray.getColor(R.styleable.DayNightToggleButton_dnToggleUncheckedColor, ToggleSettings.TOGGLE_UNCHECKED_COLOR);
            int toggleChecked = typedArray.getColor(R.styleable.DayNightToggleButton_dnToggleCheckedColor, ToggleSettings.TOGGLE_CHECKED_COLOR);
            int backgroundUnCheckColor = typedArray.getColor(R.styleable.DayNightToggleButton_dnBackgroundUncheckedColor, ToggleSettings.BACKGROUND_UNCHECKED_COLOR);
            int backgroundCheckColor = typedArray.getColor(R.styleable.DayNightToggleButton_dnBackgroundCheckedColor, ToggleSettings.BACKGROUND_CHECKED_COLOR);
            int padding = typedArray.getDimensionPixelSize(R.styleable.DayNightToggleButton_dnTogglePadding, ToggleSettings.PADDING_DEFAULT);
            int duration = typedArray.getInt(R.styleable.DayNightToggleButton_dnDuration, ToggleSettings.DURATION_DEFAULT);
            boolean withAnimator = typedArray.getBoolean(R.styleable.DayNightToggleButton_dnToggleWithAnimate, true);
            toggleSettings = new ToggleSettings.Builder()
                    .setToggleUnCheckedColor(toggleUnchecked)
                    .setToggleCheckedColor(toggleChecked)
                    .setBackgroundUncheckedColor(backgroundUnCheckColor)
                    .setBackgroundCheckedColor(backgroundCheckColor)
                    .setPadding(padding)
                    .setDuration(duration)
                    .withAnimator(withAnimator)
                    .buildSettings();
            typedArray.recycle();
        }
        setToggleSettings(toggleSettings);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setUp();
    }
    private void setUp() {
        mFrameRadius = getHeight()/2;
        mToggleRadius =mFrameRadius-mToggleSettings.mPadding;
        mSunCenterX = mFrameRadius;
        mCenterDistance = 2*mToggleRadius;
        left = new RectF(0,0,mFrameRadius*2,mFrameRadius*2);
        right = new RectF(getWidth()-mFrameRadius*2,0,getWidth(),mFrameRadius*2);
    }

    public void setToggleSettings(ToggleSettings toggleSettings) {
        this.mToggleSettings = toggleSettings;
        mToggleColor = mToggleSettings.mToggleUnCheckedColor;
        mBackgroundColor = mToggleSettings.mBackgroundUnCheckedColor;
    }

    public ToggleSettings getToggleSettings() {
        return mToggleSettings;
    }

    private void animateToggle( boolean toggleToNight) {
        final float originX;
        final float endX;
        final float originY;
        final float endY;
        final int startBackgroundColor;
        final int endBackgroundColor;
        final int startToggleColor;
        final int endToggleColor;
        if(toggleToNight) {
            originX = mFrameRadius;
            endX = getWidth()-mFrameRadius;
            originY = 2*mToggleRadius;
            endY = mToggleRadius;
            startBackgroundColor = mToggleSettings.mBackgroundUnCheckedColor;
            endBackgroundColor = mToggleSettings.mBackgroundCheckedColor;
            startToggleColor = mToggleSettings.mToggleUnCheckedColor;
            endToggleColor = mToggleSettings.mToggleCheckedColor;
        }else {
            originX = getWidth()-mFrameRadius;
            endX = mFrameRadius;
            originY = mToggleRadius;
            endY = 2*mToggleRadius;
            startBackgroundColor = mToggleSettings.mBackgroundCheckedColor;
            endBackgroundColor = mToggleSettings.mBackgroundUnCheckedColor;
            startToggleColor = mToggleSettings.mToggleCheckedColor;
            endToggleColor = mToggleSettings.mToggleUnCheckedColor;
        }

        mToggleAnimator = ValueAnimator.ofFloat(0,1.0f);
        mToggleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            FloatEvaluator floatEvaluator = new FloatEvaluator();
            ArgbEvaluator argbEvaluator = new ArgbEvaluator();
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                mSunCenterX = floatEvaluator.evaluate(fraction,originX,endX);
                mCenterDistance = floatEvaluator.evaluate(fraction,originY,endY);
                mBackgroundColor = (int) argbEvaluator.evaluate(fraction,startBackgroundColor,endBackgroundColor);
                mToggleColor = (int) argbEvaluator.evaluate(fraction,startToggleColor,endToggleColor);
                    invalidate();
            }
        });
        mToggleAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mChecked=!mChecked;
                mIsAnimating = false;
            }
        });
        mToggleAnimator.setDuration(mToggleSettings.mDuration);
        mToggleAnimator.start();
        mIsAnimating = true;
    }
    public void setOnCheckChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.mOnCheckedChangeListener = onCheckedChangeListener;
    }

    public void toggle() {
        setChecked(!mChecked);
    }
    public void setChecked(boolean checked) {
        if(mIsAnimating) {
            return;
        }
        if(mOnCheckedChangeListener!=null) {
            mOnCheckedChangeListener.onCheckedChanged(this,checked);
        }
        animateToggle(checked);
    }

    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(View buttonView, boolean isChecked);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        framePath.reset();
        pathSun.reset();
        pathMoon.reset();
        framePath.addArc(left,90,180);
        framePath.moveTo(mFrameRadius,0);
        framePath.lineTo(mFrameRadius*4,0);
        framePath.addArc(right,270,180);
        framePath.moveTo(getWidth()-mFrameRadius,mFrameRadius*2);
        framePath.addRect(mFrameRadius,0,getWidth()-mFrameRadius,mFrameRadius*2, Path.Direction.CW);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(mBackgroundColor);
        canvas.drawPath(framePath,mPaint);

        pathSun.addCircle(mSunCenterX,mFrameRadius,mToggleRadius, Path.Direction.CW);
        pathMoon.addCircle((float) (mSunCenterX-mCenterDistance* 0.71),(float)(mFrameRadius-mCenterDistance*0.71), mToggleRadius, Path.Direction.CW);
        pathSun.op(pathMoon, Path.Op.DIFFERENCE);
        mPaint.setColor(mToggleColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(pathSun,mPaint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mToggleAnimator!=null) {
            mToggleAnimator.cancel();
        }
    }
}
