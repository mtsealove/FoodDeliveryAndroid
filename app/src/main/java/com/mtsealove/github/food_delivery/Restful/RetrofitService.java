package com.mtsealove.github.food_delivery.Restful;

import androidx.cardview.widget.CardView;
import com.mtsealove.github.food_delivery.Entity.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface RetrofitService {
    @GET("DeliveryService/Android/Get/RestaurantList.php")
    Call<List<Restaurant>> GetRestaurantList(@Query("Cat") int cat);

    @GET("DeliveryService/Android/Get/MenuList.php")
    Call<List<Menu>> GetMenuList(@Query("managerID") String managerID);

    @GET("DeliveryService/Android/Get/Menu.php")
    Call<Menu> GetMenu(@Query("ItemID") int ID);

    @FormUrlEncoded
    @POST("DeliveryService/Android/Post/CreateOrder.php")
    Call<Result> PostOrder(@Field("ManagerID") String ManagerID,
                           @Field("MemberID") String MemberID,
                           @Field("OrderTime") String OrderTime,
                           @Field("Location") String Location,
                           @Field("ItemID") int itemID);

    @GET("DeliveryService/Android/Get/CurrentOrder.php")
    Call<List<Order>> GetCurrentOrder(@Query("MemberID") String MemberID);

    @FormUrlEncoded
    @POST("DeliveryService/Android/Post/Login.php")
    Call<ResLogin> PostLogin(@Field("ID") String ID, @Field("Password") String password);

    @FormUrlEncoded
    @POST("DeliveryService/Android/Post/IdCheck.php")
    Call<ResIdExist> CheckIdExist(@Field("ID") String ID);

    @FormUrlEncoded
    @POST("DeliveryService/Android/Post/CreateMember.php")
    Call<Result> CreateMember(@Field("ID") String ID, @Field("Name") String name, @Field("Password") String password);

    @GET("DeliveryService/Android/Get/Review.php")
    Call<List<Review>> GetReview(@Query("ManagerID") String managerID);

    @FormUrlEncoded
    @POST("DeliveryService/Android/Post/CreateReview.php")
    Call<Result> CreateReview(@Field("MemberID") String memberID, @Field("ManagerID") String managerID, @Field("Content") String content);
}
