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

                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            registerView.onRegistering();
                        }
                    });
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

    /**
     * 注册信息到Bmob云端
     * @param userName
     * @param pwd
     */
    private void registerBmob(final String userName, final String pwd) {
        User user = new User(userName, pwd);
        user.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    //Bmob端注册成功再在环信上注册信息
                    registerEaseMob(userName, pwd);
                } else {
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            registerView.registerExist();
                        }
                    });
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
