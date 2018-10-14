package forestal.app.pindo.pindo.config;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Console;
import java.util.ArrayList;
import java.util.Map;

import forestal.app.pindo.pindo.R;
import forestal.app.pindo.pindo.adapters.ParcelasAdapter;
import postgres.conexion.DBPostgresConnection;
import utillities.entidades.ParcelasPostgresEntity;
import utillities.entidades.ParcelasRelevamientoSQLiteEntity;
import utillities.entidades.RodalesRelevamiento;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ParcelasListByRodalFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ParcelasListByRodalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParcelasListByRodalFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static String idrodal;

    private OnFragmentInteractionListener mListener;

    public ParcelasListByRodalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment ParcelasListByRodalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ParcelasListByRodalFragment newInstance(Bundle arguments) {
        ParcelasListByRodalFragment fragment = new ParcelasListByRodalFragment();
        if (arguments != null){
            idrodal = arguments.getString("idrodal");

            fragment.setArguments(arguments);
        }

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parcelas_list_by_rodal, container, false);
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
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Bundle bun = getArguments();

        String id_rodal = bun.getString("idrodal");

        //Instancio el Listview aca
        ListView listview_parcelas = (ListView)view.findViewById(R.id.lista_parcelas_by_rodal);

        //EL id del rodal esta en la variable estatica guardada en el constructor

        //Traigo las Parcelas by IDRODAL
        ParcelasRelevamientoSQLiteEntity parcelas_rel = new ParcelasRelevamientoSQLiteEntity();


        final ArrayList<ParcelasRelevamientoSQLiteEntity> parc_post = parcelas_rel.getParcelasRelevamientoByIdRodal(getContext(), id_rodal);

        //dEBO CONTROLAR QUE LAS PARCELAS NO SEAN NULL
        if (parc_post != null){
            //creo el adapter
            ParcelasAdapter parc_adapter = new ParcelasAdapter(getContext(), parc_post, getActivity());
            listview_parcelas.setAdapter(parc_adapter);



            Button btn_selectAll = (Button)view.findViewById(R.id.buttonSelectAll);

            btn_selectAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ParcelasRelevamientoSQLiteEntity select_parc = new ParcelasRelevamientoSQLiteEntity();

                    Integer cant_proc = 0;

                    for (ParcelasRelevamientoSQLiteEntity parcela : parc_post){

                        if (select_parc.setParcelaRelevamientoToTrue(getContext(), parcela.getId().toString())){
                            cant_proc++;
                        }
                    }

                    if (cant_proc > 0){
                        Toast.makeText(getContext(), "Se han seleccionado: " + cant_proc.toString() + " parcelas.", Toast.LENGTH_LONG).show();

                        //Obtengo los RODALES_RELEVAMIENTO que corresponde con los Rodales seleccionados
                        RodalesRelevamiento rodales_rel_entity = new RodalesRelevamiento();

                        //Obtengo la lista completa de la Tabla de Rodales Relevamiento
                        ArrayList<RodalesRelevamiento> lista_rodales_relev = rodales_rel_entity.getListaRodalesSelect(getContext());

                        if(lista_rodales_relev != null){
                            RodalesVerParcelasSincFragment rodalesVerParcfr = new RodalesVerParcelasSincFragment();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                            ft.replace(R.id.fragment_contenedor, rodalesVerParcfr);
                            ft.commit();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            View mview = getLayoutInflater().inflate(R.layout.dialog_result_error, null);
                            builder.setView(mview);
                            final AlertDialog dialog = builder.create();
                            //Seteo el texto de error
                            TextView txt_msj = (TextView)mview.findViewById(R.id.text_dialog_error);
                            txt_msj.setText("No se han Seleccionado los Rodales...");
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


                    } else {
                        Toast.makeText(getContext(), "Error al seleccionar las parcelas. Intente nuevamente.", Toast.LENGTH_LONG).show();
                    }


                }
            });
        }






        Button btn_volver = (Button)view.findViewById(R.id.buttonVolverParc);
        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Obtengo los RODALES_RELEVAMIENTO que corresponde con los Rodales seleccionados
                RodalesRelevamiento rodales_rel_entity = new RodalesRelevamiento();

                //Obtengo la lista completa de la Tabla de Rodales Relevamiento
                ArrayList<RodalesRelevamiento> lista_rodales_relev = rodales_rel_entity.getListaRodalesSelect(getContext());

                if(lista_rodales_relev != null){
                    RodalesVerParcelasSincFragment rodalesVerParcfr = new RodalesVerParcelasSincFragment();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    ft.replace(R.id.fragment_contenedor, rodalesVerParcfr);
                    ft.commit();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    View mview = getLayoutInflater().inflate(R.layout.dialog_result_error, null);
                    builder.setView(mview);
                    final AlertDialog dialog = builder.create();
                    //Seteo el texto de error
                    TextView txt_msj = (TextView)mview.findViewById(R.id.text_dialog_error);
                    txt_msj.setText("No se han Seleccionado los Rodales...");
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
        });


    }
}
