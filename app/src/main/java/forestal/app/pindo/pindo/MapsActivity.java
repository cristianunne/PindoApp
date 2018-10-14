package forestal.app.pindo.pindo;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import utillities.entidades.ListaParcelasRelevamiento;
import utillities.entidades.ParcelasRelevamientoSQLiteEntity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String geometria;
    private String latitud = null;
    private String longitud = null;
    private String rodal;
    private ArrayList<ParcelasRelevamientoSQLiteEntity> lista;

    private String operacion;

    private JSONObject geojson;
    private JSONObject geojson_rodal = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps3);

        //try {
            Bundle parametros = getIntent().getExtras();

            this.operacion = parametros.getString("operacion");

            ArrayList<String> lista = getIntent().getStringArrayListExtra("lista");

            if (this.operacion.equals("parcelas_all")){

                String rodal_id = parametros.getString("rodal_id");

                //Traigo la lista de Parcelas
                getDataOfListaParcelasRelevamiento(rodal_id);

                //ACA DEBO CONTROLAR QUE VENGAN LOS DATOS DEL RODAL

                this.latitud = parametros.getString("centroid_lat");
                this.longitud = parametros.getString("centroid_longi");
                this.geometria = parametros.getString("rodal");



            } else if (this.operacion.equals("parcelas")){

                //cargo el geojsonrodal
                this.geometria = parametros.getString("rodal");
                this.latitud = parametros.getString("centroid_lat");
                this.longitud = parametros.getString("centroid_longi");
            }

            try{
                geojson_rodal = new JSONObject(this.geometria);

            } catch (NullPointerException e){
                e.printStackTrace();
            } catch (JSONException ex){
                ex.printStackTrace();
            }



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_);
        mapFragment.getMapAsync(this);


    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        UiSettings uisetting = mMap.getUiSettings();
        uisetting.setZoomControlsEnabled(true);


            if(this.geojson_rodal != null){
                GeoJsonLayer layer_rodal = new GeoJsonLayer(mMap, this.geojson_rodal);

                for (GeoJsonFeature feature : layer_rodal.getFeatures()) {
                    GeoJsonPolygonStyle style = new GeoJsonPolygonStyle();
                    style.setStrokeColor(Color.rgb(255, 230, 51));
                    style.setStrokeWidth(2);
                    feature.setPolygonStyle(style);
                }
                layer_rodal.addLayerToMap();
            }


          if (this.operacion.equals("parcelas_all")){
              LatLng coord = null;
              for (int i = 0; i < getLista().size(); i++){

                  ParcelasRelevamientoSQLiteEntity parce = getLista().get(i);
                  Double lat = parce.getLat();
                  Double longi = parce.getLongi();
                  String titulo = "Parcela " + parce.getIdparcela().toString();


                  coord = new LatLng(lat, longi);
                  mMap.addMarker(new MarkerOptions().position(coord).title(titulo).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
              }

              if(!this.latitud.equals("NO") && !this.longitud.equals("NO")){
                  Double lat_ = Double.valueOf(this.latitud);
                  Double long_ = Double.valueOf(this.longitud);
                  LatLng centroid_Cam = new LatLng(lat_, long_);

                  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centroid_Cam, 15));
              } else {
                  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 13));
              }



          } else if (this.operacion.equals("parcelas")){

              Double lat_ = Double.valueOf(this.latitud);
              Double long_ = Double.valueOf(this.longitud);

              String titulo = "Parcela";

              LatLng centroid_Cam = new LatLng(lat_, long_);

              mMap.addMarker(new MarkerOptions().position(centroid_Cam).title(titulo).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
              mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centroid_Cam, 15));
          }

    }

    private void getDataOfListaParcelasRelevamiento(String id_rodal){

        ParcelasRelevamientoSQLiteEntity parc_rel = new ParcelasRelevamientoSQLiteEntity();
        ArrayList<ParcelasRelevamientoSQLiteEntity> lista = parc_rel.getParcelasRelevamientoSelectByIdRodal(getApplicationContext(), id_rodal);
        Integer cant = lista.size();
        setLista(lista);
    }


    public ArrayList<ParcelasRelevamientoSQLiteEntity> getLista() {
        return lista;
    }

    public void setLista(ArrayList<ParcelasRelevamientoSQLiteEntity> lista) {
        this.lista = lista;
    }
}
