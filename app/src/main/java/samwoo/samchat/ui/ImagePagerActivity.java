package samwoo.samchat.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import samwoo.samchat.R;
import samwoo.samchat.base.BaseActivity;

/**
 * Created by Administrator on 2017/9/13.
 */

public class ImagePagerActivity extends BaseActivity {
    @BindView(R.id.pager_img)
    ViewPager viewPager;
    @BindView(R.id.layout_guide)
    LinearLayout guideLayout;

    public static final String INTENT_IMGURLS = "imgurls";
    public static final String INTENT_POSITION = "position";
    public static final String INTENT_IMAGESIZE = "imagesize";
    private int startPos;
    private ArrayList<String> imgUrls;
    private ImageSize imageSize;
    private ArrayList<View> guideViewList = new ArrayList<View>();

    public static void startImagePagerActivity(Context context, List<String> imgUrls, int position, ImageSize imageSize) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putStringArrayListExtra(INTENT_IMGURLS, (ArrayList<String>) imgUrls);
        intent.putExtra(INTENT_POSITION, position);
        intent.putExtra(INTENT_IMAGESIZE, imageSize);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutResoure() {
        return R.layout.activity_image_pager;
    }

    @Override
    public void init() {
        getIntentData();
        ImageAdapter imageAdapter = new ImageAdapter(this, imgUrls);
        imageAdapter.setImageSize(imageSize);
        viewPager.setAdapter(imageAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < guideViewList.size(); i++) {
                    guideViewList.get(i).setSelected(i == position ? true : false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(startPos);

        addGuideView(guideLayout, startPos, imgUrls);
    }

    /**
     * 动态添加指引View到guideLayout中
     * @param guideLayout
     * @param startPos
     * @param imgUrls
     */
    private void addGuideView(LinearLayout guideLayout, int startPos, ArrayList<String> imgUrls) {
        if (imgUrls != null && imgUrls.size() > 0) {
            guideViewList.clear();
            for (int i = 0; i < imgUrls.size(); i++) {
                View view = new View(this);
                view.setBackgroundResource(R.drawable.selector_guide_bg);
                view.setSelected(i == startPos ? true : false);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.guideview_width),
                        getResources().getDimensionPixelSize(R.dimen.guideview_heigth));
                layoutParams.setMargins(10, 0, 0, 0);
                view.setLayoutParams(layoutParams);
                guideLayout.addView(view);
                guideViewList.add(view);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get data from pre-Activity
     */
    private void getIntentData() {
        startPos = getIntent().getIntExtra(INTENT_POSITION, 0);
        imgUrls = getIntent().getStringArrayListExtra(INTENT_IMGURLS);
        imageSize = (ImageSize) getIntent().getSerializableExtra(INTENT_IMAGESIZE);
    }

    private class ImageAdapter extends PagerAdapter {
        private List<String> datas = new ArrayList<>();
        private Context context;
        private ImageView smallImageView = null;
        private ImageSize imageSize;

        public ImageAdapter(Context context, List<String> datas) {
            this.context = context;
            this.datas = datas;
        }

        public void setImageSize(ImageSize imageSize) {
            this.imageSize = imageSize;
        }

        @Override
        public int getCount() {
            return datas == null ? 0 : datas.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
            super.restoreState(state, loader);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_pager, container, false);
            if (view != null) {
                ImageView imageView = view.findViewById(R.id.imgage);

                if (imageSize != null) {
                    //预览imageView
                    smallImageView = new ImageView(context);
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(imageSize.getWidth(), imageSize.getHeight());
                    layoutParams.gravity = Gravity.CENTER;
                    smallImageView.setLayoutParams(layoutParams);
                    smallImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ((FrameLayout) view).addView(smallImageView);
                }

                //loading
                final ProgressBar loading = new ProgressBar(context);
                FrameLayout.LayoutParams loadingLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                loadingLayoutParams.gravity = Gravity.CENTER;
                loading.setLayoutParams(loadingLayoutParams);
                ((FrameLayout) view).addView(loading);

                final String imgurl = datas.get(position);
                Glide.with(context)
                        .load(imgurl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.1f)
                        .error(R.drawable.logo_chat)
                        .into(new GlideDrawableImageViewTarget(imageView) {
                            @Override
                            public void onLoadStarted(Drawable placeholder) {
                                super.onLoadStarted(placeholder);
                                loading.setVisibility(View.GONE);
                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                                loading.setVisibility(View.GONE);
                            }

                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                                super.onResourceReady(resource, animation);
                                loading.setVisibility(View.GONE);
                            }
                        });
                container.addView(view, 0);
            }
            return view;
        }
    }

    public static class ImageSize implements Serializable {
        private int width;
        private int height;

        public ImageSize(int width, int height) {
            this.height = height;
            this.width = width;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}
