package com.example.uhf_bt.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.uhf_bt.BoardCategoryActivity;
import com.example.uhf_bt.Globals;
import com.example.uhf_bt.MainActivity;
import com.example.uhf_bt.R;
import com.example.uhf_bt.Utils;
import com.example.uhf_bt.component.ListAddItemView;
import com.example.uhf_bt.dto.AddItem;
import com.example.uhf_bt.dto.AssignBarCode;
import com.example.uhf_bt.dto.ReadAllItem;
import com.example.uhf_bt.json.JsonTaskGetAllItemList;
import com.example.uhf_bt.json.JsonTaskUpdateItem;
import com.rscja.deviceapi.interfaces.ConnectionStatus;
import com.rscja.deviceapi.interfaces.KeyEventCallback;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BarcodeFragment extends Fragment implements View.OnClickListener{
    static boolean isExit_=false;
    MainActivity mContext;

    private List<AddItem> itemList = new ArrayList<>();
    TextView nowBarcode;
    Button btnScan,btClear;
    Object lock=new Object();
    Spinner spingCodingFormat;
    CheckBox  cbContinuous;
    EditText etTime;

    ListView listView;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if(msg.obj.toString()!=null) {

                Globals.nowBarCode = msg.obj.toString();
                nowBarcode.setText(msg.obj.toString());
                Utils.playSound(1);
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_barcode, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

         isExit_=false;
         nowBarcode = (TextView)getActivity().findViewById(R.id.nowBarcode);
         cbContinuous=(CheckBox)getActivity().findViewById(R.id.cbContinuous);
         etTime=(EditText)getActivity().findViewById(R.id.etTime);

         btnScan=(Button)getActivity().findViewById(R.id.btnScan);
         btClear=(Button)getActivity().findViewById(R.id.btClear);
         btnScan.setOnClickListener(this);
         btClear.setOnClickListener(this);
         spingCodingFormat=(Spinner)getActivity().findViewById(R.id.spingCodingFormat);
         mContext=(MainActivity) getActivity();
         listView = (ListView) getActivity().findViewById(R.id.listAllItem);

        mContext.uhf.setKeyEventCallback(new KeyEventCallback() {
             @Override
             public void onKeyDown(int keycode) {
                 Log.d("DeviceAPI_setKeyEvent","  keycode ="+keycode +"   ,isExit_="+isExit_);
                 if(!isExit_ && mContext.uhf.getConnectStatus() == ConnectionStatus.CONNECTED)
                     scan();
             }

             @Override
             public void onKeyUp(int i) {

             }
         });
        cbContinuous.setOnClickListener(this);
        cbContinuous.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isExit_=true;
        isRuning=false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnScan:
                scan();
                break;
            case R.id.btClear:
                nowBarcode.setText("");
                break;
            case R.id.cbContinuous:
                if(!cbContinuous.isChecked()){
                    isRuning=false;
                }
                break;
        }
    }


    private synchronized void scan(){
       if(!isRuning){
           isRuning=true;
           String str=etTime.getText().toString();
           if(str==null || str.isEmpty()){
               new ScanThread(cbContinuous.isChecked(),Integer.parseInt(etTime.getHint().toString())).start();
           }else{
               new ScanThread(cbContinuous.isChecked(),Integer.parseInt(str)).start();
           }
       }
    }

    boolean isRuning=false;
    class   ScanThread  extends Thread{
        boolean isContinuous=false;
        int  time;
        public ScanThread(boolean isContinuous,int time){
            this.isContinuous=isContinuous;
            this.time=time;
        }
        public void run(){
           while (isRuning) {
               String data = null;
               byte[] temp = mContext.uhf.scanBarcodeToBytes();
               if (temp != null && temp.length>0) {
                   if (spingCodingFormat.getSelectedItemPosition() == 1) {
                       try {
                           data = new String(temp, "utf8");
                       } catch (Exception ex) {
                       }
                   } else if (spingCodingFormat.getSelectedItemPosition() == 2) {
                       try {
                           data = new String(temp, "gb2312");
                       } catch (Exception ex) {
                       }
                   } else {
                       data = new String(temp);
                   }
                   Message msg = Message.obtain();
                   msg.obj = data;
                   handler.sendMessage(msg);
               }else {
                   Message msg = Message.obtain();
                   msg.obj = "Scan failed";
                   handler.sendMessage(msg);
               }

               if(!isContinuous) {
                   isRuning = false;
                   break;
               }else {
                   try {
                       Thread.sleep(time);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }

           }
       }
   }

}
