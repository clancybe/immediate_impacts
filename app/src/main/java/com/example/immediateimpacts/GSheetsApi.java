package com.example.immediateimpacts;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

//Purchases API Link: https://sheet.best/api/sheets/902e414a-edb9-4f9d-8942-663db1c081b8
//Handouts API Link: https://sheet.best/api/sheets/0dca7fcd-9a63-437c-accc-fb221731ec5b

public interface GSheetsApi {

    @GET("902e414a-edb9-4f9d-8942-663db1c081b8")
    Call<List<PostPurchase>> getPurchPosts();

    @GET("0dca7fcd-9a63-437c-accc-fb221731ec5b")
    Call<List<PostDistribution>> getDistPosts();

    @POST("902e414a-edb9-4f9d-8942-663db1c081b8")
    Call<PostPurchase> submitPurchase(@Body PostPurchase post);

    @POST("0dca7fcd-9a63-437c-accc-fb221731ec5b")
    Call<PostDistribution> submitDistribution(@Body PostDistribution post);
}
