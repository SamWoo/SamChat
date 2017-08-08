package samwoo.samchat.presenter;

import com.hyphenate.chat.EMConversation;

import java.util.List;

/**
 * Created by Administrator on 2017/8/8.
 */

public interface ConversationPresenter {
    List<EMConversation> getConversations();

    void loadAllConversations();

}
