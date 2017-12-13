package samwoo.samchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import samwoo.samchat.R;
import samwoo.samchat.ui.ChatActivity;
import samwoo.samchat.ui.ChatActivityBak;

/**
 * Created by Administrator on 2017/8/8.
 */

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> implements View.OnClickListener {
    private Context context;
    private List<EMConversation> msgList;
    private EMConversation conversation;
    private OnItemClickedListener listener = null;

    @Override
    public void onClick(View view) {
//        if (listener != null) {
//            listener.onItemClicked(view, (int) view.getTag());
//        }
        //获取当前会话实体
        conversation = msgList.get((int) view.getTag());
        //跳转到聊天界面
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("user_name", conversation.conversationId());
        context.startActivity(intent);
        //清空未读消息数
        conversation.markAllMessagesAsRead();
    }

    public static interface OnItemClickedListener {
        void onItemClicked(View view, int position);
    }

    public ConversationAdapter(Context context, List<EMConversation> msgList) {
        this.context = context;
        this.msgList = msgList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View msgView = LayoutInflater.from(context).inflate(R.layout.item_messages, null);
        msgView.setOnClickListener(this);
        return new ViewHolder(msgView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //显示消息来源者名称
        holder.mUserName.setText(msgList.get(position).conversationId());
        //显示消息
        EMMessage message = msgList.get(position).getLastMessage();
        if (message.getBody() instanceof EMTextMessageBody) {
            holder.mLastMsg.setText(((EMTextMessageBody) message.getBody()).getMessage());
        } else {
            holder.mLastMsg.setText("没有新消息!!");
        }
        //显示时间邮戳
        holder.mTimeStamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
        //显示未读消息数
        int unreadMsg = msgList.get(position).getUnreadMsgCount();
        if (unreadMsg > 0) {
            holder.mUnreadCount.setVisibility(View.VISIBLE);
            holder.mUnreadCount.setText(String.valueOf(unreadMsg));
        } else {
            holder.mUnreadCount.setVisibility(View.GONE);
        }
        //设置位置标签，便于点击事件位置绑定
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return msgList == null ? 0 : msgList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_avatar)
        ImageView mAvatar;
        @BindView(R.id.last_msg)
        TextView mLastMsg;
        @BindView(R.id.unread_count)
        TextView mUnreadCount;
        @BindView(R.id.time_stamp)
        TextView mTimeStamp;
        @BindView(R.id.user_name)
        TextView mUserName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    //暴露外部点击事件接口供外部事件调用
    public void setOnItemClickListener(OnItemClickedListener listener) {
        this.listener = listener;
    }
}
