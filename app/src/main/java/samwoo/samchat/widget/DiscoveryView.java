package samwoo.samchat.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Image;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import samwoo.samchat.R;

/**
 * Created by Administrator on 2017/9/8.
 */

public class DiscoveryView extends RelativeLayout implements View.OnClickListener {
    @BindView(R.id.img_disc)
    ImageView mDisc;
    @BindView(R.id.tv_disc)
    TextView tvDisc;
    @BindView(R.id.img_dynamic)
    ImageView mDynamic;
    @BindView(R.id.layout_disc)
    RelativeLayout disc;

    private int discId;
    private int dynicId;
    private String text;
    private OnViewClickListener listener;

    public static interface OnViewClickListener {
        public void onViewClick(View view);
    }

    public DiscoveryView(Context context) {
        super(context);
    }

    public DiscoveryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DiscoveryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_discovery, this);
        ButterKnife.bind(this, this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DiscoveryView);
        discId = a.getResourceId(R.styleable.DiscoveryView_leftIcon, 0);
        dynicId = a.getResourceId(R.styleable.DiscoveryView_rightIcon, 0);
        text = a.getString(R.styleable.DiscoveryView_Text);
        a.recycle();

        //设置view点击监听器
        this.setOnClickListener(this);
        //设置touch事件监听更改view的背景色
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:  // 按下时图像变灰
                        disc.setBackgroundColor(getResources().getColor(R.color.btn_white_pressed));
                        break;
                    case MotionEvent.ACTION_UP:   // 手指离开或取消操作时恢复原色
                    case MotionEvent.ACTION_CANCEL:
                        disc.setBackgroundColor(Color.WHITE);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setLeftIcon(discId);
        setRightIcon(dynicId);
        setText(text);
    }

    public void setLeftIcon(int leftIcon) {
        mDisc.setImageResource(leftIcon);
    }

    public void setRightIcon(int rightIcon) {
        mDynamic.setImageResource(rightIcon);
    }

    public void setText(String text) {
        if (!TextUtils.isEmpty(text)) tvDisc.setText(text);
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onViewClick(view);
        }
    }

    public void setOnViewClickeListener(OnViewClickListener listener) {
        this.listener = listener;
    }

}
