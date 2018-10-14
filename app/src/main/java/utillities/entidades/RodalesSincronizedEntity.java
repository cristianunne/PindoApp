package utillities.entidades;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

import forestal.app.pindo.pindo.config.RodalesSincronizarConfigFragment;
import forestal.app.pindo.pindo.sqlite.SQLite_OpenHelper;
import utillities.utilidades.Utilidades;
import utillities.utilidades._Default;

public class RodalesSincronizedEntity extends _Default {


    private Integer idrodales;
    private String cod_sap;
    private String campo;
    private String uso;
    private Boolean finalizado;
    private String empresa;
    private String fecha_plantacion;
    private String fecha_inv;
    private String especie;
    private String geometry;
    private Double latitud;
    private Double longitud;

    public RodalesSincronizedEntity() {
        super();

        setGeometry("");
        setLongitud(0.0);
        setLatitud(0.0);

    }

    /*
    TODO: recibe la conexion a SQLITE conexion
     */
    public ArrayList<RodalesSincronizedEntity> getListaRodales(Context context){
        ArrayList<RodalesSincronizedEntity> lista_rodales = null;

        //Abrir la coneccion
        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        db.beginTransaction();

        try{

            String sql = "SELECT * FROM " + Utilidades.TABLA_RODALES + " WHERE " + Utilidades.RODALES_idrodales + " NOT IN (SELECT "  + Utilidades.RODALES_RELEVAMIENTO_idrodales +
                    " FROM " + Utilidades.TABLA_RODALES_RELEVAMIENTO + ")";
            Cursor cursor = db.rawQuery(sql,null);

            if (cursor.getCount() > 0) {

                lista_rodales = new ArrayList<>();

                while (cursor.moveToNext()) {

                    //Creo un nuevo objeto de Rodales
                    RodalesSincronizedEntity rodal = new RodalesSincronizedEntity();
                    rodal.setIdrodales(cursor.getInt(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_idrodales)));
                    rodal.setCod_sap(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_cod_sap)));
                    rodal.setCampo(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_campo)));
                    rodal.setUso(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_uso)));
                    rodal.setGeometry(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_geometry)));

                    //Convierto a Boolean el string
                    Boolean finalizado = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_finalizado)));

                    rodal.setFinalizado(finalizado);

                    rodal.setEmpresa(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_empresa)));
                    rodal.setFecha_plantacion(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_Fecha_plantacion)));
                    rodal.setFecha_inv(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_Fecha_inv)));
                    rodal.setEspecie(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_especie)));

                    //Seteo las coordenadas del centroide de las capas
                    rodal.setLatitud(cursor.getDouble(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_LAT)));
                    rodal.setLongitud(cursor.getDouble(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_LONG)));

                    //Agrego el rodal a la lista
                    lista_rodales.add(rodal);
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



    public RodalesSincronizedEntity getRodalById(Context context, String id_rodal){
        RodalesSincronizedEntity rodal = null;

        //Abrir la coneccion
        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        db.beginTransaction();

        try{

            String sql = "SELECT * FROM " + Utilidades.TABLA_RODALES + " WHERE " + Utilidades.RODALES_idrodales + " = " + id_rodal;
            Cursor cursor = db.rawQuery(sql,null);

            if (cursor.getCount() > 0) {

                rodal = new RodalesSincronizedEntity();
                while (cursor.moveToNext()) {

                    //Creo un nuevo objeto de Rodales

                    rodal.setIdrodales(cursor.getInt(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_idrodales)));
                    rodal.setCod_sap(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_cod_sap)));
                    rodal.setCampo(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_campo)));
                    rodal.setUso(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_uso)));
                    rodal.setGeometry(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_geometry)));

                    //Convierto a Boolean el string
                    Boolean finalizado = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_finalizado)));

                    rodal.setFinalizado(finalizado);
                    rodal.setEmpresa(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_empresa)));
                    rodal.setFecha_plantacion(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_Fecha_plantacion)));
                    rodal.setFecha_inv(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_Fecha_inv)));
                    rodal.setEspecie(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_especie)));

                    //Seteo las coordenadas del centroide de las capas
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


    public ArrayList<RodalesSincronizedEntity> getAllRodales(Context context){
        ArrayList<RodalesSincronizedEntity> listaRodal = null;

        //Abrir la coneccion
        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        db.beginTransaction();

        try{

            String sql = "SELECT * FROM " + Utilidades.TABLA_RODALES;
            Cursor cursor = db.rawQuery(sql,null);

            if (cursor.getCount() > 0) {

                listaRodal = new ArrayList<>();

                RodalesSincronizedEntity rodal = new RodalesSincronizedEntity();
                while (cursor.moveToNext()) {

                    //Creo un nuevo objeto de Rodales

                    rodal.setIdrodales(cursor.getInt(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_idrodales)));
                    rodal.setCod_sap(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_cod_sap)));
                    rodal.setCampo(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_campo)));
                    rodal.setUso(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_uso)));
                    rodal.setGeometry(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_geometry)));

                    //Convierto a Boolean el string
                    Boolean finalizado = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_finalizado)));

                    rodal.setFinalizado(finalizado);
                    rodal.setEmpresa(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_empresa)));
                    rodal.setFecha_plantacion(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_Fecha_plantacion)));
                    rodal.setFecha_inv(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_Fecha_inv)));
                    rodal.setEspecie(cursor.getString(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_especie)));

                    //Seteo las coordenadas del centroide de las capas
                    rodal.setLatitud(cursor.getDouble(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_LAT)));
                    rodal.setLongitud(cursor.getDouble(cursor.getColumnIndex(Utilidades.RODALES_RELEVAMIENTO_LONG)));


                    listaRodal.add(rodal);
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


        return listaRodal;
    }


    /*
        TODO: Método que elimina un rodal de la lista de sincronizados
     */

    public int deleteRodalSincronized(RodalesSincronizedEntity rodal, Context context){

        //Abrir la coneccion
        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        int res = -1;

        try{
            String[] args = new String[]{rodal.getIdrodales().toString()};
            res = db.delete(Utilidades.TABLA_RODALES, "idrodales = ?", args);

            return res;


        }catch (Exception e){
            this._mensaje = e.getMessage();
            this._status = false;
            e.printStackTrace();
            return res;
        }

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

    public Boolean getFinalizado() {
        return finalizado;
    }

    public void setFinalizado(Boolean finalizado) {
        this.finalizado = finalizado;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getFecha_plantacion() {
        return fecha_plantacion;
    }

    public void setFecha_plantacion(String fecha_plantacion) {
        this.fecha_plantacion = fecha_plantacion;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
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

    public String getFecha_inv() {
        return fecha_inv;
    }

    public void setFecha_inv(String fecha_inv) {
        this.fecha_inv = fecha_inv;
    }
}
