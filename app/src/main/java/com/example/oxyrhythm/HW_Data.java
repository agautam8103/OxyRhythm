package com.example.oxyrhythm;

public class HW_Data {
    private int heart_rate, blood_oxygen;
    private float temperature;
    
    public HW_Data(int heart_rate, int blood_oxygen, float temperature) {
        this.heart_rate = heart_rate;
        this.blood_oxygen = blood_oxygen;
        this.temperature = temperature;
    }

    public int getHeartRate() {return heart_rate;}

    public void setHeartRate(int heart_rate) {this.heart_rate = heart_rate;}

    public int getBloodOxygen() {return blood_oxygen;}

    public void setBloodOxygen(int blood_oxygen) {this.blood_oxygen = blood_oxygen;}

    public float getTemperature() {return temperature;}

    public void setTemperature(float temperature) {this.temperature = temperature;}
}
