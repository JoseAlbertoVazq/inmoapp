package dam.javazquez.inmoapp.ui.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import dam.javazquez.inmoapp.R;
import dam.javazquez.inmoapp.responses.PropertyFavsResponse;
import dam.javazquez.inmoapp.responses.PropertyResponse;
import dam.javazquez.inmoapp.ui.addProperty.AddPropertyActivity;
import dam.javazquez.inmoapp.ui.favs.PropertyFavFragment;
import dam.javazquez.inmoapp.ui.login.LoginActivity;
import dam.javazquez.inmoapp.ui.map.MapsActivity;
import dam.javazquez.inmoapp.ui.mines.MyPropertyFragment;
import dam.javazquez.inmoapp.ui.properties.PropertyFragment;
import dam.javazquez.inmoapp.util.UtilToken;

public class DashboardActivity extends AppCompatActivity implements PropertyFragment.OnListFragmentInteractionListener, PropertyFavFragment.OnListFragmentInteractionListener, MyPropertyFragment.OnListFragmentInteractionListener {
    private TextView mTextMessage;
    FragmentTransaction fragmentChanger;
    EditText rooms, city, province, address, zipcode, min_price, max_price, min_size, max_size;
    int frag = 0;
    Map<String, String> options = new HashMap<>();
    private BottomNavigationView mBottomNavigationMenu;
    private Fragment properties, favs, mines;
    private String jwt;
    private FloatingActionButton fab, search, map;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {

        if (jwt != null) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    frag = 0;
                    //  mTextMessage.setText(R.string.title_home);
                    fragmentChanger = getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_dashboard, properties);
                    fragmentChanger.commit();
                    return true;

