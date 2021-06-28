package com.example.btl_java.login;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable {
    int id;
    String username;
    String password;
    double money;
    String email;
    String phoneNumber;
    String linkImg;

    protected User(Parcel in) {
        id = in.readInt();
        username = in.readString();
        password = in.readString();
        money = in.readDouble();
        email = in.readString();
        phoneNumber = in.readString();
        linkImg = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User() {
        id = 0;
    }

    public User(int id, String username, String password, double money, String email, String phoneNumber, String linkImg) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.money = money;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.linkImg = linkImg;
    }

    public User(String username, String password, double money, String email, String phoneNumber, String linkImg) {
        this.username = username;
        this.password = password;
        this.money = money;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.linkImg = linkImg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLinkImg() {
        return linkImg;
    }

    public void setLinkImg(String linkImg) {
        this.linkImg = linkImg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeDouble(money);
        dest.writeString(email);
        dest.writeString(phoneNumber);
        dest.writeString(linkImg);
    }
}
