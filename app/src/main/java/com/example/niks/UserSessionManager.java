package com.example.niks;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.niks.ApiHelper.JSONField;

public class UserSessionManager {

    private static final String MY_PREF = "user_pref";
    private static final String IS_LOGIN = "is_login";

    Context context;
    SharedPreferences sharedPreferences;

    private final SharedPreferences.Editor editor;

    public UserSessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLoginStatus() {
        editor.putBoolean(IS_LOGIN, true).commit();
    }

    public boolean getLoginStatus() {
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public void setUserDetails(String strUserID, String strUserName, String strUserGender, String strUserEmail, String strUserPassword, String strUserMobile, String strUserAddress) {
        editor.putString(JSONField.KEY_USER_ID, strUserID);
        editor.putString(JSONField.KEY_USER_NAME, strUserName);
        editor.putString(JSONField.KEY_USER_GENDER, strUserGender);
        editor.putString(JSONField.KEY_USER_EMAIL, strUserEmail);
        editor.putString(JSONField.KEY_USER_PASSWORD, strUserPassword);
        editor.putString(JSONField.KEY_USER_MOBILE, strUserMobile);
        editor.putString(JSONField.KEY_USER_ADDRESS, strUserAddress);
        editor.commit();
    }

    public String getUserId() {
        return sharedPreferences.getString(JSONField.KEY_USER_ID, "");
    }

    public String getUserName() {
        return sharedPreferences.getString(JSONField.KEY_USER_NAME, "");
    }

    public String getUserEmail() {
        return sharedPreferences.getString(JSONField.KEY_USER_EMAIL, "");
    }

    public String getUserGender() {
        return sharedPreferences.getString(JSONField.KEY_USER_GENDER, "");
    }

    public String getUserPhone() {
        return sharedPreferences.getString(JSONField.KEY_USER_MOBILE, "");
    }


    public String getUserAddress() {
        return sharedPreferences.getString(JSONField.KEY_USER_ADDRESS, "");
    }

    public void logout() {
        editor.remove(JSONField.KEY_USER_ID);
        editor.remove(JSONField.KEY_USER_NAME);
        editor.remove(JSONField.KEY_USER_EMAIL);
        editor.remove(JSONField.KEY_USER_MOBILE);
        editor.remove(JSONField.KEY_USER_PASSWORD);
        editor.remove(JSONField.KEY_USER_GENDER);
        editor.commit();
    }
}