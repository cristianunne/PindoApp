package forestal.app.pindo.pindo.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import forestal.app.pindo.pindo.MapsActivity;
import forestal.app.pindo.pindo.R;
import utillities.entidades.ParcelasRelevamientoSQLiteEntity;
import utillities.entidades.RodalesSincronizedEntity;

public class ParcelasAdapter extends ArrayAdapter<ParcelasRelevamientoSQLiteEntity> {

    private Context context;
    private ArrayList<ParcelasRelevamientoSQLiteEntity> lista;
    private FragmentActivity activity;
    private View mview;


    public ParcelasAdapter(@NonNull Context context, ArrayList<ParcelasRelevamientoSQLiteEntity> lista, FragmentActivity activity) {
        super(context, 0, lista);

        setContext(context);
        setActivity(activity);
        setLista(lista);
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {

        final ParcelasRelevamientoSQLiteEntity parcela = this.getLista().get(position);


        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_lista_parcelas, parent, false);
        final View layout = convertView;
        ViewHolder holder = new ViewHolder();

        try{
            //Obtengo los elementos de la lista
            holder.idrodal = (TextView)convertView.findViewById(R.id.textViewId_parc);
            holder.cod_sap = (TextView)convertView.findViewById(R.id.textViewCod_Sap_parc);
            holder.parcela = (TextView)convertView.findViewById(R.id.textViewParcela_parc);
            holder.superficie = (TextView)convertView.findViewById(R.id.textViewSup_parc);
            holder.tipo = (TextView)convertView.findViewById(R.id.textViewtipo);
            holder.seleccionar = (Button)convertView.findViewById(R.id.btn_add_parc);
            holder.eliminar = (Button)convertView.findViewById(R.id.btn_eliminar_parc);
            holder.verMapa = (ImageButton)convertView.findViewById(R.id.btn_vermap_parc);



            //Seteo los valores de los UI
            holder.idrodal.setText(String.format("%s", parcela.getIdrodales().toString()));
            holder.cod_sap.setText(parcela.getCod_sap());
            holder.parcela.setText(String.format("%s",parcela.getIdparcela().toString()));
            holder.superficie.setText(String.format("%s", parcela.getSuperficie().toString()));
            holder.tipo.setText(String.format("%s", parcela.getTipo().toString()));


            //Controles de los Botones
            holder.seleccionar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Me setea el campo revela a 1
                    ParcelasRelevamientoSQLiteEntity select_parc = new ParcelasRelevamientoSQLiteEntity();
                    if (select_parc.setParcelaRelevamientoToTrue(getContext(), parcela.getId().toString())){
                        layout.setVisibility(View.GONE);
                        lista.remove(position);
                        notifyDataSetChanged();
                        //Refresca
                        parent.invalidate();
                        Toast.makeText(getContext(), "Parcela Seleccionada correctamente", Toast.LENGTH_LONG).show();
                    } else {
                        //Mostrar mensaje de error
                        Toast.makeText(getContext(), "Error al Seleccionar la Parcela", Toast.LENGTH_LONG).show();
                    }

                }
            });


            //Consulto que las coordenadas de los centroides sea diferentes de 0.0

            if(parcela.getLat() == 0 || parcela.getLongi() == 0){
                holder.verMapa.setBackgroundColor(Color.TRANSPARENT);
                holder.verMapa.setEnabled(false);

            } else{

                holder.verMapa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //aca le paso los parametros al mapa
                        RodalesSincronizedEntity rodal_ = new RodalesSincronizedEntity().getRodalById(getContext(), parcela.getIdrodales().toString());




                        Intent intent = new Intent(activity, MapsActivity.class);
                        intent.putExtra("operacion", "parcelas");
                        intent.putExtra("geometry", parcela.getGeometry());
                        intent.putExtra("lat", parcela.getLat().toString());
                        intent.putExtra("longi", parcela.getLongi().toString());

                        intent.putExtra("rodal", rodal_.getGeometry());
                        activity.startActivity(intent);

                    }
                });

            }

            //Boton eliminar
            holder.eliminar = (Button)convertView.findViewById(R.id.btn_eliminar_parc);



            holder.eliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                   setMview(LayoutInflater.from(getContext()).inflate(R.layout.dialog_result_acept_cancel, parent, false));
                    //Creo un mensaje de alerta para consultar si se desea borrar la parcela
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setView(getMview());
                    final AlertDialog dialog = builder.create();

                    TextView output = (TextView) getMview().findViewById(R.id.text_dr_title_ac_canc);
                    String msj = "Desea Eliminar la Parcela: " + parcela.getId().toString() + " ?";
                    output.setText(msj);

                    dialog.setCanceledOnTouchOutside(false);

                    Button btn_aceptar_dialog = (Button)getMview().findViewById(R.id.btn_aceptar_dialog_ac_can);
                    btn_aceptar_dialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ParcelasRelevamientoSQLiteEntity parc_rel = new ParcelasRelevamientoSQLiteEntity();

                            if (parc_rel.deleteParcelaById(getContext(), parcela.getId().toString())){

                                layout.setVisibility(View.GONE);
                                lista.remove(position);
                                notifyDataSetChanged();
                                //Refresca
                                parent.invalidate();
                                dialog.dismiss();
                                ((ViewGroup)getMview().getParent()).removeView(getMview());
                                Toast.makeText(getContext(), "Parcela Eliminada", Toast.LENGTH_LONG).show();

                            } else {
                                //Mostrar mensaje de error
                                dialog.dismiss();
                                ((ViewGroup)getMview().getParent()).removeView(getMview());
                                Toast.makeText(getContext(), "Error al eliminar la Parcela", Toast.LENGTH_LONG).show();
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



            convertView.setTag(holder);


        } catch (NullPointerException e){
            e.printStackTrace();
        }


        return convertView;
    }



    static class ViewHolder{

        TextView idrodal;
        TextView cod_sap;
        TextView parcela;
        TextView superficie;
        TextView tipo;
        Button seleccionar;
        Button eliminar;
        ImageButton verMapa;
    }



    @NonNull
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<ParcelasRelevamientoSQLiteEntity> getLista() {
        return lista;
    }

    public void setLista(ArrayList<ParcelasRelevamientoSQLiteEntity> lista) {
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
