package samwoo.samchat.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/8/7.
 */

public abstract class BaseFragment extends Fragment {
    private ProgressDialog progressDialog;
    private Unbinder binder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(getResLayout(), null);
        binder = ButterKnife.bind(this, root);
        init();
        return root;
    }

    public abstract int getResLayout();

    public abstract void init();

    public void toast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
        }
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    public void startActivity(Class activity, boolean isFinish) {
        Intent intent = new Intent(getActivity(), activity);
        startActivity(intent);
        if (isFinish) getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog = null;
        }
        binder.unbind();
    }
}
