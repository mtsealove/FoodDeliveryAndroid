package com.mtsealove.github.food_delivery.Restful;

import androidx.annotation.Nullable;
import com.mtsealove.github.food_delivery.Entity.Menu;
import com.mtsealove.github.food_delivery.Entity.Restaurant;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface RetrofitService {
    @GET("DeliveryService/Android/Get/RestaurantList.php")
    Call<List<Restaurant>> GetRestaurantList(@Query("Cat") int cat);

    @GET("DeliveryService/Android/Get/MenuList.php")
    Call<List<Menu>> GetMenuList(@Query("managerID") String managerID);

    @GET("DeliveryService/Android/Get/Menu.php")
    Call<Menu> GetMenu(@Query("ItemID") int ID);
}
