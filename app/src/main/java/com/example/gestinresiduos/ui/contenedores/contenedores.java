package com.example.gestinresiduos.ui.contenedores;

import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gestinresiduos.R;
import com.example.gestinresiduos.clases.Contenedores;
import com.example.gestinresiduos.crud.CRUDConte;


import java.util.ArrayList;
import java.util.List;

public class contenedores extends Fragment {

    private ContenedoresViewModel mViewModel;

    private EditText id;
    private Spinner spinnerColor;
    private EditText capacidad;
    private EditText contenido;
    private EditText ubicacion;

    private List<Contenedores> listaConte;

    private Button insertar;
    private Button buscar;
    private Button editar;
    private Button eliminar;
    private Button anterior;
    private Button siguiente;
    private CRUDConte crud;

    private int posicion=0;

    public static contenedores newInstance() {
        return new contenedores();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contenedores_fragment, container, false);

        id = (EditText) view.findViewById(R.id.id);
        capacidad = (EditText) view.findViewById(R.id.capacidad);
        contenido = (EditText) view.findViewById(R.id.contenido);
        ubicacion = (EditText) view.findViewById(R.id.etUbicacion);
        spinnerColor = (Spinner) view.findViewById(R.id.spinnerColor);

        listaConte=new ArrayList<Contenedores>();

        crud = new CRUDConte();

