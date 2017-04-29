package br.com.raphaelframos.borabeber;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.raphaelframos.borabeber.model.Bar;
import br.com.raphaelframos.borabeber.model.Bares;
import br.com.raphaelframos.borabeber.model.Bebida;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 10000 * 1000;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private DatabaseReference mDatabase;


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        criaBares();

    }

    private void criaBares() {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Criando o bar
        Bar bar = new Bar();
        bar.setDescricao("Melhor bar da cidade");
        bar.setEndereco("Coronel Pimenta, 1, Centro");
        bar.setNome("Boemia");
        bar.setPromocao("2 Skols por 10");
        bar.setTipo("Bar");
        bar.setLatitude(-21.2036102);
        bar.setLongitude(-41.8861522);
        //Criando as bebidas
        ArrayList<Bebida> bebidas = new ArrayList<>();
        Bebida bebida = new Bebida();
        bebida.setNome("Skol");
        bebida.setValor(6.0);
        bebidas.add(bebida);

        Bebida bebida2 = new Bebida();
        bebida2.setValor(5.0);
        bebida2.setNome("Bavaria");
        //Adicionando as bebidas na lista
        bebidas.add(bebida);
        bebidas.add(bebida2);

        //Adicionando bebidas no bar
        bar.setBebidas(bebidas);


        Bar bar2 = new Bar();
        bar2.setDescricao("Outro bar da cidade");
        bar2.setEndereco("Outra rua, 2, Centro");
        bar2.setNome("QualQual");
        bar2.setPromocao("");
        bar2.setTipo("Bar");
        bar2.setLatitude(-21.2137120);
        bar2.setLongitude(-41.8962552);
        ArrayList<Bebida> bebidas2 = new ArrayList<>();
        Bebida bebida3 = new Bebida();
        bebida3.setNome("Brahma");
        bebida3.setValor(6.0);
        bebidas2.add(bebida);

        Bebida bebida4 = new Bebida();
        bebida4.setValor(5.0);
        bebida4.setNome("Antartica");
        bebidas2.add(bebida3);
        bebidas2.add(bebida4);

        //Adicionando bebidas no bar
        bar2.setBebidas(bebidas2);















        Bares bares = new Bares();
        final ArrayList<Bar> bars = new ArrayList<>();
        bars.add(bar);
        bars.add(bar2);
        bares.setBares(bars);
        bares.setChild("bares");
        mDatabase.child(bares.getChild()).setValue(bares);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("Teste ", "Resultado " + dataSnapshot.child("bares"));
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Bares bares = data.getValue(Bares.class);
                    Log.v("Bares", "Teste " + bares + " tamanho " + bares.getBares().size());
                    for(Bar bar : bares.getBares()){
                        mostraNoMapa(bar);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void mostraNoMapa(Bar bar) {
        mMap.addMarker(new MarkerOptions().position(bar.getPosition()).title(bar.getNome()));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(sydney).title("Eu").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

        Log.d(TAG, "Location update started ..............: " + pendingResult);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }
}
