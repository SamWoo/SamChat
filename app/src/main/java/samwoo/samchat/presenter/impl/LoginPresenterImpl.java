package samwoo.samchat.presenter.impl;

import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import samwoo.samchat.presenter.LoginPresenter;
import samwoo.samchat.utils.RegexUtils;
import samwoo.samchat.utils.ThreadUtils;
import samwoo.samchat.view.ILoginView;

/**
 * Created by Administrator on 2017/8/6.
 */

public class LoginPresenterImpl implements LoginPresenter {
    private ILoginView loginView;

    public LoginPresenterImpl(ILoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void login(String userName, String userPwd) {
        if (RegexUtils.checkName(userName)) {
            if (RegexUtils.checkPassword(userPwd)) {
                EMClient.getInstance().login(userName, userPwd, emCallBack);
            } else {
                loginView.userPassWdError();
            }
        } else {
            loginView.userNameError();
        }
    }

    private EMCallBack emCallBack = new EMCallBack() {
        @Override
        public void onSuccess() {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loginView.loginSuccessed();
                }
            });
        }

        @Override
        public void onError(int code, String error) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loginView.loginFailed();
                }
            });
        }

        @Override
        public void onProgress(int progress, String status) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loginView.onLogining();
                }
            });
        }
    };
}
