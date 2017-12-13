package samwoo.samchat.ui;

import android.content.Intent;
import android.os.Bundle;

import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.hyphenate.easeui.ui.EaseChatFragment;

import samwoo.samchat.R;

/**
 * Created by Administrator on 2017/10/2.
 */

public class ChatActivity extends EaseBaseActivity {
    private static ChatActivity activityInstance;
    private EaseChatFragment chatFragment;
    private String toChatUserName;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_ease_chat);
        activityInstance = this;
        //聊天人或群id
        toChatUserName = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);
        chatFragment = new EaseChatFragment();
        //传入参数
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String userName = intent.getStringExtra("user_name");
        if (toChatUserName.equals(userName)) {
            super.onNewIntent(intent);
        } else {
            finish();
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        chatFragment.onBackPressed();
    }

    public String getChatUserName() {
        return toChatUserName;
    }
}
