package com.example.ayf.wsblogs.comments;

public class CommentsData {

    private String author;
    private String toSb;
    private String comments;
    private String commentsTime;
    private int headImgId;

    public CommentsData(String author, String toSb, String comments, String commentsTime, int headImgId) {
        this.author = author;
        this.toSb = toSb;
        this.comments = comments;
        this.commentsTime = commentsTime;
        this.headImgId = headImgId;
    }

    public String getAuthor() {
        return author;
    }

    public String getToSb() {
        return toSb;
    }

    public String getComments() {
        return comments;
    }

    public String getCommentsTime() {
        return commentsTime;
    }

    public int getHeadImgId() {
        return headImgId;
    }
}
