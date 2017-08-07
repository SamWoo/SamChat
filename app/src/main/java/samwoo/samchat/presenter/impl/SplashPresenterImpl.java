package samwoo.samchat.presenter.impl;

import com.hyphenate.chat.EMClient;

import samwoo.samchat.presenter.SplashPresenter;
import samwoo.samchat.view.ISplashView;

/**
 * Created by Administrator on 2017/8/5.
 */

public class SplashPresenterImpl implements SplashPresenter {
    private ISplashView splashView;

    public SplashPresenterImpl(ISplashView splashView) {
        this.splashView = splashView;
    }

    @Override
    public void checkLoginStatus() {
        if (EMClient.getInstance().isLoggedInBefore() && EMClient.getInstance().isConnected()) {
            splashView.Logined();
        } else {
            splashView.NotLogin();
        }
    }
}
