package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import cz.msebera.android.httpclient.Header;

public class MedioPagoEditar extends AppCompatActivity implements View.OnClickListener{

    EditText nom,desc;
    Button guardar;
    final String servidor = "http://10.0.2.2/t2app/";

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medio_pago_editar);

        nom = (EditText) findViewById(R.id.txtnomPago2);
        desc = (EditText) findViewById(R.id.txtdesPago2);
        guardar = (Button) findViewById(R.id.btnGuardarMPago2);
        guardar.setOnClickListener(this);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        MedioPago mp = (MedioPago) b.getSerializable("id");
        id = mp.getId();

        Consultar(id);
    }

    private void Consultar(String id) {
        String url = servidor + "mediopago_consultar.php";

        RequestParams params = new RequestParams();
        params.put("id",id);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode==200)
                {
                    String nommp="",descmp="";
                    try {
                        JSONArray js = new JSONArray(new String(responseBody));
                        for (int i=0; i < js.length();i++)
                        {
                            nommp = js.getJSONObject(i).getString("nom_mediopago");
                            descmp = js.getJSONObject(i).getString("desc_mediopago");

                        }

                        nom.setText(nommp);
                        desc.setText(descmp);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(),"No se pudo consultar ",Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view==guardar) //cuando se presione el boton Grabar
        {
            String nom_ = nom.getText().toString();
            String desc_ = desc.getText().toString();

            ActualizarMedioPago(id,nom_,desc_);

        }
    }

    private void ActualizarMedioPago(String id, String nom_, String desc_) {
        String url = servidor + "mediopago_actualizar.php";

        RequestParams params = new RequestParams();
        params.put("id",id);
        params.put("nom",nom_);
        params.put("desc",desc_);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode==200) //proceso correctamente
                {
                    String res = new String(responseBody);
                    Toast.makeText(getApplicationContext(),"Actualizado correctamente "+res,Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getApplicationContext(),ListarMedioPagoActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(),"No se pudo actualizar ",Toast.LENGTH_LONG).show();

            }
        });
    }
}