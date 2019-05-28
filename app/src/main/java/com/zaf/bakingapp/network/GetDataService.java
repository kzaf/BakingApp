package com.zaf.bakingapp.network;

import com.zaf.bakingapp.models.Cake;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetDataService {
    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<List<Cake>> getAllCakes();
}
