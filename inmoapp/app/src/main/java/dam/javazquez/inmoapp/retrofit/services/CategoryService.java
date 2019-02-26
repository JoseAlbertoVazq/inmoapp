package dam.javazquez.inmoapp.retrofit.services;

import dam.javazquez.inmoapp.responses.CategoryResponse;
import dam.javazquez.inmoapp.responses.ResponseContainer;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryService {

    final String BASE_URL = "/categories";

    @GET(BASE_URL)
    Call<ResponseContainer<CategoryResponse>> listCategories();

}
