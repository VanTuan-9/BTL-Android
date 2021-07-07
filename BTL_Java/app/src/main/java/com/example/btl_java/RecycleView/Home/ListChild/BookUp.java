package com.example.btl_java.RecycleView.Home.ListChild;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class BookUp implements Parcelable {
    private int idBook; // id sách
    @SerializedName("userId")
    private int idUser;
    private String nameBook;  // tên sách
    @SerializedName("linkBook")
    private String urlBook;   //link ảnh sách
    private String author;    //tác giả
    @SerializedName("publishingCompany")
    private String NXB;       //tên NXB
    @SerializedName("language")
    private String languageBook; // ngôn ngữ của sách: tiếng việt, tiếng anh,...
    private String dayXB;     //Ngày ra mắt của sách
    @SerializedName("numberOfPages")
    private int numberPages;    //số trang của sách
    @SerializedName("status")
    private int Category; // gồm 1: new, 2: deficient, 3: full
    private int view;  // lượt xem
    private int likes; // lượt thích
    private String content; //Nội dung sách
    private String timeCreated;
    @SerializedName("describe")
    private String shortContent;
    private int classify;
    private int price;

    public BookUp(int idBook, int idUser, String nameBook, String urlBook, String author, String NXB, String languageBook, String dayXB, int numberPages, int category, int view, int likes, String content, String timeCreated, String shortContent, int classify,int price) {
        this.idBook = idBook;
        this.idUser = idUser;
        this.nameBook = nameBook;
        this.urlBook = urlBook;
        this.author = author;
        this.NXB = NXB;
        this.languageBook = languageBook;
        this.dayXB = dayXB;
        this.numberPages = numberPages;
        Category = category;
        this.view = view;
        this.likes = likes;
        this.content = content;
        this.timeCreated = timeCreated;
        this.shortContent = shortContent;
        this.classify = classify;
        this.price = price;
    }

    public BookUp(int idUser, String nameBook, String urlBook, String author, String NXB, String languageBook, int numberPages, int category, int view, int likes, String content, String shortContent, int classify, int price) {
        this.idUser = idUser;
        this.nameBook = nameBook;
        this.urlBook = urlBook;
        this.author = author;
        this.NXB = NXB;
        this.languageBook = languageBook;
        this.numberPages = numberPages;
        Category = category;
        this.view = view;
        this.likes = likes;
        this.content = content;
        this.shortContent = shortContent;
        this.classify = classify;
        this.price = price;
    }

    protected BookUp(Parcel in) {
        idBook = in.readInt();
        idUser = in.readInt();
        nameBook = in.readString();
        urlBook = in.readString();
        author = in.readString();
        NXB = in.readString();
        languageBook = in.readString();
        dayXB = in.readString();
        numberPages = in.readInt();
        Category = in.readInt();
        view = in.readInt();
        likes = in.readInt();
        content = in.readString();
        timeCreated = in.readString();
        shortContent = in.readString();
        classify = in.readInt();
        price = in.readInt();
    }

    public static final Creator<BookUp> CREATOR = new Creator<BookUp>() {
        @Override
        public BookUp createFromParcel(Parcel in) {
            return new BookUp(in);
        }

        @Override
        public BookUp[] newArray(int size) {
            return new BookUp[size];
        }
    };

    public int getIdBook() {
        return idBook;
    }

    public void setIdBook(int idBook) {
        this.idBook = idBook;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getNameBook() {
        return nameBook;
    }

    public void setNameBook(String nameBook) {
        this.nameBook = nameBook;
    }

    public String getUrlBook() {
        return urlBook;
    }

    public void setUrlBook(String urlBook) {
        this.urlBook = urlBook;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getNXB() {
        return NXB;
    }

    public void setNXB(String NXB) {
        this.NXB = NXB;
    }

    public String getLanguageBook() {
        return languageBook;
    }

    public void setLanguageBook(String languageBook) {
        this.languageBook = languageBook;
    }

    public String getDayXB() {
        return dayXB;
    }

    public void setDayXB(String dayXB) {
        this.dayXB = dayXB;
    }

    public int getNumberPages() {
        return numberPages;
    }

    public void setNumberPages(int numberPages) {
        this.numberPages = numberPages;
    }

    public int getCategory() {
        return Category;
    }

    public void setCategory(int category) {
        Category = category;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getShortContent() {
        return shortContent;
    }

    public void setShortContent(String shortContent) {
        this.shortContent = shortContent;
    }

    public int getClassify() {
        return classify;
    }

    public void setClassify(int classify) {
        this.classify = classify;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idBook);
        dest.writeInt(idUser);
        dest.writeString(nameBook);
        dest.writeString(urlBook);
        dest.writeString(author);
        dest.writeString(NXB);
        dest.writeString(languageBook);
        dest.writeString(dayXB);
        dest.writeInt(numberPages);
        dest.writeInt(Category);
        dest.writeInt(view);
        dest.writeInt(likes);
        dest.writeString(content);
        dest.writeString(timeCreated);
        dest.writeString(shortContent);
        dest.writeInt(classify);
        dest.writeInt(price);
    }
}
