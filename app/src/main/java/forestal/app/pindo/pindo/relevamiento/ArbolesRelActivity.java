package forestal.app.pindo.pindo.relevamiento;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.Map;

import forestal.app.pindo.pindo.Fragment;
import forestal.app.pindo.pindo.R;
import forestal.app.pindo.pindo.relevamiento.dialogs.DialogArbolesRelevamientoAdd;
import forestal.app.pindo.pindo.relevamiento.dialogs.DialogArbolesRelevamientoEdit;
import utillities.entidades.ArbolesRelevamientoEntity;
import utillities.entidades.RodalesRelevamiento;
import utillities.entidades.RodalesSincronizedEntity;

import static android.R.color.black;

public class ArbolesRelActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private SupportMapFragment mMapFragment;



    private GoogleMap mMap;
    private ImageButton btn_add_arbol, btn_cleanArboles;
    private Button btn_salir;
    private Context context;
    private Fragment mainActivity;

    private TableLayout tableLayout;

    //Argumentos
    private Integer idrodal;
    private Integer idparcela;
    private Integer idparcela2;
    private Integer idparcelapostgres;

    private Double lat;
    private Double longi;

    ImageButton img_edit, img_delete;

    private boolean alturas_cargadas;

    private JSONObject jsonrodal;

    //Lista de Arboles
    private ArrayList<ArbolesRelevamientoEntity> listaArboles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arboles_rel);

        setAlturas_cargadas(true);

        setContext(this);
        //Instancio el Table Layout
        setTableLayout((TableLayout)findViewById(R.id.tablely));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getApplicationContext());

        if (status == ConnectionResult.SUCCESS) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map_fragment);
            mapFragment.getMapAsync(this);
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, (Activity) getApplicationContext(), 10);
            dialog.show();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Traigo los Argumentos
        Bundle argumentos = getIntent().getExtras();

        if (argumentos != null){

            setIdparcela(argumentos.getInt("idparcela"));
            setIdrodal(argumentos.getInt("idrodal"));
            setIdparcelapostgres(argumentos.getInt("idparcelapostgres"));
            setIdparcela2(argumentos.getInt("idparcela2"));

            //Llamo al metodo del jsonrodal
            if(!setJsonRodal()){
                this.jsonrodal = null;

            }


            //Llamo al metodo que muestra los arboles
            showArboles();


            //obtengo el Boton aRBOL
            btn_add_arbol = (ImageButton)findViewById(R.id.btn_add_arbol);

            btn_add_arbol.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Recupero los datos desde la db
                    ArbolesRelevamientoEntity arbol_entity = new ArbolesRelevamientoEntity();
                    ArrayList<ArbolesRelevamientoEntity> lista_arboles = arbol_entity.getListaArbolesByIdParcela(getContext(), getIdparcela());

                    double promedio = getPromedioDap(listaArboles);



                    addArbol(promedio);
                }
            });


            //Boton eliminar arboles
            btn_cleanArboles = (ImageButton)findViewById(R.id.btn_clean_arbol);

            btn_cleanArboles.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    deleteAllArboles();

                }
            });

            //Boton que sale de la actividad
            btn_salir = (Button)findViewById(R.id.btn_arb_salir);
            btn_salir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Aca compruebo que se hayan cargado las alturas
                    ArbolesRelevamientoEntity arbol_entity = new ArbolesRelevamientoEntity();
                    ArrayList<ArbolesRelevamientoEntity> lista_arboles = arbol_entity.getListaArbolesByIdParcela(getContext(), getIdparcela());

                    if (lista_arboles != null){

                        for (ArbolesRelevamientoEntity arbol : lista_arboles){


                            if (arbol.getAltura() == 0.0 || arbol.getAlturapoda() == 0.0){

                                setAlturas_cargadas(false);

                            }


                        }

                        if (!isAlturas_cargadas()){

                            AlertDialog.Builder builder_res = new AlertDialog.Builder(getContext());
                            View mview = LayoutInflater.from(getContext()).inflate(R.layout.dialog_result_acept_cancel, null, false);
                            //View mview = getLayoutInflater().inflate(R.layout.dialog_result_error, null);
                            builder_res.setView(mview);
                            final AlertDialog dialog_res = builder_res.create();
                            //Seteo el texto de error
                            TextView txt_msj = (TextView)mview.findViewById(R.id.text_dr_title_ac_canc);
                            txt_msj.setText("No se han especificado datos de Altura en algunos de los árboles!");
                            dialog_res.setCanceledOnTouchOutside(false);

                            Button btn_aceptar_dialog_res = (Button)mview.findViewById(R.id.btn_aceptar_dialog_ac_can);
                            btn_aceptar_dialog_res.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    dialog_res.dismiss();
                                    finish();

                                }
                            });

                            Button btn_cancelar_dialog_res = (Button)mview.findViewById(R.id.btn_cancelar_dialog_ac_can);
                            btn_cancelar_dialog_res.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog_res.dismiss();
                                    finish();

                                }
                            });
                            dialog_res.show();

                        } else {
                            finish();
                        }




                    } else {
                        finish();
                    }

                }
            });

        } //Llave del if


    }







    @Override
    public void onBackPressed() {

        //Desactivo para que salga solo por el otro lado
        /*Log.d("mensaje", "onBackPressed: se apreto");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.arboles_rel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.normal) {
            // Handle the camera action
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        } else if (id == R.id.satelite) {

            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        } else if (id == R.id.hibrido) {

            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        } else if (id == R.id.terrain) {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        UiSettings uisetting = mMap.getUiSettings();
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

        mMap.setMyLocationEnabled(true);



        //Agrego la localización actual
        Double lat = 0.0;
        Double longi = 0.0;

        Bundle argumentos = getIntent().getExtras();



        lat = argumentos.getDouble("latitud");
        longi = argumentos.getDouble("longitud");

        setLat(lat);
        setLongi(longi);

        //De inicio llamo al metodo que crea los marcadores
        drawArbolesToMap();

    }




    public void showArboles(){

        ArbolesRelevamientoEntity arbol_entity = new ArbolesRelevamientoEntity();

        if (getIdparcela() != null){

            ArrayList<ArbolesRelevamientoEntity> lista_arboles = arbol_entity.getListaArbolesByIdParcela(this, getIdparcela());

            if (lista_arboles != null){

                SparseIntArray sparseIntArray = null;

                if(lista_arboles.size() >= 3){
                    sparseIntArray = getListaArbolesOrder(lista_arboles);

                    Integer cant = sparseIntArray.size();
                }

                setListaArboles(lista_arboles);
                Integer j = 1;
                for (final ArbolesRelevamientoEntity arbol : lista_arboles){
                    Integer i = 0;

                    //Data Row
                    TableRow rows = new TableRow(this);
                    rows.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 80));

                    String[] coltext = {j.toString(), "", getIdparcela2().toString(), arbol.getMarca(), arbol.getDap().toString(), arbol.getAltura().toString(),
                    arbol.getAlturapoda().toString(), arbol.getDmsm().toString()};
                    int ju = 1;
                    for (String text:coltext){

                        if (ju == 2){
                            //Agrego el boton que debe ser en funcion del promedio
                            TableRow.LayoutParams params_2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 80);

                            LinearLayout contenedor = new LinearLayout(getContext());
                            contenedor.setLayoutParams(params_2);
                            contenedor.setOrientation(LinearLayout.VERTICAL);
                            contenedor.setGravity(Gravity.CENTER);

                            ImageView miImageView = new ImageView(getApplicationContext());


                            if(sparseIntArray != null){

                                for(int i_s = 0; i_s < sparseIntArray.size(); i_s++){

                                    Integer key = sparseIntArray.keyAt(i_s);

                                    if(key.equals(arbol.getId())){

                                        //compruebo si es menor o mayor
                                        Integer value = sparseIntArray.get(key);

                                        if(value.equals(1)){
                                            miImageView.setImageResource(R.drawable.ic_point_1);
                                            miImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                        } else if(value.equals(3)){
                                            miImageView.setImageResource(R.drawable.ic_point_3);
                                            miImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                        } else if(value.equals(2)){
                                            miImageView.setImageResource(R.drawable.ic_point_2);
                                            miImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                        }

                                    }

                                }

                            }


                            TableRow.LayoutParams params_3 = new TableRow.LayoutParams(40, 40);
                            miImageView.setLayoutParams(params_3);

                            /*ImageButton img_ = new ImageButton(this);
                            img_.setImageResource(R.drawable.ic_point_1);
                            img_.setLayoutParams(params_2);
                            img_.setPadding(5, 5, 5, 5);
                            img_.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                            img_.setAdjustViewBounds(true);*/
                            contenedor.setBackgroundResource(R.drawable.borde_celdas);
                            contenedor.addView(miImageView);
                            rows.addView(contenedor);

                        } else {
                            TextView tv = new TextView(this);
                            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                    80));
                            tv.setGravity(Gravity.CENTER);
                            tv.setTextSize(18);
                            tv.setPadding(10, 10, 10, 10);
                            tv.setText(text);
                            tv.setTextColor(getResources().getColor(black));
                            tv.setBackgroundResource(R.drawable.borde_celdas);
                            //tv.setLayoutParams(new TableRow.LayoutParams(i));
                            rows.addView(tv);

                        }

                        i = i + 1;
                        ju++;
                    }

                    TableRow.LayoutParams params_2 = new TableRow.LayoutParams(80, 80);

                    //Agrego el boto
                    img_edit = new ImageButton(this);
                    img_edit.setImageResource(R.drawable.editar);
                    img_edit.setBackgroundResource(R.drawable.effect_button_arb);
                    img_edit.setLayoutParams(params_2);
                    img_edit.setPadding(5, 5, 5, 5);
                    img_edit.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                    img_edit.setAdjustViewBounds(true);
                    img_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            editArbol(arbol);

                        }
                    });

                    rows.addView(img_edit);

                    //Agego el Boton de Eliminar
                    TableRow.LayoutParams params = new TableRow.LayoutParams(80, 80);

                    img_delete = new ImageButton(this);
                    img_delete.setImageResource(R.drawable.eliminar);
                    img_delete.setBackgroundResource(R.drawable.effect_button_arb);
                    img_delete.setLayoutParams(params);
                    img_delete.setPadding(5, 5, 5, 5);
                    img_delete.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                    img_delete.setAdjustViewBounds(true);

                    img_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleleArbol(arbol.getId());
                        }
                    });


                    rows.addView(img_delete);


                    getTableLayout().addView(rows);

                    j = j + 1;


                }
            }
        }

    }

    public SparseIntArray getListaArbolesOrder(ArrayList<ArbolesRelevamientoEntity> listaArboles){

        SparseIntArray lista = null;
        double minimo = 0;
        double maximo = 0;
        double suma = 0;

        int i = -1;


        if(listaArboles != null){

            lista = new SparseIntArray();

            //Recorro la lista de arboles y obtengo el mas pequeño
            for (ArbolesRelevamientoEntity arbol : listaArboles){

                if(i == -1){

                    if(arbol.getDap() != null){
                        minimo = arbol.getDap();
                        maximo = arbol.getDap();
                        i++;
                    }

                } else {

                    if(arbol.getDap() != null){

                        if(arbol.getDap() < minimo){
                            minimo = arbol.getDap();
                        }

                        if(arbol.getDap() > maximo){
                            maximo = arbol.getDap();
                        }

                    }

                }

                if(arbol.getDap() != null){

                    suma = suma + arbol.getDap();
                }

            }

            //Calculo el promedio
            double promedio = suma / listaArboles.size();
            double distancia_min = 0;

            //Cargo en el HashMap los valores de minimo y maximo

            int j = -1;

            //vUELVO A RECORRER LOS arboles Y CARGAR LOS ID Y SU POSICION
            for (ArbolesRelevamientoEntity arbol : listaArboles){

                if(arbol.getDap() == minimo){

                    lista.put(arbol.getId(), 1);

                }

                if(arbol.getDap() == maximo){
                    lista.put(arbol.getId(), 3);
                }

                if(j == -1){

                    if(arbol.getDap() != null){

                        distancia_min = Math.abs(promedio - arbol.getDap());
                        j = 0;
                    }

                } else {

                    if(arbol.getDap() != null){

                        //Evaluo si la distancia obtenida es menor a la ya guardada

                        double dist_aux = Math.abs(promedio - arbol.getDap());

                        if(dist_aux < distancia_min){

                            distancia_min = dist_aux;

                        }

                    }

                }

            }

            //Vuelvo a recorrer los arbolesy la lista para saber que no esta presente alli y agrego como intermedio
            Double aux_dis_m = distancia_min;

            for (ArbolesRelevamientoEntity arbol : listaArboles){

                double dist_aux = Math.abs(promedio - arbol.getDap());
                //Recorro la lista para que no este ahi
                //Encontre el arbol ahora consulto que no este en la lista
                if(dist_aux == distancia_min){

                    boolean res = false;
                    if(lista != null){
                        for (int i_j = 0; i_j < lista.size(); i_j++){

                            Integer key = lista.keyAt(i_j);

                            if(key.equals(arbol.getId())){
                                res = true;
                            }

                        }
                    }

                    if(!res){
                        lista.put(arbol.getId(), 2);
                    }

                }

            }

        }

        return lista;
    }

    public double getPromedioDap(ArrayList<ArbolesRelevamientoEntity> listaArboles){

        double result = 0;

        int cant = 0;
        double suma = 0;

        if(listaArboles != null){
            for (ArbolesRelevamientoEntity arbolesRelevamientoEntity : listaArboles){

                if(arbolesRelevamientoEntity.getDap() != null){

                    suma = suma + arbolesRelevamientoEntity.getDap();
                    cant++;

                }

            }

            //Calculo el promedio

            result = suma / cant;
        }

        return result;
    }

    public void addArbol(double promedio){



        Location location = getMiPosition();


        if (location != null){
            DialogArbolesRelevamientoAdd dlg_arbol_add = new DialogArbolesRelevamientoAdd(this, getIdparcela().toString(), getIdrodal().toString(), location,this);

            dlg_arbol_add.setPromedioDap(promedio);

            dlg_arbol_add.showDialogParcelarelevamiento(this);


        } else {
            Toast.makeText(getApplicationContext(), "No hay posición de GPS", Toast.LENGTH_LONG).show();
        }



    }

    public void editArbol(ArbolesRelevamientoEntity arbol){
        DialogArbolesRelevamientoEdit arbol_edit = new DialogArbolesRelevamientoEdit(this, this);

        arbol_edit.showDialogArbolesrelevamiento(this, arbol);


    }
    private void deleleArbol(final Integer idarbol){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View mview = LayoutInflater.from(this).inflate(R.layout.dialog_result_acept_cancel, null, false);
        //View mview = getLayoutInflater().inflate(R.layout.dialog_result_error, null);
        builder.setView(mview);
        final AlertDialog dialog = builder.create();
        //Seteo el texto de error
        TextView txt_msj = (TextView)mview.findViewById(R.id.text_dr_title_ac_canc);

        String msj = "¿Desea eliminar el árbol " + idarbol + "?";
        txt_msj.setText(msj);
        dialog.setCanceledOnTouchOutside(false);

        Button btn_aceptar_dialog = (Button)mview.findViewById(R.id.btn_aceptar_dialog_ac_can);
        btn_aceptar_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Este metodo solo cambia el estado de la parcela a 0
                ArbolesRelevamientoEntity arboles_ent = new ArbolesRelevamientoEntity();
                if (arboles_ent.deleteArbolById(getApplicationContext(), idarbol)){

                    cleanTable();
                    showArboles();
                    drawArbolesToMap();
                    Toast.makeText(getApplicationContext(), "Árbol Eliminado", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Error al eliminar el Árbol. Intente nuevamente.", Toast.LENGTH_LONG).show();
                }


                dialog.dismiss();

            }
        });

        Button btn_cancelar_dialog = (Button)mview.findViewById(R.id.btn_cancelar_dialog_ac_can);
        btn_cancelar_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //Muestro las parcelas sincronizadas previamente sin volver a sincronizar
            }
        });
        dialog.show();
    }

    private void deleteAllArboles(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View mview = LayoutInflater.from(this).inflate(R.layout.dialog_result_acept_cancel, null, false);
        //View mview = getLayoutInflater().inflate(R.layout.dialog_result_error, null);
        builder.setView(mview);
        final AlertDialog dialog = builder.create();
        //Seteo el texto de error
        TextView txt_msj = (TextView)mview.findViewById(R.id.text_dr_title_ac_canc);
        txt_msj.setText("¿Desea eliminar todos los Árboles?");
        dialog.setCanceledOnTouchOutside(false);

        Button btn_aceptar_dialog = (Button)mview.findViewById(R.id.btn_aceptar_dialog_ac_can);
        btn_aceptar_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Este metodo solo cambia el estado de la parcela a 0
                ArbolesRelevamientoEntity arboles_ent = new ArbolesRelevamientoEntity();
                if (arboles_ent.deleteArbolesByIdParcela(getApplicationContext(), getIdparcela())){

                    cleanTable();
                    Toast.makeText(getApplicationContext(), "Árboles Eliminados", Toast.LENGTH_LONG).show();
                    drawArbolesToMap();

                } else {
                    Toast.makeText(getApplicationContext(), "Error al eliminar los Árboles. Intente nuevamente.", Toast.LENGTH_LONG).show();
                }


                dialog.dismiss();

            }
        });

        Button btn_cancelar_dialog = (Button)mview.findViewById(R.id.btn_cancelar_dialog_ac_can);
        btn_cancelar_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //Muestro las parcelas sincronizadas previamente sin volver a sincronizar
            }
        });
        dialog.show();
    }


    public void cleanTable() {

        int childCount = getTableLayout().getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            getTableLayout().removeViews(1, childCount - 1);
        }
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


    public void drawArbolesToMap(){

        ArbolesRelevamientoEntity arbol_entity = new ArbolesRelevamientoEntity();
        ArrayList<ArbolesRelevamientoEntity> lista_arboles = arbol_entity.getListaArbolesByIdParcela(this, getIdparcela());

        //lIMPIO LA LISTA DE ARBOLES Y LA INSERTO NUEVAMENTE
        if (getListaArboles() != null){
            getListaArboles().clear();
            setListaArboles(null);
        }
        setListaArboles(lista_arboles);



        if (mMap != null){
            mMap.clear();

        }


        //Debo dibujar el rodal si tiene

        if(this.jsonrodal != null){
            GeoJsonLayer layer_rodal = new GeoJsonLayer(mMap, this.jsonrodal);

            for (GeoJsonFeature feature : layer_rodal.getFeatures()) {
                GeoJsonPolygonStyle style = new GeoJsonPolygonStyle();
                style.setStrokeColor(Color.rgb(255, 230, 51));
                style.setStrokeWidth(2);
                feature.setPolygonStyle(style);
            }
            layer_rodal.addLayerToMap();
        }


        LatLng sydney = new LatLng(getLat(), getLongi());

        if (getIdparcelapostgres() != null && getIdparcelapostgres() != 0.0){
            mMap.addMarker(new MarkerOptions().position(sydney).title("PARCELA N° : " + getIdparcelapostgres()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

        } else {
            mMap.addMarker(new MarkerOptions().position(sydney).title("PARCELA N° : " + getIdparcela2()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

        }


        if (getListaArboles() != null){
            for (ArbolesRelevamientoEntity arbol : getListaArboles()){

                // Add a marker in Sydney and move the camera
                LatLng arbol_ = new LatLng(arbol.getLat(), arbol.getLongi());
                Marker m = mMap.addMarker(new MarkerOptions().position(arbol_).title("Árbol N° " + arbol.getId().toString()));
                m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.arbol));

            }

        }

        float zoom_level = 16;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoom_level));


    }

    private Boolean setJsonRodal(){

        RodalesSincronizedEntity rodal = new RodalesSincronizedEntity().getRodalById(getContext(), getIdrodal().toString());


        try{

            this.jsonrodal = new JSONObject(rodal.getGeometry());

            return true;

        } catch (JSONException e){
            e.printStackTrace();
            return false;
        } catch (NullPointerException ex){
            ex.printStackTrace();
            return false;
        }

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButtonSi:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.radioButtonNo:
                if (checked)
                    // Ninjas rule
                    break;
        }

    }

    static class ViewHolder{

        EditText marca;
        EditText dap;
        EditText altura;
        EditText altura_poda;
        EditText dmsm;
        Spinner sp_calidad;
        EditText lat;
        EditText longi;

        Button aceptar;
        Button cancelar;

    }

    public TableLayout getTableLayout() {
        return tableLayout;
    }

    public void setTableLayout(TableLayout tableLayout) {
        this.tableLayout = tableLayout;
    }

    public Integer getIdrodal() {
        return idrodal;
    }

    public void setIdrodal(Integer idrodal) {
        this.idrodal = idrodal;
    }

    public Integer getIdparcela() {
        return idparcela;
    }

    public void setIdparcela(Integer idparcela) {
        this.idparcela = idparcela;
    }

    public ArrayList<ArbolesRelevamientoEntity> getListaArboles() {
        return listaArboles;
    }

    public void setListaArboles(ArrayList<ArbolesRelevamientoEntity> listaArboles) {
        this.listaArboles = listaArboles;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Fragment getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(Fragment mainActivity) {
        this.mainActivity = mainActivity;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLongi() {
        return longi;
    }

    public void setLongi(Double longi) {
        this.longi = longi;
    }

    public boolean isAlturas_cargadas() {
        return alturas_cargadas;
    }

    public void setAlturas_cargadas(boolean alturas_cargadas) {
        this.alturas_cargadas = alturas_cargadas;
    }

    public Integer getIdparcelapostgres() {
        return idparcelapostgres;
    }

    public void setIdparcelapostgres(Integer idparcelapostgres) {
        this.idparcelapostgres = idparcelapostgres;
    }

    public Integer getIdparcela2() {
        return idparcela2;
    }

    public void setIdparcela2(Integer idparcela2) {
        this.idparcela2 = idparcela2;
    }
}
