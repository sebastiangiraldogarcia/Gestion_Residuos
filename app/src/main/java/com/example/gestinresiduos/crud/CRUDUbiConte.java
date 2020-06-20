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

public class CRUDUbiConte {

    //Consulta la LatLng de un contenedor.
    public String LatLng(String ubicacion, String latitud, String longitud){
        String resquest="";
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        httpPost = new HttpPost(Ruta.URL + "contenedores/LatLng_conte.php");

        nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair("ubicacion",ubicacion.trim()));
        nameValuePairs.add(new BasicNameValuePair("latitud",latitud.trim()));
        nameValuePairs.add(new BasicNameValuePair("longitud",longitud.trim()));

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

}
