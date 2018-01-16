package samwoo.samchat.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import samwoo.samchat.R;


/**
 * Created by Administrator on 2017/12/15.
 */

public class WatchView extends View {
    private Paint mPaint;
    //    private Canvas mCavas;
    private Calendar mCalender;
    private int width;
    private int height;
    private int distance;
    private Paint.FontMetrics fontMetrics;
    private int strokeWidth = 20;
    private String[] str = {"Ⅻ", "Ⅰ", "Ⅱ", "Ⅲ", "Ⅳ", "Ⅴ", "Ⅵ", "Ⅶ", "Ⅷ", "Ⅸ", "Ⅹ", "Ⅺ"};

    public WatchView(Context context) {
        super(context);
        init();
    }

    public WatchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WatchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WatchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(40);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        fontMetrics = mPaint.getFontMetrics();

        mCalender = Calendar.getInstance();
    }

    public void run() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                postInvalidate();
            }
        }, 0, 1000);
    }

    private Rect getTextRect(String str) {
        Rect rect = new Rect();
        mPaint.getTextBounds(str, 0, str.length(), rect);
        return rect;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureSize(widthMeasureSpec);
        int height = measureSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureSize(int measureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else if (mode == MeasureSpec.AT_MOST) {
            result = 200;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        width = getMeasuredWidth();
        height = getMeasuredHeight();
        distance = Math.min(width, height);
        int radius = distance / 2;
        canvas.translate(width / 2, height / 2);
        //画背景
        drawWatchBackground(canvas, radius);
        //画表盘
//        drawPalent(canvas, radius);
        //画数字
//        drawNumber(canvas, radius);
        //画指针
        drawPoint(canvas, radius);
    }

    /**
     * 画表盘
     *
     * @param canvas
     * @param radius
     */
    private void drawPalent(Canvas canvas, int radius) {
        canvas.save();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(strokeWidth);
        canvas.drawCircle(0, 0, radius - strokeWidth, mPaint);
        for (int i = 0; i < 60; i++) {
            if (i % 5 == 0) {
                mPaint.setColor(Color.BLACK);
                if (i == 0 || i == 15 || i == 30 || i == 45) {
                    mPaint.setStrokeWidth(10);
                    canvas.drawLine(radius - 40, 0, radius - strokeWidth, 0, mPaint);
                }
                mPaint.setStrokeWidth(5);
                canvas.drawLine(radius - 35, 0, radius - strokeWidth, 0, mPaint);
            } else {
                mPaint.setStrokeWidth(2);
                mPaint.setColor(Color.GRAY);
                canvas.drawLine(radius - 35, 0,
                        radius - strokeWidth, 0, mPaint);
            }
            canvas.rotate(6, 0, 0);
        }
        canvas.restore();
    }

    /**
     * 画指针
     *
     * @param canvas
     * @param radius
     */
    private void drawPoint(Canvas canvas, int radius) {
        mCalender.setTimeInMillis(System.currentTimeMillis());
        int hour = mCalender.get(Calendar.HOUR) % 12;
        int minute = mCalender.get(Calendar.MINUTE);
        int second = mCalender.get(Calendar.SECOND);
        int startX = 0;
        int startY = 0;
        RectF rect = new RectF(-10, -10, 10, 10);

        //hour
        float degree = (hour + minute / 60f) * 360f / 12f;
        double radians = Math.toRadians(degree);
        Log.i("Sam", "degree=" + degree + "----" + "radians==" + radians);
        int endX = (int) (startX + radius * 0.5 * Math.cos(radians));
        int endY = (int) (startY + radius * 0.5 * Math.sin(radians));

        canvas.save();
        Path hourPathLeft = new Path();
        hourPathLeft.moveTo(0, 10);
        hourPathLeft.arcTo(rect, 270, -90);
        hourPathLeft.lineTo(endX, endY);
        hourPathLeft.lineTo(0, 10);
        hourPathLeft.close();

        Path hourPathRight = new Path();
        hourPathRight.moveTo(0, 10);
        hourPathRight.arcTo(rect, 270, 90);
        hourPathRight.lineTo(endX, endY);
        hourPathRight.lineTo(0, 10);
        hourPathRight.close();

//        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.RED);
        canvas.rotate(-90, 0, 0);
        canvas.drawPath(hourPathLeft, mPaint);
        canvas.drawPath(hourPathRight, mPaint);
//        canvas.drawLine(startX, startY, endX, endY, mPaint);
        canvas.restore();

        //minute
        degree = minute * 360 / 60;
        radians = Math.toRadians(degree);
        endX = (int) (startX + radius * 0.7 * Math.cos(radians));
        endY = (int) (startY + radius * 0.7 * Math.sin(radians));
        canvas.save();
        hourPathLeft = new Path();
        hourPathLeft.moveTo(0, 10);
        hourPathLeft.arcTo(rect, 270, -90);
        hourPathLeft.lineTo(endX, endY);
        hourPathLeft.lineTo(0, 10);
        hourPathLeft.close();

        hourPathRight = new Path();
        hourPathRight.moveTo(0, 10);
        hourPathRight.arcTo(rect, 270, 90);
        hourPathRight.lineTo(endX, endY);
        hourPathRight.lineTo(0, 10);
        hourPathRight.close();
        mPaint.setStrokeWidth(2);
        mPaint.setColor(Color.BLUE);
        canvas.rotate(-90, 0, 0);
        canvas.drawPath(hourPathLeft, mPaint);
        canvas.drawPath(hourPathRight, mPaint);
//        canvas.drawLine(startX, startY, endX, endY, mPaint);
        canvas.restore();

        //second
        degree = second * 360 / 60;
        radians = Math.toRadians(degree);
        endX = (int) (startX + radius * 0.9 * Math.cos(radians));
        endY = (int) (startY + radius * 0.9 * Math.sin(radians));
        canvas.save();
        mPaint.setStrokeWidth(2);
        mPaint.setColor(Color.BLACK);
        canvas.rotate(-90, 0, 0);
        canvas.drawLine(startX, startY, endX, endY, mPaint);
        canvas.restore();

        canvas.save();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        canvas.drawCircle(0, 0, 20, mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0, 0, 10, mPaint);
        canvas.restore();
    }

    /**
     * 画数字
     *
     * @param canvas
     * @param raduis
     */
    private void drawNumber(Canvas canvas, int raduis) {
        canvas.save();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);

        for (int i = 0; i < 12; i++) {
            canvas.rotate(30 * i);
            canvas.translate(0, 90 - raduis);

//            if (i == 0) {
//                canvas.drawText(12 + "", -getTextRect(12 + "").width() / 2,
//                        -getTextRect(12 + "").height() / 2, mPaint);
//            } else if (i == 6) {
//                canvas.drawText(9 + "", -getTextRect(9 + "").width() / 2,
//                        -getTextRect(i + "").height() / 2, mPaint);
//            } else if (i == 9) {
//                canvas.drawText(6 + "", -getTextRect(6 + "").width() / 2,
//                        -getTextRect(i + "").height() / 2, mPaint);
//            } else {
//                canvas.drawText(i + "", -getTextRect(i + "").width() / 2,
//                        -getTextRect(i + "").height() / 2, mPaint);
//            }

            canvas.drawText(str[i], -getTextRect(str[i]).width() / 2,
                    -getTextRect(str[i]).height() / 2, mPaint);

            canvas.translate(0, raduis - 90);
            canvas.rotate(-30 * i);
        }
        canvas.restore();
    }

    /**
     * 画手表背景
     *
     * @param radius
     */
    private void drawWatchBackground(Canvas canvas, int radius) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.watch_bg);
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        Log.e("Sam", "w====" + bmpWidth + "**********" + "h=====" + bmpHeight);
        Matrix matrix = new Matrix();
        float scaleWidth = (float) distance / bmpWidth;
        float scaleHeight = (float) distance / bmpHeight;
        Log.e("Sam", "scaleW----->" + scaleWidth + "*******" + "scaleH---->" + scaleHeight);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight, matrix, true);
        canvas.drawBitmap(bitmap, -radius, -radius, null);
    }
}
