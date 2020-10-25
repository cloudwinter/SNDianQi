package com.sn.dianqi.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sn.dianqi.R;
import com.sn.dianqi.util.LogUtils;

import androidx.core.content.ContextCompat;


public class AnjianYuanView extends LinearLayout {


    public static final String TAG = "JiyiView";

    Context mContext;

    ImageView iconImageView;
    TextView titleTextView;

    int bgNormalRes = -1;
    int bgSelectedRes = -1;

    public AnjianYuanView(Context context) {
        super(context,null);
    }

    public AnjianYuanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        String title = "";
        int iconRes = -1;
        if (attrs != null) {
            TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.AnjianYuanView);
            iconRes = typedArray.getResourceId(R.styleable.AnjianYuanView_icon,-1);
            bgNormalRes = typedArray.getResourceId(R.styleable.AnjianYuanView_bgnormal,-1);
            bgSelectedRes = typedArray.getResourceId(R.styleable.AnjianYuanView_bgselected,-1);
            title = typedArray.getString(R.styleable.AnjianYuanView_title);
            LogUtils.i(TAG, "title:" + title);
        }
        setOrientation(VERTICAL);
        View contentView = inflate(mContext,R.layout.view_anjianyuan,this);
        iconImageView = findViewById(R.id.img_icon);
        titleTextView = findViewById(R.id.text_title);
        titleTextView.setText(title);
        if (iconRes != -1) {
            iconImageView.setBackground(ContextCompat.getDrawable(mContext, iconRes));
        }
        if (bgNormalRes != -1) {
            setBackground(ContextCompat.getDrawable(mContext,bgNormalRes));
        }
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected) {
            if (bgSelectedRes != -1) {
                setBackground(ContextCompat.getDrawable(mContext,bgSelectedRes));
            }
        } else {
            if (bgNormalRes != -1) {
                setBackground(ContextCompat.getDrawable(mContext,bgNormalRes));
            }
        }
    }
}
