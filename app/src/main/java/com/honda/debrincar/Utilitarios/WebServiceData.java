package com.honda.debrincar.Utilitarios;

import android.content.Context;
import android.net.UrlQuerySanitizer;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.honda.debrincar.Activities.CadastroUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class WebServiceData extends AsyncTask<URL, Integer, String> {
    private static final String TAG = "WebServiceData";

    private String data = "";
    //private String[] estados = new String[27];
    private List<String> estados = new ArrayList<>();

    private HashMap<String, Integer> mapaEstadosNum = new HashMap<>();
    private HashMap<Integer, List<String>> mapaCidades = new HashMap<>();

    @Override
    protected String doInBackground(URL... strings) {
        try {
            URL url = new URL("https://servicodados.ibge.gov.br/api/v1/localidades/estados/");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";
            while (line != null){
                line = bufferedReader.readLine();
                data += line;
            }

            JSONArray jsonArray = new JSONArray(data);

            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String idEstado = jsonObject.get("id").toString();
                int posicao = Integer.parseInt(idEstado);
                String estado = jsonObject.get("nome").toString();
                mapaEstadosNum.put(estado, posicao);
                estados.add(estado);
            }
            for(int i=0; i<estados.size(); i++){
                int estadoAtual = mapaEstadosNum.get(estados.get(i));
                URL urlCidade = new URL("https://servicodados.ibge.gov.br/api/v1/localidades/estados/" +
                        estadoAtual + "/municipios");
                HttpURLConnection httpCidades = (HttpURLConnection) urlCidade.openConnection();
                InputStream inputStreamCidades = httpCidades.getInputStream();
                BufferedReader bufferedReaderCidades = new BufferedReader(new InputStreamReader(inputStreamCidades));
                String lineCidades = "";
                String dataCidades = "";
                while (lineCidades != null){
                    lineCidades = bufferedReaderCidades.readLine();
                    dataCidades += lineCidades;
                }
                JSONArray jaCidades = new JSONArray(dataCidades);
                List<String> cidades = new ArrayList<>();
                for(int j=0; j<jaCidades.length(); j++){
                    JSONObject joCidades = (JSONObject) jaCidades.get(j);
                    String cidade = joCidades.get("nome").toString();
                    cidades.add(cidade);
                }
                Collections.sort(cidades);
                mapaCidades.put(estadoAtual, cidades);
            }

        } catch (Exception e){
            Log.d(TAG, "Erro na criação dos estados: " + e.getMessage());
            e.printStackTrace();

        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        ConfiguraçãoApp.estados = estados;
        ConfiguraçãoApp.mapaEstados = mapaEstadosNum;
        ConfiguraçãoApp.mapaCidades = mapaCidades;
    }
}
