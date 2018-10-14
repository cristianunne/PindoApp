package forestal.app.pindo.pindo.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import utillities.utilidades.Utilidades;

public class SQLite_OpenHelper extends SQLiteOpenHelper {


    public SQLite_OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Crear la tabla Uusarios
        db.execSQL(Utilidades.CREATE_TABLA_USUARIOS);
        //Agrego el usuario administrador
        db.execSQL(Utilidades.ADD_USUARIO_TABLA_USUARIO);

        //Creo la tabla ConfiPostgis
        db.execSQL(Utilidades.CREATE_TABLA_POSTGIS_CONFIG);
        db.execSQL(Utilidades.ADD_ENTIDAD_TABLA_POSTGIS);

        db.execSQL(Utilidades.CREATE_TABLA_RODALES);

        db.execSQL(Utilidades.CREATE_TABLA_RODALES_RELEVAMIENTO);

        //db.execSQL(Utilidades.CREATE_TABLA_PARCELAS_RELEVAMIENTO);

        db.execSQL(Utilidades.CREATE_TABLA_PARCELAS_REL);

        db.execSQL(Utilidades.CREATE_TABLA_ARBOLES);
        db.execSQL(Utilidades.CREATE_TABLA_RESTRICCIONES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_POSTGIS_CONFIG);
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_RODALES);
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_RODALES_RELEVAMIENTO);
        //db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_PARCELAS_RELEVAMIENTO);
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_PARCELAS_REL);
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_ARBOLES);
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_RESTRICCIONES);
        onCreate(db);
    }
}
