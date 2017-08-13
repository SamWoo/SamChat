package samwoo.samchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.DateUtils;

import java.util.List;

import samwoo.samchat.widget.ReceiveMsgView;
import samwoo.samchat.widget.SendMsgView;

/**
 * Created by Administrator on 2017/8/8.
 */

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<EMMessage> mList;
    private static final int ITEM_TYPE_SEND_MESSAGE = 0;
    private static final int ITEM_TYPE_RECEIVE_MESSAGE = 1;
    private boolean showTimestamp;

    public MessageListAdapter(Context context, List<EMMessage> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_SEND_MESSAGE) {
            return new SendMsgViewHolder(new SendMsgView(context));
        } else {
            return new RecevieMsgViewHolder(new ReceiveMsgView(context));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0 || tooClosedTimeStamp(position)) {
            showTimestamp = true;
        } else {
            showTimestamp = false;
        }

        if (holder instanceof SendMsgViewHolder) {
            ((SendMsgViewHolder) holder).sendMsgView.bindView(mList.get(position), showTimestamp);
        } else if (holder instanceof RecevieMsgViewHolder) {
            ((RecevieMsgViewHolder) holder).receiveMsgView.bindView(mList.get(position), showTimestamp);
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage message = mList.get(position);
        return message.direct() == EMMessage.Direct.SEND ? ITEM_TYPE_SEND_MESSAGE : ITEM_TYPE_RECEIVE_MESSAGE;
    }

    public class SendMsgViewHolder extends RecyclerView.ViewHolder {
        public SendMsgView sendMsgView;

        public SendMsgViewHolder(View itemView) {
            super(itemView);
            sendMsgView = (SendMsgView) itemView;
        }
    }

    public class RecevieMsgViewHolder extends RecyclerView.ViewHolder {
        public ReceiveMsgView receiveMsgView;

        public RecevieMsgViewHolder(View itemView) {
            super(itemView);
            receiveMsgView = (ReceiveMsgView) itemView;
        }
    }

    //两条消息时间太近就不显示时间戳
    private boolean tooClosedTimeStamp(int position) {
        long currentItemTimestamp = mList.get(position).getMsgTime();
        long preItemTimeStamp = mList.get(position - 1).getMsgTime();
        return !DateUtils.isCloseEnough(currentItemTimestamp, preItemTimeStamp);
    }

    public void addNewMsg(EMMessage message) {
        mList.add(message);
        notifyDataSetChanged();
    }
}
