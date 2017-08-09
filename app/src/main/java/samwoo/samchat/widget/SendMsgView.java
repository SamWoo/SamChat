package samwoo.samchat.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import samwoo.samchat.R;

/**
 * Created by Administrator on 2017/8/9.
 */

public class SendMsgView extends RelativeLayout {
    @BindView(R.id.time_stamp)
    TextView mTimeStamp;
    @BindView(R.id.img_avatar)
    ImageView mAvatar;
    @BindView(R.id.tv_send_msg)
    TextView mSendMsg;
    @BindView(R.id.img_msg_progress)
    ImageView msgProgress;

    public SendMsgView(Context context) {
        super(context);
        init();
    }

    public SendMsgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SendMsgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SendMsgView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_send_message_item, this);
        ButterKnife.bind(this, this);
    }

    public void bindView(EMMessage message, boolean showTimestamp) {
        if (showTimestamp) {
            mTimeStamp.setVisibility(VISIBLE);
            String time = DateUtils.getTimestampString(new Date(message.getMsgTime()));
            mTimeStamp.setText(time);
        } else {
            mTimeStamp.setVisibility(GONE);
        }

        EMMessageBody body = message.getBody();
        if (body instanceof EMTextMessageBody) {
            //文本消息
            mSendMsg.setText(((EMTextMessageBody) body).getMessage());
        } else if (body instanceof EMImageMessageBody) {
            //图片消息
        } else {
            mSendMsg.setText("没有新消息!!");
        }

        switch (message.status()) {
            case INPROGRESS:
                msgProgress.setVisibility(VISIBLE);
                msgProgress.setImageResource(R.drawable.send_msg_progress);
                AnimationDrawable drawable = (AnimationDrawable) msgProgress.getDrawable();
                drawable.start();
                break;
            case SUCCESS:
                msgProgress.setVisibility(GONE);
                break;
            case FAIL:
                msgProgress.setImageResource(R.drawable.msg_error);
                break;
        }
    }

}
