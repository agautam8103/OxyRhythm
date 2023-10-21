package com.example.oxyrhythm;

public class OxyUser {
    private String firstName, lastName, sex;
    private double height, weight;
    int birthYear, birthDay, birthMonth;

    public OxyUser(String firstName, String lastName, String sex, double height, double weight, int birthYear, int birthDay, int birthMonth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.height = height;
        this.weight = weight;
        this.birthYear = birthYear;
        this.birthDay = birthDay;
        this.birthMonth = birthMonth;
    }

    public OxyUser() {
        this.firstName = null;
        this.lastName = null;
        this.sex = null;
        this.height = 0.0;
        this.weight = 0.0;
        this.birthYear = 0;
        this.birthDay = 0;
        this.birthMonth = 0;
    }

    public String getFirstName() {return firstName;}

    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() {return lastName;}

    public void setLastName(String lastName) {this.lastName = lastName;}

    public String getSex() {return sex;}

    public void setSex(String sex) {this.sex = sex;}

    public double getHeight() {return height;}

    public void setHeight(double height) {this.height = height;}

    public double getWeight() {return weight;}

    public void setWeight(double weight) {this.weight = weight;}

    public int getBirthYear() {return birthYear;}

    public void setBirthYear(int birthYear) {this.birthYear = birthYear;}

    public int getBirthDay() {return birthDay;}

    public void setBirthDay(int birthDay) {this.birthDay = birthDay;}

    public int getBirthMonth() {return birthMonth;}

    public void setBirthMonth(int birthMonth) {this.birthMonth = birthMonth;}
}
