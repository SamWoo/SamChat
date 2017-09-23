package samwoo.samchat.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.OnClick;
import samwoo.samchat.R;
import samwoo.samchat.base.BaseActivity;

/**
 * Created by Administrator on 2017/9/24.
 */

public class ScanQRcodeActivity extends BaseActivity {
    private CaptureFragment captureFragment;
    private boolean isOpenLight = false;

    @Override
    public void init() {
        captureFragment = new CaptureFragment();
        CodeUtils.setFragmentArgs(captureFragment, R.layout.fragment_qrcode_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
    }

    @Override
    public int getLayoutResoure() {
        return R.layout.activity_qrcode_scan;
    }

    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            intent.putExtras(bundle);
            ScanQRcodeActivity.this.setResult(RESULT_OK, intent);
            ScanQRcodeActivity.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            intent.putExtras(bundle);
            ScanQRcodeActivity.this.setResult(RESULT_OK, intent);
            ScanQRcodeActivity.this.finish();
        }
    };

    @OnClick({R.id.linear1})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.linear1:
                if (!isOpenLight) {
                    isOpenLight = true;
                    CodeUtils.isLightEnable(true);
                } else {
                    isOpenLight = false;
                    CodeUtils.isLightEnable(false);
                }
                break;
        }
    }
}
