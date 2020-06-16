package com.example.gestinresiduos.crud;

import com.example.gestinresiduos.Ruta;

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

public class CRUDConte {

    //Inserta los datos del Contenedor en el servidor.
    public boolean insertar(String capacidad, String color, String contenido, String ubicacion, String latitud, String longitud){
        HttpClient httpclient;
        List<NameValuePair> nameValuePairs;
        HttpPost httppost;
        httpclient=new DefaultHttpClient();
        httppost= new HttpPost(Ruta.URL + "contenedores/insertar_conte.php"); // Url del Servidor
        //Añadimos nuestros datos
        nameValuePairs = new ArrayList<NameValuePair>(6);
        nameValuePairs.add(new BasicNameValuePair("capacidad",capacidad.trim()));
        nameValuePairs.add(new BasicNameValuePair("color",color.trim()));
        nameValuePairs.add(new BasicNameValuePair("contenido",contenido.trim()));
        nameValuePairs.add(new BasicNameValuePair("ubicacion",ubicacion.trim()));
        nameValuePairs.add(new BasicNameValuePair("latitud",latitud.trim()));
        nameValuePairs.add(new BasicNameValuePair("longitud",longitud.trim()));

        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpclient.execute(httppost);
            return true;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    //Consulta el usuario en el servidor.
    public String buscar(String id, String ubicacion){
        String resquest="";
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        httpPost = new HttpPost(Ruta.URL + "contenedores/encontrar_conte.php");

        nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("id",id.trim()));
        nameValuePairs.add(new BasicNameValuePair("ubicacion",ubicacion.trim()));

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

    //Edita los datos de nuestro servidor
    public boolean editar(String id, String capacidad, String color, String contenido, String ubicacion, String latitud, String longitud){
        HttpClient httpclient;
        List<NameValuePair> nameValuePairs;
        HttpPost httppost;
        httpclient=new DefaultHttpClient();
        httppost= new HttpPost(Ruta.URL + "contenedores/editar_conte.php"); // Url del Servidor
        //Añadimos nuestros datos
        nameValuePairs = new ArrayList<NameValuePair>(7);
        nameValuePairs.add(new BasicNameValuePair("id",id.trim()));
        nameValuePairs.add(new BasicNameValuePair("capacidad",capacidad.trim()));
        nameValuePairs.add(new BasicNameValuePair("color",color.trim()));
        nameValuePairs.add(new BasicNameValuePair("contenido",contenido.trim()));
        nameValuePairs.add(new BasicNameValuePair("ubicacion",ubicacion.trim()));
        nameValuePairs.add(new BasicNameValuePair("latitud",latitud.trim()));
        nameValuePairs.add(new BasicNameValuePair("longitud",longitud.trim()));

        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpclient.execute(httppost);
            return true;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    //Eliminamos un contenedor del servidor
    public boolean eliminar(String id){
        HttpClient httpclient;
        List<NameValuePair> nameValuePairs;
        HttpPost httppost;
        httpclient=new DefaultHttpClient();
        httppost= new HttpPost(Ruta.URL + "contenedores/eliminar_conte.php"); // Url del Servidor
        //Añadimos nuestros datos
        nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("id",id.trim()));
        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpclient.execute(httppost);
            return true;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

}
