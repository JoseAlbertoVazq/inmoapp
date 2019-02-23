package dam.javazquez.inmoapp.retrofit.services;

import dam.javazquez.inmoapp.responses.LoginResponse;
import dam.javazquez.inmoapp.responses.UserResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface LoginService {

    @POST("/auth")
    Call<LoginResponse> doLogin(@Header("Authorization") String authorization);


    @POST("/users")
    Call<LoginResponse> doSignUp(@Body UserResponse signedUpUser);

}
