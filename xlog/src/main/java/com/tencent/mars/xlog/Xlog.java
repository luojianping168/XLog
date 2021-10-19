package com.tencent.mars.xlog;

public class Xlog implements Log.LogImp {
	public static final int LEVEL_ALL = 0;
	public static final int LEVEL_VERBOSE = 0;
	public static final int LEVEL_DEBUG = 1;
	public static final int LEVEL_INFO = 2;
	public static final int LEVEL_WARNING = 3;
	public static final int LEVEL_ERROR = 4;
	public static final int LEVEL_FATAL = 5;
	public static final int LEVEL_NONE = 6;
	public static final int COMPRESS_LEVEL1 = 1;
	public static final int COMPRESS_LEVEL2 = 2;
	public static final int COMPRESS_LEVEL3 = 3;
	public static final int COMPRESS_LEVEL4 = 4;
	public static final int COMPRESS_LEVEL5 = 5;
	public static final int COMPRESS_LEVEL6 = 6;
	public static final int COMPRESS_LEVEL7 = 7;
	public static final int COMPRESS_LEVEL8 = 8;
	public static final int COMPRESS_LEVEL9 = 9;
	public static final int AppednerModeAsync = 0;
	public static final int AppednerModeSync = 1;
	public static final int ZLIB_MODE = 0;
	public static final int ZSTD_MODE = 1;

	public Xlog() {
	}

	public static void open(boolean isLoadLib, int level, int mode, String cacheDir, String logDir, String nameprefix, String pubkey) {
		if (isLoadLib) {
			System.loadLibrary("c++_shared");
			System.loadLibrary("marsxlog");
		}

		Xlog.XLogConfig logConfig = new Xlog.XLogConfig();
		logConfig.level = level;
		logConfig.mode = mode;
		logConfig.logdir = logDir;
		logConfig.nameprefix = nameprefix;
		logConfig.pubkey = pubkey;
		logConfig.compressmode = 0;
		logConfig.compresslevel = 0;
		logConfig.cachedir = cacheDir;
		logConfig.cachedays = 0;
		appenderOpen(logConfig);
	}

	private static String decryptTag(String tag) {
		return tag;
	}

	public void logV(long logInstancePtr, String tag, String filename, String funcname, int line, int pid, long tid, long maintid, String log) {
		logWrite2(logInstancePtr, 0, decryptTag(tag), filename, funcname, line, pid, tid, maintid, log);
	}

	public void logD(long logInstancePtr, String tag, String filename, String funcname, int line, int pid, long tid, long maintid, String log) {
		logWrite2(logInstancePtr, 1, decryptTag(tag), filename, funcname, line, pid, tid, maintid, log);
	}

	public void logI(long logInstancePtr, String tag, String filename, String funcname, int line, int pid, long tid, long maintid, String log) {
		logWrite2(logInstancePtr, 2, decryptTag(tag), filename, funcname, line, pid, tid, maintid, log);
	}

	public void logW(long logInstancePtr, String tag, String filename, String funcname, int line, int pid, long tid, long maintid, String log) {
		logWrite2(logInstancePtr, 3, decryptTag(tag), filename, funcname, line, pid, tid, maintid, log);
	}

	public void logE(long logInstancePtr, String tag, String filename, String funcname, int line, int pid, long tid, long maintid, String log) {
		logWrite2(logInstancePtr, 4, decryptTag(tag), filename, funcname, line, pid, tid, maintid, log);
	}

	public void logF(long logInstancePtr, String tag, String filename, String funcname, int line, int pid, long tid, long maintid, String log) {
		logWrite2(logInstancePtr, 5, decryptTag(tag), filename, funcname, line, pid, tid, maintid, log);
	}

	public void appenderOpen(int level, int mode, String cacheDir, String logDir, String nameprefix, int cacheDays) {
		Xlog.XLogConfig logConfig = new Xlog.XLogConfig();
		logConfig.level = level;
		logConfig.mode = mode;
		logConfig.logdir = logDir;
		logConfig.nameprefix = nameprefix;
		logConfig.compressmode = 0;
		logConfig.pubkey = "";
		logConfig.cachedir = cacheDir;
		logConfig.cachedays = cacheDays;
		appenderOpen(logConfig);
	}

	public static native void logWrite(Xlog.XLoggerInfo var0, String var1);

	public static void logWrite2(int level, String tag, String filename, String funcname, int line, int pid, long tid, long maintid, String log) {
		logWrite2(0L, level, tag, filename, funcname, line, pid, tid, maintid, log);
	}

	public static native void logWrite2(long var0, int var2, String var3, String var4, String var5, int var6, int var7, long var8, long var10, String var12);

	public native int getLogLevel(long var1);

	public native void setAppenderMode(long var1, int var3);

	public long openLogInstance(int level, int mode, String cacheDir, String logDir, String nameprefix, int cacheDays) {
		Xlog.XLogConfig logConfig = new Xlog.XLogConfig();
		logConfig.level = level;
		logConfig.mode = mode;
		logConfig.logdir = logDir;
		logConfig.nameprefix = nameprefix;
		logConfig.compressmode = 0;
		logConfig.pubkey = "";
		logConfig.cachedir = cacheDir;
		logConfig.cachedays = cacheDays;
		return this.newXlogInstance(logConfig);
	}

	public native long getXlogInstance(String var1);

	public native void releaseXlogInstance(String var1);

	public native long newXlogInstance(Xlog.XLogConfig var1);

	public native void setConsoleLogOpen(long var1, boolean var3);

	private static native void appenderOpen(Xlog.XLogConfig var0);

	public native void appenderClose();

	public native void appenderFlush(long var1, boolean var3);

	public native void setMaxFileSize(long var1, long var3);

	public native void setMaxAliveTime(long var1, long var3);

	public static class XLogConfig {
		public int level = 2;
		public int mode = 0;
		public String logdir;
		public String nameprefix;
		public String pubkey = "";
		public int compressmode = 0;
		public int compresslevel = 0;
		public String cachedir;
		public int cachedays = 0;

		public XLogConfig() {
		}
	}

	static class XLoggerInfo {
		public int level;
		public String tag;
		public String filename;
		public String funcname;
		public int line;
		public long pid;
		public long tid;
		public long maintid;

		XLoggerInfo() {
		}
	}
}