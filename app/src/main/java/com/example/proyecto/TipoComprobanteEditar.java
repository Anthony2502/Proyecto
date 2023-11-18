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

public class TipoComprobanteEditar extends AppCompatActivity implements View.OnClickListener{
    EditText nomTC,descTC;
    Button guardarTC;

    final String servidor = "http://10.0.2.2/t2app/";
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_comprobante_editar);

        nomTC = (EditText) findViewById(R.id.txtnomTC2);
        descTC = (EditText) findViewById(R.id.txtdesTC2);
        guardarTC = (Button) findViewById(R.id.btnguardarTC2);
        guardarTC.setOnClickListener(this);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        TipoComprobante tc = (TipoComprobante) b.getSerializable("id");
        id = tc.getId();

        Consultar(id);
    }

    private void Consultar(String id) {
        String url = servidor + "tipocomprobante_consultar.php";

        RequestParams params = new RequestParams();
        params.put("id",id);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode==200)
                {
                    String nomtc="",desctc="";
                    try {
                        JSONArray js = new JSONArray(new String(responseBody));
                        for (int i=0; i < js.length();i++)
                        {
                            nomtc = js.getJSONObject(i).getString("nom_tipocomprobante");
                            desctc = js.getJSONObject(i).getString("desc_tipocomprobante");

                        }

                        nomTC.setText(nomtc);
                        descTC.setText(desctc);

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
        if(view==guardarTC) //cuando se presione el boton Grabar
        {
            String nom_ = nomTC.getText().toString();
            String desc_ = descTC.getText().toString();

            ActualizarTipoComprobante(id,nom_,desc_);

        }
    }

    private void ActualizarTipoComprobante(String id, String nom_,String desc_) {
        String url = servidor + "tipocomprobante_actualizar.php";

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

                    Intent intent = new Intent(getApplicationContext(),ListarTipoComprobanteActivity.class);
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