package samwoo.samchat.presenter.impl;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.List;

import samwoo.samchat.presenter.MainPresenter;
import samwoo.samchat.ui.LoginActivity;
import samwoo.samchat.utils.ThreadUtils;
import samwoo.samchat.view.IMainView;

/**
 * Created by Administrator on 2017/8/7.
 */

public class MainPresenterImpl implements MainPresenter {
    private IMainView mainView;

    public MainPresenterImpl(IMainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void listenMessage() {
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
        EMClient.getInstance().addConnectionListener(connectionListener);
    }

    @Override
    public void destoryListener() {
        EMClient.getInstance().removeConnectionListener(connectionListener);
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
    }

    //message监听器
    private EMMessageListener messageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainView.updateUnreadMsgCount();
                }
            });

        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {

        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> messages) {

        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {

        }
    };

    //connection监听
    private EMConnectionListener connectionListener = new EMConnectionListener() {
        @Override
        public void onConnected() {

        }

        @Override
        public void onDisconnected(int errorCode) {
            if (errorCode == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainView.loginAnother();
                    }
                });
            }
        }
    };
}
