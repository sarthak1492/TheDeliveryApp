package sarthaksharma.theDeliveryApp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;
import java.util.Locale;

import sarthaksharma.theDeliveryApp.Listeners.RecyclerItemClickListener;
import sarthaksharma.theDeliveryApp.LocationServices.GPSTracker;

public class LocationScreenActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    protected GoogleApiClient mGoogleApiClient;

    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(new LatLng(-0, 0), new LatLng(0, 0));

    private PlaceAutoCompleteAdapter mAutoCompleteAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    ProgressBar pbLocationFetch;
    ImageView imgCurrentLocation;

    TextView lblSelectLocaton;
    private EditText txtNewLocation;
    Button btnApplyLocation;


    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    Button btnAddNewAddress;

    String strNewLocation, strNewSearchedLocation, strNewCurrentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();
        setContentView(sarthaksharma.theDeliveryApp.R.layout.activity_location_screen);
        Toolbar toolbar = (Toolbar) findViewById(sarthaksharma.theDeliveryApp.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#201a32"));
        }

        this.getSupportActionBar().setHomeAsUpIndicator(sarthaksharma.theDeliveryApp.R.drawable.ic_action_close);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lblSelectLocaton = (TextView) findViewById(sarthaksharma.theDeliveryApp.R.id.lblSelectLocation);

        txtNewLocation = (EditText) findViewById(sarthaksharma.theDeliveryApp.R.id.txtNewLocation);



        mAutoCompleteAdapter = new PlaceAutoCompleteAdapter(this, sarthaksharma.theDeliveryApp.R.layout.searchview_adapter,
                mGoogleApiClient, BOUNDS_INDIA, null);

        preferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = preferences.edit();

        mRecyclerView = (RecyclerView) findViewById(sarthaksharma.theDeliveryApp.R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAutoCompleteAdapter);

        imgCurrentLocation = (ImageView) findViewById(sarthaksharma.theDeliveryApp.R.id.imgCurrentLocation);
        pbLocationFetch = (ProgressBar) findViewById(sarthaksharma.theDeliveryApp.R.id.pbLocationFetch);

        pbLocationFetch.setVisibility(View.INVISIBLE);
        imgCurrentLocation.setVisibility(View.VISIBLE);


        btnApplyLocation = (Button) findViewById(sarthaksharma.theDeliveryApp.R.id.btnApplyLocation);
        btnApplyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{

                    if (strNewCurrentLocation != null){
                        strNewLocation = null;
                        strNewLocation = strNewCurrentLocation;

                        Intent intent = new Intent(LocationScreenActivity.this, EditProfileScreenActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("newLocation1", strNewLocation);
                        intent.putExtras(bundle);
                        editor.remove("newLocation");
                        editor.commit();
                        startActivity(intent);
                        finish();

                    } else if (strNewSearchedLocation != null){
                        strNewLocation = null;
                        strNewLocation = strNewSearchedLocation;

                        Intent intent = new Intent(LocationScreenActivity.this, EditProfileScreenActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("newLocation1", strNewLocation);
                        intent.putExtras(bundle);
                        editor.remove("newLocation");
                        editor.commit();
                        startActivity(intent);
                        finish();

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        txtNewLocation.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                    mAutoCompleteAdapter.getFilter().filter(s.toString());
                } else if (!mGoogleApiClient.isConnected()) {
                    Toast.makeText(getApplicationContext(), Constants.API_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
                    Log.e(Constants.PlacesTag, Constants.API_NOT_CONNECTED);
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        final PlaceAutoCompleteAdapter.PlaceAutocomplete item = mAutoCompleteAdapter.getItem(position);
                        final String placeId = String.valueOf(item.placeId);

                        /*
                             Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                         */

                        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
                        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(PlaceBuffer places) {
                                if (places.getCount() == 1) {
                                    //Do the things here on Click.....
                                    txtNewLocation.setText(places.get(0).getAddress());
                                    lblSelectLocaton.setText(places.get(0).getName());
                                    strNewSearchedLocation = String.valueOf(places.get(0).getName());
                                } else {
                                    Toast.makeText(getApplicationContext(), Constants.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        Log.i("TAG", "Clicked: " + item.description);
                        Log.i("TAG", "Called getPlaceById to get Place details for " + item.placeId);
                    }
                })
        );

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

    public void fncAddNewAddress(View view){
        Intent intent = new Intent(LocationScreenActivity.this, NewAddressScreenActivity.class);
        startActivity(intent);
    }



    public void fncGetCurrentLocation(View view) {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION}, 99);

                GPSTracker services = new GPSTracker(getApplicationContext());
                Location location = services.getLocation();

                if (location != null) {

                    imgCurrentLocation.setVisibility(View.INVISIBLE);
                    pbLocationFetch.setVisibility(View.VISIBLE);

                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();


                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    try {
                        final List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
                        if (addresses.size() > 0){

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    pbLocationFetch.setVisibility(View.INVISIBLE);
                                    imgCurrentLocation.setVisibility(View.VISIBLE);
                                    lblSelectLocaton.setText(addresses.get(0).getSubLocality());
                                    strNewCurrentLocation = addresses.get(0).getSubLocality();

                                }
                            }, 2000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 99);
        }

        ActivityCompat.requestPermissions(LocationScreenActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 99: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        GPSTracker services = new GPSTracker(getApplicationContext());
                        Location location = services.getLocation();
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            imgCurrentLocation.setVisibility(View.INVISIBLE);
                            pbLocationFetch.setVisibility(View.VISIBLE);

                            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                            try {
                                final List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
                                if (addresses.size() > 0){
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            pbLocationFetch.setVisibility(View.INVISIBLE);
                                            imgCurrentLocation.setVisibility(View.VISIBLE);
                                            lblSelectLocaton.setText(addresses.get(0).getSubLocality());
                                            strNewCurrentLocation = addresses.get(0).getSubLocality();

                                        }
                                    }, 2000);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

                }

                return;
            }

        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v("Google API Callback", "Connection Done");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v("Google API Callback", "Connection Suspended");
        Log.v("Code", String.valueOf(i));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v("Google API Callback", "Connection Failed");
        Log.v("Error Code", String.valueOf(connectionResult.getErrorCode()));
        Toast.makeText(this, Constants.API_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
            Log.v("Google API", "Connecting");
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            Log.v("Google API", "Dis-Connecting");
            mGoogleApiClient.disconnect();
        }
    }

}
