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
import android.widget.EditText;
import android.widget.Toast;

import forestal.app.pindo.pindo.R;
import utillities.utilidades.Utilidades;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostgisConfigFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PostgisConfigFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostgisConfigFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PostgisConfigFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostgisConfigFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostgisConfigFragment newInstance(String param1, String param2) {
        PostgisConfigFragment fragment = new PostgisConfigFragment();
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
        return inflater.inflate(R.layout.fragment_postgis_config, container, false);
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

        //rECUPERO LOS eDITtEXT
        final EditText Et_db_name = (EditText)view.findViewById(R.id.editTextdbName);
        final EditText Et_host = (EditText)view.findViewById(R.id.editTextHost);
        final EditText Et_port = (EditText)view.findViewById(R.id.editTextPort);
        final EditText Et_user = (EditText)view.findViewById(R.id.editTextUser);
        final EditText Et_pass = (EditText)view.findViewById(R.id.editTextPass);

        //Recupero los botones buttonAceptarPgisConf buttonCancelarPgisConf
        Button btn_AceptarPgisConf = (Button) view.findViewById(R.id.buttonAceptarPgisConf);
        Button btn_CancelarPgisConf = (Button) view.findViewById(R.id.buttonCancelarPgisConf);

        Bundle bun = getArguments();

        Et_db_name.setText(bun.getString(Utilidades.POSTGIS_CONFIG_DB));
        Et_host.setText(bun.getString(Utilidades.POSTGIS_CONFIG_HOST));
        Et_port.setText(bun.getString(Utilidades.POSTGIS_CONFIG_PORT));
        Et_user.setText(bun.getString(Utilidades.POSTGIS_CONFIG_USER));
        Et_pass.setText(bun.getString(Utilidades.POSTGIS_CONFIG_PASSWORD));

        btn_AceptarPgisConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Recupero lo datos de los edittex

                Integer port = Integer.parseInt(Et_port.getText().toString());

                ConexionPostgisConfig con_postgis = new ConexionPostgisConfig();
                Boolean res = con_postgis.insertConfigToPostgisConfig(v.getContext(), Et_user.getText().toString(), Et_pass.getText().toString(), Et_host.getText().toString(), port, Et_db_name.getText().toString());

                if (res == true){

                    //Agrego la ventana de alerta
                    final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                    View mview = getLayoutInflater().inflate(R.layout.dialog_result, null);
                    // Get the layout inflater
                    //LayoutInflater inflater = (LayoutInflater) builder.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    Button btn = (Button) mview.findViewById(R.id.btn_aceptar_dialog);

                    builder.setView(mview);
                    final AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(false);

                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v_) {
                            HomeConfigFragment homeConfFr = new HomeConfigFragment();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.fragment_contenedor, homeConfFr);
                            ft.commit();

                            dialog.dismiss();
                        }
                    });

                    dialog.show();



                } else {
                    Toast.makeText(v.getContext(), "Error al Ingresar los parámetros de Configuración", Toast.LENGTH_LONG).show();
                }

            }
        });

        btn_CancelarPgisConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //direcciono al HOMECONFIG
                HomeConfigFragment homeConfFr = new HomeConfigFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_contenedor, homeConfFr);
                ft.commit();
            }
        });



    }
}
