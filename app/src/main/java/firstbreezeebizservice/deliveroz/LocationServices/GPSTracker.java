package firstbreezeebizservice.deliveroz.LocationServices;

/**
 * Created by Sarthak.Sharma on 24-10-2017.
 */

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Sarthak.Sharma on 24-10-2017.
 */

public class GPSTracker implements LocationListener {

    Context context;

    public GPSTracker(Context context) {
        this.context = context;
    }

    public Location getLocation() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(context, "Permission not granted", Toast.LENGTH_LONG).show();
            return null;
        }
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);
        Location location = null;
        if (providers.size() > 0) {
            for (String provider: providers){
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 10, this);
                location = lm.getLastKnownLocation(provider);
            }

            return location;

        } else {
            Toast.makeText(context, "Please enable GPS", Toast.LENGTH_LONG).show();
        }

        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
