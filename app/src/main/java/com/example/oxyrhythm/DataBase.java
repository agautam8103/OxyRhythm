//Class to store data on local Android device using SharedPreferences

package com.example.oxyrhythm;

import android.content.Context;
import android.content.SharedPreferences;

public class DataBase {
    private final SharedPreferences data_saver;
    private final OxyUser saved_oxy_user = new OxyUser();

    public DataBase(Context context) {
        data_saver = context.getSharedPreferences("Saved_Profile", Context.MODE_PRIVATE);
    }

    public void saveFirstName(String f_name) {
        SharedPreferences.Editor editor = data_saver.edit();
        editor.putString("First_Name", f_name);
        editor.apply();
    }

    public void saveLastName(String l_name) {
        SharedPreferences.Editor editor = data_saver.edit();
        editor.putString("Last_Name", l_name);
        editor.apply();
    }

    public void saveSex(String sex) {
        SharedPreferences.Editor editor = data_saver.edit();
        editor.putString("Sex", sex);
        editor.apply();
    }

    public void saveHeightUnit(String hu) {
        SharedPreferences.Editor editor = data_saver.edit();
        editor.putString("Height_Unit", hu);
        editor.apply();
    }

    public void saveWeightUnit(String wu) {
        SharedPreferences.Editor editor = data_saver.edit();
        editor.putString("Weight_Unit", wu);
        editor.apply();
    }

    public void saveBirthYear(int birth_year) {
        SharedPreferences.Editor editor = data_saver.edit();
        editor.putInt("Birth_Year", birth_year);
        editor.apply();
    }

    public void saveWeight(float weight) {
        SharedPreferences.Editor editor = data_saver.edit();
        editor.putFloat("Weight", weight);
        editor.apply();
    }

    public void saveHeight(float height) {
        SharedPreferences.Editor editor = data_saver.edit();
        editor.putFloat("Height", height);
        editor.apply();
    }
    
    public OxyUser getSavedOxyUser() {
        saved_oxy_user.SetAttributes(data_saver.getString("First_Name", null),
                                 data_saver.getString("Last_Name", null),
                                 data_saver.getString("Sex", "Male"),
                                 data_saver.getFloat("Height", 0),
                                 data_saver.getFloat("Weight", 0),
                                 data_saver.getInt("Birth_Year", 0),
                                 data_saver.getString("Height_Unit", "cm"),
                                 data_saver.getString("Weight_Unit", "kg"));
        
        return saved_oxy_user;
    }
}
