package com.example.uhf_bt.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.uhf_bt.FileUtils;
import com.example.uhf_bt.MainActivity;
import com.example.uhf_bt.R;
import com.rscja.deviceapi.interfaces.ConnectionStatus;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class BTRenameFragment extends Fragment {
    static boolean isExit_ = false;
    MainActivity mContext;
    EditText etNewName;
    EditText etOldName;
    Button btSet;

    public BTRenameFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_btrename, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isExit_ = false;
        mContext = (MainActivity) getActivity();
        etNewName = (EditText) getActivity().findViewById(R.id.etNewName);
        etOldName = (EditText) getActivity().findViewById(R.id.etOldName);
        etNewName.setText(mContext.remoteBTName);
        etOldName.setEnabled(false);
        etOldName.setVisibility(View.GONE);
        btSet = (Button) getActivity().findViewById(R.id.btSet);
        btSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = etNewName.getText().toString();
                if (newName != null && newName.length() == 0) {
                    Toast.makeText(mContext, "设置失败", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    boolean result = mContext.uhf.setRemoteBluetoothName(newName);
                    if (result) {
                        mContext.updateConnectMessage(mContext.remoteBTName, newName);
                        mContext.saveConnectedDevice(mContext.remoteBTAdd, newName);
                        Toast.makeText(mContext, "设置成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "设置失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        mContext.addConnectStatusNotice(new MainActivity.IConnectStatus(){
            @Override
            public void getStatus(ConnectionStatus connectionStatus) {
              if(connectionStatus==ConnectionStatus.CONNECTED){
                  etNewName.setText(mContext.remoteBTName);
              }
            }
        });
    }
}
