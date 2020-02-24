package com.example.ayf.wsblogs.unify;

public class User {
    public static String username;
    public static int userID;
    public static String userPhone;

    public static String getUserPhone() {
        return userPhone;
    }

    public static void setUserPhone(String userPhone) {
        User.userPhone = userPhone;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        User.username = username;
    }

    public static int getUserID() {
        return userID;
    }

    public static void setUserID(int userID) {
        User.userID = userID;
    }
}
