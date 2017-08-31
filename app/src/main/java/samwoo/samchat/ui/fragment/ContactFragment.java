package samwoo.samchat.ui.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import samwoo.samchat.App;
import samwoo.samchat.R;
import samwoo.samchat.adapter.ContactAdapter;
import samwoo.samchat.base.BaseFragment;
import samwoo.samchat.model.ContactItemModel;
import samwoo.samchat.presenter.ContactPresenter;
import samwoo.samchat.presenter.impl.ContactPresenterImpl;
import samwoo.samchat.ui.AddContactActivity;
import samwoo.samchat.ui.ChatActivity;
import samwoo.samchat.view.IContactView;
import samwoo.samchat.widget.RecycleViewDivider;
import samwoo.samchat.widget.SlideBar;

/**
 * Created by Administrator on 2017/8/7.
 */

public class ContactFragment extends BaseFragment implements IContactView {
    public static final String TAG = "ContactFragment";
    @BindView(R.id.top_back)
    ImageView mBack;
    @BindView(R.id.top_text)
    TextView mTitle;
    @BindView(R.id.add)
    ImageView mAdd;
    @BindView(R.id.slid_bar)
    SlideBar mSlidBar;
    @BindView(R.id.contract_section)
    TextView mSection;
    @BindView(R.id.rv_contracts)
    RecyclerView mRecycleView;
    @BindView(R.id.layout_swipe)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private ContactPresenter contactPresenter;
    private ContactAdapter adapter;


    @Override
    public int getResLayout() {
        return R.layout.fragment_contacts;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void init() {
        mAdd.setVisibility(View.VISIBLE);
        mBack.setVisibility(View.GONE);
        mTitle.setText("联系人");
        contactPresenter = new ContactPresenterImpl(this);

        initRecycleView();
        initSwipeRefresh();

        EMClient.getInstance().contactManager().setContactListener(contactListener);
        contactPresenter.getContactsFromServe();

    }

    private void initSwipeRefresh() {
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.btn_blue_normal), getResources().getColor(R.color.colorRed));
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mSlidBar.setOnSlidBarChangeListener(mOnSlidBarChangeListener);
    }

    private void initRecycleView() {
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycleView.setHasFixedSize(true);
        adapter = new ContactAdapter(getContext(), contactPresenter.getContactsList());
        if (App.DEBUG) {
            Log.e(TAG, "数据加载--=====" + contactPresenter.getContactsList());
        }
        adapter.setOnItemClicked(onItemClickedListener);
        mRecycleView.setAdapter(adapter);
        mRecycleView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL, R.drawable.recycleview_divider));
    }

    @OnClick(R.id.add)
    public void onViewCliked(View view) {
        switch (view.getId()) {
            case R.id.add:
                startActivity(AddContactActivity.class, false);
                break;
        }
    }

    //右侧字母索引表滑动监听
    SlideBar.OnSlideBarChangeListener mOnSlidBarChangeListener = new SlideBar.OnSlideBarChangeListener() {
        @Override
        public void onSlidingFinish() {
            mSection.setVisibility(View.GONE);
        }

        @Override
        public void onSectionChange(int index, String section) {
            mSection.setVisibility(View.VISIBLE);
            mSection.setText(section);
            scrollToSection(section);
        }
    };

    /**
     * RecyclerView滚动直到界面出现对应section的联系人
     *
     * @param section 首字符
     */
    private void scrollToSection(String section) {
        int sectionPosition = getSectionPosition(section);
        if (sectionPosition != -1) {
            mRecycleView.smoothScrollToPosition(sectionPosition);
        }
    }

    /**
     * @param section 首字符
     * @return 在联系人列表中首字符是section的第一个联系人在联系人列表中的位置
     */
    private int getSectionPosition(String section) {
        List<ContactItemModel> contactItemModels = adapter.getContactItemModels();
        for (int i = 0; i < contactItemModels.size(); i++) {
            if (section.equals(contactItemModels.get(i).getFirstLetterString())) {
                return i;
            }
        }
        return -1;
    }

    //下拉刷新ContactList
    SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            contactPresenter.refreshContactList();
        }
    };

    //item点击事件监听
    private ContactAdapter.OnItemClickedListener onItemClickedListener = new ContactAdapter.OnItemClickedListener() {
        /**
         * 单击跳转到聊天界面
         * @param name 点击item的联系人名字
         */
        @Override
        public void onClicked(String name) {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra("user_name", name);
            startActivity(intent);
        }

        /**
         * 长按删除好友
         * @param name 点击item的联系人名字
         */
        @Override
        public void onLongClicked(String name) {
            showDeleteFriendDialog(name);
        }
    };

    private void showDeleteFriendDialog(String name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("确定删除" + name + "?")
                .setTitle("删除好友")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        contactPresenter.deleteFriend(name);
                    }
                }).create().show();
    }

    //联系人增删监听
    private EMContactListener contactListener = new EMContactListener() {
        @Override
        public void onContactAdded(String username) {
            contactPresenter.refreshContactList();
        }

        @Override
        public void onContactDeleted(String username) {
            contactPresenter.refreshContactList();
        }

        @Override
        public void onContactInvited(String username, String reason) {

        }

        @Override
        public void onFriendRequestAccepted(String username) {

        }

        @Override
        public void onFriendRequestDeclined(String username) {

        }
    };

    @Override
    public void getContactsSuccessed() {
        adapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
//        toast("获取联系人完成!!");
    }

    @Override
    public void deleteContactSuccessed() {
        contactPresenter.refreshContactList();
        toast("已删除联系人!!");
    }

    @Override
    public void getContactsFailed() {
        toast("获取联系人失败!!");
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void deleteContactFailed() {
        toast("删除联系人失败!!");
    }
}
