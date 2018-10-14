package forestal.app.pindo.pindo.config.sincronizedclass;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import forestal.app.pindo.pindo.R;
import forestal.app.pindo.pindo.sqlite.SQLite_OpenHelper;
import utillities.entidades.ParcelasPostgresEntity;
import utillities.utilidades.Utilidades;

public class SincronizarParcelasSQlite {

    private Context context;
    private LayoutInflater inflater;
    private View mview;
    public ProgressDialog progressDialog;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    ProgressBar pgrBar;
    TextView output;

    private Integer cantidad_procesados;

    public SincronizarParcelasSQlite(Context context, View mview) {
        this.context = context;
        this.mview = mview;
        cantidad_procesados = 0;
    }

    public void insertarParcelasToTablaRelevamiento(final ArrayList<ParcelasPostgresEntity> lista){

        final Integer max = lista.size();
        createDialogProgress(max);


        final SQLite_OpenHelper conn = new SQLite_OpenHelper(this.context, Utilidades.DB_NAME, null, 1);
        final SQLiteDatabase db = conn.getWritableDatabase();

        this.setTitle("Sincronizando.....");

        //No trunco la Tabla_Parcelas_Relevamiento debido a que voy a realizar una consulta antes

        final Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < max; i++){

                    ContentValues conValues = new ContentValues();
                    conValues.put(Utilidades.PARCELAS_REL_idparcelarel, lista.get(i).getIdgisparcela());
                    conValues.put(Utilidades.PARCELAS_REL_cod_sap, lista.get(i).getCod_sap());
                    conValues.put(Utilidades.PARCELAS_REL_superficie, lista.get(i).getSuperficie());
                    conValues.put(Utilidades.PARCELAS_REL_pendiente, lista.get(i).getPendiente());
                    conValues.put(Utilidades.PARCELAS_REL_comentarios, lista.get(i).getComentarios());
                    conValues.put(Utilidades.PARCELAS_REL_lat, lista.get(i).getLat());
                    conValues.put(Utilidades.PARCELAS_REL_longi, lista.get(i).getLongi());
                    conValues.put(Utilidades.PARCELAS_REL_geometry, lista.get(i).getGeometria());
                    conValues.put(Utilidades.PARCELAS_REL_idrodal, lista.get(i).getIdrodalpostgres());

                    conValues.put(Utilidades.PARCELAS_REL_idpostgres, lista.get(i).getIdgisparcela());
                    conValues.put(Utilidades.PARCELAS_REL_sincronizado, 1);

                    conValues.put(Utilidades.PARCELAS_REL_tipo, lista.get(i).getTipo());


                    try{
                        //Comentarios se llena al momento de relevar, id es autoincremental; releva es default 0
                        long resultado = db.insertOrThrow(Utilidades.TABLA_PARCELAS_REL, Utilidades.PARCELAS_REL_id, conValues);

                        setCantidad_procesados(getCantidad_procesados() + 1);

                    } catch (SQLiteException e){


                        //e.printStackTrace();
                    }


                    publishProgress(i);

                    try {
                        Thread.sleep(10000 / max);

                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }



                }

                dialog.dismiss();
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

    public void publishProgress(Integer value){

        pgrBar.setProgress(value);
    }

    public void setTitle(String text){
        output.setText(text);
    }

    public Integer getCantidad_procesados() {
        return cantidad_procesados;
    }

    public void setCantidad_procesados(Integer cantidad_procesados) {
        this.cantidad_procesados = cantidad_procesados;
    }
}
