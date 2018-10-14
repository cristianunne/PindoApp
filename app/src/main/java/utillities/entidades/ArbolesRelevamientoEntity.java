package utillities.entidades;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import forestal.app.pindo.pindo.sqlite.SQLite_OpenHelper;
import utillities.utilidades.Utilidades;
import utillities.utilidades._Default;

public class ArbolesRelevamientoEntity extends _Default {

    private Integer id;
    private Integer idrodal;
    private Integer idparcela;
    private String marca;
    private Double dap;
    private Double altura;
    private Double alturapoda;
    private Double dmsm;
    private String calidad;
    private String cosechado;
    private Integer sincronizado;
    private Integer idtablaarbpostgres;
    private Double lat;
    private Double longi;
    private Integer idpostgres;
    private String fecha_rel;


    public ArbolesRelevamientoEntity() {
        super();
    }


    public Boolean insertArbolRelevamiento(@NonNull Context context, @NonNull ArbolesRelevamientoEntity arbol){


        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();

        try{

            ContentValues values = new ContentValues();
            values.put(Utilidades.ARBOLES_idrodal, arbol.getIdrodal());
            values.put(Utilidades.ARBOLES_idparcela, arbol.getIdparcela());
            values.put(Utilidades.ARBOLES_marca, arbol.getMarca());
            values.put(Utilidades.ARBOLES_dap, arbol.getDap());
            values.put(Utilidades.ARBOLES_altura, arbol.getAltura());
            values.put(Utilidades.ARBOLES_altura_poda, arbol.getAlturapoda());
            values.put(Utilidades.ARBOLES_dmsm, arbol.getDmsm());
            values.put(Utilidades.ARBOLES_calidad, arbol.getCalidad());
            values.put(Utilidades.ARBOLES_cosechado, arbol.getCosechado());
            values.put(Utilidades.ARBOLES_latitud, arbol.getLat());
            values.put(Utilidades.ARBOLES_longitud, arbol.getLongi());
            values.put(Utilidades.ARBOLES_fecharel, arbol.getFecha_rel());

            try {

                long res = db.insertOrThrow(Utilidades.TABLA_ARBOLES, Utilidades.ARBOLES_id, values);

                if (res != -1){
                    return true;
                }

            } catch (SQLiteException e){
                this._status = false;
                this._mensaje = e.getMessage();
                e.printStackTrace();
            }


        } catch (SQLiteException e){
            this._mensaje = e.getMessage();
            this._status = false;
        }


        return false;
    }

    public Boolean editArbolByID(Context context, ArbolesRelevamientoEntity arbol){
        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();

        try{

            ContentValues values = new ContentValues();
            values.put(Utilidades.ARBOLES_idrodal, arbol.getIdrodal());
            values.put(Utilidades.ARBOLES_idparcela, arbol.getIdparcela());
            values.put(Utilidades.ARBOLES_marca, arbol.getMarca());
            values.put(Utilidades.ARBOLES_dap, arbol.getDap());
            values.put(Utilidades.ARBOLES_altura, arbol.getAltura());
            values.put(Utilidades.ARBOLES_altura_poda, arbol.getAlturapoda());
            values.put(Utilidades.ARBOLES_dmsm, arbol.getDmsm());
            values.put(Utilidades.ARBOLES_calidad, arbol.getCalidad());
            values.put(Utilidades.ARBOLES_cosechado, arbol.getCosechado());
            values.put(Utilidades.ARBOLES_latitud, arbol.getLat());
            values.put(Utilidades.ARBOLES_longitud, arbol.getLongi());

            try {

                Integer res = db.update(Utilidades.TABLA_ARBOLES, values, Utilidades.ARBOLES_id + " = " + arbol.getId(), null);

                if (res > 0){
                    return true;
                }

            } catch (SQLiteException e){
                this._status = false;
                this._mensaje = e.getMessage();
                e.printStackTrace();
            }


        } catch (SQLiteException e){
            this._mensaje = e.getMessage();
            this._status = false;
        }


        return false;
    }

