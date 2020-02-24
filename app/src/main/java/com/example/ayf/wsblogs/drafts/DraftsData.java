package com.example.ayf.wsblogs.drafts;

public class DraftsData {

    private String title;
    private String msg;
    private String time;
    private int id;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMsg() {
        return msg;
    }

    public String getTime() {
        return time;
    }

    public DraftsData(String title,String msg,String time,int a){
        this.msg = msg;
        this.time = time;
        this.title = title;
        this.id = a;
    }
}