                case R.id.navigation_favs:
                    frag = 1;
                    //mTextMessage.setText(R.string.title_favs);
                    fragmentChanger = getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_dashboard, favs);
                    fragmentChanger.commit();
                    return true;
                case R.id.navigation_mylist:
                    frag = 2;
                    //mTextMessage.setText(R.string.title_mylist);
                    fragmentChanger = getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_dashboard, mines);
                    fragmentChanger.commit();
                    return true;
                case R.id.navigation_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.title_add).setMessage(R.string.message_logout);
                builder.setPositiveButton(R.string.ok, (dialog, which) -> doLogout()
                );
                builder.setNegativeButton(R.string.cancel, (dialog, id) -> {
                    Log.d("Back", "Going back");
                });
                AlertDialog dialog = builder.create();

                dialog.show();
                return true;

            }
        } else {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    frag = 0;
                    //    mTextMessage.setText(R.string.title_home);
                    fragmentChanger = getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_dashboard, properties);
                    fragmentChanger.commit();

                    return true;

                case R.id.navigation_login:
                    //      mTextMessage.setText(R.string.title_login);
                    startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                    return true;
            }

        }

        return false;
    };



    public void doLogout(){
        UtilToken.clearAll(this);
        startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToken();
        setContentView(R.layout.activity_dashboard);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        fab = findViewById(R.id.fab);
        search = findViewById(R.id.searchFab);
        map = findViewById(R.id.mapFab);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        properties = new PropertyFragment();
        favs = new PropertyFavFragment();
        mines = new MyPropertyFragment();
        mBottomNavigationMenu = findViewById(R.id.nav_view);
        setVisibleFalseDependsJwt();
        fab.setOnClickListener(v -> {
            if (jwt == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.title_add).setMessage(R.string.message_add);
                builder.setPositiveButton(R.string.go, (dialog, which) ->
                        startActivity(new Intent(DashboardActivity.this, LoginActivity.class)));
                builder.setNegativeButton(R.string.cancel, (dialog, id) -> {
                    Log.d("Back", "Going back");
                });
                AlertDialog dialog = builder.create();

                dialog.show();
            } else {
                startActivity(new Intent(DashboardActivity.this, AddPropertyActivity.class));
            }
        });

        search.setOnClickListener(v -> searchOptions());
        map.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, MapsActivity.class));
        });
    }

    public void setVisibleFalseDependsJwt() {
        if (jwt == null) {
            mBottomNavigationMenu.getMenu().removeItem(R.id.navigation_favs);
            mBottomNavigationMenu.getMenu().removeItem(R.id.navigation_mylist);
            mBottomNavigationMenu.getMenu().removeItem(R.id.navigation_logout);
        } else {
            mBottomNavigationMenu.getMenu().removeItem(R.id.navigation_login);
        }
    }

    public void getToken() {
        jwt = UtilToken.getToken(getApplicationContext());
    }


    public void searchOptions () {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ResourceType")
        View dialogLayout = inflater.inflate(R.layout.activity_search, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogLayout);

        rooms = dialogLayout.findViewById(R.id.search_rooms);
        city = dialogLayout.findViewById(R.id.search_city);
        province = dialogLayout.findViewById(R.id.search_province);
        zipcode = dialogLayout.findViewById(R.id.search_zipcode);
        address = dialogLayout.findViewById(R.id.search_address);
        min_price = dialogLayout.findViewById(R.id.search_min_price);
        max_price = dialogLayout.findViewById(R.id.search_max_price);
        min_size = dialogLayout.findViewById(R.id.search_min_size);
        max_size = dialogLayout.findViewById(R.id.search_max_size);
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            if(frag == 0){
/*                if(rooms.getText().toString() == null) {
                    rooms.setText("");
                } else if (city.getText().toString() == null){
                    city.setText("");
                } else if (province.getText().toString() == null){
                    province.setText("");
                } else if(zipcode.getText().toString() == null) {
                    zipcode.setText("");
                } else if (address.getText().toString() == null) {
                    zipcode.setText("");
                } else if (min_price.getText().toString() == null){
                    address.setText("");
                } else if(max_price.getText().toString() == null) {
                    max_price.setText("");
                } else if (min_size.getText().toString() == null) {
                    min_size.setText("");
                } else if (max_size.getText().toString() == null){
                    max_size.setText("");
                }*/
/*                options.put("rooms", rooms.getText().toString());
                options.put("city", city.getText().toString());
                options.put("province", province.getText().toString());
                options.put("zipcode", zipcode.getText().toString());
                options.put("address", address.getText().toString());
                options.put("min_price", min_price.getText().toString());
                options.put("max_price", max_price.getText().toString());
                options.put("min_size", min_size.getText().toString());
                options.put("max_size", max_size.getText().toString());*/
                if(rooms.getText().toString()!=""){
                    options.put("rooms", rooms.getText().toString());
                } else if(city.getText().toString()!=""){
                    options.put("city", city.getText().toString());
                } else if (province.getText().toString()!="") {
                    options.put("province", province.getText().toString());
                } else if (zipcode.getText().toString()!= ""){
                    options.put("zipcode", zipcode.getText().toString());
                } else if (address.getText().toString()!= ""){
                    options.put("address", address.getText().toString());
                } else if (min_price.getText().toString() != "") {
                    options.put("min_price", min_price.getText().toString());
                } else if (max_price.getText().toString()!=""){
                    options.put("max_price", max_price.getText().toString());
                } else if(min_size.getText().toString() != "") {
                    options.put("min_size", min_size.getText().toString());
                } else if(max_size.getText().toString()!= ""){
                    options.put("max_size", max_size.getText().toString());
                }
                System.out.println(options.values());
                properties = new PropertyFragment(options);
                fragmentChanger = getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_dashboard, properties);
                fragmentChanger.commit();
            }
        });
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {
            Log.d("Back", "Going back");
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    @Override
    public void onListFragmentInteraction(PropertyResponse item) {

    }


    @Override
    public void onListFragmentInteraction(PropertyFavsResponse item) {

    }
}