    public ArrayList<ArbolesRelevamientoEntity> getAllListaArboles(Context context){

        ArrayList<ArbolesRelevamientoEntity> lista = null;

        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        db.beginTransaction();

        try {

            String sql = "SELECT * FROM " + Utilidades.TABLA_ARBOLES;

            Cursor cursor = db.rawQuery(sql,null);
            if (cursor.getCount() > 0) {

                lista = new ArrayList<>();

                while (cursor.moveToNext()){

                    ArbolesRelevamientoEntity arbol = new ArbolesRelevamientoEntity();

                    arbol.setId(cursor.getInt(cursor.getColumnIndex(Utilidades.ARBOLES_id)));
                    arbol.setIdrodal(cursor.getInt(cursor.getColumnIndex(Utilidades.ARBOLES_idrodal)));
                    arbol.setIdparcela(cursor.getInt(cursor.getColumnIndex(Utilidades.ARBOLES_idparcela)));
                    arbol.setMarca(cursor.getString(cursor.getColumnIndex(Utilidades.ARBOLES_marca)));
                    arbol.setDap(cursor.getDouble(cursor.getColumnIndex(Utilidades.ARBOLES_dap)));
                    arbol.setAltura(cursor.getDouble(cursor.getColumnIndex(Utilidades.ARBOLES_altura)));
                    arbol.setAlturapoda(cursor.getDouble(cursor.getColumnIndex(Utilidades.ARBOLES_altura_poda)));
                    arbol.setDmsm(cursor.getDouble(cursor.getColumnIndex(Utilidades.ARBOLES_dmsm)));
                    arbol.setCalidad(cursor.getString(cursor.getColumnIndex(Utilidades.ARBOLES_calidad)));
                    arbol.setCosechado(cursor.getString(cursor.getColumnIndex(Utilidades.ARBOLES_cosechado)));
                    arbol.setLat(cursor.getDouble(cursor.getColumnIndex(Utilidades.ARBOLES_latitud)));
                    arbol.setLongi(cursor.getDouble(cursor.getColumnIndex(Utilidades.ARBOLES_longitud)));
                    arbol.setSincronizado(cursor.getInt(cursor.getColumnIndex(Utilidades.ARBOLES_sincronizado)));
                    arbol.setIdtablaarbpostgres(cursor.getInt(cursor.getColumnIndex(Utilidades.ARBOLES_idtablaarbolespostgres)));
                    arbol.setFecha_rel(cursor.getString(cursor.getColumnIndex(Utilidades.ARBOLES_fecharel)));

                    lista.add(arbol);
                    arbol = null;

                }

            }

            db.setTransactionSuccessful();

        } catch (SQLiteException e){
            this._mensaje = e.getMessage();
            this._status = false;
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return lista;
    }

    public ArrayList<ArbolesRelevamientoEntity> getListaArbolesByIdParcela(Context context, Integer idparcela){

        ArrayList<ArbolesRelevamientoEntity> lista = null;

        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        db.beginTransaction();

        try {

            String sql = "SELECT * FROM " + Utilidades.TABLA_ARBOLES + " WHERE " + Utilidades.ARBOLES_idparcela + " = " + idparcela;

            Cursor cursor = db.rawQuery(sql,null);
            if (cursor.getCount() > 0) {

                lista = new ArrayList<>();

                while (cursor.moveToNext()){

                    ArbolesRelevamientoEntity arbol = new ArbolesRelevamientoEntity();

                    arbol.setId(cursor.getInt(cursor.getColumnIndex(Utilidades.ARBOLES_id)));
                    arbol.setIdrodal(cursor.getInt(cursor.getColumnIndex(Utilidades.ARBOLES_idrodal)));
                    arbol.setIdparcela(cursor.getInt(cursor.getColumnIndex(Utilidades.ARBOLES_idparcela)));
                    arbol.setMarca(cursor.getString(cursor.getColumnIndex(Utilidades.ARBOLES_marca)));
                    arbol.setDap(cursor.getDouble(cursor.getColumnIndex(Utilidades.ARBOLES_dap)));
                    arbol.setAltura(cursor.getDouble(cursor.getColumnIndex(Utilidades.ARBOLES_altura)));
                    arbol.setAlturapoda(cursor.getDouble(cursor.getColumnIndex(Utilidades.ARBOLES_altura_poda)));
                    arbol.setDmsm(cursor.getDouble(cursor.getColumnIndex(Utilidades.ARBOLES_dmsm)));
                    arbol.setCalidad(cursor.getString(cursor.getColumnIndex(Utilidades.ARBOLES_calidad)));
                    arbol.setCosechado(cursor.getString(cursor.getColumnIndex(Utilidades.ARBOLES_cosechado)));
                    arbol.setLat(cursor.getDouble(cursor.getColumnIndex(Utilidades.ARBOLES_latitud)));
                    arbol.setLongi(cursor.getDouble(cursor.getColumnIndex(Utilidades.ARBOLES_longitud)));
                    arbol.setSincronizado(cursor.getInt(cursor.getColumnIndex(Utilidades.ARBOLES_sincronizado)));
                    arbol.setIdtablaarbpostgres(cursor.getInt(cursor.getColumnIndex(Utilidades.ARBOLES_idtablaarbolespostgres)));
                    arbol.setFecha_rel(cursor.getString(cursor.getColumnIndex(Utilidades.ARBOLES_fecharel)));

                    lista.add(arbol);
                    arbol = null;

                }

            }

            db.setTransactionSuccessful();

        } catch (SQLiteException e){
            this._mensaje = e.getMessage();
            this._status = false;
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return lista;
    }

    public ArrayList<ArbolesRelevamientoEntity> getListaArbolesByIdRodal(Context context, Integer idrodal){
        ArrayList<ArbolesRelevamientoEntity> lista = null;

        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        db.beginTransaction();

        try {

            //Traigo los arboles donde SINCRONIZADO = 0
            String sql = "SELECT * FROM " + Utilidades.TABLA_ARBOLES + " WHERE " + Utilidades.ARBOLES_idrodal + " = " + idrodal + " AND " + Utilidades.ARBOLES_sincronizado + " = 0" ;

            Cursor cursor = db.rawQuery(sql,null);
            if (cursor.getCount() > 0) {

                lista = new ArrayList<>();

                while (cursor.moveToNext()){

                    ArbolesRelevamientoEntity arbol = new ArbolesRelevamientoEntity();

                    arbol.setId(cursor.getInt(cursor.getColumnIndex(Utilidades.ARBOLES_id)));
                    arbol.setIdrodal(cursor.getInt(cursor.getColumnIndex(Utilidades.ARBOLES_idrodal)));
                    arbol.setIdparcela(cursor.getInt(cursor.getColumnIndex(Utilidades.ARBOLES_idparcela)));
                    arbol.setMarca(cursor.getString(cursor.getColumnIndex(Utilidades.ARBOLES_marca)));
                    arbol.setDap(cursor.getDouble(cursor.getColumnIndex(Utilidades.ARBOLES_dap)));
                    arbol.setAltura(cursor.getDouble(cursor.getColumnIndex(Utilidades.ARBOLES_altura)));
                    arbol.setAlturapoda(cursor.getDouble(cursor.getColumnIndex(Utilidades.ARBOLES_altura_poda)));
                    arbol.setDmsm(cursor.getDouble(cursor.getColumnIndex(Utilidades.ARBOLES_dmsm)));
                    arbol.setCalidad(cursor.getString(cursor.getColumnIndex(Utilidades.ARBOLES_calidad)));
                    arbol.setCosechado(cursor.getString(cursor.getColumnIndex(Utilidades.ARBOLES_cosechado)));
                    arbol.setLat(cursor.getDouble(cursor.getColumnIndex(Utilidades.ARBOLES_latitud)));
                    arbol.setLongi(cursor.getDouble(cursor.getColumnIndex(Utilidades.ARBOLES_longitud)));
                    arbol.setSincronizado(cursor.getInt(cursor.getColumnIndex(Utilidades.ARBOLES_sincronizado)));
                    arbol.setIdtablaarbpostgres(cursor.getInt(cursor.getColumnIndex(Utilidades.ARBOLES_idtablaarbolespostgres)));
                    arbol.setFecha_rel(cursor.getString(cursor.getColumnIndex(Utilidades.ARBOLES_fecharel)));

                    lista.add(arbol);
                    arbol = null;

                }

            }

            db.setTransactionSuccessful();

        } catch (SQLiteException e){
            this._mensaje = e.getMessage();
            this._status = false;
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return lista;


    }


    public Boolean setSincronizedArbolRelevamientoToTrue(Context context, Integer id_in_poatgres, Integer arbol_id){


        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        db.beginTransaction();

        try{

            ContentValues values = new ContentValues();
            values.put(Utilidades.ARBOLES_sincronizado, "1");
            values.put(Utilidades.ARBOLES_idpostgres, id_in_poatgres);
            Integer res = db.update(Utilidades.TABLA_ARBOLES, values, Utilidades.ARBOLES_id + " = " + arbol_id, null);

            db.setTransactionSuccessful();

            if (res > 0){
                return true;
            } else {
                return false;
            }

        } catch (SQLiteException e){
            this._mensaje = e.getMessage();
            this._status = false;
        } finally {
            db.endTransaction();
        }


        return false;
    }
    public Boolean deleteArbolesByIdParcela(Context context, Integer idparcela){

        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        db.beginTransaction();
        int res = -1;
        try{


            String[] args = new String[]{idparcela.toString()};
            res = db.delete(Utilidades.TABLA_ARBOLES, Utilidades.ARBOLES_idparcela + " = ?", args);
            db.setTransactionSuccessful();

            if (res >= 0){
                return true;
            }

        } catch (SQLiteException e){
            this._mensaje = e.getMessage();
            this._status = false;
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }


        return false;
    }

    public Boolean deleteArbolById(Context context, Integer idarbol){

        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        db.beginTransaction();
        int res = -1;
        try{


            String[] args = new String[]{idarbol.toString()};
            res = db.delete(Utilidades.TABLA_ARBOLES, Utilidades.ARBOLES_id + " = ?", args);
            db.setTransactionSuccessful();

            if (res > 0){
                return true;
            }

        } catch (SQLiteException e){
            this._mensaje = e.getMessage();
            this._status = false;
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }


        return false;
    }

    public Boolean deleteArbolByIdRodal(Context context, Integer idrodal){

        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        db.beginTransaction();
        int res = -1;
        try{


            String[] args = new String[]{idrodal.toString()};
            res = db.delete(Utilidades.TABLA_ARBOLES, Utilidades.ARBOLES_idrodal + " = ?", args);
            db.setTransactionSuccessful();

            if (res >= 0){
                return true;
            }

        } catch (SQLiteException e){
            this._mensaje = e.getMessage();
            this._status = false;
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }


        return false;
    }

    public Map<Integer, Integer> getCantidadArbolesByIdParcelas(Context context){

        Map<Integer, Integer> lista = null;


        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        db.beginTransaction();

        try{
            String sql = "SELECT " + Utilidades.ARBOLES_idparcela + ", COUNT(*) AS cantidad FROM " + Utilidades.TABLA_ARBOLES
                    + " GROUP BY " + Utilidades.ARBOLES_idparcela;

            Cursor cursor = db.rawQuery(sql,null);

            if (cursor.getCount() > 0) {

                lista = new HashMap<Integer, Integer>();

                while (cursor.moveToNext()){

                    lista.put(cursor.getInt(cursor.getColumnIndex(Utilidades.ARBOLES_idparcela)), cursor.getInt(cursor.getColumnIndex("cantidad")));

                }
            }

            db.setTransactionSuccessful();


        } catch (SQLiteException e){
            this._mensaje = e.getMessage();
            this._status = false;
        } finally {
            db.endTransaction();
        }



        return lista;
    }


    public Integer getCantidadDeArboles(Context context){

        Integer cantidad = 0;

        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        db.beginTransaction();

        try{
            String sql = "SELECT COUNT(*) AS cantidad FROM " + Utilidades.TABLA_ARBOLES;

            Cursor cursor = db.rawQuery(sql,null);

            if (cursor.getCount() > 0) {



                while (cursor.moveToNext()){

                    cantidad = cursor.getInt(cursor.getColumnIndex("cantidad"));

                }
            }

            db.setTransactionSuccessful();


        } catch (SQLiteException e){
            this._mensaje = e.getMessage();
            this._status = false;
        } finally {
            db.endTransaction();
        }

        return cantidad;

    }


    public Map<Integer, Integer> getCantidadArbolesByIdRodal(Context context){

        Map<Integer, Integer> lista = null;


        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        db.beginTransaction();

        try{
            String sql = "SELECT " + Utilidades.ARBOLES_idrodal + ", COUNT(*) AS cantidad FROM " + Utilidades.TABLA_ARBOLES
                    + " GROUP BY " + Utilidades.PARCELAS_REL_idrodal;

            Cursor cursor = db.rawQuery(sql,null);

            if (cursor.getCount() > 0) {

                lista = new HashMap<Integer, Integer>();

                while (cursor.moveToNext()){

                    lista.put(cursor.getInt(cursor.getColumnIndex(Utilidades.ARBOLES_idrodal)), cursor.getInt(cursor.getColumnIndex("cantidad")));

                }
            }

            db.setTransactionSuccessful();


        } catch (SQLiteException e){
            this._mensaje = e.getMessage();
            this._status = false;
        } finally {
            db.endTransaction();
        }



        return lista;
    }

    public Map<Integer, Integer> getCantidadArbolesNoUploadByIdRodal(Context context){

        Map<Integer, Integer> lista = null;


        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        db.beginTransaction();

        try{
            String sql = "SELECT " + Utilidades.ARBOLES_idrodal + ", COUNT(*) AS cantidad FROM " + Utilidades.TABLA_ARBOLES
                    + " WHERE " + Utilidades.ARBOLES_sincronizado + " = 0 GROUP BY " + Utilidades.PARCELAS_REL_idrodal;

            Cursor cursor = db.rawQuery(sql,null);

            if (cursor.getCount() > 0) {

                lista = new HashMap<Integer, Integer>();

                while (cursor.moveToNext()){

                    lista.put(cursor.getInt(cursor.getColumnIndex(Utilidades.ARBOLES_idrodal)), cursor.getInt(cursor.getColumnIndex("cantidad")));

                }
            }

            db.setTransactionSuccessful();


        } catch (SQLiteException e){
            this._mensaje = e.getMessage();
            this._status = false;
        } finally {
            db.endTransaction();
        }



        return lista;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdrodal() {
        return idrodal;
    }

    public void setIdrodal(Integer idrodal) {
        this.idrodal = idrodal;
    }

    public Integer getIdparcela() {
        return idparcela;
    }

    public void setIdparcela(Integer idparcela) {
        this.idparcela = idparcela;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Double getDap() {
        return dap;
    }

    public void setDap(Double dap) {
        this.dap = dap;
    }

    public Double getAltura() {
        return altura;
    }

    public void setAltura(Double altura) {
        this.altura = altura;
    }

    public Double getAlturapoda() {
        return alturapoda;
    }

    public void setAlturapoda(Double alturapoda) {
        this.alturapoda = alturapoda;
    }

    public Double getDmsm() {
        return dmsm;
    }

    public void setDmsm(Double dmsm) {
        this.dmsm = dmsm;
    }

    public String getCalidad() {
        return calidad;
    }

    public void setCalidad(String calidad) {
        this.calidad = calidad;
    }

    public String getCosechado() {
        return cosechado;
    }

    public void setCosechado(String cosechado) {
        this.cosechado = cosechado;
    }

    public Integer getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(Integer sincronizado) {
        this.sincronizado = sincronizado;
    }

    public Integer getIdtablaarbpostgres() {
        return idtablaarbpostgres;
    }

    public void setIdtablaarbpostgres(Integer idtablaarbpostgres) {
        this.idtablaarbpostgres = idtablaarbpostgres;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLongi() {
        return longi;
    }

    public void setLongi(Double longi) {
        this.longi = longi;
    }

    public Integer getIdpostgres() {
        return idpostgres;
    }

    public void setIdpostgres(Integer idpostgres) {
        this.idpostgres = idpostgres;
    }

    public String getFecha_rel() {
        return fecha_rel;
    }

    public void setFecha_rel(String fecha_rel) {
        this.fecha_rel = fecha_rel;
    }
}
