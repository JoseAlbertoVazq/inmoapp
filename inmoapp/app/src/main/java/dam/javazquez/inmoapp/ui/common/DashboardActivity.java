package dam.javazquez.inmoapp.ui.common;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import dam.javazquez.inmoapp.R;
import dam.javazquez.inmoapp.responses.PropertyFavsResponse;
import dam.javazquez.inmoapp.responses.PropertyResponse;
import dam.javazquez.inmoapp.ui.addProperty.AddPropertyActivity;
import dam.javazquez.inmoapp.ui.favs.PropertyFavFragment;
import dam.javazquez.inmoapp.ui.login.LoginActivity;
import dam.javazquez.inmoapp.ui.mines.MyPropertyFragment;
import dam.javazquez.inmoapp.ui.properties.PropertyFragment;
import dam.javazquez.inmoapp.util.UtilToken;

public class DashboardActivity extends AppCompatActivity implements PropertyFragment.OnListFragmentInteractionListener, PropertyFavFragment.OnListFragmentInteractionListener, MyPropertyFragment.OnListFragmentInteractionListener {
    private TextView mTextMessage;
    FragmentTransaction fragmentChanger;
    private BottomNavigationView mBottomNavigationMenu;
    private Fragment properties, favs, mines;
    private String jwt;
    private FloatingActionButton fab;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {

                if(jwt!=null) {

                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                          //  mTextMessage.setText(R.string.title_home);
                            fragmentChanger = getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_dashboard, properties);
                            fragmentChanger.commit();
                            return true;

                        case R.id.navigation_favs:
                            //mTextMessage.setText(R.string.title_favs);
                            fragmentChanger = getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_dashboard, favs);
                            fragmentChanger.commit();
                            return true;
                        case R.id.navigation_mylist:
                            //mTextMessage.setText(R.string.title_mylist);
                            fragmentChanger = getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_dashboard, mines);
                            fragmentChanger.commit();
                            return true;

                    }
                }
                else {

                    switch (item.getItemId()) {
                        case R.id.navigation_home:
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToken();
        setContentView(R.layout.activity_dashboard);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        fab = findViewById(R.id.fab);
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
            }
        });
    }

    public void setVisibleFalseDependsJwt() {
        if (jwt == null) {
            mBottomNavigationMenu.getMenu().removeItem(R.id.navigation_favs);
            mBottomNavigationMenu.getMenu().removeItem(R.id.navigation_mylist);
        } else {
            mBottomNavigationMenu.getMenu().removeItem(R.id.navigation_login);
        }
    }
    public void getToken(){
        jwt = UtilToken.getToken(getApplicationContext());
    }

    @Override
    public void onListFragmentInteraction(PropertyResponse item) {

    }

    @Override
    public void onListFragmentInteraction(PropertyFavsResponse item) {

    }
}
