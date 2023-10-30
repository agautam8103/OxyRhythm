package com.example.oxyrhythm;

import android.content.Context;
import android.content.SharedPreferences;

public class DataBase {
    private final SharedPreferences data_saver;

    public DataBase(Context context) {
        data_saver = context.getSharedPreferences("Saved_Profile", Context.MODE_PRIVATE);
    }

    public void saveFirstName(String f_name) {
        SharedPreferences.Editor editor = data_saver.edit();
        editor.putString("First_Name", f_name);
        editor.apply();
    }

    public String getFirstName(){
        return data_saver.getString("First_Name",null);
    }

    public void saveLastName(String l_name) {
        SharedPreferences.Editor editor = data_saver.edit();
        editor.putString("Last_Name", l_name);
        editor.apply();
    }

    public void saveSex(String sex) {
        SharedPreferences.Editor editor = data_saver.edit();
        editor.putString("Last_Name", sex);
        editor.apply();
    }
}
