package postgres.conexion;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Map;

import utillities.utilidades.Utilidades;
import utillities.utilidades._Default;

public class DBPostgresConnection extends _Default implements Runnable{



    private Connection conn;
    private String url = "jdbc:postgresql://%s:%d/%s";
    private Map<String, String> config;

    private String msj_error;

    /*public DBPostgresConnection() {
        super();
        this.url = String.format(this.url, Utilidades.HOST, Utilidades.PORT, Utilidades.DB);

        //TODO abrir Conexion
        this.conectar();
        this.desconectar();

    }*/

    public DBPostgresConnection(Map<String, String> config){
        super();
        this.config = config;
        Integer port = Integer.parseInt(config.get(Utilidades.POSTGIS_CONFIG_PORT));

        this.url = String.format(this.url, config.get(Utilidades.POSTGIS_CONFIG_HOST), port, config.get(Utilidades.POSTGIS_CONFIG_DB));

        //TODO abrir Conexion
        this.conectar();
        //this.desconectar();

    }

    @Override
    public void run() {
        try{
            //Hago referencia a la Clase de Postgres que va a ser utilizado
            Class.forName(Utilidades.CLASS_DB);
            //Abro la conexion a la base de Datos

            //String rul_comp = this.url + "?user=" + this.config.get(Utilidades.POSTGIS_CONFIG_USER) + "&password="+this.config.get(Utilidades.POSTGIS_CONFIG_PASSWORD);
            //Log.d("url_comp", rul_comp);


            //setConn(DriverManager.getConnection(rul_comp));
            setConn(DriverManager.getConnection(this.url, this.config.get(Utilidades.POSTGIS_CONFIG_USER), this.config.get(Utilidades.POSTGIS_CONFIG_PASSWORD)));


        } catch (Exception e){
            this._mensaje = e.getMessage();
            this._status = false;
        }
    }


    private void conectar()
    {
        Thread thread = new Thread(this);
        thread.start();
        try {

            thread.join();

        } catch (Exception e){
            e.printStackTrace();
            this._mensaje = e.getMessage();
            this._status = false;
        }
    }

    public void desconectar()
    {
        if (getConn() != null){

            try{
                getConn().close();
            } catch (Exception e){

            } finally {
                setConn(null);
            }
        }
    }

    //TODO metodos de consulta

    public ResultSet select(String query){

        //Inicializo la conexion
        this.conectar();
        ResultSet resultSet = null;

        try{
            resultSet = new ExecuteRodales(getConn(), query).execute().get();

        } catch (Exception e){
            this._status = false;
            this._mensaje = e.getMessage();
        }
        this.desconectar();

        return resultSet;
    }

    /***
     * Inserta o Actualiza un campo en postgres
     * @param query
     * @return
     */
    public Integer executeUpdate(String query){

        //Inicializo la conexion
        this.conectar();
        Integer cantidad = 0;

        ExecutePostgresInsert executePostgresInsert = new ExecutePostgresInsert(getConn(), query);

        try{


            cantidad = executePostgresInsert.execute().get();
        } catch (Exception e){
            this._status = false;
            this._mensaje = e.getMessage();
        }
        this.desconectar();

        this._mensaje = executePostgresInsert.getMsj_error();

        return cantidad;
    }




    public Connection getConn() {
        return this.conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public String getMsj_error() {
        return msj_error;
    }

    public void setMsj_error(String msj_error) {
        this.msj_error = msj_error;
    }
}
