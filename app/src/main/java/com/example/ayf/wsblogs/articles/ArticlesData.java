package com.example.ayf.wsblogs.articles;

public class ArticlesData {

    private int articleID;
    private String userName;
    private String title;
    private String articleMsg;
    private int starts;
    private String uploadTime;

    public ArticlesData(int articleID, String userName, String title, String articleMsg, int starts, String uploadTime) {
        this.articleID = articleID;
        this.userName = userName;
        this.title = title;
        this.articleMsg = articleMsg;
        this.starts = starts;
        this.uploadTime = uploadTime;
    }

    public int getArticleID() {
        return articleID;
    }

    public String getUserName() {
        return userName;
    }

    public String getTitle() {
        return title;
    }

    public String getArticleMsg() {
        return articleMsg;
    }

    public int getStarts() {
        return starts;
    }

    public String getUploadTime() {
        return uploadTime;
    }
}
