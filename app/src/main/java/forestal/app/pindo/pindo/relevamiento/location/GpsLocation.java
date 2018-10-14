package forestal.app.pindo.pindo.relevamiento.location;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class GpsLocation {

    private Double latitud;
    private Double longitud;
    private LocationManager locManager;

    private FragmentActivity activity;


    public GpsLocation(FragmentActivity activity) {

        setActivity(activity);
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }


    public void Location(){

        this.locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            getActivity().startActivity(settingsIntent);
        }

        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(getActivity().getApplicationContext(), "Permiso No Garantizado", Toast.LENGTH_LONG).show();

        }else
        {
            try{
                Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                setLatitud(loc.getLatitude());
                setLongitud(loc.getLongitude());
            } catch (NullPointerException e){
                Toast.makeText(getActivity().getApplicationContext(), "Problemas al obtener la Ubicación.", Toast.LENGTH_LONG).show();
            }

        }
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public FragmentActivity getActivity() {
        return activity;
    }

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }
}
