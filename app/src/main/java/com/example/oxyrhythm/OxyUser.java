package com.example.oxyrhythm;

public class OxyUser {
    private String firstName;
    private String lastName;
    private String sex;
    private double height;
    private double weight;

    public OxyUser(String firstName, String lastName, String sex, double height, double weight) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.height = height;
        this.weight = weight;
    }

    public OxyUser() {
        this.firstName = null;
        this.lastName = null;
        this.sex = null;
        this.height = 0.0;
        this.weight = 0.0;
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
}
