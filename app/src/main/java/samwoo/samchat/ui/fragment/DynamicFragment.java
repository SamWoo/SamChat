package samwoo.samchat.ui.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import samwoo.samchat.R;
import samwoo.samchat.base.BaseFragment;

/**
 * Created by Administrator on 2017/8/7.
 */

public class DynamicFragment extends BaseFragment {
    @BindView(R.id.top_back)
    ImageView mBack;
    @BindView(R.id.top_text)
    TextView mTitle;

    @Override
    public int getResLayout() {
        return R.layout.fragment_messages;
    }

    @Override
    public void init() {
        mBack.setVisibility(View.GONE);
        mTitle.setText("动态");
    }
}
