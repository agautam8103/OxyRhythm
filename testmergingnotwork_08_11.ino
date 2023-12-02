//LIBRARY WAS CHANGED, ADDED NEW LINES IN THE CODE 




#ifdef ARDUINO_ARCH_NRF52840  // Check if using Mbed Nano core

#include "Arduino.h"
#define SerialUSB Serial

#else  // Use standard Arduino core

#define SerialUSB Serial

#endif

#include <Wire.h>
#include <Adafruit_MLX90614.h>
#include "MAX30105.h"
#include <ArduinoBLE.h>
#include "heartRate.h"
#include "spo2_algorithm.h"

// Bluetooth
const char* sensorUUID = "19B10000-E8F2-537E-4F6C-D104768A1214";
const char* heartrateUUID = "19B10001-E8F2-537E-4F6C-D104768A1214";
BLEService sensorService(sensorUUID); // Define a service for the sensor data
BLECharacteristic sensorDataCharacteristic(heartrateUUID, BLERead | BLENotify, "HeartRate");

MAX30105 particleSensor;
Adafruit_MLX90614 mlx = Adafruit_MLX90614();

const byte RATE_SIZE = 10; // Increase this for more averaging. 10 is good.
byte rates[RATE_SIZE]; // Array of heart rates
byte rateSpot = 0;
long lastBeat = 0; // Time at which the last beat occurred
int spO2 = 0; // Initial SpO2 value

float beatsPerMinute;
int beatAvg;
int lastSentBeatAvg = -1; // Initialize with a value that won't match the first reading
float bodytemp;

void setup() {
#ifdef ARDUINO_ARCH_NRF52840
    SerialUSB.begin(115200);
#else
    SerialUSB.begin(9600);
#endif

    // Start communication with the BLE module
    if (!BLE.begin()) {
        while (1);
    }

    //temperature
    mlx.begin();

    //Wire.setClock(500000);
    delay(100);
    // Initialize sensor
    if (!particleSensor.begin(Wire, I2C_SPEED_FAST)) {
        SerialUSB.println("MAX30105 was not found. Please check wiring/power. ");
        while (1);
    }

    SerialUSB.print("Emissivity = "); SerialUSB.println(mlx.readEmissivity());
    SerialUSB.println("================================================");

    // Set the local name for the BLE peripheral
    BLE.setLocalName("OxyRhythm");

    // Advertise the service and characteristics
    BLE.setAdvertisedService(sensorService);
    sensorService.addCharacteristic(sensorDataCharacteristic);
    BLE.addService(sensorService);

    particleSensor.setup();

    BLE.advertise();
    SerialUSB.println("Bluetooth connected");
}

void loop() {
    BLEDevice central = BLE.central();
    long irValue = particleSensor.getIR();
    long redValue = particleSensor.getRed(); // Red LED sensor reading for SpO2

    //bodytemp = mlx.readObjectTempC();

    if (checkForBeat(irValue) == true && irValue >= 50000) {
        // We sensed a beat!
        long delta = millis() - lastBeat;
        lastBeat = millis();

        beatsPerMinute = 60 / (delta / 1000.0);

        // Calculate SpO2
        float ratio = redValue / irValue;
        spO2 = 100 - 3.33 * ratio;

        if (beatsPerMinute < 255 && beatsPerMinute > 20) {
            rates[rateSpot++] = (byte)beatsPerMinute; // Store this reading in the array
            rateSpot %= RATE_SIZE; // Wrap the variable

            // Take the average of readings
            beatAvg = 0;
            for (byte x = 0; x < RATE_SIZE; x++)
                beatAvg += rates[x];
            beatAvg /= RATE_SIZE;

            // Ensure the calculated BPM and SpO2 are within reasonable ranges
            if (beatAvg > 20 && beatAvg < 150 && spO2 >= 70 && spO2 <= 100) {
                if (central) {
                    String sensorDataString =  String(beatAvg) + " " +
                                             String(spO2) + " " +
                                             String((int)mlx.readObjectTempC());

                    // Convert the string to a char array for BLE
                    char sensorDataCharArray[50]; // Adjust the array size as needed
                    sensorDataString.toCharArray(sensorDataCharArray, sensorDataString.length() + 1);

                    // Send the concatenated string through the characteristic
                    sensorDataCharacteristic.writeValue((char*)sensorDataCharArray, sensorDataString.length() + 1);
                    //lastSentBeatAvg = beatAvg;
                }
            }
        }
    }

    SerialUSB.print("IR=");
    SerialUSB.print(irValue);
    SerialUSB.print(", Red=");
    SerialUSB.print(redValue);
    SerialUSB.print(", BPM=");
    SerialUSB.print(beatsPerMinute);
    SerialUSB.print(", Avg BPM=");
    SerialUSB.print(beatAvg);
    SerialUSB.print(", SpO2=");
    SerialUSB.print(spO2);
    SerialUSB.print("% ");
    SerialUSB.print(", body temp= ");
    SerialUSB.print(mlx.readObjectTempC());
    SerialUSB.print("*C ");

    if (irValue < 50000)
        SerialUSB.print(" No finger");

    SerialUSB.println();
}
