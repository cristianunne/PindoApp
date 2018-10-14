package forestal.app.pindo.pindo.uploadpostgres;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import forestal.app.pindo.pindo.R;
import forestal.app.pindo.pindo.config.ConexionPostgisConfig;
import forestal.app.pindo.pindo.uploadpostgres.postgres.ParcelasUploadPostgres;
import forestal.app.pindo.pindo.uploadpostgres.progress.upload.ArbolesUploadProgress;
import forestal.app.pindo.pindo.uploadpostgres.progress.upload.ParcelasUploadProgress;
import postgres.conexion.DBPostgresConnection;
import utillities.entidades.ArbolesRelevamientoEntity;
import utillities.entidades.ParcelasRelevamientoSQLiteEntity;
import utillities.entidades.RodalesRelevamiento;

public class RodalesUploadAdapter extends ArrayAdapter<RodalesRelevamiento> {


    private FragmentActivity activity;
    private ArrayList<RodalesRelevamiento> lista;
    private Context context;


    public RodalesUploadAdapter(@NonNull Context context, ArrayList<RodalesRelevamiento> lista, FragmentActivity activity) {
        super(context, 0, lista);

        setActivity(activity);
        setLista(lista);
        setContext(context);

    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final RodalesRelevamiento rodal = getLista().get(position);

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.rodal_upload_item, parent, false);

        //Creo el holer view

        ViewHolder holder = new ViewHolder();

