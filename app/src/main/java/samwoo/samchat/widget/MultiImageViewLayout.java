package samwoo.samchat.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import samwoo.samchat.R;

/**
 * Created by Administrator on 2017/9/2.
 */

public class MultiImageViewLayout extends LinearLayout {
    private Context context;
    private ImageView mImageViewCache;
    //照片的地址
    private List<String> imagesList;
    private List<ImageView> imageViews = new ArrayList<ImageView>();

    private static int MAX_WIDTH = 0;
    private int pxOneMaxWandH;//单张图最大允许宽高
    private int pxMoreWandH = 0;//多张图宽高
    private int MAX_ROW_COUNT = 3;//每行显示最多图片数
    private int pxImagePadding = dip2px(getContext(), 3);

    private LayoutParams onePicPara;
    private LayoutParams morePara, moreParaColumnFirst;
    private LayoutParams rowPara;

    final float[] PressX = new float[1];
    final float[] PressY = new float[1];

    private OnItemClickListener listener;


    //点击图片的处理接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position, float PressX, float PressY);

        void onItemLongClick(View view, int position, float PressX, float PressY);
    }

    public MultiImageViewLayout(Context context) {
        super(context);
        init();
    }

    public MultiImageViewLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultiImageViewLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MultiImageViewLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MAX_WIDTH == 0) {
            int width = measureWidth(widthMeasureSpec);
            if (width > 0) {
                MAX_WIDTH = width;
                if (imagesList != null && imagesList.size() > 0) setList(imagesList);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * measureWidth
     *
     * @param measureSpec
     * @return
     */
    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * 设置ViewList显示
     *
     * @param list
     * @throws IllegalArgumentException
     */
    public void setList(List<String> list) throws IllegalArgumentException {
        if (list == null) throw new IllegalArgumentException("imageList is null....");
        imagesList = list;
        if (MAX_WIDTH > 0) {
            pxMoreWandH = (MAX_WIDTH - pxImagePadding * 2) / 3;
            pxOneMaxWandH = MAX_WIDTH * 2 / 3;
            initImageLayoutParams();
        }
        initView();
    }

    //初始化view
    @SuppressLint("WrongConstant")
    private void initView() {
        this.setOrientation(VISIBLE);
        removeAllChild(this);

        if (MAX_WIDTH == 0) {
            addView(new View(getContext()));
            return;
        }

        if (imagesList == null || imagesList.size() == 0) {
            return;
        }

        if (imagesList.size() == 1) {
            addView(getImageViewFromCache(0, false));
        } else {
            int allCount = imagesList.size();
            if (allCount == 4) {
                MAX_ROW_COUNT = 2;
            } else {
                MAX_ROW_COUNT = 3;
            }
            int rowCount = allCount / MAX_ROW_COUNT + (allCount % MAX_ROW_COUNT > 0 ? 1 : 0);//行数
            for (int rowCursor = 0; rowCursor < rowCount; rowCursor++) {
                LinearLayout rowLayout = new LinearLayout(getContext());
                rowLayout.setOrientation(HORIZONTAL);
                rowLayout.setLayoutParams(rowPara);
                if (rowCursor != 0) {
                    rowLayout.setPadding(0, pxImagePadding, 0, 0);
                }
                int columnCount = allCount % MAX_ROW_COUNT == 0 ? MAX_ROW_COUNT : allCount % MAX_ROW_COUNT;//每行的列数
                if (rowCursor != rowCount - 1) {
                    columnCount = MAX_ROW_COUNT;
                }
                addView(rowLayout);
                int rowOffset = rowCursor * MAX_ROW_COUNT;//行偏移
                for (int columnCursor = 0; columnCursor < columnCount; columnCursor++) {
                    int position = columnCursor + rowOffset;
                    rowLayout.addView(getImageViewFromCache(position, true));
                }
            }
        }
    }

    /**
     * 获取ImageView缓存
     *
     * @param position
     * @param isMultiImage
     * @return
     */
    private ImageView getImageViewFromCache(int position, boolean isMultiImage) {
        ImageView imageView = null;
        if (!isMultiImage) {
            if (mImageViewCache == null) {
                mImageViewCache = new ImageView(getContext());
            }
            mImageViewCache.setAdjustViewBounds(true);
            mImageViewCache.setScaleType(ImageView.ScaleType.FIT_START);
            mImageViewCache.setMaxHeight(pxOneMaxWandH);
            mImageViewCache.setLayoutParams(onePicPara);
            imageView = mImageViewCache;
        } else {
            for (int i = 0; i < imageViews.size(); i++) {
                if (imageViews.get(i).getParent() == null) {
                    imageView = imageViews.get(i);
                    break;
                }
            }
            if (imageView == null) {
                imageView = new ImageView(getContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(position % MAX_ROW_COUNT == 0 ? moreParaColumnFirst : morePara);
                imageViews.add(imageView);
            }
        }

        String path = imagesList.get(position);
        imageView.setOnTouchListener(onTouchListener);
        imageView.setOnClickListener(onClickListener);
        imageView.setOnLongClickListener(onLongClickListener);
        imageView.setId(path.hashCode());
        imageView.setTag(R.id.FriendCircle_Position, position);
        Glide.with(getContext())
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .error(R.drawable.logo_chat)
                .into(imageView);

        return imageView;
    }

    /**
     * 点击图片监听事件
     */
    private OnTouchListener onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    ((ImageView) view).setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                    PressX[0] = motionEvent.getX();
                    PressY[0] = motionEvent.getY();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    ((ImageView) view).setColorFilter(Color.TRANSPARENT);
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    /**
     * 图片点击事件监听
     */
    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.onItemClick(view, (Integer) view.getTag(R.id.FriendCircle_Position), PressX[0], PressY[0]);
            }
        }
    };

    private OnLongClickListener onLongClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            if (listener != null) {
                listener.onItemLongClick(view, (Integer) view.getTag(R.id.FriendCircle_Position), PressX[0], PressY[0]);
            }
            return true;
        }
    };

    /**
     * 清除所有view
     *
     * @param viewGroup
     */
    private void removeAllChild(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            if (getChildAt(i) instanceof ViewGroup) {
                ((ViewGroup) getChildAt(i)).removeAllViews();
            }
        }
        this.removeAllViews();
    }

    /**
     * 初始化ImageLayoutParams
     */
    private void initImageLayoutParams() {
        onePicPara = new LayoutParams(pxOneMaxWandH, LayoutParams.WRAP_CONTENT);
        morePara = new LayoutParams(pxMoreWandH, pxMoreWandH);
        moreParaColumnFirst = new LayoutParams(pxMoreWandH, pxMoreWandH);
        morePara.setMargins(pxImagePadding, 0, 0, 0);
        rowPara = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //对外监听事件函数接口
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
