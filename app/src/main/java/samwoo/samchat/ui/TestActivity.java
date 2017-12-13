package samwoo.samchat.ui;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import samwoo.samchat.R;
import samwoo.samchat.base.BaseActivity;
import samwoo.samchat.widget.TaijiView;

/**
 * Created by Administrator on 2017/9/19.
 */

public class TestActivity extends BaseActivity {
    @BindView(R.id.view_taiji)
    TaijiView mView;
    private int mCount = 0;
    RotateAnimation ra;
    AnimationSet anim;

    @Override
    public void init() {
        anim = new AnimationSet(false);
        LinearInterpolator li=new LinearInterpolator();
        ra = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setRepeatCount(-1);
        ra.setRepeatMode(Animation.RESTART);
        ra.setDuration(2000 * 1);
        ra.setInterpolator(li);
        anim.addAnimation(ra);
    }

    @Override
    public int getLayoutResoure() {
        return R.layout.activity_test;
    }

    @OnClick({R.id.view_taiji})
    public void onViewClick(View view) {
        mCount++;
        if (mCount % 2 == 0) {
            mView.clearAnimation();
        } else {
            mView.startAnimation(anim);
        }
    }
}
