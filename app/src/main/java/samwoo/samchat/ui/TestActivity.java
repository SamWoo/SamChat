package samwoo.samchat.ui;

import android.util.Log;
import android.view.View;

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

    @Override
    public void init() {
    }

    @Override
    public int getLayoutResoure() {
        return R.layout.activity_test;
    }

    @OnClick({R.id.view_taiji})
    public void onViewClick(View view) {
        mCount++;
        if (mCount % 2 == 0) {
            mView.stopRotate();
            Log.e("Sam", "stopRotate..." + mCount);
        } else {
            mView.startRotate();
            Log.e("Sam", "startRotate..." + mCount);
        }
    }
}
