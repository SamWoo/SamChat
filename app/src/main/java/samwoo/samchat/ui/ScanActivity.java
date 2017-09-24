package samwoo.samchat.ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.hyphenate.util.ImageUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.OnClick;
import samwoo.samchat.R;
import samwoo.samchat.base.BaseActivity;
import samwoo.samchat.utils.ImageUtil;

/**
 * Created by Administrator on 2017/9/23.
 */

public class ScanActivity extends BaseActivity {
    public static final int REQUEST_CODE = 10;
    public static final int REQUEST_IMAGE = 11;

    @Override
    public void init() {
        initPermission();
    }

    private void initPermission() {
        //检查权限
    }

    @Override
    public int getLayoutResoure() {
        return R.layout.activity_scan;
    }


    @OnClick({R.id.button1, R.id.button2, R.id.button3, R.id.button4})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.button2:
                Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
                intent2.addCategory(Intent.CATEGORY_OPENABLE);
                intent2.setType("image/*");
                startActivityForResult(intent2, REQUEST_IMAGE);
                break;
            case R.id.button3:
                startActivityForResult(new Intent(this, ScanQRcodeActivity.class), REQUEST_CODE);
                break;
            case R.id.button4:
                startActivity(CreateQRcodeActivity.class, false);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                if (null != data) {
                    Bundle bundle = data.getExtras();
                    if (bundle == null) return;
                    if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                        String result = bundle.getString(CodeUtils.RESULT_STRING);
                        Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                    } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                        Toast.makeText(this, "二维码解析失败!!", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case REQUEST_IMAGE:
                if (null != data) {
                    Uri uri = data.getData();
                    try {
                        CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(this, uri), new CodeUtils.AnalyzeCallback() {
                            @Override
                            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                                Toast.makeText(getApplication(), "解析结果:" + result, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onAnalyzeFailed() {
                                Toast.makeText(getApplication(), "二维码解析失败!!", Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}
