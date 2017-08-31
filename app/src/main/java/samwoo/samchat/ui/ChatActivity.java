package samwoo.samchat.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import samwoo.samchat.R;
import samwoo.samchat.adapter.MessageListAdapter;
import samwoo.samchat.base.BaseActivity;
import samwoo.samchat.presenter.ChatPresenter;
import samwoo.samchat.presenter.impl.ChatPresenterImpl;
import samwoo.samchat.utils.ThreadUtils;
import samwoo.samchat.view.IChatView;

/**
 * Created by Administrator on 2017/8/8.
 */

public class ChatActivity extends BaseActivity implements IChatView {
    @BindView(R.id.top_text)
    TextView mTitle;
    @BindView(R.id.top_back)
    ImageView mBack;
    @BindView(R.id.chat_edit)
    EditText mChatEdit;
    @BindView(R.id.chat_recycler)
    RecyclerView mChatRecyclerView;
    @BindView(R.id.send)
    TextView mSendMsg;

    private ChatPresenter chatPresenter;
    private MessageListAdapter adapter;
    private LinearLayoutManager manager;
    private String userName;

    @Override
    public void init() {
        userName = getIntent().getStringExtra("user_name");
        mTitle.setText(userName);
        chatPresenter = new ChatPresenterImpl(this);
        mChatEdit.addTextChangedListener(mTextWatcher);
        mChatEdit.setOnEditorActionListener(mOnEditActionListener);

        manager = new LinearLayoutManager(this);
        mChatRecyclerView.setLayoutManager(manager);
        adapter = new MessageListAdapter(this, chatPresenter.getMessages());
        mChatRecyclerView.setAdapter(adapter);
        mChatRecyclerView.addOnScrollListener(mOnScrollListener);

        EMClient.getInstance().chatManager().addMessageListener(emMessageListener);
        chatPresenter.loadMsg(userName);
    }

    @Override
    public int getLayoutResoure() {
        return R.layout.activity_chat;
    }

    @OnClick({R.id.send, R.id.top_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.send:
                sendMsg();
                break;
            case R.id.top_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    private void sendMsg() {
        String msg = mChatEdit.getText().toString().trim();
        chatPresenter.sendMsg(userName, msg);
        hideKeyBoard();
        mChatEdit.getText().clear();
    }

    //滚动监听
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
                if (firstVisibleItemPosition == 0) {
//                    Log.e("Sam", "我在滚动！！！！"+firstVisibleItemPosition);
                    chatPresenter.loadMoreMsg(userName);
                }
            }
        }
    };

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            mSendMsg.setEnabled(editable.length() != 0);
        }
    };

    private TextView.OnEditorActionListener mOnEditActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_SEND) {
                sendMsg();
                return true;
            }
            return false;
        }
    };

    private EMMessageListener emMessageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final EMMessage msg = messages.get(0);
                    if (msg.getUserName().equals(userName)) {
                        adapter.addNewMsg(msg);
                        //清空当前聊天对象的未读信息数
                        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(userName);
                        conversation.markAllMessagesAsRead();
                    }
                }
            });
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

    @Override
    public void sendMsgSuccessed() {
        adapter.notifyDataSetChanged();
        mChatRecyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public void loadMoreMsgSuccessed() {
        adapter.notifyDataSetChanged();
//        toast("加载更多消息完成!!");
    }
}
