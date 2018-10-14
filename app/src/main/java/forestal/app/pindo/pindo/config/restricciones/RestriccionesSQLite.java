package forestal.app.pindo.pindo.config.restricciones;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import forestal.app.pindo.pindo.sqlite.SQLite_OpenHelper;
import utillities.utilidades.Utilidades;
import utillities.utilidades._Default;

public class RestriccionesSQLite extends _Default {

    public RestriccionesSQLite() {
        super();
    }




    public RestriccionesEntity getValueDap(Context context){

        RestriccionesEntity restriccionesEntity = null;

        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        db.beginTransaction();

        try {

            String sql = "SELECT * FROM " + Utilidades.TABLA_RESTRICCIONES;
            Cursor cursor = db.rawQuery(sql,null);

            if(cursor.getCount() > 0){

                restriccionesEntity = new RestriccionesEntity();

                while (cursor.moveToNext()){

                    restriccionesEntity.setId(cursor.getInt(cursor.getColumnIndex(Utilidades.RESTRICCIONES_id)));
                    restriccionesEntity.setDap(cursor.getInt(cursor.getColumnIndex(Utilidades.RESTRICCIONES_dap)));

                    break;
                }

                db.setTransactionSuccessful();
                return restriccionesEntity;
            }

            db.setTransactionSuccessful();

        } catch (SQLiteException e){
            this._mensaje = e.getMessage();
            this._status = false;
        } finally {
            db.endTransaction();
        }

        return restriccionesEntity;
    }

    public boolean insertRestriccionDap(Context context, int value){


        //Inserto el valor, pero antes realizo un truncate de la tabla
        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();

        //Realizo el truncate de la tabla
        db.delete(Utilidades.TABLA_RESTRICCIONES, null, null);

        ContentValues conValues = new ContentValues();
        conValues.put(Utilidades.RESTRICCIONES_dap, value);

        long resultado = db.insert(Utilidades.TABLA_RESTRICCIONES, Utilidades.RESTRICCIONES_id, conValues);

        if (resultado != -1){
            return true;
        }

        return false;

    }


}
