package utillities.utilidades;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import forestal.app.pindo.pindo.R;

public class DialogPindo {


    public AlertDialog dialog;
    private View mview;
    private String text;
    private Context context;

    public DialogPindo(Context context, String text) {


        this.mview = LayoutInflater.from(context).inflate(R.layout.dialog_result_error, null, false);
        this.text = text;
        dialog = null;
        setContext(context);
    }

    public void createDialogError(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(mview);
        dialog = builder.create();

        TextView output = (TextView) mview.findViewById(R.id.text_dialog_error);
        output.setText(text);

        dialog.setCanceledOnTouchOutside(false);

        Button btn_aceptar_dialog = (Button)mview.findViewById(R.id.btn_aceptar_dialog_error);
        btn_aceptar_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });
        
    }

    public void showDialogError()
    {
        try{
            this.dialog.show();
        } catch (NullPointerException e){
            e.printStackTrace();
        }

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
