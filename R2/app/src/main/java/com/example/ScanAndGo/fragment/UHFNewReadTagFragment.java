package com.example.ScanAndGo.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ScanAndGo.MainActivity;
import com.example.ScanAndGo.NumberTool;
import com.example.ScanAndGo.R;
import com.example.ScanAndGo.Utils;
import com.rscja.deviceapi.RFIDWithUHFBLE;
import com.rscja.deviceapi.RFIDWithUHFUART;
import com.rscja.deviceapi.entity.InventoryModeEntity;
import com.rscja.deviceapi.entity.UHFTAGInfo;
import com.rscja.deviceapi.interfaces.ConnectionStatus;
import com.rscja.deviceapi.interfaces.KeyEventCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class UHFNewReadTagFragment extends Fragment implements View.OnClickListener {

    private String TAG = "UHFNewReadTagFragment";

    private boolean loopFlag = false;
    private ListView LvTags;
    private Button InventoryLoop, btInventory, btStop;//
    private Button btClear;
    private TextView tv_count, tv_total, tv_time;
    private boolean isExit = false;
    private long total = 0;
    private MainActivity mContext;
    private SimpleAdapter adapter;
    private HashMap<String, String> map;
    private ArrayList<HashMap<String, String>> tagList;
    private List<String> tempDatas = new ArrayList<>();
    private RadioButton rbEPC, rbEPC_TID, rbEPC_TID_USER;

    private AlertDialog mDialog;
    private EditText etUserPtr, etUserLen;

    private long mStrTime;
    private ExecutorService executorService;

    private ConnectStatus mConnectStatus = new ConnectStatus();

    //--------------------------------------获取 解析数据-------------------------------------------------
    final int FLAG_START = 0;//开始
    final int FLAG_STOP = 1;//停止
    final int FLAG_UHFINFO = 2;
    final int FLAG_UPDATE_TIME = 3; // 更新时间
    final int FLAG_GET_MODE = 4; // 获取模式
    final int FLAG_SUCCESS = 10;//成功
    final int FLAG_FAIL = 11;//失败
    final int FLAG_SET_SUCC = 12;
    final int FLAG_SET_FAIL = 13;

    boolean isRunning = false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FLAG_STOP:
                    if (msg.arg1 == FLAG_SUCCESS) {
                        //停止成功
                        btClear.setEnabled(true);
                        btStop.setEnabled(false);
                        InventoryLoop.setEnabled(true);
                        btInventory.setEnabled(true);
                    } else {
                        //停止失败
                        Utils.playSound(2);
                        Toast.makeText(mContext, R.string.uhf_msg_inventory_stop_fail, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case FLAG_START:
                    if (msg.arg1 == FLAG_SUCCESS) {
                        //开始读取标签成功
                        btClear.setEnabled(false);
                        btStop.setEnabled(true);
                        InventoryLoop.setEnabled(false);
                        btInventory.setEnabled(false);
                    } else {
                        //开始读取标签失败
                        Utils.playSound(2);
                    }
                    break;
                case FLAG_UHFINFO:
                    UHFTAGInfo info = (UHFTAGInfo) msg.obj;
                    addEPCToList(info);
                    Utils.playSound(1);
                    break;
                case FLAG_UPDATE_TIME:
                    float useTime = (System.currentTimeMillis() - mStrTime) / 1000.0F;
                    tv_time.setText(NumberTool.getPointDouble(loopFlag ? 1 : 3, useTime) + "s");
                    break;
                case FLAG_SET_SUCC:
                    showToast("success");
                    break;
                case FLAG_SET_FAIL:
                    showToast("fail");
                    break;
                case FLAG_GET_MODE:
                    byte[] data = (byte[]) msg.obj;
                    if (data != null) {
                        if (data[0] == 0) {
                            rbEPC.setChecked(true);
                        } else if (data[0] == 1) {
                            rbEPC_TID.setChecked(true);
                        } else if (data.length >= 3 && data[0] == 2) {
                            rbEPC_TID_USER.setChecked(true);
                            etUserPtr.setText(String.valueOf(data[1]));
                            etUserLen.setText(String.valueOf(data[2]));
                        } else {
                            rbEPC.setChecked(false);
                            rbEPC_TID.setChecked(false);
                            rbEPC_TID_USER.setChecked(false);
                        }
                        if (showToastFlag) showToast("success");
                    } else {
                        if (showToastFlag) showToast("fail");
                        rbEPC.setChecked(false);
                        rbEPC_TID.setChecked(false);
                        rbEPC_TID_USER.setChecked(false);
                    }
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uhf_new_read_tag, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        executorService = Executors.newFixedThreadPool(3);
        isExit = false;
        LvTags = (ListView) view.findViewById(R.id.LvTags);
        btInventory = (Button) view.findViewById(R.id.btInventory);
        InventoryLoop = (Button) view.findViewById(R.id.InventoryLoop);
        btStop = (Button) view.findViewById(R.id.btStop);
        btStop.setEnabled(false);
        btClear = (Button) view.findViewById(R.id.btClear);
        tv_count = (TextView) view.findViewById(R.id.tv_count);
        tv_total = (TextView) view.findViewById(R.id.tv_total);
        tv_time = (TextView) view.findViewById(R.id.tv_time);

        rbEPC = (RadioButton) view.findViewById(R.id.rbEPC);
        rbEPC.setOnClickListener(this);
        rbEPC_TID = (RadioButton) view.findViewById(R.id.rbEPC_TID);
        rbEPC_TID.setOnClickListener(this);
        rbEPC_TID_USER = (RadioButton) view.findViewById(R.id.rbEPC_TID_USER);
        rbEPC_TID_USER.setOnClickListener(this);

        InventoryLoop.setOnClickListener(this);
        btInventory.setOnClickListener(this);
        btClear.setOnClickListener(this);
        btStop.setOnClickListener(this);
        tagList = new ArrayList<HashMap<String, String>>();
        adapter = new SimpleAdapter(getContext(), tagList, R.layout.listtag_items,
                new String[]{MainActivity.TAG_DATA, MainActivity.TAG_LEN, MainActivity.TAG_COUNT, MainActivity.TAG_RSSI},
                new int[]{R.id.TvTagUii, R.id.TvTagLen, R.id.TvTagCount,
                        R.id.TvTagRssi});
        LvTags.setAdapter(adapter);
        clearData();

        initFilter(view);
    }

    private CheckBox cbFilter;
    private ViewGroup layout_filter;
    private Button btnSetFilter;
    private void initFilter(View view) {
        layout_filter = (ViewGroup) view.findViewById(R.id.layout_filter);
        layout_filter.setVisibility(View.GONE);
        cbFilter = (CheckBox) view.findViewById(R.id.cbFilter);
        cbFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                layout_filter.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        final EditText etLen = (EditText) view.findViewById(R.id.etLen);
        final EditText etPtr = (EditText) view.findViewById(R.id.etPtr);
        final EditText etData = (EditText) view.findViewById(R.id.etData);
        final RadioButton rbEPC = (RadioButton) view.findViewById(R.id.rbEPC_filter);
        final RadioButton rbTID = (RadioButton) view.findViewById(R.id.rbTID_filter);
        final RadioButton rbUser = (RadioButton) view.findViewById(R.id.rbUser_filter);
        btnSetFilter = (Button) view.findViewById(R.id.btSet);

        btnSetFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int filterBank = RFIDWithUHFUART.Bank_EPC;
                if (rbEPC.isChecked()) {
                    filterBank = RFIDWithUHFUART.Bank_EPC;
                } else if (rbTID.isChecked()) {
                    filterBank = RFIDWithUHFUART.Bank_TID;
                } else if (rbUser.isChecked()) {
                    filterBank = RFIDWithUHFUART.Bank_USER;
                }
                if (etLen.getText().toString() == null || etLen.getText().toString().isEmpty()) {
                    showToast("数据长度不能为空");
                    return;
                }
                if (etPtr.getText().toString() == null || etPtr.getText().toString().isEmpty()) {
                    showToast("起始地址不能为空");
                    return;
                }
                int ptr = Utils.toInt(etPtr.getText().toString(), 0);
                int len = Utils.toInt(etLen.getText().toString(), 0);
                String data = etData.getText().toString().trim();
                if (len > 0) {
                    String rex = "[\\da-fA-F]*"; //匹配正则表达式，数据为十六进制格式
                    if (data == null || data.isEmpty() || !data.matches(rex)) {
                        showToast("过滤的数据必须是十六进制数据");
//                        mContext.playSound(2);
                        return;
                    }

                    int l = data.replace(" ", "").length();
                    if (len <= l * 4) {
                        if(l % 2 != 0)
                            data += "0";
                    } else {
                        showToast(R.string.uhf_msg_set_filter_fail2);
                        return;
                    }

                    if (mContext.uhf.setFilter(filterBank, ptr, len, data)) {
                        showToast(R.string.uhf_msg_set_filter_succ);
                    } else {
                        showToast(R.string.uhf_msg_set_filter_fail);
                    }
                } else {
                    //禁用过滤
                    String dataStr = "00";
                    if (mContext.uhf.setFilter(RFIDWithUHFUART.Bank_EPC, 0, 0, dataStr)
                            && mContext.uhf.setFilter(RFIDWithUHFUART.Bank_TID, 0, 0, dataStr)
                            && mContext.uhf.setFilter(RFIDWithUHFUART.Bank_USER, 0, 0, dataStr)) {
                        showToast(R.string.msg_disable_succ);
                    } else {
                        showToast(R.string.msg_disable_fail);
                    }
                }
                cbFilter.setChecked(false);
            }
        });

        rbEPC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbEPC.isChecked()) {
                    etPtr.setText("32");
                }
            }
        });
        rbTID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbTID.isChecked()) {
                    etPtr.setText("0");
                }
            }
        });
        rbUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbUser.isChecked()) {
                    etPtr.setText("0");
                }
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "UHFReadTagFragment.onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        mContext = (MainActivity) getActivity();
        mContext.uhf.setKeyEventCallback(new KeyEventCallback() {
            @Override
            public void onKeyDown(int keycode) {
                Log.d(TAG, "  keycode =" + keycode + "   ,isExit=" + isExit);
                if (!isExit && mContext.uhf.getConnectStatus() == ConnectionStatus.CONNECTED) {
                    if(loopFlag) {
                        stopInventory();
                    } else {
                        startThread();
                    }
                }
            }

            @Override
            public void onKeyUp(int i) {

            }
        });
        mContext.addConnectStatusNotice(mConnectStatus);

        if (getUserVisibleHint()) {
            alertTips();
            if (mContext.uhf.getConnectStatus() == ConnectionStatus.CONNECTED) {
                setViewsEnabled(true);
                getMode(false);
            } else {
                cbFilter.setChecked(false);
                setViewsEnabled(false);
            }
        }
    }

    private void setViewsEnabled(boolean enabled) {
        InventoryLoop.setEnabled(enabled);
        btInventory.setEnabled(enabled);
        cbFilter.setEnabled(enabled);
        rbEPC.setEnabled(enabled);
        rbEPC_TID.setEnabled(enabled);
        rbEPC_TID_USER.setEnabled(enabled);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mContext.uhf.getConnectStatus() == ConnectionStatus.CONNECTED) {
            setViewsEnabled(true);
        } else {
            cbFilter.setChecked(false);
            setViewsEnabled(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopInventory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isExit = true;
        mContext.removeConnectStatusNotice(mConnectStatus);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btClear:
                clearData();
                break;
            case R.id.InventoryLoop:
                startThread();
                break;
            case R.id.btInventory:
                inventory();
                break;
            case R.id.btStop:
                if (mContext.uhf.getConnectStatus() == ConnectionStatus.CONNECTED) {
                    stopInventory();
                }
                break;
            case R.id.rbEPC:
                executorService.execute(epcModeRunnable);
                break;
            case R.id.rbEPC_TID:
                executorService.execute(epcTidModeRunnable);
                break;
            case R.id.rbEPC_TID_USER:
                alertSet();
                break;
        }
    }

    private Runnable epcModeRunnable = new Runnable() {
        @Override
        public void run() {
            setMode(Mode.EPC);
        }
    };

    private Runnable epcTidModeRunnable = new Runnable() {
        @Override
        public void run() {
            setMode(Mode.EPC_TID);
        }
    };

    private Runnable epcTidUserModeRunnable = new Runnable() {
        @Override
        public void run() {
            setMode(Mode.EPC_TID_USER);
        }
    };

    public enum  Mode {
        EPC, EPC_TID, EPC_TID_USER
    }

    private void setMode(Mode mode) {
        switch (mode) {
            case EPC:
                if (mContext.uhf.setEPCMode()) {
                    handler.sendEmptyMessage(FLAG_SET_SUCC);
                } else {
                    handler.sendEmptyMessage(FLAG_SET_FAIL);
                }
                break;
            case EPC_TID:
                if (mContext.uhf.setEPCAndTIDMode()) {
                    handler.sendEmptyMessage(FLAG_SET_SUCC);
                } else {
                    handler.sendEmptyMessage(FLAG_SET_FAIL);
                }
                break;
            case EPC_TID_USER:
                String strUserPtr = etUserPtr.getText().toString();
                String strUserLen = etUserLen.getText().toString();
                int userPtr = 0;
                int userLen = 6;
                if (!TextUtils.isEmpty(strUserPtr)) {
                    userPtr = Integer.valueOf(strUserPtr);
                }
                if (!TextUtils.isEmpty(strUserLen)) {
                    userLen = Integer.valueOf(strUserLen);
                }
                if (mContext.uhf.setEPCAndTIDUserMode(userPtr, userLen)) {
                    handler.sendEmptyMessage(FLAG_SET_SUCC);
                } else {
                    handler.sendEmptyMessage(FLAG_SET_FAIL);
                }
                break;
        }
    }

    private AlertDialog getAlert(View view, String title, String message, boolean cancelable, DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setIcon(R.drawable.webtext);
        if(view != null) {
            builder.setView(view);
        } else {
            builder.setMessage(message);
        }
        builder.setCancelable(cancelable);
        if(positiveListener != null) {
            builder.setPositiveButton(R.string.ok, positiveListener);
        } else {
            builder.setNegativeButton(R.string.close, null);
        }
        return builder.create();
    }

    private void alertSet() {
        if (mDialog == null) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_epc_tid_user, null);
            etUserPtr = view.findViewById(R.id.etUserPtr);
            etUserLen = view.findViewById(R.id.etUserLen);
            DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    executorService.execute(epcTidUserModeRunnable);
                }
            };
            mDialog = getAlert(view, "EPC+TID+USER", null, false, positiveListener);
        }
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    private AlertDialog tipsDialog;
    private void alertTips() {
        if (tipsDialog == null) {
            String tips = getString(R.string.inventory_epc_tid_user_tips);
            tipsDialog = getAlert(null, getString(R.string.tips), tips, true, null);
        }
        if (!tipsDialog.isShowing()) {
            tipsDialog.show();
        }
    }

    private void clearData() {
        tv_count.setText("0");
        tv_total.setText("0");
        tv_time.setText("0s");
        tagList.clear();
        tempDatas.clear();
        total = 0;
        adapter.notifyDataSetChanged();
    }

    /**
     * 停止识别
     */
    private void stopInventory() {
        loopFlag = false;
        ConnectionStatus connectionStatus = mContext.uhf.getConnectStatus();
        Message msg = handler.obtainMessage(FLAG_STOP);
        boolean result = mContext.uhf.stopInventory();
        if (result || connectionStatus == ConnectionStatus.DISCONNECTED) {
            msg.arg1 = FLAG_SUCCESS;
        } else {
            msg.arg1 = FLAG_FAIL;
        }
        mContext.isScanning = false;
        handler.sendMessage(msg);
    }

    class ConnectStatus implements MainActivity.IConnectStatus {
        @Override
        public void getStatus(ConnectionStatus connectionStatus) {
            if (connectionStatus == ConnectionStatus.CONNECTED) {
                if (!loopFlag) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getMode(false);
                    setViewsEnabled(true);
                }

                cbFilter.setEnabled(true);
            } else if (connectionStatus == ConnectionStatus.DISCONNECTED) {
                loopFlag = false;
                mContext.isScanning = false;
                btClear.setEnabled(true);
                btStop.setEnabled(false);
                setViewsEnabled(false);

                cbFilter.setChecked(false);
                cbFilter.setEnabled(false);
            }
        }
    }

    private boolean showToastFlag;
    private Runnable getModeRunnable = new Runnable() {
        @Override
        public void run() {
            if (mContext.uhf.getConnectStatus() == ConnectionStatus.CONNECTED) {
                InventoryModeEntity data = mContext.uhf.getEPCAndTIDUserMode();
                Message msg = handler.obtainMessage(FLAG_GET_MODE, data);
                handler.sendMessage(msg);
            }
        }
    };

    private void getMode(boolean showToast) {
        showToastFlag = showToast;
        executorService.execute(getModeRunnable);
    }

    private void inventory() {
        mStrTime = System.currentTimeMillis();
        UHFTAGInfo uhftagInfo = mContext.uhf.inventorySingleTag();
        if (uhftagInfo != null) {
            Message msg = handler.obtainMessage(FLAG_UHFINFO);
            msg.obj = uhftagInfo;
            handler.sendMessage(msg);
        }
        handler.sendEmptyMessage(FLAG_UPDATE_TIME);
    }

    public synchronized void startThread() {
        if (isRunning) {
            return;
        }
        isRunning = true;
        cbFilter.setChecked(false);
        new TagThread().start();
    }

    class TagThread extends Thread {

        public void run() {
            Message msg = handler.obtainMessage(FLAG_START);
            if (mContext.uhf.startInventoryTag()) {
                loopFlag = true;
                mContext.isScanning = true;
                mStrTime = System.currentTimeMillis();
                msg.arg1 = FLAG_SUCCESS;
            } else {
                msg.arg1 = FLAG_FAIL;
            }
            handler.sendMessage(msg);
            isRunning = false;//执行完成设置成false
            while (loopFlag) {
                getUHFInfo();
                handler.sendEmptyMessage(FLAG_UPDATE_TIME);
            }
            stopInventory();
        }
    }

    private synchronized void getUHFInfo() {
        List<UHFTAGInfo> list = mContext.uhf.readTagFromBufferList_EpcTidUser();
        if (list != null && !list.isEmpty()) {
            for (int k = 0; k < list.size(); k++) {
                Message msg = handler.obtainMessage(FLAG_UHFINFO, list.get(k));
                handler.sendMessage(msg);
                if(!loopFlag) {
                    break;
                }
            }
        }
    }

    /**
     * 添加EPC到列表中
     *
     * @param uhftagInfo
     */
    private void addEPCToList(UHFTAGInfo uhftagInfo) {
        if (!TextUtils.isEmpty(uhftagInfo.getEPC())) {
            int index = checkIsExist(uhftagInfo.getEPC());

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("EPC:");
            stringBuilder.append(uhftagInfo.getEPC());
            if (!TextUtils.isEmpty(uhftagInfo.getTid())) {
                stringBuilder.append("\r\nTID:");
                stringBuilder.append(uhftagInfo.getTid());
            }
            if (!TextUtils.isEmpty(uhftagInfo.getUser())) {
                stringBuilder.append("\r\nUSER:");
                stringBuilder.append(uhftagInfo.getUser());
            }

            map = new HashMap<String, String>();
            map.put(MainActivity.TAG_EPC, uhftagInfo.getEPC());
            map.put(MainActivity.TAG_DATA, stringBuilder.toString());
            map.put(MainActivity.TAG_COUNT, String.valueOf(1));
            map.put(MainActivity.TAG_RSSI, uhftagInfo.getRssi());
            // mContext.getAppContext().uhfQueue.offer(epc + "\t 1");
            if (index == -1) {
                tagList.add(map);
                tempDatas.add(uhftagInfo.getEPC());
                tv_count.setText("" + adapter.getCount());
            } else {
                int tagCount = Integer.parseInt(tagList.get(index).get(MainActivity.TAG_COUNT), 10) + 1;
                map.put(MainActivity.TAG_COUNT, String.valueOf(tagCount));
                tagList.set(index, map);
            }
            tv_total.setText(String.valueOf(++total));
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 判断EPC是否在列表中
     *
     * @param epc 索引
     * @return
     */
    public int checkIsExist(String epc) {
        if (TextUtils.isEmpty(epc)) {
            return -1;
        }
        return binarySearch(tempDatas, epc);
    }

    /**
     * 二分查找，找到该值在数组中的下标，否则为-1
     */
    static int binarySearch(List<String> array, String src) {
        int left = 0;
        int right = array.size() - 1;
        // 这里必须是 <=
        while (left <= right) {
            if (compareString(array.get(left), src)) {
                return left;
            } else if (left != right) {
                if (compareString(array.get(right), src))
                    return right;
            }
            left++;
            right--;
        }
        return -1;
    }

    static boolean compareString(String str1, String str2) {
        if (str1.length() != str2.length()) {
            return false;
        } else if (str1.hashCode() != str2.hashCode()) {
            return false;
        } else {
            char[] value1 = str1.toCharArray();
            char[] value2 = str2.toCharArray();
            int size = value1.length;
            for (int k = 0; k < size; k++) {
                if (value1[k] != value2[k]) {
                    return false;
                }
            }
            return true;
        }
    }

    private Toast toast;

    public void showToast(String text) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showToast(int resId) {
        showToast(getString(resId));
    }
}
