package com.example.uhf_bt.fragment;

import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.uhf_bt.MainActivity;
import com.example.uhf_bt.R;
import com.example.uhf_bt.Utils;
import com.rscja.deviceapi.RFIDWithUHFBLE;
import com.rscja.deviceapi.interfaces.ConnectionStatus;
import com.rscja.deviceapi.interfaces.KeyEventCallback;

import androidx.fragment.app.Fragment;


public class UHFReadFragment extends Fragment implements View.OnClickListener {
    private MainActivity mContext;
    private String TAG = "UHFReadFragment";
    private boolean isExit = false;
    Spinner SpinnerBank_Read;
    EditText EtPtr_Read;
    EditText EtLen_Read;
    EditText EtAccessPwd_Read;
    Spinner SpinnerOption_Read;
    EditText EtPtr2_Read;
    EditText EtLen2_Read;
    EditText EtData_Read;
    Button BtRead;
    private ViewGroup layout_read_filter;


    CheckBox cb_filter;
    EditText etPtr_filter;
    EditText etData_filter;
    EditText etLen_filter;
    RadioButton rbEPC_filter;
    RadioButton rbTID_filter;
    RadioButton rbUser_filter;
    CheckBox cb_QT_R;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_uhfread, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = (MainActivity) getActivity();
        SpinnerBank_Read = (Spinner) getView().findViewById(R.id.SpinnerBank_Read);
        EtPtr_Read = (EditText) getView().findViewById(R.id.EtPtr_Read);
        EtLen_Read = (EditText) getView().findViewById(R.id.EtLen_Read);
        EtAccessPwd_Read = (EditText) getView().findViewById(R.id.EtAccessPwd_Read);
        SpinnerOption_Read = (Spinner) getView().findViewById(R.id.SpinnerOption_Read);
        EtPtr2_Read = (EditText) getView().findViewById(R.id.EtPtr2_Read);
        EtLen2_Read = (EditText) getView().findViewById(R.id.EtLen2_Read);
        EtData_Read = (EditText) getView().findViewById(R.id.EtData_Read);
        etLen_filter = (EditText) getView().findViewById(R.id.etLen_filter);
        BtRead = (Button) getView().findViewById(R.id.BtRead);
        layout_read_filter = getView().findViewById(R.id.layout_read_filter);
        layout_read_filter.setVisibility(View.GONE);

        cb_QT_R = (CheckBox) getView().findViewById(R.id.cb_QT_R);
        cb_filter = (CheckBox) getView().findViewById(R.id.cb_filter);
        etPtr_filter = (EditText) getView().findViewById(R.id.etPtr_filter);
        etData_filter = (EditText) getView().findViewById(R.id.etData_filter);
        rbEPC_filter = (RadioButton) getView().findViewById(R.id.rbEPC_filter);
        rbTID_filter = (RadioButton) getView().findViewById(R.id.rbTID_filter);
        rbUser_filter = (RadioButton) getView().findViewById(R.id.rbUser_filter);

        rbEPC_filter.setOnClickListener(this);
        rbTID_filter.setOnClickListener(this);
        rbUser_filter.setOnClickListener(this);
        BtRead.setOnClickListener(this);

        //     EtData_Read.setKeyListener(null);
        EtPtr2_Read.setEnabled(false);
        EtLen2_Read.setEnabled(false);
        EtData_Read.setText("");

