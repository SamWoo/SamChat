package samwoo.samchat.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import samwoo.samchat.presenter.ConversationPresenter;
import samwoo.samchat.utils.ThreadUtils;
import samwoo.samchat.view.IConversationView;

/**
 * Created by Administrator on 2017/8/8.
 */

public class ConversationPresenterImpl implements ConversationPresenter {
    private IConversationView conversationView;
    private List<EMConversation> mList;

    public ConversationPresenterImpl(IConversationView conversationView) {
        this.conversationView = conversationView;
        mList = new ArrayList<EMConversation>();
    }

    @Override
    public List<EMConversation> getConversations() {
        return mList;
    }

    @Override
    public void loadAllConversations() {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                Map<String, EMConversation> conversationMap = EMClient.getInstance().chatManager().getAllConversations();
                mList.clear();
                mList.addAll(conversationMap.values());

                Collections.sort(mList, new Comparator<EMConversation>() {
                    @Override
                    public int compare(EMConversation c1, EMConversation c2) {
                        return (int) (c2.getLastMessage().getMsgTime() - c1.getLastMessage().getMsgTime());
                    }
                });

                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        conversationView.allConversationLoaded();
                    }
                });
            }
        });
    }
}
