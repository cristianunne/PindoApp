package forestal.app.pindo.pindo.relevamiento.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import forestal.app.pindo.pindo.R;
import forestal.app.pindo.pindo.relevamiento.fragments.ParcelasRelevamientoFragment;
import forestal.app.pindo.pindo.relevamiento.location.GpsLocation;
import utillities.entidades.ParcelasPostgresEntity;
import utillities.entidades.ParcelasRelevamientoSQLiteEntity;

public class DialogParcelaRelevamiento {

    public AlertDialog dialog;
    private View mview;
    private String text;
    private Context context;
    private String idrodal;
    private String cod_sap;
    private FragmentActivity activity;

    public DialogParcelaRelevamiento(Context context, String idrodal, String cod_sap, FragmentActivity activity) {

        this.context = context;

        this.mview = LayoutInflater.from(context).inflate(R.layout.dialog_parcela_relevamiento, null, false);

        setActivity(activity);
        setIdrodal(idrodal);
        setCod_sap(cod_sap);
    }


    public void showDialogParcelarelevamiento()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(mview);
        dialog = builder.create();

        dialog.setCanceledOnTouchOutside(false);


        try {

            final ViewHolder holder = new ViewHolder();



            holder.parcela = (EditText) getMview().findViewById(R.id.tv_addparc_parcela);
            holder.superficie = (EditText) getMview().findViewById(R.id.tv_addparc_superficie);
            holder.pendiente = (EditText) getMview().findViewById(R.id.tv_addrodal_pendiente);
            holder.comentarios = (EditText) getMview().findViewById(R.id.tv_addparc_coment);
            holder.lat_point_rel = (EditText) getMview().findViewById(R.id.tv_addparc_lat);
            holder.longi_point_rel = (EditText) getMview().findViewById(R.id.tv_addparc_long);
            holder.tv_edit_parc_tipo = (EditText) getMview().findViewById(R.id.tv_edit_parc_tipo);

            holder.btn_location = (ImageButton) getMview().findViewById(R.id.btn_location_parc);




            holder.btn_aceptar = (Button)getMview().findViewById(R.id.btn_addparc_acept);

            holder.btn_aceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(verifiedField(holder, holder.parcela.getText().toString(), holder.superficie.getText().toString(), holder.pendiente.getText().toString(),
                            holder.lat_point_rel.getText().toString(), holder.longi_point_rel.getText().toString(),  holder.tv_edit_parc_tipo.getText().toString())){

                        Integer id_rodal = Integer.valueOf(getIdrodal());
                        String cod_sap = getCod_sap();
                        Integer parcela = Integer.valueOf(holder.parcela.getText().toString());
                        Double sup = Double.valueOf(holder.superficie.getText().toString());
                        Double pend = Double.valueOf(holder.pendiente.getText().toString());
                        String comen = holder.comentarios.getText().toString();
                        String tipo = holder.tv_edit_parc_tipo.getText().toString();
                        Double lat = Double.valueOf(holder.lat_point_rel.getText().toString());
                        Double longi = Double.valueOf(holder.longi_point_rel.getText().toString());

                        //cREO LA ENTIDAD Y ALMACENO LOS DATOS
                        ParcelasRelevamientoSQLiteEntity parcela_ent = new ParcelasRelevamientoSQLiteEntity();

                        parcela_ent.setIdrodales(id_rodal);
                        parcela_ent.setCod_sap(cod_sap);
                        parcela_ent.setIdparcela(parcela);
                        parcela_ent.setSuperficie(sup);
                        parcela_ent.setPendiente(pend);
                        parcela_ent.setComentarios(comen);
                        parcela_ent.setTipo(tipo);
                        parcela_ent.setLat(lat);
                        parcela_ent.setLongi(longi);
                        parcela_ent.setReleva(1);

                        //setIdpOSTGRES QUEDA VACIO PORQUE NO HAY UNA SINCRONIZACION


                        ParcelasRelevamientoSQLiteEntity parcela_ent_proc = new ParcelasRelevamientoSQLiteEntity();

                        if (parcela_ent_proc.insertParcelasRelevamiento(getContext(), parcela_ent)){
                            Toast.makeText(getContext(), "Parcela agregada correctamente!", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            //Actualizo el fragment
                            //Controlar el el fragment la cantidad de parcelas relevamiento
                            ParcelasRelevamientoFragment fr_parcrel = new ParcelasRelevamientoFragment();

                            Bundle arguments = new Bundle();
                            arguments.putString("idrodal", getIdrodal());
                            arguments.putString("cod_sap", getCod_sap());
                            fr_parcrel.setArguments(arguments);

                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                            ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);

                            ft.replace(R.id.fragment_contenedor, fr_parcrel);

                            ft.commit();


                        } else{
                            Toast.makeText(getContext(), "Error al agregar la Parcela!", Toast.LENGTH_LONG).show();
                            dialog.dismiss();

                        }



                    }


                }
            });


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




            holder.btn_cancelar = (Button)getMview().findViewById(R.id.btn_cancelar_dialog_ac_can);
            holder.btn_cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }


    private Boolean verifiedField(ViewHolder holder, String parcela, String sup, String pend, String lat, String longi, String tipo){

        Boolean res = true;

        if (parcela.equals("")){
            holder.parcela.setBackgroundResource(R.drawable.bordes_campos_error);
            holder.parcela.setHint("Complete");
            res = false;
        }

        if (sup.equals("")){
            holder.superficie.setBackgroundResource(R.drawable.bordes_campos_error);
            holder.superficie.setHint("Complete");
            res = false;
        }

        if (pend.equals("")){
            holder.pendiente.setBackgroundResource(R.drawable.bordes_campos_error);
            holder.pendiente.setHint("Complete");
            res = false;
        }


        if (lat.equals("")){
            holder.lat_point_rel.setBackgroundResource(R.drawable.bordes_campos_error);
            holder.lat_point_rel.setHint("Complete");
            res = false;
        }

        if (longi.equals("")){
            holder.longi_point_rel.setBackgroundResource(R.drawable.bordes_campos_error);
            holder.longi_point_rel.setHint("Complete");
            res = false;
        }

        if (tipo.equals("")){
            holder.tv_edit_parc_tipo.setBackgroundResource(R.drawable.bordes_campos_error);
            holder.tv_edit_parc_tipo.setHint("Complete");
            res = false;
        }

        return res;
    }




    static class ViewHolder{
        EditText parcela;
        EditText superficie;
        EditText pendiente;
        EditText comentarios;
        EditText lat_point_rel;
        EditText longi_point_rel;
        EditText tv_edit_parc_tipo;

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


    public FragmentActivity getActivity() {
        return activity;
    }

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }

    public String getIdrodal() {
        return idrodal;
    }

    public void setIdrodal(String idrodal) {
        this.idrodal = idrodal;
    }

    public String getCod_sap() {
        return cod_sap;
    }

    public void setCod_sap(String cod_sap) {
        this.cod_sap = cod_sap;
    }
}
