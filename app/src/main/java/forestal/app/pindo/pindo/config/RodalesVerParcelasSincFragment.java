package forestal.app.pindo.pindo.config;

import android.annotation.SuppressLint;
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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import forestal.app.pindo.pindo.R;
import forestal.app.pindo.pindo.adapters.RodalesVerParcelasAdapter;
import utillities.entidades.RodalesRelevamiento;
import utillities.entidades.RodalesSincronizedEntity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RodalesVerParcelasSincFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RodalesVerParcelasSincFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RodalesVerParcelasSincFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ListView listView;
    private ArrayList<RodalesSincronizedEntity> lista;


    public RodalesVerParcelasSincFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RodalesVerParcelasSincFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RodalesVerParcelasSincFragment newInstance(String param1, String param2) {
        RodalesVerParcelasSincFragment fragment = new RodalesVerParcelasSincFragment();
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
        return inflater.inflate(R.layout.fragment_rodales_ver_parcelas_sinc, container, false);
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
        this.listView = (ListView) view.findViewById(R.id.lista_rodales_ver_parcelas);

        //Obtengo los RODALES_RELEVAMIENTO que corresponde con los Rodales seleccionados
        RodalesRelevamiento rodales_rel_entity = new RodalesRelevamiento();

        //Obtengo la lista completa de la Tabla de Rodales Relevamiento
        ArrayList<RodalesRelevamiento> lista_rodales_relev = rodales_rel_entity.getListaRodalesSelect(getContext());



        RodalesVerParcelasAdapter rodal_adapter = new RodalesVerParcelasAdapter(getContext(), lista_rodales_relev, getActivity());

        this.listView.setAdapter(rodal_adapter);



        Button btn_volver = (Button)view.findViewById(R.id.buttonVolverRod_ver_sinc);
        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Vuelvo al fragment anterior
                HomeConfigFragment homefrconfig = new HomeConfigFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                ft.replace(R.id.fragment_contenedor, homefrconfig);
                ft.commit();
            }
        });






    }
}
