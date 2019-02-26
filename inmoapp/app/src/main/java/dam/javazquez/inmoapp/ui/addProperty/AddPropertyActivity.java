package dam.javazquez.inmoapp.ui.addProperty;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dam.javazquez.inmoapp.R;
import dam.javazquez.inmoapp.responses.CategoryResponse;
import dam.javazquez.inmoapp.responses.PropertyFavsResponse;
import dam.javazquez.inmoapp.responses.PropertyResponse;
import dam.javazquez.inmoapp.responses.ResponseContainer;
import dam.javazquez.inmoapp.responses.UserResponse;
import dam.javazquez.inmoapp.retrofit.generator.AuthType;
import dam.javazquez.inmoapp.retrofit.generator.ServiceGenerator;
import dam.javazquez.inmoapp.retrofit.services.CategoryService;
import dam.javazquez.inmoapp.retrofit.services.PropertyService;
import dam.javazquez.inmoapp.retrofit.services.UserService;
import dam.javazquez.inmoapp.util.Geocode;
import dam.javazquez.inmoapp.util.UtilToken;
import dam.javazquez.inmoapp.util.data.GeographySpain;
import dam.javazquez.inmoapp.util.geography.GeographyListener;
import dam.javazquez.inmoapp.util.geography.GeographySelector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPropertyActivity extends FragmentActivity
        implements View.OnClickListener, GeographyListener {

    private EditText title, description, price, size, zipcode, address;
    private String fullAddress, jwt, loc;
    private TextView tvRegion;
    private TextView tvProvincia;
    private TextView tvMunicipio;
    PropertyService service;
    UserResponse me;
    private Button btProbar, btnAdd;
    private Spinner categories;
    private List<CategoryResponse> listCategories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);
        jwt = UtilToken.getToken(this);
        me = getMe();

        btnAdd = findViewById(R.id.add_property);
        btProbar = (Button) findViewById(R.id.btProbar);
        btProbar.setOnClickListener(this);

        title = findViewById(R.id.title_add);
        description = findViewById(R.id.description_add);
        price = findViewById(R.id.price_add);
        size = findViewById(R.id.size_add);
        address = findViewById(R.id.address_add);
        zipcode = findViewById(R.id.zipcode_add);

        tvRegion = (TextView) findViewById(R.id.tvRegion);
        tvProvincia = (TextView) findViewById(R.id.tvProvincia);
        tvMunicipio = (TextView) findViewById(R.id.tvMunicipio);

        categories = findViewById(R.id.spinner_category);
        loadAllCategories();




        btnAdd.setOnClickListener(v -> {
            fullAddress = "Calle " + address.getText().toString() + ", " + zipcode.getText().toString() + " " + " " + tvProvincia.getText().toString() + ", España";
            try {
                loc = getLoc(fullAddress);
            } catch (IOException e) {
                e.printStackTrace();
            }

            PropertyFavsResponse create = new PropertyFavsResponse();
            CategoryResponse chosen = (CategoryResponse) categories.getSelectedItem();
            create.setTitle(title.getText().toString());
            create.setDescription(description.getText().toString());
            create.setAddress(address.getText().toString());
            create.setZipcode(zipcode.getText().toString());
            create.setCity(tvMunicipio.getText().toString());
            create.setPrice(Long.parseLong(price.getText().toString()));
            create.setSize(Long.parseLong(size.getText().toString()));
            create.setProvince(tvProvincia.getText().toString());
            create.setOwnerId(me.get_id());
            create.setCategoryId(chosen);
            create.setLoc(loc);
            //faltaría subir fotos
            addProperty(create);
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btProbar) {
            GeographySelector gs = new GeographySelector(AddPropertyActivity.this);
            gs.setOnGeograpySelectedListener(AddPropertyActivity.this);
            FragmentManager fm = getSupportFragmentManager();
            gs.show(fm, "geographySelector");
        }

        if (v.getId() == R.id.add_property) {

        }

    }


    public String getLoc(String fullAddress) throws IOException {
        String loc = Geocode.getLatLong(AddPropertyActivity.this, fullAddress);
        return loc;
    }

    @Override
    public void onGeographySelected(Map<String, String> hm) {
        tvRegion.setText(hm.get(GeographySpain.REGION));
        tvProvincia.setText(hm.get(GeographySpain.PROVINCIA));
        tvMunicipio.setText(hm.get(GeographySpain.MUNICIPIO));
    }

    public void loadAllCategories() {
        CategoryService serviceC = ServiceGenerator.createService(CategoryService.class);
        Call<ResponseContainer<CategoryResponse>> callC = serviceC.listCategories();

        callC.enqueue(new Callback<ResponseContainer<CategoryResponse>>() {
            @Override
            public void onResponse(Call<ResponseContainer<CategoryResponse>> call, Response<ResponseContainer<CategoryResponse>> response) {
                if (response.isSuccessful()) {
                    int spinnerPosition = 1;
                    Log.d("successCategory", "Got category");
                    listCategories = response.body().getRows();
                    System.out.println(listCategories);
                    List<String> namesC = new ArrayList<>();

                 /*   for (CategoryResponse category : listCategories) {
                        namesC.add(category.getName());
                    }*/
                    ArrayAdapter<CategoryResponse> adapter =
                            new ArrayAdapter<>(AddPropertyActivity.this, android.R.layout.simple_spinner_dropdown_item, listCategories);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categories.setAdapter(adapter);
                    categories.setSelection(listCategories.size() - 1);
                } else {
                    Toast.makeText(AddPropertyActivity.this, "Error loading categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseContainer<CategoryResponse>> call, Throwable t) {
                Toast.makeText(AddPropertyActivity.this, "Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public UserResponse getMe() {
        UserResponse me = new UserResponse();
        UserService serviceU = ServiceGenerator.createService(UserService.class, jwt, AuthType.JWT);
        Call<UserResponse> callU = serviceU.getMe();

        callU.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    me.set_id(response.body().get_id());
                    me.setEmail(response.body().getEmail());
                    me.setName(response.body().getName());
                    me.setPicture(response.body().getPicture());
                    me.setPassword(response.body().getPassword());
                    Log.d("OK", "Got user");
                } else {
                    Log.e("Error", "Error getting user");
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("Failure", "Error getting user");
            }
        });

        return me;
    }

    public void addProperty(PropertyFavsResponse create) {
        service = ServiceGenerator.createService(PropertyService.class, jwt, AuthType.JWT);

        Call<PropertyResponse> call = service.create(create);
        call.enqueue(new Callback<PropertyResponse>() {
            @Override
            public void onResponse(Call<PropertyResponse> call, Response<PropertyResponse> response) {
                if (response.isSuccessful()) {
                    //tratamiento de imágenes aquí, coger el id de la response y
                    //añadírsela a las imágenes subidas

                    Toast.makeText(AddPropertyActivity.this, "Created", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddPropertyActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PropertyResponse> call, Throwable t) {
                Toast.makeText(AddPropertyActivity.this, "Failure", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
