package com.example.dadmwebservices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dadmwebservices.dto.CatalogoDTO;

public class CatalogoDetalleActivity extends AppCompatActivity {

    private EditText programaRecoleccion;
    private EditText tipoResiduos;
    private EditText departamento;
    private EditText municipio;
    private EditText codigoDane;
    private EditText direccion;
    private EditText razonSocial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo_detalle);

        programaRecoleccion = findViewById(R.id.editTextProgramaRecoleccion);
        tipoResiduos = findViewById(R.id.editTextTipoResiduos);
        departamento = findViewById(R.id.editTextDepartamento);
        municipio = findViewById(R.id.editTextMunicipio);
        codigoDane = findViewById(R.id.editTextCodigoDane);
        direccion = findViewById(R.id.editTextDireccion);
        razonSocial = findViewById(R.id.editTextRazonSocial);

        Button botonVolver = findViewById(R.id.button1);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String catalogo = extras.getString("catalogo");
            if(catalogo != null && !catalogo.isEmpty()) {
                CatalogoDTO catalogoDTO = deserializadorCatalogo(catalogo);
                programaRecoleccion.setText(catalogoDTO.getProgramaRecoleccion());
                tipoResiduos.setText(catalogoDTO.getTipoResiduos());
                departamento.setText(catalogoDTO.getDepartamento());
                municipio.setText(catalogoDTO.getMunicipio());
                codigoDane.setText(catalogoDTO.getCodigoDane());
                direccion.setText(catalogoDTO.getDireccion());
                razonSocial.setText(catalogoDTO.getRazonSocial());

                deshabilitarEdicion();
            }
        }
    }

    public void volver(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private CatalogoDTO deserializadorCatalogo(String catalogoSerializado) {
        String[] catalogoInfo = catalogoSerializado.split(";");

        return new CatalogoDTO(
                catalogoInfo[0],
                catalogoInfo[1],
                catalogoInfo[2],
                catalogoInfo[3],
                catalogoInfo[4],
                catalogoInfo[5],
                catalogoInfo[6]);
    }

    private void deshabilitarEdicion() {
        programaRecoleccion.setEnabled(false);
        programaRecoleccion.setFocusableInTouchMode(false);
        programaRecoleccion.setClickable(false);

        tipoResiduos.setEnabled(false);
        tipoResiduos.setFocusableInTouchMode(false);
        tipoResiduos.setClickable(false);

        departamento.setEnabled(false);
        departamento.setFocusableInTouchMode(false);
        departamento.setClickable(false);

        municipio.setEnabled(false);
        municipio.setFocusableInTouchMode(false);
        municipio.setClickable(false);

        codigoDane.setEnabled(false);
        codigoDane.setFocusableInTouchMode(false);
        codigoDane.setClickable(false);

        direccion.setEnabled(false);
        direccion.setFocusableInTouchMode(false);
        direccion.setClickable(false);

        razonSocial.setEnabled(false);
        razonSocial.setFocusableInTouchMode(false);
        razonSocial.setClickable(false);
    }
}