package com.example.bhrdjawabarat.api;

import com.example.bhrdjawabarat.model.ModelJadwal;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiService {
    @GET
    Call<ModelJadwal> getJadwal(@Url String url);
}

