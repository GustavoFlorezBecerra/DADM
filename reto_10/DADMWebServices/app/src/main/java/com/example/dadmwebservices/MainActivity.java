package com.example.dadmwebservices;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.dadmwebservices.dto.CatalogoDTO;
import com.example.dadmwebservices.service.CatalogData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView catalogoListView;
    private EditText programaRecoleccionEditText;
    private EditText codigoDaneEditText;
    private ArrayAdapter arrayAdapter;

    private List<CatalogoDTO> catalogo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, obtenerNombresCatalogo());

        catalogoListView = findViewById(R.id.listView1);
        catalogoListView.setAdapter(arrayAdapter);

        programaRecoleccionEditText = findViewById(R.id.ProgramaRecoleccionTextFiltroNombre);

        codigoDaneEditText = findViewById(R.id.EditTextCodigoDane);

        catalogoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", i);
                dataBundle.putString("catalogo", catalogo.get(i).toString());

                Intent intent = new Intent(getApplicationContext(),CatalogoDetalleActivity.class);

                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });
    }


    public void consultar(View view){
        obtenerCatalogo();
    }

    private List<String> obtenerNombresCatalogo() {
        List<String> nombresCatalogo = new ArrayList<>();
        for(CatalogoDTO catalogoDTO : catalogo) {
            nombresCatalogo.add(catalogoDTO.getRazonSocial());
        }

        return nombresCatalogo;
    }

    private void obtenerCatalogo(){
        Object[] fetchData = new Object[1];
        fetchData[0] = buildCatalogoUrl();
        CatalogData catalogData = new CatalogData() {

            @SuppressLint("StaticFieldLeak")
            @Override
            protected void onPostExecute(String s){
                try {
                    catalogo = new ArrayList<>();
                    JSONArray data = new JSONArray(s);

                    for(int i = 0; i < data.length(); i++) {
                        JSONObject obj = data.getJSONObject(i);
                        catalogo.add(new CatalogoDTO(
                                obj.getString("programa_de_recolecci_n"),
                                obj.getString("tipo_de_residuos"),
                                obj.getString("departamento"),
                                obj.getString("municipio"),
                                obj.getString("codigo_dane"),
                                obj.getString("direccion"),
                                obj.getString("raz_n_social")
                        ));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                arrayAdapter.clear();
                arrayAdapter.addAll(obtenerNombresCatalogo());
                arrayAdapter.notifyDataSetChanged();
            }
        };

        catalogData.execute(fetchData);
    }

    private String buildCatalogoUrl() {
        StringBuilder url = new StringBuilder();
        url.append("https://www.datos.gov.co/resource/c2fr-ezse.json");

        if(programaRecoleccionEditText.getText().length() == 0 && codigoDaneEditText.getText().length() == 0) {
            return url.toString();
        } else if(programaRecoleccionEditText.getText().length() > 0 && codigoDaneEditText.getText().length() == 0) {
            url.append("?");
            url.append("programa_de_recolecci_n=");
            url.append(programaRecoleccionEditText.getText());

            return url.toString();
        } else if(programaRecoleccionEditText.getText().length() == 0 && codigoDaneEditText.getText().length() > 0) {
            url.append("?");
            url.append("codigo_dane=");
            url.append(codigoDaneEditText.getText());

            return url.toString();
        } else {
            url.append("?");
            url.append("codigo_dane=");
            url.append(codigoDaneEditText.getText());
            url.append("&");
            url.append("programa_de_recolecci_n=");
            url.append(programaRecoleccionEditText.getText());

            return url.toString();
        }
    }
}