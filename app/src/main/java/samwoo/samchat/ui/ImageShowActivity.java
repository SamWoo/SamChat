package samwoo.samchat.ui;

import android.os.Handler;
import android.view.MotionEvent;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import samwoo.samchat.R;
import samwoo.samchat.base.BaseActivity;
import samwoo.samchat.widget.LoadingDialog;

/**
 * Created by Administrator on 2017/9/14.
 */

public class ImageShowActivity extends BaseActivity {
    @BindView(R.id.img_head_show)
    ImageView headView;
    public static final String url = "http://ww3.sinaimg.cn/large/0069Qhc4gw1ewrgu1o8psg307g0clkjl.gif";

    @Override
    public void init() {
        final LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismiss();
            }
        }, 2 * 1000);

        Glide.with(this)
//                .load(url)
                .load(R.drawable.so)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(headView);
    }

    @Override
    public int getLayoutResoure() {
        return R.layout.activity_image_show;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return super.onTouchEvent(event);
    }
}
