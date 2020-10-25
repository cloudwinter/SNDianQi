package com.sn.dianqi.fragment;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sn.dianqi.MyApplication;
import com.sn.dianqi.R;
import com.sn.dianqi.base.BaseFragment;
import com.sn.dianqi.blue.BluetoothLeService;
import com.sn.dianqi.util.BlueUtils;
import com.sn.dianqi.util.LogUtils;
import com.sn.dianqi.util.Prefer;
import com.sn.dianqi.util.ToastUtils;
import com.sn.dianqi.view.AnjianYuanView;
import com.sn.dianqi.view.JiyiView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 快捷
 */
public class KuaijieFragment extends BaseFragment implements View.OnClickListener, View.OnLongClickListener {


    public static final String TAG = "KuaijieFragment";
    @BindView(R.id.img_anjian_top_icon)
    ImageView topIconImgView;
    @BindView(R.id.text_anjian_top_title)
    TextView topTitleTextView;

    @BindView(R.id.view_jiyi1)
    JiyiView jiyi1View;

    @BindView(R.id.view_jiyi2)
    JiyiView jiyi2View;

    @BindView(R.id.view_kandianshi)
    AnjianYuanView kandianshiView;
    @BindView(R.id.view_lingyali)
    AnjianYuanView lingyaliView;
    @BindView(R.id.view_zhihan)
    AnjianYuanView zhihanView;
    @BindView(R.id.view_fuyuan)
    AnjianYuanView fuyuanView;
    // 特征值
    private BluetoothGattCharacteristic characteristic;

