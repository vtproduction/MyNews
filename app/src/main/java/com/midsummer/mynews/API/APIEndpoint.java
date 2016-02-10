package com.midsummer.mynews.API;

import com.midsummer.mynews.helper.Constant;
import com.midsummer.mynews.model.APIResponse;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by nienb on 9/2/16.
 */
public interface APIEndpoint {

    @GET("/search?show-tags=keyword&show-elements=all&show-fields=headline%2CtrailText%2Cbody%2Cmain%2CcreationDate%2Cthumbnail&api-key=" + Constant.APIKEY)
    Call<APIResponse> getLastestArticle(@Query("page") int page);

    @GET("/search?show-tags=keyword&show-elements=all&show-fields=headline%2CtrailText%2Cbody%2Cmain%2CcreationDate%2Cthumbnail&api-key=" + Constant.APIKEY)
    Call<APIResponse> getArticleBySearch(@Query("q") String searchQuery,@Query("page") int page);

    @GET("/{id}?show-tags=keyword&show-elements=all&show-fields=headline%2CtrailText%2Cbody%2Cmain%2CcreationDate%2Cthumbnail&api-key=" + Constant.APIKEY)
    Call<APIResponse> getArticle(@Path("id") String ArticleId);


}
