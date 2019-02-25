package dam.javazquez.inmoapp.ui.login;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import dam.javazquez.inmoapp.R;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, SignUpFragment.OnFragmentInteractionListener {

    FragmentTransaction fragmentChanger;
    Fragment login, signup;
    Button signupButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = new LoginFragment();
        signup = new SignUpFragment();

        fragmentChanger = getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_login, login);
        fragmentChanger.commit();



    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
