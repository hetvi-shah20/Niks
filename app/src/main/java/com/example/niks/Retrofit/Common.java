package com.example.niks.Retrofit;

import com.example.niks.Retrofit.NiksAPI;

public class Common {

    private static final String BASE_URL = "http://192.168.0.104/drinkshop/";

    public static NiksAPI getAPI()
    {
        return RetrofitClient.getClient(BASE_URL).create(NiksAPI.class);
    }
}
