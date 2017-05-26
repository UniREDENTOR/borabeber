package br.com.raphaelframos.borabeber.model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by raphaelramos on 28/04/17.
 */

public class Bar {

    private String descricao;
    private String nome;
    private String tipo;
    private Double latitude;
    private Double longitude;
    private String endereco;
    private String promocao;
    private String horario;
    private ArrayList<Bebida> bebidas;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getPromocao() {
        return promocao;
    }

    public void setPromocao(String promocao) {
        this.promocao = promocao;
    }

    public ArrayList<Bebida> getBebidas() {
        return bebidas;
    }

    public void setBebidas(ArrayList<Bebida> bebidas) {
        this.bebidas = bebidas;
    }

    public LatLng getPosition() {
        try{
            return new LatLng(getLatitude(), getLongitude());

        }catch (Exception e){
            return  new LatLng(0,0);
        }
    }

    public String getBebidasDemonstracao() {
        String resultado = "";
        for(Bebida bebida: bebidas){
               resultado += bebida.getDemonstracao();
            resultado += "\n";
        }
        return resultado;
    }

    public String getDistancia(Location distancia) {
        Location location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        DecimalFormat formato = new DecimalFormat("#.#");
        return formato.format(distancia.distanceTo(location));
    }

    public Integer getDistanciaInteira(Location distancia) {
        Location location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return (int) distancia.distanceTo(location);
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
