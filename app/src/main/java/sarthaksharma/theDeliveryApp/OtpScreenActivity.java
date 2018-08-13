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
import android.widget.TextView;

public class OtpScreenActivity extends AppCompatActivity {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    TextView lblOtpSummary;

    String strPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#201a32"));
        }

        lblOtpSummary = (TextView) findViewById(R.id.lblOtpSummary);
        preferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = preferences.edit();

        strPhoneNumber = preferences.getString("FirstMobileNumber", null);

        if (strPhoneNumber != null) {
            lblOtpSummary.setText("We have sent a 4-digit code Code on +91 " + strPhoneNumber + ".");
        }
    }

    public void fncHomePageNavigation(View view) {
        if (!isNetworkAvailable()) {
            Intent intent = new Intent(OtpScreenActivity.this, NoConnectionActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(OtpScreenActivity.this, HomeScreenActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
