package com.tencent.mars.xlog;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author zhaoyuan zhangweizang
 */
public class Log {
    private static final String TAG = "mars.xlog.log";
    public static final int LEVEL_VERBOSE = 0;
    public static final int LEVEL_DEBUG = 1;
    public static final int LEVEL_INFO = 2;
    public static final int LEVEL_WARNING = 3;
    public static final int LEVEL_ERROR = 4;
    public static final int LEVEL_FATAL = 5;
    public static final int LEVEL_NONE = 6;
    private static int level = 6;
    public static Context toastSupportContext = null;
    private static Log.LogImp debugLog = new Log.LogImp() {
        private Handler handler = new Handler(Looper.getMainLooper());

        public void logV(long logInstancePtr, String tag, String filename, String funcname, int line, int pid, long tid, long maintid, String log) {
            if (Log.level <= 0) {
                android.util.Log.v(tag, log);
            }

        }

        public void logI(long logInstancePtr, String tag, String filename, String funcname, int line, int pid, long tid, long maintid, String log) {
            if (Log.level <= 2) {
                android.util.Log.i(tag, log);
            }

        }

        public void logD(long logInstancePtr, String tag, String filename, String funcname, int line, int pid, long tid, long maintid, String log) {
            if (Log.level <= 1) {
                android.util.Log.d(tag, log);
            }

        }

        public void logW(long logInstancePtr, String tag, String filename, String funcname, int line, int pid, long tid, long maintid, String log) {
            if (Log.level <= 3) {
                android.util.Log.w(tag, log);
            }

        }

        public void logE(long logInstancePtr, String tag, String filename, String funcname, int line, int pid, long tid, long maintid, String log) {
            if (Log.level <= 4) {
                android.util.Log.e(tag, log);
            }

        }

        public void logF(long logInstancePtr, String tag, String filename, String funcname, int line, int pid, long tid, long maintid, final String log) {
            if (Log.level <= 5) {
                android.util.Log.e(tag, log);
                if (Log.toastSupportContext != null) {
                    this.handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(Log.toastSupportContext, log, Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        }

        public int getLogLevel(long logInstancePtr) {
            return Log.level;
        }

        public void setAppenderMode(long logInstancePtr, int mode) {
        }

        public long openLogInstance(int level, int mode, String cacheDir, String logDir, String nameprefix, int cacheDays) {
            return 0L;
        }

        public long getXlogInstance(String nameprefix) {
            return 0L;
        }

        public void releaseXlogInstance(String nameprefix) {
        }

        public void appenderOpen(int level, int mode, String cacheDir, String logDir, String nameprefix, int cacheDays) {
        }

        public void appenderClose() {
        }

        public void appenderFlush(long logInstancePtr, boolean isSync) {
        }

        public void setConsoleLogOpen(long logInstancePtr, boolean isOpen) {
        }

        public void setMaxAliveTime(long logInstancePtr, long aliveSeconds) {
        }

        public void setMaxFileSize(long logInstancePtr, long aliveSeconds) {
        }
    };
    private static Log.LogImp logImp;
    private static final String SYS_INFO;
    private static Map<String, LogInstance> sLogInstanceMap;

    public Log() {
    }

    public static void setLogImp(Log.LogImp imp) {
        logImp = imp;
    }

    public static Log.LogImp getImpl() {
        return logImp;
    }

    public static void appenderOpen(int level, int mode, String cacheDir, String logDir, String nameprefix, int cacheDays) {
        if (logImp != null) {
            logImp.appenderOpen(level, mode, cacheDir, logDir, nameprefix, cacheDays);
        }

    }

    public static void appenderClose() {
        if (logImp != null) {
            logImp.appenderClose();
            Iterator var0 = sLogInstanceMap.entrySet().iterator();

            while(var0.hasNext()) {
                Map.Entry<String, LogInstance> entry = (Map.Entry)var0.next();
                closeLogInstance((String)entry.getKey());
            }
        }

    }

    public static void appenderFlush(boolean isSync) {
        if (logImp != null) {
            logImp.appenderFlush(0L, isSync);
            Iterator var0 = sLogInstanceMap.entrySet().iterator();

            while(var0.hasNext()) {
                Map.Entry<String, LogInstance> entry = (Map.Entry)var0.next();
                ((Log.LogInstance)entry.getValue()).appenderFlush();
            }
        }

    }

    public static void appenderFlushSync(boolean isSync) {
        if (logImp != null) {
            logImp.appenderFlush(0L, isSync);
        }

    }

    public static int getLogLevel() {
        return logImp != null ? logImp.getLogLevel(0L) : 6;
    }

    public static void setLevel(int level, boolean jni) {
        Log.level = level;
        android.util.Log.w("mars.xlog.log", "new log level: " + level);
        if (jni) {
            android.util.Log.e("mars.xlog.log", "no jni log level support");
        }

    }

    public static void setConsoleLogOpen(boolean isOpen) {
        if (logImp != null) {
            logImp.setConsoleLogOpen(0L, isOpen);
        }

    }

    public static void f(String tag, String msg) {
        f(tag, msg, (Object[])null);
    }

    public static void e(String tag, String msg) {
        e(tag, msg, (Object[])null);
    }

    public static void w(String tag, String msg) {
        w(tag, msg, (Object[])null);
    }

    public static void i(String tag, String msg) {
        i(tag, msg, (Object[])null);
    }

    public static void d(String tag, String msg) {
        d(tag, msg, (Object[])null);
    }

    public static void v(String tag, String msg) {
        v(tag, msg, (Object[])null);
    }

    public static void f(String tag, String format, Object... obj) {
        if (logImp != null && logImp.getLogLevel(0L) <= 5) {
            String log = obj == null ? format : String.format(format, obj);
            logImp.logF(0L, tag, "", "", 0, Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), log);
        }

    }

    public static void e(String tag, String format, Object... obj) {
        if (logImp != null && logImp.getLogLevel(0L) <= 4) {
            String log = obj == null ? format : String.format(format, obj);
            if (log == null) {
                log = "";
            }

            logImp.logE(0L, tag, "", "", 0, Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), log);
        }

    }

    public static void w(String tag, String format, Object... obj) {
        if (logImp != null && logImp.getLogLevel(0L) <= 3) {
            String log = obj == null ? format : String.format(format, obj);
            if (log == null) {
                log = "";
            }

            logImp.logW(0L, tag, "", "", 0, Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), log);
        }

    }

