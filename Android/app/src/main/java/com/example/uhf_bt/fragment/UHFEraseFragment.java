package com.example.uhf_bt.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.uhf_bt.MainActivity;
import com.example.uhf_bt.R;
import com.example.uhf_bt.Utils;
import com.rscja.deviceapi.RFIDWithUHFBLE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by Administrator on 2019/6/14.
 * Description:
 */

public class UHFEraseFragment extends Fragment implements View.OnClickListener {

    private MainActivity mContext;
    private CheckBox cb_filter;
    private EditText etPtr_filter;
    private EditText etLen_filter;
    private EditText etData_filter;
    private RadioGroup rgFilterArea;
    private RadioButton rbEPC_filter;
    private RadioButton rbTID_filter;
    private RadioButton rbUser_filter;

    private Spinner spEraseArea;
    private EditText EtStartAddress, EtStartLength;
    private EditText EtAccessPwd;
    private Button btnErase;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uhf_erase, container, false);
        init(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = (MainActivity) getActivity();
    }

    private void init(View view) {
        etPtr_filter = view.findViewById(R.id.etPtr_filter);
        etLen_filter = view.findViewById(R.id.etLen_filter);
        etData_filter = view.findViewById(R.id.etData_filter);
        rgFilterArea = view.findViewById(R.id.rgFilterArea);
        rgFilterArea.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbEPC_filter:
                        etPtr_filter.setText("32");
                        break;
                    case R.id.rbTID_filter:
                        etPtr_filter.setText("0");
                        break;
                    case R.id.rbUser_filter:
                        etPtr_filter.setText("0");
                        break;
                }
            }
        });

        rbEPC_filter = view.findViewById(R.id.rbEPC_filter);
        rbTID_filter = view.findViewById(R.id.rbTID_filter);
        rbUser_filter = view.findViewById(R.id.rbUser_filter);

        EtStartAddress = view.findViewById(R.id.EtStartAddress);
        EtStartLength = view.findViewById(R.id.EtStartLength);
        EtAccessPwd = view.findViewById(R.id.EtAccessPwd);
        btnErase = view.findViewById(R.id.btnErase);
        btnErase.setOnClickListener(this);

        spEraseArea = view.findViewById(R.id.spEraseArea);
        spEraseArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spEraseArea.getSelectedItem().toString().equals("EPC")) {
                    EtStartAddress.setText("2");
                } else {
                    EtStartAddress.setText("0");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cb_filter = view.findViewById(R.id.cb_filter);
        cb_filter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    String data = etData_filter.getText().toString().trim();
                    String rex = "[\\da-fA-F]*"; //匹配正则表达式，数据为十六进制格式
                    if (TextUtils.isEmpty(data) || !data.matches(rex)) {
                        Toast.makeText(mContext, "过滤的数据必须是十六进制数据", Toast.LENGTH_SHORT).show();
                        cb_filter.setChecked(false);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnErase:
                erase();
                break;
        }
    }

    /**
     * 擦除数据
     */
    private void erase() {
        String strPWD = EtAccessPwd.getText().toString().trim();// 访问密码
        if (!TextUtils.isEmpty(strPWD)) {
            if (strPWD.length() != 8) {
                Toast.makeText(mContext, R.string.uhf_msg_addr_must_len8, Toast.LENGTH_SHORT).show();
                return;
            } else if (!Utils.vailHexInput(strPWD)) {
                Toast.makeText(mContext, R.string.rfid_mgs_error_nohex, Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
//            Toast.makeText(mContext, R.string.rfid_mgs_error_nopwd, Toast.LENGTH_SHORT).show();
//            return;
            strPWD = "00000000";
        }

        String startAddress = EtStartAddress.getText().toString().trim();
        if (startAddress.equals("")) {
            Toast.makeText(mContext, R.string.uhf_msg_addr_not_null, Toast.LENGTH_SHORT).show();
            return;
        } else if (!TextUtils.isDigitsOnly(startAddress)) {
            Toast.makeText(mContext, R.string.uhf_msg_addr_must_decimal, Toast.LENGTH_SHORT).show();
            return;
        }

        String startLen = EtStartLength.getText().toString().trim();
        if (startLen.equals("")) {
            Toast.makeText(mContext, R.string.uhf_msg_len_not_null, Toast.LENGTH_SHORT).show();
            return;
        } else if (!TextUtils.isDigitsOnly(startLen)) {
            Toast.makeText(mContext, R.string.uhf_msg_len_must_decimal, Toast.LENGTH_SHORT).show();
            return;
        }

        int eraseBank = spEraseArea.getSelectedItemPosition();
        boolean result = false;
        if (cb_filter.isChecked()) {
            String filterData = etData_filter.getText().toString();
            if (TextUtils.isEmpty(filterData)) {
                Toast.makeText(mContext, "过滤数据不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            String ptrStr = etPtr_filter.getText().toString();
            if (TextUtils.isEmpty(ptrStr)) {
                Toast.makeText(mContext, "过滤起始地址不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            String lenStr = etLen_filter.getText().toString();
            if (TextUtils.isEmpty(lenStr)) {
                Toast.makeText(mContext, "过滤数据长度不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            int filterPtr = Integer.parseInt(ptrStr);
            int filterCnt = Integer.parseInt(lenStr);
            int filterBank = RFIDWithUHFBLE.Bank_EPC;
            if (rbTID_filter.isChecked()) {
                filterBank = RFIDWithUHFBLE.Bank_TID;
            } else if (rbUser_filter.isChecked()) {
                filterBank = RFIDWithUHFBLE.Bank_USER;
            }
            result = mContext.uhf.eraseData(strPWD,
                    filterBank,
                    filterPtr,
                    filterCnt,
                    filterData,
                    eraseBank,
                    Integer.valueOf(startAddress),
                    Integer.valueOf(startLen));
        } else {
            result = mContext.uhf.eraseData(strPWD,
                    eraseBank,
                    Integer.valueOf(startAddress),
                    Integer.valueOf(startLen));
        }
        if (result) {
            Toast.makeText(mContext, R.string.rfid_mgs_erase_succ, Toast.LENGTH_SHORT).show();
            Utils.playSound(1);
        } else {
            Toast.makeText(mContext, R.string.rfid_mgs_erase_fail, Toast.LENGTH_SHORT).show();
            Utils.playSound(2);
        }
    }
}
