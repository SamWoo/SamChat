package samwoo.samchat.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import samwoo.samchat.utils.ActivityCollector;

/**
 * Created by Administrator on 2017/8/5.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Handler mHandler = new Handler();
    private Unbinder binder;
    private InputMethodManager methodManager;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResoure());
        binder = ButterKnife.bind(this);
        ActivityCollector.add(this);
        init();
        initLogger();
    }

    //初始化Logger
    private void initLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(2)         // (Optional) How many method line to show. Default 2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag("Sam")             // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    //初始化界面
    public abstract void init();

    //获取xml布局文件ID
    public abstract int getLayoutResoure();

    //跳转到对应的activity同时判断是否关闭当前activity
    protected void startActivity(Class activity, boolean isFinish) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        if (isFinish) finish();//关闭当前活动的activity
    }

    //延时跳转到对应的activity
    protected void postDelay(Runnable runnable, long times) {
        mHandler.postDelayed(runnable, times);
    }

    //Toast提示信息
    protected void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void hideKeyBoard() {
        if (methodManager == null) {
            methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        methodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    public void showProgress(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(true);
        }
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Sam","onStart......");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Sam","onResume......");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Sam","onPause......");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Sam","onStop......");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binder.unbind();
        if (progressDialog != null) {
            progressDialog = null;
        }
        //删除activity
        ActivityCollector.remove(this);
        Logger.d("onDestory......");
    }
}
