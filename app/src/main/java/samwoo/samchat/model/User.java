package samwoo.samchat.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/8/7.
 */

public class User extends BmobObject {
    private String name;
    private String password;

    public User(String name, String password) {
        this.password = password;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
