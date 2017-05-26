package br.com.raphaelframos.borabeber.model;

/**
 * Created by raphaelramos on 28/04/17.
 */

public class Bebida {

    private String nome;
    private Double valor;
    private String tipo;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getDemonstracao() {
        return nome + " - " + valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
