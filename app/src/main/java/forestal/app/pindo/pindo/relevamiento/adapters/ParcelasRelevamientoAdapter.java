package forestal.app.pindo.pindo.relevamiento.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import forestal.app.pindo.pindo.MapsActivity;
import forestal.app.pindo.pindo.R;
import forestal.app.pindo.pindo.relevamiento.ArbolesRelActivity;
import forestal.app.pindo.pindo.relevamiento.dialogs.DialogEditParcelaRelevamiento;
import utillities.entidades.ArbolesRelevamientoEntity;
import utillities.entidades.ParcelasRelevamientoSQLiteEntity;
import utillities.entidades.RodalesSincronizedEntity;

public class ParcelasRelevamientoAdapter extends ArrayAdapter<ParcelasRelevamientoSQLiteEntity> {

    private ArrayList<ParcelasRelevamientoSQLiteEntity> lista;
    private Context context;
    private FragmentActivity activity;

    private final int REQUEST_CODE = 20;

    public ParcelasRelevamientoAdapter(@NonNull Context context, ArrayList<ParcelasRelevamientoSQLiteEntity> lista, FragmentActivity activity) {
        super(context, 0, lista);

        setActivity(activity);
        setContext(context);
        setLista(lista);
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ParcelasRelevamientoSQLiteEntity parc_rel = getLista().get(position);
        final ViewGroup parent2 = parent;

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.parcelas_relevamiento_item, parent, false);

        final View layout = convertView;

        HolderView holder = new HolderView();

        try{

            holder.idrodal = (TextView)convertView.findViewById(R.id.tv_parc_idrodal);
            holder.cod_sap = (TextView)convertView.findViewById(R.id.tv_parc_cod_sap);
            holder.parcela = (TextView)convertView.findViewById(R.id.tv_parc_parc);
            holder.superficie = (TextView)convertView.findViewById(R.id.tv_parc_sup);
            holder.pendiente = (TextView)convertView.findViewById(R.id.tv_parc_pendiente);
            holder.parcelasinc = (TextView)convertView.findViewById(R.id.tv_parc_parc_sinc);

            holder.cant_arboles = (TextView)convertView.findViewById(R.id.tv_parc_cant_arboles);

            holder.btn_rel_arboles = (Button) convertView.findViewById(R.id.btn_rel_arb);
            holder.btn_editar = (Button) convertView.findViewById(R.id.btn_edit_parc);
            holder.btn_eliminar = (Button) convertView.findViewById(R.id.btn_eliminar_parc);

            holder.btn_ver_mapa = (ImageButton) convertView.findViewById(R.id.btn_vermap_parc);



            //Cargo los elementos con las variabels correspondientes
            holder.idrodal.setText(String.format("%s", parc_rel.getIdrodales().toString()));
            holder.cod_sap.setText(String.format("%s", parc_rel.getTipo()));
            holder.parcelasinc.setText(String.format("%s", parc_rel.getIdpostgres().toString()));
            holder.parcela.setText(String.format("%s", parc_rel.getIdparcela().toString()));
            holder.superficie.setText(String.format("%s", parc_rel.getSuperficie().toString()));
            holder.pendiente.setText(String.format("%s", parc_rel.getPendiente().toString()));

            holder.cant_arboles.setText(String.format("%s", parc_rel.getCantidad_arboles().toString()));


            holder.btn_rel_arboles.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getActivity(), ArbolesRelActivity.class);


                    intent.putExtra("idparcela", parc_rel.getId());
                    intent.putExtra("idparcela2", parc_rel.getIdparcela());
                    intent.putExtra("idparcelapostgres", parc_rel.getIdpostgres());
                    intent.putExtra("idrodal", parc_rel.getIdrodales());

                    intent.putExtra("latitud", parc_rel.getLat());
                    intent.putExtra("longitud", parc_rel.getLongi());

                    getActivity().startActivity(intent);

                }
            });



            holder.btn_editar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DialogEditParcelaRelevamiento dlg_edit = new DialogEditParcelaRelevamiento(getContext(), parc_rel, getActivity());
                    dlg_edit.showDialogEditParcelarelevamiento();
                }
            });


            holder.btn_eliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    View mview = LayoutInflater.from(getContext()).inflate(R.layout.dialog_result_acept_cancel, null, false);
                    //View mview = getLayoutInflater().inflate(R.layout.dialog_result_error, null);
                    builder.setView(mview);
                    final AlertDialog dialog = builder.create();
                    //Seteo el texto de error
                    TextView txt_msj = (TextView)mview.findViewById(R.id.text_dr_title_ac_canc);
                    txt_msj.setText("¿Desea eliminar la Parcela?");
                    dialog.setCanceledOnTouchOutside(false);

                    Button btn_aceptar_dialog = (Button)mview.findViewById(R.id.btn_aceptar_dialog_ac_can);
                    btn_aceptar_dialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            //Primero elimino los arboles

                            if (new ArbolesRelevamientoEntity().deleteArbolesByIdParcela(getContext(), parc_rel.getId())){
                                //Este metodo solo cambia el estado de la parcela a 0
                                ParcelasRelevamientoSQLiteEntity parc_rel_ent = new ParcelasRelevamientoSQLiteEntity();
                                if (parc_rel_ent.changeParcelaToNoRelevamiento(getContext(), parc_rel.getId().toString())){


                                    layout.setVisibility(View.GONE);
                                    getLista().remove(position);
                                    notifyDataSetChanged();
                                    //Refresca
                                    parent2.invalidate();
                                    Toast.makeText(getContext(), "Parcela Eliminado", Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(getContext(), "Error al eliminar la Parcela. Intente nuevamente.", Toast.LENGTH_LONG).show();
                                }

                            }




                            dialog.dismiss();

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
                }
            });


            //Boton ver Mapa
            holder.btn_ver_mapa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    RodalesSincronizedEntity rodal = new RodalesSincronizedEntity().getRodalById(getContext(), parc_rel.getIdrodales().toString());

                    Intent intent = new Intent(activity, MapsActivity.class);
                    intent.putExtra("operacion", "parcelas");
                    intent.putExtra("geometry", parc_rel.getGeometry());
                    intent.putExtra("centroid_lat", parc_rel.getLat().toString());
                    intent.putExtra("centroid_longi", parc_rel.getLongi().toString());

                    try{
                        intent.putExtra("rodal", rodal.getGeometry());
                    } catch (NullPointerException e){
                        //No hacer nada

                        intent.putExtra("rodal","NO");
                    }


                    activity.startActivity(intent);

                }
            });






        }catch (NullPointerException e){
            e.printStackTrace();
        }




        return convertView;
    }



    static class HolderView{

        TextView idrodal;
        TextView cod_sap;
        TextView parcela;
        TextView parcelasinc;
        TextView superficie;
        TextView pendiente;
        TextView cant_arboles;

        Button btn_rel_arboles;
        Button btn_editar;
        Button btn_eliminar;

        ImageButton btn_ver_mapa;

    }




    public ArrayList<ParcelasRelevamientoSQLiteEntity> getLista() {
        return lista;
    }

    public void setLista(ArrayList<ParcelasRelevamientoSQLiteEntity> lista) {
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

    public FragmentActivity getActivity() {
        return activity;
    }

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }
}
