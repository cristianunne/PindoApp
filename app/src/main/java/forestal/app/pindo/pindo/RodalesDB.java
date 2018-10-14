package forestal.app.pindo.pindo;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;

import postgres.conexion.PostgresConexion;

public class RodalesDB extends AsyncTask<String, String, String> {

    PostgresConexion postgres_conn;
    Context context;




    public RodalesDB(Context context) {

        this.context = context;
    }



    @Override
    protected String doInBackground(String... strings) {

        postgres_conn = new PostgresConexion();

        try{

            Connection conn = postgres_conn.getConectionPostgres();

            if (conn != null){


                Log.d("Mensaje: ", "Todo ok");
            } else {

                Log.d("Mensaje: ", "mmm no conecto ok");
            }


        } catch (Exception e){
            Log.e("Error 1: ", e.getMessage());
        }



        return null;
    }
}
