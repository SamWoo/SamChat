package samwoo.samchat.view;

import android.view.View;

/**
 * Created by Administrator on 2017/8/5.
 */

public interface ILoginView {
    void userNameError();

    void userPassWdError();

    void onLogining();

    void loginSuccessed();

    void loginFailed();
}
