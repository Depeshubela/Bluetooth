package tw.mingtsay.app.bluetoothconnector;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.bluetooth.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.OutputStream;


public class WeatherActivity extends AppCompatActivity {
    private final static String TAG = WeatherActivity.class.getSimpleName();
    private BluetoothDevice device;
    public InputStream inputStream;
    public TextView Locale;
    public Button TurnBack,buttonTry;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private BLE mBluetoothLeService;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_weather);

        buttonTry = findViewById(R.id.buttonTry);
        TurnBack =  findViewById(R.id.TurnBack);
        //Locale = findViewById(R.id.Locale);



        //read();

        buttonTry.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mBluetoothLeService.sendWeather();
            }
        });



        TurnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(WeatherActivity.this, ConnectActivity.class);
                startActivity(intent);
            }
        });




    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unbindService(mServiceConnection);
        //mBluetoothLeService = null;
    }


    @Override
    protected void onPause() {
        super.onPause();
        //readerStop = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //reader.start();
    }
    @Override
    protected void onStop(){
        super.onStop();

    }


/*
    private void read() {
        byte[] buffer = new byte[256];
        int bytes;

        try {

            // Get the BluetoothSocket input and output streams

            inputStream = socket.getInputStream();


            DataInputStream mmInStream = new DataInputStream(inputStream);

            // here you can use the Input Stream to take the string from the client  whoever is connecting
            //similarly use the output stream to send the data to the client

            // Read from the InputStream
            bytes = mmInStream.read(buffer);
            String readMessage = new String(buffer, 0, bytes);
            // Send the obtained bytes to the UI Activity

            Locale.setText(readMessage);
        } catch (Exception e) {
            //catch your exception here
        }
        // }

    }
        //if (inputStream == null) return;

        /*try {
            if (inputStream.available() <= 0) return;
            byte[] buffer = new byte[256];
            Locale.append(new String(buffer, 0, inputStream.read(buffer)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


}