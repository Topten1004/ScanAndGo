package com.example.uhf_bt.tool;

public class LogUtils {

    private  static CwLog cwLog=new  CwLog();
    public static void d(String tag, String log){
        cwLog.d(tag,log);
    }
    public static void e(String tag, String log){
        cwLog.e(tag,log);
    }

    public static void setDebug(boolean debug){
        cwLog.setDebug(debug);

    }
    public static boolean isDebug(){
        return cwLog.isDebug();
    }
}
