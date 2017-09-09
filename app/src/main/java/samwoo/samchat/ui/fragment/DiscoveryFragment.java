package samwoo.samchat.ui.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import samwoo.samchat.R;
import samwoo.samchat.base.BaseFragment;
import samwoo.samchat.ui.DynamicActivity;
import samwoo.samchat.widget.DiscoveryView;

/**
 * Created by Administrator on 2017/9/9.
 */

public class DiscoveryFragment extends BaseFragment {
    @BindView(R.id.top_back)
    ImageView mBack;
    @BindView(R.id.top_text)
    TextView mTitle;
    @BindView(R.id.add)
    ImageView mAdd;

    @Override
    public int getResLayout() {
        return R.layout.framgent_discovery;
    }

    @Override
    public void init() {
        mAdd.setVisibility(View.GONE);
        mBack.setVisibility(View.GONE);
        mTitle.setText("发现");
    }

    @OnClick({R.id.friend_circle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.friend_circle:
                startActivity(DynamicActivity.class, false);
                break;
        }
    }
}
