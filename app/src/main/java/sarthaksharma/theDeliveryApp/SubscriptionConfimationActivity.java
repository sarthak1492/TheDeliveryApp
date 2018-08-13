package sarthaksharma.theDeliveryApp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class SubscriptionConfimationActivity extends AppCompatActivity {

    TextView lblConfirmationSummary;
    SharedPreferences sharedPreferences;
    String strUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_confimation);
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        strUserName = sharedPreferences.getString("name", null);

        lblConfirmationSummary = (TextView) findViewById(R.id.lblConfirmationSummary);
        lblConfirmationSummary.setText("Hey " + strUserName + "! " + getApplicationContext().getString(R.string.confirmationSummary));
    }
}
