package com.example.dadmsql;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.dadmsql.db.EmpresasDBHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "MESSAGE";
    private ListView empresaListView;
    private EmpresasDBHelper dbHelper;
    private EditText nombreEmpresaEditText;
    private EditText clasificacionEmpresaEditText;
    private ArrayAdapter arrayAdapter;
    private List<Integer> empresasId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new EmpresasDBHelper(getApplicationContext());

        List empresas = dbHelper.obtenerEmpresasTodas();
        empresasId = dbHelper.obtenerIdEmpresas();

        arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, empresas);

        empresaListView = findViewById(R.id.listView1);
        empresaListView.setAdapter(arrayAdapter);

        nombreEmpresaEditText = findViewById(R.id.EditTextFiltroNombre);
        setupFiltroNombreEditTextChangeListener();

        clasificacionEmpresaEditText = findViewById(R.id.EditTextFiltroClasificacion);
        setupFiltroClasificacionEditTextChangeListener();

        empresaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", empresasId.isEmpty() ? 0 : empresasId.get(position));

                Intent intent = new Intent(getApplicationContext(),CrearEmpresaActivity.class);

                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        switch(item.getItemId()) {
            case R.id.item1:Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", 0);

                Intent intent = new Intent(getApplicationContext(),CrearEmpresaActivity.class);
                intent.putExtras(dataBundle);

                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     *
     * @param keycode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }

    /**
     *
     *
     */
    private void setupFiltroNombreEditTextChangeListener() {

        nombreEmpresaEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> empresasFiltro;

                if(nombreEmpresaEditText.getText().length() == 0){
                    empresasFiltro = dbHelper.obtenerEmpresasTodasFiltro(null, null);
                    empresasId = dbHelper.obtenerIdEmpresas(null, null);
                }  else if(nombreEmpresaEditText.getText().length() > 0 && clasificacionEmpresaEditText.getText().length() > 0) {
                    empresasFiltro = dbHelper.obtenerEmpresasTodasFiltro(nombreEmpresaEditText.getText().toString(), clasificacionEmpresaEditText.getText().toString());
                    empresasId = dbHelper.obtenerIdEmpresas(nombreEmpresaEditText.getText().toString(), clasificacionEmpresaEditText.getText().toString());
                }else {
                    empresasFiltro = dbHelper.obtenerEmpresasTodasFiltro(nombreEmpresaEditText.getText().toString(), null);
                    empresasId = dbHelper.obtenerIdEmpresas(nombreEmpresaEditText.getText().toString(), null);
                }

                updateData(empresasFiltro);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     *
     *
     */
    private void setupFiltroClasificacionEditTextChangeListener() {

        clasificacionEmpresaEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> empresasFiltro;

                if(clasificacionEmpresaEditText.getText().length() == 0){
                    empresasFiltro = dbHelper.obtenerEmpresasTodasFiltro(null, null);
                    empresasId = dbHelper.obtenerIdEmpresas(null, null);
                } else if(clasificacionEmpresaEditText.getText().length() > 0  && nombreEmpresaEditText.getText().length() > 0) {
                    empresasFiltro = dbHelper.obtenerEmpresasTodasFiltro(nombreEmpresaEditText.getText().toString(), clasificacionEmpresaEditText.getText().toString());
                    empresasId = dbHelper.obtenerIdEmpresas(nombreEmpresaEditText.getText().toString(), clasificacionEmpresaEditText.getText().toString());
                } else {
                    empresasFiltro = dbHelper.obtenerEmpresasTodasFiltro(null, clasificacionEmpresaEditText.getText().toString());
                    empresasId = dbHelper.obtenerIdEmpresas(null, clasificacionEmpresaEditText.getText().toString());
                }

                updateData(empresasFiltro);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void updateData(List<String> empresas){
        arrayAdapter.clear();
        arrayAdapter.addAll(empresas);
        arrayAdapter.notifyDataSetChanged();
    }
}
