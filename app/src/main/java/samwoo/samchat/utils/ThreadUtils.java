package samwoo.samchat.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/8/6.
 */

public class ThreadUtils {
    private static Executor executor = Executors.newSingleThreadExecutor();
    private static Handler handler = new Handler(Looper.getMainLooper());

    public static void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

    public static void runOnBackgroundThread(Runnable runnable) {
        executor.execute(runnable);
    }
}
