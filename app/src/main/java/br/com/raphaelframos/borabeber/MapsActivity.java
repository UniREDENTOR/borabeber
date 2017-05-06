package br.com.raphaelframos.borabeber;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.raphaelframos.borabeber.fragments.BarDialogFragment;
import br.com.raphaelframos.borabeber.model.Bar;
import br.com.raphaelframos.borabeber.model.Bares;
import br.com.raphaelframos.borabeber.model.Bebida;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 100000 * 1000000;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private DatabaseReference mDatabase;
    private Location location;


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setNumUpdates(1);
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
        Toolbar tlb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tlb);
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
                    Log.v("Bares", "Teste " + bares.getBares().get(0).getBebidas().get(0).getNome() + " tamanho " + bares.getBares().size());
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
        try{
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(bar.getPosition()).title(bar.getNome());

            Marker marker = mMap.addMarker(markerOptions);
            marker.setTag(bar);
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Bar bar = (Bar) marker.getTag();
                    if(bar != null) {
                        BarDialogFragment barDialogFragment = new BarDialogFragment();
                        barDialogFragment.setBar(bar);
                        barDialogFragment.setLocation(location);
                        barDialogFragment.show(getSupportFragmentManager(), "barFragment");
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        mostraMinhaLocalizacao();

    }

    public void mostraMinhaLocalizacao(){
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_filtro:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
