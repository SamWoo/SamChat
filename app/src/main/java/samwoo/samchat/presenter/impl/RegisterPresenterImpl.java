package samwoo.samchat.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import samwoo.samchat.model.User;
import samwoo.samchat.presenter.RegisterPresenter;
import samwoo.samchat.utils.RegexUtils;
import samwoo.samchat.utils.ThreadUtils;
import samwoo.samchat.view.IRegisterView;

/**
 * Created by Administrator on 2017/8/6.
 */

public class RegisterPresenterImpl implements RegisterPresenter {
    private IRegisterView registerView;

    public RegisterPresenterImpl(IRegisterView registerView) {
        this.registerView = registerView;
    }

    @Override
    public void register(String name, String passwd, String confirmPwd) {
        if (RegexUtils.checkName(name)) {
            if (RegexUtils.checkPassword(passwd)) {
                if (passwd.equals(confirmPwd)) {
                    registerBmob(name, passwd);
                    registerView.onRegistering();
                } else {
                    registerView.confiredPwdError();
                }
            } else {
                registerView.userPasswdError();
            }
        } else {
            registerView.userNameError();
        }

    }

    private void registerBmob(final String userName, final String pwd) {
        User user = new User(userName, pwd);
        user.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    registerEaseMob(userName, pwd);
                } else {
                    registerView.registerExist();
                }
            }
        });
    }

    private void registerEaseMob(final String userName, final String pwd) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(userName, pwd);
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            registerView.registerSuccessed();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            registerView.registerFailde();
                        }
                    });
                }
            }
        });
    }
}
