package forestal.app.pindo.pindo.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
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



import forestal.app.pindo.pindo.MapsActivity;

import forestal.app.pindo.pindo.R;

import utillities.entidades.RodalesRelevamiento;
import utillities.entidades.RodalesSincronizedEntity;



public class RodalesAdapter extends ArrayAdapter<RodalesSincronizedEntity> {

    private Context context;
    private ArrayList<RodalesSincronizedEntity> lista;
    private static View mview;
    private Activity activity;

    private FragmentManager fragmentManager;
    private FragmentTransaction ft;


    public RodalesAdapter(Context context, ArrayList<RodalesSincronizedEntity> lista, Activity activity) {
        super(context, 0, lista);
        this.context = context;
        this.lista = lista;
        this.activity = activity;

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {

        final RodalesSincronizedEntity rodal = this.lista.get(position);

        convertView = LayoutInflater.from(this.context).inflate(R.layout.item_lista, null);
        //convertView.setVisibility(View.GONE);

        final View layout = convertView;

        TextView textView = (TextView) convertView.findViewById(R.id.textViewId);
        textView.setText(String.valueOf(rodal.getIdrodales()));

        TextView textView2 = (TextView) convertView.findViewById(R.id.textViewCod_Sap);
        textView2.setText(String.valueOf(rodal.getCod_sap()));

        TextView textView3 = (TextView) convertView.findViewById(R.id.textViewCampo);
        textView3.setText(String.valueOf(rodal.getCampo()));

        TextView textView4 = (TextView) convertView.findViewById(R.id.textViewEmpresa);
        textView4.setText(String.valueOf(rodal.getEmpresa()));

        //Imprimo los nuevos datos
        //Log.d("id, fecha, especie", rodal.getIdrodales().toString() + ", " + rodal.getFecha_plantacion() + ", " + rodal.getEspecie());

        mview = LayoutInflater.from(this.context).inflate(R.layout.dialog_result_acept_cancel, parent, false);

        Button btn_eliminar = (Button)convertView.findViewById(R.id.btn_eliminar_rodal);
        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setView(mview);
                final AlertDialog dialog = builder.create();

                TextView output = (TextView) mview.findViewById(R.id.text_dr_title_ac_canc);
                String msj = "Desea Eliminar el Rodal N°: " + rodal.getIdrodales() + " ?";
                output.setText(msj);

                dialog.setCanceledOnTouchOutside(false);

                Button btn_aceptar_dialog = (Button)mview.findViewById(R.id.btn_aceptar_dialog_ac_can);
                btn_aceptar_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RodalesSincronizedEntity rodales = new RodalesSincronizedEntity();
                        int res = rodales.deleteRodalSincronized(rodal, layout.getContext());

                        if (res >= 0){
                                layout.setVisibility(View.GONE);
                                lista.remove(position);
                                notifyDataSetChanged();
                                //Refresca
                                parent.invalidate();
                                Toast.makeText(getContext(), "Rodal Eliminado", Toast.LENGTH_LONG).show();

                        }
                        dialog.dismiss();
                        ((ViewGroup)mview.getParent()).removeView(mview);

                    }
                });

                Button btn_cancelar_dialog = (Button)mview.findViewById(R.id.btn_cancelar_dialog_ac_can);
                btn_cancelar_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        dialog.dismiss();
                        ((ViewGroup)mview.getParent()).removeView(mview);

                    }
                });
                dialog.show();
            }
        });

        //Boton encargado de asignar un rodal a la tabla RODALES_SELECT
        Button btn_seleccionar = (Button)convertView.findViewById(R.id.btn_add_rodal);
        btn_seleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RodalesRelevamiento rodal_rel = new RodalesRelevamiento();
                //Cargo los elementos del rodal
                rodal_rel.setIdrodales(lista.get(position).getIdrodales());
                rodal_rel.setCod_sap(lista.get(position).getCod_sap());
                rodal_rel.setCampo(lista.get(position).getCampo());
                rodal_rel.setFinalizado(lista.get(position).getFinalizado().toString());
                rodal_rel.setEmpresa(lista.get(position).getEmpresa());
                rodal_rel.setUso(rodal.getUso());

                rodal_rel.setLatitud(lista.get(position).getLatitud());
                rodal_rel.setLongitud(lista.get(position).getLongitud());

                rodal_rel.setGeometry(rodal.getGeometry());
                rodal_rel.setLatitud(rodal.getLatitud());
                rodal_rel.setLongitud(rodal.getLongitud());

                rodal_rel.setFecha_plantacion(lista.get(position).getFecha_plantacion());
                rodal_rel.setFecha_inv(lista.get(position).getFecha_inv());
                rodal_rel.setEspecie(lista.get(position).getEspecie());

                //Inserto el Rodal
                Boolean resul = rodal_rel.insertRodalToRodalesRelevamiento(getContext(), rodal_rel);

                //Prueba de LOG


                if (resul){
                    Toast.makeText(getContext(), "Rodal Agregado Correctamente!", Toast.LENGTH_LONG).show();
                    layout.setVisibility(View.GONE);
                    lista.remove(position);
                    notifyDataSetChanged();
                    //Refresca
                    parent.invalidate();


                } else {
                    Toast.makeText(getContext(), "Rodal No Insertdado.. Verifique que no haya sido ya agregado", Toast.LENGTH_LONG).show();
                    //                    //Toast.makeText(getContext(), rodal_rel.get_mensaje(), Toast.LENGTH_LONG).show();

                }


            }
        });

        //Boton ver Mapa
        ImageButton btn_vermapa = (ImageButton)convertView.findViewById(R.id.btn_vermap_rodal);

        if (rodal.getLongitud() == 0 || rodal.getLongitud() == 0){
            btn_vermapa.setBackgroundColor(Color.TRANSPARENT);
            btn_vermapa.setEnabled(false);
        } else {

            btn_vermapa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    Intent intent = new Intent(activity, MapsActivity.class);
                    intent.putExtra("operacion", "rodal");
                    intent.putExtra("geometry", rodal.getGeometry());
                    intent.putExtra("lat", rodal.getLatitud().toString());
                    intent.putExtra("longi", rodal.getLongitud().toString());
                    activity.startActivity(intent);


                }
            });
        }




        return convertView;
    }



    public FragmentTransaction getFt() {
        return ft;
    }

    public void setFt(FragmentTransaction ft) {
        this.ft = ft;
    }
}
