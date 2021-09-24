package com.luojianping.xlog;


import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

import com.tencent.mars.xlog.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LogPlus {
    private static int sCurrentLogLevel = Log.LEVEL_DEBUG;
    private static String sPrefix = null;

    @IntDef({Log.LEVEL_VERBOSE, Log.LEVEL_DEBUG, Log.LEVEL_INFO, Log.LEVEL_WARNING, Log.LEVEL_ERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LogLevel {
    }

    /**
     * 初始化LogPlus，可选
     *
     * @param prefix   Tag前缀，可以防止 RIL，IMS，AT，GSM，STK，CDMA，SMS 开头的log不会被打印
     * @param logLevel 打log等级
     */
    public static void init(@Nullable String prefix, @LogLevel int logLevel) {
        if (prefix != null) {
            prefix = prefix.trim();
            if (prefix.length() > 0) {
                sPrefix = prefix;
            }
        }

        sCurrentLogLevel = logLevel;
    }

    public static void v(@Nullable String tag, String msg, @Nullable Throwable tr) {
        log(Log.LEVEL_VERBOSE, tag, msg, tr);
    }

    public static void v(@Nullable String tag, String msg) {
        log(Log.LEVEL_VERBOSE, tag, msg, null);
    }

    public static void v(String msg, @Nullable Throwable tr) {
        log(Log.LEVEL_VERBOSE, null, msg, tr);
    }

    public static void v(String msg) {
        log(Log.LEVEL_VERBOSE, null, msg, null);
    }

    public static void d(@Nullable String tag, String msg, @Nullable Throwable tr) {
        log(Log.LEVEL_DEBUG, tag, msg, tr);
    }

    public static void d(@Nullable String tag, String msg) {
        log(Log.LEVEL_DEBUG, tag, msg, null);
    }

    public static void d(String msg, @Nullable Throwable tr) {
        log(Log.LEVEL_DEBUG, null, msg, tr);
    }

    public static void d(String msg) {
        log(Log.LEVEL_DEBUG, null, msg, null);
    }

    public static void i(@Nullable String tag, String msg, @Nullable Throwable tr) {
        log(Log.LEVEL_INFO, tag, msg, tr);
    }

    public static void i(@Nullable String tag, String msg) {
        log(Log.LEVEL_INFO, tag, msg, null);
    }

    public static void i(String msg, @Nullable Throwable tr) {
        log(Log.LEVEL_INFO, null, msg, tr);
    }

    public static void i(String msg) {
        log(Log.LEVEL_INFO, null, msg, null);
    }

    public static void w(@Nullable String tag, String msg, @Nullable Throwable tr) {
        log(Log.LEVEL_WARNING, tag, msg, tr);
    }

    public static void w(@Nullable String tag, String msg) {
        log(Log.LEVEL_WARNING, tag, msg, null);
    }

    public static void w(String msg, @Nullable Throwable tr) {
        log(Log.LEVEL_WARNING, null, msg, tr);
    }

    public static void w(String msg) {
        log(Log.LEVEL_WARNING, null, msg, null);
    }

    public static void e(@Nullable String tag, String msg, @Nullable Throwable tr) {
        log(Log.LEVEL_ERROR, tag, msg, tr);
    }

    public static void e(@Nullable String tag, String msg) {
        log(Log.LEVEL_ERROR, tag, msg, null);
    }

    public static void e(String msg, @Nullable Throwable tr) {
        log(Log.LEVEL_ERROR, null, msg, tr);
    }

    public static void e(String msg) {
        log(Log.LEVEL_ERROR, null, msg, null);
    }

    private static void log(int logLevel, String tag, String msg, Throwable tr) {

        if (logLevel < sCurrentLogLevel) {
            return;
        }
        StackTraceElement e = Thread.currentThread().getStackTrace()[4];
        String fileName = e.getFileName();
        int lineNum = e.getLineNumber();
        String methodName = e.getMethodName();

        StringBuilder sb = new StringBuilder();

        sb.append(methodName)
                .append('(')
                .append(fileName)
                .append(':')
                .append(lineNum)
                .append(')')
                .append(msg);

        msg = sb.toString();

        sb.delete(0, sb.length());

        // 注意，如果tag是"IMS"开头的(还有其他)，log会不打印，所以加个"前缀_"
        // 参考这里 http://stackoverflow.com/a/36469141/5324526
        if (sPrefix != null) {
            sb.append(sPrefix).append('_');
        }

        if (tag == null || tag.length() == 0 || tag.trim().length() == 0) {
            String className = e.getClassName();
            int index = className.lastIndexOf('.') + 1;
            sb.append(className, index, className.length());
        } else {
            sb.append(tag);
        }

        tag = sb.toString();

        if (tr == null) {
            switch (logLevel) {
                case Log.LEVEL_VERBOSE:
                    Log.v(tag, msg);
                    break;
                case Log.LEVEL_DEBUG:
                    Log.d(tag, msg);
                    break;
                case Log.LEVEL_INFO:
                    Log.i(tag, msg);
                    break;
                case Log.LEVEL_WARNING:
                    Log.w(tag, msg);
                    break;
                case Log.LEVEL_ERROR:
                    Log.e(tag, msg);
                    break;
            }
        } else {
            switch (logLevel) {
                case Log.LEVEL_VERBOSE:
                    Log.v(tag, msg, tr);
                    break;
                case Log.LEVEL_DEBUG:
                    Log.d(tag, msg, tr);
                    break;
                case Log.LEVEL_INFO:
                    Log.i(tag, msg, tr);
                    break;
                case Log.LEVEL_WARNING:
                    Log.w(tag, msg, tr);
                    break;
                case Log.LEVEL_ERROR:
                    Log.e(tag, msg, tr);
                    break;
            }
        }
    }
}
