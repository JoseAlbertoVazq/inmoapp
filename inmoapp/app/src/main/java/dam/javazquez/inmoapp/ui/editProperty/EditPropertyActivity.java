package dam.javazquez.inmoapp.ui.editProperty;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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
import dam.javazquez.inmoapp.dto.EditPropertyDto;
import dam.javazquez.inmoapp.responses.CategoryResponse;
import dam.javazquez.inmoapp.responses.PropertyResponse;
import dam.javazquez.inmoapp.responses.ResponseContainer;
import dam.javazquez.inmoapp.responses.UserResponse;
import dam.javazquez.inmoapp.retrofit.generator.AuthType;
import dam.javazquez.inmoapp.retrofit.generator.ServiceGenerator;
import dam.javazquez.inmoapp.retrofit.services.CategoryService;
import dam.javazquez.inmoapp.retrofit.services.PropertyService;
import dam.javazquez.inmoapp.ui.addProperty.AddPropertyActivity;
import dam.javazquez.inmoapp.util.Geocode;
import dam.javazquez.inmoapp.util.UtilToken;
import dam.javazquez.inmoapp.util.data.GeographySpain;
import dam.javazquez.inmoapp.util.geography.GeographyListener;
import dam.javazquez.inmoapp.util.geography.GeographySelector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPropertyActivity extends FragmentActivity
        implements View.OnClickListener, GeographyListener {
    private EditText title, description, price, size, zipcode, address;
    private String fullAddress, jwt, loc;
    private TextView tvRegion;
    private TextView tvProvincia;
    private TextView tvMunicipio;
    PropertyService service;
    Uri uriSelected;
    UserResponse me;
    private Button btProbar, btnEdit;
    private Spinner categories;
    private List<CategoryResponse> listCategories = new ArrayList<>();
    private FloatingActionButton addPhoto;
    EditPropertyDto propertyDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_property);
        jwt = UtilToken.getToken(EditPropertyActivity.this);

        Intent i = getIntent();
        propertyDto = (EditPropertyDto) i.getSerializableExtra("property");

        loadItems();
        setItems();

        btnEdit.setOnClickListener(v -> {
            editProperty();
        });

    }

    public void loadItems() {
        title = findViewById(R.id.title_edit);
        description = findViewById(R.id.description_edit);
        price = findViewById(R.id.price_edit);
        size = findViewById(R.id.size_edit);
        zipcode = findViewById(R.id.zipcode_edit);
        address = findViewById(R.id.address_edit);

        tvRegion = (TextView) findViewById(R.id.tvRegion);
        tvProvincia = (TextView) findViewById(R.id.tvProvincia);
        tvMunicipio = (TextView) findViewById(R.id.tvMunicipio);

        btProbar = findViewById(R.id.btProbar);
        btProbar.setOnClickListener(this);
        categories = findViewById(R.id.spinner_category);
        loadAllCategories();

        btnEdit = findViewById(R.id.edit_property);
    }

    public void setItems() {
        title.setText(propertyDto.getTitle());
        description.setText(propertyDto.getDescription());
        price.setText(String.valueOf(propertyDto.getPrice()));
        address.setText(propertyDto.getAddress());
        size.setText(String.valueOf(propertyDto.getSize()));
        zipcode.setText(String.valueOf(propertyDto.getZipcode()));
        address.setText(propertyDto.getAddress());
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
                            new ArrayAdapter<>(EditPropertyActivity.this, android.R.layout.simple_spinner_dropdown_item, listCategories);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categories.setAdapter(adapter);
                    categories.setSelection(listCategories.size() - 1);
                } else {
                    Toast.makeText(EditPropertyActivity.this, "Error loading categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseContainer<CategoryResponse>> call, Throwable t) {
                Toast.makeText(EditPropertyActivity.this, "Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void editProperty() {
        fullAddress = "Calle " + address.getText().toString() + ", " + zipcode.getText().toString() + " " + " " + tvProvincia.getText().toString() + ", España";
        try {
            loc = getLoc(fullAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }

        EditPropertyDto edited = propertyDto;
        CategoryResponse chosen = (CategoryResponse) categories.getSelectedItem();
        edited.setTitle(title.getText().toString());
        edited.setDescription(description.getText().toString());
        edited.setAddress(address.getText().toString());
        edited.setZipcode(zipcode.getText().toString());
        edited.setCity(tvMunicipio.getText().toString());
        edited.setPrice(Long.parseLong(price.getText().toString()));
        edited.setSize(Long.parseLong(size.getText().toString()));
        edited.setProvince(tvProvincia.getText().toString());
        edited.setCategoryId(chosen.getId());
        edited.setLoc(loc);

        editProperty(edited);

    }

    public String getLoc(String fullAddress) throws IOException {
        String loc = Geocode.getLatLong(EditPropertyActivity.this, fullAddress);
        return loc;
    }

    @Override
    public void onGeographySelected(Map<String, String> hm) {
        tvRegion.setText(hm.get(GeographySpain.REGION));
        tvProvincia.setText(hm.get(GeographySpain.PROVINCIA));
        tvMunicipio.setText(hm.get(GeographySpain.MUNICIPIO));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btProbar) {
            GeographySelector gs = new GeographySelector(EditPropertyActivity.this);
            gs.setOnGeograpySelectedListener(EditPropertyActivity.this);
            FragmentManager fm = getSupportFragmentManager();
            gs.show(fm, "geographySelector");
        }

    }

    public void editProperty(EditPropertyDto edited) {
        service = ServiceGenerator.createService(PropertyService.class, jwt, AuthType.JWT);


        Call<EditPropertyDto> call = service.edit(edited.getId(), edited);
        call.enqueue(new Callback<EditPropertyDto>() {
            @Override
            public void onResponse(Call<EditPropertyDto> call, Response<EditPropertyDto> response) {

                if (response.isSuccessful()) {

                    Toast.makeText(EditPropertyActivity.this, "Created", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditPropertyActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EditPropertyDto> call, Throwable t) {
              //  Toast.makeText(EditPropertyActivity.this, "Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
