package com.example.gestinresiduos.ui.slideshow;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.gestinresiduos.R;
import com.example.gestinresiduos.clases.Usuarios;
import com.example.gestinresiduos.crud.CRUDUser;

import java.util.ArrayList;
import java.util.List;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;


    private EditText user;
    private EditText pass;
    private Spinner spinnerRol;

    private List<Usuarios> listaUsers;

    private Button insertar;
    private Button buscar;
    private Button editar;
    private Button eliminar;
    private CRUDUser crud;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View view = inflater.inflate(R.layout.fragment_slideshow, container, false);

        user = (EditText) view.findViewById(R.id.user);
        pass = (EditText) view.findViewById(R.id.pass);
        spinnerRol = (Spinner) view.findViewById(R.id.spinnerRol);

        listaUsers=new ArrayList<Usuarios>();

        crud = new CRUDUser();

        String []opcionesRol = {"Seleccione un rol","Administrador", "Usuario"};
        ArrayAdapter<String> adapterC = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, opcionesRol);
        spinnerRol.setAdapter(adapterC);

        //Insertamos un usuario.
        insertar=(Button)view.findViewById(R.id.insertar);

        insertar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!user.getText().toString().trim().equalsIgnoreCase("")&&
                        !pass.getText().toString().trim().equalsIgnoreCase("")&&
                        !spinnerRol.getSelectedItem().toString().trim().equalsIgnoreCase("Seleccione un rol"))

                    new Insertar(getActivity()).execute();

                else
                    Toast.makeText(getActivity(), "Asegurese de que todos los campos tengan información",
                            Toast.LENGTH_LONG).show();
            }

        });

        //Buscamos un usuario
        buscar=(Button)view.findViewById(R.id.buscar);

        buscar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!user.getText().toString().trim().equalsIgnoreCase(""))
                    new Buscar (getActivity()).execute();

                else
                    Toast.makeText(getActivity(), "Asegurese de escrbir el nombre del usuario a buscar",
                            Toast.LENGTH_LONG).show();

            }

        });

        //Editamos un usuario
        editar=(Button)view.findViewById(R.id.editar);

        editar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!user.getText().toString().trim().equalsIgnoreCase("")&&
                        !pass.getText().toString().trim().equalsIgnoreCase("")&&
                        !spinnerRol.getSelectedItem().toString().trim().equalsIgnoreCase("Seleccione un rol")){
                    new Traer_user(getActivity()).execute();
                }else{
                    Toast.makeText(getActivity(), "Asegurese de que todos los campos tengan información",
                            Toast.LENGTH_LONG).show();
                }
            }

        });

        //Eliminamos un usuario
        eliminar=(Button)view.findViewById(R.id.eliminar);

        eliminar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!user.getText().toString().trim().equalsIgnoreCase("")){

                    new Eliminar(getActivity()).execute();

                }else
                    Toast.makeText(getActivity(), "Asegurese de escrbir el nombre del usuario a eliminar",
                            Toast.LENGTH_LONG).show();
            }

        });

        return view;
    }

    private boolean user_existe(){
        listaUsers.clear();
        String usuario = user.getText().toString().trim();

        if(!crud.buscar(usuario).equalsIgnoreCase("")){
            String [] cargarDatos=crud.buscar(usuario).split("/");
            for (int i = 0; i < cargarDatos.length; i++) {
                String datosUsuarios[]=cargarDatos[i].split("<br>");
                Usuarios user=new Usuarios();
                user.setUsername(datosUsuarios[0]);
                user.setPassword(datosUsuarios[1]);
                user.setRol(datosUsuarios[2]);
                listaUsers.add(user);
            }
            return true;
        }
        return false;
    }

    //AsyncTask para insertar Usuario
    class Insertar extends AsyncTask<String,String,String> {

        private Activity context;
        String usuario = user.getText().toString().trim();
        String password = pass.getText().toString().trim();
        String rol = spinnerRol.getSelectedItem().toString().trim();

        Insertar(Activity context){
            this.context=context;
        }
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            if(crud.insertar(usuario, password, rol))
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        user.setText("");
                        pass.setText("");
                        spinnerRol.setSelection(0);
                        Toast.makeText(context, "Usuario insertado con éxito", Toast.LENGTH_LONG).show();
                    }
                });
            else
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(context, "Usuario no insertado con éxito", Toast.LENGTH_LONG).show();
                    }
                });
            return null;
        }
    }

    //AsyncTask para consultar usuario
    class Buscar extends AsyncTask<String,String,String> {

        private Activity context;

        Buscar(Activity context){
            this.context=context;
        }
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            if(user_existe())
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Usuarios usuario=new Usuarios();
                        usuario = listaUsers.get(0);
                        user.setText(usuario.getUsername());
                        pass.setText(usuario.getPassword());
                        if (usuario.getRol().equals("Administrador")){
                            spinnerRol.setSelection(1);
                        }else{
                            spinnerRol.setSelection(2);
                        }
                    }
                });
            else
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(context, "No existe un usuario con este nombre", Toast.LENGTH_LONG).show();
                        pass.setText("");
                        spinnerRol.setSelection(0);
                    }
                });
            return null;
        }
    }

    //AsyncTask para Actualizar Personas
    class Traer_user extends AsyncTask<String,String,String>{

        private Activity context;

        Traer_user(Activity context){
            this.context=context;
        }
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            if(user_existe())
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Usuarios usuario=new Usuarios();
                        usuario = listaUsers.get(0);
                        if(usuario.getUsername().trim().equals(user.getText().toString().trim())){
                            new Editar(getActivity()).execute();
                        }else{
                            Toast.makeText(getActivity(), "No existe un usuario con este nombre",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            else
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(getActivity(), "No existe un usuario con este nombre",
                                Toast.LENGTH_LONG).show();
                    }
                });
            return null;
        }
    }

    //AsyncTask para Actualizar Personas
    class Editar extends AsyncTask<String,String,String>{

        private Activity context;
        String usuario = user.getText().toString().trim();
        String password = pass.getText().toString().trim();
        String rol = spinnerRol.getSelectedItem().toString().trim();

        Editar(Activity context){
            this.context=context;
        }
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            if(crud.editar(usuario, password, rol))
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(context, "Usuario editado con éxito",
                                Toast.LENGTH_LONG).show();
                        user.setText("");
                        pass.setText("");
                        spinnerRol.setSelection(0);
                    }
                });
            else
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(context, "Usuario no editado con éxito",
                                Toast.LENGTH_LONG).show();
                    }
                });
            return null;
        }
    }

    class Eliminar extends AsyncTask<String,String,String>{

        private Activity context;
        String usuario = user.getText().toString().trim();

        Eliminar(Activity context){
            this.context=context;
        }
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            if(crud.eliminar(usuario))
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(context, "Usuario eliminado con éxito", Toast.LENGTH_LONG).show();
                        user.setText("");
                        pass.setText("");
                        spinnerRol.setSelection(0);
                    }
                });
            else
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(context, "Usuario no eliminado con éxito", Toast.LENGTH_LONG).show();
                    }
                });
            return null;
        }
    }
}
