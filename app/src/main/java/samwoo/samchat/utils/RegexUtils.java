package samwoo.samchat.utils;

import android.text.TextUtils;

/**
 * Created by Administrator on 2017/8/6.
 */

public class RegexUtils {
    public static final String USER_NAME_REGEX = "^[a-zA-Z]\\w{2,19}$";
    public static final String USER_PASSWORD_REGEX = "^[0-9]{3,20}$";

    public static boolean checkName(String name) {
        return name.matches(USER_NAME_REGEX);
    }

    public static boolean checkPassword(String password) {
        return password.matches(USER_PASSWORD_REGEX);
    }
}
