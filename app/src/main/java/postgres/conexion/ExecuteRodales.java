package postgres.conexion;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.ProgressBar;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExecuteRodales extends AsyncTask<String, Integer, ResultSet> {

    private Connection conn;
    private String query;
    private Context context;
    private LayoutInflater inflater;

    ProgressDialog progressDialog;

    AlertDialog.Builder builder;
    AlertDialog dialog;

    ProgressBar pgrBar;


    public ExecuteRodales(Connection conn, String query) {
        this.conn = conn;
        this.query = query;


    }

    @Override
    protected void onPreExecute() {

        //Inicializo el dialog
        /*builder = new AlertDialog.Builder(this.context);
        View mview = this.inflater.inflate(R.layout.dialog_progress, null);
        builder.setView(mview);
        dialog = builder.create();
        pgrBar = (ProgressBar) mview.findViewById(R.id.progressBar);
        pgrBar.setMax(100);
        pgrBar.setProgress(0);
        dialog.show();*/

    }

    @Override
    protected void onProgressUpdate(Integer... values) {

        //pgrBar.setProgress(values[0]);
    }

    @Override
    protected ResultSet doInBackground(String... strings) {
        ResultSet resultSet = null;


        try{
            if (this.conn != null){
                resultSet = this.conn.prepareStatement(this.query).executeQuery();
            }


        } catch (SQLException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (this.conn != null){
                    this.conn.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return resultSet;
    }





}
