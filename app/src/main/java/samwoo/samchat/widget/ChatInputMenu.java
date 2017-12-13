package samwoo.samchat.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import butterknife.BindView;
import samwoo.samchat.R;

/**
 * Created by Administrator on 2017/10/2.
 * 聊天页面底部的聊天输入菜单栏
 * 主要包含3个控件:ChatPrimaryMenu(主菜单栏，包含文字输入、发送等功能),
 * ChatExtendMenu(扩展栏，点击加号按钮出来的小宫格的菜单栏),
 * 以及EmojiconMenu(表情栏)
 */

public class ChatInputMenu extends LinearLayout {
    @BindView(R.id.primary_menu_container)
    FrameLayout primaryMenuContainer;
    @BindView(R.id.emojicon_menu_container)
    FrameLayout emojiconMenuContainer;
    @BindView(R.id.extend_menu_container)
    FrameLayout extendMenuContainer;

    private Context context;
    private boolean inited;

    public ChatInputMenu(Context context) {
        super(context);
        init(context, null);
    }

    public ChatInputMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ChatInputMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ChatInputMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.widget_chat_input_menu, this);

    }

    public void initView(){
        if (inited) return;
    }
}
