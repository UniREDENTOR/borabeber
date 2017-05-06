package br.com.raphaelframos.borabeber.fragments;


import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import br.com.raphaelframos.borabeber.R;
import br.com.raphaelframos.borabeber.model.Bar;

/**
 * A simple {@link Fragment} subclass.
 */
public class BarDialogFragment extends DialogFragment {

    private Bar bar;
    private TextView textViewNome;
    private TextView textViewDescricao;
    private TextView textViewEndereco;
    private TextView textViewTipo;
    private TextView textViewPromocao;
    private TextView textViewBebidas;
    private TextView textViewDistancia;
    private Location minhaLocalizacao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bar_dialog, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textViewBebidas = (TextView) getView().findViewById(R.id.textViewBebidas);
        textViewDescricao = (TextView) getView().findViewById(R.id.textViewDescricao);
        textViewEndereco = (TextView) getView().findViewById(R.id.textViewEndereco);
        textViewTipo = (TextView) getView().findViewById(R.id.textViewTipo);
        textViewPromocao = (TextView) getView().findViewById(R.id.textViewPromocao);
        textViewNome = (TextView) getView().findViewById(R.id.textViewNome);
        textViewDistancia = (TextView) getView().findViewById(R.id.textViewDistancia);

        textViewNome.setText(bar.getNome());
        textViewDescricao.setText(bar.getDescricao());
        textViewPromocao.setText(bar.getPromocao());
        textViewTipo.setText(bar.getTipo());
        textViewEndereco.setText(bar.getEndereco());
        textViewBebidas.setText(bar.getBebidasDemonstracao());
        textViewDistancia.setText("Distância: " + bar.getDistancia(minhaLocalizacao) + "m");

        getDialog().setTitle("Estabelecimento");
    }

    public void setBar(Bar bar) {
        this.bar = bar;
    }

    public void setLocation(Location location){
        this.minhaLocalizacao = location;
    }
}
