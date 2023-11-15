package com.example.uhf_bt;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.uhf_bt.component.ListAddItemView;
import com.example.uhf_bt.dto.AddItem;
import com.example.uhf_bt.dto.AssignBarCode;
import com.example.uhf_bt.dto.LocationItem;
import com.example.uhf_bt.dto.PostCategory;
import com.example.uhf_bt.dto.ReadAllItem;
import com.example.uhf_bt.fragment.BarcodeFragment;
import com.example.uhf_bt.json.JsonTaskGetAllItemList;
import com.example.uhf_bt.json.JsonTaskGetLocationItemList;
import com.example.uhf_bt.json.JsonTaskUpdateItem;
import com.rscja.deviceapi.RFIDWithUHFBLE;
import com.rscja.deviceapi.interfaces.ConnectionStatus;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class BoardRegisterLocationItemActivity extends BaseActivity {

    private ListView listView;

    private List<AddItem> itemList = new ArrayList<>();
    public boolean isRunning = false;
    public boolean isScanning = false;
    public String nowBarCode;
    public int locationId = 0;
    public int subLocationId = 0;

    public EditText etTime;
    private static final int REQUEST_SELECT_DEVICE = 1;
    public Button btn_connect;

    private final static String TAG = "MainActivity";
    public static final String SHOW_HISTORY_CONNECTED_LIST = "showHistoryConnectedList";
    private static final int RUNNING_DISCONNECT_TIMER = 10;
    private BoardRegisterLocationItemActivity.DisconnectTimerTask timerTask;
    private long timeCountCur; // 断开时间选择
    private long period = 1000 * 30; // 隔多少时间更新一次
    private long lastTouchTime = System.currentTimeMillis(); // 上次接触屏幕操作的时间戳
    private static final int REQUEST_ENABLE_BT = 2;
    private boolean mIsActiveDisconnect = true; // 是否主动断开连接
    public BluetoothAdapter mBtAdapter = null;

    public RFIDWithUHFBLE uhf = RFIDWithUHFBLE.getInstance();

    public TextView txtNowBarCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_register_location_item);

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        btn_connect = (Button)findViewById(R.id.btnConnectDevice);
        Globals g = (Globals)getApplication();

        if (g.isLogin == false)
        {
            startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 0);
        }

        txtNowBarCode = (TextView)findViewById(R.id.txtNowBarCode);

        reCallAPI();
    }

    public void reCallAPI()
    {
        Globals g = (Globals)getApplication();

        String req = g.apiUrl + "item/readall";

        try {
            itemList.clear();

            List<ReadAllItem> locationItems = new ArrayList<>();

            locationItems = new JsonTaskGetAllItemList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req).get();

            Collections.sort(locationItems);

            if (locationItems != null) {

                for (ReadAllItem p : locationItems) {

                    Log.d("aaaaa  aaaaaaa", p.name);
                    AddItem newVM = new AddItem(p.id, 2, p.name, null, p.barcode, false  );

                    itemList.add(newVM);
                }
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        listView = findViewById(R.id.listAssignLocationItems);
        ListAddItemView adapter = new ListAddItemView(this, itemList, null, this);

        // Set the adapter for the ListView
        listView.setAdapter(adapter);
    }

    public void onAssign(View v)
    {
        String req = Globals.apiUrl +  "item/assign-barcode?id=" + String.valueOf(Globals.checkedItem);

        AssignBarCode model = new AssignBarCode();

        model.barcode = Globals.nowBarCode;

        new JsonTaskUpdateItem().execute(req, model.toJsonString());

        Globals.nowBarCode = "";
        Globals.checkedItem = 0;

        reCallAPI();
    }

    public void onAddItemTo(View v)
    {
        startActivityForResult(new Intent(getApplicationContext(), BoardCategoryActivity.class), 0);
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RUNNING_DISCONNECT_TIMER:
                    long time = (long) msg.obj;
                    formatConnectButton(time);
                    break;
            }
        }
    };

    private void formatConnectButton(long disconnectTime) {
        if (uhf.getConnectStatus() == ConnectionStatus.CONNECTED) {
            if (!isScanning && System.currentTimeMillis() - lastTouchTime > 1000 * 30 && timerTask != null) {
                long minute = disconnectTime / 1000 / 60;
                if(minute > 0) {
                    btn_connect.setText(getString(R.string.disConnectForMinute, minute)); //倒计时分
                } else {
                    btn_connect.setText(getString(R.string.disConnectForSecond, disconnectTime / 1000)); // 倒计时秒
                }
            } else {
                btn_connect.setText(R.string.disConnect);
            }
        } else {
            btn_connect.setText(R.string.Connect);
        }
    }

    public void resetDisconnectTime() {
        timeCountCur = SPUtils.getInstance(getApplicationContext()).getSPLong(SPUtils.DISCONNECT_TIME, 0);
        if (timeCountCur > 0) {
            formatConnectButton(timeCountCur);
        }
    }

    public void OnConnectDevice(View view) {
        if (isScanning) {
            showToast(R.string.title_stop_read_card);
        } else if (uhf.getConnectStatus() == ConnectionStatus.CONNECTING) {
            showToast(R.string.connecting);
        } else if (uhf.getConnectStatus() == ConnectionStatus.CONNECTED) {
            disconnect(true);
        } else {
            showBluetoothDevice(true);
        }
    }

    private void showBluetoothDevice(boolean isHistory) {

        if (mBtAdapter == null) {
            showToast("Bluetooth is not available");
            return;
        }
        if (!mBtAdapter.isEnabled()) {
            Log.i(TAG, "onClick - BT not enabled yet");
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            Intent newIntent = new Intent(BoardRegisterLocationItemActivity.this, DeviceListActivity.class);
            newIntent.putExtra(SHOW_HISTORY_CONNECTED_LIST, isHistory);
            startActivityForResult(newIntent, REQUEST_SELECT_DEVICE);
            cancelDisconnectTimer();
        }
    }

    private class DisconnectTimerTask extends TimerTask {

        @Override
        public void run() {
            Log.e(TAG, "timeCountCur = " + timeCountCur);
            Message msg = mHandler.obtainMessage(RUNNING_DISCONNECT_TIMER, timeCountCur);
            mHandler.sendMessage(msg);
            if(isScanning) {
                resetDisconnectTime();
            } else if (timeCountCur <= 0){
                disconnect(true);
            }
            timeCountCur -= period;
        }
    }

    public void disconnect(boolean isActiveDisconnect) {
        cancelDisconnectTimer();
        mIsActiveDisconnect = isActiveDisconnect; // 主动断开为true
        uhf.disconnect();
    }

    public void cancelDisconnectTimer() {
        timeCountCur = 0;
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }
    public void OnScanBarCode(View v){

        Log.e("Scan button clicked::", "button clicked");

        if(!isRunning){
            isRunning=true;

//            if(str==null || str.isEmpty()){
//                new ScanThread( false, Integer.parseInt(etTime.getHint().toString())).start();
//            }else{
//                new ScanThread(false, Integer.parseInt(str)).start();
//            }

            new ScanThread(false, 0).start();

        }
    }


    class   ScanThread  extends Thread{
        boolean isContinuous = false;
        int time;
        public ScanThread(boolean isContinuous,int time){
            this.isContinuous = isContinuous;
            this.time = time;
        }

        public void run() {
            while (isRunning) {

                String data = null;
                byte[] temp = uhf.scanBarcodeToBytes();
                if (temp != null && temp.length>0) {

                    try {
                        data = new String(temp, "utf8");
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }

                    txtNowBarCode.setText(data);

//                    if (spingCodingFormat.getSelectedItemPosition() == 1) {
//                        try {
//                            data = new String(temp, "utf8");
//                        } catch (Exception ex) {
//                        }
//                    } else if (spingCodingFormat.getSelectedItemPosition() == 2) {
//                        try {
//                            data = new String(temp, "gb2312");
//                        } catch (Exception ex) {
//                        }
//                    } else {
//                        data = new String(temp);
//                    }


//                    Message msg = Message.obtain();
//                    msg.obj = data;
//                    handler.sendMessage(msg);
//                } else {
//                    Message msg = Message.obtain();
//                    msg.obj = "扫描失败";
//                    handler.sendMessage(msg);
//                }

                if(!isContinuous) {
                    isRunning = false;
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


    public void disconnect(boolean isActiveDisconnect) {
        cancelDisconnectTimer();
        mIsActiveDisconnect = isActiveDisconnect; // 主动断开为true
        uhf.disconnect();
    }

    public void cancelDisconnectTimer() {
        timeCountCur = 0;
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    public void btnLogOut(View v)
    {
        Globals g = (Globals) getApplication();

        g.isLogin = false;

        startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 0);
    }

    public void btnItem(View v)
    {
        startActivityForResult(new Intent(getApplicationContext(), BoardCategoryActivity.class), 0);

    }

    public void btnInventory(View v)
    {
        startActivityForResult(new Intent(getApplicationContext(), BoardInventoryActivity.class), 0);

    }

    public void btnLocation(View v)
    {
        startActivityForResult(new Intent(getApplicationContext(), BoardLocationActivity.class), 0);
    }}}

