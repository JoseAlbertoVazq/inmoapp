package dam.javazquez.inmoapp.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.Map;

import dam.javazquez.inmoapp.R;
import dam.javazquez.inmoapp.responses.PropertyResponse;
import dam.javazquez.inmoapp.responses.ResponseContainer;
import dam.javazquez.inmoapp.retrofit.generator.ServiceGenerator;
import dam.javazquez.inmoapp.retrofit.services.PropertyService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TODO = "";
    FusedLocationProviderClient fusedLocationClient;
    private GoogleMap mMap;
    private PropertyService service;
    private Map options = new HashMap();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Intent i = getIntent();
        options = (Map) i.getSerializableExtra("options");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latlng = new LatLng(37.3803677,-6.0071807999999995);
        service = ServiceGenerator.createService(PropertyService.class);
        Call<ResponseContainer<PropertyResponse>> callGeo = service.listGeo(options);
        callGeo.enqueue(new Callback<ResponseContainer<PropertyResponse>>() {
            @Override
            public void onResponse(Call<ResponseContainer<PropertyResponse>> call, Response<ResponseContainer<PropertyResponse>> response) {
                if (response.isSuccessful()){
                    for(PropertyResponse prop : response.body().getRows()){
                        // Spliteo la localización del inmueble en dos
                        System.out.println(prop.getLoc());
                        if(prop.getLoc() == null){


                            String lat = "37.3803677";
                            String lon = "-6.0071807999999995";
                            System.out.println(lat);
                            System.out.println(lon);
                            //La convierto a objeto LatLng para que map pueda trabajar con él, parseando cada parte a double
                            LatLng loc = new LatLng(Double.parseDouble(lat),Double.parseDouble(lon));
                            mMap.addMarker(new MarkerOptions().position(loc).title("Marker in "+prop.getAddress()));
                        }else {
                            String[] parts = prop.getLoc().split(",");
                            if (parts.length == 1) {
                                String lat = "37.3803677";
                                String lon = "-6.0071807999999995";
                                LatLng loc = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
                                mMap.addMarker(new MarkerOptions().position(loc).title("Marker in " + prop.getAddress()));
                            } else {
                                String lat = parts[0];
                                String lon = parts[1];
                                System.out.println(lat);
                                System.out.println(lon);
                                //La convierto a objeto LatLng para que map pueda trabajar con él, parseando cada parte a double
                                LatLng loc = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
                                mMap.addMarker(new MarkerOptions().position(loc).title("Marker in " + prop.getAddress()));
                            }
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseContainer<PropertyResponse>> call, Throwable t) {

            }
        });
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
    }

    public String getCurrentLocation() {
        final String[] currentLoc = new String[1];
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return TODO;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            // convierto la cadena a longitud + latitud
                            currentLoc[0] = location.getLongitude() + "," + location.getLatitude();
                        }
                    }
                });

        return currentLoc[0];
    }
}
