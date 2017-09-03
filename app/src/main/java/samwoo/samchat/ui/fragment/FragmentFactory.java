package samwoo.samchat.ui.fragment;

import samwoo.samchat.R;
import samwoo.samchat.base.BaseFragment;

/**
 * Created by Administrator on 2017/8/7.
 */

public class FragmentFactory {
    private static FragmentFactory fragmentFactory;
    private BaseFragment mConversationFragment;
    private BaseFragment mContactFragment;
    private BaseFragment mDynamicFragment;
    private BaseFragment mSettingFragment;

    //单例模式
    public static FragmentFactory getInstance() {
        if (fragmentFactory == null) {
            synchronized (FragmentFactory.class) {
                if (fragmentFactory == null) {
                    fragmentFactory = new FragmentFactory();
                }
            }
        }
        return fragmentFactory;
    }

    public BaseFragment getFragment(int id) {
        switch (id) {
            case R.id.conversations:
                return getConversationFragment();
            case R.id.contacts:
                return getContactFragment();
            case R.id.dynamic:
                return getDynamicFragment();
            case R.id.setting:
                return getSettingFragment();
        }
        return null;
    }

    private BaseFragment getConversationFragment() {
        if (mConversationFragment == null) {
            mConversationFragment = new ConversationFragment();
        }
        return mConversationFragment;
    }

    private BaseFragment getContactFragment() {
        if (mContactFragment == null) {
            mContactFragment = new ContactFragment();
        }
        return mContactFragment;
    }

    private BaseFragment getDynamicFragment() {
        if (mDynamicFragment == null) {
            mDynamicFragment = new DynamicFragment();
        }
        return mDynamicFragment;
    }

    private BaseFragment getSettingFragment() {
        if (mSettingFragment == null) {
            mSettingFragment = new SettingFragment();
        }
        return mSettingFragment;
    }
}
