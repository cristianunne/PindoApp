package forestal.app.pindo.pindo.login;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import forestal.app.pindo.pindo.sqlite.SQLite_OpenHelper;
import utillities.utilidades.Utilidades;


public class Login {

    private Context context;
    public Boolean iniciarSesion(Context context, String user, String password){

        this.context = context;

        //Abrir la coneccion
        SQLite_OpenHelper conn = new SQLite_OpenHelper(this.context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        db.beginTransaction();

        try{

            String sql = "SELECT * FROM " + Utilidades.TABLA_USUARIOS + " WHERE " + Utilidades.USUARIOS_CAMPO_USUARIO + " = '" + user + "' AND " + Utilidades.USUARIOS_CAMPO_PASSWORD + " = '" + password + "'";
            Cursor cursor = db.rawQuery(sql,null);

            if (cursor.getCount() > 0) {
                db.setTransactionSuccessful();

                return true;
            } else {

                db.setTransactionSuccessful();
                return false;
            }

        } catch (SQLiteException e){
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
