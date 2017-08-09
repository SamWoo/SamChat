package samwoo.samchat.widget;

import android.annotation.TargetApi;
import android.content.Context;
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

public class ReceiveMsgView extends RelativeLayout {
    @BindView(R.id.img_avatar)
    ImageView mAvatar;
    @BindView(R.id.tv_receive_msg)
    TextView mReceiveMsg;
    @BindView(R.id.time_stamp)
    TextView mTimeStamp;

    public ReceiveMsgView(Context context) {
        super(context);
        init();
    }

    public ReceiveMsgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ReceiveMsgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ReceiveMsgView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_receive_message_item, this);
        ButterKnife.bind(this, this);
    }

    public void bindView(EMMessage message, boolean showTimeStamp) {
        if (showTimeStamp) {
            mTimeStamp.setVisibility(VISIBLE);
            mTimeStamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
        } else {
            mTimeStamp.setVisibility(GONE);
        }

        EMMessageBody body = message.getBody();
        if (body instanceof EMTextMessageBody) {
            mReceiveMsg.setText(((EMTextMessageBody) body).getMessage());
        } else if (body instanceof EMImageMessageBody) {

        } else {
            mReceiveMsg.setText("没有新消息");
        }
    }
}
