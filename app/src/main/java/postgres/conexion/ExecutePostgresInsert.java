package postgres.conexion;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ExecutePostgresInsert extends AsyncTask<String, Integer, Integer> {

    private Connection conn;
    private String query;
    private String msj_error;

    public ExecutePostgresInsert(Connection conn, String query) {
        this.conn = conn;
        this.query = query;
    }



    @Override
    protected Integer doInBackground(String... strings) {

        Integer cantidad = 0;

        try{
            if (this.conn != null){

                PreparedStatement st = this.conn.prepareStatement(this.query);
                cantidad = st.executeUpdate();
            }


        } catch (SQLException e){
            e.printStackTrace();
            setMsj_error(e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            setMsj_error(e.getMessage());
        }finally {
            try {
                if (this.conn != null){
                    this.conn.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
                setMsj_error(e.getMessage());
            }
        }



        return cantidad;
    }

    public String getMsj_error() {
        return msj_error;
    }

    public void setMsj_error(String msj_error) {
        this.msj_error = msj_error;
    }
}
