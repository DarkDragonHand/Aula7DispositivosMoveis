package com.example.aula7dispositivosmoveis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private TextView temperatura;
    private TextView umidade;
    private TextView orvalho;
    private TextView pressao;
    private ListView listView;

    String[] de = {"temp", "umid", "orv", "press"};
    int[] para = {R.id.txtTemp, R.id.txtUmid, R.id.txtOrv, R.id.txtPress};

    List<Map<String, String>> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperatura = findViewById(R.id.txtTemperatura);
        umidade = findViewById(R.id.txtUmidade);
        orvalho = findViewById(R.id.txtOrvalho);
        pressao = findViewById(R.id.txtPressao);
        listView = findViewById(R.id.listaView);
    }

    public void onClickBuscar(View view) {
        lista = new ArrayList<>();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://ghelfer.net/la/weather.json", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String dados = new String(response);
                Toast.makeText(getApplicationContext(), dados, Toast.LENGTH_SHORT).show();
                try {
                    carregarDados(dados);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void carregarDados(String dados) throws JSONException {
        JSONObject res = new JSONObject(dados);
        JSONArray array = res.getJSONArray("weather");

        double sumTemp = 0;
        double sumUmid = 0;
        double sumOrv = 0;
        double sumPress = 0;

        for (int i = 0; i < array.length(); i++) {

            JSONObject json = array.getJSONObject(i);

            String temp = json.get("temperature").toString();
            sumTemp += Double.parseDouble(temp);

            String umid = json.get("humidity").toString();
            sumUmid += Double.parseDouble(umid);

            String orv = json.get("dewpoint").toString();
            sumOrv += Double.parseDouble(orv);

            String press = json.get("pressure").toString();
            sumPress += Double.parseDouble(press);

            Map<String, String> mapa = new HashMap<>();
            mapa.put("temp", temp);
            mapa.put("umid", umid);
            mapa.put("orv", orv);
            mapa.put("press", press);

            lista.add(mapa);
        }

        temperatura.setText(String.valueOf(sumTemp / array.length()));
        umidade.setText(String.valueOf(sumUmid / array.length()));
        orvalho.setText(String.valueOf(sumOrv / array.length()));
        pressao.setText(String.valueOf(sumPress / array.length()));

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, lista, R.layout.linha_tempo, de, para);
        listView.setAdapter(simpleAdapter);

    }
}