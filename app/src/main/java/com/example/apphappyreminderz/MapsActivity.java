package com.example.apphappyreminderz;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng tienda1 = new LatLng(-3.7511281, -73.2472613);
        LatLng tienda2 = new LatLng(-3.7456579, -73.2420543);
        LatLng tienda3 = new LatLng(-3.7482872, -73.2441525);
        LatLng tienda4 = new LatLng(40.8859, -79.934);
        LatLng tienda5 = new LatLng(-3.749365, -73.2444145);

        mMap.addMarker(new MarkerOptions().position(tienda1).title("La Tiendita de Ruth \uD83C\uDF81").snippet("La Tiendita de Ruth Iquitos, ofrece regalos y sorpresas para toda ocasión,\n" +
                "arreglos flores, adornos, peluches, joyas, carteras,tazas personalizadas, etc."));

        mMap.addMarker(new MarkerOptions().position(tienda2).title("Rosatel \uD83C\uDF81").snippet("Los mejores arreglos florales, cajas de rosas, tulipanes, peluches y variedad de\n" +
                "regalos."));
        mMap.addMarker(new MarkerOptions().position(tienda3).title("Floreria & Regalos Amaranthos \uD83C\uDF81").snippet("Venta de Arreglos Florales en Iquitos, flores al por mayor, peluches, globos,\n" +
                "chocolates, tarjetas, regalos, Bouquet de Novias, botoniers, centros de mesa, etc."));

        mMap.addMarker(new MarkerOptions().position(tienda4).title("Canela's Boutique y Regalos \uD83C\uDF81").snippet(" Iquitos, peluches de Amor y Amistad. Flores\n" +
                "Naturales. Chocolates. Lentes de sol. Adornos. Carteras. Perfumes. Globos."));

        mMap.addMarker(new MarkerOptions().position(tienda5).title("Loreto Importaciones SA \uD83C\uDF81").snippet("Loreto Importaciones S.A. de Iquitos, Perú. Teléfonos, direcciones y sucursales\n" +
                "de Ferreterías en Páginas Amarillas."));

        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tienda3));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
    }
}