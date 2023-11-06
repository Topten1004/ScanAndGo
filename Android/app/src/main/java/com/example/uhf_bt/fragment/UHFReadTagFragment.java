package com.example.uhf_bt.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uhf_bt.DateUtils;
import com.example.uhf_bt.FileUtils;
import com.example.uhf_bt.MainActivity;
import com.example.uhf_bt.NumberTool;
import com.example.uhf_bt.R;
import com.example.uhf_bt.Utils;
import com.example.uhf_bt.tool.CheckUtils;
import com.rscja.deviceapi.RFIDWithUHFBLE;
import com.rscja.deviceapi.entity.UHFTAGInfo;
import com.rscja.deviceapi.interfaces.ConnectionStatus;
import com.rscja.deviceapi.interfaces.KeyEventCallback;

import com.rscja.utility.StringUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import androidx.fragment.app.Fragment;


public class UHFReadTagFragment extends Fragment implements View.OnClickListener {

    private String TAG = "UHFReadTagFragment";
    int lastIndex=-1;
    private ListView LvTags;
    private Button InventoryLoop, btInventory, btStop;//
    private Button btInventoryPerMinute;
    private Button btClear;
    private TextView tv_count, tv_total, tv_time;
    private boolean isExit = false;
    private long total = 0;
    private MainActivity mContext;
    private MyAdapter adapter;
    private List<UHFTAGInfo> tempDatas = new ArrayList<>();
    private ArrayList<HashMap<String, String>> tagList;
    EditText etTime;
    private ConnectStatus mConnectStatus = new ConnectStatus();
    int maxRunTime = 99999999;
    //--------------------------------------获取 解析数据-------------------------------------------------
    final int FLAG_START = 0;//开始
    final int FLAG_STOP = 1;//停止
    final int FLAG_UPDATE_TIME = 2; // 更新时间
    final int FLAG_UHFINFO = 3;
    final int FLAG_UHFINFO_LIST = 5;
    final int FLAG_SUCCESS = 10;//成功
    final int FLAG_FAIL = 11;//失败
    private long mStrTime;

    Handler handler = new Handler(Looper.getMainLooper()) {
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
                        btInventoryPerMinute.setEnabled(true);
                    } else {
                        //停止失败
                        Utils.playSound(2);
                        Toast.makeText(mContext, R.string.uhf_msg_inventory_stop_fail, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case FLAG_UHFINFO_LIST:
                    List<UHFTAGInfo> list = ( List<UHFTAGInfo>) msg.obj;
                    addEPCToList(list);
                    break;
                case FLAG_START:
                    if (msg.arg1 == FLAG_SUCCESS) {
                        //开始读取标签成功
                        btClear.setEnabled(false);
                        btStop.setEnabled(true);
                        InventoryLoop.setEnabled(false);
                        btInventory.setEnabled(false);
                        btInventoryPerMinute.setEnabled(false);
                    } else {
                        //开始读取标签失败
                        Utils.playSound(2);
                    }
                    break;
                case FLAG_UPDATE_TIME:
                    float useTime = (System.currentTimeMillis() - mStrTime) / 1000.0F;
                    tv_time.setText(NumberTool.getPointDouble(1, useTime) + "s");
                    break;
                case FLAG_UHFINFO:
                    UHFTAGInfo info = (UHFTAGInfo) msg.obj;
                    List list1=new ArrayList<UHFTAGInfo>();
                    list1.add(info);
                    addEPCToList(list1);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uhfread_tag, container, false);
        initFilter(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "UHFReadTagFragment.onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        lastIndex=-1;
        mContext = (MainActivity) getActivity();
        init();
        selectIndex=-1;
        mContext.selectEPC=null;
    }

    @Override
    public void onPause() {
        super.onPause();
        stop();
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
            case R.id.btInventoryPerMinute:
                inventoryPerMinute();
                break;
            case R.id.InventoryLoop:
                startThread();
                break;
            case R.id.btInventory:
                inventory();
                break;
            case R.id.btStop:
                stop();
                break;
        }
    }

