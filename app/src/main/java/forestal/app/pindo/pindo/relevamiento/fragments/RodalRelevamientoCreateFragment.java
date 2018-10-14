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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import forestal.app.pindo.pindo.R;
import utillities.entidades.RodalesRelevamiento;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RodalRelevamientoCreateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RodalRelevamientoCreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RodalRelevamientoCreateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RodalRelevamientoCreateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RodalRelevamientoCreateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RodalRelevamientoCreateFragment newInstance(String param1, String param2) {
        RodalRelevamientoCreateFragment fragment = new RodalRelevamientoCreateFragment();
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
        return inflater.inflate(R.layout.fragment_rodal_relevamiento_create, container, false);
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

        saveRodalRelevamiento(view, savedInstanceState);

    }




    private void saveRodalRelevamiento(View view, Bundle bundle){

        //Obtengo los textos introducidos
        final ViewHolder holder = new ViewHolder();

        try{

            holder.tv_addrodal_id = (EditText)view.findViewById(R.id.tv_addrodal_id);
            holder.tv_addrodal_codsap = (EditText)view.findViewById(R.id.tv_addrodal_codsap);
            holder.tv_addrodal_campo = (EditText)view.findViewById(R.id.tv_addrodal_campo);
            holder.tv_addrodal_empresa = (EditText)view.findViewById(R.id.tv_addrodal_empresa);
            holder.tv_addrodal_uso = (EditText)view.findViewById(R.id.tv_addrodal_uso);

            holder.tv_addrodal_fechaplantacion = (EditText)view.findViewById(R.id.tv_addrodal_fechaplantacion);
            holder.tv_addrodal_fechainv = (EditText)view.findViewById(R.id.tv_addrodal_fechainv);
            holder.tv_addrodal_especie = (EditText)view.findViewById(R.id.tv_addrodal_especie);


            holder.btn_addrodal_aceptar = (Button)view.findViewById(R.id.btn_addrodal_aceptar);
            holder.btn_addrodal_cancelar = (Button)view.findViewById(R.id.btn_addrodal_cancelar);




            holder.btn_addrodal_aceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Valido los campos

                    //Obtengo los textos
                    String rodal_id = holder.tv_addrodal_id.getText().toString();
                    String cod_sap = holder.tv_addrodal_codsap.getText().toString();
                    String campo = holder.tv_addrodal_campo.getText().toString();
                    String empresa = holder.tv_addrodal_empresa.getText().toString();
                    String uso = holder.tv_addrodal_uso.getText().toString();

                    String fecha_pl = holder.tv_addrodal_fechaplantacion.getText().toString();
                    String fecha_inv = holder.tv_addrodal_fechainv.getText().toString();
                    String especie = holder.tv_addrodal_especie.getText().toString();


                    Boolean res = validateFields(holder, rodal_id, cod_sap, campo, empresa, uso);

                    if (res){

                        Integer id_rodal = Integer.valueOf(rodal_id);
                        RodalesRelevamiento rodal_rel = new RodalesRelevamiento(id_rodal, cod_sap, campo, uso, null, empresa, null, 0.0, 0.0);

                        rodal_rel.setFecha_plantacion(fecha_pl);
                        rodal_rel.setFecha_inv(fecha_inv);
                        rodal_rel.setEspecie(especie);

                        if (rodal_rel.insertRodalToRodalesRelevamiento(getContext(), rodal_rel)){

                            Toast.makeText(getContext(), "Rodal Agregado Correctamente!", Toast.LENGTH_LONG).show();

                            //Tengo que volver a cargar este fragment
                            RodalesRelevamientoFragment rodal_rel_ft = new RodalesRelevamientoFragment();
                            try{
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);

                                ft.replace(R.id.fragment_contenedor, rodal_rel_ft);

                                ft.commit();
                            } catch (NullPointerException e){
                                e.printStackTrace();
                            }


                        } else{
                            Toast.makeText(getContext(), "Error al agregar el Rodal!", Toast.LENGTH_LONG).show();
                        }


                    } else{
                        Toast.makeText(getContext(), "Complete todos los campos para avanzar!", Toast.LENGTH_LONG).show();
                    }



                }
            });

            holder.btn_addrodal_cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RodalesRelevamientoFragment rodal_rel_ft = new RodalesRelevamientoFragment();

                    try{
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);

                        ft.replace(R.id.fragment_contenedor, rodal_rel_ft);

                        ft.commit();

                    } catch (NullPointerException e){
                        e.printStackTrace();
                    }

                }
            });






        } catch (NullPointerException e){
            e.printStackTrace();
        }



    }


    private Boolean validateFields(ViewHolder holder, String rodal_id, String cod_sap, String campo, String empresa, String uso){

        Boolean res = true;

        if (rodal_id.equals("")){
            holder.tv_addrodal_id.setBackgroundResource(R.drawable.bordes_campos_error);
            holder.tv_addrodal_id.setHint("Complete");
            res = false;
        }

        if (cod_sap.equals("")){
            holder.tv_addrodal_codsap.setBackgroundResource(R.drawable.bordes_campos_error);
            holder.tv_addrodal_codsap.setHint("Complete");
            res = false;
        }

        if (campo.equals("")){
            holder.tv_addrodal_campo.setBackgroundResource(R.drawable.bordes_campos_error);
            holder.tv_addrodal_campo.setHint("Complete");
            res = false;
        }

        if (empresa.equals("")){
            holder.tv_addrodal_empresa.setBackgroundResource(R.drawable.bordes_campos_error);
            holder.tv_addrodal_empresa.setHint("Complete");
            res = false;
        }

        if (uso.equals("")){
            holder.tv_addrodal_uso.setBackgroundResource(R.drawable.bordes_campos_error);
            holder.tv_addrodal_uso.setHint("Complete");
            res = false;
        }


        return res;
    }



    static class ViewHolder{

        EditText tv_addrodal_id;
        EditText tv_addrodal_codsap;
        EditText tv_addrodal_campo;
        EditText tv_addrodal_empresa;
        EditText tv_addrodal_uso;
        EditText tv_addrodal_fechaplantacion;
        EditText tv_addrodal_fechainv;
        EditText tv_addrodal_especie;



        Button btn_addrodal_aceptar;
        Button btn_addrodal_cancelar;
    }

}
