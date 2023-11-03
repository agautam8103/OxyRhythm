package com.example.oxyrhythm;

public class OxyUser {
    private String firstName, lastName, sex;
    private float height, weight;
    int birthYear;

    public OxyUser(String firstName, String lastName, String sex, float height, float weight, int birthYear) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.height = height;
        this.weight = weight;
        this.birthYear = birthYear;
    }

    public OxyUser() {
        this.firstName = null;
        this.lastName = null;
        this.sex = null;
        this.height = 0;
        this.weight = 0;
        this.birthYear = 0;
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
}
