package com.example.sarprasfilkom.api;

public class UtilsApi {
    public static final String BASE_URL_API = "https://sarprasfilkomub.online/";

    // Mendeklarasikan Interface BaseApiService
    public static BaseApiService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}
