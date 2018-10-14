package forestal.app.pindo.pindo.uploadpostgres.progress.upload;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import forestal.app.pindo.pindo.R;
import forestal.app.pindo.pindo.config.RodalesVerParcelasSincFragment;
import forestal.app.pindo.pindo.uploadpostgres.UploadPostgresFragment;
import forestal.app.pindo.pindo.uploadpostgres.postgres.ParcelasUploadPostgres;
import postgres.conexion.DBPostgresConnection;
import utillities.entidades.ParcelasRelevamientoSQLiteEntity;
import utillities.entidades.RodalesRelevamiento;

public class ParcelasUploadProgress {

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

    public ParcelasUploadProgress(DBPostgresConnection dbcon, Context context, View mview, FragmentActivity activity) {
        this.context = context;
        this.mview = mview;
        setDbcon(dbcon);
        setActivity(activity);

        setCantidad_procesados(0);
    }


    public void uploadParcelaToPostgres(final ArrayList<ParcelasRelevamientoSQLiteEntity> listaParcelas){

        final Integer max = listaParcelas.size();
        createDialogProgress(max);

        final ParcelasUploadPostgres parcelasUploadPostgres = new ParcelasUploadPostgres();

        this.setTitle("Subiendo Parcelas a Postgres");

        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                Integer i = 0;
                for (ParcelasRelevamientoSQLiteEntity parc : listaParcelas){

                    if(parcelasUploadPostgres.uploadParcelaRel(getDbcon(), parc)){

                        //Traigo el ID GENERADO EN POSTGRES y seteo a SQLITE COMO QUE YA SINCRONIZO

                        Integer id_postgres = -1;

                        id_postgres = parcelasUploadPostgres.getIdLastParcelaAdd(getDbcon());

                        if(id_postgres != -1){

                            //Tego que setear en SQLITE el campo Sincronizado y el campo IDENPOSTGRES

                            if(new ParcelasRelevamientoSQLiteEntity().setSincronizedParcelaRelevamientoToTrue(getContext(), id_postgres, parc.getId())){

                                //CARGO EN SQLITE EL IDPARCELAPOSTGRES

                                //Como todos el proceso fue exitoso sumo para informar en el Toat
                                setCantidad_procesados(getCantidad_procesados() + 1);
                            }

                        } else {
                            //BORRO el ultimo agregado

                            parcelasUploadPostgres.removeLastAdd(getDbcon(), id_postgres);
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
                    Toast.makeText(context, "Se han procesado: " + getCantidad_procesados().toString() + " de " + max.toString() + " parcelas.", Toast.LENGTH_LONG).show();
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
        //View mview = this.inflater.inflate(R.layout.dialog_progress, null);
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

    public Integer getCantidad_procesados() {
        return cantidad_procesados;
    }

    public void setCantidad_procesados(Integer cantidad_procesados) {
        this.cantidad_procesados = cantidad_procesados;
    }

    public DBPostgresConnection getDbcon() {
        return dbcon;
    }

    public void setDbcon(DBPostgresConnection dbcon) {
        this.dbcon = dbcon;
    }

    public FragmentActivity getActivity() {
        return activity;
    }

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }
}
