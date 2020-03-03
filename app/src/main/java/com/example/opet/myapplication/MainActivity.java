//Mudar para o seu package
package com.example.opet.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView textData;
    private TextView textTerreno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        textData = findViewById(R.id.textData);
        textTerreno = findViewById(R.id.textTerreno);
    }

    public void carregarDados(View view) {
        progressBar.setVisibility(View.VISIBLE);
        textData.setVisibility(View.GONE);
        textTerreno.setVisibility(View.GONE);

        RequestQueue queue = Volley.newRequestQueue(this);
        String endpoint = "https://swapi.co/api/species";

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, endpoint, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                //textData.setVisibility(View.VISIBLE);
                //textData.setText(response.toString());
                try {
                    procurarIdiomas(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                textData.setVisibility(View.VISIBLE);
                textData.setText("Erro ao carregar dados.");
            }
        });

        queue.add(objectRequest);
    }

    private void formatarSaida(List resultado) {
        textData.setVisibility(View.VISIBLE);
        textTerreno.setVisibility(View.VISIBLE);

        String saida="";
        for (int i = 0; i<resultado.size();i++){
            saida.concat("Nome: " + resultado.get(0).toString() + "Terreno: " + resultado.get(9).toString()+"\n");
            textData.setText(saida);
        }

    }

    private void  procurarIdiomas(JSONObject response) throws JSONException {

        ArrayList lista = new ArrayList((Collection) response.get("results"));

        List resultado = new ArrayList();

        for (int i = 0; i<lista.size(); i++){
            resultado.add(lista.get(i));
        }

        formatarSaida(resultado);
    }
}
