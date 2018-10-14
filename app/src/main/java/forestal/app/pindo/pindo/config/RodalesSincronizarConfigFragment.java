package forestal.app.pindo.pindo.config;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import forestal.app.pindo.pindo.R;
import forestal.app.pindo.pindo.RodalesHome;
import forestal.app.pindo.pindo.config.sincronizedclass.SincronizarRodalesSQlite;
import postgres.conexion.DBPostgresConnection;
import utillities.entidades.RodalesEntity;
import utillities.entidades.RodalesSincronizedEntity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RodalesSincronizarConfigFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RodalesSincronizarConfigFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RodalesSincronizarConfigFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ImageButton btn_verRodales, btn_sincronizar;
    LayoutInflater lyinflater;

    Button volver;


    public RodalesSincronizarConfigFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RodalesSincronizarConfigFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RodalesSincronizarConfigFragment newInstance(String param1, String param2) {
        RodalesSincronizarConfigFragment fragment = new RodalesSincronizarConfigFragment();
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
        return inflater.inflate(R.layout.fragment_rodales_sincronizar_config, container, false);
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
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Configuro el boton volver
        volver = (Button) view.findViewById(R.id.buttonVolverSinc);
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


        lyinflater = getLayoutInflater();
        btn_sincronizar = (ImageButton) view.findViewById(R.id.id_sinc_rodal_sinc);

        btn_sincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //oBTENGO LO PARAMETROS DE CONFIGURACION DE POSTGIS
                ConexionPostgisConfig con_postgis = new ConexionPostgisConfig();
                //Mapa con la configuracion
                Map<String, String> configu_ = con_postgis.getConfigPostis(v.getContext());

                //Llamo a la calse que maneja la consulta a Postgres
                DBPostgresConnection db = new DBPostgresConnection(configu_);

                //Consulto si se realizo la conexión adecuadamente
                if (db.getConn() == null){
                    //No me conecto asi que imprimo el mensaje con el dialog de error


                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    View mview = lyinflater.inflate(R.layout.dialog_result_error, null);
                    builder.setView(mview);
                    final AlertDialog dialog = builder.create();
                    //Seteo el texto de error
                    TextView txt_msj = (TextView)mview.findViewById(R.id.text_dialog_error);
                    txt_msj.setText(db.get_mensaje().toString());
                    dialog.setCanceledOnTouchOutside(false);

                    Button btn_aceptar_dialog_error = (Button)mview.findViewById(R.id.btn_aceptar_dialog_error);
                    btn_aceptar_dialog_error.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                } else {


                    //Obtenemos la lista de rodales de Postgres
                    RodalesEntity rodalEnt = new RodalesEntity();
                    ArrayList<RodalesEntity> listaRodales = rodalEnt.getListaRodales(db);
                    View mview = getLayoutInflater().inflate(R.layout.dialog_progress, null);


                    //Voy a instanciar la clase Conexion POSTGRES
                    ArrayList<RodalesEntity> lista_rodales_aux = rodalEnt.getListaRodalesWithGeometry(db, listaRodales);

                    //Creamos la clase que sincroniza a SQLIte
                    SincronizarRodalesSQlite sinc_rodal_sqlite = new SincronizarRodalesSQlite(getActivity(), mview);

                    //Sincronice los rodales e inserto en la tabla

                    sinc_rodal_sqlite.insertListaRodalesToTablaRodal(lista_rodales_aux);

                    //MUESTRO LAS COORDENADAS

                }

            }
        });

        //Boton que lleva a ver los Rodales Sincronizados
        btn_verRodales = (ImageButton)view.findViewById(R.id.id_sinc_rodal_ver);
        btn_verRodales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Antes de crear el adaprte verifico los nodos de la lista para que no se repitan con los ya ingresados
                RodalesSincronizedEntity rodales_ent = new RodalesSincronizedEntity();
                ArrayList<RodalesSincronizedEntity> lista = rodales_ent.getListaRodales(view.getContext());

                if (lista != null){
                    //Abrir el Fragment que muestra la lista
                    RodalesHome rodalesfragment = new RodalesHome();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    ft.replace(R.id.fragment_contenedor, rodalesfragment);

                    ft.commit();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    View mview = getLayoutInflater().inflate(R.layout.dialog_result_error, null);
                    builder.setView(mview);
                    final AlertDialog dialog = builder.create();
                    //Seteo el texto de error
                    TextView txt_msj = (TextView)mview.findViewById(R.id.text_dialog_error);
                    txt_msj.setText("No se han sincronizado los Rodales");
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
