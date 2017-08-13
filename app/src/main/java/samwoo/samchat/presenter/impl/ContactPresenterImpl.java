package samwoo.samchat.presenter.impl;

import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import samwoo.samchat.App;
import samwoo.samchat.database.DatabaseManager;
import samwoo.samchat.model.ContactItemModel;
import samwoo.samchat.presenter.ContactPresenter;
import samwoo.samchat.utils.ThreadUtils;
import samwoo.samchat.view.IContactView;

/**
 * Created by Administrator on 2017/8/11.
 */

public class ContactPresenterImpl implements ContactPresenter {
    public static final String TAG = "ContactPresenterImpl";

    private IContactView contactView;
    private List<ContactItemModel> mContactList;

    public ContactPresenterImpl(IContactView contactView) {
        this.contactView = contactView;
        mContactList = new ArrayList<ContactItemModel>();
    }

    //返回联系人列表
    @Override
    public List<ContactItemModel> getContactsList() {
        return mContactList;
    }

    //获取服务器上所有的联系人列表
    @Override
    public void getContactsFromServe() {
        if (mContactList.size() > 0) {
            notifyGetContactListSuccess();
            return;
        }

        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                mContactList.clear();
                List<String> contacts = null;
                try {
                    contacts = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    Collections.sort(contacts, new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            return o1.charAt(0) - o2.charAt(0);
                        }
                    });

                    DatabaseManager.getInstance().deleteAllContact();
                    if (!contacts.isEmpty()) {
                        for (int i = 0; i < contacts.size(); i++) {
                            ContactItemModel item = new ContactItemModel();
                            item.name = contacts.get(i);
                            if (itemInSameGroup(i, item)) {
                                item.showFirstLetter = false;
                            }
                            mContactList.add(item);
                            if (App.DEBUG) {
                                Log.e(TAG, "contactList=====" + contacts.get(i) + "*******ContactList******" + mContactList.get(i).name);
                            }
                            DatabaseManager.getInstance().saveContact(item.name);
                        }
                    }
                    notifyGetContactListSuccess();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    notifyGetContactListFailed();
                }

            }
        });
    }

    private void notifyGetContactListFailed() {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                contactView.getContactsFailed();
            }
        });
    }

    private void notifyGetContactListSuccess() {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                contactView.getContactsSuccessed();
            }
        });
    }

    /**
     * 当前联系人跟上个联系人比较，如果首字符相同则返回true
     *
     * @param i         当前联系人下标
     * @param itemModel 当前联系人数据模型
     * @return true 表示当前联系人和上一联系人在同一组
     */
    private boolean itemInSameGroup(int i, ContactItemModel itemModel) {
        return i > 0 && (itemModel.getFirstLetter() == mContactList.get(i - 1).getFirstLetter());
    }

    @Override
    public void deleteFriend(String name) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(name);
                    deleteContactSuccessed();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    deleteContactFailed();
                }
            }
        });
    }

    @Override
    public void refreshContactList() {
        mContactList.clear();
        getContactsFromServe();
    }

    private void deleteContactFailed() {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                contactView.deleteContactFailed();
            }
        });
    }

    private void deleteContactSuccessed() {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                contactView.deleteContactSuccessed();
            }
        });
    }
}
