package forestal.app.pindo.pindo;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import forestal.app.pindo.pindo.config.HomeConfigFragment;
import forestal.app.pindo.pindo.config.ParcelasListByRodalFragment;
import forestal.app.pindo.pindo.config.PostgisConfigFragment;
import forestal.app.pindo.pindo.config.RodalesSincronizarConfigFragment;
import forestal.app.pindo.pindo.config.RodalesVerParcelasSincFragment;
import forestal.app.pindo.pindo.config.restricciones.fragment.RestriccionesFragment;
import forestal.app.pindo.pindo.config.sincronizedclass.SincronizarRodalesSQlite;
import forestal.app.pindo.pindo.excelexport.ExcelExport;
import forestal.app.pindo.pindo.excelexport.ExportFlorExcelFragment;
import forestal.app.pindo.pindo.login.LoginFragment;
import forestal.app.pindo.pindo.navegador.NavegadorActivity;
import forestal.app.pindo.pindo.relevamiento.fragments.ParcelasRelevamientoFragment;
import forestal.app.pindo.pindo.relevamiento.fragments.RodalRelevamientoCreateFragment;
import forestal.app.pindo.pindo.relevamiento.fragments.RodalesRelevamientoFragment;
import forestal.app.pindo.pindo.sqlite.SQLite_OpenHelper;
import forestal.app.pindo.pindo.uploadpostgres.UploadPostgresFragment;


public class Fragment extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener,
        MapsFragment.OnFragmentInteractionListener, RodalesHome.OnFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener,
        HomeConfigFragment.OnFragmentInteractionListener, PostgisConfigFragment.OnFragmentInteractionListener,
        RodalesSincronizarConfigFragment.OnFragmentInteractionListener, RodalesVerParcelasSincFragment.OnFragmentInteractionListener,
        ParcelasListByRodalFragment.OnFragmentInteractionListener, RodalesRelevamientoFragment.OnFragmentInteractionListener,
        RodalRelevamientoCreateFragment.OnFragmentInteractionListener, ParcelasRelevamientoFragment.OnFragmentInteractionListener,
        ExportFlorExcelFragment.OnFragmentInteractionListener, UploadPostgresFragment.OnFragmentInteractionListener,
        RestriccionesFragment.OnFragmentInteractionListener{

    HomeFragment homefrg;
    RodalesHome rodalesfragment;
    LoginFragment loginFragment;
    SincronizarRodalesSQlite sincRodales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int REQUEST_EXTERNAL_STORAGE = 1;

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);


        SQLite_OpenHelper conn = new SQLite_OpenHelper(this, "db_pindo", null, 1);

        homefrg = new HomeFragment();
        homefrg.setArguments(null);

        //Agrego el Fragmento Home
        FragmentManager fm = getSupportFragmentManager();

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_contenedor, homefrg).commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

        if(id == R.id.nav_home){

            HomeFragment homefrg_ = new HomeFragment();
            homefrg_.setArguments(null);

            //Agrego el Fragmento Home
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_contenedor, homefrg_);
            ft.commit();

        }
        if (id == R.id.nav_rodales) {


            RodalesRelevamientoFragment rodal_rel_ft = new RodalesRelevamientoFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
            ft.replace(R.id.fragment_contenedor, rodal_rel_ft);
            ft.commit();


        } if (id == R.id.excel) {

            ExcelExport excelExport = new ExcelExport(getApplicationContext(), this);
            excelExport.createExcelExport();


        }
        if (id == R.id.nav_florexcel) {


            ExportFlorExcelFragment export_fr = new ExportFlorExcelFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);

            ft.replace(R.id.fragment_contenedor, export_fr);

            ft.commit();



        }
        if (id == R.id.nav_config){

            //Deberia llevarme al fragment de inicio de session
            loginFragment = new LoginFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);

            ft.replace(R.id.fragment_contenedor, loginFragment);

            ft.commit();

        }

        if (id == R.id.navegador){

            Intent intent = new Intent (getApplicationContext(), NavegadorActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
