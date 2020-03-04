//Mudar para o seu package
package com.example.opet.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView textData;
    private String saida = "";
    private int cont=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        textData = findViewById(R.id.textData);
        textData.setMovementMethod(new ScrollingMovementMethod());

    }

    private void trazerJSON (String endpoint){

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, endpoint, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if(response.getString("next")!="null"){
                        saida=saida.concat(procurarIdiomas(response));
                        trazerJSON(response.getString("next"));
                    }else{
                        saida=saida.concat(procurarIdiomas(response));
                        formatarSaida();
                    }
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

    public void carregarDados(View view){

        progressBar.setVisibility(View.VISIBLE);
        textData.setVisibility(View.GONE);
        String endpoint = "https://swapi.co/api/species";
        trazerJSON(endpoint);
    }

    private void formatarSaida() {
        progressBar.setVisibility(View.GONE);
        textData.setVisibility(View.VISIBLE);
        textData.setText(saida);
    }

    private String procurarIdiomas(@NonNull JSONObject response) {

        try {
            JSONArray lista;
            lista = response.getJSONArray("results");
            String saida="";

            for (int i = 0; i<lista.length();i++){
                JSONObject aux = (JSONObject) lista.get(i);
                cont++;
                saida = saida.concat(String.valueOf(cont)+" Especie:  " + aux.getString("name") + "  -  Idioma: " + aux.getString("language") + "\n");
            }

            return saida;
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}
