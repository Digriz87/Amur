package com.app.amur.amur.adapters;

/**
 * Created by Digriz on 06.01.2018.
 */

public class UserInformation  {

    private String DateOfBirthday;
    int yearsOld; // Для возвраста
    String name; // Для имени
    int lengthOfMan;  // Для роста
    int weight; // Для веса
    String colorOfHair; // Для цвета волос
    String bodyForm; // Для телосложения
    String national; // Для национальности
    String sex; // Для пола (м\ж)
    String lookingFor; // Виды отношений из Спиннера
    String positionOnMap;
    String orientationOfPeople; // Ориентация человека
    String aboutYourSelf; // Рассказ о себе
    String userId;

    public UserInformation(){

    }

    public UserInformation(String DateOfBirthday, int yearsOld, String name, int lengthOfMan, int weight, String colorOfHair, String bodyForm, String national, String sex, String lookingFor, String positionOnMap, String orientationOfPeople, String aboutYourSelf, String userId) {
        this.DateOfBirthday = DateOfBirthday;
        this.yearsOld = yearsOld;
        this.name = name;
        this.lengthOfMan = lengthOfMan;
        this.weight = weight;
        this.colorOfHair = colorOfHair;
        this.bodyForm = bodyForm;
        this.national = national;
        this.sex = sex;
        this.lookingFor = lookingFor;
        this.positionOnMap = positionOnMap;
        this.orientationOfPeople = orientationOfPeople;
        this.aboutYourSelf = aboutYourSelf;
        this.userId = userId;

    }

    public String getDateOfBirthday() {
        return DateOfBirthday;
    }

    public void setDateOfBirthday(String DateOfBirthday) {
        this.DateOfBirthday = DateOfBirthday;
    }
    public Integer getYearsOld() {
        return yearsOld;
    }

    public void setYearsOld(int yearsOld) {
        this.yearsOld = yearsOld;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Integer getLengthOfMan() {
        return lengthOfMan;
    }

    public void setLengthOfMan(int lengthOfMan) {
        this.lengthOfMan = lengthOfMan;
    }
    public Integer getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
    public String getColorOfHair() {
        return colorOfHair;
    }

    public void setColorOfHair(String colorOfHair) {
        this.colorOfHair = colorOfHair;
    }
    public String getBodyForm() {
        return bodyForm;
    }

    public void setBodyForm(String bodyForm) {
        this.bodyForm = bodyForm;
    }
    public String getNational() {
        return national;
    }

    public void setNational(String national) {
        this.national = national;
    }
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(String lookingFor) {
        this.lookingFor = lookingFor;
    }
    public String getPositionOnMap() {
        return positionOnMap;
    }

    public void setPositionOnMap(String positionOnMap) {
        this.positionOnMap = positionOnMap;
    }

    public String getOrientationOfPeople() {
        return orientationOfPeople;
    }

    public void setOrientationOfPeople(String orientationOfPeople) {
        this.orientationOfPeople = orientationOfPeople;
    }

    public String getAboutYourSelf() {
        return aboutYourSelf;
    }

    public void setAboutYourSelf(String aboutYourSelf) {
        this.aboutYourSelf = aboutYourSelf;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
