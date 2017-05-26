package br.com.raphaelframos.borabeber;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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
import br.com.raphaelframos.borabeber.fragments.FiltroDialogFragment;
import br.com.raphaelframos.borabeber.model.Bar;
import br.com.raphaelframos.borabeber.model.Bares;
import br.com.raphaelframos.borabeber.model.Bebida;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, FiltroDialogFragment.AtualizaMapa {

    private GoogleMap mMap;
    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 100000 * 1000000;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private DatabaseReference mDatabase;
    private Location location;
    private ArrayList<Bar> baresEncontrados;
    private ProgressDialog progressDialog;
    private boolean foiLocalizado = false;


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
        if(!isLocationEnabled(this)){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Localização desabilitada. Habilite para continuar");
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //this will navigate user to the device location settings screen
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            AlertDialog alert = dialog.create();
            alert.show();
        }

    }

    private void inicia() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Buscando bares...");
        progressDialog.show();
        criaBares();
    }

    private void criaBares() {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        /*
        //Criando o bar
        Bar bar = new Bar();
        bar.setDescricao("Digite a descricao do bar");
        bar.setEndereco("Coronel Pimenta");
        bar.setNome("Boemia");
        bar.setPromocao("Digite a promocao");
        bar.setHorario("Digite o horario de funcionamento");
        bar.setTipo("Digite o tipo");
        //Escreva a latitude igual o exemplo abaixo
        bar.setLatitude(-21.2036102);
        //Escreva a longitude igual o exemplo abaixo
        bar.setLongitude(-41.8861522);


        Bar barLeandra = new Bar();
        barLeandra.setHorario("19h-0h");
        barLeandra.setNome("Redentor");
        barLeandra.setTipo("Restaurante");

        ArrayList<Bebida> bebidasDaLeandra = new ArrayList<>();
        Bebida bebidaNumeroUm = new Bebida();
        bebidaNumeroUm.setNome("Skol");
        bebidaNumeroUm.setValor(6.5);
        bebidaNumeroUm.setTipo("Litrao");
        bebidasDaLeandra.add(bebidaNumeroUm);


        barLeandra.setBebidas(bebidasDaLeandra);


        //Criando as bebidas
        ArrayList<Bebida> bebidas = new ArrayList<>();


        Bebida bebida = new Bebida();
        bebida.setNome("Digite o nome da bebida");
        //Coloque o valor igual o exemplo abaixo. Com . ao inves de ,
        bebida.setValor(6.0);
        bebidas.add(bebida);

        Bebida bebida2 = new Bebida();
        bebida2.setNome("Digite o nome da bebida");
        //Coloque o valor igual o exemplo abaixo. Com . ao inves de ,
        bebida2.setValor(4.0);




        //Adicionando as bebidas na lista
        bebidas.add(bebida);
        bebidas.add(bebida2);

        //Adicionando bebidas no bar
        bar.setBebidas(bebidas);


        Bar bar2 = new Bar();
        bar2.setDescricao("Digite a descricao");
        bar2.setEndereco("Digite o endereco");
        bar2.setNome("Digite o nome");
        bar2.setPromocao("Digite a promocao");
        bar.setHorario("Digite o horario");
        bar2.setTipo("Digite o tipo");
        //Latitude e longitude igual ao de cima
        bar2.setLatitude(-21.2137120);
        bar2.setLongitude(-41.8962552);
        ArrayList<Bebida> bebidas2 = new ArrayList<>();
        Bebida bebida3 = new Bebida();
        bebida3.setNome("Nome da bebida");
        bebida3.setValor(6.0);
        bebidas2.add(bebida);

        Bebida bebida4 = new Bebida();
        bebida4.setValor(5.0);
        bebida4.setNome("Nome da bebida");
        bebidas2.add(bebida3);
        bebidas2.add(bebida4);

        //Adicionando bebidas no bar
        bar2.setBebidas(bebidas2);















        Bares bares = new Bares();
        final ArrayList<Bar> bars = new ArrayList<>();
        bars.add(bar);
        bars.add(bar2);
        bars.add(barLeandra);
        bares.setBares(bars);






        mDatabase.child(bares.getChild()).setValue(bares);
        */
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Bares baresCadastrados = data.getValue(Bares.class);
                    mostraBares(baresCadastrados.getBares());
                    baresEncontrados = baresCadastrados.getBares();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void mostraBares(ArrayList<Bar> bares) {

        for(Bar bar : bares){
            mostraNoMapa(bar);
        }
        progressDialog.dismiss();
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
            progressDialog.dismiss();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        Log.v("", "Minha localizacao " + location);
        mostraMinhaLocalizacao();
    }

    public void mostraMinhaLocalizacao(){
        try {
            LatLng minhaLocalizacao = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(minhaLocalizacao).title("Eu").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(minhaLocalizacao, 16));
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }catch (Exception e){
            e.printStackTrace();
        }
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
                FiltroDialogFragment filtroDialogFragment = new FiltroDialogFragment();
                filtroDialogFragment.setAtualizaCallback(this);
                filtroDialogFragment.show(getSupportFragmentManager(), "filtro");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void atualiza(int valor, int distancia, String tipo) {
        atualizaMapa(valor, distancia, tipo);
    }

    @Override
    public void limpa() {
        populaMapa(baresEncontrados);
    }

    public void atualizaMapa(int valor, int distancia, String tipo){
        ArrayList<Bar> baresFiltrados = new ArrayList<>();
        boolean checkTipo = !tipo.equalsIgnoreCase("Todos");
        boolean checkDistancia = distancia != 0;
        boolean checkValor = valor != 0;

        for(Bar bar : baresEncontrados){
            if(checkDistancia && checkTipo && checkValor){
                if(bar.getTipo().equalsIgnoreCase(tipo) && (bar.getDistanciaInteira(location) <= distancia)) {
                    for (Bebida bebida : bar.getBebidas()) {
                        if (bebida.getValor() < valor) {
                            baresFiltrados.add(bar);
                            break;
                        }
                    }
                }
            }else if(checkDistancia && checkTipo){
                if(bar.getTipo().equalsIgnoreCase(tipo) && (bar.getDistanciaInteira(location) <= distancia)) {
                    baresFiltrados.add(bar);
                }
            }else if(checkDistancia && checkValor){
                if(bar.getDistanciaInteira(location) <= distancia) {
                    for (Bebida bebida : bar.getBebidas()) {
                        if (bebida.getValor() < valor) {
                            baresFiltrados.add(bar);
                            break;
                        }
                    }
                }
            }else if(checkTipo && checkValor){
                if(bar.getTipo().equalsIgnoreCase(tipo)) {
                    for (Bebida bebida : bar.getBebidas()) {
                        if (bebida.getValor() < valor) {
                            baresFiltrados.add(bar);
                            break;
                        }
                    }
                }
            }else if(checkDistancia){
                if(bar.getDistanciaInteira(location) <= distancia) {
                    baresFiltrados.add(bar);
                }
            }else if(checkTipo){
                if(bar.getTipo().equalsIgnoreCase(tipo)) {
                    baresFiltrados.add(bar);
                }
            }else if(checkValor) {
                for (Bebida bebida : bar.getBebidas()) {
                    if (bebida.getValor() < valor) {
                        baresFiltrados.add(bar);
                        break;
                    }
                }
            }

        }
        populaMapa(baresFiltrados);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.v("", "Permissao " + requestCode + " e " + permissions + " grant " + grantResults);
        switch (requestCode) {
            case 123: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    inicia();
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setTitle("Atenção");
                    alert.setMessage("Você precisa liberar as permissões de localização nas configurações.");
                    alert.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    alert.show();
                }
            }
        }

    }

    private void populaMapa(ArrayList<Bar> baresFiltrados) {
        progressDialog.show();
        mMap.clear();
        mostraMinhaLocalizacao();
        mostraBares(baresFiltrados);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            inicia();
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }
}
