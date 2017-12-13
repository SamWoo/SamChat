package samwoo.samchat.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.List;

import butterknife.BindView;
import samwoo.samchat.R;
import samwoo.samchat.adapter.ConversationAdapter;
import samwoo.samchat.base.BaseFragment;
import samwoo.samchat.presenter.ConversationPresenter;
import samwoo.samchat.presenter.impl.ConversationPresenterImpl;
import samwoo.samchat.utils.ThreadUtils;
import samwoo.samchat.view.IConversationView;
import samwoo.samchat.widget.RecycleViewDivider;

/**
 * Created by Administrator on 2017/8/7.
 */

public class ConversationFragment extends BaseFragment implements IConversationView {
    @BindView(R.id.top_text)
    TextView mTitle;
    @BindView(R.id.top_back)
    ImageView mBack;
    @BindView(R.id.msg_recycleView)
    RecyclerView recyclerView;

    private ConversationPresenter conversationPresenter;
    private ConversationAdapter adapter;

    @Override
    public int getResLayout() {
        return R.layout.fragment_messages;
    }

    @Override
    public void init() {
        mBack.setVisibility(View.GONE);
        mTitle.setText("消息");

        conversationPresenter = new ConversationPresenterImpl(this);

        initRecyclerView();
        //加载当前所有会话信息
        conversationPresenter.loadAllConversations();

        //设置信息监听器
        EMClient.getInstance().chatManager().addMessageListener(listener);
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        adapter = new ConversationAdapter(getActivity(), conversationPresenter.getConversations());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL, R.drawable.recycleview_divider));
        adapter.setOnItemClickListener(new ConversationAdapter.OnItemClickedListener() {
            @Override
            public void onItemClicked(View view, int position) {
                //跳转到聊天界面
            }
        });
    }

    private EMMessageListener listener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    toast("有一条新消息!!");
                    conversationPresenter.loadAllConversations();
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
        public void onMessageRecalled(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(listener);
    }

    @Override
    public void allConversationLoaded() {
//        toast("加载完全!!");
        adapter.notifyDataSetChanged();
    }
}
