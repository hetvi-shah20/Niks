package com.example.niks.ApiHelper;
public class WebURL {
    private static final String  IP_ADDRESS =  "192.168.0.104";
    private static final String KEY_MAIN_URL = "http://" + IP_ADDRESS + "/Niks/";
    public static final String KEY_SIGNUP_URL =  KEY_MAIN_URL + "signupapi.php";
    public static final String KEY_LOGIN_URL = KEY_MAIN_URL + "loginapi.php";
    public static final String KEY_CHANGE_PWD_URL = KEY_MAIN_URL + "changepassword.php";
    public static final String KEY_USER_DETAILS_UPDATE = KEY_MAIN_URL + "edituserdetails.php";
    public static final String KEY_FORGOTPWD_URL = KEY_MAIN_URL + "fotgot-password-api.php";
    public static final String KEY_DISPLAY_CATEGORY = KEY_MAIN_URL + "category.php";
    public static final String KEY_CAT_IMAGE_URL = KEY_MAIN_URL + "/upload/";
    public static final String KEY_SUBCATEGORY_DISPLAY = KEY_MAIN_URL + "subcategory.php";
    public static final String KEY_SUBCAT_IMAGE_URL = KEY_MAIN_URL + "/subcategory/";
    public static final String KEY_PRODUCT_URL = KEY_MAIN_URL + "productdisplay.php";
    public static final String KEY_CART_INSERT_URL = KEY_MAIN_URL + "cart-insert.php";
    public static final String KEY_CART_UPDATE_URL = KEY_MAIN_URL + "cart-update.php";
    public static final String CART_PRODUCT_DETAIL_URL = KEY_MAIN_URL + "cart-view-product-single.php";
    public static final String VIEW_CART_URL = KEY_MAIN_URL + "cart-view.php";
    public static final String REMOVE_FROM_CART_URL = KEY_MAIN_URL + "cart-remove.php";
    public static final String ADD_ADDRESS_URL = KEY_MAIN_URL + "add-address-api.php";
    public static final String VIEW_ADDRESS_URL = KEY_MAIN_URL + "address-view.php";
    public static final String INSERT_ORDER_URL = KEY_MAIN_URL + "insert-order-api.php";
    public static final String ORDER_LISTING = KEY_MAIN_URL + "order-listing-api.php";

}
