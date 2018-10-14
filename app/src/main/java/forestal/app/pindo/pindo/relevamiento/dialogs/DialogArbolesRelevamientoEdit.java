package forestal.app.pindo.pindo.relevamiento.dialogs;

import android.content.Context;
import android.content.res.Resources;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import forestal.app.pindo.pindo.R;
import forestal.app.pindo.pindo.config.restricciones.RestriccionesEntity;
import forestal.app.pindo.pindo.config.restricciones.RestriccionesSQLite;
import forestal.app.pindo.pindo.relevamiento.ArbolesRelActivity;
import forestal.app.pindo.pindo.relevamiento.listenersEditText.TextChangedListener;
import utillities.entidades.ArbolesRelevamientoEntity;

public class DialogArbolesRelevamientoEdit {

    public AlertDialog dialog;
    private View mview;
    private String text;
    private Context context;
    private String idparcela;
    private String idrodal;
    private FragmentActivity activity;

    private boolean almacenar;
    private double promedioDap;

    RadioButton rb1, rb2;



    public DialogArbolesRelevamientoEdit(Context context, FragmentActivity activity) {

        this.context = context;

        this.mview = LayoutInflater.from(context).inflate(R.layout.dialog_arboles_relevamiento_edit, null, false);

        setActivity(activity);
        setIdparcela(idparcela);
        setIdrodal(idrodal);
        setAlmacenar(true);
    }

    public void showDialogArbolesrelevamiento(final ArbolesRelActivity arb_activity, final ArbolesRelevamientoEntity arbol) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(mview);
        dialog = builder.create();

        dialog.setCanceledOnTouchOutside(false);

