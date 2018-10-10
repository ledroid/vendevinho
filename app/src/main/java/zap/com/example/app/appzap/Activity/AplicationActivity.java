package zap.com.example.app.appzap.Activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import zap.com.example.app.appzap.Constents;
import zap.com.example.app.appzap.R;
import zap.com.example.app.appzap.Service.ServiceRest;

import static zap.com.example.app.appzap.R.layout.list_item;

public class AplicationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private String TAG = AplicationActivity.class.getSimpleName();
    //URL REST.
    private static String url = "598b16291100004705515ec5";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aplication);
        loadComponents();
    }

    public void loadComponents(){
        ilList = new ArrayList<>();
        list = (ListView) findViewById(R.id.list);

        spinnerItens = (Spinner) findViewById(R.id.spinnerItens);
        spinnerItens.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        categorias = new ArrayList<String>();
        categorias.add("Select ");
        // categorias.add("Nome");
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
                new GetRequest().execute();
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
            dialog = new ProgressDialog(AplicationActivity.this);
            dialog.setMessage("Aguarde ...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            service = new ServiceRest();

            String jsonString = service.serviceCall(Constents.BASE_URL + url);

            if (jsonString != null && jsonString.length() > 0) {
                try {
                    JSONArray clientes = new JSONArray(jsonString);
                    for (int i = 0; i <clientes.length(); i++) {
                        JSONObject jObject = clientes.getJSONObject(i);
                        String nomeCliente = jObject.getString("nome");
                        String cpfCliete = jObject.getString("cpf");
                        categorias.add(nomeCliente);

                        String idCliente = jObject.getString("id");
                        String nomeCli = jObject.getString("nome");
                        String cpfCli = jObject.getString("cpf");

                        HashMap<String, String> cli = new HashMap<>();
                        cli.put("id", idCliente);
                        cli.put("nome", nomeCli);
                        cli.put("cpf", cpfCli);

                        ilList.add(cli);
                    }
                } catch (final JSONException e) {
                    dialog.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Erro JSON Parsin !\n"
                                    + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                    e.printStackTrace();
                }
            } else {
                dialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Not did get json server. ",
                                Toast.LENGTH_LONG).show();
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
                    AplicationActivity.this, ilList,
                    list_item, new String[]{
                            "id", "nome", "cpf"},
                    new int[]{R.id.codigoCli, R.id.nomeCli, R.id.numeroCPF});

            list.setAdapter(adapter);
        }
    }
}
