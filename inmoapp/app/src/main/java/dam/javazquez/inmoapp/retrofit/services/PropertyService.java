package dam.javazquez.inmoapp.retrofit.services;

import dam.javazquez.inmoapp.responses.PropertyResponse;
import dam.javazquez.inmoapp.responses.ResponseContainer;
import retrofit2.Call;
import retrofit2.http.GET;

public interface PropertyService {
    final String BASE_URL = "/properties";

    @GET(BASE_URL)
    Call<ResponseContainer<PropertyResponse>> listProperties();
}
