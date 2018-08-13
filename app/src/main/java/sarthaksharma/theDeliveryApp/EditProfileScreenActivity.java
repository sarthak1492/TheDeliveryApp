package sarthaksharma.theDeliveryApp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class EditProfileScreenActivity extends AppCompatActivity {

    EditText txtLocation;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    EditText txtName, txtEmailID, txtPhoneNumber;

    String strName, strEmailID, strPhoneNumber, strLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_screen);
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

        txtName = (EditText) findViewById(R.id.txtName);
        strName = preferences.getString("FirstUserName", null);
        if (strName != null){
            txtName.setText(strName);
        }else {
            txtName.setText(preferences.getString("NewUserName", null));
        }


        txtEmailID = (EditText) findViewById(R.id.txtEmailID);
        strEmailID = preferences.getString("NewEmailID", null);
        if (strEmailID != null){
            txtEmailID.setText(strEmailID);
        }

        txtPhoneNumber = (EditText) findViewById(R.id.txtPhoneNumber);
        strPhoneNumber = preferences.getString("FirstMobileNumber", null);
        if (strPhoneNumber != null){
            txtPhoneNumber.setText(strPhoneNumber);
        }else {
            txtPhoneNumber.setText(preferences.getString("NewMobileNumber", null));
        }

        txtLocation = (EditText) findViewById(R.id.txtLocation);
        txtLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileScreenActivity.this, LocationScreenActivity.class);
                startActivity(intent);
            }
        });


        try {
            if(preferences.getString("newLocation", null) != null){
                txtLocation.setText(preferences.getString("newLocation", null));
            }else {
                Bundle bundle = getIntent().getExtras();
                strLocation = bundle.getString("newLocation1", null);
                txtLocation.setText(bundle.getString("newLocation1"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void fncSaveChanges(View view){

        if (!isNetworkAvailable()){
            Intent intent = new Intent(EditProfileScreenActivity.this, NoConnectionActivity.class);
            startActivity(intent);
            finish();
        }else {

            strName = null;
            strEmailID = null;
            strLocation = null;
            strPhoneNumber = null;

            try{
                strName = txtName.getText().toString();
                strEmailID = txtEmailID.getText().toString();
                strLocation = txtLocation.getText().toString();
                strPhoneNumber = txtPhoneNumber.getText().toString();

                editor.putString("FirstUserName", strName);
                editor.putString("NewEmailID", strEmailID);
                editor.putString("newLocation", strLocation);
                editor.putString("FirstMobileNumber", strPhoneNumber);
                editor.commit();


                Intent intent = new Intent(EditProfileScreenActivity.this, ProfileScreenActivity.class);
                startActivity(intent);

            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this, "There was an error saving saving details.", Toast.LENGTH_SHORT).show();
            }
        }
        
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EditProfileScreenActivity.this, ProfileScreenActivity.class);
        startActivity(intent);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null || activeNetworkInfo.isConnected();
    }
}
