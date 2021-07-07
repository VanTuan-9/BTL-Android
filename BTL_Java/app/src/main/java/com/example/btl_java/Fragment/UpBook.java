package com.example.btl_java.Fragment;

import com.example.btl_java.RecycleView.Home.ListChild.BookUp;
import com.example.btl_java.RecycleView.Home.ListChild.BookUpParam;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UpBook {
    public static final String DOMAIN = "https://api-user-last.herokuapp.com/api/bookups/";
    Gson gson = new GsonBuilder().setDateFormat("yyyy MM dd HH:mm:ss").create();
    UpBook api = new Retrofit.Builder()
            .baseUrl(DOMAIN)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(UpBook.class);
    @Multipart
    @POST("data")
    Call<BookUpParam> registerBook(@Part("nameBook") RequestBody nameBook,
                                   @Part MultipartBody.Part linkBook,
                                   @Part("author") RequestBody author,
                                   @Part("publishingCompany") RequestBody publishingCompany,
                                   @Part("language") RequestBody languageBook,
                                   @Part("numberOfPages") RequestBody numberPages,
                                   @Part("status") RequestBody Category,
                                   @Part("content") RequestBody content,
                                   @Part("describe") RequestBody shortContent,
                                   @Part("price") RequestBody price,
                                   @Part("userId") RequestBody idUser);
}
