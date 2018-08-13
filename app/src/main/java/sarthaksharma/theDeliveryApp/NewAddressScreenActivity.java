package sarthaksharma.theDeliveryApp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class NewAddressScreenActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spDeliveryType;
    List<String> arrDeliveryType;
    String strDeliveryType;

    EditText txtFullName, txtMobileNumber, txtPincode, txtHouseNumber, txtLocality, txtLandmark, txtTownCity, txtState;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#201a32"));
        }

        preferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = preferences.edit();

        txtFullName = (EditText) findViewById(R.id.txtFullName);
        txtMobileNumber = (EditText) findViewById(R.id.txtMobileNumber);
        txtPincode = (EditText) findViewById(R.id.txtPincode);
        txtHouseNumber = (EditText) findViewById(R.id.txtHouseNumber);
        txtLocality = (EditText) findViewById(R.id.txtLocality);
        txtLandmark = (EditText) findViewById(R.id.txtLandmark);
        txtTownCity = (EditText) findViewById(R.id.txtTownCity);
        txtState = (EditText) findViewById(R.id.txtState);

        spDeliveryType = (Spinner) findViewById(R.id.spDeliveryType);
        arrDeliveryType = new ArrayList<>();
        spDeliveryType.setOnItemSelectedListener(this);

        arrDeliveryType.add("Selected Delivery Type");
        arrDeliveryType.add("Home");
        arrDeliveryType.add("Office/Commercial");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrDeliveryType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDeliveryType.setAdapter(adapter);

        if (preferences.getString("newLocation", null) != null){
            txtTownCity.setText(preferences.getString("newLocation", null));
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        strDeliveryType = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void fncSaveNewAddress(View view){

    }
}
