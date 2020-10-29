package com.sn.dianqi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sn.dianqi.MainActivity;
import com.sn.dianqi.R;
import com.sn.dianqi.util.LocaleUtils;
import com.sn.dianqi.util.Prefer;
import com.sn.dianqi.util.PreferenceUtil;

import java.util.Locale;

import androidx.annotation.NonNull;

/**
 * 语言对话框
 */
public class LanguageDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private RelativeLayout rlChinese;
    private ImageView imgSelectedChinese;
    private RelativeLayout rlEnglish;
    private ImageView imgSelectedEnglish;
    private TextView cancel;

    public LanguageDialog(@NonNull Context context) {
        super(context, R.style.LanguageDialogStyle);
        mContext = context;
        initView(context);
    }

    public LanguageDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_language, null);
        rlChinese = view.findViewById(R.id.rl_chinese);
        rlChinese.setOnClickListener(this);
        rlEnglish = view.findViewById(R.id.rl_english);
        rlEnglish.setOnClickListener(this);
        imgSelectedChinese = view.findViewById(R.id.img_selected_zh);
        imgSelectedEnglish = view.findViewById(R.id.img_selected_en);
        cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        setContentView(view);

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        view.setLayoutParams(layoutParams);

        getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    public void show() {
        super.show();
        // 判断当前选中的语言
        if (Prefer.getInstance().getSelectedLanguage().equals("zh")) {
            imgSelectedChinese.setVisibility(View.VISIBLE);
            imgSelectedEnglish.setVisibility(View.GONE);
        } else {
            imgSelectedChinese.setVisibility(View.GONE);
            imgSelectedEnglish.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.rl_chinese:
                imgSelectedChinese.setVisibility(View.VISIBLE);
                imgSelectedEnglish.setVisibility(View.GONE);
                Prefer.getInstance().setSelectedLanguage("zh");
                if (LocaleUtils.needUpdateLocale(mContext, LocaleUtils.LOCALE_CHINESE)) {
                    LocaleUtils.updateLocale(mContext, LocaleUtils.LOCALE_CHINESE);
                    restartAct();
                }
                dismiss();
                break;
            case R.id.rl_english:
                imgSelectedChinese.setVisibility(View.GONE);
                imgSelectedEnglish.setVisibility(View.VISIBLE);
                Prefer.getInstance().setSelectedLanguage("en");
//                if (LocaleUtils.needUpdateLocale(mContext, LocaleUtils.LOCALE_ENGLISH)) {
//                    LocaleUtils.updateLocale(mContext, LocaleUtils.LOCALE_ENGLISH);
//
//                }

                Resources resources = mContext.getResources();
                DisplayMetrics dm = resources.getDisplayMetrics();
                Configuration config = resources.getConfiguration();
                // 应用用户选择语言
                config.locale = Locale.ENGLISH;
                resources.updateConfiguration(config, dm);
                dismiss();
                restartAct();
                break;

        }
    }


    /**
     * 重启当前Activity
     */
    private void restartAct() {
//        finish();
        Intent _Intent = new Intent(mContext, MainActivity.class);
        _Intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        _Intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        _Intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(_Intent);
        //清除Activity退出和进入的动画
//        overridePendingTransition(0, 0);
    }
}
