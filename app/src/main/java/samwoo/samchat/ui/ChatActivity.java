package samwoo.samchat.ui;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import samwoo.samchat.R;
import samwoo.samchat.base.BaseActivity;

/**
 * Created by Administrator on 2017/8/8.
 */

public class ChatActivity extends BaseActivity {
    @BindView(R.id.top_text)
    TextView mTitle;
    @BindView(R.id.top_back)
    ImageView mBack;
    @BindView(R.id.chat_edit)
    EditText mChatEdit;

    @Override
    public void init() {
        String userName = getIntent().getStringExtra("user_name");
        mTitle.setText(userName);
    }

    @Override
    public int getLayoutResoure() {
        return R.layout.activity_chat;
    }

    @OnClick({R.id.send, R.id.top_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.send:

                break;
            case R.id.top_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }
}
