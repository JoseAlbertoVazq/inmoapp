package dam.javazquez.inmoapp.ui.login;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import dam.javazquez.inmoapp.R;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, SignUpFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
