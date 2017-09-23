package samwoo.samchat.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2017/9/19.
 */

public class TaijiView extends View implements Runnable {

    private Paint paint;
    private int degree = 0;
    private boolean isRotate = false;

    public TaijiView(Context context) {
        super(context);
        init();
    }

    public TaijiView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TaijiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TaijiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        new Thread(this).start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measure(widthMeasureSpec);
        int height = measure(heightMeasureSpec);
        int result = Math.min(width, height);
        setMeasuredDimension(result, result);
    }

    /**
     * measure width & height
     *
     * @param measureSpec
     * @return
     */
    private int measure(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        return specMode == MeasureSpec.UNSPECIFIED ? 200 : specSize;
    }

    //重写的run函数，用来控制转盘转动
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        canvas.save();
        RectF rectF = new RectF(0, 0, width, height);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawArc(rectF, 180, 180, true, paint);
        paint.setColor(Color.BLACK);
        canvas.drawArc(0, height / 4, width / 2, height * 3 / 4, 180, 180, true, paint);
        canvas.drawArc(rectF, 0, 180, true, paint);
        paint.setColor(Color.WHITE);
        canvas.drawArc(width / 2, height / 4, width, height * 3 / 4, 0, 180, true, paint);

        canvas.drawCircle(width / 4, height / 2, 50, paint);
        paint.setColor(Color.BLACK);
        canvas.drawCircle(width * 3 / 4, height / 2, 50, paint);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(width / 2, height / 2, width / 2, paint);
        canvas.rotate(degree, width / 2, height / 2);
//        paint.setColor(Color.RED);
//        paint.setTextSize(20);
//        canvas.drawText("太极", width / 2, height + 10, paint);
//        canvas.restore();
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (isRotate) {
                    Log.e("Sam", "isRotate=====" + isRotate);
                    this.degree += 10;
//                    this.postInvalidate();//刷新界面
                    Log.e("Sam", "degree------>" + degree);
                    Thread.sleep(50);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startRotate() {
        this.isRotate = true;
        this.invalidate();
    }

    public void stopRotate() {
        this.isRotate = false;
        this.invalidate();
    }
}