    private Handler mHandler = new Handler();

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mKuaijieReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mKuaijieReceiver, makeGattUpdateIntentFilter());
        characteristic = MyApplication.getInstance().gattCharacteristic;
        askStatus();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_kuaijie, container, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 按键以外发送停止码
                sendBlueCmd("FF FF FF FF 05 00 00 00 00 D7 00");
            }
        });
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        jiyi1View.setOnClickListener(this);
        jiyi1View.setOnLongClickListener(this);
        jiyi2View.setOnClickListener(this);
        jiyi2View.setOnLongClickListener(this);
        kandianshiView.setOnClickListener(this);
        kandianshiView.setOnLongClickListener(this);
        lingyaliView.setOnClickListener(this);
        lingyaliView.setOnLongClickListener(this);
        zhihanView.setOnClickListener(this);
        zhihanView.setOnLongClickListener(this);
        fuyuanView.setOnClickListener(this);
    }

    /**
     * 设置顶部icon和title
     * @param iconResId
     * @param titleResId
     */
    private void setTopIconAndTitle(int iconResId,int titleResId) {
        topIconImgView.setBackground(ContextCompat.getDrawable(getContext(),iconResId));
        topTitleTextView.setText(getString(titleResId));
    }


    private void askStatus() {
        mHandler.postAtTime(new Runnable() {
            @Override
            public void run() {
                try {
                    // 记忆1
                    sendBlueCmd("FF FF FF FF 03 00 28 00 09 1F 0E");
                    Thread.sleep(300L);
                    // 记忆2
                    sendBlueCmd("FF FF FF FF 03 00 31 00 09 CE C9");
                    Thread.sleep(300L);
                    // 看电视
                    sendBlueCmd("FF FF FF FF 03 00 16 00 09 7E C2");
                    Thread.sleep(300L);
                    // 止鼾
                    sendBlueCmd("FF FF FF FF 03 00 1F 00 09 AE C0");
                    Thread.sleep(300L);
                    // 复原
                    sendBlueCmd("FF FF FF FF 03 00 3A 00 09 BF 0B");
                } catch (Exception e) {
                    LogUtils.e(TAG, "askStatus 异常" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, 1000L);

    }


    /**
     * 发送蓝牙命令
     *
     * @param cmd
     */
    private void sendBlueCmd(String cmd) {
        cmd = cmd.replace(" ", "");
        Log.i(TAG, "sendBlueCmd: " + cmd);
        // 判断蓝牙是否连接
        if (!Prefer.getInstance().isBleConnected()) {
            // FIXME 设置string
            ToastUtils.showToast(getContext(), "蓝牙未连接");
            LogUtils.i(TAG, "sendBlueCmd -> 蓝牙未连接");
            return;
        }
        byte[] bytes = BlueUtils.StringToBytes(cmd);
        characteristic.setValue(BlueUtils.StringToBytes(cmd));
        MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(characteristic);
    }


    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.view_jiyi1:
                if (jiyi1View.isSelected()) {
                    // 有记忆
                    sendBlueCmd("FF FF FF FF 05 00 00 AF 0A 2A F7");
                } else {
                    sendBlueCmd("FF FF FF FF 05 00 00 A0 0A 2F 07");
                }
                break;
            case R.id.view_jiyi2:
                if (jiyi2View.isSelected()) {
                    // 有记忆
                    sendBlueCmd("FF FF FF FF 05 00 00 BF 0B E6 F7");
                } else {
                    sendBlueCmd("FF FF FF FF 05 00 00 B0 0B E3 07");
                }
                break;
            case R.id.view_kandianshi:
                setTopIconAndTitle(R.mipmap.ic_kandianshi_da,R.string.kandianshi);
                if (kandianshiView.isSelected()) {
                    // 有记忆
                    sendBlueCmd("FF FF FF FF 05 00 00 5F 05 2E F3");
                } else {
                    sendBlueCmd("FF FF FF FF 05 00 00 50 05 2B 03");
                }
                break;
            case R.id.view_lingyali:
                setTopIconAndTitle(R.mipmap.ic_lingyali_da,R.string.lingyali);
                if (lingyaliView.isSelected()) {
                    // 有记忆
                    sendBlueCmd("FF FF FF FF 05 00 00 9F 09 7E F6");
                } else {
                    sendBlueCmd("FF FF FF FF 05 00 00 90 09 7B 06");
                }
                break;
            case R.id.view_zhihan:
                setTopIconAndTitle(R.mipmap.ic_zhihan_da,R.string.zhihan);
                if (zhihanView.isSelected()) {
                    // 有记忆
                    sendBlueCmd("FF FF FF FF 05 00 00 FF 0F D6 F4");
                } else {
                    sendBlueCmd("FF FF FF FF 05 00 00 F0 0F D3 04");
                }
                break;
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_jiyi1:
                if (jiyi1View.isSelected()) {
                    sendBlueCmd("FF FF FF FF 05 00 00 A1 0A 2E 97");
                }
                break;
            case R.id.view_jiyi2:
                if (jiyi1View.isSelected()) {
                    sendBlueCmd("FF FF FF FF 05 00 00 B1 0B E2 97");
                }
                break;
            case R.id.view_kandianshi:
                setTopIconAndTitle(R.mipmap.ic_kandianshi_da,R.string.kandianshi);
                if (kandianshiView.isSelected()) {
                    sendBlueCmd("FF FF FF FF 05 00 00 51 05 2A 93");
                } else {
                    sendBlueCmd("FF FF FF FF 05 00 00 00 05 17 03");
                }
                break;
            case R.id.view_lingyali:
                setTopIconAndTitle(R.mipmap.ic_lingyali_da,R.string.lingyali);
                if (kandianshiView.isSelected()) {
                    sendBlueCmd("FF FF FF FF 05 00 00 91 09 7A 96");
                } else {
                    sendBlueCmd("FF FF FF FF 05 00 00 00 09 17 06");
                }
                break;
            case R.id.view_zhihan:
                setTopIconAndTitle(R.mipmap.ic_zhihan_da,R.string.zhihan);
                if (kandianshiView.isSelected()) {
                    sendBlueCmd("FF FF FF FF 05 00 00 F1 0F D2 94");
                } else {
                    sendBlueCmd("FF FF FF FF 05 00 00 00 0F 97 04");
                }
                break;
            case R.id.view_fuyuan:
                setTopIconAndTitle(R.mipmap.ic_fuyuan_da,R.string.fuyuan);
                sendBlueCmd("FF FF FF FF 05 00 00 00 08 D6 C6");
                break;
        }
    }


    private void handleReceiveData(String data) {
        if (data.contains("FF FF FF FF 03 12 00 AA")) {
            // 记忆1有记忆返回码
            jiyi1View.setSelected(true);
        }
        if (data.contains("FF FF FF FF 03 12 00 AA")) {
            // 记忆2有记忆返回码
            jiyi2View.setSelected(true);
        }
        if (data.contains("FF FF FF FF 03 12 00 A5")) {
            // 看电视
            kandianshiView.setSelected(true);
        }
        if (data.contains("FF FF FF FF 03 12 00 A9")) {
            // 零压力
            lingyaliView.setSelected(true);
        }
        if (data.contains("FF FF FF FF 03 12 00 AF")) {
            // 止鼾
            zhihanView.setSelected(true);
        }


        // 记忆1 按键回码
        if (data.contains("FF FF FF FF 05 00 00 A0 0A 2F 07")) {
            jiyi1View.setSelected(true);
        }
        if (data.contains("FF FF FF FF 05 00 00 AF 0A 2A F7")) {
            jiyi1View.setSelected(false);
        }

        // 记忆2 按键回码
        if (data.contains("FF FF FF FF 05 00 00 B0 0B E3 07")) {
            jiyi2View.setSelected(true);
        }
        if (data.contains("FF FF FF FF 05 00 00 BF 0B E6 F7")) {
            jiyi2View.setSelected(false);
        }

        // 看电视 按键回码
        if (data.contains("FF FF FF FF 05 00 00 50 05 2B 03")) {
            kandianshiView.setSelected(true);
        }
        if (data.contains("FF FF FF FF 05 00 00 5F 05 2E F3")) {
            kandianshiView.setSelected(false);
        }

        // 零压力 按键回码
        if (data.contains("FF FF FF FF 05 00 00 90 09 7B 06")) {
            lingyaliView.setSelected(true);
        }
        if (data.contains("FF FF FF FF 05 00 00 9F 09 7E F6")) {
            lingyaliView.setSelected(false);
        }

        // 止鼾 按键回码
        if (data.contains("FF FF FF FF 05 00 00 F0 0F D3 04")) {
            zhihanView.setSelected(true);
        }
        if (data.contains("FF FF FF FF 05 00 00 FF 0F D6 F4")) {
            zhihanView.setSelected(false);
        }
    }


    /* 意图过滤器 */
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }


    /**
     * 广播接收器，负责接收BluetoothLeService类发送的数据
     */
    private final BroadcastReceiver mKuaijieReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) { //发现GATT服务器
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //处理发送过来的数据  (//有效数据)
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    String data = bundle.getString(BluetoothLeService.EXTRA_DATA);
                    if (data != null) {
                        LogUtils.e("==快捷  接收设备返回的数据==", data);
                        handleReceiveData(data);
                    }
                }
            }
        }
    };


}
