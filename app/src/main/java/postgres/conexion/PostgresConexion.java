package postgres.conexion;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import utillities.utilidades.Utilidades;



public class PostgresConexion {




    private Connection conn = null;

    public PostgresConexion() {
        super();
    }


    public Connection getConectionPostgres()
    {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Class.forName(Utilidades.CLASS_DB);

            conn = DriverManager.getConnection("jdbc:postgresql://" + Utilidades.HOST + ":" + Utilidades.PORT + "/" + Utilidades.DB, Utilidades.USER, Utilidades.PASS);

            return conn;

        } catch (ClassNotFoundException e) {
            Log.e("Error 2: ", e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            Log.e("Error 3: ", e.getMessage());
            e.printStackTrace();
        }


        return null;
    }


}
