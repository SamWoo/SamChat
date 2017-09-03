package samwoo.samchat.ui.fragment;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import samwoo.samchat.R;
import samwoo.samchat.base.BaseFragment;
import samwoo.samchat.widget.MultiImageView;

/**
 * Created by Administrator on 2017/9/3.
 */

public class SettingFragment extends BaseFragment {
    @BindView(R.id.image_friend)
    ImageView imageView;
    @BindView(R.id.tv_friend_dynamic)
    TextView tv_dynamic;
    @BindView(R.id.tv_friend_name)
    TextView tv_name;
    @BindView(R.id.multi_image)
    MultiImageView multiImageView;
    @BindView(R.id.tv_comment)
    TextView tv_comment;

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
            "http://pic2.ooopic.com/11/79/98/31bOOOPICb1_1024.jpg",
            "http://f.hiphotos.baidu.com/image/pic/item/faf2b2119313b07e97f760d908d7912396dd8c9c.jpg",
            "http://g.hiphotos.baidu.com/image/pic/item/4b90f603738da977c76ab6fab451f8198718e39e.jpg",
            "http://e.hiphotos.baidu.com/image/pic/item/902397dda144ad343de8b756d4a20cf430ad858f.jpg"
    };

    private List<String> mList = new ArrayList<String>();

    @Override
    public int getResLayout() {
        return R.layout.item_dynamic;
    }

    @Override
    public void init() {
        mList.clear();
        for (int i = 0; i < PHOTOS.length; i++) {
            mList.add(PHOTOS[i]);
        }

        multiImageView.setList(mList);
    }
}
