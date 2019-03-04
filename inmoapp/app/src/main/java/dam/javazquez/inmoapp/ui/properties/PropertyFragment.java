package dam.javazquez.inmoapp.ui.properties;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;

import dam.javazquez.inmoapp.R;
import dam.javazquez.inmoapp.responses.PropertyResponse;
import dam.javazquez.inmoapp.responses.ResponseContainer;
import dam.javazquez.inmoapp.retrofit.generator.AuthType;
import dam.javazquez.inmoapp.retrofit.generator.ServiceGenerator;
import dam.javazquez.inmoapp.retrofit.services.PropertyService;
import dam.javazquez.inmoapp.util.UtilToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {}
 * interface.
 */
public class PropertyFragment extends Fragment {
    private static final String TODO = "";
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    Context ctx;
    List<PropertyResponse> properties = new ArrayList<>();
    String jwt;
    PropertyService service;
    PropertyAdapter adapter;
    Map<String, String> options = new HashMap<>();
    FusedLocationProviderClient fusedLocationClient;
    private static final int NO_FAV_CODE = 0;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    public PropertyFragment(){

    }

    @SuppressLint("ValidFragment")
    public PropertyFragment(Map<String,String> options) {
        this.options = options;
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PropertyFragment newInstance(int columnCount) {
        PropertyFragment fragment = new PropertyFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_properties_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            jwt = UtilToken.getToken(view.getContext());
            options.put("near", "-6.0071807999999995,37.3803677");
            options.put("max_distance","1000000000000");
            options.put("min_distance","1");
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            System.out.println(getCurrentLocation(context));

            if (jwt == null) {
                service = ServiceGenerator.createService(PropertyService.class);

                Call<ResponseContainer<PropertyResponse>> call = service.listProperties(options);
                call.enqueue(new Callback<ResponseContainer<PropertyResponse>>() {
                    @Override
                    public void onResponse(Call<ResponseContainer<PropertyResponse>> call, Response<ResponseContainer<PropertyResponse>> response) {
                        if (response.code() != 200) {
                            Toast.makeText(getActivity(), "Error in request", Toast.LENGTH_SHORT).show();
                        } else {
                            properties = response.body().getRows();

                            adapter = new PropertyAdapter(NO_FAV_CODE, context, properties, mListener);
                            recyclerView.setAdapter(adapter);

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseContainer<PropertyResponse>> call, Throwable t) {
                        Log.e("NetworkFailure", t.getMessage());
                        Toast.makeText(getActivity(), "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                service = ServiceGenerator.createService(PropertyService.class, jwt, AuthType.JWT);

                Call<ResponseContainer<PropertyResponse>> call = service.listPropertiesAuth(options);
                call.enqueue(new Callback<ResponseContainer<PropertyResponse>>() {
                    @Override
                    public void onResponse(Call<ResponseContainer<PropertyResponse>> call, Response<ResponseContainer<PropertyResponse>> response) {
                        if (response.code() != 200) {
                            Toast.makeText(getActivity(), "Error in request", Toast.LENGTH_SHORT).show();
                        } else {
                            properties = response.body().getRows();

                            adapter = new PropertyAdapter(NO_FAV_CODE,context, properties, mListener);
                            recyclerView.setAdapter(adapter);

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseContainer<PropertyResponse>> call, Throwable t) {
                        Log.e("NetworkFailure", t.getMessage());
                        Toast.makeText(getActivity(), "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        return view;
    }


        @Override
        public void onAttach (Context context){
            super.onAttach(context);
            if (context instanceof OnListFragmentInteractionListener) {
                mListener = (OnListFragmentInteractionListener) context;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement OnListFragmentInteractionListener");
            }
        }

        @Override
        public void onDetach () {
            super.onDetach();
            mListener = null;
        }

        /**
         * This interface must be implemented by activities that contain this
         * fragment to allow an interaction in this fragment to be communicated
         * to the activity and potentially other fragments contained in that
         * activity.
         * <p/>
         * See the Android Training lesson <a href=
         * "http://developer.android.com/training/basics/fragments/communicating.html"
         * >Communicating with Other Fragments</a> for more information.
         */
        public interface OnListFragmentInteractionListener {
            // TODO: Update argument type and name
            void onListFragmentInteraction(PropertyResponse item);
        }

    public String getCurrentLocation(Context context) {
        final String[] currentLoc = new String[1];
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
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
