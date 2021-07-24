package tw.mingtsay.app.bluetoothconnector;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


public class ConnectActivity extends Activity {
    private final static String TAG = ConnectActivity.class.getSimpleName();
    private final UUID serialPortUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private String name, address,mDeviceName,mDeviceAddress;
    //private TextView mConnectionState;
    //private TextView mDataField;
    private Button buttonConnect, buttonDisconnect,sendWeather, TurnBack;
    //private EditText textInput;
    //private TextView textContent;
    private BLE mBluetoothLeService;
    //private BluetoothAdapter bluetoothAdapter;
    //private BluetoothSocket socket;
    //private InputStream inputStream;
    private OutputStream outputStream;


    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BLE.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_connect);


        final String deviceName = getIntent().getStringExtra("DeviceName");
        final String deviceAddress = getIntent().getStringExtra("DeviceAddress");

        mDeviceName = getIntent().getStringExtra("DeviceName");
        mDeviceAddress = getIntent().getStringExtra("DeviceAddress");


        name = deviceName != null ? deviceName : "裝置名稱未顯示";
        address = deviceAddress;


        setTitle(String.format("%s (%s)", address, name));

        buttonConnect = findViewById(R.id.buttonConnect);
        buttonDisconnect = findViewById(R.id.buttonDisconnect);
        sendWeather = findViewById(R.id.sendWeather);
        //textInput = findViewById(R.id.textInput);
        //textContent = findViewById(R.id.textContent);
        //TurnBack = findViewById(R.id.TurnBack);

        //buttonDisconnect.setEnabled(false);

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(mReceiver, filter);

        //buttonDisconnect.setEnabled(false);
        //buttonConnect.setEnabled(true);
        //sendWeather.setEnabled(false);


        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //connect();

                Intent it = new Intent(ConnectActivity.this, BLE.class);
                bindService(it, mServiceConnection, BIND_AUTO_CREATE);


                //sendWeather();
/*
                Intent intent = new Intent();
                intent.setClass(ConnectActivity.this, WeatherActivity.class);
                startActivity(intent);
*/

            }
        });

        buttonDisconnect.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(View view) {

                mBluetoothLeService.disconnect();
                unbindService(mServiceConnection);
            }
        });


        sendWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBluetoothLeService.sendWeather();

                Intent intent = new Intent();
                intent.setClass(ConnectActivity.this, WeatherActivity.class);
                startActivity(intent);



            }
        });
/*
        textInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                send();
                return false;
            }
        });*/


    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();


            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                buttonDisconnect.setEnabled(true);
                buttonConnect.setEnabled(false);
                //sendWeather.setEnabled(true);

            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                buttonDisconnect.setEnabled(false);
                buttonConnect.setEnabled(true);
                //sendWeather.setEnabled(false);


            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unbindService(mServiceConnection);
        //mBluetoothLeService = null;
    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    protected void onStop(){
        super.onStop();
    }









/*
    private void connect() {
        final BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);

        try {
            socket = device.createRfcommSocketToServiceRecord(serialPortUUID);
            socket.connect();
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
/*
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void disconnect() {
        mBluetoothLeService.disconnect();
        unbindService(mServiceConnection);
        unregisterReceiver(mReceiver);
    }
/*
        if (mBluetoothLeService == null) return;

        try {

            socket.close();
            socket = null;
            inputStream = null;
            outputStream = null;

            unbindService(mServiceConnection);
            unregisterReceiver(mReceiver);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/
/*
    private void sendWeather() {
        //if (outputStream == null) return;

        try {
            outputStream.write("weather".getBytes());
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
/*
    private void read() {
        if (inputStream == null) return;

        try {
            if (inputStream.available() <= 0) return;
            byte[] buffer = new byte[256];
            textContent.append(new String(buffer, 0, inputStream.read(buffer)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

*/
}