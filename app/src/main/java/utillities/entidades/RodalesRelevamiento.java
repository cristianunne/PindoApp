package utillities.entidades;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import forestal.app.pindo.pindo.sqlite.SQLite_OpenHelper;
import utillities.utilidades.Utilidades;
import utillities.utilidades._Default;

public class RodalesRelevamiento extends _Default {


    private Integer id;
    private Integer idrodales;
    private String cod_sap;
    private String campo;
    private String uso;
    private String finalizado;
    private String empresa;
    private String geometry;
    private Double latitud;
    private Double longitud;

    private String fecha_plantacion;
    private String fecha_inv;
    private String especie;

    private Integer cantidad_parcelas;
    private Integer cantidad_parcelas_no_upload;
    private Integer cantidad_arboles;
    private Integer cantidad_arboles_no_upload;

    private ArrayList<ParcelasRelevamientoSQLiteEntity> listaParcelas;


    public RodalesRelevamiento() {
        super();
        setCantidad_parcelas(0);
        setCantidad_parcelas_no_upload(0);
        setCantidad_arboles(0);
        setCantidad_arboles_no_upload(0);

    }

    public RodalesRelevamiento(Integer idrodales, String cod_sap, String campo, String uso, String finalizado, String empresa, String geometry, Double latitud, Double longitud) {
        this.idrodales = idrodales;
        this.cod_sap = cod_sap;
        this.campo = campo;
        this.uso = uso;
        this.finalizado = finalizado;
        this.empresa = empresa;
        this.geometry = geometry;
        this.latitud = latitud;
        this.longitud = longitud;

        setFecha_plantacion("");
        setFecha_inv("");
        setEspecie("");

        setCantidad_parcelas(0);
        setCantidad_parcelas_no_upload(0);
        setCantidad_arboles(0);
        setCantidad_arboles_no_upload(0);
    }

