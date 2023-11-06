package com.example.uhf_bt.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.uhf_bt.MainActivity;
import com.example.uhf_bt.R;
import com.example.uhf_bt.Utils;
import com.rscja.deviceapi.RFIDWithUHFBLE;
import androidx.fragment.app.Fragment;


public class UHFKillFragment extends Fragment implements View.OnClickListener {
    private MainActivity mContext;
    CheckBox cb_filter_kill;
    EditText etPtr_filter_kill;
    EditText etLen_filter_kill;
    EditText etData_filter_kill;
    RadioButton rbEPC_filter_kill;
    RadioButton rbTID_filter_kill;
    RadioButton rbUser_filter_kill;
    EditText EtAccessPwd_Kill;
    Button btnKill;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_uhfkill, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = (MainActivity) getActivity();
        cb_filter_kill = (CheckBox) getView().findViewById(R.id.cb_filter_kill);
        EtAccessPwd_Kill = (EditText) getView().findViewById(R.id.EtAccessPwd_Kill);
        etPtr_filter_kill = (EditText) getView().findViewById(R.id.etPtr_filter_kill);
        etLen_filter_kill = (EditText) getView().findViewById(R.id.etLen_filter_kill);
        etData_filter_kill = (EditText) getView().findViewById(R.id.etData_filter_kill);
        rbEPC_filter_kill = (RadioButton) getView().findViewById(R.id.rbEPC_filter_kill);
        rbTID_filter_kill = (RadioButton) getView().findViewById(R.id.rbTID_filter_kill);
        rbUser_filter_kill = (RadioButton) getView().findViewById(R.id.rbUser_filter_kill);
        btnKill = (Button) getView().findViewById(R.id.btnKill);

        rbEPC_filter_kill.setOnClickListener(this);
        rbTID_filter_kill.setOnClickListener(this);
        rbUser_filter_kill.setOnClickListener(this);
        btnKill.setOnClickListener(this);

        cb_filter_kill.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    String data = etData_filter_kill.getText().toString().trim();
                    String rex = "[\\da-fA-F]*"; //匹配正则表达式，数据为十六进制格式
                    if (TextUtils.isEmpty(data) || !data.matches(rex)) {
                        Toast.makeText(mContext, "过滤的数据必须是十六进制数据", Toast.LENGTH_SHORT).show();
                        cb_filter_kill.setChecked(false);
                        return;
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rbEPC_filter_kill:
                etPtr_filter_kill.setText("32");
                break;
            case R.id.rbTID_filter_kill:
                etPtr_filter_kill.setText("0");
                break;
            case R.id.rbUser_filter_kill:
                etPtr_filter_kill.setText("0");
                break;
            case R.id.btnKill:
                kill();
                break;
        }
    }

    public void kill() {
        String strPWD = EtAccessPwd_Kill.getText().toString().trim();// 访问密码

        if (!TextUtils.isEmpty(strPWD)) {
            if (strPWD.length() != 8) {
                Toast.makeText(mContext, R.string.uhf_msg_addr_must_len8, Toast.LENGTH_SHORT).show();
                return;
            } else if (!Utils.vailHexInput(strPWD)) {
                Toast.makeText(mContext, R.string.rfid_mgs_error_nohex, Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(mContext, R.string.rfid_mgs_error_nopwd, Toast.LENGTH_SHORT).show();
            return;
        }

        boolean result = false;
        if (cb_filter_kill.isChecked()) {
            String filterData = etData_filter_kill.getText().toString();
            if (filterData == null || filterData.isEmpty()) {
                Toast.makeText(mContext, "过滤数据不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (etPtr_filter_kill.getText().toString() == null || etPtr_filter_kill.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "过滤起始地址不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (etLen_filter_kill.getText().toString() == null || etLen_filter_kill.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "过滤数据长度不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            int filterPtr = Integer.parseInt(etPtr_filter_kill.getText().toString());
            int filterCnt = Integer.parseInt(etLen_filter_kill.getText().toString());
            int filterBank = RFIDWithUHFBLE.Bank_EPC;
            if (rbTID_filter_kill.isChecked()) {
                filterBank = RFIDWithUHFBLE.Bank_TID;
            } else if (rbUser_filter_kill.isChecked()) {
                filterBank = RFIDWithUHFBLE.Bank_USER;
            }
            result = mContext.uhf.killTag(strPWD,
                    filterBank,
                    filterPtr,
                    filterCnt,
                    filterData);
        } else {
            result = mContext.uhf.killTag(strPWD);
        }
        if (!result) {
            Toast.makeText(mContext, R.string.rfid_mgs_kill_fail, Toast.LENGTH_SHORT).show();
            Utils.playSound(2);
        } else {
            Toast.makeText(mContext, R.string.rfid_mgs_kill_succ, Toast.LENGTH_SHORT).show();
            Utils.playSound(1);
        }

    }
}
