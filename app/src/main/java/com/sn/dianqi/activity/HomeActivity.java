package com.sn.dianqi.activity;

import android.os.Bundle;

import com.sn.dianqi.R;
import com.sn.dianqi.base.BaseActivity;
import com.sn.dianqi.view.TranslucentActionBar;

import androidx.annotation.Nullable;
import butterknife.BindView;

public class HomeActivity extends BaseActivity implements TranslucentActionBar.ActionBarClickListener {

    @BindView(R.id.actionbar)
    TranslucentActionBar actionBar;


    @Override
    public void onLeftClick() {

    }

    @Override
    public void onRightClick() {

    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
}
