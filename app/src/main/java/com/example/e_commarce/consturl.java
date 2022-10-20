package com.example.e_commarce;

public class consturl {

//    host your whole backend stuffs like database on platform like 000webhosting or heroku and put API URL as base URL
    private static String baseurl = "";
    private static String categoryurl = baseurl +"getCategory_android.php";
    private static String signupurl = baseurl+"signup_android.php";
    private static String loginurl = baseurl + "login.php";
    public static String producturl = baseurl + "getProductAll_android.php" ;
    public static String catprod = baseurl + "getProduct_android.php";

    public static String getCategoryurl() {
        return categoryurl;
    }

    public static String getCatprod() {
        return catprod;
    }

    public static String getLoginurl() {
        return loginurl;
    }

    public static String getSignupurl() {
        return signupurl;
    }

    public static String getProducturl() {
        return producturl;
    }
}
