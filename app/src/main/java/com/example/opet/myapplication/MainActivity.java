//Mudar para o seu package
package com.example.opet.myapplication;

import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText ano;
    private EditText municipio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        ano = findViewById(R.id.textAno);
        municipio = findViewById(R.id.textMunicipio);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void carregarDados(View view) {

        String codigo = String.valueOf(municipio.getText());
        int data = Integer.parseInt(ano.getText().toString());
        String endpoint = "http://www.transparencia.gov.br/api-de-dados/bolsa-familia-por-municipio?mesAno=";

        for (int i = 1; i < 13; i++) {
            endpoint = endpoint.concat(String.valueOf(data) + String.valueOf(String.format("%02d", i))+"&codigoIbge=" + codigo + "%20&pagina=1");
            trazerJSON(endpoint);
            endpoint = "http://www.transparencia.gov.br/api-de-dados/bolsa-familia-por-municipio?mesAno=";
;        }

        progressBar.setVisibility(View.VISIBLE);
        ano.setVisibility(View.GONE);

    }

    private void trazerJSON(String endpoint) {

        RequestQueue queue = Volley.newRequestQueue(this);


        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, endpoint, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                //formatarSaida(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                ano.setVisibility(View.VISIBLE);
                ano.setText("Erro ao carregar dados.");
            }
        });

        queue.add(objectRequest);
    }


     /*private void formatarSaida(JSONObject response) {
        textAno.setVisibility(View.VISIBLE);
        try {
            String name = response.getString("name");
            textAno.setText("Nome: "+name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/
}
