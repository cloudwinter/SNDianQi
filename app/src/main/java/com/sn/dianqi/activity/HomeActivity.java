package com.sn.dianqi.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sn.dianqi.R;
import com.sn.dianqi.adapter.TabPagerAdapter;
import com.sn.dianqi.base.BaseActivity;
import com.sn.dianqi.base.BaseFragment;
import com.sn.dianqi.fragment.DengguangFragment;
import com.sn.dianqi.fragment.KuaijieFragment;
import com.sn.dianqi.fragment.WeitiaoFragment;
import com.sn.dianqi.view.NoScrollViewPager;
import com.sn.dianqi.view.TranslucentActionBar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity implements View.OnClickListener, TranslucentActionBar.ActionBarClickListener {

    // 设置tab个数
    private final static int tabCount = 3;

    @BindView(R.id.actionbar)
    TranslucentActionBar actionBar;

    @BindView(R.id.ll_content)
    RelativeLayout relativeLayout;

    @BindView(R.id.vp_home)
    NoScrollViewPager viewPager;

    @BindView(R.id.tab1)
    LinearLayout tab1;
    @BindView(R.id.tab1_img)
    ImageView tab1Img;
    @BindView(R.id.tab1_text)
    TextView tab1TextView;

    @BindView(R.id.tab2)
    LinearLayout tab2;
    @BindView(R.id.tab2_img)
    ImageView tab2Img;
    @BindView(R.id.tab2_text)
    TextView tab2TextView;


//    @BindView(R.id.tab3)
//    LinearLayout tab3;
//    @BindView(R.id.tab3_img)
//    ImageView tab3Img;
//    @BindView(R.id.tab3_text)
//    TextView tab3TextView;


    @BindView(R.id.tab4)
    LinearLayout tab4;
    @BindView(R.id.tab4_img)
    ImageView tab4Img;
    @BindView(R.id.tab4_text)
    TextView tab4TextView;

    List<TextView> tabTextViews;
    List<ImageView> tabImageViews;


    // fragment
    KuaijieFragment kuaijieFragment;
    WeitiaoFragment weitiaoFragment;
    DengguangFragment dengguangFragment;
    List<BaseFragment> fragments;
    TabPagerAdapter tabPagerAdapter;


    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {
        Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        actionBar.setData(null, R.mipmap.ic_back, null, R.mipmap.ic_set, null, this);
        actionBar.setStatusBarHeight(getStatusBarHeight());
        initView();
        setCurrentTab(1);
    }


    private void initView() {
        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
//        tab3.setOnClickListener(this);
        tab4.setOnClickListener(this);

        tabTextViews = new ArrayList<>();
        tabTextViews.add(tab1TextView);
        tabTextViews.add(tab2TextView);
//        tabTextViews.add(tab3TextView);
        tabTextViews.add(tab4TextView);

        tabImageViews = new ArrayList<>();
        tabImageViews.add(tab1Img);
        tabImageViews.add(tab2Img);
//        tabImageViews.add(tab3Img);
        tabImageViews.add(tab4Img);

        fragments = new ArrayList<>();
        fragments.add(new KuaijieFragment());
        fragments.add(new WeitiaoFragment());
        fragments.add(new DengguangFragment());
        tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(tabPagerAdapter);
        viewPager.setScroll(true);
        viewPager.setOffscreenPageLimit(3);
    }


    private void setCurrentTab(int tabIndex) {
        if (tabCount == 3 && tabIndex == 4) {
            tabIndex = 3;
        }
        int position = tabIndex - 1;
        for (int i = 0; i < tabCount; i++) {
            if (position == i) {
                tabTextViews.get(i).setSelected(true);
                tabImageViews.get(i).setSelected(true);
            } else {
                tabTextViews.get(i).setSelected(false);
                tabImageViews.get(i).setSelected(false);
            }
        }
        viewPager.setCurrentItem(position, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab1:
                setCurrentTab(1);
                break;
            case R.id.tab2:
                setCurrentTab(2);
                break;
//            case R.id.tab3:
//            setCurrentTab(3);
//                break;
            case R.id.tab4:
                setCurrentTab(4);
                break;
            default:
                break;
        }
    }
}
