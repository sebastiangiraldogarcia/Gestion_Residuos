package com.example.gestinresiduos.ui.map;

import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.graphics.BitmapFactory;

import com.example.gestinresiduos.clases.Contenedores;
import com.example.gestinresiduos.clases.ubiConte;
import com.example.gestinresiduos.crud.CRUDUbiConte;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;


import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.gestinresiduos.R;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

/**
 * Display {@link SymbolLayer} icons on the map.
 */

public class Mapa extends Fragment implements OnMapReadyCallback{

    private MapaViewModel mViewModel;

    private MapView mapView;

    public static Mapa newInstance() {
        return new Mapa();
    }

    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";

    private EditText ubicacion;
    private EditText latitud;
    private EditText longitud;
    private List<ubiConte> listaConte;
    private CRUDUbiConte crud;

    private int posicion=0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mapa_fragment, container, false);

        mapView = view.findViewById(R.id.map1);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments.

                    }
                    public void onMapReady (Mapbox mapbox) {
                        //start

                        new LatLng(getActivity()).execute();

                        List<Feature> symbolLayerIconFeatureList = new ArrayList<>();
                        symbolLayerIconFeatureList.add(Feature.fromGeometry(
                                Point.fromLngLat(-57.225365, -33.213144)));

                        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41")

                                // Add the SymbolLayer icon image to the map style
                                .withImage(ICON_ID, BitmapFactory.decodeResource(
                                getActivity().getResources(), R.drawable.mapbox_marker_icon_default))

                                // Adding a GeoJson source for the SymbolLayer icons.
                                .withSource(new GeoJsonSource(SOURCE_ID,
                                        FeatureCollection.fromFeatures(symbolLayerIconFeatureList)))


                                // Adding the actual SymbolLayer to the map style. An offset is added that the bottom of the red
                                // marker icon gets fixed to the coordinate, rather than the middle of the icon being fixed to
                                // the coordinate point. This is offset is not always needed and is dependent on the image
                                // that you use for the SymbolLayer icon.
                                .withLayer(new SymbolLayer(LAYER_ID, SOURCE_ID)
                                        .withProperties(iconImage(ICON_ID),
                                                iconAllowOverlap(true),
                                                iconOffset(new Float[] {0f, -9f}))
                                ), new Style.OnStyleLoaded() {
                            @Override
                            public void onStyleLoaded(@NonNull Style style) {
                            // Map is set up and the style has loaded. Now you can add additional data or make other map adjustments.
                            }
                        });                        
                        //end
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        
    }

    //AsyncTask para buscar Contenedor
    class LatLng extends AsyncTask<String,String,String> {

        private Activity context;

        LatLng(Activity context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            if (conte())
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        conte(posicion);
                    }
                });
            else
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            return null;
        }
    }
}
