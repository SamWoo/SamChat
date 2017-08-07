package samwoo.samchat.ui;

import butterknife.ButterKnife;
import samwoo.samchat.R;
import samwoo.samchat.base.BaseActivity;
import samwoo.samchat.common.Constants;
import samwoo.samchat.presenter.SplashPresenter;
import samwoo.samchat.presenter.impl.SplashPresenterImpl;
import samwoo.samchat.view.ISplashView;

/**
 * Created by Administrator on 2017/8/5.
 */

public class SplashActivity extends BaseActivity implements ISplashView {
    private SplashPresenter splashPresenter;

    @Override
    public int getLayoutResoure() {
        return R.layout.activity_splash;
    }

    @Override
    public void init() {
        splashPresenter = new SplashPresenterImpl(this);
        splashPresenter.checkLoginStatus();
    }

    @Override
    public void NotLogin() {
        postDelay(new Runnable() {
            @Override
            public void run() {
                startActivity(LoginActivity.class, true);
            }
        }, Constants.POST_DELAY);
    }

    @Override
    public void Logined() {
        startActivity(MainActivity.class, true);
    }

}
