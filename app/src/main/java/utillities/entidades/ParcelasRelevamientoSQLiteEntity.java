package utillities.entidades;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import forestal.app.pindo.pindo.sqlite.SQLite_OpenHelper;
import utillities.utilidades.Utilidades;
import utillities.utilidades._Default;

public class ParcelasRelevamientoSQLiteEntity extends _Default {

    private Integer id; //
    private String cod_sap; //
    private Integer idparcela; //
    private Double superficie; //
    private Double pendiente; //
    private Double lat; //
    private Double longi; //
    private String comentarios; //
    private String geometry; //
    private Integer idrodales; //
    private Integer releva; //
    private Integer cantidad_arboles;
    private Integer sincronizado;
    private Integer idpostgres;
    private String tipo;

    private ArrayList<ArbolesRelevamientoEntity> listaArboles;


    public ParcelasRelevamientoSQLiteEntity() {
        super();
        setCantidad_arboles(0);
    }

    public ArrayList<ParcelasRelevamientoSQLiteEntity> getAllParcelasRelevamiento(Context context){

        ArrayList<ParcelasRelevamientoSQLiteEntity> lista = null;

        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        db.beginTransaction();

        try{
            String sql = "SELECT * FROM " + Utilidades.TABLA_PARCELAS_REL;

            Cursor cursor = db.rawQuery(sql,null);

            if (cursor.getCount() > 0) {

                lista = new ArrayList<>();

                while (cursor.moveToNext()){
                    ParcelasRelevamientoSQLiteEntity parcela = new ParcelasRelevamientoSQLiteEntity();

                    parcela.setId(cursor.getInt(cursor.getColumnIndex(Utilidades.PARCELAS_REL_id)));
                    parcela.setCod_sap(cursor.getString(cursor.getColumnIndex(Utilidades.PARCELAS_REL_cod_sap)));
                    parcela.setIdparcela(cursor.getInt(cursor.getColumnIndex(Utilidades.PARCELAS_REL_idparcelarel)));
                    parcela.setSuperficie(cursor.getDouble(cursor.getColumnIndex(Utilidades.PARCELAS_REL_superficie)));
                    parcela.setPendiente(cursor.getDouble(cursor.getColumnIndex(Utilidades.PARCELAS_REL_pendiente)));
                    parcela.setLat(cursor.getDouble(cursor.getColumnIndex(Utilidades.PARCELAS_REL_lat)));
                    parcela.setLongi(cursor.getDouble(cursor.getColumnIndex(Utilidades.PARCELAS_REL_longi)));
                    parcela.setComentarios(cursor.getString(cursor.getColumnIndex(Utilidades.PARCELAS_REL_comentarios)));
                    parcela.setGeometry(cursor.getString(cursor.getColumnIndex(Utilidades.PARCELAS_REL_geometry)));
                    parcela.setIdrodales(cursor.getInt(cursor.getColumnIndex(Utilidades.PARCELAS_REL_idrodal)));
                    parcela.setSincronizado(cursor.getInt(cursor.getColumnIndex(Utilidades.PARCELAS_REL_sincronizado)));
                    parcela.setIdpostgres(cursor.getInt(cursor.getColumnIndex(Utilidades.PARCELAS_REL_idpostgres)));
                    parcela.setTipo(cursor.getString(cursor.getColumnIndex(Utilidades.PARCELAS_REL_tipo)));

                    lista.add(parcela);
                    parcela = null;

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


    public ArrayList<ParcelasRelevamientoSQLiteEntity> getAllParcelasRelevamientoByIdRodal(Context context, String idrodal){

        ArrayList<ParcelasRelevamientoSQLiteEntity> lista = null;

        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        db.beginTransaction();

        try{
            String sql = "SELECT * FROM " + Utilidades.TABLA_PARCELAS_REL + " WHERE " + Utilidades.PARCELAS_REL_idrodal + " = " + idrodal;

            Cursor cursor = db.rawQuery(sql,null);

            if (cursor.getCount() > 0) {

                lista = new ArrayList<>();

                while (cursor.moveToNext()){
                    ParcelasRelevamientoSQLiteEntity parcela = new ParcelasRelevamientoSQLiteEntity();

                    parcela.setId(cursor.getInt(cursor.getColumnIndex(Utilidades.PARCELAS_REL_id)));
                    parcela.setCod_sap(cursor.getString(cursor.getColumnIndex(Utilidades.PARCELAS_REL_cod_sap)));
                    parcela.setIdparcela(cursor.getInt(cursor.getColumnIndex(Utilidades.PARCELAS_REL_idparcelarel)));
                    parcela.setSuperficie(cursor.getDouble(cursor.getColumnIndex(Utilidades.PARCELAS_REL_superficie)));
                    parcela.setPendiente(cursor.getDouble(cursor.getColumnIndex(Utilidades.PARCELAS_REL_pendiente)));
                    parcela.setLat(cursor.getDouble(cursor.getColumnIndex(Utilidades.PARCELAS_REL_lat)));
                    parcela.setLongi(cursor.getDouble(cursor.getColumnIndex(Utilidades.PARCELAS_REL_longi)));
                    parcela.setComentarios(cursor.getString(cursor.getColumnIndex(Utilidades.PARCELAS_REL_comentarios)));
                    parcela.setGeometry(cursor.getString(cursor.getColumnIndex(Utilidades.PARCELAS_REL_geometry)));
                    parcela.setIdrodales(cursor.getInt(cursor.getColumnIndex(Utilidades.PARCELAS_REL_idrodal)));
                    parcela.setSincronizado(cursor.getInt(cursor.getColumnIndex(Utilidades.PARCELAS_REL_sincronizado)));
                    parcela.setIdpostgres(cursor.getInt(cursor.getColumnIndex(Utilidades.PARCELAS_REL_idpostgres)));
                    parcela.setTipo(cursor.getString(cursor.getColumnIndex(Utilidades.PARCELAS_REL_tipo)));

                    lista.add(parcela);
                    parcela = null;

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

    public ArrayList<ParcelasRelevamientoSQLiteEntity> getParcelasRelevamientoByIdRodal(Context context, String idrodal){

        ArrayList<ParcelasRelevamientoSQLiteEntity> lista = null;

        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        db.beginTransaction();

        try{
            String sql = "SELECT * FROM " + Utilidades.TABLA_PARCELAS_REL + " WHERE " + Utilidades.PARCELAS_REL_idrodal + " = " + idrodal + " AND " + Utilidades.PARCELAS_REL_releva + " = 0";

            Cursor cursor = db.rawQuery(sql,null);

            if (cursor.getCount() > 0) {

                lista = new ArrayList<>();

                while (cursor.moveToNext()){
                    ParcelasRelevamientoSQLiteEntity parcela = new ParcelasRelevamientoSQLiteEntity();

                    parcela.setId(cursor.getInt(cursor.getColumnIndex(Utilidades.PARCELAS_REL_id)));
                    parcela.setCod_sap(cursor.getString(cursor.getColumnIndex(Utilidades.PARCELAS_REL_cod_sap)));
                    parcela.setIdparcela(cursor.getInt(cursor.getColumnIndex(Utilidades.PARCELAS_REL_idparcelarel)));
                    parcela.setSuperficie(cursor.getDouble(cursor.getColumnIndex(Utilidades.PARCELAS_REL_superficie)));
                    parcela.setPendiente(cursor.getDouble(cursor.getColumnIndex(Utilidades.PARCELAS_REL_pendiente)));
                    parcela.setLat(cursor.getDouble(cursor.getColumnIndex(Utilidades.PARCELAS_REL_lat)));
                    parcela.setLongi(cursor.getDouble(cursor.getColumnIndex(Utilidades.PARCELAS_REL_longi)));
                    parcela.setComentarios(cursor.getString(cursor.getColumnIndex(Utilidades.PARCELAS_REL_comentarios)));
                    parcela.setGeometry(cursor.getString(cursor.getColumnIndex(Utilidades.PARCELAS_REL_geometry)));
                    parcela.setIdrodales(cursor.getInt(cursor.getColumnIndex(Utilidades.PARCELAS_REL_idrodal)));
                    parcela.setSincronizado(cursor.getInt(cursor.getColumnIndex(Utilidades.PARCELAS_REL_sincronizado)));
                    parcela.setIdpostgres(cursor.getInt(cursor.getColumnIndex(Utilidades.PARCELAS_REL_idpostgres)));
                    parcela.setTipo(cursor.getString(cursor.getColumnIndex(Utilidades.PARCELAS_REL_tipo)));

                    lista.add(parcela);
                    parcela = null;

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

    public ArrayList<ParcelasRelevamientoSQLiteEntity> getParcelasRelevamientoSelectByIdRodal(Context context, String idrodal){

        ArrayList<ParcelasRelevamientoSQLiteEntity> lista = null;

        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        db.beginTransaction();

        try{
            String sql = "SELECT * FROM " + Utilidades.TABLA_PARCELAS_REL + " WHERE " + Utilidades.PARCELAS_REL_idrodal + " = " + idrodal + " AND "
                    + Utilidades.PARCELAS_REL_releva + " = 1 ORDER BY " + Utilidades.PARCELAS_REL_idparcelarel + " ASC";

            Cursor cursor = db.rawQuery(sql,null);

            if (cursor.getCount() > 0) {

                lista = new ArrayList<>();

                while (cursor.moveToNext()){
                    ParcelasRelevamientoSQLiteEntity parcela = new ParcelasRelevamientoSQLiteEntity();

                    parcela.setId(cursor.getInt(cursor.getColumnIndex(Utilidades.PARCELAS_REL_id)));
                    parcela.setCod_sap(cursor.getString(cursor.getColumnIndex(Utilidades.PARCELAS_REL_cod_sap)));
                    parcela.setIdparcela(cursor.getInt(cursor.getColumnIndex(Utilidades.PARCELAS_REL_idparcelarel)));
                    parcela.setSuperficie(cursor.getDouble(cursor.getColumnIndex(Utilidades.PARCELAS_REL_superficie)));
                    parcela.setPendiente(cursor.getDouble(cursor.getColumnIndex(Utilidades.PARCELAS_REL_pendiente)));
                    parcela.setLat(cursor.getDouble(cursor.getColumnIndex(Utilidades.PARCELAS_REL_lat)));
                    parcela.setLongi(cursor.getDouble(cursor.getColumnIndex(Utilidades.PARCELAS_REL_longi)));
                    parcela.setComentarios(cursor.getString(cursor.getColumnIndex(Utilidades.PARCELAS_REL_comentarios)));
                    parcela.setGeometry(cursor.getString(cursor.getColumnIndex(Utilidades.PARCELAS_REL_geometry)));
                    parcela.setIdrodales(cursor.getInt(cursor.getColumnIndex(Utilidades.PARCELAS_REL_idrodal)));
                    parcela.setSincronizado(cursor.getInt(cursor.getColumnIndex(Utilidades.PARCELAS_REL_sincronizado)));
                    parcela.setIdpostgres(cursor.getInt(cursor.getColumnIndex(Utilidades.PARCELAS_REL_idpostgres)));
                    parcela.setTipo(cursor.getString(cursor.getColumnIndex(Utilidades.PARCELAS_REL_tipo)));


                    lista.add(parcela);
                    parcela = null;

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

    public ArrayList<ParcelasRelevamientoSQLiteEntity> getParcelasRelevamientoSelectNoUpdateByIdRodal(Context context, String idrodal){

        ArrayList<ParcelasRelevamientoSQLiteEntity> lista = null;

        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        db.beginTransaction();

        try{
            String sql = "SELECT * FROM " + Utilidades.TABLA_PARCELAS_REL + " WHERE " + Utilidades.PARCELAS_REL_idrodal + " = " + idrodal + " AND "
                    + Utilidades.PARCELAS_REL_releva + " = 1 AND " + Utilidades.PARCELAS_REL_sincronizado + " = 0" +
                    " ORDER BY " + Utilidades.PARCELAS_REL_idparcelarel + " ASC";

            Cursor cursor = db.rawQuery(sql,null);

            if (cursor.getCount() > 0) {

                lista = new ArrayList<>();

                while (cursor.moveToNext()){
                    ParcelasRelevamientoSQLiteEntity parcela = new ParcelasRelevamientoSQLiteEntity();

                    parcela.setId(cursor.getInt(cursor.getColumnIndex(Utilidades.PARCELAS_REL_id)));
                    parcela.setCod_sap(cursor.getString(cursor.getColumnIndex(Utilidades.PARCELAS_REL_cod_sap)));
                    parcela.setIdparcela(cursor.getInt(cursor.getColumnIndex(Utilidades.PARCELAS_REL_idparcelarel)));
                    parcela.setSuperficie(cursor.getDouble(cursor.getColumnIndex(Utilidades.PARCELAS_REL_superficie)));
                    parcela.setPendiente(cursor.getDouble(cursor.getColumnIndex(Utilidades.PARCELAS_REL_pendiente)));
                    parcela.setLat(cursor.getDouble(cursor.getColumnIndex(Utilidades.PARCELAS_REL_lat)));
                    parcela.setLongi(cursor.getDouble(cursor.getColumnIndex(Utilidades.PARCELAS_REL_longi)));
                    parcela.setComentarios(cursor.getString(cursor.getColumnIndex(Utilidades.PARCELAS_REL_comentarios)));
                    parcela.setGeometry(cursor.getString(cursor.getColumnIndex(Utilidades.PARCELAS_REL_geometry)));
                    parcela.setIdrodales(cursor.getInt(cursor.getColumnIndex(Utilidades.PARCELAS_REL_idrodal)));
                    parcela.setSincronizado(cursor.getInt(cursor.getColumnIndex(Utilidades.PARCELAS_REL_sincronizado)));
                    parcela.setIdpostgres(cursor.getInt(cursor.getColumnIndex(Utilidades.PARCELAS_REL_idpostgres)));
                    parcela.setTipo(cursor.getString(cursor.getColumnIndex(Utilidades.PARCELAS_REL_tipo)));


                    lista.add(parcela);
                    parcela = null;

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

    public Map<Integer, Integer> getCantidadParcelasByIdRodal(Context context){

        Map<Integer, Integer> lista = null;


        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        db.beginTransaction();

        try{
            String sql = "SELECT " + Utilidades.PARCELAS_REL_idrodal + ", COUNT(*) AS cantidad FROM " + Utilidades.TABLA_PARCELAS_REL
                    + " WHERE " + Utilidades.PARCELAS_REL_releva + " = 1"
                    + " GROUP BY " + Utilidades.PARCELAS_REL_idrodal;

            Cursor cursor = db.rawQuery(sql,null);

            if (cursor.getCount() > 0) {

                lista = new HashMap<Integer, Integer>();

                while (cursor.moveToNext()){

                    lista.put(cursor.getInt(cursor.getColumnIndex(Utilidades.PARCELAS_REL_idrodal)), cursor.getInt(cursor.getColumnIndex("cantidad")));

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

    public Map<Integer, Integer> getCantidadParcelasNoUploadByIdRodal(Context context){

        Map<Integer, Integer> lista = null;


        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        db.beginTransaction();

        try{
            String sql = "SELECT " + Utilidades.PARCELAS_REL_idrodal + ", COUNT(*) AS cantidad FROM " + Utilidades.TABLA_PARCELAS_REL
                    + " WHERE " + Utilidades.PARCELAS_REL_releva + " = 1 AND " + Utilidades.PARCELAS_REL_sincronizado + " = 0"
                    + " GROUP BY " + Utilidades.PARCELAS_REL_idrodal;

            Cursor cursor = db.rawQuery(sql,null);

            if (cursor.getCount() > 0) {

                lista = new HashMap<Integer, Integer>();

                while (cursor.moveToNext()){

                    lista.put(cursor.getInt(cursor.getColumnIndex(Utilidades.PARCELAS_REL_idrodal)), cursor.getInt(cursor.getColumnIndex("cantidad")));

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


    public Boolean editarParcelaById(Context context, ParcelasRelevamientoSQLiteEntity parcela){

        Boolean res = false;

        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        //db.beginTransaction();

        try{
            Integer res_= -1;
            ContentValues values = new ContentValues();
            values.put(Utilidades.PARCELAS_REL_superficie, parcela.getSuperficie());
            values.put(Utilidades.PARCELAS_REL_pendiente, parcela.getPendiente());
            values.put(Utilidades.PARCELAS_REL_comentarios, parcela.getComentarios());
            values.put(Utilidades.PARCELAS_REL_tipo, parcela.getTipo());

            values.put(Utilidades.PARCELAS_REL_lat, parcela.getLat());
            values.put(Utilidades.PARCELAS_REL_longi, parcela.getLongi());

            res_ = db.update(Utilidades.TABLA_PARCELAS_REL, values, Utilidades.PARCELAS_REL_id + " = " + parcela.getId(), null);


            if(res_ > 0){
                return true;
            }


            //db.setTransactionSuccessful();

        } catch (SQLiteException e){
            this._mensaje = e.getMessage();
            this._status = false;
        } finally {
            //db.endTransaction();
        }



        return res;
    }

    /**
     * TODO: Recibe el contexto y la parcela a insertar para el relevamiento
     * @param context
     * @param parcela
     * @return
     */
    public Boolean insertParcelasRelevamiento(Context context, ParcelasRelevamientoSQLiteEntity parcela){


        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();


        ContentValues conValues = new ContentValues();
        conValues.put(Utilidades.PARCELAS_REL_idrodal, parcela.getIdrodales());
        conValues.put(Utilidades.PARCELAS_REL_cod_sap, parcela.getCod_sap());
        conValues.put(Utilidades.PARCELAS_REL_idparcelarel, parcela.getIdparcela());
        conValues.put(Utilidades.PARCELAS_REL_superficie, parcela.getSuperficie());
        conValues.put(Utilidades.PARCELAS_REL_pendiente, parcela.getPendiente());
        conValues.put(Utilidades.PARCELAS_REL_lat, parcela.getLat());
        conValues.put(Utilidades.PARCELAS_REL_longi, parcela.getLongi());
        conValues.put(Utilidades.PARCELAS_REL_comentarios, parcela.getComentarios());
        conValues.put(Utilidades.PARCELAS_REL_releva, parcela.getReleva());
        conValues.put(Utilidades.PARCELAS_REL_tipo, parcela.getTipo());


        //Al insertar no agrego la variable para sincronized porque eso se maneja en la clase SincronizarParcelasSQlite

        long resultado = -1;
        try {


            resultado = db.insertOrThrow(Utilidades.TABLA_PARCELAS_REL, Utilidades.PARCELAS_REL_id, conValues);


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

    public Boolean setParcelaRelevamientoToTrue(Context context, String id_parcela){


        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        db.beginTransaction();

        try{

            ContentValues values = new ContentValues();
            values.put(Utilidades.PARCELAS_REL_releva, "1");
            Integer res = db.update(Utilidades.TABLA_PARCELAS_REL, values, Utilidades.PARCELAS_REL_id + " = " + id_parcela, null);

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

    /**
     *Setea en SQLITE los campos sincronizados y el id en postgres
     * @param context
     * @param id_in_poatgres
     * @return
     */
    public Boolean setSincronizedParcelaRelevamientoToTrue(Context context, Integer id_in_poatgres, Integer parcela_id){


        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        db.beginTransaction();

        try{

            ContentValues values = new ContentValues();
            values.put(Utilidades.PARCELAS_REL_sincronizado, "1");
            values.put(Utilidades.PARCELAS_REL_idpostgres, id_in_poatgres);
            Integer res = db.update(Utilidades.TABLA_PARCELAS_REL, values, Utilidades.PARCELAS_REL_id + " = " + parcela_id, null);

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

    public Integer getCantidadByIdRodal(Context context, String idrodal){

        Integer cantidad = 0;

        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        db.beginTransaction();

        try{

            String sql = "SELECT COUNT(*) AS cantidad FROM " + Utilidades.TABLA_PARCELAS_REL + " WHERE " + Utilidades.PARCELAS_REL_idrodal + " = " + idrodal;
            Cursor cursor = db.rawQuery(sql,null);

            if (cursor.getCount() > 0) {

                while (cursor.moveToNext()){

                    cantidad = cursor.getInt(cursor.getColumnIndex("cantidad"));
                    break;
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

    public Integer getCantidadParcelasSelectByIdRodal(Context context, String idrodal){
        Integer cantidad = 0;

        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        db.beginTransaction();

        try{

            String sql = "SELECT COUNT(*) AS cantidad FROM " + Utilidades.TABLA_PARCELAS_REL + " WHERE " +
                    Utilidades.PARCELAS_REL_idrodal + " = " + idrodal + " AND " + Utilidades.PARCELAS_REL_releva + " = 1";
            Cursor cursor = db.rawQuery(sql,null);

            if (cursor.getCount() > 0) {

                while (cursor.moveToNext()){

                    cantidad = cursor.getInt(cursor.getColumnIndex("cantidad"));
                    break;
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

    public Boolean changeParcelaToNoRelevamiento(Context context, String idparcela){

        Boolean res = false;

        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        //db.beginTransaction();

        try{
            Integer res_= -1;



            res_ = db.delete(Utilidades.TABLA_PARCELAS_REL,Utilidades.PARCELAS_REL_id + " = " + idparcela, null);


            if(res_ > 0){
                return true;
            }


            //db.setTransactionSuccessful();

        } catch (SQLiteException e){
            this._mensaje = e.getMessage();
            this._status = false;
        } finally {
            //db.endTransaction();
        }



        return res;

    }

    public int deleteParcelasRelevamientoByRodal(String idrodal, Context context){

        //Abrir la coneccion
        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        int res = -1;

        try{
            String[] args = new String[]{idrodal};
            res = db.delete(Utilidades.TABLA_PARCELAS_REL, Utilidades.PARCELAS_REL_idrodal + " = ?", args);

            return res;


        }catch (Exception e){
            this._mensaje = e.getMessage();
            this._status = false;
            e.printStackTrace();
            return res;
        }

    }

    /**
     *
     * @param context
     * @param idparcela corresponde al id autoincremental en SQLite
     * @return true si es > 0 o false si es < 0
     */
    public Boolean deleteParcelaById(Context context, String idparcela){
        //Abrir la coneccion
        SQLite_OpenHelper conn = new SQLite_OpenHelper(context, Utilidades.DB_NAME, null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        int res = -1;

        try{
            String[] args = new String[]{idparcela};
            res = db.delete(Utilidades.TABLA_PARCELAS_REL, Utilidades.PARCELAS_REL_id + " = ?", args);

           if (res > 0){
               return true;
           } else {
               return false;
           }


        }catch (Exception e){
            this._mensaje = e.getMessage();
            this._status = false;
            e.printStackTrace();
        }


        return false;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCod_sap() {
        return cod_sap;
    }

    public void setCod_sap(String cod_sap) {
        this.cod_sap = cod_sap;
    }

    public Integer getIdparcela() {
        return idparcela;
    }

    public void setIdparcela(Integer idparcela) {
        this.idparcela = idparcela;
    }

    public Double getSuperficie() {
        return superficie;
    }

    public void setSuperficie(Double superficie) {
        this.superficie = superficie;
    }

    public Double getPendiente() {
        return pendiente;
    }

    public void setPendiente(Double pendiente) {
        this.pendiente = pendiente;
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

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public Integer getIdrodales() {
        return idrodales;
    }

    public void setIdrodales(Integer idrodales) {
        this.idrodales = idrodales;
    }

    public Integer getReleva() {
        return releva;
    }

    public void setReleva(Integer releva) {
        this.releva = releva;
    }

    public Integer getCantidad_arboles() {
        return cantidad_arboles;
    }

    public void setCantidad_arboles(Integer cantidad_arboles) {
        this.cantidad_arboles = cantidad_arboles;
    }

    public ArrayList<ArbolesRelevamientoEntity> getListaArboles() {
        return listaArboles;
    }

    public void setListaArboles(ArrayList<ArbolesRelevamientoEntity> listaArboles) {
        this.listaArboles = listaArboles;
    }

    public Integer getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(Integer sincronizado) {
        this.sincronizado = sincronizado;
    }

    public Integer getIdpostgres() {
        return idpostgres;
    }

    public void setIdpostgres(Integer idpostgres) {
        this.idpostgres = idpostgres;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
