package forestal.app.pindo.pindo.excelexport;

import android.content.Context;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;

import forestal.app.pindo.pindo.R;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import utillities.entidades.ArbolesRelevamientoEntity;
import utillities.entidades.ParcelasRelevamientoSQLiteEntity;
import utillities.entidades.RodalesRelevamiento;

public class ExcelExportProgress {

    private Context context;
    private View mview;
    private FragmentActivity activity;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    ProgressBar pgrBar;
    TextView output;


    public ExcelExportProgress(Context context, View mview, FragmentActivity activity) {
        this.context = context;
        this.mview = mview;
        setActivity(activity);
    }


    public void ExportToExcel(Integer cantidad, final ArrayList<RodalesRelevamiento> listaRodales, final File directorio, final String xlsFile,
                              final WorkbookSettings workbookSettings){

        final Integer max = cantidad;
        createDialogProgress(max);

        setTitle("Exportando a Excel.....");

        final Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {

                //TODO el codigo de exportacion

                if(listaRodales != null){

                    try{
                        File file = new File(directorio, xlsFile);

                        WritableWorkbook workbook;

                        workbook = Workbook.createWorkbook(file, workbookSettings);
                        WritableSheet sheet = workbook.createSheet("florexcel", 0);

                        //Agrego los titulos de las columnas
                        sheet.addCell(new Label(0, 0, "Rodal"));
                        sheet.addCell(new Label(1, 0, "Especie"));
                        sheet.addCell(new Label(2, 0, "Superficie"));
                        sheet.addCell(new Label(3, 0, "FechaPlant"));
                        sheet.addCell(new Label(4, 0, "FechaInv"));

                        //Espacio vacio

                        //Parcelas
                        sheet.addCell(new Label(6, 0, "Rodal"));
                        sheet.addCell(new Label(7, 0, "Parcela"));
                        sheet.addCell(new Label(8, 0, "Superficie"));
                        sheet.addCell(new Label(9, 0, "Pendiente"));

                        //Espacio vacio

                        //Arboles
                        sheet.addCell(new Label(11, 0, "Rodal"));
                        sheet.addCell(new Label(12, 0, "Parcela"));
                        sheet.addCell(new Label(13, 0, "Id Arbol"));
                        sheet.addCell(new Label(14, 0, "Arbol"));
                        sheet.addCell(new Label(15, 0, "DAP"));
                        sheet.addCell(new Label(16, 0, "Altura"));
                        sheet.addCell(new Label(17, 0, "altura_est"));
                        sheet.addCell(new Label(18, 0, "altura_poda"));
                        sheet.addCell(new Label(19, 0, "Calidad"));

                        int i = 1;
                        int j = 1;
                        int k = 1;

                        int publ = 1;
                        //Agrego los rodales
                        for (RodalesRelevamiento rodal : listaRodales){

                            boolean rodal_write = false;


                            if(rodal.getListaParcelas() != null){
                                //Recorro las parcelas
                                for(ParcelasRelevamientoSQLiteEntity parcela : rodal.getListaParcelas()){

                                    //Escribe la parcela solo si tiene arboles
                                    if(parcela.getListaArboles() != null){
                                        sheet.addCell(new Label(6, j, parcela.getCod_sap()));
                                        sheet.addCell(new Number(7, j, parcela.getId()));
                                        sheet.addCell(new Number(8, j, parcela.getSuperficie()));
                                        sheet.addCell(new Number(9, j, parcela.getPendiente()));

                                        j++;
                                    }
                                    int auto = 1;

                                    if(parcela.getListaArboles() != null){

                                        rodal_write = true;
                                        for(ArbolesRelevamientoEntity arbol : parcela.getListaArboles()){

                                            sheet.addCell(new Label(11, k, parcela.getCod_sap()));
                                            sheet.addCell(new Number(12, k, arbol.getIdparcela()));
                                            sheet.addCell(new Number(13, k, arbol.getId()));
                                            //Id autoincremental
                                            sheet.addCell(new Number(14, k, auto));
                                            sheet.addCell(new Number(15, k, arbol.getDap()));
                                            sheet.addCell(new Number(16, k, arbol.getAltura()));
                                            sheet.addCell(new Label(17, k, ""));
                                            sheet.addCell(new Number(18, k, arbol.getAlturapoda()));
                                            sheet.addCell(new Label(19, k, arbol.getCalidad()));

                                            k++;
                                            auto++;

                                            publishProgress(publ);
                                            publ++;

                                            try {
                                                Thread.sleep(500);

                                            } catch (InterruptedException e){
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                }

                            }

                            if(rodal_write){


                                sheet.addCell(new Label(0, i, rodal.getCod_sap()));
                                sheet.addCell(new Label(1, i, rodal.getEspecie()));
                                sheet.addCell(new Label(2, i, ""));
                                sheet.addCell(new DateTime(3, i, Date.valueOf(rodal.getFecha_plantacion())));
                                sheet.addCell(new Label(4, i, rodal.getFecha_inv()));
                                i++;

                            }
                        }

                        workbook.write();
                        workbook.close();

                        dialog.dismiss();


                    } catch (Exception e){
                        e.printStackTrace();
                        dialog.dismiss();

                        try{
                            Looper.prepare();

                            //pongo el cartelito de Dialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            View mview = getActivity().getLayoutInflater().inflate(R.layout.dialog_result_error, null);
                            builder.setView(mview);
                            final AlertDialog dialog = builder.create();
                            //Seteo el texto de error
                            TextView txt_msj = (TextView)mview.findViewById(R.id.text_dialog_error);
                            String msj = "Se ha producido un error. Reinicie la Aplicación.";
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

                            //Toast.makeText(context, "Se ha producido un error. Vuelva a intentarlo.", Toast.LENGTH_LONG).show();
                            Looper.loop();
                        } catch (Exception ex){
                            ex.printStackTrace();
                        }

                    }

                }

            }
        });

        hilo.start();

    }

    private void createDialogProgress(Integer max){

        builder = new AlertDialog.Builder(this.context);
        //View mview = this.inflater.inflate(R.layout.dialog_progress, null);
        builder.setView(this.mview);
        dialog = builder.create();
        pgrBar = (ProgressBar) this.mview.findViewById(R.id.progressBar);
        output = (TextView) this.mview.findViewById(R.id.output_txt);
        pgrBar.setMax(max);
        pgrBar.setProgress(0);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void publishProgress(Integer value){

        pgrBar.setProgress(value);
    }

    public void setTitle(String text){
        output.setText(text);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public View getMview() {
        return mview;
    }

    public void setMview(View mview) {
        this.mview = mview;
    }

    public AlertDialog.Builder getBuilder() {
        return builder;
    }

    public void setBuilder(AlertDialog.Builder builder) {
        this.builder = builder;
    }

    public AlertDialog getDialog() {
        return dialog;
    }

    public void setDialog(AlertDialog dialog) {
        this.dialog = dialog;
    }

    public ProgressBar getPgrBar() {
        return pgrBar;
    }

    public void setPgrBar(ProgressBar pgrBar) {
        this.pgrBar = pgrBar;
    }

    public TextView getOutput() {
        return output;
    }

    public void setOutput(TextView output) {
        this.output = output;
    }

    public FragmentActivity getActivity() {
        return activity;
    }

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }
}
