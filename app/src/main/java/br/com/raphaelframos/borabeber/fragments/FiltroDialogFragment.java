package br.com.raphaelframos.borabeber.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import br.com.raphaelframos.borabeber.R;

/**
 * Created by raphaelramos on 05/05/17.
 */

public class FiltroDialogFragment extends DialogFragment {

    private SeekBar seekBarValor;
    private SeekBar seekBarDistancia;
    private Spinner spinnerTipos;
    private Button buttonBuscar;
    private Button buttonLimpar;
    private TextView textViewValorCerveja;
    private TextView textViewValorDistancia;
    private AtualizaMapa atualizaMapa;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_filtro_dialog, container);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        seekBarDistancia = (SeekBar) getView().findViewById(R.id.seekDistancia);
        seekBarValor = (SeekBar) getView().findViewById(R.id.seekValor);
        spinnerTipos = (Spinner) getView().findViewById(R.id.spinnerTipos);
        textViewValorCerveja = (TextView) getView().findViewById(R.id.textViewValorCerveja);
        textViewValorDistancia = (TextView) getView().findViewById(R.id.textViewValorDistancia);
        buttonBuscar = (Button) getView().findViewById(R.id.buttonBuscar);
        buttonLimpar = (Button) getView().findViewById(R.id.buttonLimpar);

        seekBarDistancia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewValorDistancia.setText(progress + "m");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarValor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewValorCerveja.setText("R$ " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        buttonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int progressValor = seekBarValor.getProgress();
                int progressDistancia = seekBarDistancia.getProgress();
                String tipo = (String) spinnerTipos.getSelectedItem();
                atualizaMapa.atualiza(progressValor, progressDistancia, tipo);
                dismiss();
            }
        });

        buttonLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizaMapa.limpa();
            }
        });
    }

    public void setAtualizaCallback(AtualizaMapa atualizaCallback){
        this.atualizaMapa = atualizaCallback;
    }

    public interface AtualizaMapa{
        void atualiza(int valor, int distancia, String tipo);
        void limpa();
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getDialog().setTitle("Filtros");
    }
}
