package samwoo.samchat.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import samwoo.samchat.R;
import samwoo.samchat.base.BaseActivity;
import samwoo.samchat.widget.RecycleViewDivider;

/**
 * Created by Administrator on 2017/8/12.
 */

public class AddContactActivity extends BaseActivity {
    @BindView(R.id.top_text)
    TextView mTitle;
    @BindView(R.id.add)
    ImageView mAdd;
    @BindView(R.id.add_recycleview)
    RecyclerView mRecyclerView;

    @Override
    public void init() {
        mTitle.setText("添加联系人");
        mAdd.setVisibility(View.GONE);
        initRecycleView();
    }

    private void initRecycleView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new RecycleViewDivider(getApplicationContext(), LinearLayoutManager.VERTICAL, R.drawable.recycleview_divider));
    }

    @Override
    public int getLayoutResoure() {
        return R.layout.activity_add_contact;
    }

    @OnClick(R.id.top_back)
    public void onClickedView(View view) {
        switch (view.getId()) {
            case R.id.top_back:
                onBackPressed();
                break;
        }
    }
}
