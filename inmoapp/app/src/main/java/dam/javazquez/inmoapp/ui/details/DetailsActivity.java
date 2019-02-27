package dam.javazquez.inmoapp.ui.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import dam.javazquez.inmoapp.R;
import dam.javazquez.inmoapp.responses.PropertyResponse;
import dam.javazquez.inmoapp.retrofit.services.PropertyService;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ImageView photo, imageViewLeftArrow, imageViewRightArrow;
    private Context ctx;
    private PropertyResponse property;
    private int count = 0;
    private MapView mapViewDetails;
    private PropertyService service;
    private GoogleMap gmap;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private TextView title, description, price, size, room, zipcode, address, category, city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent i = getIntent();
        property = (PropertyResponse) i.getSerializableExtra("property");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadItems();
        setItems();
        if (property.getPhotos().size() == 0) {
            imageViewLeftArrow.setImageDrawable(null);
            imageViewRightArrow.setImageDrawable(null);
        }else {
            imageViewRightArrow.setOnClickListener(v -> changePictureRight());
            imageViewLeftArrow.setOnClickListener(v -> changePictureLeft());
        }

            Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapViewDetails = findViewById(R.id.mapViewDetails);
        mapViewDetails.onCreate(mapViewBundle);
        mapViewDetails.getMapAsync(this);
        imageViewRightArrow.setOnClickListener(v -> changePictureRight());
        imageViewLeftArrow.setOnClickListener(v -> changePictureLeft());
    }

    private void loadItems() {
        ctx = this;

        title = findViewById(R.id.details_title);
        description = findViewById(R.id.details_description);
        price = findViewById(R.id.price_details);
        size = findViewById(R.id.details_size);
        room = findViewById(R.id.rooms_details);
        address = findViewById(R.id.details_address);
        zipcode = findViewById(R.id.details_zipcode);
        category = findViewById(R.id.category_details);
        city = findViewById(R.id.details_city);
        mapViewDetails = findViewById(R.id.mapViewDetails);
        imageViewLeftArrow = findViewById(R.id.imageViewLeftArrow);
        imageViewRightArrow = findViewById(R.id.imageViewRightArrow);
        photo = findViewById(R.id.details_photo);

        if (property.getPhotos().size() == 0) {
            Glide.with(this).load("https://rexdalehyundai.ca/dist/img/nophoto.jpg")
                    .centerCrop()
                    .into(photo);
        } else {
            Glide.with(this).load(property.getPhotos().get(0)).centerCrop().into(photo);
        }


    }

    public void setItems(){

        title.setText(property.getTitle());
        description.setText(property.getDescription());
        price.setText(String.valueOf(property.getPrice())+"€");
        address.setText(property.getAddress());
        size.setText(String.valueOf(property.getSize())+"/m2");
        city.setText(property.getZipcode() + ", " + property.getCity() + ", " + property.getProvince());
        category.setText(property.getCategoryId().getName());

    }



    public void changePictureRight(){
        //count++;
        Glide
                .with(ctx)
                .load(property.getPhotos().get(count))
                .into(photo);

        count++;
        if (count>=property.getPhotos().size()){
            count=0;
        }


    }
    public void changePictureLeft(){
        //count--;
        Glide
                .with(ctx)
                .load(property.getPhotos().get(count))
                .into(photo);
        count--;
        if (count<0){
            count=property.getPhotos().size()-1;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMinZoomPreference(10);
        String loc =property.getLoc();
        String[] locs =loc.split(",");
        locs[0].trim();
        locs[1].trim();
        float latitud = Float.parseFloat(locs[0]);
        float longitud = Float.parseFloat(locs[1]);


        LatLng position = new LatLng(latitud, longitud);
        googleMap.addMarker(new MarkerOptions()
                .position(position)
                .title(property.getAddress())
                .snippet("dam.javazquez.inmoapp")
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location))
        );
        gmap.moveCamera(CameraUpdateFactory.newLatLng(position));
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapViewDetails.onSaveInstanceState(mapViewBundle);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapViewDetails.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapViewDetails.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapViewDetails.onStop();
    }
    @Override
    protected void onPause() {
        mapViewDetails.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapViewDetails.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapViewDetails.onLowMemory();
    }

}
