package forestal.app.pindo.pindo.uploadpostgres.progress.upload;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import forestal.app.pindo.pindo.R;
import forestal.app.pindo.pindo.uploadpostgres.UploadPostgresFragment;
import forestal.app.pindo.pindo.uploadpostgres.postgres.ArbolesUploadPostgres;
import postgres.conexion.DBPostgresConnection;
import utillities.entidades.ArbolesRelevamientoEntity;
import utillities.entidades.ParcelasRelevamientoSQLiteEntity;

public class ArbolesUploadProgress {

    private Context context;
    private LayoutInflater inflater;
    private View mview;
    public ProgressDialog progressDialog;
    private FragmentActivity activity;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    ProgressBar pgrBar;
    TextView output;

    private DBPostgresConnection dbcon;

    private Integer cantidad_procesados;

    public ArbolesUploadProgress(Context context, View mview, FragmentActivity activity, DBPostgresConnection dbcon) {
        this.context = context;
        this.mview = mview;
        this.activity = activity;
        this.dbcon = dbcon;

        setCantidad_procesados(0);
    }

    public void uploadArbolesToPostgres(final ArrayList<ArbolesRelevamientoEntity> listaArboles, final ArrayList<ParcelasRelevamientoSQLiteEntity> listaParcelas){

        final Integer max = listaArboles.size();
        createDialogProgress(max);

        final ArbolesUploadPostgres arbolesUploadPostgres = new ArbolesUploadPostgres();

        this.setTitle("Subiendo Arboles a Postgres");

        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                Integer i = 0;

                for(ArbolesRelevamientoEntity arbol : listaArboles){


                    //Traigo la lista de parcelas
                    for (ParcelasRelevamientoSQLiteEntity parc : listaParcelas){

                        //Si pertenece a la parcela pregunto si esta sincronizado
                        if(arbol.getIdparcela().equals(parc.getId())){

                            if(parc.getSincronizado().equals(1)){

                                arbol.setIdparcela(parc.getIdpostgres());

                                //Inserto el arbol
                                if(arbolesUploadPostgres.uploadArbolesRel(getDbcon(), arbol)){

                                    Integer id_postgres = -1;

                                    //Informo a SQLITE QUE SE PROCESO CORRECTAMENTE

                                    id_postgres = arbolesUploadPostgres.getIdLastArbolAdd(getDbcon());

                                    if(id_postgres != -1){

                                        //Seteo los campos en SQLite

                                        if(new ArbolesRelevamientoEntity().setSincronizedArbolRelevamientoToTrue(getContext(), id_postgres, arbol.getId())){

                                            setCantidad_procesados(getCantidad_procesados() + 1);

                                        }

                                    }

                                }

                            }

                        }

                    }


                    i++;
                    publishProgress(i);

                    try {
                        Thread.sleep(5000 / max);

                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }

                }

                dialog.dismiss();
                refreshFragment();

                try{
                    Looper.prepare();
                    Toast.makeText(context, "Se han procesado: " + getCantidad_procesados().toString() + " de " + max.toString() + " árboles.", Toast.LENGTH_LONG).show();
                    Looper.loop();
                    //toast.show();
                    //Log.d("canti_proc", getCantidad_procesados().toString());
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        hilo.start();

    }


    public void createDialogProgress(Integer max){

        builder = new AlertDialog.Builder(this.context);
        builder.setView(this.mview);
        dialog = builder.create();
        pgrBar = (ProgressBar) this.mview.findViewById(R.id.progressBar);
        output = (TextView) this.mview.findViewById(R.id.output_txt);
        pgrBar.setMax(max);
        pgrBar.setProgress(0);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void refreshFragment(){
        UploadPostgresFragment uploadPostgresFragment = new UploadPostgresFragment();


        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.fragment_contenedor, uploadPostgresFragment);

        ft.commit();

    }

    public void publishProgress(Integer value){

        pgrBar.setProgress(value);
    }

    public void setTitle(String text){
        output.setText(text);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    public void setInflater(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public View getMview() {
        return mview;
    }

    public void setMview(View mview) {
        this.mview = mview;
    }

    public FragmentActivity getActivity() {
        return activity;
    }

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }

    public DBPostgresConnection getDbcon() {
        return dbcon;
    }

    public void setDbcon(DBPostgresConnection dbcon) {
        this.dbcon = dbcon;
    }

    public Integer getCantidad_procesados() {
        return cantidad_procesados;
    }

    public void setCantidad_procesados(Integer cantidad_procesados) {
        this.cantidad_procesados = cantidad_procesados;
    }
}
