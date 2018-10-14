package forestal.app.pindo.pindo.relevamiento.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.ActivityCompat;

import forestal.app.pindo.pindo.R;
import forestal.app.pindo.pindo.relevamiento.fragments.ParcelasRelevamientoFragment;
import forestal.app.pindo.pindo.relevamiento.location.GpsLocation;
import utillities.entidades.ParcelasRelevamientoSQLiteEntity;

public class DialogEditParcelaRelevamiento {

    public AlertDialog dialog;
    private View mview;
    private String text;
    private Context context;
    private FragmentActivity activity;


    private ParcelasRelevamientoSQLiteEntity parcela;

    public DialogEditParcelaRelevamiento(Context context, ParcelasRelevamientoSQLiteEntity parcela, FragmentActivity activity) {

        this.context = context;

        this.mview = LayoutInflater.from(context).inflate(R.layout.dialog_edit_parcela_relevamiento_2, null, false);

        setParcela(parcela);
        setActivity(activity);


    }


    public void showDialogEditParcelarelevamiento()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(mview);
        dialog = builder.create();

        dialog.setCanceledOnTouchOutside(false);


        try{
            final ViewHolder holder = new ViewHolder();


            holder.parcela = (EditText)getMview().findViewById(R.id.tv_edit_parc_parcela);
            holder.superficie = (EditText)getMview().findViewById(R.id.tv_edit_parc_superficie);
            holder.pendiente = (EditText)getMview().findViewById(R.id.tv_edit_parc_pendiente);
            holder.comentarios = (EditText)getMview().findViewById(R.id.tv_edit_parc_coment);
            holder.tipo = (EditText)getMview().findViewById(R.id.tv_edit_parc_tipo);
            holder.lat_point_rel = (EditText)getMview().findViewById(R.id.tv_edit_parc_lat_point_rel);
            holder.longi_point_rel = (EditText)getMview().findViewById(R.id.tv_edit_parc_longi_point_rel);


            holder.parcela.setText(String.format("%s", getParcela().getIdparcela().toString()));
            holder.superficie.setText(String.format("%s", getParcela().getSuperficie().toString()));
            holder.pendiente.setText(String.format("%s", getParcela().getPendiente().toString()));

            if(getParcela().getComentarios() == null){
                holder.comentarios.setText(String.format("%s", ""));
            } else if(getParcela().getComentarios().equals("null")){
                holder.comentarios.setText(String.format("%s", ""));
            } else {
                holder.comentarios.setText(String.format("%s", getParcela().getComentarios()));
            }
            holder.tipo.setText(String.format("%s", getParcela().getTipo()));

            holder.lat_point_rel.setText(String.format("%s", getParcela().getLat().toString()));
            holder.longi_point_rel.setText(String.format("%s", getParcela().getLongi().toString()));


            //Este boton edita
            holder.btn_aceptar = (Button)getMview().findViewById(R.id.btn_aceptar_edit_parc);

            holder.btn_aceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ParcelasRelevamientoSQLiteEntity parc_ent = new ParcelasRelevamientoSQLiteEntity();

                    String sup = holder.superficie.getText().toString();
                    String pend = holder.pendiente.getText().toString();
                    String comen = holder.comentarios.getText().toString();
                    String lat = holder.lat_point_rel.getText().toString();
                    String longi = holder.longi_point_rel.getText().toString();
                    String tipo = holder.tipo.getText().toString();

                    getParcela().setSuperficie(Double.valueOf(sup));
                    getParcela().setPendiente(Double.valueOf(pend));
                    getParcela().setComentarios(comen);

                    getParcela().setTipo(tipo);

                    getParcela().setLat(Double.valueOf(lat));
                    getParcela().setLongi(Double.valueOf(longi));


                    if (parc_ent.editarParcelaById(getContext(), getParcela())){
                        Toast.makeText(getContext(), "Parcela editada correctamente!", Toast.LENGTH_LONG).show();

                        dialog.dismiss();

                        //Boton de ver parcelas


                        //Controlar el el fragment la cantidad de parcelas relevamiento
                        ParcelasRelevamientoFragment fr_parcrel = new ParcelasRelevamientoFragment();

                        Bundle arguments = new Bundle();
                        arguments.putString("idrodal", getParcela().getIdrodales().toString());
                        fr_parcrel.setArguments(arguments);

                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);

                        ft.replace(R.id.fragment_contenedor, fr_parcrel);

                        ft.commit();

                    } else {
                        Toast.makeText(getContext(), "Error al editar la Parcela!", Toast.LENGTH_LONG).show();
                    }
                }
            });



            holder.btn_location = (ImageButton)getMview().findViewById(R.id.btn_location_parc);

            holder.btn_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    GpsLocation location = new GpsLocation(getActivity());

                    location.Location();
                    try{
                        String latitud = location.getLatitud().toString();
                        String longitud = location.getLongitud().toString();
                        holder.lat_point_rel.setText(latitud);
                        holder.longi_point_rel.setText(longitud);
                    } catch (NullPointerException e){

                    }



                }
            });


            holder.btn_cancelar = (Button)getMview().findViewById(R.id.btn_cancelar_edit_parc);
            holder.btn_cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });



        } catch (NullPointerException e){
            e.printStackTrace();
        }





        try {
            dialog.show();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }




    static class ViewHolder{

        EditText parcela;
        EditText superficie;
        EditText pendiente;
        EditText comentarios;
        EditText tipo;
        EditText lat_point_rel;
        EditText longi_point_rel;

        Button btn_aceptar;
        Button btn_cancelar;
        ImageButton btn_location;
    }



    public AlertDialog getDialog() {
        return dialog;
    }

    public void setDialog(AlertDialog dialog) {
        this.dialog = dialog;
    }

    public View getMview() {
        return mview;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ParcelasRelevamientoSQLiteEntity getParcela() {
        return parcela;
    }

    public void setParcela(ParcelasRelevamientoSQLiteEntity parcela) {
        this.parcela = parcela;
    }

    public FragmentActivity getActivity() {
        return activity;
    }

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }
}
