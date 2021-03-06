package dam.javazquez.inmoapp.ui.details;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import dam.javazquez.inmoapp.R;
import dam.javazquez.inmoapp.responses.PhotoUploadResponse;
import dam.javazquez.inmoapp.responses.PropertyFavsResponse;
import dam.javazquez.inmoapp.responses.PropertyResponse;
import dam.javazquez.inmoapp.responses.ResponseContainer;
import dam.javazquez.inmoapp.retrofit.generator.AuthType;
import dam.javazquez.inmoapp.retrofit.generator.ServiceGenerator;
import dam.javazquez.inmoapp.retrofit.services.PhotoService;
import dam.javazquez.inmoapp.retrofit.services.PropertyService;
import dam.javazquez.inmoapp.util.UtilToken;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static final int READ_REQUEST_CODE = 42;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    Map options = new HashMap();
    Uri uriSelected;
    String jwt, idUser;
    private ImageView photo, imageViewLeftArrow, imageViewRightArrow;
    private Context ctx;
    private PropertyResponse property;
    private int count = 0;
    private MapView mapViewDetails;
    private PropertyService service;
    private GoogleMap gmap;
    private ImageButton deletePhoto;
    private FloatingActionButton addPhoto;
    private TextView title, description, price, size, room, zipcode, address, category, city;
    private PhotoService servicePhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        jwt = UtilToken.getToken(DetailsActivity.this);
        idUser = UtilToken.getToken(getApplicationContext());
        options.put("near", "-6.0071807999999995,37.3803677");
        System.out.println(idUser);
        setSupportActionBar(toolbar);
        checkOwnerPhotos();
        FloatingActionButton fab = findViewById(R.id.fab);
