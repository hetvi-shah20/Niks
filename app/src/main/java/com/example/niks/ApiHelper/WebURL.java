package com.example.niks.ApiHelper;

public class WebURL {
    private static final String  IP_ADDRESS =  "192.168.43.217";
    private static final String KEY_MAIN_URL = "http://" + IP_ADDRESS + "/Niks/";
    public static final String KEY_SIGNUP_URL =  KEY_MAIN_URL + "signupapi.php";
    public static  final String KEY_LOGIN_URL = KEY_MAIN_URL + "loginapi.php";
    public static final String KEY_CHANGE_PWD_URL = KEY_MAIN_URL + "changepassword.php";
    public static  final String KEY_USER_DETAILS_UPDATE = KEY_MAIN_URL + "edituserdetails.php";
    public static final String KEY_FORGOTPWD_URL = KEY_MAIN_URL + "fotgot-password-api.php";


}
