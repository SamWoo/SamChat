package samwoo.samchat.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import samwoo.samchat.R;
import samwoo.samchat.base.BaseActivity;
import samwoo.samchat.presenter.RegisterPresenter;
import samwoo.samchat.presenter.impl.RegisterPresenterImpl;
import samwoo.samchat.view.IRegisterView;

/**
 * Created by Administrator on 2017/8/5.
 */

public class RegisterActivity extends BaseActivity implements IRegisterView {
    @BindView(R.id.top_text)
    TextView mTitle;
    @BindView(R.id.register_name)
    TextView mName;
    @BindView(R.id.register_passwd)
    TextView mPasswd;
    @BindView(R.id.confirm_passwd)
    TextView mConfirmed;
    private RegisterPresenter registerPresenter;

    @Override
    public int getLayoutResoure() {
        return R.layout.activity_register;
    }

    @Override
    public void init() {
        mTitle.setText("注册");
        registerPresenter = new RegisterPresenterImpl(this);
    }

    @OnClick({R.id.register_btn, R.id.top_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.register_btn:
                startRegister();
                break;
            case R.id.top_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void userNameError() {
        toast("输入的用户名不符合规则!请重新输入!");
    }

    @Override
    public void userPasswdError() {
        toast("输入的密码不符合规则!请重新输入!");
    }

    @Override
    public void confiredPwdError() {
        toast("两次输入的密码不一致,请重新输入!!");
    }

    @Override
    public void onRegistering() {
        showProgress("正在注册中...");
    }

    public void startRegister() {
        hideKeyBoard();
        String name = mName.getText().toString().trim();
        String passwd = mPasswd.getText().toString().trim();
        String confirepwd = mConfirmed.getText().toString().trim();
        registerPresenter.register(name, passwd, confirepwd);
    }

    @Override
    public void registerSuccessed() {
        hideProgress();
        toast("恭喜你,注册成功!!");
        startActivity(LoginActivity.class, true);
    }

    @Override
    public void registerFailde() {
        hideProgress();
        toast("Sorry,注册失败!!");
    }

    @Override
    public void registerExist() {
        hideProgress();
        toast("用户已经存在,请直接登录!!");
    }
}
