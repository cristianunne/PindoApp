package forestal.app.pindo.pindo.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import forestal.app.pindo.pindo.R;
import forestal.app.pindo.pindo.config.ConexionPostgisConfig;
import forestal.app.pindo.pindo.config.ParcelasListByRodalFragment;
import forestal.app.pindo.pindo.config.sincronizedclass.SincronizarParcelasSQlite;
import postgres.conexion.DBPostgresConnection;
import utillities.entidades.ParcelasPostgresEntity;
import utillities.entidades.ParcelasRelevamientoSQLiteEntity;
import utillities.entidades.RodalesRelevamiento;
import utillities.utilidades.DialogPindo;

public class RodalesVerParcelasAdapter extends ArrayAdapter<RodalesRelevamiento> {

    private ArrayList<RodalesRelevamiento> lista;
    private Context context;
    private static View mview;
    private FragmentActivity activity;


    public RodalesVerParcelasAdapter(@NonNull Context context, ArrayList<RodalesRelevamiento> lista, FragmentActivity activity) {
       super(context, 0, lista);

       setContext(context);
       setLista(lista);
       this.activity = activity;

    }


    @SuppressWarnings("ConstantConditions")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final RodalesRelevamiento rodal_rel = getLista().get(position);
        final ViewGroup parent2 = parent;

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.rodales_ver_item_parcelas, null);

        //Obtengo los elementos y los seteo
        ViewHolder holder = new ViewHolder();


        try{
            holder.idrodal = (TextView)convertView.findViewById(R.id.textViewId_rver);
            holder.cod_sap = (TextView)convertView.findViewById(R.id.textViewCod_Sap_rver);
            holder.campo = (TextView)convertView.findViewById(R.id.textViewCampo_rver);
            holder.empresa = (TextView)convertView.findViewById(R.id.textViewEmpresa_rver);

            //Seteo los textos a los objetos graficos
            holder.idrodal.setText(String.format("%s", rodal_rel.getIdrodales()));
            holder.cod_sap.setText(rodal_rel.getCod_sap());
            holder.campo.setText(rodal_rel.getCampo());
            holder.empresa.setText(rodal_rel.getEmpresa());

            holder.ver_parcelas = (Button)convertView.findViewById(R.id.btn_ver_parcelas_rver);
            holder.ver_parcelas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //DEBO REALIZAR LA CONSULTA A LA TABLA EN SQLITE
                    ParcelasRelevamientoSQLiteEntity parc_rel_sqlite_entity = new ParcelasRelevamientoSQLiteEntity();
                    Integer tam = parc_rel_sqlite_entity.getCantidadByIdRodal(getContext(), rodal_rel.getIdrodales().toString());

                    //Evaluo qsi hay parcelas sincronizadas
                    if (tam > 0) {
                        //Cargar el ListView de las Parcelas
                        ParcelasListByRodalFragment parc_fr = new ParcelasListByRodalFragment();

                        Bundle arguments = new Bundle();
                        arguments.putString("idrodal", rodal_rel.getIdrodales().toString());
                        parc_fr.setArguments(arguments);

                        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();

                        ft.replace(R.id.fragment_contenedor, parc_fr);
                        ft.commit();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        View mview = LayoutInflater.from(getContext()).inflate(R.layout.dialog_result_error, parent2, false);

                        builder.setView(mview);
                        final AlertDialog dialog = builder.create();
                        //Seteo el texto de error
                        TextView txt_msj = (TextView)mview.findViewById(R.id.text_dialog_error);
                        txt_msj.setText("No se han sincronizado Parcelas para el Rodal seleccionado...!");
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

            holder.sincronizar = (Button)convertView.findViewById(R.id.btn_sincronizar_parcela);

            holder.sincronizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Aca debo controlar que la lista de parcelas no este vacia
                    //Voy a instanciar la clase Conexion PostgisConfig
                    ConexionPostgisConfig con_postgis = new ConexionPostgisConfig();

                    //Obtengo un mapa con las configuraciones
                    final Map<String, String> configu_ = con_postgis.getConfigPostis(getContext());

                    Integer tam = configu_.size();

                    if (tam > 0){

                        final ParcelasPostgresEntity parcPosEnt = new ParcelasPostgresEntity();
                        DBPostgresConnection dbcon = new DBPostgresConnection(configu_);
                        if (dbcon.is_status()){

                            Integer cantidad = parcPosEnt.getCantidadParcByIdRodal(dbcon, rodal_rel.getIdrodales().toString());

                            if (cantidad > 0){
                                //Para sincronizar controlo que no haya parcelas sincronizadas, y si hay hago una consulta si se desea eliminar las actuales

                                Integer cant_sinc = new ParcelasRelevamientoSQLiteEntity().getCantidadByIdRodal(getContext(), rodal_rel.getIdrodales().toString());

                                if (cant_sinc > 0){

                                    //Consulto se se realizara la sincronizacion o solo ver las parcelas ya sincronizadas

                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    View mview = LayoutInflater.from(getContext()).inflate(R.layout.dialog_result_acept_cancel, null, false);
                                    //View mview = getLayoutInflater().inflate(R.layout.dialog_result_error, null);
                                    builder.setView(mview);
                                    final AlertDialog dialog = builder.create();
                                    //Seteo el texto de error
                                    TextView txt_msj = (TextView)mview.findViewById(R.id.text_dr_title_ac_canc);
                                    txt_msj.setText("¿Desea Sincronizar las parcelas Nuevamente?");
                                    dialog.setCanceledOnTouchOutside(false);

                                    Button btn_aceptar_dialog = (Button)mview.findViewById(R.id.btn_aceptar_dialog_ac_can);
                                    btn_aceptar_dialog.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            sincronizedWithAlert(parent2, parcPosEnt, configu_, rodal_rel);
                                        }
                                    });

                                    Button btn_cancelar_dialog = (Button)mview.findViewById(R.id.btn_cancelar_dialog_ac_can);
                                    btn_cancelar_dialog.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            //Muestro las parcelas sincronizadas previamente sin volver a sincronizar
                                        }
                                    });
                                    dialog.show();


                                } else {
                                    View mview = LayoutInflater.from(getContext()).inflate(R.layout.dialog_progress, null);

                                    SincronizarParcelasSQlite sinc_parce_ent = new SincronizarParcelasSQlite(getContext(), mview);

                                    ArrayList<ParcelasPostgresEntity> parc_post = parcPosEnt.getListaDeParcelasPostgres(new DBPostgresConnection(configu_), rodal_rel.getIdrodales().toString());

                                    ArrayList<ParcelasPostgresEntity> parce_with_geom = parcPosEnt.getListaParcelasWithGeometry(parc_post, new DBPostgresConnection(configu_), rodal_rel.getIdrodales().toString());


                                    sinc_parce_ent.insertarParcelasToTablaRelevamiento(parce_with_geom);

                                }


                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                View mview = LayoutInflater.from(getContext()).inflate(R.layout.dialog_result_error, parent2, false);
                                //View mview = getLayoutInflater().inflate(R.layout.dialog_result_error, null);
                                builder.setView(mview);
                                final AlertDialog dialog = builder.create();
                                //Seteo el texto de error
                                TextView txt_msj = (TextView)mview.findViewById(R.id.text_dialog_error);
                                txt_msj.setText("No existen Parcelas para el Rodal seleccionado...!");
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

                            //Verifico que la consulta haya sido exitosa

                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            View mview = LayoutInflater.from(getContext()).inflate(R.layout.dialog_result_error, parent2, false);
                            //View mview = getLayoutInflater().inflate(R.layout.dialog_result_error, null);
                            builder.setView(mview);
                            final AlertDialog dialog = builder.create();
                            //Seteo el texto de error
                            TextView txt_msj = (TextView)mview.findViewById(R.id.text_dialog_error);
                            txt_msj.setText(dbcon.get_mensaje());
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

                        //Error al traer la configuracion
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        View mview = LayoutInflater.from(getContext()).inflate(R.layout.dialog_result_error, parent2, false);
                        //View mview = getLayoutInflater().inflate(R.layout.dialog_result_error, null);
                        builder.setView(mview);
                        final AlertDialog dialog = builder.create();
                        //Seteo el texto de error
                        TextView txt_msj = (TextView)mview.findViewById(R.id.text_dialog_error);
                        txt_msj.setText(con_postgis.get_mensaje());
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




            convertView.setTag(holder);

        } catch (NullPointerException e){
            e.printStackTrace();
        }

        return convertView;
    }

    /**
     * Esta clase se utiliza para renderizAR LOS ELEMENTOS
     */
    static class ViewHolder{

        TextView idrodal;
        TextView cod_sap;
        TextView campo;
        TextView empresa;
        Button ver_parcelas;
        Button sincronizar;
    }


    private void sincronizedWithAlert(ViewGroup parent, final ParcelasPostgresEntity parcPosEnt,  final Map<String, String> configu_, final RodalesRelevamiento rodal_rel){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View mview = LayoutInflater.from(getContext()).inflate(R.layout.dialog_result_acept_cancel, parent, false);
        //View mview = getLayoutInflater().inflate(R.layout.dialog_result_error, null);
        builder.setView(mview);
        final AlertDialog dialog = builder.create();
        //Seteo el texto de error
        TextView txt_msj = (TextView)mview.findViewById(R.id.text_dr_title_ac_canc);
        txt_msj.setText("Desea sobreescribir las parcelas ya sincronizadas?. Si presiona Cancelar solo se sincronizarán las parcelas no sincronizadas previamente!");
        dialog.setCanceledOnTouchOutside(false);


        final ParcelasRelevamientoSQLiteEntity parc_rel_sqlite = new ParcelasRelevamientoSQLiteEntity();

        Button btn_aceptar_dialog = (Button)mview.findViewById(R.id.btn_aceptar_dialog_ac_can);
        btn_aceptar_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                if (parc_rel_sqlite.deleteParcelasRelevamientoByRodal(rodal_rel.getIdrodales().toString(), getContext()) > -1){

                    View mview = LayoutInflater.from(getContext()).inflate(R.layout.dialog_progress, null);

                    SincronizarParcelasSQlite sinc_parce_ent = new SincronizarParcelasSQlite(getContext(), mview);

                    ArrayList<ParcelasPostgresEntity> parc_post = parcPosEnt.getListaDeParcelasPostgres(new DBPostgresConnection(configu_), rodal_rel.getIdrodales().toString());


                    //Ahora traigo la geometria
                    ArrayList<ParcelasPostgresEntity> parce_with_geom = parcPosEnt.getListaParcelasWithGeometry(parc_post, new DBPostgresConnection(configu_), rodal_rel.getIdrodales().toString());

                    //Antes de insertar elimino todas las parcelas by id rodal

                    sinc_parce_ent.insertarParcelasToTablaRelevamiento(parce_with_geom);


                } else {
                    //MUENTRO UN MENSAJE DE ERROR INDICANDO LO QUE SUCEDIO

                    DialogPindo dialog_error = new DialogPindo(getContext(), "Error al procesar las parcelas. Intente nuevamente.");
                    dialog_error.createDialogError();
                    dialog_error.showDialogError();

                }

            }
        });

        Button btn_cancelar_dialog = (Button)mview.findViewById(R.id.btn_cancelar_dialog_ac_can);
        btn_cancelar_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                //Realizo la eliminación de las parcelas by rodal

                View mview = LayoutInflater.from(getContext()).inflate(R.layout.dialog_progress, null);

                SincronizarParcelasSQlite sinc_parce_ent = new SincronizarParcelasSQlite(getContext(), mview);

                ArrayList<ParcelasPostgresEntity> parc_post = parcPosEnt.getListaDeParcelasPostgres(new DBPostgresConnection(configu_), rodal_rel.getIdrodales().toString());



                //Ahora traigo la geometria
                ArrayList<ParcelasPostgresEntity> parce_with_geom = parcPosEnt.getListaParcelasWithGeometry(parc_post, new DBPostgresConnection(configu_), rodal_rel.getIdrodales().toString());

                ParcelasRelevamientoSQLiteEntity parc_entidad = new ParcelasRelevamientoSQLiteEntity();

                //Traigo la lista actual de Parcelas
                ArrayList<ParcelasRelevamientoSQLiteEntity> parc_actual = parc_entidad.getAllParcelasRelevamientoByIdRodal(getContext(), rodal_rel.getIdrodales().toString());
                Integer cantidad = new ParcelasRelevamientoSQLiteEntity().getCantidadByIdRodal(getContext(), rodal_rel.getIdrodales().toString());

                Log.d("cantidad", cantidad.toString());

                if (cantidad > 0){
                    for (int i = 0; i < cantidad; i++){

                        ParcelasRelevamientoSQLiteEntity p = parc_actual.get(i);

                        for (int j = 0; j < parce_with_geom.size(); j++){

                            ParcelasPostgresEntity p2 = parce_with_geom.get(j);

                            if (p2.getIdgisparcela() == p.getIdparcela()){

                                parce_with_geom.remove(j);
                            }

                        }

                    }
                }



                sinc_parce_ent.insertarParcelasToTablaRelevamiento(parce_with_geom);


            }
        });

        dialog.show();

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

    public static View getMview() {
        return mview;
    }

    public static void setMview(View mview) {
        RodalesVerParcelasAdapter.mview = mview;
    }
}
