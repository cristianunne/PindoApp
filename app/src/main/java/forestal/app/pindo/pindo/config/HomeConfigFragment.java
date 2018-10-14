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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import forestal.app.pindo.pindo.R;
import forestal.app.pindo.pindo.config.restricciones.fragment.RestriccionesFragment;
import forestal.app.pindo.pindo.uploadpostgres.UploadPostgresFragment;
import utillities.entidades.RodalesRelevamiento;
import utillities.utilidades.Utilidades;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeConfigFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeConfigFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeConfigFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ImageButton imgbutton1conf_postgis, imageButtonSyncRodal;


    public HomeConfigFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeConfigFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeConfigFragment newInstance(String param1, String param2) {
        HomeConfigFragment fragment = new HomeConfigFragment();
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
        return inflater.inflate(R.layout.fragment_home_config, container, false);
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

        imgbutton1conf_postgis = view.findViewById(R.id.id_conf_postgis);
        imgbutton1conf_postgis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               //Voy a instanciar la clase Conexion PostgisConfig
               ConexionPostgisConfig con_postgis = new ConexionPostgisConfig();

               //Obtengo un mapa con las configuraciones
               Map<String, String> configu_ = con_postgis.getConfigPostis(v.getContext());

               Integer tam = configu_.size();

               //Si el tamaño en > 1 encontro los parametros de onfiguraciones
               if(tam > 0){

                   //Construyo el Fragmento y le mando los parametros de configuracion
                   PostgisConfigFragment postFr = new PostgisConfigFragment();

                   //Mando los Parametros
                   Bundle args = new Bundle();
                   args.putString(Utilidades.POSTGIS_CONFIG_DB, configu_.get(Utilidades.POSTGIS_CONFIG_DB));
                   args.putString(Utilidades.POSTGIS_CONFIG_PORT, configu_.get(Utilidades.POSTGIS_CONFIG_PORT));
                   args.putString(Utilidades.POSTGIS_CONFIG_HOST, configu_.get(Utilidades.POSTGIS_CONFIG_HOST));
                   args.putString(Utilidades.POSTGIS_CONFIG_USER, configu_.get(Utilidades.POSTGIS_CONFIG_USER));
                   args.putString(Utilidades.POSTGIS_CONFIG_PASSWORD, configu_.get(Utilidades.POSTGIS_CONFIG_PASSWORD));
                   postFr.setArguments(args);

                   FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();


                   ft.replace(R.id.fragment_contenedor, postFr);
                   ft.commit();


               }

            }
        });

        imageButtonSyncRodal = (ImageButton) view.findViewById(R.id.id_sinc_rodales);

        imageButtonSyncRodal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RodalesSincronizarConfigFragment rodalesSyncfR = new RodalesSincronizarConfigFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                ft.replace(R.id.fragment_contenedor, rodalesSyncfR);
                ft.commit();

            }
        });



        ImageButton imgButtonSincParc = (ImageButton) view.findViewById(R.id.id_sinc_parcelas);
        imgButtonSincParc.setOnClickListener(new View.OnClickListener() {
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

        //Configuracion de Relevamiento

        ImageButton btn_config_rel = (ImageButton)view.findViewById(R.id.id_conf_restricciones);
        btn_config_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RestriccionesFragment restriccionesFragment = new RestriccionesFragment();

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);

                ft.replace(R.id.fragment_contenedor, restriccionesFragment);

                ft.commit();

            }
        });


        ImageButton btn_uploadpostgres = (ImageButton) view.findViewById(R.id.id_enviar_datos);
        btn_uploadpostgres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UploadPostgresFragment uploadPostgresFragment = new UploadPostgresFragment();


                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);

                ft.replace(R.id.fragment_contenedor, uploadPostgresFragment);

                ft.commit();


            }
        });



    }


}
