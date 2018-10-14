package forestal.app.pindo.pindo.config.sincronizedclass;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
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
import utillities.entidades.RodalesEntity;
import utillities.utilidades.Utilidades;

public class SincronizarRodalesSQlite {

    private Context context;
    private LayoutInflater inflater;
    private View mview;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    ProgressBar pgrBar;
    TextView output;


    public SincronizarRodalesSQlite(Context context, View mview) {
        this.context = context;
        this.mview = mview;
    }

    /**
     * Agrega los rodales extraidos de POSTGRES a una tabla en SQLITE
     * @param listaRodal
     */
    public void insertListaRodalesToTablaRodal(final ArrayList<RodalesEntity> listaRodal) {



        final Integer max = listaRodal.size();
        createDialogProgress(max);

        final SQLite_OpenHelper conn = new SQLite_OpenHelper(this.context, Utilidades.DB_NAME, null, 1);
        final SQLiteDatabase db = conn.getWritableDatabase();

        this.setTitle("Sincronizando.....");
        //Realizo el truncate de la tabla
        Integer res = db.delete(Utilidades.TABLA_RODALES, null, null);


        Thread hilo = new Thread(new Runnable() {
                @Override
                public void run() {

                    for (int i = 0; i < max; i++) {
                        //Inserto el Objeto a la db
                        ContentValues conValues = new ContentValues();
                        conValues.put(Utilidades.RODALES_idrodales, listaRodal.get(i).getIdrodales());
                        conValues.put(Utilidades.RODALES_cod_sap, listaRodal.get(i).getCod_sap());
                        conValues.put(Utilidades.RODALES_campo, listaRodal.get(i).getCampo());
                        conValues.put(Utilidades.RODALES_uso, listaRodal.get(i).getUso());
                        conValues.put(Utilidades.RODALES_finalizado, listaRodal.get(i).getFinalizado().toString());
                        conValues.put(Utilidades.RODALES_empresa, listaRodal.get(i).getEmpresa());
                        conValues.put(Utilidades.RODALES_Fecha_plantacion, listaRodal.get(i).getFecha_plantacion());
                        conValues.put(Utilidades.RODALES_especie, listaRodal.get(i).getEspecie());
                        conValues.put(Utilidades.RODALES_geometry, listaRodal.get(i).getGeometry());
                        conValues.put(Utilidades.RODALES_LAT, listaRodal.get(i).getLatitud());
                        conValues.put(Utilidades.RODALES_LONG, listaRodal.get(i).getLongitud());

                        long resultado = db.insert(Utilidades.TABLA_RODALES, Utilidades.RODALES_idrodales, conValues);

                        publishProgress(i);
                        try {
                            Thread.sleep(10000 / max);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    dialog.dismiss();


                }
        });

        hilo.start();


    }


    public Boolean insertRodalToTablaRodal(RodalesEntity rodal){



        return false;
    }

    public Integer getCantidadRegistros()
    {
        Integer cantidad = 0;
        SQLite_OpenHelper conn = new SQLite_OpenHelper(this.context, Utilidades.DB_NAME, null, 1);
        final SQLiteDatabase db = conn.getReadableDatabase();
        db.beginTransaction();
        try {

            String sql = "SELECT * FROM " + Utilidades.TABLA_RODALES;

            Cursor cursor = db.rawQuery(sql,null);

            cantidad = cursor.getCount();


        } catch (SQLiteException e){
            Toast.makeText(this.context, "Mensaje: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

        return cantidad;

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

}
