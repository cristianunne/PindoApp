package utillities.entidades;

import android.util.Log;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;

import postgres.conexion.DBPostgresConnection;
import utillities.utilidades._Default;

/***
 * Clase que maneja al Rodal como Entidad traido de POSTGRES
 */

public class RodalesEntity extends _Default {


    private Integer idrodales;
    private String cod_sap;
    private String campo;
    private String uso;
    private Boolean finalizado;
    private String empresa;
    private String fecha_plantacion;
    private String especie;
    private String geometry;
    private Double latitud;
    private Double longitud;




    public RodalesEntity() {
        //inicializar la clase Padre
        super();

        //Seteo las variables de Clase
        this.idrodales = -1;
        this.cod_sap = "";
        this.campo = "";
        this.uso = "";
        this.finalizado = false;
        this.empresa = "";
        this.geometry = "";
    }

    public ArrayList<RodalesEntity> getListaRodales(DBPostgresConnection db) {

        //Llamo a la calse que maneja la consulta a Postgres
        //DBPostgresConnection db = new DBPostgresConnection(config);


        ArrayList<RodalesEntity> lista = new ArrayList<>();

        try{
            ResultSet result = db.select("SELECT rodales.*, empresa.nombre, plantaciones.fecha, procedencias.especie FROM rodales INNER JOIN empresa ON rodales.empresa_idempresa = empresa.idempresa " +
            "INNER JOIN plantaciones ON rodales.idrodales = plantaciones.rodales_idrodales INNER JOIN procedencias ON plantaciones.procedencias_idprocedencias = procedencias.idprocedencias");

            //Evaluo si el resultado estuvo ok
            if (result != null){

                while (result.next()){
                    //Creo un nuevo objeto de Rodales
                    RodalesEntity rodal = new RodalesEntity();
                    rodal.setIdrodales(result.getInt("idrodales"));
                    rodal.setCod_sap(result.getString("cod_sap"));
                    rodal.setCampo(result.getString("campo"));
                    rodal.setUso(result.getString("uso"));
                    rodal.setFinalizado(result.getBoolean("finalizado"));
                    rodal.setEmpresa(result.getString("nombre"));
                    rodal.setFecha_plantacion(result.getString("fecha"));
                    rodal.setEspecie(result.getString("especie"));
                    //Agrego el objeto a la lista
                    lista.add(rodal);
                    rodal = null;
                }
            }
        } catch (Exception e){

            this._mensaje = e.getMessage();
            this._status = false;
        }

        return lista;
    }

    /**
     * Metodo que llena el arreglo con la clase geometria
     * @param db
     * @param rodales
     * @return
     */
    public ArrayList<RodalesEntity> getListaRodalesWithGeometry(DBPostgresConnection db, ArrayList<RodalesEntity> rodales){

        ArrayList<RodalesEntity> lista_rodal_aux = new ArrayList<>();

        try {

            String sql = "SELECT rodales_idrodales AS idrodales, ST_X(ST_CENTROID(st_union(ST_Transform(geom, 4326)))) AS longI, " +
                    "ST_Y(ST_CENTROID(st_union(ST_Transform(geom, 4326)))) AS lat, ST_AsGeoJSON(st_union(ST_Transform(geom, 4326))) AS geometry FROM gis GROUP BY rodales_idrodales;";

            //ResultSet result = db.select("SELECT rodales_idrodales AS idrodales, ST_AsGeoJSON(st_union(st_transform(geom, 4326))) AS geometry FROM gis GROUP BY rodales_idrodales;");
            ResultSet result = db.select(sql);
            db.desconectar();

            if (result != null){
                while (result.next()){
                    //Creo un nuevo objeto de Rodales
                    RodalesEntity rodal = new RodalesEntity();
                    rodal.setIdrodales(result.getInt("idrodales"));
                    rodal.setGeometry(result.getString("geometry"));

                    rodal.setLatitud(result.getDouble("lat"));
                    rodal.setLongitud(result.getDouble("longi"));

                    //Agrego el objeto a la lista
                    lista_rodal_aux.add(rodal);
                    rodal = null;
                }

                //Procedo a llenar la lista originar con las geometrias

                for (RodalesEntity rodal_ori : rodales){

                    for (RodalesEntity rodal_with_geom : lista_rodal_aux){

                        if (rodal_ori.getIdrodales() == rodal_with_geom.getIdrodales()){

                            rodal_ori.setGeometry(rodal_with_geom.getGeometry());
                            rodal_ori.setLatitud(rodal_with_geom.getLatitud());
                            rodal_ori.setLongitud(rodal_with_geom.getLongitud());
                        }
                    }

                }
            }
        } catch (Exception e){
            this._mensaje = e.getMessage();
            this._status = false;
        }

        return rodales;
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
}
