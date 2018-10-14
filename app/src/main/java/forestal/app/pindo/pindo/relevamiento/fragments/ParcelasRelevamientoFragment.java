package forestal.app.pindo.pindo.relevamiento.fragments;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import forestal.app.pindo.pindo.MapsActivity;
import forestal.app.pindo.pindo.R;
import forestal.app.pindo.pindo.relevamiento.adapters.ParcelasRelevamientoAdapter;
import forestal.app.pindo.pindo.relevamiento.dialogs.DialogParcelaRelevamiento;
import utillities.entidades.ArbolesRelevamientoEntity;
import utillities.entidades.ListaParcelasRelevamiento;
import utillities.entidades.ParcelasRelevamientoSQLiteEntity;
import utillities.entidades.RodalesRelevamiento;
import utillities.entidades.RodalesSincronizedEntity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ParcelasRelevamientoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ParcelasRelevamientoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParcelasRelevamientoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ListView listview;
    ArrayList<ParcelasRelevamientoSQLiteEntity> lista;

    public ParcelasRelevamientoFragment() {
        // Required empty public constructor


    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ParcelasRelevamientoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ParcelasRelevamientoFragment newInstance(String param1, String param2) {
        ParcelasRelevamientoFragment fragment = new ParcelasRelevamientoFragment();
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
        return inflater.inflate(R.layout.fragment_parcelas_relevamiento, container, false);
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


        Bundle args = getArguments();

        try{

            String id_rodal = args.getString("idrodal");

            //Consulto si hay parcelas para relevar
            ParcelasRelevamientoSQLiteEntity parcelas = new ParcelasRelevamientoSQLiteEntity();

            Integer cantidad = parcelas.getCantidadParcelasSelectByIdRodal(getContext(), id_rodal);

            if (cantidad <= 0){

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View mview = LayoutInflater.from(getContext()).inflate(R.layout.dialog_result_error, null);

                builder.setView(mview);
                final AlertDialog dialog = builder.create();
                //Seteo el texto de error
                TextView txt_msj = (TextView)mview.findViewById(R.id.text_dialog_error);
                String msj = "No existen Parcelas para el Rodal seleccionado. Por favor sincronice o cree una nueva parcela!";
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

            } else {

                //Obtengo el List view
                this.listview = (ListView) view.findViewById(R.id.lista_parcelas_relevamiento);

                ParcelasRelevamientoSQLiteEntity parc_rel = new ParcelasRelevamientoSQLiteEntity();
                ArrayList<ParcelasRelevamientoSQLiteEntity> lista = parc_rel.getParcelasRelevamientoSelectByIdRodal(getContext(), id_rodal);

                //Aca debo cargar los datos de los arboles
                loadCantidadArboles(lista);


                setLista(lista);

                ParcelasRelevamientoAdapter parcela_adapter = new ParcelasRelevamientoAdapter(getContext(), lista, getActivity());
                this.listview.setAdapter(parcela_adapter);


            }



        } catch (NullPointerException e){
            e.printStackTrace();
        }

        Button btn_crear_parcela = (Button)view.findViewById(R.id.buttonCrearParcela);
        btn_crear_parcela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Traigo los datos del Rodal Actual
                Bundle args = getArguments();

               try{
                   String id_rodal = args.getString("idrodal");
                   String cod_sap = args.getString("cod_sap");
                   DialogParcelaRelevamiento dlg_par_rel = new DialogParcelaRelevamiento(getContext(), id_rodal, cod_sap, getActivity());
                   dlg_par_rel.showDialogParcelarelevamiento();


               } catch (NullPointerException e){
                   e.printStackTrace();
               }





            }
        });


        ImageButton bnt_volver = (ImageButton)view.findViewById(R.id.buttonParc_RelVolver);
        bnt_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RodalesRelevamientoFragment rodal_rel_ft = new RodalesRelevamientoFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);

                ft.replace(R.id.fragment_contenedor, rodal_rel_ft);

                ft.commit();
            }
        });

        Button bnt_vermapa_all = (Button)view.findViewById(R.id.buttonVerMapaAll);
        bnt_vermapa_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle args = getArguments();
                final String id_rodal = args.getString("idrodal");
                RodalesSincronizedEntity rodal = new RodalesSincronizedEntity().getRodalById(getContext(), id_rodal);

                ArrayList<String> lista_ = new ArrayList<>();

                for(ParcelasRelevamientoSQLiteEntity parc : getLista()){

                    lista_.add(parc.getId().toString());
                }

                Intent intent = new Intent(getContext(), MapsActivity.class);
                intent.putStringArrayListExtra("lista", lista_);
                intent.putExtra("operacion", "parcelas_all");
                intent.putExtra("rodal_id", id_rodal);


                try{
                    intent.putExtra("rodal", rodal.getGeometry());
                    intent.putExtra("centroid_lat", rodal.getLatitud().toString());
                    intent.putExtra("centroid_longi", rodal.getLongitud().toString());

                } catch (NullPointerException e){
                    intent.putExtra("rodal", "NO");
                    intent.putExtra("centroid_lat", "NO");
                    intent.putExtra("centroid_longi", "NO");
                }

                startActivity(intent);
            }
        });

        ImageButton refresh = (ImageButton) view.findViewById(R.id.buttonParc_Relrefresh);


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle args = getArguments();
                String id_rodal = args.getString("idrodal");
                String cod_sap = args.getString("cod_sap");

                //Controlar el el fragment la cantidad de parcelas relevamiento
                ParcelasRelevamientoFragment fr_parcrel = new ParcelasRelevamientoFragment();

                Bundle arguments = new Bundle();
                arguments.putString("idrodal", id_rodal);
                arguments.putString("cod_sap", cod_sap);
                fr_parcrel.setArguments(arguments);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();


                ft.replace(R.id.fragment_contenedor, fr_parcrel);

                ft.commit();
            }
        });





    }


    public void loadCantidadArboles(ArrayList<ParcelasRelevamientoSQLiteEntity> lista){

        ArbolesRelevamientoEntity arboles_rel = new ArbolesRelevamientoEntity();

        Map<Integer, Integer> lista_cantidad =  arboles_rel.getCantidadArbolesByIdParcelas(getContext());


        if (lista_cantidad != null){

            for (int i = 0; i < lista.size(); i++){

                ParcelasRelevamientoSQLiteEntity parcela = lista.get(i);
                for( Map.Entry<Integer, Integer> cant : lista_cantidad.entrySet()){

                    Integer clave = cant.getKey();
                    Integer valor = cant.getValue();

                    //Uso el id de SQLITE porque ese guarda como clave foranea
                    if (parcela.getId().equals(clave)){

                        parcela.setCantidad_arboles(valor);
                    }
                }

            }
        }

    }

    public ArrayList<ParcelasRelevamientoSQLiteEntity> getLista() {
        return lista;
    }

    public void setLista(ArrayList<ParcelasRelevamientoSQLiteEntity> lista) {
        this.lista = lista;
    }
}
