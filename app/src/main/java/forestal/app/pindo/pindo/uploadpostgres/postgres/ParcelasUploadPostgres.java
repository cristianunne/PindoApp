package forestal.app.pindo.pindo.uploadpostgres.postgres;

import android.util.Log;

import java.sql.ResultSet;

import postgres.conexion.DBPostgresConnection;
import utillities.entidades.ParcelasRelevamientoSQLiteEntity;
import utillities.utilidades.CamposPostgres;
import utillities.utilidades._Default;

public class ParcelasUploadPostgres extends _Default {

    public ParcelasUploadPostgres() {
        super();
    }



    public boolean uploadParcelaRel(DBPostgresConnection DB, ParcelasRelevamientoSQLiteEntity parcela){

        try{

            String point = "ST_GeomFromText('POINT(" + parcela.getLongi() + " " + parcela.getLat() + ")', 4326)";

            String sql = "INSERT INTO " + CamposPostgres.TABLA_PARCELAS_REL + "( " + CamposPostgres.TABLA_PARCELAS_REL_rodales_idrodales + ", "
                    + CamposPostgres.TABLA_PARCELAS_REL_geom + ", " + CamposPostgres.TABLA_PARCELAS_REL_comentarios +
                    ") VALUES (" + parcela.getIdrodales() + ", " + point + ", '" + parcela.getComentarios() + "')";

            Integer cant = DB.executeUpdate(sql);

            if (cant != null && cant > 0){

                return true;
            }

        } catch (Exception e){
            this._mensaje = e.getMessage();
            this._status = false;
            e.printStackTrace();
        }

        this._mensaje = DB.getMsj_error();


        return false;
    }

    /**
     * Devuelve un Integer representando el id de la ultima parcela agregada
     * @param DB
     * @return Integer > 0 representa id de last add
     */

    public Integer getIdLastParcelaAdd(DBPostgresConnection DB){

        Integer result = -1;

        String sql = "SELECT " + CamposPostgres.TABLA_PARCELAS_REL_idparcelarel + " FROM " + CamposPostgres.TABLA_PARCELAS_REL +
                " ORDER BY " + CamposPostgres.TABLA_PARCELAS_REL_idparcelarel + " DESC LIMIT 1";


        try{
            //Ejecuto el query
            ResultSet resultSet = DB.select(sql);

            if(resultSet != null){

                while (resultSet.next()){
                    result = resultSet.getInt(CamposPostgres.TABLA_PARCELAS_REL_idparcelarel);
                    break;
                }

                return result;

            }



        } catch (Exception e) {
            this._status = false;
            this._mensaje = e.getMessage();
            e.printStackTrace();
        }



        return result;
    }

    public boolean removeLastAdd(DBPostgresConnection DB, Integer id_last_add){

        try{

            String sql = "DELETE FROM " + CamposPostgres.TABLA_PARCELAS_REL  + " WHERE " + CamposPostgres.TABLA_PARCELAS_REL_idparcelarel + " = " + id_last_add;

            Integer cant = DB.executeUpdate(sql);

            if (cant != null && cant > 0){

                return true;
            }

        } catch (Exception e){
            this._mensaje = e.getMessage();
            this._status = false;
            e.printStackTrace();
        }
        return false;
    }

}
