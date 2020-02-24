package com.example.ayf.wsblogs.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "UserArticles.db";
    public static final String TABLE_NAME = "userArticles";//表名
    private static final String ID = "_id";//id自增长
    private static final String USERID = "user_id";//用户名
    private static final String MSG = "msg";//文章内容
    private static final String TIME = "time";//时间
    private static final String TITLE = "article_title";//文章标题
    public static final int DB_VERSION = 1;

    public static final String SQL_CREATE_TABLE = "create table " + TABLE_NAME + "(" +
            ID + " integer primary key autoincrement," +
            USERID + "  integer," +
            MSG + " text," +
            TIME + " varchar(20)," +
            TITLE + " text" +
            ")";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                // do something
                break;

            default:
                break;
        }
    }
}
