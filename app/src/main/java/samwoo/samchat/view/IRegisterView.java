package samwoo.samchat.view;

/**
 * Created by Administrator on 2017/8/6.
 */

public interface IRegisterView {
    void userNameError();

    void userPasswdError();

    void confiredPwdError();

    void onRegistering();

    void registerSuccessed();

    void registerFailde();

    void registerExist();
}