        try{

            holder.idrodal = (TextView)convertView.findViewById(R.id.tv_rodal_rel_id);
            holder.tv_n_parc_total = (TextView)convertView.findViewById(R.id.tv_n_parc_total);
            holder.tv_n_parc_up = (TextView)convertView.findViewById(R.id.tv_n_parc_up);
            holder.tv_n_arboles = (TextView)convertView.findViewById(R.id.tv_n_arboles);
            holder.tv_n_arboles_up = (TextView)convertView.findViewById(R.id.tv_n_arboles_up);

            holder.btn_up_parc = (Button) convertView.findViewById(R.id.btn_up_parc);
            holder.btn_up_arboles = (Button) convertView.findViewById(R.id.btn_up_arboles);



            String id_rodal = getLista().get(position).getIdrodales().toString();
            holder.idrodal.setText(id_rodal);

            holder.tv_n_parc_total.setText(String.format("%s", rodal.getCantidad_parcelas().toString()));

            holder.tv_n_parc_up.setText(String.format("%s", rodal.getCantidad_parcelas_no_upload().toString()));

            holder.tv_n_arboles.setText(String.format("%s", rodal.getCantidad_arboles().toString()));

            holder.tv_n_arboles_up.setText(String.format("%s", rodal.getCantidad_arboles_no_upload().toString()));


            //Boton upload se activa solo si hay parcelas disponibles para subir

            if(rodal.getCantidad_parcelas_no_upload() > 0){
                //Boton subir parcelas
                holder.btn_up_parc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Voy a instanciar la clase Conexion PostgisConfig
                        ConexionPostgisConfig con_postgis = new ConexionPostgisConfig();

                        //Obtengo un mapa con las configuraciones
                        final Map<String, String> configu_ = con_postgis.getConfigPostis(getContext());

                        Integer tam = configu_.size();

                        if(tam > 0){

                            DBPostgresConnection dbcon = new DBPostgresConnection(configu_);

                            if(dbcon.is_status()){

                                //Traigo todas las parcelas by id rodal
                                ArrayList<ParcelasRelevamientoSQLiteEntity> parcelas =
                                        new ParcelasRelevamientoSQLiteEntity().getParcelasRelevamientoSelectNoUpdateByIdRodal(getContext(), rodal.getIdrodales().toString());

                                if(parcelas != null){
                                    //Usar el ID DE SQLITE porque el id de la parcela que agrego se puede repetir

                                    //Instancio la clase UploadProgress para procesar le paso la mview
                                    View Mview = getActivity().getLayoutInflater().inflate(R.layout.dialog_progress, null);
                                    ParcelasUploadProgress parcelasUploadProgress = new ParcelasUploadProgress(dbcon, getContext(), Mview, getActivity());

                                    //Ejecuta la subida con el Progress
                                    parcelasUploadProgress.uploadParcelaToPostgres(parcelas);

                                }

                            } else {
                                //Hay problemas de cONEXION CON POSTGRES
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                View mview = LayoutInflater.from(getContext()).inflate(R.layout.dialog_result_error, null);

                                builder.setView(mview);
                                final AlertDialog dialog = builder.create();
                                //Seteo el texto de error
                                TextView txt_msj = (TextView)mview.findViewById(R.id.text_dialog_error);
                                String msj = "No se pudo establecer la conexion con Postgres. Verifique los datos de conexión.";
                                txt_msj.setText(msj);
                                dialog.setCanceledOnTouchOutside(false);

                                Button btn_aceptar_dialog_error = (Button)mview.findViewById(R.id.btn_aceptar_dialog_error);
                                btn_aceptar_dialog_error.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                            }

                        } else {
                            //No se pudieron traer los datos de conexion desde SQLITE

                            //Hay problemas de cONEXION CON POSTGRES
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            View mview = LayoutInflater.from(getContext()).inflate(R.layout.dialog_result_error, null);

                            builder.setView(mview);
                            final AlertDialog dialog = builder.create();
                            //Seteo el texto de error
                            TextView txt_msj = (TextView)mview.findViewById(R.id.text_dialog_error);
                            String msj = "No se pudo recuperar los datos de conexión. Vuelva a intentarlo.";
                            txt_msj.setText(msj);
                            dialog.setCanceledOnTouchOutside(false);

                            Button btn_aceptar_dialog_error = (Button)mview.findViewById(R.id.btn_aceptar_dialog_error);
                            btn_aceptar_dialog_error.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }

                    }
                });

            } else {
                holder.btn_up_parc.setEnabled(false);
                holder.btn_up_parc.setBackgroundColor(Color.TRANSPARENT);

            }


            //Muestro el boton de subir arboles solo si hay disponibles
            if (rodal.getCantidad_arboles_no_upload() > 0){

                //Proceso el Boton de los Arboles
                holder.btn_up_arboles.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Voy a instanciar la clase Conexion PostgisConfig
                        ConexionPostgisConfig con_postgis = new ConexionPostgisConfig();
                        //Obtengo un mapa con las configuraciones
                        final Map<String, String> configu_ = con_postgis.getConfigPostis(getContext());
                        Integer tam = configu_.size();

                        if(tam > 0){

                            DBPostgresConnection dbcon = new DBPostgresConnection(configu_);

                            if(dbcon.is_status()){

                                //Traigo los arboles por id del rodal
                                ArrayList<ArbolesRelevamientoEntity> arboles = new ArbolesRelevamientoEntity().getListaArbolesByIdRodal(getContext(), rodal.getIdrodales());

                                if(arboles != null){

                                    //Instancio la clase UploadProgress para procesar le paso la mview
                                    View Mview = getActivity().getLayoutInflater().inflate(R.layout.dialog_progress, null);
                                    ArbolesUploadProgress arbolesUploadProgress = new ArbolesUploadProgress(getContext(), Mview, getActivity(), dbcon);


                                    //Traigo las parcelas

                                    //Traigo todas las parcelas by id rodal
                                    ArrayList<ParcelasRelevamientoSQLiteEntity> parcelas =
                                            new ParcelasRelevamientoSQLiteEntity().getParcelasRelevamientoSelectByIdRodal(getContext(), rodal.getIdrodales().toString());


                                    //Ejecuta la subida con el Progress
                                    arbolesUploadProgress.uploadArbolesToPostgres(arboles, parcelas);

                                }

                            } else {

                                //Hay problemas de cONEXION CON POSTGRES
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                View mview = LayoutInflater.from(getContext()).inflate(R.layout.dialog_result_error, null);

                                builder.setView(mview);
                                final AlertDialog dialog = builder.create();
                                //Seteo el texto de error
                                TextView txt_msj = (TextView)mview.findViewById(R.id.text_dialog_error);
                                String msj = "No se pudo establecer la conexion con Postgres. Verifique los datos de conexión.";
                                txt_msj.setText(msj);
                                dialog.setCanceledOnTouchOutside(false);

                                Button btn_aceptar_dialog_error = (Button)mview.findViewById(R.id.btn_aceptar_dialog_error);
                                btn_aceptar_dialog_error.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();

                            }

                        }

                    }
                });

            } else {

                holder.btn_up_arboles.setEnabled(false);
                holder.btn_up_arboles.setBackgroundColor(Color.TRANSPARENT);
            }

        } catch (NullPointerException e){
            e.printStackTrace();
        }


        return convertView;
    }


    static class ViewHolder{

        TextView idrodal;
        TextView tv_n_parc_total;
        TextView tv_n_parc_up;
        TextView tv_n_arboles;
        TextView tv_n_arboles_up;

        Button btn_up_parc;
        Button btn_up_arboles;
    }

    public FragmentActivity getActivity() {
        return activity;
    }

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }

    public ArrayList<RodalesRelevamiento> getLista() {
        return lista;
    }

    public void setLista(ArrayList<RodalesRelevamiento> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}
