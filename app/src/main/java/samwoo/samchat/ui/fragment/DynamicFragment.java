package samwoo.samchat.ui.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import samwoo.samchat.App;
import samwoo.samchat.R;
import samwoo.samchat.adapter.ContactAdapter;
import samwoo.samchat.adapter.DynamicAdapter;
import samwoo.samchat.adapter.DynamicListAdapter;
import samwoo.samchat.base.BaseFragment;
import samwoo.samchat.model.DynamicItemModel;
import samwoo.samchat.widget.RecycleViewDivider;

/**
 * Created by Administrator on 2017/8/7.
 */

public class DynamicFragment extends BaseFragment {
    @BindView(R.id.top_back)
    ImageView mBack;
    @BindView(R.id.top_text)
    TextView mTitle;
    @BindView(R.id.dynamic_recycle)
    RecyclerView recyclerView;
    @BindView(R.id.layout_swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.listview_dynamic)
    ListView listView;

    public static final String TAG = "DynamicFragement";
    private List<DynamicItemModel> modelList = new ArrayList<DynamicItemModel>();
    //    private List<String> list = new ArrayList<String>();
    private DynamicAdapter adapter;

    public static final String[] PHOTOS = {
            "http://f.hiphotos.baidu.com/image/pic/item/faf2b2119313b07e97f760d908d7912396dd8c9c.jpg",
            "http://g.hiphotos.baidu.com/image/pic/item/4b90f603738da977c76ab6fab451f8198718e39e.jpg",
            "http://e.hiphotos.baidu.com/image/pic/item/902397dda144ad343de8b756d4a20cf430ad858f.jpg",
            "http://a.hiphotos.baidu.com/image/pic/item/a6efce1b9d16fdfa0fbc1ebfb68f8c5495ee7b8b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/a71ea8d3fd1f4134e61e0f90211f95cad1c85e36.jpg",
            "http://c.hiphotos.baidu.com/image/pic/item/7dd98d1001e939011b9c86d07fec54e737d19645.jpg",
            "http://f.hiphotos.baidu.com/image/pic/item/f11f3a292df5e0fecc3e83ef586034a85edf723d.jpg",
            "http://pica.nipic.com/2007-10-17/20071017111345564_2.jpg",
            "http://pic4.nipic.com/20091101/3672704_160309066949_2.jpg",
            "http://pic4.nipic.com/20091203/1295091_123813163959_2.jpg",
            "http://pic2.ooopic.com/11/79/98/31bOOOPICb1_1024.jpg"
    };

    @Override
    public int getResLayout() {
        return R.layout.fragment_dynamic;
    }

    @Override
    public void init() {
        mBack.setVisibility(View.GONE);
        mTitle.setText("动态");
        initData();
//        initListview();
        initRecycleView();
        initSwipeRefresh();
    }

    private void initListview() {
        DynamicListAdapter listAdapter = new DynamicListAdapter(getContext(), modelList);
        listView.setAdapter(listAdapter);
    }


    private void initSwipeRefresh() {
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.btn_blue_normal), getResources().getColor(R.color.colorRed));
        swipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
    }

    private void initRecycleView() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(false);
        adapter = new DynamicAdapter(getContext(), modelList);
        if (App.DEBUG) {
            for (int k = 0; k < modelList.size(); k++) {
                for (int j = 0; j < modelList.get(k).getImages().size(); j++) {
                    Log.e("Sam", "path" + "[" + k + "]" + "[" + j + "]" + "======" + modelList.get(k).getImages().get(j) + "\n");
                }
            }
        }
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, R.drawable.recycleview_divider));
    }

    /**
     * 测试用例数据源
     */
    private void initData() {
        modelList.clear();
//        list.clear();
        DynamicItemModel model;
        List<String> list;
        //装载图片path
//        for (int i = 0; i < PHOTOS.length; i++) {
//            list.add(PHOTOS[i]);
//        }

        //设置测试用数据源
        for (int i = 0; i < 10; i++) {
            list = new ArrayList<String>();
            model = new DynamicItemModel();
            Random random = new Random();
            int N = random.nextInt(9) + 1;
            if (App.DEBUG) Log.e("Sam", "N====" + N);

            if (N % 2 == 0) {
                model.setAvatarId(R.drawable.avatar_1);
                model.setName("Helen");
                model.setDynamicText("Hi, boy! Can I help you?");
            } else {
                model.setAvatarId(R.drawable.avatar_2);
                model.setName("Bob");
                model.setDynamicText("Hi, Girl! Nice to meet you!!");
            }

            for (int j = 0; j < N; j++) {
                list.add(PHOTOS[j]);
            }

            model.setImages(list);
            modelList.add(model);
//            for (int k = 0; k < list.size(); k++) {
//                Log.e("Sam", "path======" + model.getImages().get(k) + "\n");
//            }
        }
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
    };

}
