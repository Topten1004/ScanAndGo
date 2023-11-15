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
import com.example.uhf_bt.dto.LocationItem;
import com.example.uhf_bt.fragment.BarcodeFragment;
import com.example.uhf_bt.json.JsonTaskGetLocationItemList;
import com.rscja.deviceapi.RFIDWithUHFBLE;
import com.rscja.deviceapi.interfaces.ConnectionStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class BoardRegisterLocationItemActivity extends BaseActivity {

    private ListView listView;

    private List<AddItem> itemList = new ArrayList<>();
    ScrollView scrBarcode;
    Spinner spingCodingFormat;
    TextView tvData;
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

        reCallAPI();
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.obj.toString()!=null) {
                if(tvData.getText().length()>1000){
                    tvData.setText(msg.obj.toString() + "\r\n");
                }else {
                    tvData.setText(tvData.getText() + msg.obj.toString() + "\r\n");
                }

                scroll2Bottom(scrBarcode, tvData);
                Utils.playSound(1);
            }
        }
    };
    public void reCallAPI()
    {
        Globals g = (Globals)getApplication();

        String req = g.apiUrl + "item/readall";

        try {
            itemList.clear();

            List<LocationItem> locationItems = new ArrayList<>();

            locationItems = new JsonTaskGetLocationItemList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req).get();

            Collections.sort(locationItems);

            if (locationItems != null) {

                for (LocationItem p : locationItems) {

                    AddItem newVM = new AddItem(p.id, 1, p.item_name, p.reg_date, p.barcode, false  );

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

    public void OnScanBarCode(View v){

        Log.e("Scan button clicked::", "button clicked");

        if(!isRunning){
            isRunning=true;
            String str= etTime.getText().toString();

            if(str==null || str.isEmpty()){
                new ScanThread( false, Integer.parseInt(etTime.getHint().toString())).start();
            }else{
                new ScanThread(false, Integer.parseInt(str)).start();
            }
        }
    }


    class   ScanThread  extends Thread{
        boolean isContinuous = false;
        int  time;
        public ScanThread(boolean isContinuous,int time){
            this.isContinuous=isContinuous;
            this.time=time;
        }

        public void run(){
            while (isRunning) {
                String data = null;
                byte[] temp = uhf.scanBarcodeToBytes();
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
                    msg.obj = "扫描失败";
                    handler.sendMessage(msg);
                }

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
    public void OnConnectDevice(View v)
    {
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

    public static void scroll2Bottom(final ScrollView scroll, final View inner) {
        Handler handler = new Handler();
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (scroll == null || inner == null) {
                    return;
                }
                // 内层高度超过外层
                int offset = inner.getMeasuredHeight()
                        - scroll.getMeasuredHeight();
                if (offset < 0) {
                    offset = 0;
                }
                scroll.scrollTo(0, offset);
            }
        });

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
    }
}
