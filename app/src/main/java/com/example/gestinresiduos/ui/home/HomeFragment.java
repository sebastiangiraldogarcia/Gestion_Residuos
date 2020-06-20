package com.example.gestinresiduos.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.gestinresiduos.MainActivity;
import com.example.gestinresiduos.R;
import com.example.gestinresiduos.UsuariosQR;
import com.example.gestinresiduos.crud.CRUDResi;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private Spinner spinnerUbicacion;
    private Spinner spinnerMaterial;
    private Spinner spinnerLiquido;
    private Spinner spinnerPeso;
    private Button insertar;
    private Button insertarQR;

    private String user;
    private String rol;
    private String color;
    private String idConte;
    private String capacidadConte;
    private String contenidoConte;
    private int peso;

    private List<String> listaUbicaciones;
    private CRUDResi crud;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        spinnerMaterial = (Spinner) view.findViewById(R.id.spinnerMaterial);
        spinnerUbicacion = (Spinner) view.findViewById(R.id.spinnerUbicacion);
        spinnerLiquido = (Spinner) view.findViewById(R.id.spinnerLiquido);
        spinnerPeso = (Spinner) view.findViewById(R.id.spinnerPeso);

        listaUbicaciones = new ArrayList<String>();
        crud = new CRUDResi();

        String []opcionesMaterial = {"Seleccione un material","Plástico o aluminio", "Cartón o papel", "Vidrio o metal", "Orgánico", "Otro"};
        ArrayAdapter<String> adapterM = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, opcionesMaterial);
        spinnerMaterial.setAdapter(adapterM);

        new HomeFragment.BuscarUbicacion(getActivity()).execute();

        String []opcionesLiquido = {"Seleccione una opción","Si", "No"};
        ArrayAdapter<String> adapterL = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, opcionesLiquido);
        spinnerLiquido.setAdapter(adapterL);

        String []opcionesPeso = {"Seleccione un tipo de peso","Alto", "Medio", "Bajo"};
        ArrayAdapter<String> adapterP = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, opcionesPeso);
        spinnerPeso.setAdapter(adapterP);

        MainActivity activity = (MainActivity)getActivity();

        Bundle b = activity.getMyData();

        Log.d("lat", b.getString("user"));

        if(b != null){
            //rol = getArguments().getString("rol");
            user = b.getString("user");
        }

        color = "sin info";
        peso = 0;

        insertarQR=(Button) view.findViewById(R.id.qr);
        insertarQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), UsuariosQR.class);
                i.putExtra("user", user.trim());
                startActivity(i);
            }
        });


        //Insertamos un residuo.
        insertar=(Button) view.findViewById(R.id.insertar);

        insertar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!spinnerMaterial.getSelectedItem().toString().trim().equalsIgnoreCase("Seleccione un material")&&
                        !spinnerUbicacion.getSelectedItem().toString().trim().equalsIgnoreCase("Seleccione una ubicación")&&
                        !spinnerLiquido.getSelectedItem().toString().trim().equalsIgnoreCase("Seleccione una opción")&&
                        !spinnerPeso.getSelectedItem().toString().trim().equalsIgnoreCase("Seleccione un tipo de peso")) {
                    if (spinnerMaterial.getSelectedItem().toString().trim().equals("Plástico o aluminio")) {
                        color = "Amarillo";
                    } else if (spinnerMaterial.getSelectedItem().toString().trim().equals("Cartón o papel")) {
                        color = "Azul";
                    } else if (spinnerMaterial.getSelectedItem().toString().trim().equals("Vidrio o metal")) {
                        color = "Verde";
                    } else if (spinnerMaterial.getSelectedItem().toString().trim().equals("Orgánico")) {
                        color = "Marron";
                    } else if (spinnerMaterial.getSelectedItem().toString().trim().equals("Otro")) {
                        color = "Gris";
                    }

                    if (spinnerPeso.getSelectedItem().toString().trim().equals("Alto")) {
                        peso = 30;
                    } else if (spinnerPeso.getSelectedItem().toString().trim().equals("Medio")) {
                        peso = 20;
                    } else if (spinnerPeso.getSelectedItem().toString().trim().equals("Bajo")) {
                        peso = 10;
                    }

                    if(!color.equalsIgnoreCase("")&& peso!=0){
                        Log.d("rol", color);
                        new HomeFragment.BuscarIdConte(getActivity()).execute();
                    }

                }else {
                    Toast.makeText(getActivity(), "Asegurese de que todos los campos tengan información",
                            Toast.LENGTH_LONG).show();
                }
            }

        });

        return view;
    }

    public void qr (View v){
        Intent i = new Intent(getActivity(), UsuariosQR.class);
        i.putExtra("user", user.trim());
        startActivity(i);
    }

    private boolean ubicaciones_existe(){
        listaUbicaciones.clear();

        if(!crud.buscar().equalsIgnoreCase("")){
            String [] cargarDatos=crud.buscar().split("/");
            for (int i = 0; i < cargarDatos.length; i++) {
                listaUbicaciones.add(cargarDatos[i]);
            }
            return true;
        }
        return false;
    }

    private boolean idConte_existe(){

        String Ubicacion = spinnerUbicacion.getSelectedItem().toString().trim();

        if(!crud.buscarIdConte(Ubicacion, color).equalsIgnoreCase("")){
            String [] cargarDatos=crud.buscarIdConte(Ubicacion, color).split("/");
            for (int i = 0; i < cargarDatos.length; i++) {
                String datosConte[]=cargarDatos[i].split("<br>");
                idConte = datosConte[0];
                contenidoConte = datosConte[1];
                capacidadConte = datosConte[2];
            }
            Log.d("idConte", idConte);
            int capacidad = Integer.parseInt(capacidadConte.trim());
            int contenido = Integer.parseInt(contenidoConte.trim());
            if(contenido+peso <= capacidad){
                return true;
            }
        }
        return false;
    }

    //AsyncTask para buscar las ubicaciones
    class BuscarUbicacion extends AsyncTask<String,String,String> {

        private Activity context;

        BuscarUbicacion(Activity context){
            this.context=context;
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            if(ubicaciones_existe())
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        List <String> opcionesUbicacion;
                        opcionesUbicacion = new ArrayList<String>();
                        opcionesUbicacion.add("Seleccione una ubicación");
                        for (int i = 0; i<listaUbicaciones.size(); i++){
                            Log.d("rol", listaUbicaciones.get(i).trim());
                            opcionesUbicacion.add(listaUbicaciones.get(i).trim());
                        }
                        ArrayAdapter<String> adapterC = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, opcionesUbicacion);
                        spinnerUbicacion.setAdapter(adapterC);
                    }
                });
            else
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(getActivity(), "No hay ubicaciones",
                                Toast.LENGTH_LONG).show();
                    }
                });
            return null;
        }
    }

    //AsyncTask para Buscar id del contenedor
    class BuscarIdConte extends AsyncTask<String,String,String>{

        private Activity context;

        BuscarIdConte(Activity context){
            this.context=context;
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            if(idConte_existe())
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        new HomeFragment.Insertar(getActivity()).execute();
                        new HomeFragment.EditarConte(getActivity()).execute();
                    }
                });
            else
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(getActivity(), "Lo siento es posible que no haya un contenedor para este tipo de residuos en esta ubicación o se encuentre lleno",
                                Toast.LENGTH_LONG).show();
                    }
                });
            return null;
        }
    }

    //AsyncTask para insertar Residuo
    class Insertar extends AsyncTask<String,String,String> {

        private Activity context;
        String Material = spinnerMaterial.getSelectedItem().toString().trim();
        String Liquido = spinnerLiquido.getSelectedItem().toString().trim();
        String Peso = String.valueOf(peso);

        Insertar(Activity context){
            this.context=context;
        }
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            if(crud.insertar(Material, Liquido, idConte, user, Peso))
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        spinnerMaterial.setSelection(0);
                        spinnerUbicacion.setSelection(0);
                        spinnerLiquido.setSelection(0);
                        spinnerPeso.setSelection(0);
                        Toast.makeText(context, "Por favor bote el residuo en el contenedor de color "
                                + color, Toast.LENGTH_LONG).show();
                        color="";
                    }
                });
            else
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(context, "Residuo no insertado con éxito", Toast.LENGTH_LONG).show();
                    }
                });
            return null;
        }
    }

    //AsyncTask para Actualizar el contenido del contenedor
    class EditarConte extends AsyncTask<String,String,String>{

        private Activity context;
        int contenido = Integer.parseInt(contenidoConte.trim()) + peso;
        String Contenido= String.valueOf(contenido);

        EditarConte(Activity context){
            this.context=context;
        }
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            if(crud.editarConte(idConte, Contenido))
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                    }
                });
            else
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(context, "ERROR",
                                Toast.LENGTH_LONG).show();
                    }
                });
            return null;
        }
    }

}
