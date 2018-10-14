package forestal.app.pindo.pindo.relevamiento.dialogs;

import android.content.Context;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.format.Time;
import android.util.Log;
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

public class DialogArbolesRelevamientoAdd {

    public AlertDialog dialog;
    private View mview;
    private String text;
    private Context context;
    private String idparcela;
    private String idrodal;
    private FragmentActivity activity;
    private Location location;
    ArbolesRelActivity arb_activity;

    private boolean almacenar;

    private double promedioDap;

    RadioButton rb1, rb2;

    private Boolean res_;


    public DialogArbolesRelevamientoAdd(Context context, String idparcela, String idrodal, Location location, FragmentActivity activity) {

        this.context = context;

        this.mview = LayoutInflater.from(context).inflate(R.layout.dialog_arboles_relevamiento_add, null, false);

        setActivity(activity);
        setIdparcela(idparcela);
        setIdrodal(idrodal);
        setLocation(location);
        setAlmacenar(true);


    }




    public void showDialogParcelarelevamiento(ArbolesRelActivity arb_activity) {

        setArb_activity(arb_activity);

        setRes_(false);
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

            rb1 = (RadioButton) getMview().findViewById(R.id.radioButtonSi);
            rb2 = (RadioButton) getMview().findViewById(R.id.radioButtonNo);
            holder.sp_calidad = (Spinner) getMview().findViewById(R.id.et_addarbol_spinner_addarbol);

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

           // holder.sp_cosechado = (Spinner) getMview().findViewById(R.id.et_addarbol_spinner_cosechado);

            ArrayAdapter<CharSequence> adapter_calidad = ArrayAdapter.createFromResource(getContext(), R.array.opciones, android.R.layout.simple_spinner_item);
            holder.sp_calidad.setAdapter(adapter_calidad);

            //ArrayAdapter<CharSequence> adapter_cosechado = ArrayAdapter.createFromResource(getContext(), R.array.boleano, android.R.layout.simple_spinner_item);
            //holder.sp_cosechado.setAdapter(adapter_cosechado);

            Double lat = getLocation().getLatitude();
            Double longi = getLocation().getLongitude();

            holder.lat.setText(lat.toString());
            holder.longi.setText(longi.toString());


            //Boton aceptar
            holder.aceptar = (Button)getMview().findViewById(R.id.btn_addarbol_acept);
            holder.aceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Obtengo los datos

                    final ArbolesRelevamientoEntity arbol = new ArbolesRelevamientoEntity();

                    arbol.setIdparcela(Integer.valueOf(getIdparcela()));
                    arbol.setIdrodal(Integer.valueOf(getIdrodal()));

                    if(!holder.marca.getText().toString().isEmpty()){
                        arbol.setMarca(holder.marca.getText().toString());
                    }

                    if (!holder.dap.getText().toString().isEmpty()){
                        arbol.setDap(Double.valueOf(holder.dap.getText().toString()));
                    }

                    if (!holder.altura.getText().toString().isEmpty()){
                        arbol.setAltura(Double.valueOf(holder.altura.getText().toString()));
                    }

                    if (!holder.altura_poda.getText().toString().isEmpty()){
                        arbol.setAlturapoda(Double.valueOf(holder.altura_poda.getText().toString()));
                    }

                    if(!holder.dmsm.getText().toString().isEmpty()){
                        arbol.setDmsm(Double.valueOf(holder.dmsm.getText().toString()));
                    }


                    arbol.setCalidad(holder.sp_calidad.getSelectedItem().toString());
                    //arbol.setCosechado(holder.sp_cosechado.getSelectedItem().toString());

                    //Recupero el button seleccionado

                    if(rb1.isChecked()){
                        arbol.setCosechado("SI");
                    } else if(rb2.isChecked()){
                        arbol.setCosechado("NO");
                    }


                    if(!holder.lat.getText().toString().isEmpty()){
                        arbol.setLat(Double.valueOf(holder.lat.getText().toString()));
                    }

                    if(!holder.longi.getText().toString().isEmpty()){
                        arbol.setLongi(Double.valueOf(holder.longi.getText().toString()));
                    }

                    //Agrego la fecha del dispositivo como fecha de captura
                    Time time = new Time(Time.getCurrentTimezone());
                    time.setToNow();
                    Integer dia = time.monthDay;
                    Integer mes = time.month + 1;
                    Integer ano = time.year;

                    String fecha = ano + "-" + mes + "-" + dia;

                    arbol.setFecha_rel(fecha);

                    final ArbolesRelevamientoEntity arbol_ant = new ArbolesRelevamientoEntity();

                    if(isAlmacenar()){

                        if (arbol_ant.insertArbolRelevamiento(getContext(), arbol)){

                            getArb_activity().cleanTable();
                            getArb_activity().showArboles();
                            getArb_activity().drawArbolesToMap();

                            Toast.makeText(getContext(), "Arbol agregado correctamente!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Error al agregar el Árbol", Toast.LENGTH_LONG).show();
                        }


                        dialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Verifique los datos ingresados", Toast.LENGTH_LONG).show();
                    }


                }
            });


            holder.cancelar =  (Button)getMview().findViewById(R.id.btn_cancelar_dialog_ac_can);
            holder.cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


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


        } catch (NullPointerException e){
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
        EditText lat;
        EditText longi;
        TextView tv_error;

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

    public String getIdparcela() {
        return idparcela;
    }

    public void setIdparcela(String idparcela) {
        this.idparcela = idparcela;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Boolean getRes_() {
        return res_;
    }

    public void setRes_(Boolean res_) {
        this.res_ = res_;
    }

    public ArbolesRelActivity getArb_activity() {
        return arb_activity;
    }

    public void setArb_activity(ArbolesRelActivity arb_activity) {
        this.arb_activity = arb_activity;
    }

    public double getPromedioDap() {
        return promedioDap;
    }

    public void setPromedioDap(double promedioDap) {
        this.promedioDap = promedioDap;
    }

    public boolean isAlmacenar() {
        return almacenar;
    }

    public void setAlmacenar(boolean almacenar) {
        this.almacenar = almacenar;
    }
}
