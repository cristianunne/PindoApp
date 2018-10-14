package forestal.app.pindo.pindo.uploadpostgres;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

import forestal.app.pindo.pindo.R;
import forestal.app.pindo.pindo.config.HomeConfigFragment;
import utillities.entidades.ArbolesRelevamientoEntity;
import utillities.entidades.ParcelasRelevamientoSQLiteEntity;
import utillities.entidades.RodalesRelevamiento;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UploadPostgresFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UploadPostgresFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadPostgresFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ListView listView;

    public UploadPostgresFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UploadPostgresFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadPostgresFragment newInstance(String param1, String param2) {
        UploadPostgresFragment fragment = new UploadPostgresFragment();
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
        return inflater.inflate(R.layout.fragment_upload_postgres, container, false);
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

        //Antes de enviar la lista cargo la cantidad de parcelas
        setListView((ListView) view.findViewById(R.id.lista_rodales));


        ArrayList<RodalesRelevamiento> listaRodalesRelevamiento = new RodalesRelevamiento().getListaRodalesSelect(getContext());

        //Cargo la cantidad de de parcelas por rodal

        if(listaRodalesRelevamiento != null){
            loadParcelasAndArboles(listaRodalesRelevamiento);

            RodalesUploadAdapter rodalesUploadAdapter = new RodalesUploadAdapter(getContext(), listaRodalesRelevamiento, getActivity());

            getListView().setAdapter(rodalesUploadAdapter);


        }


        try{

            Button volver = (Button) view.findViewById(R.id.buttonVolverRod);
            volver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Aca debo iniciar session y direcciono al HOMECONFIG
                    HomeConfigFragment homeConfFr = new HomeConfigFragment();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();


                    ft.replace(R.id.fragment_contenedor, homeConfFr);

                    ft.commit();
                }
            });


        } catch (NullPointerException e){
            e.printStackTrace();
        }



    }



    private void loadParcelasAndArboles( ArrayList<RodalesRelevamiento> listaRodalesRelevamiento){


        if(listaRodalesRelevamiento != null){
            ParcelasRelevamientoSQLiteEntity parcelas_rel = new ParcelasRelevamientoSQLiteEntity();
            Map<Integer, Integer> lista_cantidad =  parcelas_rel.getCantidadParcelasByIdRodal(getContext());


            for(RodalesRelevamiento rodal : listaRodalesRelevamiento){

                if(lista_cantidad != null){

                    for( Map.Entry<Integer, Integer> cant : lista_cantidad.entrySet()){

                        Integer clave = cant.getKey();
                        Integer valor = cant.getValue();

                        if (rodal.getIdrodales().equals(clave)){

                            rodal.setCantidad_parcelas(valor);
                        }

                    }

                } else {
                    rodal.setCantidad_parcelas(0);
                }


            }

            //Carga de la cantidad de Parcelas NO sincronizadas
            Map<Integer, Integer> lista_cantidad_parc_no_sinc =  parcelas_rel.getCantidadParcelasNoUploadByIdRodal(getContext());

            for(RodalesRelevamiento rodal : listaRodalesRelevamiento){

                if(lista_cantidad_parc_no_sinc != null){

                    for( Map.Entry<Integer, Integer> cant : lista_cantidad_parc_no_sinc.entrySet()){

                        Integer clave = cant.getKey();
                        Integer valor = cant.getValue();

                        if (rodal.getIdrodales().equals(clave)){

                            rodal.setCantidad_parcelas_no_upload(valor);
                        }

                    }
                } else {
                    rodal.setCantidad_parcelas_no_upload(0);
                }
            }

            //Cargar cantidad de arboles total

            ArbolesRelevamientoEntity arbolesRelevamientoEntity = new ArbolesRelevamientoEntity();

            Map<Integer, Integer> lista_cantidad_arb_total = arbolesRelevamientoEntity.getCantidadArbolesByIdRodal(getContext());

            for (RodalesRelevamiento rodal : listaRodalesRelevamiento){

                if(lista_cantidad_arb_total != null){
                    for( Map.Entry<Integer, Integer> cant : lista_cantidad_arb_total.entrySet()){

                        Integer clave = cant.getKey();
                        Integer valor = cant.getValue();

                        if (rodal.getIdrodales().equals(clave)){

                            rodal.setCantidad_arboles(valor);
                        }

                    }
                } else {
                    rodal.setCantidad_arboles(0);
                }

            }

            //Cargo los arboles No Sincronizados

            Map<Integer, Integer> lista_cantidad_arb_no_up = arbolesRelevamientoEntity.getCantidadArbolesNoUploadByIdRodal(getContext());

            for (RodalesRelevamiento rodal : listaRodalesRelevamiento){

                if(lista_cantidad_arb_no_up != null){
                    for( Map.Entry<Integer, Integer> cant : lista_cantidad_arb_no_up.entrySet()){

                        Integer clave = cant.getKey();
                        Integer valor = cant.getValue();

                        if (rodal.getIdrodales().equals(clave)){

                            rodal.setCantidad_arboles_no_upload(valor);
                        }

                    }
                } else {
                    rodal.setCantidad_arboles_no_upload(0);
                }

            }



        }

    }


    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }
}
