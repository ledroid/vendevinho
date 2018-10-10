package zap.com.example.app.appzap.Activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import zap.com.example.app.appzap.Constents;
import zap.com.example.app.appzap.R;
import zap.com.example.app.appzap.Service.ServiceRest;

public class SaleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private String TAG = AplicationActivity.class.getSimpleName();
    //URL REST.
    private static String url = "598b16861100004905515ec7";
    //Progress Dialog.
    private ProgressDialog dialog;
    //Array de elementos do REST.
    ArrayList<HashMap<String, String>> ilList;
    //List View com os itens do REST.
    private ListView list;
    //Classe REST para realizar o acesso ao URL com os dados.
    private ServiceRest service;

    private Spinner spinnerItens;
    private ArrayAdapter<String> dataAdapter;
    private List<String> categorias;

    private TextView codigoSale;
    View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);
        loadComponents();
    }

    public void loadComponents(){
        ilList = new ArrayList<>();
        list = (ListView) findViewById(R.id.listSale);

        codigoSale = (TextView) findViewById(R.id.codigoSale);

        spinnerItens = (Spinner) findViewById(R.id.spinnerItens);
        spinnerItens.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        categorias = new ArrayList<String>();
        categorias.add("Select ");
        // categorias.add("Casa Silva Reserva");
        // categorias.add("Casa Silva Gran Reserva");
        // categorias.add("Punto Final Etiqueta Negra");
        // categorias.add("Punto Final");
        // categorias.add("Punto Final Etiqueta Branca");
        // categorias.add("Casa Valduga Raízes");

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categorias);
        spinnerItens.setAdapter(dataAdapter);
        new GetRequest().execute();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.spinnerItens:{

            }
            break;

            default:
                break;
        }
    }

    private class GetRequest extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(SaleActivity.this);
            dialog.setMessage("Aguarde ...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            service = new ServiceRest();

            String jsonString = service.serviceCall(Constents.BASE_URL + url);

            if (jsonString != null) {
                try {
                    JSONArray sales = new JSONArray(jsonString);
                    for (int i = 0; i < sales.length(); i++) {
                        JSONObject jObject = sales.getJSONObject( i );
                        // JSONObject tipoItens = jObject.getJSONObject( "itens" );
                        JsonParser parser = new JsonParser();
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        JsonElement jsonElement = parser.parse( jsonString );

                        JSONArray itens = jObject.getJSONArray( "itens" );
                        String cod = jObject.getString( "codigo" );
                        String data = jObject.getString( "data" );
                        String cliente = jObject.getString( "cliente" );
                        JSONObject items = itens.getJSONObject( i );
                        String produto = items.getString( "produto" );
                        String variedade = items.getString( "variedade" );
                        String pais = items.getString( "pais" );
                        String categoria = items.getString( "categoria" );
                        String safra = items.getString( "safra" );
                        String preco = items.getString( "preco" );
                        String valorTotal = jObject.getString( "valorTotal" );
                        JsonElement elementItem = parser.parse( itens.toString() );

                        JSONObject prod = itens.getJSONObject( i );
                        String prods = prod.getString( "produto" );
                        String item = gson.toJson(elementItem);

                        categorias.add( prods );

                        String produtos = gson.toJson( jsonElement );

                        HashMap<String, String> hSales = new HashMap<>();

                        hSales.put( "cliente", cliente );
                        hSales.put( "data", data );
                        hSales.put( "produto", produto );
                        hSales.put( "variedade", variedade );
                        hSales.put( "pais", pais );
                        hSales.put( "categoria", categoria );
                        hSales.put( "safra", safra );
                        hSales.put( "preco", preco );
                        hSales.put( "valorTotal", valorTotal );

                        ilList.add( hSales );

                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "JSON Erro Parsin " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Erro JSON Parsin !\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG, "Not did get json server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Not did get json server. ", Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (dialog.isShowing())
                dialog.dismiss();
            ListAdapter adapter = new SimpleAdapter(
                    SaleActivity.this, ilList,
                    R.layout.list_item_sale, new String[]{
                    "cliente", "data","produto",
                    "variedade","pais","categoria",
                    "safra","preco","valorTotal"
                    }, new int[]{
                    R.id.codigoSale, R.id.dateSale, R.id.itemSale,
                    R.id.prodSale, R.id.countrySale, R.id.categorySale,
                    R.id.safraSale, R.id.praceSale, R.id.totalSale });

            list.setAdapter(adapter);
        }
    }
}