/*
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/

        Intent i = getIntent();
        property = (PropertyResponse) i.getSerializableExtra("property");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadItems();
        setItems();
        if (property.getPhotos().size() == 0) {
            imageViewLeftArrow.setImageDrawable(null);
            imageViewRightArrow.setImageDrawable(null);
        } else {
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

        addPhoto.setOnClickListener(v -> {
            performFileSearch();
        });
        deletePhoto.setOnClickListener(v -> deletePhoto());
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
        addPhoto = findViewById(R.id.addPhotoDetails);
        deletePhoto = findViewById(R.id.deletePhoto);
        if (property.getPhotos().size() == 0) {
            Glide.with(this).load("https://rexdalehyundai.ca/dist/img/nophoto.jpg")
                    .centerCrop()
                    .into(photo);
        } else {
            Glide.with(this).load(property.getPhotos().get(0)).centerCrop().into(photo);
        }

        if (jwt == null) {
            addPhoto.setImageDrawable(null);
        }


    }

    public void deletePhoto() {
        servicePhoto = ServiceGenerator.createService(PhotoService.class);
        Call<ResponseContainer<PhotoUploadResponse>> callList = servicePhoto.getAll();
        callList.enqueue(new Callback<ResponseContainer<PhotoUploadResponse>>() {
            @Override
            public void onResponse(Call<ResponseContainer<PhotoUploadResponse>> call, Response<ResponseContainer<PhotoUploadResponse>> response) {
                if (response.isSuccessful()) {
                    if (property.getPhotos().size() > 0) {
                        for (PhotoUploadResponse photo : response.body().getRows()) {
                            if (photo.getImgurLink().equals(property.getPhotos().get(count))) {
                                PhotoService servicePhotoDelete = ServiceGenerator.createService(PhotoService.class, jwt, AuthType.JWT);
                                Call<PhotoUploadResponse> callDelete = servicePhotoDelete.delete(photo.getId());
                                callDelete.enqueue(new Callback<PhotoUploadResponse>() {
                                    @Override
                                    public void onResponse(Call<PhotoUploadResponse> call, Response<PhotoUploadResponse> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(DetailsActivity.this, "Photo deleted", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(DetailsActivity.this, "no is.Successful DELETE", Toast.LENGTH_SHORT).show();

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<PhotoUploadResponse> call, Throwable t) {
                                        Toast.makeText(DetailsActivity.this, "Failure DELETE", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        }
                    }
                } else {
                    Toast.makeText(DetailsActivity.this, "no is.Successful GET", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseContainer<PhotoUploadResponse>> call, Throwable t) {
                Toast.makeText(DetailsActivity.this, "Failure GET", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Log.i("Filechooser URI", "Uri: " + uri.toString());
            }
            uriSelected = uri;
        }

        uploadPhoto();
    }

    public void setItems() {

        title.setText(property.getTitle());
        description.setText(property.getDescription());
        price.setText(String.valueOf(property.getPrice()) + "€");
        address.setText(property.getAddress());
        room.setText(String.valueOf(property.getRooms()));
        size.setText(String.valueOf(property.getSize()) + "/m2");
        city.setText(property.getZipcode() + ", " + property.getCity() + ", " + property.getProvince());
        category.setText(property.getCategoryId().getName());

    }


    public void performFileSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("image/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    public void changePictureLeft() {
        ctx = this;
        count--;
        if (count < 0) {
            count = property.getPhotos().size() - 1;
        }
        Glide
                .with(ctx)
                .load(property.getPhotos().get(count))
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .into(photo);
    }

    public void changePictureRight() {
        ctx = this;
        if (count >= property.getPhotos().size() - 1) {
            count = 0;
        } else {
            count++;
        }
        Glide
                .with(ctx)
                .load(property.getPhotos().get(count))
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .into(photo);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMinZoomPreference(10);
        String loc = property.getLoc();
        String[] locs = loc.split(",");
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

    public void uploadPhoto() {

        try {
            InputStream inputStream = getContentResolver().openInputStream(uriSelected);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            int cantBytes;
            byte[] buffer = new byte[1024 * 4];

            while ((cantBytes = bufferedInputStream.read(buffer, 0, 1024 * 4)) != -1) {
                baos.write(buffer, 0, cantBytes);
            }


            RequestBody requestFile =
                    RequestBody.create(
                            MediaType.parse(getContentResolver().getType(uriSelected)), baos.toByteArray());


            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("photo", "photo", requestFile);

            RequestBody propertyId = RequestBody.create(MultipartBody.FORM, property.getId());

            PhotoService servicePhoto = ServiceGenerator.createService(PhotoService.class, jwt, AuthType.JWT);
            Call<PhotoUploadResponse> callPhoto = servicePhoto.upload(body, propertyId);
            callPhoto.enqueue(new Callback<PhotoUploadResponse>() {
                @Override
                public void onResponse(Call<PhotoUploadResponse> call, Response<PhotoUploadResponse> response) {

                    if (response.isSuccessful()) {
                        property.getPhotos().add(response.body().getId());
                        Log.d("Uploaded", "Éxito");
                        Log.d("Uploaded", response.body().toString());
                        System.out.println(response.code());

                    } else {
                        Log.e("Upload error", response.errorBody().toString());
                    }

                }

                @Override
                public void onFailure(Call<PhotoUploadResponse> call, Throwable t) {
                    Log.e("Upload error", t.getMessage());
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Trato de comprobar si el inmueble en el que se ha pulsado se encuentra entre los que el usuario logueado
     * ha publicado, para que si lo es, enseñe los botones de añadir y eliminar fotos al inmueble, y si no lo es
     * que no pueda hacerlo
     */
    public void checkOwnerPhotos() {
        service = ServiceGenerator.createService(PropertyService.class, jwt, AuthType.JWT);
        Call<ResponseContainer<PropertyFavsResponse>> callProp = service.getMine();
        callProp.enqueue(new Callback<ResponseContainer<PropertyFavsResponse>>() {
            @Override
            public void onResponse(Call<ResponseContainer<PropertyFavsResponse>> call, Response<ResponseContainer<PropertyFavsResponse>> response) {
                if (response.isSuccessful()) {
                    for (PropertyFavsResponse propertyMine : response.body().getRows()) {
                        System.out.println(propertyMine.getId());
                        System.out.println(property.getId());
                        if (propertyMine.getId().equals(property.getId())) {
                            Log.d("ok", "ok");
                        } else {
                            /*addPhoto.setImageDrawable(null);
                            deletePhoto.setImageDrawable(null);*/
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseContainer<PropertyFavsResponse>> call, Throwable t) {

            }
        });
    }

}