        cb_filter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                layout_read_filter.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                if (isChecked) {
                    String data = etData_filter.getText().toString().trim();
                    String rex = "[\\da-fA-F]*"; //匹配正则表达式，数据为十六进制格式
                    if (data == null || data.isEmpty() || !data.matches(rex)) {
                        return;
                    }
                }
            }
        });
        SpinnerBank_Read.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String element = adapterView.getItemAtPosition(i).toString();//得到spanner的值
                if (element.equals("EPC")) {
                    EtPtr_Read.setText("2");
                } else {
                    EtPtr_Read.setText("0");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mContext.uhf.setKeyEventCallback(new KeyEventCallback() {
            @Override
            public void onKeyDown(int keycode) {
             read();
            }

            @Override
            public void onKeyUp(int i) {

            }

        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rbEPC_filter:
                if (rbEPC_filter.isChecked()) {
                    etPtr_filter.setText("32");
                }
                break;
            case R.id.rbTID_filter:
                if (rbTID_filter.isChecked()) {
                    etPtr_filter.setText("0");
                }
                break;
            case R.id.rbUser_filter:
                if (rbUser_filter.isChecked()) {
                    etPtr_filter.setText("0");
                }
                break;
            case R.id.BtRead:
                read();
                break;
        }
    }

    private void read() {
        String ptrStr = EtPtr_Read.getText().toString().trim();
        if (ptrStr.equals("")) {
            Toast.makeText(mContext, R.string.uhf_msg_addr_not_null, Toast.LENGTH_SHORT).show();
            return;
        } else if (!TextUtils.isDigitsOnly(ptrStr)) {
            Toast.makeText(mContext, R.string.uhf_msg_addr_must_decimal, Toast.LENGTH_SHORT).show();
            return;
        }

        String cntStr = EtLen_Read.getText().toString().trim();
        if (cntStr.equals("")) {
            Toast.makeText(mContext, R.string.uhf_msg_len_not_null, Toast.LENGTH_SHORT).show();
            return;
        } else if (!TextUtils.isDigitsOnly(cntStr)) {
            Toast.makeText(mContext, R.string.uhf_msg_len_must_decimal, Toast.LENGTH_SHORT).show();
            return;
        }

        String pwdStr = EtAccessPwd_Read.getText().toString().trim();
        if (!TextUtils.isEmpty(pwdStr)) {
            if (pwdStr.length() != 8) {
                Toast.makeText(mContext, R.string.uhf_msg_addr_must_len8, Toast.LENGTH_SHORT).show();
                return;
            } else if (!Utils.vailHexInput(pwdStr)) {
                Toast.makeText(mContext, R.string.rfid_mgs_error_nohex, Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            pwdStr = "00000000";
        }

        String data = "";
        int  Bank = SpinnerBank_Read.getSelectedItemPosition();
        if (cb_filter.isChecked()) { //  过滤
            String filterData = etData_filter.getText().toString();
            if (TextUtils.isEmpty(filterData)) {
                Toast.makeText(mContext, "过滤数据不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            String ptrFilter = etPtr_filter.getText().toString();
            if (TextUtils.isEmpty(ptrFilter)) {
                Toast.makeText(mContext, "过滤起始地址不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            String lenStr = etLen_filter.getText().toString();
            if (TextUtils.isEmpty(lenStr)) {
                Toast.makeText(mContext, "过滤数据长度不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            String rex = "[\\da-fA-F]*"; //匹配正则表达式，数据为十六进制格式
            if (filterData == null || filterData.isEmpty() || !filterData.matches(rex)) {
                Toast.makeText(mContext, "过滤的数据必须是十六进制数据", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Integer.parseInt(lenStr) /4 > filterData.length()) {
                Toast.makeText(mContext, "过滤数据长度和过滤内容不匹配", Toast.LENGTH_SHORT).show();
                return;
            }
            if(filterData.length()%2 !=0){
                filterData=filterData+"0";
            }

            int filterPtr = Integer.parseInt(ptrFilter);
            int filterCnt = Integer.parseInt(lenStr);
            int filterBank = RFIDWithUHFBLE.Bank_EPC;
            if (rbEPC_filter.isChecked()) {
                filterBank = RFIDWithUHFBLE.Bank_EPC;
            } else if (rbTID_filter.isChecked()) {
                filterBank = RFIDWithUHFBLE.Bank_TID;
            } else if (rbUser_filter.isChecked()) {
                filterBank = RFIDWithUHFBLE.Bank_USER;
            }
            data = mContext.uhf.readData(pwdStr,
                    filterBank,
                    filterPtr,
                    filterCnt,
                    filterData,
                    Bank,
                    Integer.parseInt(ptrStr),
                    Integer.parseInt(cntStr)
            );
        } else {
            data = mContext.uhf.readData(pwdStr,
                    Bank,
                    Integer.parseInt(ptrStr),
                    Integer.parseInt(cntStr));
        }
        EtData_Read.setText(data);
        if (data != null && data.length() > 0) {
            Toast.makeText(mContext, R.string.rfid_msg_read_succ, Toast.LENGTH_SHORT).show();
            Utils.playSound(1);
        } else {
            Toast.makeText(mContext, R.string.rfid_msg_read_fail, Toast.LENGTH_SHORT).show();
            Utils.playSound(2);
        }
    }
}
