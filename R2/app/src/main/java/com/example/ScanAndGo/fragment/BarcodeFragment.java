package com.example.ScanAndGo.fragment;


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
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ScanAndGo.Globals;
import com.example.ScanAndGo.MainActivity;
import com.example.ScanAndGo.R;
import com.example.ScanAndGo.Utils;
import com.rscja.deviceapi.interfaces.ConnectionStatus;
import com.rscja.deviceapi.interfaces.KeyEventCallback;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BarcodeFragment extends Fragment implements View.OnClickListener{
    static boolean isExit_=false;
    MainActivity mContext;
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

            nowBarcode.setText("handleMessage");
            if(msg.obj.toString()!=null) {

                Globals.nowBarCode = msg.obj.toString();
                nowBarcode.setText(msg.obj.toString());
                Utils.playSound(1);
            }
        }
    };

    public String getBarcodeData()
    {
        String result = nowBarcode.getText().toString();
        return result;
    }

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

    public void setEmptyText() {
        nowBarcode.setText("");
    }

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
