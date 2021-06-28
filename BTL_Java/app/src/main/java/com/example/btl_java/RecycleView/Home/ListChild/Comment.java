package com.example.btl_java.RecycleView.Home.ListChild;

public class Comment {
    private String userName;
    private String timeCMT;
    private String content;
    private int bookID;
    private int userID;

    public Comment(String userName, String timeCMT, String content, int bookID, int userID) {
        this.userName = userName;
        this.timeCMT = timeCMT;
        this.content = content;
        this.bookID = bookID;
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTimeCMT() {
        return timeCMT;
    }

    public void setTimeCMT(String timeCMT) {
        this.timeCMT = timeCMT;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
