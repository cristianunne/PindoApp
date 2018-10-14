package forestal.app.pindo.pindo.relevamiento.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import forestal.app.pindo.pindo.R;

import forestal.app.pindo.pindo.relevamiento.adapters.RodalesRelevamientoAdapter;
import utillities.entidades.ParcelasRelevamientoSQLiteEntity;
import utillities.entidades.RodalesRelevamiento;


/**
 * Esta clase va a recibir la Lista de los Rodales a Relevar
 */
public class RodalesRelevamientoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private ListView listview;

    private OnFragmentInteractionListener mListener;

    public RodalesRelevamientoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RodalesRelevamientoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RodalesRelevamientoFragment newInstance(String param1, String param2) {
        RodalesRelevamientoFragment fragment = new RodalesRelevamientoFragment();
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
        return inflater.inflate(R.layout.fragment_rodales_relevamiento, container, false);
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

        //Obtengo el List view
        this.listview = (ListView) view.findViewById(R.id.lista_rodales_relevamiento);
        RodalesRelevamiento rodal_rel = new RodalesRelevamiento();

        ArrayList<RodalesRelevamiento> lista = rodal_rel.getListaRodalesSelect(getContext());

        if (lista != null){
            //Consulto la cantidad de parccelas por idrodal
            loadCantidadParcelas(lista);


            RodalesRelevamientoAdapter rodal_adapter = new RodalesRelevamientoAdapter(view.getContext(), lista, getActivity());
            this.listview.setAdapter(rodal_adapter);

        }





        //Evento del boton crear Rodal
        Button btn_crear_rodal = (Button)view.findViewById(R.id.buttonCrearRodal);
        btn_crear_rodal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RodalRelevamientoCreateFragment fr_rodcreate = new RodalRelevamientoCreateFragment();

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);

                ft.replace(R.id.fragment_contenedor, fr_rodcreate);

                ft.commit();

            }
        });



    }


    public void loadCantidadParcelas(ArrayList<RodalesRelevamiento> lista){

        ParcelasRelevamientoSQLiteEntity parcelas_rel = new ParcelasRelevamientoSQLiteEntity();

        Map<Integer, Integer> lista_cantidad =  parcelas_rel.getCantidadParcelasByIdRodal(getContext());


        if (lista_cantidad != null){

            for (int i = 0; i < lista.size(); i++){

                RodalesRelevamiento rodal = lista.get(i);
                for( Map.Entry<Integer, Integer> cant : lista_cantidad.entrySet()){

                    Integer clave = cant.getKey();
                    Integer valor = cant.getValue();

                    if (rodal.getIdrodales().equals(clave)){

                        rodal.setCantidad_parcelas(valor);
                    }

                }


            }

        }



    }
}
