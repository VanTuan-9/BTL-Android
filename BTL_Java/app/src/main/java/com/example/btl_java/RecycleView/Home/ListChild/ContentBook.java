package com.example.btl_java.RecycleView.Home.ListChild;

import android.os.Parcel;
import android.os.Parcelable;

public class ContentBook implements Parcelable {
    private int idBook;
    private String[] listContentBook;

    public ContentBook(int idBook, String[] listContentBook) {
        this.idBook = idBook;
        this.listContentBook = listContentBook;
    }

    protected ContentBook(Parcel in) {
        idBook = in.readInt();
        listContentBook = in.createStringArray();
    }

    public static final Creator<ContentBook> CREATOR = new Creator<ContentBook>() {
        @Override
        public ContentBook createFromParcel(Parcel in) {
            return new ContentBook(in);
        }

        @Override
        public ContentBook[] newArray(int size) {
            return new ContentBook[size];
        }
    };

    public int getIdBook() {
        return idBook;
    }

    public void setIdBook(int idBook) {
        this.idBook = idBook;
    }

    public String[] getListContentBook() {
        return listContentBook;
    }

    public void setListContentBook(String[] listContentBook) {
        this.listContentBook = listContentBook;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idBook);
        dest.writeStringArray(listContentBook);
    }
}
