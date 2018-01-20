package samwoo.samchat.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import samwoo.samchat.common.Constants;
import samwoo.samchat.greendao.ContactDao;
import samwoo.samchat.greendao.DaoMaster;
import samwoo.samchat.greendao.DaoSession;

/**
 * Created by Administrator on 2017/8/12.
 */

public class DatabaseManager {
    //封装数据库操作
    private static final String TAG = "DatabaseManager";

    private static DatabaseManager sInstance;
    private DaoSession mDaoSession;

    //单例模式
    public static DatabaseManager getInstance() {
        if (sInstance == null) {
            synchronized (DatabaseManager.class) {
                if (sInstance == null) {
                    sInstance = new DatabaseManager();
                }
            }
        }
        return sInstance;
    }

    //初始化数据库
    public void init(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, Constants.DATABASE_NAME, null);
        SQLiteDatabase writableDatabase = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(writableDatabase);
        mDaoSession = daoMaster.newSession();
    }

    //保存/增加一个联系人
    public void saveContact(String name) {
        Contact contact = new Contact();
        contact.setUserName(name);
        mDaoSession.getContactDao().save(contact);
    }

    //删除所有联系人
    public void deleteAllContact() {
        mDaoSession.getContactDao().deleteAll();
    }

    //删除某一个联系人
    public void deleteContact(String name) {
        Contact contact = mDaoSession.getContactDao()
                .queryBuilder()
                .where(ContactDao.Properties.UserName.eq(name))
                .build()
                .unique();
        if (null != contact) {
            mDaoSession.getContactDao().delete(contact);
        }
    }

    /**
     * 根据昵称查询数据库返回一个符合条件的集合
     *
     * @param name
     * @return
     */
    public ArrayList<String> queryContactByName(String name) {
        List<Contact> list = mDaoSession.getContactDao()
                .queryBuilder()
                .where(ContactDao.Properties.UserName.eq(name))
                .limit(5)
                .build()
                .list();
        ArrayList<String> contacts = new ArrayList<>();
        for (Contact contact : list) {
            String userName = contact.getUserName();
            contacts.add(userName);
        }
        return contacts;
    }

    //查询所有联系人
    public List<String> queryAllContacts() {
        List<Contact> mList = mDaoSession.getContactDao()
                .queryBuilder()
                .list();
        ArrayList<String> contacts = new ArrayList<String>();
        for (int i = 0; i < mList.size(); i++) {
            String contact = mList.get(i).getUserName();
            contacts.add(contact);
        }
        return contacts;
    }
}
