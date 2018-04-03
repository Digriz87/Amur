package com.app.amur.amur;



public class User {
    public String UserId;
    public String UrlOfPhoto;
    public String StatusUser;
    public String NameOfUser;



    public User(String userId, String urlOfPhoto, String statusUser, String nameOfUser) {
        this.UserId = userId;
        this.UrlOfPhoto = urlOfPhoto;
        this.StatusUser = statusUser;
        this.NameOfUser = nameOfUser;

    }
    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        this.UserId = userId;
    }

    public String getUrlOfPhoto() {
        return UrlOfPhoto;
    }

    public void setUrlOfPhoto(String urlOfPhoto) {
        this.UrlOfPhoto = urlOfPhoto;
    }

    public String getStatusUser() {
        return StatusUser;
    }

    public void setStatusUser(String statusUser) {
        this.StatusUser = statusUser;
    }

    public String getNameOfUser() {
        return NameOfUser;
    }

    public void setNameOfUser(String nameOfUser) {
        this.NameOfUser = nameOfUser;
    }


}

