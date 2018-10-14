package forestal.app.pindo.pindo.excelexport;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import forestal.app.pindo.pindo.Fragment;
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

public class ExcelExport {

    private WorkbookSettings workbookSettings;
    private WritableWorkbook workbook;
    private Context context;
    private FragmentActivity activity;

    public static final String TAG = "logcat";


    public ExcelExport(Context context, FragmentActivity activity) {

        this.activity = activity;
        setContext(context);
        setWorkbookSettings(new WorkbookSettings());

        getWorkbookSettings().setLocale(new Locale("en", "EN"));

        final int REQUEST_EXTERNAL_STORAGE = 1;

        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);

    }


    public void createExcelExport(){


        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(getContext(), "No hay permiso", Toast.LENGTH_LONG).show();

        } else {

            if(isExternalStorageWritable()){

                Time time = new Time(Time.getCurrentTimezone());
                time.setToNow();
                Integer dia = time.monthDay;
                Integer mes = (time.month + 1);
                Integer ano = time.year;

                Integer hora = time.hour;
                Integer min = time.minute;
                Integer second = time.second;

                String xlsFile = "floxexcel_" + ano + "_" + mes + "_" + dia + "_" + hora + "_" + min + "_" + second + ".xls";

                String nombreDirectorio = "pindoexport";
                File directorio = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), nombreDirectorio);

                //Muestro un mensaje en el logcat si no se creo la carpeta por algun motivo
                if (!directorio.exists()){

                    //Si no existe el directorio me lo crea
                    if (!directorio.mkdirs()) {
                        Log.e(TAG, "Error: No se creo el directorio público porque ya existe");
                    }

                }
                //Si efectivamente lo creo y existe cargo el excel
                if (directorio.exists()){

                    //writeExcel(directorio, xlsFile);
                    if (writeExcel(directorio, xlsFile)){
                        Toast.makeText(getContext(), "Proceso Exitoso", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Error. Intente Nuevamente", Toast.LENGTH_LONG).show();
                    }

                }


            }else{

                Toast.makeText(getContext(), "El archivo se almacenara en Memoria Interna", Toast.LENGTH_LONG).show();

            }
        }

    }

    public void createExcelExport(View view){


        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(getContext(), "No hay permiso", Toast.LENGTH_LONG).show();

        } else {


            if(isExternalStorageWritable()){

                Time time = new Time(Time.getCurrentTimezone());
                time.setToNow();
                Integer dia = time.monthDay;
                Integer mes = time.month + 1;
                Integer ano = time.year;

                Integer hora = time.hour;
                Integer min = time.minute;
                Integer second = time.second;

                String xlsFile = "floxexcel_" + ano + "_" + mes + "_" + dia + "_" + hora + "_" + min + "_" + second + ".xls";

                String nombreDirectorio = "pindoexport";
                File directorio = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), nombreDirectorio);

                //Muestro un mensaje en el logcat si no se creo la carpeta por algun motivo
                if (!directorio.exists()){

                    //Si no existe el directorio me lo crea
                    if (!directorio.mkdirs()) {
                        Log.e(TAG, "Error: No se creo el directorio público porque ya existe");
                    }

                }
                //Si efectivamente lo creo y existe cargo el excel
                if (directorio.exists()){

                    //writeExcel(directorio, xlsFile);
                    writeExcelWithProgress(directorio, xlsFile, view);

                }


            }else{

                Toast.makeText(getContext(), "El archivo se almacenara en Memoria Interna", Toast.LENGTH_LONG).show();

            }
        }

    }


    public void writeExcelWithProgress(File directorio, String xlsFile, View view){


        Integer cant_arboles = new ArbolesRelevamientoEntity().getCantidadDeArboles(getContext());
        ArrayList<RodalesRelevamiento> listaRodales = getListaRodales();

        if(listaRodales != null){

            ExcelExportProgress excelExportProgress = new ExcelExportProgress(getContext(), view, getActivity());
            excelExportProgress.ExportToExcel(cant_arboles, listaRodales, directorio, xlsFile, getWorkbookSettings());
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            View mview = getActivity().getLayoutInflater().inflate(R.layout.dialog_result_error, null);
            builder.setView(mview);
            final AlertDialog dialog = builder.create();
            //Seteo el texto de error
            TextView txt_msj = (TextView)mview.findViewById(R.id.text_dialog_error);
            String msj = "No hay datos disponibles a exportar.";
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
        }






    }

    public Boolean writeExcel(File directorio, String xlsFile){


        ArrayList<RodalesRelevamiento> listaRodales = getListaRodales();

        if(listaRodales != null){

            try{

                File file = new File(directorio, xlsFile);

                WritableWorkbook workbook;

                workbook = Workbook.createWorkbook(file, getWorkbookSettings());
                WritableSheet sheet = workbook.createSheet("Rodales", 0);

                //Agrego los titulos de las columnas
                sheet.addCell(new Label(0, 0, "Rodal"));
                sheet.addCell(new Label(1, 0, "Cod SAP"));
                sheet.addCell(new Label(2, 0, "Campo"));
                sheet.addCell(new Label(3, 0, "Uso"));
                sheet.addCell(new Label(4, 0, "Finalizado"));
                sheet.addCell(new Label(5, 0, "Empresa"));
                sheet.addCell(new Label(6, 0, "Especie"));
                sheet.addCell(new Label(7, 0, "FechaPlant"));
                sheet.addCell(new Label(8, 0, "FechaInv"));


                int i = 1;
                for (RodalesRelevamiento rodal : listaRodales){

                    sheet.addCell(new Number(0, i, rodal.getId()));
                    sheet.addCell(new Label(1, i, rodal.getCod_sap()));
                    sheet.addCell(new Label(2, i, rodal.getCampo()));
                    sheet.addCell(new Label(3, i, rodal.getUso()));
                    sheet.addCell(new Label(4, i, rodal.getFinalizado()));
                    sheet.addCell(new Label(5, i, rodal.getEmpresa()));
                    sheet.addCell(new Label(6, i, rodal.getEspecie()));
                    sheet.addCell(new DateTime(7, i, Date.valueOf(rodal.getFecha_plantacion())));
                    sheet.addCell(new Label(8, i, rodal.getFecha_inv()));
                    i++;

                }

                //Escribo las parcelas en otra hoja
                WritableSheet sheet_parcelas = workbook.createSheet("Parcelas", 1);

                //Agrego los titulos de las columnas
                sheet_parcelas.addCell(new Label(0, 0, "Parcela"));
                sheet_parcelas.addCell(new Label(1, 0, "Cod SAP"));
                sheet_parcelas.addCell(new Label(2, 0, "Superficie"));
                sheet_parcelas.addCell(new Label(3, 0, "Pendiente"));
                sheet_parcelas.addCell(new Label(4, 0, "Comentarios"));
                sheet_parcelas.addCell(new Label(5, 0, "Tipo"));

                ParcelasRelevamientoSQLiteEntity parcelas = new ParcelasRelevamientoSQLiteEntity();

                ArrayList<ParcelasRelevamientoSQLiteEntity> listaParcelas = parcelas.getAllParcelasRelevamiento(getContext());

                if(listaParcelas != null){
                    int j = 1;

                    for (ParcelasRelevamientoSQLiteEntity parcela : listaParcelas){

                        sheet_parcelas.addCell(new Number(0, j, parcela.getId()));
                        sheet_parcelas.addCell(new Label(1, j, parcela.getCod_sap()));
                        sheet_parcelas.addCell(new Number(2, j, parcela.getSuperficie()));
                        sheet_parcelas.addCell(new Number(3, j, parcela.getPendiente()));
                        sheet_parcelas.addCell(new Label(4, j, parcela.getComentarios()));
                        sheet_parcelas.addCell(new Label(5, j, parcela.getTipo()));
                        j++;
                    }
                }


                //Escribo las parcelas en otra hoja
                WritableSheet sheet_arboles = workbook.createSheet("Arboles", 2);

                //Arboles
                sheet_arboles.addCell(new Label(0, 0, "Rodal"));
                sheet_arboles.addCell(new Label(1, 0, "Parcela"));
                sheet_arboles.addCell(new Label(2, 0, "Id Arbol"));
                sheet_arboles.addCell(new Label(3, 0, "Marca"));
                sheet_arboles.addCell(new Label(4, 0, "DAP"));
                sheet_arboles.addCell(new Label(5, 0, "Altura"));
                sheet_arboles.addCell(new Label(6, 0, "altura_poda"));
                sheet_arboles.addCell(new Label(7, 0, "dmsm"));
                sheet_arboles.addCell(new Label(8, 0, "Calidad"));
                sheet_arboles.addCell(new Label(9, 0, "Cosechado"));
                sheet_arboles.addCell(new Label(10, 0, "Fecha de Relevamiento"));



                ArbolesRelevamientoEntity arbolesRelevamientoEntity = new ArbolesRelevamientoEntity();

                ArrayList<ArbolesRelevamientoEntity> listaArboles = arbolesRelevamientoEntity.getAllListaArboles(getContext());

                if(listaArboles != null){

                    int k = 1;

                    for (ArbolesRelevamientoEntity arbol : listaArboles){

                        sheet_arboles.addCell(new Number(0, k, arbol.getIdrodal()));
                        sheet_arboles.addCell(new Number(1, k, arbol.getIdparcela()));
                        sheet_arboles.addCell(new Number(2, k, arbol.getId()));
                        sheet_arboles.addCell(new Label(3, k, arbol.getMarca()));
                        sheet_arboles.addCell(new Number(4, k, arbol.getDap()));
                        sheet_arboles.addCell(new Number(5, k, arbol.getAltura()));
                        sheet_arboles.addCell(new Number(6, k, arbol.getAlturapoda()));
                        sheet_arboles.addCell(new Number(7, k, arbol.getDmsm()));
                        sheet_arboles.addCell(new Label(8, k, arbol.getCalidad()));
                        sheet_arboles.addCell(new Label(9, k, arbol.getCosechado()));
                        sheet_arboles.addCell(new Label(10, k, arbol.getFecha_rel()));
                        k++;
                    }

                }



                workbook.write();
                workbook.close();

                return true;


            } catch (Exception e){
                e.printStackTrace();
            }

        }

        return false;
    }


    private ArrayList<RodalesRelevamiento> getListaRodales(){

        //Obtengo los arrayList consecutivamente
        ArrayList<RodalesRelevamiento> listaRodales = new RodalesRelevamiento().getListaRodalesSelect(getContext());

        if(listaRodales != null){

            Iterator<RodalesRelevamiento> itRodales = listaRodales.iterator();

            while (itRodales.hasNext()){

                RodalesRelevamiento rodal = itRodales.next();
                rodal.setListaParcelas(new ParcelasRelevamientoSQLiteEntity().getParcelasRelevamientoSelectByIdRodal(getContext(), rodal.getIdrodales().toString()));


            }

           itRodales = listaRodales.iterator();
            //Debo cargar los arboles por parcela

            while (itRodales.hasNext()){

                RodalesRelevamiento rodal = itRodales.next();

                if(rodal.getListaParcelas() != null){
                    //Creo un iterador para las parcelas
                    Iterator<ParcelasRelevamientoSQLiteEntity> itParcelas = rodal.getListaParcelas().iterator();

                    //Recorro las parcelas y agrego los arboles
                    while (itParcelas.hasNext()){

                        ParcelasRelevamientoSQLiteEntity parcela = itParcelas.next();
                        parcela.setListaArboles(new ArbolesRelevamientoEntity().getListaArbolesByIdParcela(getContext(), parcela.getId()));

                    }
                }

            }

        }



        return listaRodales;
    }


    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public WorkbookSettings getWorkbookSettings() {
        return workbookSettings;
    }

    public void setWorkbookSettings(WorkbookSettings workbookSettings) {
        this.workbookSettings = workbookSettings;
    }

    public WritableWorkbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(WritableWorkbook workbook) {
        this.workbook = workbook;
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
}
