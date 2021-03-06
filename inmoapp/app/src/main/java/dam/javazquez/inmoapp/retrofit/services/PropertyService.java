package dam.javazquez.inmoapp.retrofit.services;

import java.util.Map;

import dam.javazquez.inmoapp.dto.AddPropertyDto;
import dam.javazquez.inmoapp.dto.EditPropertyDto;
import dam.javazquez.inmoapp.responses.PropertyFavsResponse;
import dam.javazquez.inmoapp.responses.PropertyResponse;
import dam.javazquez.inmoapp.responses.ResponseContainer;
import dam.javazquez.inmoapp.responses.ResponseContainerOneRow;
import dam.javazquez.inmoapp.responses.UserFavResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface PropertyService {
    final String BASE_URL = "/properties";

    @GET(BASE_URL)
    Call<ResponseContainer<PropertyResponse>> listProperties(@QueryMap Map<String, String> options);

    @GET(BASE_URL+"/auth")
    Call<ResponseContainer<PropertyResponse>> listPropertiesAuth(@QueryMap Map<String, String> options);

    @GET(BASE_URL)
    Call<ResponseContainer<PropertyResponse>> listGeo(@QueryMap Map<String, String> options);

    @GET(BASE_URL + "/mine")
    Call<ResponseContainer<PropertyFavsResponse>> getMine();

    @GET(BASE_URL + "/fav")
    Call<ResponseContainer<PropertyResponse>> getFavs();

    @GET(BASE_URL + "/{id}")
    Call<ResponseContainerOneRow<PropertyResponse>>getOne(@Path("id") String id);

    @POST(BASE_URL)
    Call<AddPropertyDto> create (@Body AddPropertyDto property);

    @POST(BASE_URL+"/fav/{id}")
    Call<UserFavResponse> addFav (@Path("id") String id);

    @PUT(BASE_URL + "/{id}")
    Call<EditPropertyDto> edit(@Path("id") String id, @Body EditPropertyDto edited);

    @DELETE(BASE_URL + "/{id}")
    Call<PropertyFavsResponse> delete(@Path("id") String id);

    @DELETE(BASE_URL + "/fav/{id}")
    Call<UserFavResponse> deleteFav(@Path("id") String id);
}