        try{

            final ViewHolder holder = new ViewHolder();


            holder.marca = (EditText)getMview().findViewById(R.id.et_addarbol_marca);
            holder.dap = (EditText)getMview().findViewById(R.id.et_addarbol_dap);
            holder.altura = (EditText)getMview().findViewById(R.id.et_addarbol_altura);
            holder.altura_poda = (EditText)getMview().findViewById(R.id.et_addarbol_altura_poda);
            holder.dmsm = (EditText)getMview().findViewById(R.id.et_addarbol_dmsm);
            holder.lat = (EditText)getMview().findViewById(R.id.et_addarbol_lat);
            holder.longi = (EditText)getMview().findViewById(R.id.et_addarbol_long);
            holder.tv_error = (TextView) getMview().findViewById(R.id.tv_error);

            holder.sp_calidad = (Spinner) getMview().findViewById(R.id.et_addarbol_spinner_addarbol);
            //holder.sp_cosechado = (Spinner) getMview().findViewById(R.id.et_addarbol_spinner_cosechado);


            holder.marca.setText(arbol.getMarca());
            holder.dap.setText(String.format("%s", arbol.getDap().toString()));
            holder.altura.setText(String.format("%s", arbol.getAltura().toString()));
            holder.altura_poda.setText(String.format("%s", arbol.getAlturapoda().toString()));
            holder.dmsm.setText(String.format("%s", arbol.getDmsm().toString()));

            rb1 = (RadioButton) getMview().findViewById(R.id.radioButtonSi);
            rb2 = (RadioButton) getMview().findViewById(R.id.radioButtonNo);



            final ArrayAdapter<CharSequence> adapter_calidad = ArrayAdapter.createFromResource(getContext(), R.array.opciones, android.R.layout.simple_spinner_item);
            holder.sp_calidad.setAdapter(adapter_calidad);

            //Selecciono
            holder.sp_calidad.setSelection(adapter_calidad.getPosition(arbol.getCalidad()));


            if(arbol.getCosechado() != null){
                if(arbol.getCosechado().equals("SI")){
                    rb1.setChecked(true);
                    //Limpio todos los editext
                    holder.dap.setText("");
                    holder.dap.setEnabled(false);

                    holder.marca.setText("");
                    holder.marca.setEnabled(false);

                    holder.altura.setText("");
                    holder.altura.setEnabled(false);

                    holder.altura_poda.setText("");
                    holder.altura_poda.setEnabled(false);

                    holder.dmsm.setText("");
                    holder.dmsm.setEnabled(false);

                    holder.sp_calidad.setSelection(10);
                    holder.sp_calidad.setEnabled(false);



                } else if(arbol.getCosechado().equals("NO")) {
                    rb2.setChecked(true);
                } else {
                    //No hago nada
                }

            }


            rb1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.dap.setText("");
                    holder.dap.setEnabled(false);

                    holder.marca.setText("");
                    holder.marca.setEnabled(false);

                    holder.altura.setText("");
                    holder.altura.setEnabled(false);

                    holder.altura_poda.setText("");
                    holder.altura_poda.setEnabled(false);

                    holder.dmsm.setText("");
                    holder.dmsm.setEnabled(false);

                    holder.sp_calidad.setSelection(10);
                    holder.sp_calidad.setEnabled(false);

                }
            });

            rb2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    holder.marca.setText(arbol.getMarca());
                    holder.dap.setText(String.format("%s", arbol.getDap().toString()));
                    holder.altura.setText(String.format("%s", arbol.getAltura().toString()));
                    holder.altura_poda.setText(String.format("%s", arbol.getAlturapoda().toString()));
                    holder.dmsm.setText(String.format("%s", arbol.getDmsm().toString()));

                    holder.dap.setEnabled(true);
                    holder.marca.setEnabled(true);
                    holder.altura.setEnabled(true);
                    holder.altura_poda.setEnabled(true);
                    holder.dmsm.setEnabled(true);

                    //Selecciono
                    if(adapter_calidad.getPosition(arbol.getCalidad()) == 10 ){
                        holder.sp_calidad.setSelection(0);
                        holder.sp_calidad.setEnabled(true);
                    } else {
                        holder.sp_calidad.setSelection(adapter_calidad.getPosition(arbol.getCalidad()));
                        holder.sp_calidad.setEnabled(true);

                    }

                }
            });


            holder.lat.setText(String.format("%s", arbol.getLat().toString()));
            holder.longi.setText(String.format("%s", arbol.getLongi().toString()));


            //Boton aceptar
            holder.aceptar = (Button)getMview().findViewById(R.id.btn_addarbol_acept);
            holder.aceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Obtengo los datos

                    final ArbolesRelevamientoEntity arbol_ = new ArbolesRelevamientoEntity();
                    arbol_.setId(arbol.getId());
                    arbol_.setIdparcela(arbol.getIdparcela());
                    arbol_.setIdrodal(arbol.getIdrodal());

                    if(!holder.marca.getText().toString().isEmpty()){
                        arbol_.setMarca(holder.marca.getText().toString());
                    }

                    if (!holder.dap.getText().toString().isEmpty()){
                        arbol_.setDap(Double.valueOf(holder.dap.getText().toString()));
                    }

                    if (!holder.altura.getText().toString().isEmpty()){
                        arbol_.setAltura(Double.valueOf(holder.altura.getText().toString()));
                    }

                    if (!holder.altura_poda.getText().toString().isEmpty()){
                        arbol_.setAlturapoda(Double.valueOf(holder.altura_poda.getText().toString()));
                    }

                    if(!holder.dmsm.getText().toString().isEmpty()){
                        arbol_.setDmsm(Double.valueOf(holder.dmsm.getText().toString()));
                    }


                    arbol_.setCalidad(holder.sp_calidad.getSelectedItem().toString());
                    //arbol_.setCosechado(holder.sp_cosechado.getSelectedItem().toString());

                    //Recupero el button seleccionado

                    if(rb1.isChecked()){
                        arbol_.setCosechado("SI");
                    } else if(rb2.isChecked()){
                        arbol_.setCosechado("NO");
                    }



                    if(!holder.lat.getText().toString().isEmpty()){
                        arbol_.setLat(Double.valueOf(holder.lat.getText().toString()));
                    }

                    if(!holder.longi.getText().toString().isEmpty()){
                        arbol_.setLongi(Double.valueOf(holder.longi.getText().toString()));
                    }

                    final ArbolesRelevamientoEntity arbol_ant = new ArbolesRelevamientoEntity();

                    if(isAlmacenar()){

                        if(arbol_.getAltura() == 0 || arbol_.getAlturapoda() == 0){

                            AlertDialog.Builder builder_res = new AlertDialog.Builder(getContext());
                            View mview = LayoutInflater.from(getContext()).inflate(R.layout.dialog_result_acept_cancel, null, false);
                            //View mview = getLayoutInflater().inflate(R.layout.dialog_result_error, null);
                            builder_res.setView(mview);
                            final AlertDialog dialog_res = builder_res.create();
                            //Seteo el texto de error
                            TextView txt_msj = (TextView)mview.findViewById(R.id.text_dr_title_ac_canc);
                            txt_msj.setText("No se han especificado datos de Altura. ¿Desea proceder?");
                            dialog_res.setCanceledOnTouchOutside(false);

                            Button btn_aceptar_dialog_res = (Button)mview.findViewById(R.id.btn_aceptar_dialog_ac_can);
                            btn_aceptar_dialog_res.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (arbol_ant.editArbolByID(getContext(), arbol_)){

                                        arb_activity.cleanTable();
                                        arb_activity.showArboles();

                                        Toast.makeText(getContext(), "Arbol editado correctamente!", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getContext(), "Error al editar el Árbol", Toast.LENGTH_LONG).show();
                                    }

                                    dialog_res.dismiss();

                                }
                            });

                            Button btn_cancelar_dialog_res = (Button)mview.findViewById(R.id.btn_cancelar_dialog_ac_can);
                            btn_cancelar_dialog_res.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog_res.dismiss();

                                }
                            });
                            dialog_res.show();

                        } else {
                            if (arbol_ant.editArbolByID(getContext(), arbol_)){

                                arb_activity.cleanTable();
                                arb_activity.showArboles();

                                Toast.makeText(getContext(), "Arbol editado correctamente!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), "Error al editar el Árbol", Toast.LENGTH_LONG).show();
                            }
                        }

                    }

                    dialog.dismiss();

                }
            });


            holder.cancelar =  (Button)getMview().findViewById(R.id.btn_cancelar_dialog_ac_can);
            holder.cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            //Aca va la verificacion del DAP

            //Obtengo el valor de dap almacenado en SQLite
            RestriccionesSQLite restriccionesSQLite = new RestriccionesSQLite();

            //Value dap en %
            Integer value_dap = 0;

            RestriccionesEntity restriccionesEntity = restriccionesSQLite.getValueDap(getContext());

            if(restriccionesEntity != null){

                value_dap = restriccionesEntity.getDap();
            }


            final Integer finalValue_dap = value_dap;

            holder.dap.addTextChangedListener(new TextChangedListener<EditText>(holder.dap) {
                @Override
                public void onTextChanged(EditText target, Editable s) {

                    //Aca verifico que no sea mayor a mill

                    try{

                        if(finalValue_dap != 0){
                            Double number_ = Double.valueOf(target.getText().toString());

                            //DAP PROMEDIO ES EL 100%

                            double res_ = ((number_ * 100) / finalValue_dap) - 100;

                            Double res_final = Math.abs(res_);


                            if(res_final > finalValue_dap){
                                holder.dap.setBackgroundResource(R.drawable.bordes_campos_error);
                                holder.tv_error.setText(String.format("%s", "El valor es mayor al DAP permitido"));
                                setAlmacenar(false);


                            } else {
                                holder.dap.setBackgroundResource(R.drawable.borde_campos);
                                holder.tv_error.setText(String.format("%s", ""));
                                setAlmacenar(true);
                            }
                        }

                    } catch (NumberFormatException e){
                        //No realiza nada
                    }



                }
            });





            dialog.show();


        } catch (NullPointerException | Resources.NotFoundException e){
            e.printStackTrace();
        }


    }








    static class ViewHolder{

        EditText marca;
        EditText dap;
        EditText altura;
        EditText altura_poda;
        EditText dmsm;
        Spinner sp_calidad;
        TextView tv_error;

        EditText lat;
        EditText longi;

        Button aceptar;
        Button cancelar;

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

    public void setMview(View mview) {
        this.mview = mview;
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

    public String getIdparcela() {
        return idparcela;
    }

    public void setIdparcela(String idparcela) {
        this.idparcela = idparcela;
    }

    public String getIdrodal() {
        return idrodal;
    }

    public void setIdrodal(String idrodal) {
        this.idrodal = idrodal;
    }

    public FragmentActivity getActivity() {
        return activity;
    }

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }

    public boolean isAlmacenar() {
        return almacenar;
    }

    public void setAlmacenar(boolean almacenar) {
        this.almacenar = almacenar;
    }

    public double getPromedioDap() {
        return promedioDap;
    }

    public void setPromedioDap(double promedioDap) {
        this.promedioDap = promedioDap;
    }
}
