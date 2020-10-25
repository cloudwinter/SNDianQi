package com.sn.dianqi;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sn.dianqi.activity.ConnectActivity;
import com.sn.dianqi.activity.HomeActivity;
import com.sn.dianqi.base.BaseActivity;
import com.sn.dianqi.common.Constants;
import com.sn.dianqi.util.PreferenceUtil;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 首页
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private final static int PERMISSION_REQUEST_COARSE_LOCATION = 3;

    @BindView(R.id.text_enter)
    TextView textView;
    @BindView(R.id.img_logo)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        String language = PreferenceUtil.getString(Constants.SharePre.KEY_LANGUAGE, "zh");
        if ("zh".equals(language)) {
            imageView.setImageResource(R.mipmap.zh_logo);
        } else {
            imageView.setImageResource(R.mipmap.en_logo);
        }
        textView.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_enter:
                // 跳转到蓝牙搜索和连接界面
                Intent intent = new Intent(MainActivity.this, ConnectActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    private long exitTime = 0;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次后退键退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                // 退出代码
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
