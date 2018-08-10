package firstbreezeebizservice.deliveroz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class ProfileScreenActivity extends AppCompatActivity {

    TextView lblEdit;
    TextView lblCityName;
    TextView lblUserName;
    TextView lblPhoneNo;
    TextView lblEmailID;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#201a32"));
        }

        lblEdit = (TextView) findViewById(R.id.lblEdit);
        lblEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()){
                    Intent intent = new Intent(ProfileScreenActivity.this, EditProfileScreenActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(ProfileScreenActivity.this, NoConnectionActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        preferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = preferences.edit();

        lblCityName = (TextView) findViewById(R.id.lblCityName);
        String strLocation = preferences.getString("newLocation", null);

        if (strLocation != null){
            lblCityName.setText(strLocation);
        }else{
            lblCityName.setText("*Specify your location*");
        }

        lblEmailID = (TextView) findViewById(R.id.lblEmailID);
        String strEmailID = preferences.getString("NewEmailID", null);
        if (strEmailID != null){
            lblEmailID.setText(preferences.getString("NewEmailID", null));
        }else{
            lblEmailID.setText("*specify your email id*");
        }

        lblUserName = (TextView) findViewById(R.id.lblUserName);
        String strUserName = preferences.getString("FirstUserName", null);

        if (strUserName != null){
            lblUserName.setText(strUserName);
        }

        lblPhoneNo = (TextView) findViewById(R.id.lblPhoneNo);

        if (preferences.getString("FirstMobileNumber", null) != null){
            lblPhoneNo.setText(preferences.getString("FirstMobileNumber", null));
        }else {
            lblPhoneNo.setText("*mobile number is required*");
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
