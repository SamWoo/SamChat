package samwoo.samchat.presenter.impl;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

import samwoo.samchat.presenter.ChatPresenter;
import samwoo.samchat.utils.ThreadUtils;
import samwoo.samchat.view.IChatView;

/**
 * Created by Administrator on 2017/8/8.
 */

public class ChatPresenterImpl implements ChatPresenter {
    private IChatView chatView;
    private List<EMMessage> messageList;

    public ChatPresenterImpl(IChatView chatView) {
        this.chatView = chatView;
        messageList = new ArrayList<>();
    }

    @Override
    public List<EMMessage> getMessages() {
        return messageList;
    }

    @Override
    public void sendMsg(String userName, String msg) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                //获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(userName);
                //创建一条文本消息
                EMMessage message = EMMessage.createTxtSendMessage(msg, userName);
                //如果是群聊，设置chattype,默认是单聊
//                message.setChatType(EMMessage.ChatType.GroupChat);
                //设置发送状态
                message.setStatus(EMMessage.Status.INPROGRESS);
                //消息发送状态回调
                message.setMessageStatusCallback(emCallBack);
                //添加信息到List集合中
                messageList.add(message);
                //发送消息
                EMClient.getInstance().chatManager().sendMessage(message);
            }
        });
    }

    @Override
    public void loadMsg(String userName) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(userName);
                if (conversation != null) {
                    messageList.addAll(conversation.getAllMessages());
                    conversation.markAllMessagesAsRead();
                }
            }
        });
    }

    private EMCallBack emCallBack = new EMCallBack() {
        @Override
        public void onSuccess() {

            //发送成功
            chatView.sendMsgSuccessed();
        }

        @Override
        public void onError(int code, String error) {

            //发送失败
        }

        @Override
        public void onProgress(int progress, String status) {

            //正在发送
        }
    };
}
