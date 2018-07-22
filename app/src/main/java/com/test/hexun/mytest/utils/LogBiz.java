package com.test.hexun.mytest.utils;

import android.util.Log;

import com.test.hexun.mytest.tag.Tag;

/**
 * Created by hexun on 2018/7/21.
 */

public class LogBiz {
    public static void i(String content) {
        int length = content.length();
        length = length / 100;
        int logLength = 0;
        for (int i = 0; i < length; i++) {
            logLength = 100 * (i + 1);
            Log.i(Tag.tag, content.substring(logLength - 100, logLength));
        }
        Log.i(Tag.tag, content.substring(logLength, content.length()));
    }
}
