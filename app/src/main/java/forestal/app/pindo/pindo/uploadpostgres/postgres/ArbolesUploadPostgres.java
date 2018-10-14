package forestal.app.pindo.pindo.uploadpostgres.postgres;

import java.sql.ResultSet;

import postgres.conexion.DBPostgresConnection;
import utillities.entidades.ArbolesRelevamientoEntity;
import utillities.utilidades.CamposPostgres;
import utillities.utilidades._Default;

public class ArbolesUploadPostgres extends _Default {

    public ArbolesUploadPostgres() {
        super();
    }

    public boolean uploadArbolesRel(DBPostgresConnection DB, ArbolesRelevamientoEntity arboles){


        try{

            //Transformo cosechado
            boolean cosechado = false;

            if(arboles.getCosechado() != null){
                if(arboles.getCosechado().equals("SI")){
                    cosechado = true;
                }
            }



            String fecha_rel = "";
            if(!arboles.getFecha_rel().equals("")){
                fecha_rel = arboles.getFecha_rel();
            }

            String point = "ST_GeomFromText('POINT(" + arboles.getLongi() + " " + arboles.getLat() + ")', 4326)";
            String sql = "INSERT INTO " + CamposPostgres.TABLA_ARBOLES + "(" + CamposPostgres.ARBOLES_marca + ", " + CamposPostgres.ARBOLES_dap + ", " + CamposPostgres.ARBOLES_altura +
                    ", " + CamposPostgres.ARBOLES_altura_poda + ", " + CamposPostgres.ARBOLES_calidad + ", " + CamposPostgres.ARBOLES_cosechado + ", " + CamposPostgres.ARBOLES_dmsm + ", "
                    + CamposPostgres.ARBOLES_idparcela + ", " + CamposPostgres.ARBOLES_geom + ", " + CamposPostgres.ARBOLES_fecha_rel +  ") VALUES ('" + arboles.getMarca() + "', " + arboles.getDap() +
                    ", " + arboles.getAltura() + ", " + arboles.getAlturapoda() + ", '" + arboles.getCalidad() + "', " + cosechado + ", " +
                    arboles.getDmsm() + ", " + arboles.getIdparcela() + ", " + point + ", '" +  fecha_rel + "')";

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

    public Integer getIdLastArbolAdd(DBPostgresConnection DB){

        Integer result = -1;

        String sql = "SELECT " + CamposPostgres.ARBOLES_idarbol + " FROM " + CamposPostgres.TABLA_ARBOLES +
                " ORDER BY " + CamposPostgres.ARBOLES_idarbol + " DESC LIMIT 1";


        try{
            //Ejecuto el query
            ResultSet resultSet = DB.select(sql);

            if(resultSet != null){

                while (resultSet.next()){
                    result = resultSet.getInt(CamposPostgres.ARBOLES_idarbol);
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
}
