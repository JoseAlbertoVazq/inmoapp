package dam.javazquez.inmoapp.retrofit.services;

import dam.javazquez.inmoapp.responses.UserResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService {

    @GET("/users/me")
    Call<UserResponse> getMe();
}
