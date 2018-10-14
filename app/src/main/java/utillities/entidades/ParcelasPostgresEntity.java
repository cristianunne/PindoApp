package utillities.entidades;

import java.sql.ResultSet;
import java.util.ArrayList;

import postgres.conexion.DBPostgresConnection;
import utillities.utilidades.CamposPostgres;
import utillities.utilidades._Default;

/**
 * Esta Clase se encarga de sincronizar las parcelas desde la Base de Datos Postgres.. Tabla gis
 */
public class ParcelasPostgresEntity extends _Default {

    /**
     * La PENDIENTE y los COMENTARIOS se cargan en Campo
     */

    private Integer idrodalpostgres;
    private Integer idrodalsqlite;
    private Integer idgisparcela;
    private String cod_sap;
    private Double superficie;
    private Double pendiente; //se carga en campo
    private Double lat;
    private Double longi;
    private String comentarios;
    private String tipo;
    private String geometria; //Genero otro metodo para calcular esto


    public ParcelasPostgresEntity() {
        super();
    }


    public ArrayList<ParcelasPostgresEntity> getListaDeParcelasPostgres(DBPostgresConnection db, String rodal_id){

        ArrayList<ParcelasPostgresEntity> lista_parcelas = null;

        try{

           ResultSet result = db.select("SELECT *, cod_sap FROM " + CamposPostgres.TABLA_PARCELAS_REL +
                    " INNER JOIN " + CamposPostgres.TABLA_RODALES + " ON " +
                   CamposPostgres.TABLA_PARCELAS_REL + "." + CamposPostgres.TABLA_PARCELAS_REL_rodales_idrodales + " = " + CamposPostgres.TABLA_RODALES + "." + CamposPostgres.TABLA_RODALES_idrodales +
                   " WHERE " + CamposPostgres.TABLA_PARCELAS_REL_rodales_idrodales + " = " + rodal_id);

            if (result != null){

                lista_parcelas = new ArrayList<>();

                while (result.next()){

                    ParcelasPostgresEntity parcelas_post = new ParcelasPostgresEntity();
                    parcelas_post.setIdgisparcela(result.getInt(CamposPostgres.TABLA_PARCELAS_REL_idparcelarel));
                    parcelas_post.setIdrodalpostgres(result.getInt(CamposPostgres.TABLA_PARCELAS_REL_rodales_idrodales));
                    parcelas_post.setCod_sap(result.getString(CamposPostgres.TABLA_RODALES_cod_sap));
                    parcelas_post.setComentarios(result.getString(CamposPostgres.TABLA_PARCELAS_REL_comentarios));
                    parcelas_post.setTipo(result.getString(CamposPostgres.TABLA_PARCELAS_REL_tipo));

                    lista_parcelas.add(parcelas_post);
                    parcelas_post = null;
                }
            }



        } catch (Exception e){
            this._mensaje = e.getMessage();
            this._status = false;
        }


        return lista_parcelas;

    }

    public ArrayList<ParcelasPostgresEntity> getListaParcelasWithGeometry(ArrayList<ParcelasPostgresEntity> lista_original, DBPostgresConnection db, String rodal_id){

        ArrayList<ParcelasPostgresEntity> lista_parc = new ArrayList<>();

        String json_sql = "ST_AsGeoJSON(geom)::json AS geometry";

        String coord_lat = "ST_Y(geom) AS lat";
        String coord_longi = "ST_X(geom) AS longi";

        String sql = "SELECT id_parcelas_rel, " + coord_lat + ", " + coord_longi + ", " + json_sql + " FROM parcelas_rel WHERE rodales_idrodales = " + rodal_id + " GROUP BY id_parcelas_rel";

        try{

            ResultSet result = db.select(sql);
            db.desconectar();

            if (result != null){
                while (result.next()){

                    ParcelasPostgresEntity parc_ent = new ParcelasPostgresEntity();
                    //Agrega los datos a la entidad

                    parc_ent.setIdgisparcela(result.getInt("id_parcelas_rel"));
                    parc_ent.setGeometria(result.getString("geometry"));
                    parc_ent.setLat(result.getDouble("lat"));
                    parc_ent.setLongi(result.getDouble("longi"));


                    lista_parc.add(parc_ent);
                    parc_ent = null;

                }


                //Procedo a llenar la lista original
                for (ParcelasPostgresEntity parce_ori : lista_original){

                    for (ParcelasPostgresEntity parce_aux : lista_parc){

                        if (parce_ori.getIdgisparcela() == parce_aux.getIdgisparcela()){

                            parce_ori.setGeometria(parce_aux.getGeometria());
                            parce_ori.setLat(parce_aux.getLat());
                            parce_ori.setLongi(parce_aux.getLongi());
                        }


                    }

                }

            }


        } catch (Exception e){
            this._mensaje = e.getMessage();
            this._status = true;
        }



        return lista_original;
    }

    public Integer getCantidadParcByIdRodal(DBPostgresConnection db, String rodal_id){

        Integer cantidad = 0;

        try{

            String sql = "SELECT COUNT(*) AS cantidad FROM gis WHERE rodales_idrodales = " + rodal_id;

            ResultSet result = db.select(sql);

            if (result != null){

                while (result.next()){

                    cantidad = result.getInt("cantidad");
                }

            }



        } catch (Exception e){
            this._status = false;
            this._mensaje = e.getMessage();
        }



        return cantidad;
    }

    public Integer getIdrodalpostgres() {
        return idrodalpostgres;
    }

    public void setIdrodalpostgres(Integer idrodalpostgres) {
        this.idrodalpostgres = idrodalpostgres;
    }

    public Integer getIdrodalsqlite() {
        return idrodalsqlite;
    }

    public void setIdrodalsqlite(Integer idrodalsqlite) {
        this.idrodalsqlite = idrodalsqlite;
    }

    public Integer getIdgisparcela() {
        return idgisparcela;
    }

    public void setIdgisparcela(Integer idgisparcela) {
        this.idgisparcela = idgisparcela;
    }

    public String getCod_sap() {
        return cod_sap;
    }

    public void setCod_sap(String cod_sap) {
        this.cod_sap = cod_sap;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getGeometria() {
        return geometria;
    }

    public void setGeometria(String geometria) {
        this.geometria = geometria;
    }

}
