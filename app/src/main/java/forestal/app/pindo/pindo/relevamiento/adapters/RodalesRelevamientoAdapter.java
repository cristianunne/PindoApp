package forestal.app.pindo.pindo.relevamiento.adapters;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import forestal.app.pindo.pindo.R;
import forestal.app.pindo.pindo.adapters.ParcelasAdapter;
import forestal.app.pindo.pindo.relevamiento.fragments.ParcelasRelevamientoFragment;
import utillities.entidades.ArbolesRelevamientoEntity;
import utillities.entidades.ParcelasRelevamientoSQLiteEntity;
import utillities.entidades.RodalesRelevamiento;

public class RodalesRelevamientoAdapter extends ArrayAdapter<RodalesRelevamiento> {

    private Context context;
    private ArrayList<RodalesRelevamiento> lista;
    private FragmentActivity activity;
    private View mview;

    public RodalesRelevamientoAdapter(@NonNull Context context, ArrayList<RodalesRelevamiento> lista, FragmentActivity activity) {
        super(context, 0, lista);

        setContext(context);
        setLista(lista);
        setActivity(activity);
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final RodalesRelevamiento rodal = getLista().get(position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.rodal_relevamiento_item, parent, false);
        final View layout = convertView;
        HolderView holder = new HolderView();

        final ViewGroup parent_2 = parent;

        //Muestro el Id y las fecha


        try{

            holder.idrodal = (TextView)convertView.findViewById(R.id.tv_rodal_rel_id);
            holder.cod_sap = (TextView)convertView.findViewById(R.id.tv_rodal_rel_cod_sap);
            holder.campo = (TextView)convertView.findViewById(R.id.tv_rodal_rel_campo);
            holder.empresa = (TextView)convertView.findViewById(R.id.tv_rodal_rel_empresa);
            holder.cant_parcelas = (TextView)convertView.findViewById(R.id.tv_rodal_rel_cant_parc);

            holder.ver_parcelas = (Button) convertView.findViewById(R.id.btn_rodal_rel_verparcelas);
            holder.eliminar = (Button) convertView.findViewById(R.id.btn_rodal_rel_eliminar);
            holder.ver_mapa = (ImageButton) convertView.findViewById(R.id.btn_rodal_rel_vermap);

            holder.idrodal.setText(String.format("%s", rodal.getIdrodales().toString()));
            holder.cod_sap.setText(rodal.getCod_sap());
            holder.campo.setText(rodal.getCampo());
            holder.empresa.setText(rodal.getEmpresa());


            holder.cant_parcelas.setText(rodal.getCantidad_parcelas().toString());



            holder.eliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Mostrar el mensaje de advertencia

                    setMview(LayoutInflater.from(getContext()).inflate(R.layout.dialog_result_acept_cancel, parent_2, false));
                    //Creo un mensaje de alerta para consultar si se desea borrar la parcela
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setView(getMview());
                    final AlertDialog dialog = builder.create();

                    TextView output = (TextView) getMview().findViewById(R.id.text_dr_title_ac_canc);
                    String msj = "Desea Eliminar el Rodal: " + rodal.getIdrodales() + " ?";
                    output.setText(msj);

                    dialog.setCanceledOnTouchOutside(false);

                    Button btn_aceptar_dialog = (Button)getMview().findViewById(R.id.btn_aceptar_dialog_ac_can);
                    btn_aceptar_dialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //Elimino los arboles y las parcelas

                            if(new ArbolesRelevamientoEntity().deleteArbolByIdRodal(getContext(), rodal.getIdrodales())){
                                //Elimino las parcelas

                                if(new ParcelasRelevamientoSQLiteEntity().deleteParcelasRelevamientoByRodal(rodal.getIdrodales().toString(), getContext()) > -1){

                                    RodalesRelevamiento rodal_rel = new RodalesRelevamiento();

                                    if (rodal_rel.deleteRodalSelect(getContext(), rodal.getId().toString())){
                                        layout.setVisibility(View.GONE);
                                        lista.remove(position);
                                        notifyDataSetChanged();
                                        //Refresca
                                        parent_2.invalidate();
                                        dialog.dismiss();
                                        ((ViewGroup)getMview().getParent()).removeView(getMview());
                                        Toast.makeText(getContext(), "Rodal Eliminado", Toast.LENGTH_LONG).show();


                                    } else {
                                        dialog.dismiss();
                                        ((ViewGroup)getMview().getParent()).removeView(getMview());
                                        Toast.makeText(getContext(), "Error al eliminar el Rodal", Toast.LENGTH_LONG).show();
                                    }

                                } else {
                                    dialog.dismiss();
                                    ((ViewGroup)getMview().getParent()).removeView(getMview());
                                    Toast.makeText(getContext(), "Error al eliminar el Rodal", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                dialog.dismiss();
                                ((ViewGroup)getMview().getParent()).removeView(getMview());
                                Toast.makeText(getContext(), "Error al eliminar el Rodal", Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                    Button btn_cancelar_dialog = (Button)getMview().findViewById(R.id.btn_cancelar_dialog_ac_can);
                    btn_cancelar_dialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            dialog.dismiss();
                            ((ViewGroup)getMview().getParent()).removeView(getMview());


                        }
                    });
                    dialog.show();
                }
            });

            //El boton ver mapa solo se activa si hay coordenadas

            if (rodal.getLongitud() != null && rodal.getLatitud() != null){

                if (rodal.getLatitud() == 0 || rodal.getLongitud() == 0 ){

                    holder.ver_mapa.setEnabled(false);
                    holder.ver_mapa.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    //Agrego el evento al boton de ver mapa


                }
            } else {
                holder.ver_mapa.setEnabled(false);
                holder.ver_mapa.setBackgroundColor(Color.TRANSPARENT);
            }



            holder.ver_parcelas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Boton de ver parcelas
                    //Consulto si hay parcelas para relevar
                    ParcelasRelevamientoSQLiteEntity parcelas = new ParcelasRelevamientoSQLiteEntity();

                    Integer cantidad = parcelas.getCantidadParcelasSelectByIdRodal(getContext(), rodal.getIdrodales().toString());

                   // Log.d("cantidad", cantidad.toString());

                    //Controlar el el fragment la cantidad de parcelas relevamiento
                    ParcelasRelevamientoFragment fr_parcrel = new ParcelasRelevamientoFragment();

                    Bundle arguments = new Bundle();
                    arguments.putString("idrodal", rodal.getIdrodales().toString());
                    arguments.putString("cod_sap", rodal.getCod_sap());
                    fr_parcrel.setArguments(arguments);

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);

                    ft.replace(R.id.fragment_contenedor, fr_parcrel);

                    ft.commit();

                }
            });


        } catch (NullPointerException e){
            e.printStackTrace();
        }

        return convertView;
    }


    static class HolderView{

        TextView idrodal;
        TextView cod_sap;
        TextView campo;
        TextView empresa;
        TextView cant_parcelas;
        Button ver_parcelas;
        Button eliminar;
        ImageButton ver_mapa;

    }

    @NonNull
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<RodalesRelevamiento> getLista() {
        return lista;
    }

    public void setLista(ArrayList<RodalesRelevamiento> lista) {
        this.lista = lista;
    }

    public FragmentActivity getActivity() {
        return activity;
    }

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }

    public View getMview() {
        return mview;
    }

    public void setMview(View mview) {
        this.mview = mview;
    }
}
