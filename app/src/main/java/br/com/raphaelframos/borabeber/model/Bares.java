package br.com.raphaelframos.borabeber.model;

import java.util.ArrayList;

/**
 * Created by raphaelramos on 28/04/17.
 */

public class Bares {

    private ArrayList<Bar> bares;
    private String child;

    public Bares(){
        this.child = "Bares";
    }

    public ArrayList<Bar> getBares() {
        return bares;
    }

    public void setBares(ArrayList<Bar> bares) {
        this.bares = bares;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }
}
