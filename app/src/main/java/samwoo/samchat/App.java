package samwoo.samchat;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zhy.autolayout.config.AutoLayoutConifg;

import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.Bmob;
import samwoo.samchat.database.DatabaseManager;
import samwoo.samchat.ui.ChatActivity;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;

/**
 * Created by Administrator on 2017/8/5.
 */

public class App extends Application {
    private SoundPool mSoundPool;
    private int mSound1;
    private int mSound2;
    public static final boolean DEBUG = false;

    @Override
    public void onCreate() {
        super.onCreate();
        initConfig();
        initHuanxin();
        initBomb();
        initSoundPool();
        initDatabase();
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    private void initConfig() {
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build();

        Fresco.initialize(this, config);
        AutoLayoutConifg.getInstance().useDeviceSize();
        //初始化Zxing组件
        ZXingLibrary.initDisplayOpinion(this);
    }

    //初始化 Database
    private void initDatabase() {
        DatabaseManager.getInstance().init(this);
    }

    //初始化环信SDK
    private void initHuanxin() {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null || !processAppName.equalsIgnoreCase(getPackageName())) {
            Log.e("Sam", "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        //初始化
        EMClient.getInstance().init(getApplicationContext(), options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }

    //获取AppName
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    //初始化bomb云储存
    private void initBomb() {
        Bmob.initialize(this, "242318742053669bca57c40ad6532b85");
    }

    //初始化消息提示音
    private void initSoundPool() {
        mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        mSound1 = mSoundPool.load(this, R.raw.duan, 1);
        mSound2 = mSoundPool.load(this, R.raw.yulu, 1);
    }

    /**
     * 消息监听器，后台实时监听消息
     */
    private EMMessageListener msgListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //处理收到消息时显示notification
            if (isForegroud()) {
                mSoundPool.play(mSound1, 1, 1, 0, 0, 1);
            } else {
                mSoundPool.play(mSound2, 1, 1, 0, 0, 1);
                showNotification(messages.get(0));
            }
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {

        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> messages) {

        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {

        }
    };

    /**
     * 显示新消息来临时的通知
     *
     * @param message
     */
    private void showNotification(EMMessage message) {
        String content = "";
        if (message.getBody() instanceof EMTextMessageBody)
            content = ((EMTextMessageBody) message.getBody()).getMessage();
        //设置通知点击跳转意图
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("user_name", message.getUserName());
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //通知设置
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(getString(R.string.new_msg))//通知标题
                .setContentText(content)//通知内容
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.avatar_1))//显示通知大图标(下拉状态)
                .setSmallIcon(R.drawable.ic_conversation_selected_1)//显示通知小图标
                .setContentIntent(pi)//点击通知跳转Intent
                .setPriority(Notification.PRIORITY_HIGH)//通知级别（已废除）
                .setLights(Color.GREEN, 1000, 1000)//设置通知来时LED灯闪烁显示
                .setVibrate(new long[]{0, 1000, 1000, 1000})//设置通知来时震动
                .setAutoCancel(true)//自动取消通知
                .build();
        notificationManager.notify(1, notification);
    }

    /**
     * 判断当前应用是否处于活动状态
     *
     * @return
     */
    public boolean isForegroud() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = activityManager.getRunningAppProcesses();
        if (runningAppProcessInfos == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo info : runningAppProcessInfos) {
            if (info.processName.equals(getPackageName()) && info.importance == IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }
}
