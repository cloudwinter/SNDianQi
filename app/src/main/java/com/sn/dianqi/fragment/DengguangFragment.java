package com.sn.dianqi.fragment;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 灯光
 */
public class DengguangFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = "DengguangFragment";

    @BindView(R.id.img_anjian_top_icon)
    ImageView topIconImgView;
    @BindView(R.id.text_anjian_top_title)
    TextView topTitleTextView;

    @BindView(R.id.tv_10fenzhong)
    TextView tenMinsTextView;
    @BindView(R.id.tv_8xiaoshi)
    TextView eightHoursTextView;
    @BindView(R.id.tv_10xiaoshi)
    TextView tenHoursTextView;

    // 特征值
    private BluetoothGattCharacteristic characteristic;


    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mDengguangReceiver);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        getActivity().registerReceiver(mDengguangReceiver, makeGattUpdateIntentFilter());
        characteristic = MyApplication.getInstance().gattCharacteristic;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_dengguang, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        tenMinsTextView.setOnClickListener(this);
        eightHoursTextView.setOnClickListener(this);
        tenHoursTextView.setOnClickListener(this);
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
            ToastUtils.showToast(getContext(), getString(R.string.device_no_connected));
            LogUtils.i(TAG, "sendBlueCmd -> 蓝牙未连接");
            return;
        }
        if (characteristic == null) {
            LogUtils.i(TAG, "sendBlueCmd -> 特征值未获取到");
            return;
        }
        characteristic.setValue(BlueUtils.StringToBytes(cmd));
        MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(characteristic);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_10fenzhong:
                sendBlueCmd("FF FF FF FF 05 00 00 00 19 16 CA");
                if (tenMinsTextView.isSelected()) {
                    tenMinsTextView.setSelected(false);
                } else {
                    tenMinsTextView.setSelected(true);
                    eightHoursTextView.setSelected(false);
                    tenHoursTextView.setSelected(false);
                }
                break;
            case R.id.tv_8xiaoshi:
                sendBlueCmd("FF FF FF FF 05 00 00 00 1A 56 CB");
                // 8小时
                if (eightHoursTextView.isSelected()) {
                    eightHoursTextView.setSelected(false);
                } else {
                    tenMinsTextView.setSelected(false);
                    eightHoursTextView.setSelected(true);
                    tenHoursTextView.setSelected(false);
                }
                break;
            case R.id.tv_10xiaoshi:
                sendBlueCmd("FF FF FF FF 05 00 00 00 1B 97 0B");
                // 10小时
                if (tenHoursTextView.isSelected()) {
                    tenHoursTextView.setSelected(false);
                } else {
                    tenMinsTextView.setSelected(false);
                    eightHoursTextView.setSelected(false);
                    tenHoursTextView.setSelected(true);
                }
                break;

        }
    }

    private void handleReceiveData(String data) {
        if (data.contains("FF FF FF FF 05 00 00 00 19 16 CA")) {
            // 10分钟
            if (tenMinsTextView.isSelected()) {
                tenMinsTextView.setSelected(false);
            } else {
                tenMinsTextView.setSelected(true);
                eightHoursTextView.setSelected(false);
                tenHoursTextView.setSelected(false);
            }
        }
        if (data.contains("FF FF FF FF 05 00 00 00 1A 56 CB")) {
            // 8小时
            if (eightHoursTextView.isSelected()) {
                eightHoursTextView.setSelected(false);
            } else {
                tenMinsTextView.setSelected(false);
                eightHoursTextView.setSelected(true);
                tenHoursTextView.setSelected(false);
            }
        }
        if (data.contains("FF FF FF FF 05 00 00 00 1B 97 0B")) {
            // 10小时
            if (tenHoursTextView.isSelected()) {
                tenHoursTextView.setSelected(false);
            } else {
                tenMinsTextView.setSelected(false);
                eightHoursTextView.setSelected(false);
                tenHoursTextView.setSelected(true);
            }
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
    private final BroadcastReceiver mDengguangReceiver = new BroadcastReceiver() {
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
                        LogUtils.e("==灯光  接收设备返回的数据==", data);
                        //handleReceiveData(data);
                    }
                }
            }
        }
    };
}