        String []opcionesColor = {"Seleccione un Color","Amarillo", "Azul", "Verde", "Marron", "Gris"};
        ArrayAdapter<String> adapterC = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, opcionesColor);
        spinnerColor.setAdapter(adapterC);

        //Insertamos un usuario.
        insertar=(Button) view.findViewById(R.id.insertar);

        insertar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!capacidad.getText().toString().trim().equalsIgnoreCase("")&&
                        !spinnerColor.getSelectedItem().toString().trim().equalsIgnoreCase("Seleccione un Color")&&
                        !contenido.getText().toString().trim().equalsIgnoreCase("")&&
                        !ubicacion.getText().toString().trim().equalsIgnoreCase(""))

                    new contenedores.Insertar(getActivity()).execute();

                else
                    Toast.makeText(getActivity(), "Asegurese de que todos los campos tengan información", Toast.LENGTH_LONG).show();
            }

        });

        //Buscamos un contenedor
        buscar=(Button) view.findViewById(R.id.buscar);

        buscar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!ubicacion.getText().toString().trim().equalsIgnoreCase("")||
                        !id.getText().toString().trim().equalsIgnoreCase(""))
                    new Buscar(getActivity()).execute();

                else
                    Toast.makeText(getActivity(), "Asegurese de escrbir la ubicacion o el id del contenedor a buscar",
                            Toast.LENGTH_LONG).show();
            }

        });

        //Se mueve por nuestro ArrayList mostrando el contenedor posterior.
        siguiente=(Button) view.findViewById(R.id.siguiente);

        siguiente.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!listaConte.isEmpty()){
                    if(posicion>=listaConte.size()-1){
                        posicion=listaConte.size()-1;
                        mostrarConte(posicion);
                    }else{
                        posicion++;

                        mostrarConte(posicion);
                    }
                }
            }

        });
        //Se mueve por nuestro ArrayList mostrando el contenedor anterior
        anterior=(Button) view.findViewById(R.id.anterior);

        anterior.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!listaConte.isEmpty()){
                    if(posicion<=0){
                        posicion=0;
                        mostrarConte(posicion);
                    }
                    else{
                        posicion--;
                        mostrarConte(posicion);
                    }
                }
            }
        });

        //Editamos un contenedor
        editar=(Button) view.findViewById(R.id.editar);

        editar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!capacidad.getText().toString().trim().equalsIgnoreCase("")&&
                        !spinnerColor.getSelectedItem().toString().trim().equalsIgnoreCase("Seleccione un Color")&&
                        !contenido.getText().toString().trim().equalsIgnoreCase("")&&
                        !ubicacion.getText().toString().trim().equalsIgnoreCase("")){
                    new Traer_conte(getActivity()).execute();
                }else{
                    Toast.makeText(getActivity(), "Asegurese de que todos los campos tengan información",
                            Toast.LENGTH_LONG).show();
                }
            }

        });

        //Eliminamos un contenedor
        eliminar=(Button) view.findViewById(R.id.eliminar);

        eliminar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!id.getText().toString().trim().equalsIgnoreCase("")){

                    new contenedores.Eliminar(getActivity()).execute();

                }else
                    Toast.makeText(getActivity(), "Asegurese de escrbir el id del contenedor a eliminar",
                            Toast.LENGTH_LONG).show();
            }

        });


        return view;
    }

    //Muestra la persona almacenada como objeto en nuestro ArrayList
    private void mostrarConte(final int posicion){
        getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Contenedores conte=listaConte.get(posicion);
                id.setText(conte.getId());
                capacidad.setText(conte.getCapacidad());
                contenido.setText(conte.getContenido());
                ubicacion.setText(conte.getUbicacion());
                if(conte.getColor().trim().equals("Amarillo")){
                    spinnerColor.setSelection(1);
                }else if(conte.getColor().trim().equals("Azul")){
                    spinnerColor.setSelection(2);
                }else if(conte.getColor().trim().equals("Verde")){
                    spinnerColor.setSelection(3);
                }else if(conte.getColor().trim().equals("Marron")){
                    spinnerColor.setSelection(4);
                }else if(conte.getColor().trim().equals("Gris")){
                    spinnerColor.setSelection(5);
                }
            }
        });
    }

    private boolean conte_existe(){
        listaConte.clear();
        String Id = id.getText().toString().trim();
        String Ubicacion = ubicacion.getText().toString().trim();

        if(!crud.buscar(Id, Ubicacion).equalsIgnoreCase("")){
            String [] cargarDatos=crud.buscar(Id, Ubicacion).split("/");
            for (int i = 0; i < cargarDatos.length; i++) {
                String datosConte[]=cargarDatos[i].split("<br>");
                Contenedores conte=new Contenedores();
                conte.setId(datosConte[0]);
                conte.setColor(datosConte[1]);
                conte.setCapacidad(datosConte[2]);
                conte.setContenido(datosConte[3]);
                conte.setUbicacion(datosConte[4]);
                listaConte.add(conte);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ContenedoresViewModel.class);
        // TODO: Use the ViewModel
    }

    //AsyncTask para insertar Contenedor
    class Insertar extends AsyncTask<String,String,String> {

        private Activity context;
        String Capacidad = capacidad.getText().toString().trim();
        String Color = spinnerColor.getSelectedItem().toString().trim();
        String Contenido = contenido.getText().toString().trim();
        String Ubicacion = ubicacion.getText().toString().trim();

        Insertar(Activity context){
            this.context=context;
        }
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            if(crud.insertar(Capacidad, Color, Contenido, Ubicacion))
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        capacidad.setText("");
                        spinnerColor.setSelection(0);
                        contenido.setText("");
                        ubicacion.setText("");
                        Toast.makeText(context, "Contenedor insertado con éxito", Toast.LENGTH_LONG).show();
                    }
                });
            else
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(context, "Contenedor no insertado con éxito", Toast.LENGTH_LONG).show();
                    }
                });
            return null;
        }
    }

    //AsyncTask para buscar Contenedor
    class Buscar extends AsyncTask<String,String,String> {

        private Activity context;

        Buscar(Activity context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            if (conte_existe())
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        mostrarConte(posicion);
                        Toast.makeText(getActivity(), "Se encontraron " + String.valueOf(listaConte.size()) + " contenedores",
                                Toast.LENGTH_LONG).show();
                    }
                });
            else
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(getActivity(), "No se encontraron contenedores con este id o ubicación",
                                Toast.LENGTH_LONG).show();
                    }
                });
            return null;
        }
    }

    //AsyncTask para Actualizar Contenedores
    class Traer_conte extends AsyncTask<String, String, String> {

        private Activity context;

        Traer_conte(Activity context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            if (conte_existe())
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Contenedores conte = new Contenedores();
                        conte = listaConte.get(0);
                        if (conte.getId().trim().equals(id.getText().toString().trim())) {
                            new Editar(getActivity()).execute();
                        } else {
                            Toast.makeText(getActivity(), "No existe un contenedor con este id",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            else
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(getActivity(), "No existe un contenedor con este id",
                                Toast.LENGTH_LONG).show();
                    }
                });
            return null;
        }
    }

    //AsyncTask para Actualizar Personas
    class Editar extends AsyncTask<String, String, String> {

        private Activity context;
        String Id = id.getText().toString().trim();
        String Capacidad = capacidad.getText().toString().trim();
        String Color = spinnerColor.getSelectedItem().toString().trim();
        String Contenido = contenido.getText().toString().trim();
        String Ubicacion = ubicacion.getText().toString().trim();

        Editar(Activity context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            if (crud.editar(Id, Capacidad, Color, Contenido, Ubicacion))
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        id.setText("");
                        capacidad.setText("");
                        spinnerColor.setSelection(0);
                        contenido.setText("");
                        ubicacion.setText("");
                        Toast.makeText(context, "Contenedor editado con éxito", Toast.LENGTH_LONG).show();
                    }
                });
            else
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(context, "Contenedor no editado con éxito",
                                Toast.LENGTH_LONG).show();
                    }
                });
            return null;
        }
    }

    class Eliminar extends AsyncTask<String, String, String> {

        private Activity context;
        String Id = id.getText().toString().trim();

        Eliminar(Activity context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            if (crud.eliminar(Id))
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        id.setText("");
                        capacidad.setText("");
                        spinnerColor.setSelection(0);
                        contenido.setText("");
                        ubicacion.setText("");
                        Toast.makeText(context, "Contenedor eliminado con éxito", Toast.LENGTH_LONG).show();
                    }
                });
            else
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(context, "Contenedor no eliminado con éxito", Toast.LENGTH_LONG).show();
                    }
                });
            return null;
        }
    }

}
