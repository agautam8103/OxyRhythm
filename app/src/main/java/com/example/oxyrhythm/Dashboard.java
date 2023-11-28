package com.example.oxyrhythm;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import java.util.UUID;

public class Dashboard extends AppCompatActivity {

    public static HW_Database_SQLite database;

    private DataBase oxy_user_saved_data;
    TextView greeting_oxy_user, dash_mesg, heart_rate_label, blood_oxygen_level_label, body_temperature_label,
            Click_to_see_more_label1, Click_to_see_more_label2, Click_to_see_more_label3;
    Button heart_rate_BTN, blood_oxy_BTN, body_temp_BTN;
    ImageView heart_pic, temp_pic, blood_oxy_pic, logo;
    private Toolbar toolbar;

    private OxyUser oxy_user;
    private TextView livedata;
    private TextView livedataspo2;
    private TextView livedatabody;

    private BluetoothGatt bluetoothGatt;
    private BluetoothAdapter bluetoothAdapter;
    private boolean bluetoothConnected = false;
    final String UUID_HEART_RATE = "19B10001-E8F2-537E-4F6C-D104768A1214";
    public static final String SHARED_PREF = "Sharedpref";
    public static final String key_data = "heartrate";
    static final UUID CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    float[] heartrate = {};
    LinearLayout heartrateWidget, healthmetrics;

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                bluetoothConnected = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Dashboard.this, "Connected to Arduino", Toast.LENGTH_SHORT).show();
                    }
                });

                if (ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                bluetoothConnected = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Dashboard.this, "Disconnected from Arduino", Toast.LENGTH_SHORT).show();
                    }
                });

                // Handle disconnection
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                BluetoothGattService service = gatt.getService(UUID.fromString("19B10000-E8F2-537E-4F6C-D104768A1214"));
                BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString("19B10001-E8F2-537E-4F6C-D104768A1214"));
                BluetoothGattCharacteristic characteristicsp02 = service.getCharacteristic(UUID.fromString("19B10002-E8F2-537E-4F6C-D104768A1214"));
                if (ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                gatt.setCharacteristicNotification(characteristic, true);
                gatt.readCharacteristic(characteristic);
//                gatt.setCharacteristicNotification(characteristicsp02, true);
//                gatt.readCharacteristic(characteristicsp02);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt,characteristic,status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                final byte[] data = characteristic.getValue();
                if (data != null && data.length > 0) {
//                    int heartRate = data[0] & 0xFF; // Modify as needed for your specific data format
//                    heartrate = new float[]{heartRate};

//                    addHeartRateValue(heartRate);
                    // Update the chart
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            updateHeartRateChart();
//                        }
//                    });


                    Log.d("onChanged", "works in chara read");
                } else {
                    // Handle the case where the data is empty or not in the expected format.
                }
            } else {
                // Handle the case where the read was not successful.
            }
            setCharacteristicNotification(characteristic,true);
        }

        @Override
        public void onCharacteristicChanged(@NonNull BluetoothGatt gatt, @NonNull BluetoothGattCharacteristic characteristic, @NonNull byte[] value) {
            super.onCharacteristicChanged(gatt, characteristic);
            byte[] dataRec = characteristic.getValue();

            String jello = "";
            String suv;

            // Convert the byte array to a hexadecimal string
            for (int j = 0; j < dataRec.length; j++) {
                suv = String.format("%02x", dataRec[j]);
                jello = jello + suv;
            }

            // Convert the hexadecimal string to ASCII
            StringBuilder output = new StringBuilder();
            for (int i = 0; i < jello.length(); i += 2) {
                String str = jello.substring(i, i + 2);
                int decimal = Integer.parseInt(str, 16);
                output.append((char) decimal);
            }

            // Split the string and update the UI
            String[] anna = output.toString().split(" ");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    livedata.setText("BPM: " + anna[0]);
                    livedataspo2.setText("SPO2: " + anna[1] + "%");
                    livedatabody.setText("TMP: " + anna[2]+"°C");

                    // Initialize the database if needed
                    if (database == null) {
                        database = new HW_Database_SQLite(Dashboard.this);
                    }

                    // Store the heart rate data in SQLite database
                    int heartRateValue = Integer.parseInt(anna[0]);
                    String bloodOxygenValue = anna[1];
                    String bodyTemperatureValue = anna[2];

                    if (database != null) {
                        database.addHWData(heartRateValue, bloodOxygenValue, bodyTemperatureValue);
                    } else {
                        Log.e("Dashboard", "Database is null");
                    }
                }
            });
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            Log.d("onChanged", "The descriptor is successful");

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothGatt != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            bluetoothGatt.disconnect();
            bluetoothGatt.close();
        }
    }

    private void requestLocationPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    public void connectToBluetooth(View view) {
        if (!bluetoothConnected) {
            if (bluetoothGatt != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                bluetoothGatt.connect();
            } else {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                    bluetoothAdapter = bluetoothManager.getAdapter();

                    if (bluetoothAdapter == null) {
                        Toast.makeText(this, "Bluetooth not supported on this device", Toast.LENGTH_LONG).show();
                    } else {
                        BluetoothDevice arduinoDevice = bluetoothAdapter.getRemoteDevice("F9:B9:F4:20:0C:55");
                        bluetoothGatt = arduinoDevice.connectGatt(this, false, gattCallback);
                    }
                } else {
                    requestLocationPermission();
                }
            }
        } else {
            // You are already connected, you can handle this as needed (e.g., disconnect).
        }
    }
    private void addHeartRateValue(int newHeartRate) {
        // Create a new array with increased size
        float[] newHeartrateArray = new float[heartrate.length + 1];

        // Copy the existing values to the new array
        System.arraycopy(heartrate, 0, newHeartrateArray, 0, heartrate.length);

        // Add the new heart rate value
        newHeartrateArray[heartrate.length] = newHeartRate;

        // Update the heartrate array reference
        heartrate = newHeartrateArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (bluetoothAdapter != null) {
                BluetoothDevice arduinoDevice = bluetoothAdapter.getRemoteDevice("F9:B9:F4:20:0C:55");
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                bluetoothGatt = arduinoDevice.connectGatt(this, false, gattCallback);
            } else {
                Toast.makeText(this, "Bluetooth not supported on this device", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Location permission is required for Bluetooth functionality", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        HeartBeatChannel.setAlarm(this);

        //edits
        livedata = findViewById(R.id.livedata);
        livedataspo2 = findViewById(R.id.livedataspo2);
        livedatabody = findViewById(R.id.livedatabody);


        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            bluetoothAdapter = bluetoothManager.getAdapter();

            if (bluetoothAdapter == null) {
                Toast.makeText(this, "Bluetooth not supported on this device", Toast.LENGTH_LONG).show();
            } else {
                BluetoothDevice arduinoDevice = bluetoothAdapter.getRemoteDevice("F9:B9:F4:20:0C:55");
                bluetoothGatt = arduinoDevice.connectGatt(this, false, gattCallback);
            }
        } else {
            requestLocationPermission();
        }

        oxy_user_saved_data = new DataBase(Dashboard.this);
        greeting_oxy_user = findViewById(R.id.hello_OxyUser_label);

        setSupportActionBar(findViewById(R.id.toolbar2));

        heart_rate_BTN = findViewById(R.id.heart_rate_btn);
        heart_rate_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {heartRate();}});

        blood_oxy_BTN = findViewById(R.id.blood_oxygen_btn);
        blood_oxy_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {bloodOxygenLevel();}});

        body_temp_BTN = findViewById(R.id.body_temperature_btn);
        body_temp_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {temperature();}});

        oxy_user_saved_data = new DataBase(Dashboard.this);
        greeting_oxy_user = findViewById(R.id.hello_OxyUser_label);
        dash_mesg = findViewById(R.id.dash_mesg);
        heart_rate_label = findViewById(R.id.heart_rate_label);
        blood_oxygen_level_label = findViewById(R.id.blood_oxygen_level_label);
        body_temperature_label = findViewById(R.id.body_temperature_label);
        Click_to_see_more_label1 = findViewById(R.id.Click_to_see_more_label1);
        Click_to_see_more_label2 = findViewById(R.id.Click_to_see_more_label2);
        Click_to_see_more_label3 = findViewById(R.id.Click_to_see_more_label3);
        heart_pic = findViewById(R.id.Heart_Pic);
        temp_pic = findViewById(R.id.Temp_pic);
        blood_oxy_pic = findViewById(R.id.blood_oxygen_pic);
        logo = findViewById(R.id.logo);
        toolbar = findViewById(R.id.toolbar2);

    }

    @Override
    protected void onResume() {
        super.onResume();

        OxyUser oxy_user = oxy_user_saved_data.getSavedOxyUser();

        if (oxy_user.OxyUserIsEmpty()) { //Go to register user page if there's no user data
            heart_rate_BTN.setVisibility(View.INVISIBLE);
            blood_oxy_BTN.setVisibility(View.INVISIBLE);
            body_temp_BTN.setVisibility(View.INVISIBLE);
            heart_pic.setVisibility(View.INVISIBLE);
            temp_pic.setVisibility(View.INVISIBLE);
            blood_oxy_pic.setVisibility(View.INVISIBLE);
            logo.setVisibility(View.INVISIBLE);
            toolbar.setVisibility(View.GONE);

            Intent i = new Intent(this, GetStartedActivity.class);
            startActivity(i);
        } else { //Set everything visible on this page if a user account exists
            greeting_oxy_user.setText("Hello, " + oxy_user.getFirstName() + "!");
            dash_mesg.setText("Time to check up on your health routine");
            heart_rate_label.setText("Heart Rate");
            blood_oxygen_level_label.setText("Blood Oxygen Level");
            body_temperature_label.setText("Body Temperature");
            Click_to_see_more_label1.setText("Click for more info.");
            Click_to_see_more_label2.setText("Click for more info.");
            Click_to_see_more_label3.setText("Click for more info.");
            heart_rate_BTN.setVisibility(View.VISIBLE);
            blood_oxy_BTN.setVisibility(View.VISIBLE);
            body_temp_BTN.setVisibility(View.VISIBLE);
            heart_pic.setVisibility(View.VISIBLE);
            temp_pic.setVisibility(View.VISIBLE);
            blood_oxy_pic.setVisibility(View.VISIBLE);
            logo.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() { //Stay in this page if back phone button is pressed at any page
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
        finish();
    }

    private void heartRate() {
        Intent i = new Intent(this, HeartRate.class);
        startActivity(i);
    }

    private void bloodOxygenLevel() {
        Intent i = new Intent(this, BloodOxygenLevel.class);
        startActivity(i);
    }

    private void temperature() {
        Intent i = new Intent(this, Temperature.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menufordashboard, menu);
        getMenuInflater().inflate(R.menu.health_metrics_menu, menu);
        getMenuInflater().inflate(R.menu.help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menufordashboard) {
            Intent i = new Intent(this, RegisterUserActivity.class);
            startActivity(i);
            return true;
        }

        else if (item.getItemId() == R.id.health_metric) {
            Intent i = new Intent(this, HealthMetrics.class);
            startActivity(i);
            return true;
        }

        else if (item.getItemId() == R.id.help) {
            Intent i = new Intent(this, Help.class);
            startActivity(i);
            return true;
        }

        else return super.onOptionsItemSelected(item);
    }

    public void readCharacteristic(BluetoothGattCharacteristic characteristic){
        if(bluetoothGatt == null){
            return;
        }
        Log.d("onChanged", "read works");
        bluetoothGatt.readCharacteristic(characteristic);
    }
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,boolean enabled) {
        if (bluetoothGatt == null) {
            Log.d("onChanged", "BluetoothGatt not initialized");
            return;
        }
        bluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        // This is specific to Heart Rate Measurement.

        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        bluetoothGatt.writeDescriptor(descriptor);
        Log.d("onChanged", "set Charac notif works");

    }
}