    public static void i(String tag, String format, Object... obj) {
        if (logImp != null && logImp.getLogLevel(0L) <= 2) {
            String log = obj == null ? format : String.format(format, obj);
            if (log == null) {
                log = "";
            }

            logImp.logI(0L, tag, "", "", 0, Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), log);
        }

    }

    public static void d(String tag, String format, Object... obj) {
        if (logImp != null && logImp.getLogLevel(0L) <= 1) {
            String log = obj == null ? format : String.format(format, obj);
            if (log == null) {
                log = "";
            }

            logImp.logD(0L, tag, "", "", 0, Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), log);
        }

    }

    public static void v(String tag, String format, Object... obj) {
        if (logImp != null && logImp.getLogLevel(0L) <= 0) {
            String log = obj == null ? format : String.format(format, obj);
            if (log == null) {
                log = "";
            }

            logImp.logV(0L, tag, "", "", 0, Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), log);
        }

    }

    public static void printErrStackTrace(String tag, Throwable tr, String format, Object... obj) {
        if (logImp != null && logImp.getLogLevel(0L) <= 4) {
            String log = obj == null ? format : String.format(format, obj);
            if (log == null) {
                log = "";
            }

            log = log + "  " + android.util.Log.getStackTraceString(tr);
            logImp.logE(0L, tag, "", "", 0, Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), log);
        }

    }

    public static String getSysInfo() {
        return SYS_INFO;
    }

    public static Log.LogInstance openLogInstance(int level, int mode, String cacheDir, String logDir, String nameprefix, int cacheDays) {
        synchronized(sLogInstanceMap) {
            if (sLogInstanceMap.containsKey(nameprefix)) {
                return (Log.LogInstance)sLogInstanceMap.get(nameprefix);
            } else {
                Log.LogInstance instance = new Log.LogInstance(level, mode, cacheDir, logDir, nameprefix, cacheDays);
                sLogInstanceMap.put(nameprefix, instance);
                return instance;
            }
        }
    }

    public static void closeLogInstance(String prefix) {
        synchronized(sLogInstanceMap) {
            if (null != logImp && sLogInstanceMap.containsKey(prefix)) {
                Log.LogInstance logInstance = (Log.LogInstance)sLogInstanceMap.remove(prefix);
                logImp.releaseXlogInstance(prefix);
                logInstance.mLogInstancePtr = -1L;
            }

        }
    }

    public static Log.LogInstance getLogInstance(String prefix) {
        synchronized(sLogInstanceMap) {
            return sLogInstanceMap.containsKey(prefix) ? (Log.LogInstance)sLogInstanceMap.get(prefix) : null;
        }
    }

    static {
        logImp = debugLog;
        StringBuilder sb = new StringBuilder();

        try {
            sb.append("VERSION.RELEASE:[" + Build.VERSION.RELEASE);
            sb.append("] VERSION.CODENAME:[" + Build.VERSION.CODENAME);
            sb.append("] VERSION.INCREMENTAL:[" + Build.VERSION.INCREMENTAL);
            sb.append("] BOARD:[" + Build.BOARD);
            sb.append("] DEVICE:[" + Build.DEVICE);
            sb.append("] DISPLAY:[" + Build.DISPLAY);
            sb.append("] FINGERPRINT:[" + Build.FINGERPRINT);
            sb.append("] HOST:[" + Build.HOST);
            sb.append("] MANUFACTURER:[" + Build.MANUFACTURER);
            sb.append("] MODEL:[" + Build.MODEL);
            sb.append("] PRODUCT:[" + Build.PRODUCT);
            sb.append("] TAGS:[" + Build.TAGS);
            sb.append("] TYPE:[" + Build.TYPE);
            sb.append("] USER:[" + Build.USER + "]");
        } catch (Throwable var2) {
            var2.printStackTrace();
        }

        SYS_INFO = sb.toString();
        sLogInstanceMap = new HashMap();
    }

    public static class LogInstance {
        private long mLogInstancePtr;
        private String mPrefix;

        private LogInstance(int level, int mode, String cacheDir, String logDir, String nameprefix, int cacheDays) {
            this.mLogInstancePtr = -1L;
            this.mPrefix = null;
            if (Log.logImp != null) {
                this.mLogInstancePtr = Log.logImp.openLogInstance(level, mode, cacheDir, logDir, nameprefix, cacheDays);
                this.mPrefix = nameprefix;
            }

        }

        public void f(String tag, String format, Object... obj) {
            if (Log.logImp != null && this.getLogLevel() <= 5 && this.mLogInstancePtr != -1L) {
                String log = obj == null ? format : String.format(format, obj);
                Log.logImp.logF(this.mLogInstancePtr, tag, "", "", Process.myTid(), Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), log);
            }

        }

        public void e(String tag, String format, Object... obj) {
            if (Log.logImp != null && this.getLogLevel() <= 4 && this.mLogInstancePtr != -1L) {
                String log = obj == null ? format : String.format(format, obj);
                if (log == null) {
                    log = "";
                }

                Log.logImp.logE(this.mLogInstancePtr, tag, "", "", Process.myTid(), Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), log);
            }

        }

        public void w(String tag, String format, Object... obj) {
            if (Log.logImp != null && this.getLogLevel() <= 3 && this.mLogInstancePtr != -1L) {
                String log = obj == null ? format : String.format(format, obj);
                if (log == null) {
                    log = "";
                }

                Log.logImp.logW(this.mLogInstancePtr, tag, "", "", Process.myTid(), Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), log);
            }

        }

        public void i(String tag, String format, Object... obj) {
            if (Log.logImp != null && this.getLogLevel() <= 2 && this.mLogInstancePtr != -1L) {
                String log = obj == null ? format : String.format(format, obj);
                if (log == null) {
                    log = "";
                }

                Log.logImp.logI(this.mLogInstancePtr, tag, "", "", Process.myTid(), Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), log);
            }

        }

        public void d(String tag, String format, Object... obj) {
            if (Log.logImp != null && this.getLogLevel() <= 1 && this.mLogInstancePtr != -1L) {
                String log = obj == null ? format : String.format(format, obj);
                if (log == null) {
                    log = "";
                }

                Log.logImp.logD(this.mLogInstancePtr, tag, "", "", Process.myTid(), Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), log);
            }

        }

        public void v(String tag, String format, Object... obj) {
            if (Log.logImp != null && this.getLogLevel() <= 0 && this.mLogInstancePtr != -1L) {
                String log = obj == null ? format : String.format(format, obj);
                if (log == null) {
                    log = "";
                }

                Log.logImp.logV(this.mLogInstancePtr, tag, "", "", Process.myTid(), Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), log);
            }

        }

        public void printErrStackTrace(String tag, Throwable tr, String format, Object... obj) {
            if (Log.logImp != null && this.getLogLevel() <= 4 && this.mLogInstancePtr != -1L) {
                String log = obj == null ? format : String.format(format, obj);
                if (log == null) {
                    log = "";
                }

                log = log + "  " + android.util.Log.getStackTraceString(tr);
                Log.logImp.logE(this.mLogInstancePtr, tag, "", "", Process.myTid(), Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), log);
            }

        }

        public void appenderFlush() {
            if (Log.logImp != null && this.mLogInstancePtr != -1L) {
                Log.logImp.appenderFlush(this.mLogInstancePtr, false);
            }

        }

        public void appenderFlushSync() {
            if (Log.logImp != null && this.mLogInstancePtr != -1L) {
                Log.logImp.appenderFlush(this.mLogInstancePtr, true);
            }

        }

        public int getLogLevel() {
            return Log.logImp != null && this.mLogInstancePtr != -1L ? Log.logImp.getLogLevel(this.mLogInstancePtr) : 6;
        }

        public void setConsoleLogOpen(boolean isOpen) {
            if (null != Log.logImp && this.mLogInstancePtr != -1L) {
                Log.logImp.setConsoleLogOpen(this.mLogInstancePtr, isOpen);
            }

        }
    }

    public interface LogImp {
        void logV(long var1, String var3, String var4, String var5, int var6, int var7, long var8, long var10, String var12);

        void logI(long var1, String var3, String var4, String var5, int var6, int var7, long var8, long var10, String var12);

        void logD(long var1, String var3, String var4, String var5, int var6, int var7, long var8, long var10, String var12);

        void logW(long var1, String var3, String var4, String var5, int var6, int var7, long var8, long var10, String var12);

        void logE(long var1, String var3, String var4, String var5, int var6, int var7, long var8, long var10, String var12);

        void logF(long var1, String var3, String var4, String var5, int var6, int var7, long var8, long var10, String var12);

        int getLogLevel(long var1);

        void setAppenderMode(long var1, int var3);

        long openLogInstance(int var1, int var2, String var3, String var4, String var5, int var6);

        long getXlogInstance(String var1);

        void releaseXlogInstance(String var1);

        void appenderOpen(int var1, int var2, String var3, String var4, String var5, int var6);

        void appenderClose();

        void appenderFlush(long var1, boolean var3);

        void setConsoleLogOpen(long var1, boolean var3);

        void setMaxFileSize(long var1, long var3);

        void setMaxAliveTime(long var1, long var3);
    }
}

