package com.midsummer.mynews.API;

import com.midsummer.mynews.helper.Constant;
import com.midsummer.mynews.model.article.APIResponse;
import com.midsummer.mynews.model.topic.Topic;

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

    @GET("/search?show-tags=keyword&show-elements=all&show-fields=headline%2CtrailText%2Cbody%2Cmain%2CcreationDate%2Cthumbnail&page=1&page-size=5&api-key=" + Constant.APIKEY)
    Call<APIResponse> getArticlesBySection(@Query("section") String sectionId, @Query("from-date") String fromDate, @Query("to-date") String toDate);

    @GET("/search?show-tags=keyword&show-elements=all&show-fields=headline%2CtrailText%2Cbody%2Cmain%2CcreationDate%2Cthumbnail&page-size=10&api-key=" + Constant.APIKEY)
    Call<APIResponse> getArticlesBySection(@Query("section") String sectionId, @Query("page") int page);

    @GET("/search?show-tags=keyword&show-elements=all&show-fields=headline%2CtrailText%2Cbody%2Cmain%2CcreationDate%2Cthumbnail&api-key=" + Constant.APIKEY)
    Call<APIResponse> getArticleBySearch(@Query("q") String searchQuery,@Query("page") int page);

    @GET("/{id}?show-tags=keyword&show-elements=all&show-fields=headline%2CtrailText%2Cbody%2Cmain%2CcreationDate%2Cthumbnail&api-key=" + Constant.APIKEY)
    Call<APIResponse> getArticle(@Path("id") String ArticleId);

    @GET("/sections?show-tags=all&show-fields=trailText&page=1&page-size=5&api-key=" + Constant.APIKEY)
    Call<Topic> getTopic();




}
