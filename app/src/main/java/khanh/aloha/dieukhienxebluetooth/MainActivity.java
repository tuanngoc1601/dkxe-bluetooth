package khanh.aloha.dieukhienxebluetooth;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private String DEVICE_ADDRESS = "";
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private ProgressDialog progress;
    private Button btnTien, btnLui, btnTrai, btnPhai, btnConnect;
    private ImageView imgStatus;
    private ListView lvDevice;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private ArrayAdapter arrayAdapter;
    private ArrayList list;
    String command;
    boolean found = false;

    private static final int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth Not Supported", Toast.LENGTH_SHORT).show();
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothAdapter == null) {
                    Toast.makeText(getApplicationContext(), "Bluetooth Not Supported", Toast.LENGTH_SHORT).show();
                } else {
                    scanDevice();
                }
            }
        });

        progress = new ProgressDialog(MainActivity.this);
        progress.setMessage("Connecting...");
        progress.setIndeterminate(true);
        progress.setCancelable(false);


        lvDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                progress.show();
                DEVICE_ADDRESS = list.get(position).toString().substring(0, 17);
                scanDevice();
                //Toast.makeText(getApplicationContext(), DEVICE_ADDRESS, Toast.LENGTH_SHORT).show();
                if(found) {
                    BTconnect();
                }

            }
        });

        btnTien.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) //MotionEvent.ACTION_DOWN is when you hold a button down
                {
                    command = "F";

                    try
                    {
                        outputStream.write(command.getBytes()); //transmits the value of command to the bluetooth module
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) //MotionEvent.ACTION_UP is when you release a button
                {
                    command = "S";
                    try
                    {
                        outputStream.write(command.getBytes());
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }

                }

                return true;
            }

        });

        btnLui.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) //MotionEvent.ACTION_DOWN is when you hold a button down
                {
                    command = "B";

                    try
                    {
                        outputStream.write(command.getBytes()); //transmits the value of command to the bluetooth module
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) //MotionEvent.ACTION_UP is when you release a button
                {
                    command = "S";
                    try
                    {
                        outputStream.write(command.getBytes());
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }

                }

                return true;
            }

        });

        btnTrai.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) //MotionEvent.ACTION_DOWN is when you hold a button down
                {
                    command = "L";

                    try
                    {
                        outputStream.write(command.getBytes()); //transmits the value of command to the bluetooth module
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) //MotionEvent.ACTION_UP is when you release a button
                {
                    command = "S";
                    try
                    {
                        outputStream.write(command.getBytes());
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }

                }

                return true;
            }

        });

        btnPhai.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) //MotionEvent.ACTION_DOWN is when you hold a button down
                {
                    command = "R";

                    try
                    {
                        outputStream.write(command.getBytes()); //transmits the value of command to the bluetooth module
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) //MotionEvent.ACTION_UP is when you release a button
                {
                    command = "S";
                    try
                    {
                        outputStream.write(command.getBytes());
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }

                }

                return true;
            }

        });
    }

    private void scanDevice() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        list = new ArrayList();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice iterator : pairedDevices) {
                String deviceName = iterator.getName();
                String deviceHardwareAddress = iterator.getAddress(); // MAC address
                list.add(deviceHardwareAddress + "\n" + deviceName);
                if(iterator.getAddress().equals(DEVICE_ADDRESS))
                {
                    device = iterator;
                    found = true;
                    break;
                }

            }
            arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, list);
            lvDevice.setAdapter(arrayAdapter);
        } else {
            Toast.makeText(getApplicationContext(), "Hãy ghép đôi thiết bị với nhau", Toast.LENGTH_SHORT).show();
        }

    }

    private void init() {
        btnTien = findViewById(R.id.btnTien);
        btnLui = findViewById(R.id.btnLui);
        btnTrai = findViewById(R.id.btnTrai);
        btnPhai = findViewById(R.id.btnPhai);
        btnConnect = findViewById(R.id.btnConnect);
        lvDevice = findViewById(R.id.lvDevice);
        imgStatus = findViewById(R.id.imgStatus);
    }

    public void BTconnect()
    {
        boolean connected = true;

        try
        {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID); //Creates a socket to handle the outgoing connection
            socket.connect();
            if(socket.isConnected()) {

                Toast.makeText(getApplicationContext(),
                        "Kết nối thành công", Toast.LENGTH_LONG).show();
                imgStatus.setImageResource(R.drawable.green_circle);
                progress.dismiss();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Chưa kết nối được", Toast.LENGTH_LONG).show();
                imgStatus.setImageResource(R.drawable.red_circle);
                progress.dismiss();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
            connected = false;
            Toast.makeText(getApplicationContext(),
                    "Kết nối thất bại", Toast.LENGTH_LONG).show();
            imgStatus.setImageResource(R.drawable.red_circle);
            progress.dismiss();
        }

        if(connected)
        {
            try
            {
                outputStream = socket.getOutputStream(); //gets the output stream of the socket
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

    }


}
