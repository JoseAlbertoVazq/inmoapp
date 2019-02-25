package dam.javazquez.inmoapp.retrofit.services;

import dam.javazquez.inmoapp.responses.PropertyResponse;
import dam.javazquez.inmoapp.responses.ResponseContainer;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PropertyService {
    final String BASE_URL = "/properties";

    @GET(BASE_URL)
    Call<ResponseContainer<PropertyResponse>> listProperties();

    @GET(BASE_URL + "/mine")
    Call<ResponseContainer<PropertyResponse>> getMine();

    @GET(BASE_URL + "/fav")
    Call<ResponseContainer<PropertyResponse>> getFavs();

    @GET(BASE_URL + "/{id}")
    Call<PropertyResponse> getOne(@Path("id") String id);

    @POST(BASE_URL)
    Call<PropertyResponse> create (@Body PropertyResponse property);

    @POST(BASE_URL+"/fav/{id}")
    Call<PropertyResponse> addFav (@Path("id") String id);

    @PUT(BASE_URL + "/{id}")
    Call<PropertyResponse> edit(@Path("id") String id, @Body PropertyResponse edited);

    @DELETE(BASE_URL + "/{id}")
    Call<PropertyResponse> delete(@Path("id") String id);

    @DELETE(BASE_URL + "/fav/{id}")
    Call<PropertyResponse> deleteFav(@Path("id") String id);
}