package samwoo.samchat.presenter;

import java.util.List;

import samwoo.samchat.model.ContactItemModel;

/**
 * Created by Administrator on 2017/8/11.
 */

public interface ContactPresenter {
    void getContactsFromServe();

    List<ContactItemModel> getContactsList();

    void deleteFriend(String name);

    void refreshContactList();
}
