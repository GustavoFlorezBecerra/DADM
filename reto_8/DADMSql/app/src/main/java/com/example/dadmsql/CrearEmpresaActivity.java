package com.example.dadmsql;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dadmsql.db.EmpresaDTO;
import com.example.dadmsql.db.EmpresasDBHelper;

public class CrearEmpresaActivity extends AppCompatActivity {

    int opcionAnterior = 0;
    private EmpresasDBHelper dbHelper ;

    EditText nombre;
    EditText url;
    EditText telefono;
    EditText email;
    EditText producto;
    //EditText clasificacion;
    Spinner clasificacionSp;

    int idEmpresa = 0;
    private String clasificacionEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_empresa);

        nombre = findViewById(R.id.editTextNombreEmpresa);
        url = findViewById(R.id.editTextUrlEmpresa);
        telefono = findViewById(R.id.editTextTelefono);
        email = findViewById(R.id.editTextEmail);
        producto = findViewById(R.id.editTextProductosServicios);
        //clasificacion = findViewById(R.id.editTextClasificacion);
        clasificacionSp = findViewById(R.id.spinnerClasificacion);

        dbHelper = new EmpresasDBHelper(getApplicationContext());

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            int empresaId = extras.getInt("id");

            if(empresaId > 0) {
                idEmpresa = empresaId;
                EmpresaDTO empresa = dbHelper.obtenerDetalles(empresaId);
                nombre.setText(empresa.getNombre());
                url.setText(empresa.getUrl());
                telefono.setText(empresa.getTelefono());
                email.setText(empresa.getEmail());
                producto.setText(empresa.getProducto());
                clasificacionEmpresa = empresa.getClasificacion();

                habilitarEdicion(false);
            }
        }

        configurarClasificacionSpinner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Bundle extras = getIntent().getExtras();

        if(extras !=null) {
            int opcion = extras.getInt("id");
            if(opcion > 0){
                getMenuInflater().inflate(R.menu.empresa_menu, menu);
            } else{
                getMenuInflater().inflate(R.menu.main_menu, menu);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.editar_empresa:
                habilitarEdicion(true);
                return true;
            case R.id.eliminar_empresa:
                eliminarEmpresa();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void habilitarEdicion(boolean esEditable) {
        Button botonGuardar = findViewById(R.id.button1);
        botonGuardar.setVisibility(esEditable ? View.VISIBLE : View.INVISIBLE);

        nombre.setEnabled(esEditable);
        nombre.setFocusableInTouchMode(esEditable);
        nombre.setClickable(esEditable);

        url.setEnabled(esEditable);
        url.setFocusableInTouchMode(esEditable);
        url.setClickable(esEditable);

        telefono.setEnabled(esEditable);
        telefono.setFocusableInTouchMode(esEditable);
        telefono.setClickable(esEditable);

        email.setEnabled(esEditable);
        email.setFocusableInTouchMode(esEditable);
        email.setClickable(esEditable);

        producto.setEnabled(esEditable);
        producto.setFocusableInTouchMode(esEditable);
        producto.setClickable(esEditable);

        clasificacionSp.setEnabled(esEditable);
        clasificacionSp.setFocusableInTouchMode(esEditable);
        clasificacionSp.setClickable(esEditable);
    }

    public void ejecutar(View view){

        Bundle extras = getIntent().getExtras();

        if(extras != null && validarDatosObligatorios()){
            int empresaId = extras.getInt("id");
            if(empresaId > 0) {
                if(dbHelper.actualizarEmpresa(empresaId, nombre.getText().toString(), url.getText().toString(),
                        email.getText().toString(), telefono.getText().toString(),
                        producto.getText().toString(), clasificacionEmpresa)){
                    Toast.makeText(getApplicationContext(), "Empresa actualizada", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                } else{
                    Toast.makeText(getApplicationContext(), "No se puede actualizar la empresa", Toast.LENGTH_SHORT).show();
                }
            } else {
                if(dbHelper.registrarEmpresa(nombre.getText().toString(), url.getText().toString(),
                        email.getText().toString(), telefono.getText().toString(),
                        producto.getText().toString(), clasificacionEmpresa)){
                    Toast.makeText(getApplicationContext(), "Empresa registrada",
                            Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getApplicationContext(), "No se pudo registrar la empresa",
                            Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }
    }

    private void eliminarEmpresa(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.eliminar_empresa).setPositiveButton(
                R.string.si, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.eliminarEmpresa(idEmpresa);
                        Toast.makeText(getApplicationContext(), "Empresa borrada correctamente",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }
        )
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        AlertDialog d = builder.create();
        d.setTitle(R.string.confirma_eliminacion);
        d.show();
    }

    private void configurarClasificacionSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                CrearEmpresaActivity.this,
                R.array.clasificacion_array,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clasificacionSp.setAdapter(adapter);

        clasificacionSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0) {
                    clasificacionEmpresa = (String) parent.getItemAtPosition(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Bundle extras = getIntent().getExtras();

        int empresaId = extras.getInt("id");
        if(empresaId > 0) {
            clasificacionSp.setSelection(adapter.getPosition(clasificacionEmpresa));
        }
    }

    private boolean validarDatosObligatorios() {
        if(nombre.getText() == null || url.getText() == null ||
                email.getText() == null || telefono.getText() == null ||
                producto.getText() == null || clasificacionEmpresa == null){
            Toast.makeText(getApplicationContext(), "Faltan datos de la empresa",
                    Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }
}
