package forestal.app.pindo.pindo.navegador;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import forestal.app.pindo.pindo.R;
import utillities.entidades.ParcelasRelevamientoSQLiteEntity;
import utillities.entidades.RodalesRelevamiento;
import utillities.entidades.RodalesSincronizedEntity;

public class NavegadorActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegador);

        int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getApplicationContext());

        if (status == ConnectionResult.SUCCESS){
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map_navegador);
            mapFragment.getMapAsync(this);
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, (Activity)getApplicationContext(), 10);
            dialog.show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        setMap(googleMap);
        getMap().setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        UiSettings uisetting = getMap().getUiSettings();
        uisetting.setZoomControlsEnabled(true);

        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }

        getMap().setMyLocationEnabled(true);

        Location myLoc = getMiPosition();
        if(myLoc != null){

            double lat = myLoc.getLatitude();
            double longi = myLoc.getLongitude();
            LatLng mypos = new LatLng(lat, longi);

            float zoom_level = 16;
            getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(mypos, zoom_level));
        }
        drawParcelas();



    }

    public Location getMiPosition() {
        Location location = null;

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }

        try{

            location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

        } catch (NullPointerException e){
            return null;
        }



        return location;
    }


    private void drawParcelas(){

        //DIBUJO TODAS LAS PARCELAS DISPONIBLES Y LAS CLASIFICO POR EL RODAL

        RodalesRelevamiento rodalesRelevamiento = new RodalesRelevamiento();

        ArrayList<RodalesRelevamiento> listaRodales = rodalesRelevamiento.getListaRodalesSelect(getApplicationContext());

        if(listaRodales != null){

            for (RodalesRelevamiento rodal : listaRodales){

                if(rodal.getGeometry() != null){

                    try {
                        JSONObject geojson = new JSONObject(rodal.getGeometry());

                        GeoJsonLayer layer_rodal = new GeoJsonLayer(getMap(), geojson);

                        int red = getNumeroRandom(0, 255);
                        int gree = getNumeroRandom(0, 255);
                        int blue = getNumeroRandom(0, 255);


                        for (GeoJsonFeature feature : layer_rodal.getFeatures()) {
                            GeoJsonPolygonStyle style = new GeoJsonPolygonStyle();
                            style.setStrokeColor(Color.rgb(red, gree, blue));
                            style.setStrokeWidth(2);
                            feature.setPolygonStyle(style);
                        }
                        layer_rodal.addLayerToMap();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        //dibujo las Parcelas

        ParcelasRelevamientoSQLiteEntity parcelas = new ParcelasRelevamientoSQLiteEntity();

        ArrayList<ParcelasRelevamientoSQLiteEntity> listaParcelas = parcelas.getAllParcelasRelevamiento(getApplicationContext());

        if(listaParcelas != null){

            for (ParcelasRelevamientoSQLiteEntity parcela : listaParcelas){

                // Add a marker in Sydney and move the camera
                LatLng parc_ = new LatLng(parcela.getLat(), parcela.getLongi());
                Marker marker = getMap().addMarker(new MarkerOptions().position(parc_).title("Rodal N°: " + parcela.getIdrodales().toString() + ", Parcela Id N° " + parcela.getId().toString()
                        + ", Parcela Id Sinc N° " + parcela.getIdpostgres()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


            }

        }


    }



    @SuppressLint("NewApi")
    private int getNumeroRandom(int min, int max){
        return ThreadLocalRandom.current().nextInt(min, max);
    }



    public GoogleMap getMap() {
        return map;
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }
}
