package samwoo.samchat.presenter;

import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by Administrator on 2017/8/8.
 */

public interface ChatPresenter {
    List<EMMessage> getMessages();

    void sendMsg(String userName, String msg);

    void loadMsg(String userName);

    void loadMoreMsg(String userName);
}
