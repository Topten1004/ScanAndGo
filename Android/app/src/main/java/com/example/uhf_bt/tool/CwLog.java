package com.example.uhf_bt.tool;


import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CwLog {

    private static final String TAG = "CwLog";
    private static final String DEFAULT_LOG_PATH =  Environment.getExternalStorageDirectory() + "/UHFLog/";
	public CwLog(){
            File file = new File(DEFAULT_LOG_PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
	}
	public void setDebug(boolean debug){


	}
	public boolean isDebug(){
		return true;// "1".equals(SharedPreferencesUtils.getSharedPreferences("isDebug"));
	}

	public void i(String tag, String msg){
		if(isDebug()){
			Log.i(TAG,tag+"==>"+msg);
			writerFile(tag,msg,true);
		}
  	}
  	public void v(String tag, String msg){
		if(isDebug()){
			Log.v(TAG,tag+"==>"+msg);
			writerFile(tag,msg,true);
		}
  	}
  	public void d(String tag, String msg){
		if(isDebug()){
			Log.d(TAG,tag+"==>"+msg);
			writerFile(tag,msg,true);
		}
  	}
  	public void w(String tag, String msg){
		if(isDebug()){
			Log.w(TAG,tag+"==>"+msg);
			writerFile(tag,msg,true);
		}
  	}
	public void e(String tag, String msg){
		if(isDebug()){
			Log.e(TAG,tag+"==>"+msg);
			writerFile(tag,msg,true);
		}
  	}
	private synchronized void writerFile(String tag, String msg, boolean append){
		if(msg.isEmpty())
			return;
		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd");
		String day= df.format(new Date());
		String filepatch=DEFAULT_LOG_PATH+"CwLog_"+day+".txt";
		File file =new File(filepatch);
		FileOutputStream fileOutputStream = null;
		if (!file.exists() || file.length()>1024*1024*50) {
			try{
				file.createNewFile();
			} catch (Exception ex) {
				Log.e(TAG,"writerFile,"+ex);
			}
		}
		try{
			SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time= df2.format(new Date());
			String outlog = time+" "+tag+" "+msg+"\n";
			fileOutputStream = new FileOutputStream(file,append);
			fileOutputStream.write(outlog.getBytes());
		} catch (Exception e) {
				Log.e(TAG,"writerFile,"+e);
		} finally {
			try {
				fileOutputStream.close();
			} catch (Exception e) {
			
			}
		}
	}
}  
