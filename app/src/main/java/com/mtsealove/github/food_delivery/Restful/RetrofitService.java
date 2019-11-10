package com.mtsealove.github.food_delivery.Restful;

import com.mtsealove.github.food_delivery.Entity.Menu;
import com.mtsealove.github.food_delivery.Entity.Restaurant;
import com.mtsealove.github.food_delivery.Entity.Result;
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
}
