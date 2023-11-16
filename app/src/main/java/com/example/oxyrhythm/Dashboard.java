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
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.UUID;



public class Dashboard extends AppCompatActivity {

    private OxyUser oxy_user;
    private DataBase oxy_user_saved_data;
    TextView greeting_oxy_user;
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
//                gatt.setCharacteristicNotification(characteristic, true);
                gatt.readCharacteristic(characteristic);
//                gatt.setCharacteristicNotification(characteristicsp02, true);
//                gatt.readCharacteristic(characteristicsp02);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                final byte[] data = characteristic.getValue();
                if (data != null && data.length > 0) {
                    int heartRate = data[0] & 0xFF; // Modify as needed for your specific data format
                    heartrate = new float[]{heartRate};

                    addHeartRateValue(heartRate);
                    // Update the chart
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateHeartRateChart();
                        }
                    });


                    Log.d("onChanged", "works in chara read");
                } else {
                    // Handle the case where the data is empty or not in the expected format.
                }
            } else {
                // Handle the case where the read was not successful.
            }
        }

        @Override
        public void onCharacteristicChanged(@NonNull BluetoothGatt gatt, @NonNull BluetoothGattCharacteristic characteristic, @NonNull byte[] value) {
            super.onCharacteristicChanged(gatt, characteristic, value);
            byte[] dataRec = characteristic.getValue();
            int heartRate = dataRec[0] & 0xFF; // Modify as needed for your specific data format
            Log.d("onChanged", "on changed: " + heartRate);
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putInt(key_data, heartRate);
            editor.apply();

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

    // Method to update the heart rate chart
    private void updateHeartRateChart() {
        LineChart lineChart = findViewById(R.id.lineChart);
        setLineChartData(lineChart, heartrate, "#FF0000");
        lineChart.invalidate(); // This line is added to force a redraw of the chart
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

        LineChart lineChart = findViewById(R.id.lineChart);
        LineChart lineChart2 = findViewById(R.id.lineChart2);

        heartrateWidget = findViewById(R.id.heartRateWid);
        healthmetrics = findViewById(R.id.healthmetricsclick);

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

        healthmetrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HealthMetrics();
            }
        });
        heartrateWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HeartMonitor();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        float[] dataforheartrate = heartrate;
        float[] dataforbloodoxygen = {100, 99, 95, 88, 85, 100};

        setLineChartData(lineChart, dataforheartrate, "#FF0000");
        setLineChartData(lineChart2, dataforbloodoxygen, "#0000FF");

        oxy_user_saved_data = new DataBase(Dashboard.this);
        greeting_oxy_user = findViewById(R.id.hello_OxyUser_label);
    }

    private void setLineChartData(LineChart chart, float[] data, String lineColor) {
        ArrayList<Entry> entries = new ArrayList<>();

        for (int i = 0; i < data.length; i++) {
            entries.add(new Entry(i, data[i]));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Label");
        dataSet.setColor(Color.parseColor(lineColor));
        dataSet.setDrawValues(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        chart.setData(new LineData(dataSets));
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.getXAxis().setEnabled(false);
        chart.getAxisRight().setEnabled(false);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setDrawGridLines(false);

        chart.invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();

        oxy_user = oxy_user_saved_data.getSavedOxyUser();

        if (oxy_user.OxyUserIsEmpty()) {
            Intent i = new Intent(this, GetStartedActivity.class);
            startActivity(i);
        } else {
            greeting_oxy_user.setText("Hello " + oxy_user.getFirstName() + "!");
        }
    }

    public void HeartMonitor() {
        Intent i = new Intent(this, HeartRate.class);
        startActivity(i);
    }

    public void HealthMetrics() {
        Intent i = new Intent(this, HealthMetrics.class);
        startActivity(i);
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
        if (UUID_HEART_RATE.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG);
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            bluetoothGatt.writeDescriptor(descriptor);
            Log.d("onChanged", "set Charac notif works");
        }
    }
}