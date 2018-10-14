package forestal.app.pindo.pindo.config.restricciones.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import forestal.app.pindo.pindo.R;
import forestal.app.pindo.pindo.config.HomeConfigFragment;
import forestal.app.pindo.pindo.config.restricciones.RestriccionesEntity;
import forestal.app.pindo.pindo.config.restricciones.RestriccionesSQLite;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RestriccionesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RestriccionesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestriccionesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View ViewDialog;
    private TextView valueSeek;
    private SeekBar seekBar;
    private int valueSeekNumber;

    private OnFragmentInteractionListener mListener;

    public RestriccionesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RestriccionesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RestriccionesFragment newInstance(String param1, String param2) {
        RestriccionesFragment fragment = new RestriccionesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Recupero el VIEW

        return inflater.inflate(R.layout.fragment_restricciones, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //Recupero el valor en SQLITE para pasarle a el seek
        setViewDialog(getLayoutInflater().inflate(R.layout.dialog_restriccion_dap, null, false));

        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());


        builder.setView(getViewDialog());

        final AlertDialog dialog = builder.create();


        //Recupero el seek e implemento los metodos
        this.seekBar = (SeekBar)getViewDialog().findViewById(R.id.seekBar);
        setValueSeek((TextView)getViewDialog().findViewById(R.id.valueSeek));


        final RestriccionesSQLite restriccionesSqlite = new RestriccionesSQLite();

        RestriccionesEntity restriccionesEntity = restriccionesSqlite.getValueDap(view.getContext());

        Integer valor_inicial = 0;

        if(restriccionesEntity != null){
            valor_inicial = restriccionesEntity.getDap();
        }

        getSeekBar().setProgress(valor_inicial);
        getValueSeek().setText(String.format("%s", valor_inicial + "%"));


        //Configuro el boton volver
        Button volver = (Button) view.findViewById(R.id.buttonVolverSinc);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Vuelvo al fragment anterior
                HomeConfigFragment homefrconfig = new HomeConfigFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                ft.replace(R.id.fragment_contenedor, homefrconfig);
                ft.commit();
            }
        });


        //boton que configura el DAP
        ImageButton btn_dap = (ImageButton)view.findViewById(R.id.id_conf_dap);
        btn_dap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                dialog.setCanceledOnTouchOutside(false);

                Button btn_aceptar_dialog = (Button)getViewDialog().findViewById(R.id.btn_addarbol_acept);
                btn_aceptar_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Guardo el Valor en SQLITE
                        int value_new = getValueSeekNumber();
                        Integer mm = value_new;
                        dialog.dismiss();

                        if (restriccionesSqlite.insertRestriccionDap(v.getContext(), value_new)){

                            Toast.makeText(getContext(), "DAP modificado!", Toast.LENGTH_LONG).show();
                        }


                    }
                });


                Button btn_cancelar_dialog = (Button)getViewDialog().findViewById(R.id.btn_cancelar_dialog);
                btn_cancelar_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });


                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        Integer value = progress;
                        getValueSeek().setText(String.format("%s", value.toString() + "%"));

                        setValueSeekNumber(progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });


                dialog.show();

            }
        });
    }


    public View getViewDialog() {
        return ViewDialog;
    }

    public void setViewDialog(View viewDialog) {
        ViewDialog = viewDialog;
    }

    public TextView getValueSeek() {
        return valueSeek;
    }

    public void setValueSeek(TextView valueSeek) {
        this.valueSeek = valueSeek;
    }

    public int getValueSeekNumber() {
        return valueSeekNumber;
    }

    public void setValueSeekNumber(int valueSeekNumber) {
        this.valueSeekNumber = valueSeekNumber;
    }

    public SeekBar getSeekBar() {
        return seekBar;
    }

    public void setSeekBar(SeekBar seekBar) {
        this.seekBar = seekBar;
    }
}
