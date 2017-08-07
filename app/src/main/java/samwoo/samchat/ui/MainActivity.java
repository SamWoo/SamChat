package samwoo.samchat.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.List;

import butterknife.BindView;
import samwoo.samchat.R;
import samwoo.samchat.base.BaseActivity;
import samwoo.samchat.presenter.MainPresenter;
import samwoo.samchat.presenter.impl.MainPresenterImpl;
import samwoo.samchat.ui.fragment.FragmentFactory;
import samwoo.samchat.utils.ThreadUtils;
import samwoo.samchat.view.IMainView;

public class MainActivity extends BaseActivity implements IMainView {
    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;
    @BindView(R.id.fragment_container)
    FrameLayout mContainer;

    private FragmentManager fm;
    private MainPresenter mainPresenter;

    @Override
    public int getLayoutResoure() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        mainPresenter = new MainPresenterImpl(this);
        fm = getSupportFragmentManager();
        mBottomBar.setOnTabSelectListener(mListener);
        mainPresenter.listenMessage();
    }

    private OnTabSelectListener mListener = new OnTabSelectListener() {
        @Override
        public void onTabSelected(int tabId) {
            fm.beginTransaction().replace(R.id.fragment_container, FragmentFactory.getInstance().getFragment(tabId)).commit();
        }
    };

    @Override
    public void updateUnreadMsgCount() {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BottomBarTab bottomBarTab = mBottomBar.getTabWithId(R.id.conversations);
                int count = EMClient.getInstance().chatManager().getUnreadMessageCount();
                bottomBarTab.setBadgeCount(count);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUnreadMsgCount();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.destoryListener();
    }

    @Override
    public void loginAnother() {
        startActivity(LoginActivity.class, true);
        toast("该账号已在另一台设备上登录!!");
    }
}
