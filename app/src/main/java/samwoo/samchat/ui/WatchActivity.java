package samwoo.samchat.ui;

import butterknife.BindView;
import samwoo.samchat.R;
import samwoo.samchat.base.BaseActivity;
import samwoo.samchat.widget.WatchView;

/**
 * Created by Administrator on 2017/12/15.
 */

public class WatchActivity extends BaseActivity {
    @BindView(R.id.watch_view)
    WatchView watchView;

    @Override
    public void init() {
        watchView.run();
    }

    @Override
    public int getLayoutResoure() {
        return R.layout.activity_watch;
    }
}
