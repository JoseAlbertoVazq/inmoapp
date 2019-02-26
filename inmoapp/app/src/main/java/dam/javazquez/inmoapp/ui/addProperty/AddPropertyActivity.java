package dam.javazquez.inmoapp.ui.addProperty;

import android.app.Service;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import dam.javazquez.inmoapp.R;
import dam.javazquez.inmoapp.responses.PropertyResponse;
import dam.javazquez.inmoapp.responses.UserResponse;
import dam.javazquez.inmoapp.retrofit.generator.AuthType;
import dam.javazquez.inmoapp.retrofit.generator.ServiceGenerator;
import dam.javazquez.inmoapp.retrofit.services.PropertyService;
import dam.javazquez.inmoapp.retrofit.services.UserService;
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
    private String fullAddress, jwt;
    private TextView tvRegion;
    private TextView tvProvincia;
    private TextView tvMunicipio;
    PropertyService service;
    private Button btProbar, btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);
        jwt = UtilToken.getToken(this);
        btnAdd = findViewById(R.id.add_property);
        btProbar = (Button) findViewById(R.id.btProbar);
        btProbar.setOnClickListener(this);

        title = findViewById(R.id.title_add);
        description = findViewById(R.id.description_add);
        price = findViewById(R.id.price_add);
        size = findViewById(R.id.zipcode_add);
        address = findViewById(R.id.address_add);



        tvRegion    = (TextView)findViewById(R.id.tvRegion);
        tvProvincia = (TextView)findViewById(R.id.tvProvincia);
        tvMunicipio = (TextView)findViewById(R.id.tvMunicipio);

        fullAddress = "Calle "+address+", "+zipcode+" "+" "+tvProvincia.toString()+", España";
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btProbar){
            GeographySelector gs = new GeographySelector(this);
            gs.setOnGeograpySelectedListener(this);
            FragmentManager fm = getSupportFragmentManager();
            gs.show(fm, "geographySelector");
        }

        if(v.getId() == R.id.add_property){
            PropertyResponse create = new PropertyResponse();

            create.setTitle(title.toString());
            create.setDescription(description.toString());
            create.setAddress(address.toString());
            create.setZipcode(zipcode.toString());
            create.setCity(tvMunicipio.toString());
            create.setOwnerId(getMe());


        }

    }

    @Override
    public void onGeographySelected(Map<String, String> hm) {
        tvRegion.setText(hm.get(GeographySpain.REGION));
        tvProvincia.setText(hm.get(GeographySpain.PROVINCIA));
        tvMunicipio.setText(hm.get(GeographySpain.MUNICIPIO));


    }
    public UserResponse getMe() {
        UserResponse me = new UserResponse();
        UserService serviceU = ServiceGenerator.createService(UserService.class, jwt, AuthType.JWT);
        Call<UserResponse> callU = serviceU.getMe();

        callU.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(response.isSuccessful()){
                    me.set_id(response.body().get_id());
                    me.setEmail(response.body().getEmail());
                    me.setName(response.body().getName());
                    me.setPicture(response.body().getPicture());
                    me.setPassword(response.body().getPassword());
                    Log.d("OK", "Got user");
                } else{
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
    public void addProperty(PropertyResponse create){
        service = ServiceGenerator.createService(PropertyService.class, jwt, AuthType.JWT);

        Call<PropertyResponse> call = service.create(create);
        call.enqueue(new Callback<PropertyResponse>() {
            @Override
            public void onResponse(Call<PropertyResponse> call, Response<PropertyResponse> response) {
                if(response.isSuccessful()){
                    //tratamiento de imágenes aquí, coger el id de la response y
                    //añadírsela a las imágenes subidas

                    Toast.makeText(AddPropertyActivity.this, "Created", Toast.LENGTH_SHORT).show();
                } else{
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