    public ArrayList<RodalesRelevamiento> getListaRodalesSelect(Context context){

        ArrayList<RodalesRelevamiento> lista_rodales = null;

        //Abrir la coneccion
        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        db.beginTransaction();

        try{

            String sql = "SELECT * FROM " + Utilidades.TABLA_RODALES_RELEVAMIENTO + " ORDER BY " + Utilidades.RODALES_RELEVAMIENTO_idrodales + " ASC";
            Cursor cursor = db.rawQuery(sql,null);

            if (cursor.getCount() > 0) {

                lista_rodales = new ArrayList<>();

                while (cursor.moveToNext()) {

                    //Creo un nuevo objeto de Rodales
                    RodalesRelevamiento rodal = new RodalesRelevamiento();
                    rodal.setId(cursor.getInt(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_id)));
                    rodal.setIdrodales(cursor.getInt(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_idrodales)));
                    rodal.setCod_sap(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_cod_sap)));
                    rodal.setCampo(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_campo)));
                    rodal.setUso(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_uso)));
                    rodal.setFinalizado(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_finalizado)));
                    rodal.setEmpresa(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_empresa)));
                    rodal.setGeometry(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_geometry)));

                    rodal.setFecha_inv(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_Fecha_inv)));
                    rodal.setFecha_plantacion(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_Fecha_plantacion)));
                    rodal.setEspecie(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_especie)));


                    //Seteo las Coordenadas del Centroide
                    rodal.setLatitud(cursor.getDouble(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_LAT)));
                    rodal.setLongitud(cursor.getDouble(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_LONG)));

                    //Agrego el rodal a la lista
                    lista_rodales.add(rodal);
                    rodal = null;
                }
            }
            db.setTransactionSuccessful();


        } catch (SQLiteException e){
            this._mensaje = e.getMessage();
            this._status = false;
            e.printStackTrace();
        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
        }

        return lista_rodales;
    }


    public RodalesRelevamiento getRodalRelevamiento(Context context, String idrodal){

        RodalesRelevamiento rodal = null;
        //Abrir la coneccion
        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        db.beginTransaction();

        try{

            String sql = "SELECT * FROM " + Utilidades.TABLA_RODALES_RELEVAMIENTO + " ORDER BY " + Utilidades.RODALES_RELEVAMIENTO_idrodales + " ASC";
            Cursor cursor = db.rawQuery(sql,null);

            if (cursor.getCount() > 0) {
                rodal = new RodalesRelevamiento();


                while (cursor.moveToNext()) {

                    //Creo un nuevo objeto de Rodales

                    rodal.setId(cursor.getInt(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_id)));
                    rodal.setIdrodales(cursor.getInt(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_idrodales)));
                    rodal.setCod_sap(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_cod_sap)));
                    rodal.setCampo(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_campo)));
                    rodal.setUso(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_uso)));
                    rodal.setFinalizado(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_finalizado)));
                    rodal.setEmpresa(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_empresa)));
                    rodal.setGeometry(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_geometry)));

                    rodal.setFecha_inv(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_Fecha_inv)));
                    rodal.setFecha_plantacion(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_Fecha_plantacion)));
                    rodal.setEspecie(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_especie)));

                    //Seteo las Coordenadas del Centroide
                    rodal.setLatitud(cursor.getDouble(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_LAT)));
                    rodal.setLongitud(cursor.getDouble(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_LONG)));

                    break;
                }
            }
            db.setTransactionSuccessful();


        } catch (SQLiteException e){
            this._mensaje = e.getMessage();
            this._status = false;
            e.printStackTrace();
        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
        }

        return rodal;
    }


    public Boolean insertRodalToRodalesRelevamiento(Context context, RodalesRelevamiento rodal){

        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();


        ContentValues conValues = new ContentValues();
        conValues.put(Utilidades.RODALES_RELEVAMIENTO_idrodales, rodal.getIdrodales());
        conValues.put(Utilidades.RODALES_RELEVAMIENTO_cod_sap, rodal.getCod_sap());
        conValues.put(Utilidades.RODALES_RELEVAMIENTO_campo, rodal.getCampo());
        conValues.put(Utilidades.RODALES_RELEVAMIENTO_empresa, rodal.getEmpresa());
        conValues.put(Utilidades.RODALES_RELEVAMIENTO_uso, rodal.getUso());
        conValues.put(Utilidades.RODALES_RELEVAMIENTO_finalizado, rodal.getFinalizado());
        conValues.put(Utilidades.RODALES_RELEVAMIENTO_geometry, rodal.getGeometry());
        conValues.put(Utilidades.RODALES_RELEVAMIENTO_LAT, rodal.getLatitud());
        conValues.put(Utilidades.RODALES_RELEVAMIENTO_LONG, rodal.getLongitud());

        //Cargo las demas cosas
        conValues.put(Utilidades.RODALES_RELEVAMIENTO_Fecha_plantacion, rodal.getFecha_plantacion());
        conValues.put(Utilidades.RODALES_RELEVAMIENTO_Fecha_inv, rodal.getFecha_inv());
        conValues.put(Utilidades.RODALES_RELEVAMIENTO_especie, rodal.getEspecie());


        long resultado = -1;
        try {


            resultado = db.insertOrThrow(Utilidades.TABLA_RODALES_RELEVAMIENTO, Utilidades.RODALES_RELEVAMIENTO_id, conValues);
            Long re = resultado;


            if (resultado != -1){
                return true;
            } else {
                return false;
            }

        } catch (SQLiteException e){
            this.set_mensaje(e.getMessage());
            this.set_status(false);
            return false;
        }

    }

    public Boolean deleteRodalSelect(Context context, String idrodal){

        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        db.beginTransaction();
        int res = -1;
        try{


            String[] args = new String[]{idrodal};
            res = db.delete(Utilidades.TABLA_RODALES_RELEVAMIENTO, Utilidades.RODALES_RELEVAMIENTO_id + " = ?", args);
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


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdrodales() {
        return idrodales;
    }

    public void setIdrodales(Integer idrodales) {
        this.idrodales = idrodales;
    }

    public String getCod_sap() {
        return cod_sap;
    }

    public void setCod_sap(String cod_sap) {
        this.cod_sap = cod_sap;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getUso() {
        return uso;
    }

    public void setUso(String uso) {
        this.uso = uso;
    }

    public String getFinalizado() {
        return finalizado;
    }

    public void setFinalizado(String finalizado) {
        this.finalizado = finalizado;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getFecha_plantacion() {
        return fecha_plantacion;
    }

    public void setFecha_plantacion(String fecha_plantacion) {
        this.fecha_plantacion = fecha_plantacion;
    }

    public String getFecha_inv() {
        return fecha_inv;
    }

    public void setFecha_inv(String fecha_inv) {
        this.fecha_inv = fecha_inv;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public Integer getCantidad_parcelas() {
        return cantidad_parcelas;
    }

    public void setCantidad_parcelas(Integer cantidad_parcelas) {
        this.cantidad_parcelas = cantidad_parcelas;
    }

    public ArrayList<ParcelasRelevamientoSQLiteEntity> getListaParcelas() {
        return listaParcelas;
    }

    public void setListaParcelas(ArrayList<ParcelasRelevamientoSQLiteEntity> listaParcelas) {
        this.listaParcelas = listaParcelas;
    }

    public Integer getCantidad_parcelas_no_upload() {
        return cantidad_parcelas_no_upload;
    }

    public void setCantidad_parcelas_no_upload(Integer cantidad_parcelas_no_upload) {
        this.cantidad_parcelas_no_upload = cantidad_parcelas_no_upload;
    }

    public Integer getCantidad_arboles() {
        return cantidad_arboles;
    }

    public void setCantidad_arboles(Integer cantidad_arboles) {
        this.cantidad_arboles = cantidad_arboles;
    }

    public Integer getCantidad_arboles_no_upload() {
        return cantidad_arboles_no_upload;
    }

    public void setCantidad_arboles_no_upload(Integer cantidad_arboles_no_upload) {
        this.cantidad_arboles_no_upload = cantidad_arboles_no_upload;
    }
}
