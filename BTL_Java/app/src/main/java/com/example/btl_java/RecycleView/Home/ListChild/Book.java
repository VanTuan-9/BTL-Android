package com.example.btl_java.RecycleView.Home.ListChild;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Book implements Parcelable {
    //ten, tac gia, NXB, hình ảnh, ngon ngữ, ngày xuất bản, số trang, thể loại, cmt, đánh giá,...
    private int idBook; // id sách
    private String nameBook;  // tên sách
    private String urlBook;   //link ảnh sách
    private String author;    //tác giả
    private String NXB;       //tên NXB
    private String languageBook; // ngôn ngữ của sách: tiếng việt, tiếng anh,...
    private String dayXB;     //Ngày ra mắt của sách
    private int numberPages;    //số trang của sách
    private int Category; // gồm 1: new, 2: deficient, 3: full
    private int view;  // lượt xem
    private int likes; // lượt thích
    private String content; //Nội dung sách
    private String timeCreated;
    private String shortContent;

    public Book() {
    }

    public Book(int ibBook, String nameBook, String urlBook, String author, String NXB, String languageBook, String dayXB, int numberPages, int category, int view, int likes, String content, String timeCreated, String shortContent) {
        this.idBook = ibBook;
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
    }

    protected Book(Parcel in) {
        idBook = in.readInt();
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
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public void setBook(Book book) {
        idBook = book.getIdBook();
        nameBook = book.getNameBook();
        urlBook = book.getUrlBook();
        author = book.getAuthor();
        NXB = book.getNXB();
        languageBook = book.getLanguageBook();
        dayXB = book.getDayXB();
        numberPages = book.getNumberPages();
        Category = book.getCategory();
        view = book.getView();
        likes = book.getLikes();
        content = book.getContent();
        timeCreated = book.getTimeCreated();
        shortContent = book.getShortContent();
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

    public int getIdBook() {
        return idBook;
    }

    public void setIdBook(int idBook) {
        this.idBook = idBook;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idBook);
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
    }
}
