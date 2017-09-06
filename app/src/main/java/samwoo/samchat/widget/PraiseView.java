package samwoo.samchat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import samwoo.samchat.App;
import samwoo.samchat.R;

/**
 * Created by Administrator on 2017/9/5.
 * 自定义点赞View
 */

public class PraiseView extends RelativeLayout implements View.OnClickListener {
    @BindView(R.id.tv_praise)
    TextView tvPraise;
    @BindView(R.id.img_praise)
    ImageView imgPraise;

    private int id;
    private String mText;
    private int resId;

    public PraiseView(Context context) {
        super(context);
    }

    public PraiseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PraiseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PraiseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet atts) {
        LayoutInflater.from(context).inflate(R.layout.view_praise, this);
        ButterKnife.bind(this, this);

        TypedArray a = context.obtainStyledAttributes(atts, R.styleable.PraiseView);
        id = a.getInt(R.styleable.PraiseView_Id, 0);
        mText = a.getString(R.styleable.PraiseView_text);
        resId = a.getResourceId(R.styleable.PraiseView_image, R.drawable.share_praise_grey);
        a.recycle();
        //设置控件点击监听事件
        this.setOnClickListener(this);
    }

    //布局控件加载完后执行
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setId(id);
        setText(mText);
        setIcon(resId);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setText(String text) {
        if (!TextUtils.isEmpty(text)) tvPraise.setText(text);
    }

    public void setIcon(int resid) {
        imgPraise.setImageResource(resid);
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onViewClick(view);
        }
    }

    //定义点击事件接口
    public static interface OnViewClickListener {
        public void onViewClick(View view);
    }

    private OnViewClickListener listener;

    public void setOnViewClickListener(OnViewClickListener listener) {
        this.listener = listener;
    }

}
