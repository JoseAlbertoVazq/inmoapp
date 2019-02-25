package dam.javazquez.inmoapp.retrofit.services;

import dam.javazquez.inmoapp.responses.PhotoResponse;
import dam.javazquez.inmoapp.responses.ResponseContainer;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PhotoService {

    final String BASE_URL = "/photos";

    @GET(BASE_URL)
    Call<ResponseContainer<PhotoResponse>> getAll();

    @GET(BASE_URL + "/{id}")
    Call<PhotoResponse> getOne(@Path("id") String id);

    @POST(BASE_URL)
    Call<PhotoResponse> upload();

    @DELETE(BASE_URL + "/{id}")
    Call<PhotoResponse> delete(@Path("id") String id);

}
