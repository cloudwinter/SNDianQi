package com.sn.dianqi.fragment;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
 * 快捷(K2)
 */
public class KuaijieFragment extends BaseFragment implements View.OnClickListener,View.OnTouchListener {

    /**
     * 默认间隔
     */
    private final static long DEFAULT_INTERVAL = 2000;

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

    private long eventDownTime = 0L;

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mKuaijieReceiver);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
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

        getActivity().registerReceiver(mKuaijieReceiver, makeGattUpdateIntentFilter());
        characteristic = MyApplication.getInstance().gattCharacteristic;
        askStatus();
        return view;
    }

    private void initView() {
        jiyi1View.setOnTouchListener(this);
        jiyi2View.setOnTouchListener(this);
        kandianshiView.setOnTouchListener(this);
        lingyaliView.setOnTouchListener(this);
        zhihanView.setOnTouchListener(this);
        fuyuanView.setOnTouchListener(this);
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


    /**
     * 设置顶部的title
     * @param titleResId
     */
    private void setTitle(int titleResId) {
        topTitleTextView.setText(getString(titleResId));
    }


    private void askStatus() {
        mHandler.postAtTime(new Runnable() {
            @Override
            public void run() {
                try {
                    // 记忆1
                    sendBlueCmd("FF FF FF FF 03 00 28 00 03 9F 09");
                    Thread.sleep(300L);
                    // 记忆2
                    sendBlueCmd("FF FF FF FF 03 00 30 00 03 1F 0E");
                    Thread.sleep(300L);
                    // 看电视
                    sendBlueCmd("FF FF FF FF 03 00 18 00 03 9F 06");
                    Thread.sleep(300L);
                    // 止鼾
                    sendBlueCmd("FF FF FF FF 03 00 20 00 03 1E CB");
                    Thread.sleep(300L);
                    // 复原
                    sendBlueCmd("FF FF FF FF 03 00 38 00 03 9E CC");
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
        if (!BlueUtils.isConnected()) {
            ToastUtils.showToast(getContext(), getString(R.string.device_no_connected));
            LogUtils.i(TAG, "sendBlueCmd -> 蓝牙未连接");
            return;
        }
        if (characteristic == null) {
            characteristic = MyApplication.getInstance().gattCharacteristic;
        }
        if (characteristic == null) {
            LogUtils.i(TAG, "sendBlueCmd -> 特征值未获取到");
            return;
        }
        characteristic.setValue(BlueUtils.StringToBytes(cmd));
        MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(characteristic);
    }



    private void lingyaliLongClick() {
        if (lingyaliView.isSelected()) {
            // 有记忆
            sendBlueCmd("FF FF FF FF 05 00 00 9F 09 7E F6");
        } else {
            sendBlueCmd("FF FF FF FF 05 00 00 90 09 7B 06");
        }
    }

    private void jiyi2LongClick() {
        if (jiyi2View.isSelected()) {
            // 有记忆
            sendBlueCmd("FF FF FF FF 05 00 00 BF 0B E6 F7");
        } else {
            sendBlueCmd("FF FF FF FF 05 00 00 B0 0B E3 07");
        }
    }

    private void jiyi1LongClick() {
        if (jiyi1View.isSelected()) {
            // 有记忆
            sendBlueCmd("FF FF FF FF 05 00 00 AF 0A 2A F7");
        } else {
            sendBlueCmd("FF FF FF FF 05 00 00 A0 0A 2F 07");
        }
    }


    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.view_fuyuan:
//                setTopIconAndTitle(R.mipmap.ic_fuyuan_da,R.string.fuyuan);
//                sendBlueCmd("FF FF FF FF 05 00 00 00 08 D6 C6");
//                break;
//        }
    }


    private void handleReceiveData(String data) {
        if (data.contains("FF FF FF FF 03 06 00 0A")) {
            // 记忆1有记忆返回码
            jiyi1View.setSelected(true);
        }
        if (data.contains("FF FF FF FF 03 06 00 0B")) {
            // 记忆2有记忆返回码
            jiyi2View.setSelected(true);
        }
        if (data.contains("FF FF FF FF 03 06 00 05")) {
            // 看电视
            kandianshiView.setSelected(true);
        }
        if (data.contains("FF FF FF FF 03 06 00 09")) {
            // 零压力
            lingyaliView.setSelected(true);
        }
        if (data.contains("FF FF FF FF 03 06 00 0F")) {
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


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (v.getId()) {
            case R.id.view_jiyi1:
                if (MotionEvent.ACTION_DOWN == action) {
                    eventDownTime = System.currentTimeMillis();
                    timeHandler.sendEmptyMessageDelayed(JIYI1_WHAT, DEFAULT_INTERVAL);
                    setTitle(R.string.jiyi1);
                } else if (MotionEvent.ACTION_UP == action) {
                    timeHandler.removeMessages(JIYI1_WHAT);
                    if (isShortClick()) {
                        // 短按
                        if (jiyi1View.isSelected()) {
                            sendBlueCmd("FF FF FF FF 05 00 00 A1 0A 2E 97");
                        }
                    }
                }
                break;
            case R.id.view_jiyi2:
                if (MotionEvent.ACTION_DOWN == action) {
                    eventDownTime = System.currentTimeMillis();
                    timeHandler.sendEmptyMessageDelayed(JIYI2_WHAT, DEFAULT_INTERVAL);
                    setTitle(R.string.jiyi2);
                } else if (MotionEvent.ACTION_UP == action) {
                    timeHandler.removeMessages(JIYI2_WHAT);
                    if (isShortClick()) {
                        // 短按
                        if (jiyi2View.isSelected()) {
                            sendBlueCmd("FF FF FF FF 05 00 00 A1 0A 2E 97");
                        }
                    }
                }
                break;
            case R.id.view_kandianshi:
                setTopIconAndTitle(R.mipmap.ic_kandianshi_da,R.string.kandianshi);
                if (MotionEvent.ACTION_DOWN == action) {
                    eventDownTime = System.currentTimeMillis();
                    timeHandler.sendEmptyMessageDelayed(KANDIANSHI_WHAT, DEFAULT_INTERVAL);
                } else if (MotionEvent.ACTION_UP == action) {
                    timeHandler.removeMessages(KANDIANSHI_WHAT);
                    if (isShortClick()) {
                        // 短按
                        if (kandianshiView.isSelected()) {
                            sendBlueCmd("FF FF FF FF 05 00 00 51 05 2A 93");
                        } else {
                            sendBlueCmd("FF FF FF FF 05 00 00 00 05 17 03");
                        }
                    }
                }
                break;
            case R.id.view_lingyali:
                setTopIconAndTitle(R.mipmap.ic_lingyali_da,R.string.lingyali);
                if (MotionEvent.ACTION_DOWN == action) {
                    eventDownTime = System.currentTimeMillis();
                    timeHandler.sendEmptyMessageDelayed(LINGYALI_WHAT, DEFAULT_INTERVAL);
                } else if (MotionEvent.ACTION_UP == action) {
                    timeHandler.removeMessages(LINGYALI_WHAT);
                    if (isShortClick()) {
                        // 短按
                        setTopIconAndTitle(R.mipmap.ic_lingyali_da,R.string.lingyali);
                        if (lingyaliView.isSelected()) {
                            sendBlueCmd("FF FF FF FF 05 00 00 91 09 7A 96");
                        } else {
                            sendBlueCmd("FF FF FF FF 05 00 00 00 09 17 06");
                        }
                    }
                }
                break;
            case R.id.view_zhihan:
                setTopIconAndTitle(R.mipmap.ic_zhihan_da,R.string.zhihan);
                if (MotionEvent.ACTION_DOWN == action) {
                    eventDownTime = System.currentTimeMillis();
                    timeHandler.sendEmptyMessageDelayed(ZHIHAN_WHAT, DEFAULT_INTERVAL);
                } else if (MotionEvent.ACTION_UP == action) {
                    timeHandler.removeMessages(ZHIHAN_WHAT);
                    if (isShortClick()) {
                        // 短按
                        setTopIconAndTitle(R.mipmap.ic_zhihan_da,R.string.zhihan);
                        if (zhihanView.isSelected()) {
                            sendBlueCmd("FF FF FF FF 05 00 00 F1 0F D2 94");
                        } else {
                            sendBlueCmd("FF FF FF FF 05 00 00 00 0F 97 04");
                        }
                    }
                }
                break;
            case R.id.view_fuyuan:
                setTopIconAndTitle(R.mipmap.ic_fuyuan_da,R.string.fuyuan);
                if (MotionEvent.ACTION_DOWN == action) {
                    sendBlueCmd("FF FF FF FF 05 00 00 00 08 D6 C6");
                }
                break;
        }
        return true;
    }


    private static final int JIYI1_WHAT = 1;
    private static final int JIYI2_WHAT = 2;
    private static final int KANDIANSHI_WHAT = 3;
    private static final int LINGYALI_WHAT = 4;
    private static final int ZHIHAN_WHAT = 5;

    /**
     * 记忆1 1
     * 记忆2  2
     * 看电视 3
     * 零压力 4
     * 止鼾 5
     */
    private Handler timeHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case JIYI1_WHAT:
                    jiyi1LongClick();
                    break;
                case JIYI2_WHAT:
                    jiyi2LongClick();
                    break;
                case KANDIANSHI_WHAT:
                    kandianshiLongClick();
                    break;
                case LINGYALI_WHAT:
                    lingyaliLongClick();
                    break;
                case ZHIHAN_WHAT:
                    zhihanLongClick();
                    break;
                default:
                    break;
            }
        }
    };



    private void zhihanLongClick() {
        if (zhihanView.isSelected()) {
            // 有记忆
            sendBlueCmd("FF FF FF FF 05 00 00 FF 0F D6 F4");
        } else {
            sendBlueCmd("FF FF FF FF 05 00 00 F0 0F D3 04");
        }
    }

    private void kandianshiLongClick() {
        if (kandianshiView.isSelected()) {
            // 有记忆
            sendBlueCmd("FF FF FF FF 05 00 00 5F 05 2E F3");
        } else {
            sendBlueCmd("FF FF FF FF 05 00 00 50 05 2B 03");
        }
    }


    public boolean isShortClick() {
        long endTime = System.currentTimeMillis();
        if (getInterval(eventDownTime, endTime) < 2000) {
            return true;
        }
        return false;
    }

    public boolean isLongClick() {
        long endTime = System.currentTimeMillis();
        if (getInterval(eventDownTime, endTime) >= 2000) {
            return true;
        }
        return false;
    }

    /**
     * 单位是毫秒
     * @param startTime
     * @param endTime
     * @return
     */
    private long getInterval(long startTime,long endTime) {
        long interval = endTime - startTime;
        return interval;
    }
}
