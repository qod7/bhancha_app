package com.bhanchha.app;

import android.content.Context;

/**
 * Created by Shashwat on Nov 29.
 */

// class Build2URL {
//
//    protected static String baseURL = "http://192.168.2.103:81/Qod/";
//    //protected static String baseURL = "http://192.168.43.184:81/Qod/";
//    private static String userPass = null;
//
//    public static void setUserPass(String user, String pass) {
//        userPass = "?username=" + user + "&password=" + pass;
//    }
//
//    //foodImage
//    public static String foodImage(String id) {
//        if (id == null) return null;
//        return baseURL + id.trim() + ".jpg";
//    }
//
//    // cookImage
//    public static String cookImage(String id) {
//        if (id == null) return null;
//        return baseURL + id.trim() + ".jpg";
//        //return "http://www.gravatar.com/avatar/" + id.trim() + "?d=wavatar";
//    }
//
//    public static String browseFood() {
//        return baseURL + "browse_food.json";
//    }
//    // foodDetails
//
//    // cookDetails
//
//    public static String browseCook() {
//        return baseURL + "browse_cook.json";
//    }
//
//    // userDetails
//
//    // userLogin
//
//    // myOrders
//    public static String myOrders() {
//             return baseURL + "my_orders.json";
//    }
//}

public class BuildURL {

    //protected static String baseURL = "http://bhanchhaapp.ktmlive.com/";
    protected static String baseURL = "http://www.bhancha.com/";
    protected static String gravatarURL = "http://www.gravatar.com/avatar/";

    //foodImage
    public static String foodImage(String id) {
        if (id == null) return null;
        return id.trim();
    }

    // cookImage
    public static String cookImage(String id) {
        if (id == null) return null;
        return gravatarURL + id.trim() + "?d=http://bhancha.com/static/punjabi_chef.png";
        //return gravatarURL + id.trim() + "?d=http://i.imgur.com/wPG0sCG.png";
    }

    // user profile image
    public static String userImage(String id) {
        if (id == null) return null;

        //return gravatarURL + id.trim() + "?d=http://bhancha.com/static/bhanchha_logo.png";
        return gravatarURL + id.trim() + "?d=http://i.imgur.com/8SlEedB.png";
    }

    // foodDetails
    public static String browseFood() {
        return baseURL + "browse_food";
        //return "http://192.168.2.101:81/Qod/browse_food.json";
    }

    // cookDetails
    public static String browseCook() {
        return baseURL + "browse_cook";
    }

    // userDetails

    // userLogin
    public static String userLogin(String user, String pass) {
        return baseURL + "logincheck?user=" + user + "&pass=" + pass;
    }

    // session tag verification
    public static String verifySession(String tag) {
        return baseURL + "sessioncheck?tag=" + tag;
    }

    // myOrders
    public static String myOrders(Context applicationContext) {
        return baseURL + "browse_order?tag=" + (new SessionManager(applicationContext)).getSessionTag();
    }
}