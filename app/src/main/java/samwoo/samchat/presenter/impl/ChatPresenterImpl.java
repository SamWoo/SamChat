package samwoo.samchat.presenter.impl;

import com.hyphenate.EMCallBack;
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
    private boolean hasMoreData = true;
    public static final int DEFAULT_PAGE_SIZE = 20;

    public ChatPresenterImpl(IChatView chatView) {
        this.chatView = chatView;
        messageList = new ArrayList<>();
    }

    @Override
    public List<EMMessage> getMessages() {
        return messageList;
    }

    @Override
    public void sendMsg(final String userName, final String msg) {
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
    public void loadMsg(final String userName) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(userName);
                if (conversation != null) {
                    //获取此会话的所有消息
                    messageList.addAll(conversation.getAllMessages());
                    //指定会话消息未读数清零
                    conversation.markAllMessagesAsRead();
                }
            }
        });
    }

    @Override
    public void loadMoreMsg(final String userName) {
        if (hasMoreData) {
            ThreadUtils.runOnBackgroundThread(new Runnable() {
                @Override
                public void run() {
                    EMConversation conversation = EMClient.getInstance().chatManager().getConversation(userName);
                    String startMsgId = messageList.get(0).getMsgId();
                    //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
                    //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，
                    // APP中无需再次把获取到的messages添加到会话中
                    List<EMMessage> messages = conversation.loadMoreMsgFromDB(startMsgId, DEFAULT_PAGE_SIZE);
                    hasMoreData = (messages.size() == DEFAULT_PAGE_SIZE);
                    messageList.addAll(0, messages);

                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            chatView.loadMoreMsgSuccessed();
                        }
                    });
                }
            });
        }

    }

    private EMCallBack emCallBack = new EMCallBack() {
        @Override
        public void onSuccess() {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //发送成功
                    chatView.sendMsgSuccessed();
                }
            });

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
