package com.github.rtyvZ.kitties.common

import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.common.models.CatBreed
import com.github.rtyvZ.kitties.network.NetworkResponse
import com.github.rtyvZ.kitties.network.data.CatResponse
import com.github.rtyvZ.kitties.network.request.FavoritesRequest
import com.github.rtyvZ.kitties.network.request.VoteRequest
import com.github.rtyvZ.kitties.network.response.ApiErrorResponse
import com.github.rtyvZ.kitties.network.response.CatResponseVoteAndFav
import com.github.rtyvZ.kitties.network.response.FavoriteCatsResponse
import com.github.rtyvZ.kitties.network.response.MyVoteResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface Api {

    @GET("images/search?limit=10")
    suspend fun getListKitties(): NetworkResponse<List<CatResponse>, Any>

    @POST("votes")
    suspend fun votes(
        @Header("x-api-key") apiKey: String,
        @Body voteRequest: VoteRequest
    ): NetworkResponse<CatResponseVoteAndFav, Any>

    @DELETE("votes/{vote_id}")
    suspend fun deleteVote(
        @Header("x-api-key") apiKey: String,
        @Path("vote_id") id: String
    ): NetworkResponse<CatResponseVoteAndFav, Any>

    @POST("images/upload")
    @Multipart
    suspend fun uploadImage(
        @Header("x-api-key") apiKey: String,
        @Part body: MultipartBody.Part,
        @Part("sub_id") userUid: RequestBody
    ): NetworkResponse<Cat, ApiErrorResponse>

    @GET("votes")
    suspend fun getMyVotes(
        @Header("x-api-key") apiKey: String,
        @Query("sub_id") id: String
    ): NetworkResponse<List<MyVoteResponse>, Any>

    @POST("favourites")
    suspend fun addCatToFavorites(
        @Header("x-api-key") apiKey: String,
        @Body body: FavoritesRequest
    ): NetworkResponse<CatResponseVoteAndFav, Any>

    @GET("favourites")
    suspend fun getFavoritesCat(
        @Header("x-api-key") apiKey: String,
        @Query("sub_id") subId: String
    ): NetworkResponse<List<FavoriteCatsResponse>, Any>

    @DELETE("favourites/{favourite_id}")
    suspend fun deleteFavoriteCat(
        @Header("x-api-key") apiKey: String,
        @Path("favourite_id") id: Int
    ): NetworkResponse<Any, Any>

    @DELETE("images/{image_id}")
    suspend fun deleteUploadedImage(
        @Header("x-api-key") apiKey: String,
        @Path("image_id") id: String
    ): NetworkResponse<Any, Any>

    @GET("breeds")
    suspend fun getAllCatsBreeds(
        @Header("x-api-key") apiKey: String
    ):NetworkResponse<List<CatBreed>, Any>
}