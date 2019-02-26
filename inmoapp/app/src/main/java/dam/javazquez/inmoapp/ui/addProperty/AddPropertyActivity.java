package dam.javazquez.inmoapp.ui.addProperty;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Map;

import dam.javazquez.inmoapp.R;
import dam.javazquez.inmoapp.util.data.GeographySpain;
import dam.javazquez.inmoapp.util.geography.GeographyListener;
import dam.javazquez.inmoapp.util.geography.GeographySelector;

public class AddPropertyActivity extends FragmentActivity
        implements View.OnClickListener, GeographyListener {

    private EditText title, description, price, size, zipcode, address;

    private TextView tvRegion;
    private TextView tvProvincia;
    private TextView tvMunicipio;

    private Button btProbar, btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);

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
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btProbar){
            GeographySelector gs = new GeographySelector(this);
            gs.setOnGeograpySelectedListener(this);
            FragmentManager fm = getSupportFragmentManager();
            gs.show(fm, "geographySelector");
        }

    }

    @Override
    public void onGeographySelected(Map<String, String> hm) {
        tvRegion.setText(hm.get(GeographySpain.REGION));
        tvProvincia.setText(hm.get(GeographySpain.PROVINCIA));
        tvMunicipio.setText(hm.get(GeographySpain.MUNICIPIO));


    }
}
