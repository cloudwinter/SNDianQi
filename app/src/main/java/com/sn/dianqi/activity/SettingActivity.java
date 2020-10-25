package com.sn.dianqi.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.sn.dianqi.R;
import com.sn.dianqi.base.BaseActivity;
import com.sn.dianqi.view.TranslucentActionBar;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends BaseActivity implements TranslucentActionBar.ActionBarClickListener, View.OnClickListener {

    @BindView(R.id.actionbar)
    TranslucentActionBar actionBar;
    @BindView(R.id.ll_connect)
    LinearLayout llConnect;
    @BindView(R.id.ll_language)
    LinearLayout llLanguage;
    @BindView(R.id.ll_version)
    LinearLayout llVersion;
    @BindView(R.id.ll_privacy)
    LinearLayout llPrivacy;

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        ButterKnife.bind(this);
        // 设置title
        actionBar.setData(getString(R.string.blue_equipment), R.mipmap.ic_back, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            actionBar.setStatusBarHeight(getStatusBarHeight());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_connect:
                Intent intent = new Intent(SettingActivity.this, ConnectActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_language:
                // TODO
                break;
            case R.id.ll_privacy:
                Intent webIntent = new Intent(SettingActivity.this, WebActivity.class);
                startActivity(webIntent);
                break;

        }
    }
}
