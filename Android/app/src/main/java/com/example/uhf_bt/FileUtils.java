package com.example.uhf_bt;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-5-22.
 */

public class FileUtils {
    static String TAG = "FileUtils";
    public static String ADDR = "btaddress";
    public static String NAME = "btname";
    public static String filePath = Environment.getExternalStorageDirectory() + File.separator + "BTDeviceList.xml";

    public static void saveXmlList(List<String[]> data) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            // 获得一个序列化工具
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "utf-8");
            // 设置文件头
            serializer.startDocument("utf-8", true);
            serializer.startTag(null, "root");
            for (int i = 0; i < data.size(); i++) {
                serializer.startTag(null, "bt");
                // 蓝牙地址
                serializer.startTag(null, ADDR);
                serializer.text(data.get(i)[0]);
                serializer.endTag(null, ADDR);
                // 蓝牙名称
                serializer.startTag(null, NAME);
                serializer.text(data.get(i)[1]);
                serializer.endTag(null, NAME);
                serializer.endTag(null, "bt");
            }
            serializer.endTag(null, "root");
            serializer.endDocument();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String[]> readXmlList() {
        ArrayList<String[]> list = new ArrayList<String[]>();
        try {
            File path = new File(filePath);
            if (!path.exists()) {
                return list;
            }
            FileInputStream fis = new FileInputStream(path);

            // 获得pull解析器对象
            XmlPullParser parser = Xml.newPullParser();
            // 指定解析的文件和编码格式
            parser.setInput(fis, "utf-8");
            int eventType = parser.getEventType(); // 获得事件类型
            String addr = null;
            String name = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName(); // 获得当前节点的名称
                switch (eventType) {
                    case XmlPullParser.START_TAG: // 当前等于开始节点
                        if ("root".equals(tagName)) { //
                        } else if ("bt".equals(tagName)) { //
                        } else if (ADDR.equals(tagName)) { // <name>
                            addr = parser.nextText();
                        } else if (NAME.equals(tagName)) { // <age>
                            name = parser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG: // </persons>
                        if ("bt".equals(tagName)) {
                            Log.i(TAG, "addr---" + addr);
                            Log.i(TAG, "name---" + name);
                            String[] str = new String[2];
                            str[0] = addr;
                            str[1] = name;
                            list.add(str);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next(); // 获得下一个事件类型
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return list;
    }

    public static void clearXmlList() {
        List<String[]> list = readXmlList();
        list.clear();
        saveXmlList(list);
    }

    public static void writeFile(String fileName, String data, boolean append) {
        if (TextUtils.isEmpty(data))
            return;

        int index = fileName.lastIndexOf(File.separator);
        if (index != -1) {
            File filePath = new File(fileName.substring(0, index));
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
        } else {
            return;
        }

        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("chmod 0666 " + file);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file, append);
            fileOutputStream.write(data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception e) {
            }
        }
    }

    private static String readInStream(FileInputStream inStream) {
        if(inStream==null)
            return null;
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[512];
            int length = -1;
            while ((length = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
            }

            outStream.close();
            inStream.close();
            return outStream.toString();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    /**
     * 读取文件内容
     *
     * @param path
     * @return
     */
    public static String readFile(String path) {

        FileInputStream fin = null;
        try {
            fin = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }

        return readInStream(fin);
    }
}
