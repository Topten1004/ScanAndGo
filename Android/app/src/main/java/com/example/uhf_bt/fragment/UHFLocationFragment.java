package com.example.uhf_bt.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;

import com.example.uhf_bt.MainActivity;
import com.example.uhf_bt.R;
import com.example.uhf_bt.view.UhfLocationCanvasView;
import com.rscja.deviceapi.interfaces.IUHF;
import com.rscja.deviceapi.interfaces.IUHFLocationCallback;



public class UHFLocationFragment extends Fragment {

    String TAG="UHF_LocationFragment";
    private MainActivity mContext;
    private UhfLocationCanvasView llChart;

    private EditText etEPC;
    private Button btStart,btStop;
    final int EPC=2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_uhflocation, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = (MainActivity) getActivity();
        llChart=mContext.findViewById(R.id.llChart);
        etEPC=mContext.findViewById(R.id.etEPC);
        btStart=mContext.findViewById(R.id.btStart);
        btStop=mContext.findViewById(R.id.btStop);
        getView().post(new Runnable() {
            @Override
            public void run() {
                llChart.clean();
            }
        });
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocation();
            }
        });
        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocation();
            }
        });

        getView().post(new Runnable() {
            @Override
            public void run() {
                if(mContext.selectEPC!=null && !mContext.selectEPC.equals("")){
                    etEPC.setText(mContext.selectEPC);
                }else{
                    etEPC.setText("");
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView");
        stopLocation();
        Log.i(TAG, "onDestroyView end");
    }

    private void startLocation(){

       String epc=etEPC.getText().toString();
       if(epc!=null && epc.length()>0) {
           boolean result = mContext.uhf.startLocation(mContext, epc, IUHF.Bank_EPC, 32, new IUHFLocationCallback() {


               @Override
               public void getLocationValue(int i, boolean b) {
                   llChart.setData(i);
               }


           });
           if (!result) {
               Toast.makeText(mContext, R.string.psam_msg_fail, Toast.LENGTH_SHORT).show();
               return;
           }
           btStart.setEnabled(false);
           etEPC.setEnabled(false);
       }
    }

   public void stopLocation(){
       mContext.uhf.stopLocation();
       btStart.setEnabled(true);
       etEPC.setEnabled(true);
   }



}
