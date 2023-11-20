package com.example.oxyrhythm;

public class OxyUser {
    private String firstName, lastName, sex, height_unit, weight_unit;
    private float height, weight;
    int birthYear;

    public void SetAttributes(String firstName, String lastName, String sex, float height, float weight, int birthYear, String height_unit, String weight_unit) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.height = height;
        this.weight = weight;
        this.birthYear = birthYear;
        this.height_unit = height_unit;
        this.weight_unit = weight_unit;
    }

    public boolean OxyUserIsEmpty() {
        if (firstName == null || lastName == null || sex == null || birthYear == 0 || height == 0 || weight == 0) return true;
        else return false;
    }

    public String getFirstName() {return firstName;}

    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() {return lastName;}

    public void setLastName(String lastName) {this.lastName = lastName;}

    public String getSex() {return sex;}

    public void setSex(String sex) {this.sex = sex;}

    public float getHeight() {return height;}

    public void setHeight(float height) {this.height = height;}

    public float getWeight() {return weight;}

    public void setWeight(float weight) {this.weight = weight;}

    public int getBirthYear() {return birthYear;}

    public void setBirthYear(int birthYear) {this.birthYear = birthYear;}

    public String getHeightUnit() {return height_unit;}

    public void setHeightUnit(String height_unit) {this.height_unit = height_unit;}

    public String getWeightUnit() {return weight_unit;}

    public void setWeightUnit(String weight_unit) {this.weight_unit = weight_unit;}

}