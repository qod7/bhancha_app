package com.bhanchha.app;

/**
 * Created by Shashwat on Nov 29.
 */
 class Build2URL {

    protected static String baseURL = "http://192.168.2.103:81/Qod/";
    //protected static String baseURL = "http://192.168.43.184:81/Qod/";
    private static String userPass = null;

    public static void setUserPass(String user, String pass) {
        userPass = "?username=" + user + "&password=" + pass;
    }

    //foodImage
    public static String foodImage(String id) {
        if (id == null) return null;
        return baseURL + id.trim() + ".jpg";
    }

    // cookImage
    public static String cookImage(String id) {
        if (id == null) return null;
        return baseURL + id.trim() + ".jpg";
        //return "http://www.gravatar.com/avatar/" + id.trim() + "?d=wavatar";
    }

    public static String browseFood() {
        return baseURL + "browse_food.json";
    }
    // foodDetails

    // cookDetails

    public static String browseCook() {
        return baseURL + "browse_cook.json";
    }

    // userDetails

    // userLogin

    // myOrders
    public static String myOrders() {
             return baseURL + "my_orders.json";
    }
}



public class BuildURL {

    protected static String baseURL = "http://bhanchhaapp.ktmlive.com/";          //.jpg
    protected static String gravatarURL = "http://www.gravatar.com/avatar/";    //.jpg

    private static String userPass = null;

    public static void setUserPass(String user, String pass) {
        userPass = "?username=" + user + "&password=" + pass;
    }

    //foodImage
    public static String foodImage(String id) {
        if (id == null) return null;
        return id.trim();
    }

    // cookImage
    public static String cookImage(String id) {
        if (id == null) return null;
        return gravatarURL + id.trim() + "?d=http%3A%2F%2Fbhanchhaapp.ktmlive.com%2Fstatic%2Fcook.jpg";
    }

    public static String browseFood() {
        return baseURL + "browse_food";
    }
    // foodDetails

    // cookDetails

    public static String browseCook() {
        return baseURL + "browse_cook";
    }

    // userDetails

    // userLogin

    // myOrders
    public static String myOrders() {
        return baseURL + "browse_order" + userPass;
    }
}