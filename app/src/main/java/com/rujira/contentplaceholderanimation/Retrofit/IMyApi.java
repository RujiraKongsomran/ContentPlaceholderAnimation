package com.rujira.contentplaceholderanimation.Retrofit;


import com.rujira.contentplaceholderanimation.Model.Model;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface IMyApi {
    @GET("photos")
    Observable<List<Model>> getData();


}
