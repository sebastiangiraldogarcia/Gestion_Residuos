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

import java.util.StringTokenizer;

import com.example.gestinresiduos.crud.CRUDResi;
import com.example.gestinresiduos.clases.IntentIntegrator;
import com.example.gestinresiduos.clases.IntentResult;


public class UsuariosQR extends AppCompatActivity implements View.OnClickListener {

    private Button btScan1, insertar, btEliminar1, btGuardar1;
    private EditText etUbicacion, etPeso1, etLiquido1, etMaterial1;
    private String color;
    private int peso;
    private CRUDResi crud;
    private String idConte;
    private String capacidadConte;
    private String contenidoConte;
    private String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios_q_r);

        btScan1 = findViewById(R.id.btScan);
        btScan1.setOnClickListener(this);

        //btEliminar1 = findViewById(R.id.btEliminar);
        //btEliminar1.setOnClickListener(this);

        insertar = findViewById(R.id.insertar);

        //btGuardar1 = findViewById(R.id.btGuardar);
        //btGuardar1.setOnClickListener(this);

        etUbicacion = findViewById(R.id.etUbicacion);

        etPeso1 = findViewById(R.id.etPeso);
        etLiquido1 = findViewById(R.id.etLiquido);
        etMaterial1 = findViewById(R.id.etMaterial);

        crud = new CRUDResi();
        user = getIntent().getStringExtra("user");


//Insertamos un residuo.
        insertar=(Button)findViewById(R.id.insertar);
        insertar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!etUbicacion.getText().toString().trim().equalsIgnoreCase("")&&
                        !etMaterial1.getText().toString().trim().equalsIgnoreCase("")&&
                        !etLiquido1.getText().toString().trim().equalsIgnoreCase("")&&
                        !etPeso1.getText().toString().trim().equalsIgnoreCase("")) {

                    if (etMaterial1.getText().toString().trim().equals("Plástico o aluminio")) {
                        color = "Amarillo";
                    } else if (etMaterial1.getText().toString().trim().equals("Cartón o papel")) {
                        color = "Azul";
                    } else if (etMaterial1.getText().toString().trim().equals("Vidrio o metal")) {
                        color = "Verde";
                    } else if (etMaterial1.getText().toString().trim().equals("Orgánico")) {
                        color = "Marron";
                    } else if (etMaterial1.getText().toString().trim().equals("Otro")) {
                        color = "Gris";
                    }

                    if (etPeso1.getText().toString().trim().equals("Alto")) {
                        peso = 30;
                    } else if (etPeso1.getText().toString().trim().equals("Medio")) {
                        peso = 20;
                    } else if (etPeso1.getText().toString().trim().equals("Bajo")) {
                        peso = 10;
                    }

                    if(!color.equalsIgnoreCase("")&& peso!=0){
                        Log.d("rol", color);
                        new UsuariosQR.BuscarIdConte(UsuariosQR.this).execute();
                    }

                }else {
                    Toast.makeText(UsuariosQR.this, "Asegurese de que todos los campos tengan información",
                            Toast.LENGTH_LONG).show();
                }
            }

        });

    }

    public void onActivityResult (int requestCode, int resultCode, Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {

            String scanContent = scanningResult.getContents();
            StringTokenizer token = new StringTokenizer(scanContent, "*");

            final String ubicacion = token.nextToken();
            final String material = token.nextToken();
            final String liquido = token.nextToken();
            final String peso = token.nextToken();


            etUbicacion.setText("" + ubicacion);
            etMaterial1.setText("" + material);
            etLiquido1.setText("" + liquido);
            etPeso1.setText("" + peso);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No se ha recibido datos del escaneo.", Toast.LENGTH_SHORT);
            toast.show();

        }
    }

    @Override
    public void onClick(View v){
        if (v.getId()==R.id.btScan){
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
    }

    private boolean idConte_existe(){

        String Ubicacion = etUbicacion.getText().toString().trim();

        if(!crud.buscarIdConte(Ubicacion, color).equalsIgnoreCase("")){
            String [] cargarDatos=crud.buscarIdConte(Ubicacion, color).split("/");
            for (int i = 0; i < cargarDatos.length; i++) {
                String datosConte[]=cargarDatos[i].split("<br>");
                idConte = datosConte[0];
                contenidoConte = datosConte[1];
                capacidadConte = datosConte[2];
            }
            Log.d("rol", contenidoConte);
            int capacidad = Integer.parseInt(capacidadConte.trim());
            int contenido = Integer.parseInt(contenidoConte.trim());
            if(contenido+peso <= capacidad){
                return true;
            }
        }
        return false;
    }

    //AsyncTask para Buscar id del contenedor
    class BuscarIdConte extends AsyncTask<String,String,String> {

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
                        new UsuariosQR.Insertar(UsuariosQR.this).execute();
                        new UsuariosQR.EditarConte(UsuariosQR.this).execute();

                        Log.d("rol", etMaterial1.getText().toString().trim());
                        Log.d("rol", etLiquido1.getText().toString().trim());
                        Log.d("rol", idConte);
                        Log.d("rol", user);
                        Log.d("rol", String.valueOf(peso));

                    }
                });
            else
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(UsuariosQR.this, "Lo siento es posible que no haya un contenedor para este tipo de residuos en esta ubicación o se encuentre lleno",
                                Toast.LENGTH_LONG).show();
                    }
                });
            return null;
        }
    }

    //AsyncTask para insertar Residuo
    class Insertar extends AsyncTask<String,String,String> {

        private Activity context;
        String Material = etMaterial1.getText().toString().trim();
        String Liquido = etLiquido1.getText().toString().trim();
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
                        etUbicacion.setText("");
                        etMaterial1.setText("");
                        etLiquido1.setText("");
                        etPeso1.setText("");
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

