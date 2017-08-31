package samwoo.samchat.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/30.
 * Activity集合工具类，方便在任意界面退出应用
 */

public class ActivityCollector {
    private static List<Activity> activities = new ArrayList<>();

    //add activity into List
    public static void add(Activity activity) {
        activities.add(activity);
    }

    //remove activity out List
    public static void remove(Activity activity) {
        activities.remove(activity);
    }

    //finish all activity
    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
