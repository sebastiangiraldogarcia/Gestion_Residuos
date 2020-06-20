package com.example.gestinresiduos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gestinresiduos.clases.Usuarios;
import com.example.gestinresiduos.ui.gallery.GalleryFragment;
import com.example.gestinresiduos.ui.home.HomeFragment;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    private EditText user;
    private EditText pass;
    private Button enter;

    private List<Usuarios> listaUsers;

    Bundle bundle = new Bundle();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*Intent i = new Intent(this, MainActivity.class);
        startActivity(i);*/

        listaUsers=new ArrayList<Usuarios>();
        user=(EditText)findViewById(R.id.user);
        pass=(EditText)findViewById(R.id.pass);

        enter = findViewById(R.id.btn1);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!user.getText().toString().trim().equalsIgnoreCase("")&&
                        !pass.getText().toString().trim().equalsIgnoreCase(""))
                    new Consultar_user(Login.this).execute();

                else
                    Toast.makeText(Login.this, "Completar todos los campos", Toast.LENGTH_LONG).show();
            }
        });

        // set Fragmentclass Arguments
        HomeFragment fragobj = new HomeFragment();
        fragobj.setArguments(bundle);

    }


    //Consulta el usuario en el servidor.
    private String consultar_user(){
        String resquest="";
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        httpPost = new HttpPost(Ruta.URL + "usuarios/encontrar_user.php");

        nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("user",user.getText().toString().trim()));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            resquest = httpClient.execute(httpPost, responseHandler);
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        } catch (ClientProtocolException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return resquest;
    }

    private boolean user_existe(){
        listaUsers.clear();
        if(!consultar_user().equalsIgnoreCase("")){
            String [] cargarDatos=consultar_user().split("/");
            for (int i = 0; i < cargarDatos.length; i++) {
                String datosUsuarios[]=cargarDatos[i].split("<br>");
                Usuarios usuario=new Usuarios();
                usuario.setUsername(datosUsuarios[0]);
                usuario.setPassword(datosUsuarios[1]);
                usuario.setRol(datosUsuarios[2]);
                listaUsers.add(usuario);
                String password = usuario.getPassword().trim();
                if(password.equals(pass.getText().toString().trim())){
                    return true;
                }
            }
        }
        return false;
    }

    //AsyncTask para consultar usuario
    class Consultar_user extends AsyncTask<String,String,String> {

        private Activity context;

        Consultar_user(Activity context){
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
                        String rol = usuario.getRol();
                        Log.d("rol", rol);
                        if(rol.equals("Administrador")){
                            Intent i = new Intent(Login.this, MainActivity.class);
                            i.putExtra("rol",listaUsers.get(0).getRol().trim());
                            i.putExtra("user",listaUsers.get(0).getUsername().trim());
                            bundle.putString("user", "From Login");
                            startActivity(i);
                        }else{
                            Intent i = new Intent(Login.this, MainActivity.class);
                            i.putExtra("user", listaUsers.get(0).getUsername().trim());
                            bundle.putString("user", "From Login");
                            startActivity(i);
                        }

                    }
                });
            else
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(context, "Verifique su usario y contraseña", Toast.LENGTH_LONG).show();
                    }
                });
            return null;
        }
    }
}
