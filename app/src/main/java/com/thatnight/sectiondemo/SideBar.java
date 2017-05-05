package com.thatnight.sectiondemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ThatNight on 2017.5.5.
 */

public class SideBar extends View {

    private static String[] sSideBarTitle;
    private int mChoose = -1;
    private Paint mPaint;
    private TextView mTvDialog;
    private OnTouchLetterChangeListener mListener;
    int mHeight = 0;
    int mWidth = 0;
    int mSingleTextHeight;

    public SideBar(Context context) {
        super(context);
        initView();
    }

    public SideBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        sSideBarTitle = getResources().getStringArray(R.array.sidebar);
        mPaint = new Paint();
        setPaint();
    }

    private void setPaint() {
        mPaint.reset();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mPaint.setTextSize(30);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        mSingleTextHeight = mHeight / sSideBarTitle.length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < sSideBarTitle.length; i++) {
            if (i == mChoose) {
                mPaint.setColor(getResources().getColor(R.color.colorAccent));
                mPaint.setFakeBoldText(true);
            }
            float xPos = mWidth / 2 - mPaint.measureText(sSideBarTitle[i]) / 2;
            float yPos = mSingleTextHeight * i + mSingleTextHeight;
            canvas.drawText(sSideBarTitle[i], xPos, yPos, mPaint);
            setPaint();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float y = event.getY();
        int lastChoose = mChoose;
        int scale = (int) (y / getHeight() * sSideBarTitle.length);
        switch (action) {
            case MotionEvent.ACTION_UP:
                mChoose = -1;
                if (mTvDialog != null) {
                    mTvDialog.setVisibility(View.INVISIBLE);
                }
                break;
            default:
                if (lastChoose != scale) {
                    if (scale >= 0 && scale < sSideBarTitle.length) {
                        if (mListener != null) {
                            mListener.letterChange(sSideBarTitle[scale]);
                        }
                        if (mTvDialog != null) {
                            mTvDialog.setText(sSideBarTitle[scale]);
                            mTvDialog.setVisibility(View.VISIBLE);
                        }
                        mChoose = scale;
                    }
                }
                break;
        }
        invalidate();
        return true;
    }


    public void setOnTouchLetterChangeListener(OnTouchLetterChangeListener onTouchLetterChangeListener) {
        this.mListener = onTouchLetterChangeListener;
    }

    public interface OnTouchLetterChangeListener {
        void letterChange(String s);
    }

    public void setTvDialog(TextView tvDialog) {
        mTvDialog = tvDialog;
    }
}
