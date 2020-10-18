package com.sn.dianqi.util;

import android.util.Log;

import com.sn.dianqi.MyApplication;


/**
 *  Created by wanghongchuang
 *  on 2016/8/25.
 *  email:844285775@qq.com
 */

public class LogUtils {

    public static void i(String tag, String... strs){
        if(MyApplication.isDebug){
            StringBuilder sb = new StringBuilder();
            for(String str: strs){
                sb.append(str);
            }
            Log.i(tag, sb.toString());
        }
    }

    public static void v(String tag, String... strs){
        if(MyApplication.isDebug){
            StringBuilder sb = new StringBuilder();
            for(String str: strs){
                sb.append(str);
            }
            Log.v(tag, sb.toString());
        }
    }

    public static void d(String tag, String... strs){
        if(MyApplication.isDebug){
            StringBuilder sb = new StringBuilder();
            for(String str: strs){
                sb.append(str);
            }
            Log.d(tag, sb.toString());
        }
    }

    public static void w(String tag, String... strs){
        if(MyApplication.isDebug){
            StringBuilder sb = new StringBuilder();
            for(String str: strs){
                sb.append(str);
            }
            Log.w(tag, sb.toString());
        }
    }

    public static void e(String tag, String... strs){
        if(MyApplication.isDebug){
            StringBuilder sb = new StringBuilder();
            for(String str: strs){
                sb.append(str);
            }
            Log.e(tag, sb.toString());
        }
    }
}
