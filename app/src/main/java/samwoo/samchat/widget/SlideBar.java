package samwoo.samchat.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.hyphenate.util.DensityUtil;

import samwoo.samchat.R;

import static android.graphics.Color.*;

/**
 * Created by Administrator on 2017/8/11.
 */

public class SlideBar extends View {
    public static final String TAG = "SlideBar";
    private static final String[] SECTIONS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"
            , "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private Paint mPaint;
    private float mTextSize = 0;
    private float mTextBaseline = 0;
    private int mCurrentIndex = -1;
    private OnSlideBarChangeListener listener;

    public SlideBar(Context context) {
        super(context);
        init();
    }

    public SlideBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SlideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.black_deep));
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(DensityUtil.sp2px(getContext(), 16));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //计算分配给字符的高度
        mTextSize = h * 1.0f / SECTIONS.length;
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        //获取绘制字符的实际高度
        float mTextHeight = fontMetrics.descent - fontMetrics.ascent;
        //计算字符居中时的baseline
        mTextBaseline = mTextSize / 2 + mTextHeight / 2 - fontMetrics.descent;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float x = getWidth() * 1.0f / 2;
        float baseline = mTextBaseline;
        for (int i = 0; i < SECTIONS.length; i++) {
            canvas.drawText(SECTIONS[i], x, baseline, mPaint);
            baseline += mTextSize;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setBackgroundResource(R.drawable.bg_slide_bar);
                notifySectionChange(event);
                break;
            case MotionEvent.ACTION_MOVE:
                notifySectionChange(event);
                break;
            case MotionEvent.ACTION_UP:
                setBackgroundColor(TRANSPARENT);
                if (listener != null) {
                    listener.onSlidingFinish();
                }
                break;
        }
        return true;
    }

    private void notifySectionChange(MotionEvent event) {
        int index = getTouchIndex(event);
        if (listener != null && mCurrentIndex != index) {
            mCurrentIndex = index;
            listener.onSectionChange(index, SECTIONS[index]);
        }
    }

    //获取点击事件对应的字母索引
    private int getTouchIndex(MotionEvent event) {
        int index = (int) (event.getY() / mTextSize);
        if (index < 0) {
            index = 0;
        } else if (index > SECTIONS.length - 1) {
            index = SECTIONS.length - 1;
        }
        return index;
    }

    public static interface OnSlideBarChangeListener {
        void onSlidingFinish();

        void onSectionChange(int index, String section);
    }

    public void setOnSlidBarChangeListener(OnSlideBarChangeListener listener) {
        this.listener = listener;
    }
}
