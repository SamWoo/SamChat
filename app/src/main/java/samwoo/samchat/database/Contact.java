package samwoo.samchat.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/8/12.
 */

@Entity
public class Contact {
    public static final String TAG = "Contact";

    private Long id;
    private String userName;

    @Generated(hash = 2041396140)
    public Contact(Long id, String userName) {
        this.id = id;
        this.userName = userName;
    }

    @Generated(hash = 672515148)
    public Contact() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
