package samwoo.samchat.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import butterknife.OnClick;
import samwoo.samchat.R;
import samwoo.samchat.base.BaseActivity;

/**
 * Created by Administrator on 2017/9/24.
 */

public class CreateQRcodeActivity extends BaseActivity {
    @BindView(R.id.edit_content)
    EditText editText;
    @BindView(R.id.image_content)
    ImageView imageView;

    private Bitmap bitmap;
    public static final int WIDTH_QRCODE=600;
    public static final int HEIGHT_QRCODE=600;

    @Override
    public void init() {

    }

    @Override
    public int getLayoutResoure() {
        return R.layout.activity_qrcode_generator;
    }

    @OnClick({R.id.btn_logo, R.id.btn_normal})
    public void onViewClick(View view) {
        String content = editText.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "您的输入为空!!", Toast.LENGTH_SHORT).show();
            return;
        }
        editText.setText(null);
        switch (view.getId()) {
            case R.id.btn_logo:
                bitmap = CodeUtils.createImage(content, WIDTH_QRCODE, HEIGHT_QRCODE, BitmapFactory.decodeResource(getResources(), R.drawable.logo_chat));
                break;
            case R.id.btn_normal:
                bitmap = CodeUtils.createImage(content, WIDTH_QRCODE, HEIGHT_QRCODE, null);
                break;
        }
        imageView.setImageBitmap(bitmap);
    }

}
