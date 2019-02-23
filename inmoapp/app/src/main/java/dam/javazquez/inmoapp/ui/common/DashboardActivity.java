package dam.javazquez.inmoapp.ui.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.TextView;

import dam.javazquez.inmoapp.R;
import dam.javazquez.inmoapp.ui.login.LoginActivity;
import dam.javazquez.inmoapp.util.UtilToken;

public class DashboardActivity extends AppCompatActivity {
    private TextView mTextMessage;

    private String jwt = null;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            if(jwt!=null) {

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        mTextMessage.setText(R.string.title_home);
                        return true;

                    case R.id.navigation_favs:
                        mTextMessage.setText(R.string.title_favs);
                        return true;
                    case R.id.navigation_mylist:
                        mTextMessage.setText(R.string.title_mylist);
                        return true;

                }
            }
            else {

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        mTextMessage.setText(R.string.title_home);
                        return true;

                    case R.id.navigation_login:
                        mTextMessage.setText(R.string.title_login);
                        startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                        return true;
                }

            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToken();
        setContentView(R.layout.activity_dashboard);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void getToken(){
        jwt = UtilToken.getToken(getApplicationContext());
    }

}
