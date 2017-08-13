package samwoo.samchat.model;

import samwoo.samchat.R;

/**
 * Created by Administrator on 2017/8/11.
 */

public class ContactItemModel {
    public int avatar = R.drawable.avatar_1;
    public String name;
    public boolean showFirstLetter = true;

    public char getFirstLetter() {
        return name.charAt(0);
    }

    public String getFirstLetterString() {
        //将姓名的首字母大写返回
        return String.valueOf(getFirstLetter()).toUpperCase();
    }
}
