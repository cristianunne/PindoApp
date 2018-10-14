package forestal.app.pindo.pindo.config;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import forestal.app.pindo.pindo.sqlite.SQLite_OpenHelper;
import utillities.utilidades.Utilidades;
import utillities.utilidades._Default;

public class ConexionPostgisConfig extends _Default {


    private Context context;

    public ConexionPostgisConfig() {
        super();
    }

    public Boolean insertConfigToPostgisConfig(Context context, String user, String pass, String host, Integer port, String db_name){

        this.context = context;

        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();

        ContentValues conValues = new ContentValues();
        conValues.put(Utilidades.POSTGIS_CONFIG_USER, user);
        conValues.put(Utilidades.POSTGIS_CONFIG_PASSWORD, pass);
        conValues.put(Utilidades.POSTGIS_CONFIG_HOST, host);
        conValues.put(Utilidades.POSTGIS_CONFIG_DB, db_name);
        conValues.put(Utilidades.POSTGIS_CONFIG_PORT, port);

        //Realizo el truncate de la tabla
        db.delete(Utilidades.TABLA_POSTGIS_CONFIG, null, null);


        long resultado = db.insert(Utilidades.TABLA_POSTGIS_CONFIG, Utilidades.POSTGIS_CONFIG_ID, conValues);

        if (resultado != -1){
            return true;
        }

        return false;
    }

    public Map<String, String> getConfigPostis(Context context){
        Map config = new HashMap<String, String>();

        this.context = context;
        //Abrir la coneccion
        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        db.beginTransaction();

        try{

            String sql = "SELECT * FROM " + Utilidades.TABLA_POSTGIS_CONFIG + " LIMIT 1";

            Cursor cursor = db.rawQuery(sql,null);

            if (cursor.getCount() > 0) {

                while (cursor.moveToNext()){

                    String user = cursor.getString(cursor.getColumnIndex(Utilidades.POSTGIS_CONFIG_USER));
                    String pass = cursor.getString(cursor.getColumnIndex(Utilidades.POSTGIS_CONFIG_PASSWORD));
                    String port = cursor.getString(cursor.getColumnIndex(Utilidades.POSTGIS_CONFIG_PORT));
                    String db_name = cursor.getString(cursor.getColumnIndex(Utilidades.POSTGIS_CONFIG_DB));
                    String host = cursor.getString(cursor.getColumnIndex(Utilidades.POSTGIS_CONFIG_HOST));

                    //Agrego los datos al mapa
                    config.put(Utilidades.POSTGIS_CONFIG_USER, user);
                    config.put(Utilidades.POSTGIS_CONFIG_PASSWORD, pass);
                    config.put(Utilidades.POSTGIS_CONFIG_PORT, port);
                    config.put(Utilidades.POSTGIS_CONFIG_DB, db_name);
                    config.put(Utilidades.POSTGIS_CONFIG_HOST, host);

                }
            }

        } catch (SQLiteException e){

            this._mensaje = e.getMessage();
            this._status = false;
            Toast.makeText(this.context, "Mensaje: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

        return config;
    }


    public Boolean updateConfigToPostgisConfig(Context context, String user, String pass, String host, Integer port, String db_name){

        this.context = context;

        //Abrir la coneccion
        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        db.beginTransaction();

        ContentValues conValues = new ContentValues();
        conValues.put(Utilidades.POSTGIS_CONFIG_USER, user);
        conValues.put(Utilidades.POSTGIS_CONFIG_PASSWORD, pass);
        conValues.put(Utilidades.POSTGIS_CONFIG_HOST, host);
        conValues.put(Utilidades.POSTGIS_CONFIG_DB, db_name);
        conValues.put(Utilidades.POSTGIS_CONFIG_PORT, port);

        //Antes de insertar hago el truncate de la tabla

        return false;

    }

    public Boolean getExistsConfig(Context context){

        this.context = context;
        //Abrir la coneccion
        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        db.beginTransaction();

        try{

            String sql = "SELECT COUNT(*) AS 'count' FROM " + Utilidades.TABLA_POSTGIS_CONFIG;

            Cursor cursor = db.rawQuery(sql,null);

            if (cursor.getCount() > 0) {

                while (cursor.moveToNext()){

                    Integer num_ = cursor.getInt(cursor.getColumnIndex("count"));
                    //Toast.makeText(this.context, "Valor: " + num_.toString(), Toast.LENGTH_LONG).show();

                    if (num_ >= 1){
                        db.setTransactionSuccessful();
                        return true;
                    } else {
                        db.setTransactionSuccessful();
                        return false;
                    }
                }
            }

        } catch (SQLiteException e){

            this._mensaje = e.getMessage();
            this._status = false;
            Toast.makeText(this.context, "Mensaje: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }


        return false;
    }
}
