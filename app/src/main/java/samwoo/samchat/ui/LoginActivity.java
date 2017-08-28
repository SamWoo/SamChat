package samwoo.samchat.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import samwoo.samchat.R;
import samwoo.samchat.base.BaseActivity;
import samwoo.samchat.presenter.LoginPresenter;
import samwoo.samchat.presenter.impl.LoginPresenterImpl;
import samwoo.samchat.view.ILoginView;

/**
 * Created by Administrator on 2017/8/5.
 */

public class LoginActivity extends BaseActivity implements ILoginView {
    @BindView(R.id.top_text)
    TextView mTitle;
    @BindView(R.id.login_name)
    EditText userName;
    @BindView(R.id.login_pass)
    EditText userPassword;

    private String mUserName;
    private String mUserPassword;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 0;
    private LoginPresenter loginPresenter;


    @Override
    public int getLayoutResoure() {
        return R.layout.activity_login;
    }

    @Override
    public void init() {
        mTitle.setText("登录");
        loginPresenter = new LoginPresenterImpl(this);

        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userPassword.setText(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        userPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || ((keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                        && (keyEvent.getAction() == KeyEvent.ACTION_DOWN))) {
                    startLogin();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @OnClick({R.id.top_back, R.id.login_miss, R.id.login_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.top_back:
                onBackPressed();
                break;
            case R.id.login_btn:
                startLogin();
                break;
            case R.id.login_miss:
                startActivity(RegisterActivity.class, false);
                break;
        }
    }

    //开始登录
    public void startLogin() {
        if (checkPermission()) {
            hideKeyBoard();
            mUserName = userName.getText().toString().trim();
            mUserPassword = userPassword.getText().toString().trim();
            loginPresenter.login(mUserName, mUserPassword);
        } else {
            applyPermission();
        }
    }

    //检查权限
    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED;
    }

    //申请权限
    public void applyPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
    }

    //申请权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
                    loginPresenter.login(mUserName, mUserPassword);
                } else {
                    toast("没有权限！！");
                }
                break;
        }
    }

    @Override
    public void userNameError() {
        hideProgress();
        toast("非法用户名！！");
    }

    @Override
    public void userPassWdError() {
        hideProgress();
        toast("密码格式错误！！");
    }

    @Override
    public void onLogining() {
        showProgress("正在登录...");
    }

    @Override
    public void loginSuccessed() {
        hideProgress();
        toast("登录成功！！");
        startActivity(MainActivity.class, true);
    }

    @Override
    public void loginFailed() {
        hideProgress();
        toast("登录失败！！");
    }
}
