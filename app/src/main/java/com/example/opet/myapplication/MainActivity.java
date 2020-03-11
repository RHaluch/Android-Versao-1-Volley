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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText ano, municipio;
    private TextView textNome, textEstado, total, media, maior, menor;
    private String nome="", estado;
    private long totalBolsa=0, totalBeneficiados=0, menorValor, maiorValor;
    private int maiorMes, menorMes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        ano = findViewById(R.id.textAno);
        municipio = findViewById(R.id.textMunicipio);
        textNome = findViewById(R.id.textNome);
        textEstado = findViewById(R.id.textEstado);
        total = findViewById(R.id.total);
        media = findViewById(R.id.media);
        maior = findViewById(R.id.maior);
        menor = findViewById(R.id.menor);

    }



    public void carregarDados(View view) {

        progressBar.setVisibility(View.VISIBLE);
        String codigo = String.valueOf(municipio.getText());
        int data = Integer.parseInt(ano.getText().toString());
        String endpoint = "http://www.transparencia.gov.br/api-de-dados/bolsa-familia-por-municipio?mesAno=";


        for (int i = 1; i < 13; i++) {
            endpoint = endpoint.concat(String.valueOf(data) + String.valueOf(String.format("%02d", i))+"&codigoIbge=" + codigo + "&pagina=1");
            trazerJSON(endpoint);
            endpoint = "http://www.transparencia.gov.br/api-de-dados/bolsa-familia-por-municipio?mesAno=";
        }
    }

    private void trazerJSON(String endpoint) {

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET,endpoint,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject objeto = (JSONObject) response.get(0);
                    String data = objeto.getString("dataReferencia");
                    String dataSeparada[] = data.split("/");

                    if(nome=="") {
                        JSONObject municipio = (JSONObject) objeto.get("municipio");
                        nome = municipio.getString("nomeIBGE");
                        JSONObject objEstado = municipio.getJSONObject("uf");
                        estado = objEstado.getString("nome");
                        maiorMes = Integer.parseInt(dataSeparada[1]);
                        menorMes = Integer.parseInt(dataSeparada[1]);
                        maiorValor = objeto.getLong("valor");
                        menorValor = objeto.getLong("valor");
                    }

                    totalBolsa+=objeto.getLong("valor");
                    totalBeneficiados+=objeto.getLong("quantidadeBeneficiados");

                    if(maiorValor<objeto.getLong("valor")){
                        maiorMes = Integer.parseInt(dataSeparada[1]);
                        maiorValor = objeto.getLong("valor");
                    }
                    if(menorValor>objeto.getLong("valor")){
                        menorMes = Integer.parseInt(dataSeparada[1]);
                        menorValor = objeto.getLong("valor");
                    }

                    formatarSaida();

                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                ano.setVisibility(View.VISIBLE);
                ano.setText("Erro ao buscar dados!");
            }
        });
        queue.add(arrayRequest);
    }


     private void formatarSaida() {
        textNome.setVisibility(View.VISIBLE);
        textNome.setText("Nome do municipio: "+nome);
        textEstado.setVisibility(View.VISIBLE);
        textEstado.setText("Estado: "+estado);
        total.setVisibility(View.VISIBLE);
        total.setText("Total das Bolsas: "+String.valueOf(totalBolsa));
        media.setVisibility(View.VISIBLE);
        media.setText("Media dos Benefiados: " + String.valueOf(totalBeneficiados/12));
        maior.setVisibility(View.VISIBLE);
        maior.setText("Maior valor: "+String.valueOf(maiorValor)+ " do mês: "+String.valueOf(maiorMes));
        menor.setVisibility(View.VISIBLE);
        menor.setText("Menor valor: "+String.valueOf(menorValor)+ " do mês: "+String.valueOf(menorMes));

    }
}
