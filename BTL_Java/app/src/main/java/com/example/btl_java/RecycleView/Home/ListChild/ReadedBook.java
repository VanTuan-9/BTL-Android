package com.example.btl_java.RecycleView.Home.ListChild;

import android.os.Parcel;
import android.os.Parcelable;

public class ReadedBook implements Parcelable {
    int id_book;
    int id_user;

    public ReadedBook(int id_book, int id_user) {
        this.id_book = id_book;
        this.id_user = id_user;
    }

    protected ReadedBook(Parcel in) {
        id_book = in.readInt();
        id_user = in.readInt();
    }

    public static final Creator<ReadedBook> CREATOR = new Creator<ReadedBook>() {
        @Override
        public ReadedBook createFromParcel(Parcel in) {
            return new ReadedBook(in);
        }

        @Override
        public ReadedBook[] newArray(int size) {
            return new ReadedBook[size];
        }
    };

    public int getId_book() {
        return id_book;
    }

    public void setId_book(int id_book) {
        this.id_book = id_book;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_book);
        dest.writeInt(id_user);
    }
}
