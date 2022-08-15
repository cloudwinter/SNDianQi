package com.sn.dianqi.fragment;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 微调（W4）
 */
public class WeitiaoFragment extends BaseFragment implements View.OnTouchListener {

    public static final String TAG = "WeitiaoFragment";


    @BindView(R.id.img_anjian_top_icon)
    ImageView topIconImgView;
    @BindView(R.id.text_anjian_top_title)
    TextView topTitleTextView;

    @BindView(R.id.ll_beibu_top)
    LinearLayout beibuTopLayout;
    @BindView(R.id.ll_beibu_bottom)
    LinearLayout beibuBottomLayout;
    @BindView(R.id.ll_tuibu_top)
    LinearLayout tuibuTopLayout;
    @BindView(R.id.ll_tuibu_bottom)
    LinearLayout tuibuBottomLayout;

    @BindView(R.id.img_beibu_sanjiao_top)
    ImageView beibuTopImgView;
    @BindView(R.id.img_beibu_sanjiao_bottom)
    ImageView beibuBottomImgView;
    @BindView(R.id.img_tuibu_sanjiao_top)
    ImageView tuibuTopImgView;
    @BindView(R.id.img_tuibu_sanjiao_bottom)
    ImageView tuibuBottomImgView;

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
        getActivity().registerReceiver(mDengguangReceiver, makeGattUpdateIntentFilter());
        characteristic = MyApplication.getInstance().gattCharacteristic;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_weitiao, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        beibuTopLayout.setOnTouchListener(this);
        beibuBottomLayout.setOnTouchListener(this);
        tuibuTopLayout.setOnTouchListener(this);
        tuibuBottomLayout.setOnTouchListener(this);
    }


    private void handleReceiveData(String data) {
        // do nothing
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        AnimationDrawable animationDrawable = null;
        int action = event.getAction();
        String cmd = null;
        switch (v.getId()) {
            case R.id.ll_beibu_top:
                topTitleTextView.setText(getString(R.string.beibutiaozheng));
                if (MotionEvent.ACTION_DOWN == action) {
                    cmd = "FF FF FF FF 05 00 00 00 03 97 01";
                    beibuTopImgView.setSelected(true);
                    topIconImgView.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.weitiao_beibu_top_animation));
                    animationDrawable = (AnimationDrawable) topIconImgView.getBackground();
                    animationDrawable.start();
                } else if (MotionEvent.ACTION_UP == action) {
                    cmd = "FF FF FF FF 05 00 00 00 00 D7 00";
                    beibuTopImgView.setSelected(false);
                    if (animationDrawable != null){
                        animationDrawable.stop();
                    }
                    topIconImgView.setBackground(ContextCompat.getDrawable(getContext(),R.mipmap.ic_beibutiaozheng_da_1));

                }
                break;
            case R.id.ll_beibu_bottom:
                topTitleTextView.setText(getString(R.string.beibutiaozheng));
                if (MotionEvent.ACTION_DOWN == action) {
                    cmd = "FF FF FF FF 05 00 00 00 04 D6 C3";
                    beibuBottomImgView.setSelected(true);
                    topIconImgView.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.weitiao_beibu_bottom_animation));
                    animationDrawable = (AnimationDrawable) topIconImgView.getBackground();
                    animationDrawable.start();
                } else if (MotionEvent.ACTION_UP == action) {
                    cmd = "FF FF FF FF 05 00 00 00 00 D7 00";
                    beibuBottomImgView.setSelected(false);
                    if (animationDrawable != null){
                        animationDrawable.stop();
                    }
                    topIconImgView.setBackground(ContextCompat.getDrawable(getContext(),R.mipmap.ic_beibutiaozheng_da_1));
                }
                break;
            case R.id.ll_tuibu_top:
                topTitleTextView.setText(getString(R.string.tuibutiaozheng));
                if (MotionEvent.ACTION_DOWN == action) {
                    cmd = "FF FF FF FF 05 00 00 00 06 57 02";
                    tuibuTopImgView.setSelected(true);
                    topIconImgView.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.weitiao_tuibu_top_animation));
                    animationDrawable = (AnimationDrawable) topIconImgView.getBackground();
                    animationDrawable.start();
                } else if (MotionEvent.ACTION_UP == action) {
                    cmd = "FF FF FF FF 05 00 00 00 00 D7 00";
                    tuibuTopImgView.setSelected(false);
                    if (animationDrawable != null){
                        animationDrawable.stop();
                    }
                    topIconImgView.setBackground(ContextCompat.getDrawable(getContext(),R.mipmap.ic_tuibutiaozheng_da_1));
                }
                break;
            case R.id.ll_tuibu_bottom:
                topTitleTextView.setText(getString(R.string.tuibutiaozheng));
                if (MotionEvent.ACTION_DOWN == action) {
                    cmd = "FF FF FF FF 05 00 00 00 07 96 C2";
                    tuibuBottomImgView.setSelected(true);
                    topIconImgView.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.weitiao_tuibu_bottom_animation));
                    animationDrawable = (AnimationDrawable) topIconImgView.getBackground();
                    animationDrawable.start();
                } else if (MotionEvent.ACTION_UP == action) {
                    cmd = "FF FF FF FF 05 00 00 00 00 D7 00";
                    tuibuBottomImgView.setSelected(false);
                    if (animationDrawable != null){
                        animationDrawable.stop();
                    }
                    topIconImgView.setBackground(ContextCompat.getDrawable(getContext(),R.mipmap.ic_tuibutiaozheng_da_1));
                }
                break;
        }
        if (!TextUtils.isEmpty(cmd)) {
            sendBlueCmd(cmd);
        }
        return true;
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
            LogUtils.i(TAG, "sendBlueCmd -> 特征值未获取到");
            return;
        }
        characteristic.setValue(BlueUtils.StringToBytes(cmd));
        MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(characteristic);
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
                        handleReceiveData(data);
                    }
                }
            }
        }
    };

}