    private void init() {
        isExit = false;
        mContext.addConnectStatusNotice(mConnectStatus);
        LvTags = (ListView) mContext.findViewById(R.id.LvTags);
        btInventory = (Button) mContext.findViewById(R.id.btInventory);
        InventoryLoop = (Button) mContext.findViewById(R.id.InventoryLoop);
        btStop = (Button) mContext.findViewById(R.id.btStop);
        btStop.setEnabled(false);
        btClear = (Button) mContext.findViewById(R.id.btClear);
        tv_count = (TextView) mContext.findViewById(R.id.tv_count);
        tv_total = (TextView) mContext.findViewById(R.id.tv_total);
        tv_time = (TextView) mContext.findViewById(R.id.tv_time);
        etTime = (EditText) mContext.findViewById(R.id.etTime);

        InventoryLoop.setOnClickListener(this);
        btInventory.setOnClickListener(this);
        btClear.setOnClickListener(this);
        btStop.setOnClickListener(this);

        btInventoryPerMinute = mContext.findViewById(R.id.btInventoryPerMinute);
        btInventoryPerMinute.setOnClickListener(this);

        tagList = new ArrayList<>();
        mContext.tagList=tagList;
        adapter=new MyAdapter(mContext);
        LvTags.setAdapter(adapter);
        mContext.uhf.setKeyEventCallback(new KeyEventCallback() {
            @Override
            public void onKeyDown(int keycode) {
                Log.d(TAG, "  keycode =" + keycode + "   ,isExit=" + isExit);
                if (!isExit && mContext.uhf.getConnectStatus() == ConnectionStatus.CONNECTED) {
                    if(keycode==3){
                        mContext.isKeyDownUP=true;
                        startThread();
                    } else{
                        if(!mContext.isKeyDownUP){
                            if(keycode==1) {
                                if (mContext.isScanning) {
                                    stop();
                                } else {
                                    startThread();
                                }
                            }
                        }
                        if(keycode==2) {
                            if (mContext.isScanning) {
                                stop();
                                SystemClock.sleep(100);
                            }
                            //MR20
                            inventory();
                        }
                    }

                }
            }

            @Override
            public void onKeyUp(int keycode) {
                Log.d(TAG, "  keycode =" + keycode + "   ,isExit=" + isExit);
                if(keycode==4) {
                    stop();
                }
            }
        });
        LvTags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectIndex=position;
                adapter.notifyDataSetInvalidated();
                mContext.selectEPC=tagList.get(position).get(MainActivity.TAG_EPC);
            }
        });

        clearData();
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
        final RadioButton rbEPC = (RadioButton) view.findViewById(R.id.rbEPC);
        final RadioButton rbTID = (RadioButton) view.findViewById(R.id.rbTID);
        final RadioButton rbUser = (RadioButton) view.findViewById(R.id.rbUser);
        btnSetFilter = (Button) view.findViewById(R.id.btSet);

        btnSetFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int filterBank = RFIDWithUHFBLE.Bank_EPC;
                if (rbEPC.isChecked()) {
                    filterBank = RFIDWithUHFBLE.Bank_EPC;
                } else if (rbTID.isChecked()) {
                    filterBank = RFIDWithUHFBLE.Bank_TID;
                } else if (rbUser.isChecked()) {
                    filterBank = RFIDWithUHFBLE.Bank_USER;
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
                if (len > 0 && !TextUtils.isEmpty(data)) {
                    String rex = "[\\da-fA-F]*"; //匹配正则表达式，数据为十六进制格式
                    if (!data.matches(rex)) {
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
                    if (mContext.uhf.setFilter(RFIDWithUHFBLE.Bank_EPC, 0, 0, dataStr)
                            && mContext.uhf.setFilter(RFIDWithUHFBLE.Bank_TID, 0, 0, dataStr)
                            && mContext.uhf.setFilter(RFIDWithUHFBLE.Bank_USER, 0, 0, dataStr)) {
                        showToast(R.string.msg_disable_succ);
                    } else {
                        showToast(R.string.msg_disable_fail);
                    }
                }
                cbFilter.setChecked(true);
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
    public void onResume() {
        super.onResume();
        if (mContext.uhf.getConnectStatus() == ConnectionStatus.CONNECTED) {
            InventoryLoop.setEnabled(true);
            btInventory.setEnabled(true);
            btInventoryPerMinute.setEnabled(true);

            cbFilter.setEnabled(true);
        } else {
            InventoryLoop.setEnabled(false);
            btInventory.setEnabled(false);
            btInventoryPerMinute.setEnabled(false);

            cbFilter.setChecked(false);
            cbFilter.setEnabled(false);
        }
    }

    private void clearData() {
        total = 0;
        tv_count.setText("0");
        tv_total.setText("0");
        tv_time.setText("0s");
        tagList.clear();
        tempDatas.clear();
        adapter.notifyDataSetChanged();
        mContext.selectEPC=null;
        selectIndex=-1;
    }

    /**
     * 停止识别
     */
    private void stop() {
        cancelInventoryTask();
        mContext.isScanning=false;
    }

    private void stopInventory(){
        //Log.i(TAG, "stopInventory() 2");
        boolean result = mContext.uhf.stopInventory();
        ConnectionStatus connectionStatus = mContext.uhf.getConnectStatus();
        Message msg = handler.obtainMessage(FLAG_STOP);
        if (!result || connectionStatus == ConnectionStatus.DISCONNECTED) {
            msg.arg1 = FLAG_FAIL;
        } else {
            msg.arg1 = FLAG_SUCCESS;
        }
        handler.sendMessage(msg);
    }
    class ConnectStatus implements MainActivity.IConnectStatus {
        @Override
        public void getStatus(ConnectionStatus connectionStatus) {
            if (connectionStatus == ConnectionStatus.CONNECTED) {
                if (!mContext.isScanning) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    InventoryLoop.setEnabled(true);
                    btInventory.setEnabled(true);
                    btInventoryPerMinute.setEnabled(true);
                }

                cbFilter.setEnabled(true);
            } else if (connectionStatus == ConnectionStatus.DISCONNECTED) {
                stop();
                btClear.setEnabled(true);
                btStop.setEnabled(false);
                InventoryLoop.setEnabled(false);
                btInventory.setEnabled(false);
                btInventoryPerMinute.setEnabled(false);

                cbFilter.setChecked(false);
                cbFilter.setEnabled(false);
            }
        }
    }

    public void startThread() {
        if (mContext.isScanning) {
            return;
        }

        //--
        String time = etTime.getText().toString();
        if(time!=null && time.length()>0 && time.startsWith(".")){
            etTime.setText("");
            time="";
        }
        if (time != null && !time.isEmpty()) {
            maxRunTime = (int)(Float.parseFloat(time) * 1000);
            clearData();
        } else {
            maxRunTime=Integer.parseInt(etTime.getHint().toString())* 1000;
        }
        //--

        mContext.isScanning = true;
//        cbFilter.setChecked(true);
        new TagThread().start();
    }


    class TagThread extends Thread {
        public void run() {
            Message msg = handler.obtainMessage(FLAG_START);
            Log.i(TAG, "startInventoryTag() 1");
            if (mContext.uhf.startInventoryTag()) {
                mStrTime = System.currentTimeMillis();
                msg.arg1 = FLAG_SUCCESS;
            } else {
                msg.arg1 = FLAG_FAIL;
                mContext.isScanning = false;
            }
            handler.sendMessage(msg);
            long startTime = System.currentTimeMillis();
            while (mContext.isScanning) {
                List<UHFTAGInfo> list = getUHFInfo();
                if(list==null || list.size()==0){
                    SystemClock.sleep(1);
                }else{
                    Utils.playSound(1);
                    handler.sendMessage(handler.obtainMessage(FLAG_UHFINFO_LIST, list));
                }
                if(System.currentTimeMillis()-startTime>10){
                    startTime=System.currentTimeMillis();
                    handler.sendEmptyMessage(FLAG_UPDATE_TIME);
                }
                //-------------------------
                if ((System.currentTimeMillis() - mStrTime)>= maxRunTime) {
                    mContext.isScanning=false;
                    break;
                }
                //--------------------------------
            }
           stopInventory();
        }
    }

    private synchronized   List<UHFTAGInfo> getUHFInfo() {
        List<UHFTAGInfo> list=null;
        if(mContext.isSupportRssi){
            //旧主板才需要调用readTagFromBufferList_EpcTidUser 输出 RSSI
            list = mContext.uhf.readTagFromBufferList_EpcTidUser();
        }else {
            //读写器主板版本 2.20-2.29 readTagFromBufferList 函数支持输出Rssi，无需调用readTagFromBufferList_EpcTidUser
           list = mContext.uhf.readTagFromBufferList();
        }
        return list;
    }


    void insertTag(UHFTAGInfo info, int index,boolean exists){

        String data=info.getEPC();
        if(!TextUtils.isEmpty(info.getTid())){
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append("EPC:");
            stringBuilder.append(info.getEPC());
            stringBuilder.append("\n");
            stringBuilder.append("TID:");
            stringBuilder.append(info.getTid());
            if(!TextUtils.isEmpty(info.getUser())){
                stringBuilder.append("\n");
                stringBuilder.append("USER:");
                stringBuilder.append(info.getUser());
            }
            data=stringBuilder.toString();
        }
        HashMap<String,String> tagMap=null;
        if(exists){
            tagMap = tagList.get(index);
            tagMap.put(MainActivity.TAG_COUNT, String.valueOf(Integer.parseInt(tagMap.get(MainActivity.TAG_COUNT), 10) + 1));
        }else {
            tagMap = new HashMap<>();
            tagMap.put(MainActivity.TAG_EPC, info.getEPC());
            tagMap.put(MainActivity.TAG_COUNT, String.valueOf(1));
            tempDatas.add(index,info);
            tagList.add(index, tagMap);
            tv_count.setText(String.valueOf(tagList.size()));
        }
        tagMap.put(MainActivity.TAG_USER, info.getUser());
        tagMap.put(MainActivity.TAG_DATA, data);
        tagMap.put(MainActivity.TAG_TID, info.getTid());
        tagMap.put(MainActivity.TAG_RSSI, info.getRssi()==null?"":info.getRssi());
        tv_total.setText(String.valueOf(++total));
        adapter.notifyDataSetChanged();
    }
    private void addEPCToList(List<UHFTAGInfo> list) {
        for(int k=0;k<list.size();k++){
            boolean[] exists=new boolean[1];
            UHFTAGInfo info=list.get(k);
            int idx=CheckUtils.getInsertIndex(tempDatas,info,exists);
            insertTag(info,idx,exists[0]);
        }
    }

    private Timer mTimer = new Timer();
    private TimerTask mInventoryPerMinuteTask;
    private long period = 6 * 1000; // 每隔多少ms
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "BluetoothReader" + File.separator;
    private String fileName;
    private void inventoryPerMinute() {
        cancelInventoryTask();
        btInventoryPerMinute.setEnabled(false);
        btInventory.setEnabled(false);
        InventoryLoop.setEnabled(false);
        btStop.setEnabled(true);
        mContext.isScanning = true;
        fileName = path + "battery_" + DateUtils.getCurrFormatDate(DateUtils.DATEFORMAT_FULL) + ".txt";
        mInventoryPerMinuteTask = new TimerTask() {
            @Override
            public void run() {
                String data = DateUtils.getCurrFormatDate(DateUtils.DATEFORMAT_FULL) + "\t电量：" + mContext.uhf.getBattery() + "%\n";
                FileUtils.writeFile(fileName, data, true);
                inventory();
            }
        };
        mTimer.schedule(mInventoryPerMinuteTask, 0, period);
    }

    private void cancelInventoryTask() {
        if(mInventoryPerMinuteTask != null) {
            mInventoryPerMinuteTask.cancel();
            mInventoryPerMinuteTask = null;
        }
    }

    private void inventory() {
        mStrTime = System.currentTimeMillis();
        UHFTAGInfo info = mContext.uhf.inventorySingleTag();
        if (info != null) {
            Message msg = handler.obtainMessage(FLAG_UHFINFO);
            msg.obj = info;
            handler.sendMessage(msg);
        }
        handler.sendEmptyMessage(FLAG_UPDATE_TIME);
    }

    private Toast mToast;
    public void showToast(String text) {
        if(mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
        mToast.show();
    }

    public void showToast(int resId) {
        showToast(getString(resId));
    }
    //-----------------------------
    private int  selectIndex=-1;
    public final class ViewHolder {
        public TextView tvEPCTID;
        public TextView tvTagCount;
        public TextView tvTagRssi;
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }
        public int getCount() {
            // TODO Auto-generated method stub
            return tagList.size();
        }
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return tagList.get(arg0);
        }
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.listtag_items, null);
                holder.tvEPCTID = (TextView) convertView.findViewById(R.id.TvTagUii);
                holder.tvTagCount = (TextView) convertView.findViewById(R.id.TvTagCount);
                holder.tvTagRssi = (TextView) convertView.findViewById(R.id.TvTagRssi);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvEPCTID.setText((String) tagList.get(position).get(MainActivity.TAG_DATA));
            holder.tvTagCount.setText((String) tagList.get(position).get(MainActivity.TAG_COUNT));
            holder.tvTagRssi.setText((String) tagList.get(position).get(MainActivity.TAG_RSSI));

            if (position == selectIndex) {
                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
            }
            else {
                convertView.setBackgroundColor(Color.TRANSPARENT);
            }
            return convertView;
        }

    }



}
