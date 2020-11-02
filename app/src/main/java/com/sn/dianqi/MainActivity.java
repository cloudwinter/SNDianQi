package com.sn.dianqi;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
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
import com.sn.dianqi.blue.BluetoothLeService;
import com.sn.dianqi.common.Constants;
import com.sn.dianqi.util.Prefer;
import com.sn.dianqi.util.PreferenceUtil;
import com.sn.dianqi.util.ToastUtils;

import java.util.Locale;

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



    // 蓝牙适配器
    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        String language = Prefer.getInstance().getSelectedLanguage();
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
    protected void onResume() {
        super.onResume();
        // 获取手机本地的蓝牙适配器
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            // 未打开蓝牙
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 10);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_enter:
                // 判断当前蓝牙是否已连接，如果已连接直接调整到HomeActivity
                if (isConnected()) {
                    // 跳转到首页页面
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    // 跳转到蓝牙搜索和连接界面
                    Intent intent = new Intent(MainActivity.this, ConnectActivity.class);
                    intent.putExtra("from","main");
                    startActivity(intent);
                }
                break;
        }
    }


    private boolean isConnected() {
        BluetoothLeService bluetoothLeService = MyApplication.getInstance().mBluetoothLeService;
        if (bluetoothLeService != null && MyApplication.getInstance().gattCharacteristic != null
                && Prefer.getInstance().isBleConnected()) {
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    finish();
                }
            }
        }
        if (requestCode == 10) {
            if (!mBluetoothAdapter.isEnabled()) {
                finish();
            }
        }
    }


    private long exitTime = 0;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtils.showToast(MainActivity.this, getString(R.string.exit));
                exitTime = System.currentTimeMillis();
            } else {
                // 退出时已连接断开连接
                if (isConnected()) {
                    MyApplication.getInstance().mBluetoothLeService.disconnect();
                    Prefer.getInstance().setBleStatus("未连接",null);
                }
                Prefer.getInstance().clearData();
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
