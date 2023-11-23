package com.example.uhf_bt.tool;

import android.util.Log;

import java.io.File;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelUtils {
    private WritableWorkbook wwb=null;
    private File excelFile=null;
    public static String TAG="FileUtils";

    public ExcelUtils() {
    }

    // 创建excel表.
    public void createExcel(File file,String[] head) {
        WritableSheet ws = null;
        excelFile=file;
        try {
            if(file.exists()){
                file.delete();
            }
            if (!file.exists()) {
                wwb = Workbook.createWorkbook(file);
                ws = wwb.createSheet("sheet1", 0);
                // 在指定单元格插入数据
                for(int k=0;k<head.length;k++){
                    Label lbl1 = new Label(k, 0, head[k]);
                    ws.addCell(lbl1);
                }
                // 从内存中写入文件中
                wwb.write();
                wwb.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //将数据存入到Excel表中
    public void writeToExcel(String[] args) {
        try {
            Workbook oldWwb = Workbook.getWorkbook(excelFile);
            wwb = Workbook.createWorkbook(excelFile, oldWwb);
            WritableSheet ws = wwb.getSheet(0);
            // 当前行数
            int row = ws.getRows();
            for(int k=0;k<args.length;k++){
                Label lab1 = new Label(k, row, args[k]);
                ws.addCell(lab1);
            }
            // 从内存中写入文件中,只能刷一次.
            wwb.write();
            wwb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //将数据存入到Excel表中
    public void writeToExcel(List<String[]> listData ) {
        if(listData==null || listData.size()==0)
            return;

        Log.d(TAG,"writeToExcel:"+listData.size());
        try {
            Workbook oldWwb = Workbook.getWorkbook(excelFile);
            wwb = Workbook.createWorkbook(excelFile, oldWwb);
            WritableSheet ws = wwb.getSheet(0);
            // 当前行数
            int row = ws.getRows();
            for(int s=0;s<listData.size();s++){
                String[]args=listData.get(s);
                for(int k=0;k<args.length;k++){
                    Label lab1 = new Label(k, row, args[k]);
                    ws.addCell(lab1);
                }
                ++row;
            }
            // 从内存中写入文件中,只能刷一次.
            wwb.write();
            wwb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
