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

public class ProveedorEditar extends AppCompatActivity implements View.OnClickListener {
    EditText rsocial,ruc,cel,email,dir;
    Button guardar;
    final String servidor = "http://10.0.2.2/t2app/";

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedor_editar);

        rsocial = (EditText) findViewById(R.id.txtRazSocial2);
        ruc = (EditText) findViewById(R.id.txtRuc2);
        cel = (EditText) findViewById(R.id.txtCelP2);
        email = (EditText) findViewById(R.id.txtEmailP2);
        dir = (EditText) findViewById(R.id.txtDirP2);
        guardar = (Button) findViewById(R.id.btnGuardarP2);
        guardar.setOnClickListener(this);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        Proveedor pv = (Proveedor) b.getSerializable("id");
        id = pv.getId();

        Consultar(id);
    }

    private void Consultar(String id) {
        String url = servidor + "proveedor_consultar.php";

        RequestParams params = new RequestParams();
        params.put("id",id);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode==200)
                {
                    String razpv="",rucpv="",celpv="",emailpv="",dirpv="";
                    try {
                        JSONArray js = new JSONArray(new String(responseBody));
                        for (int i=0; i < js.length();i++)
                        {
                            razpv = js.getJSONObject(i).getString("razon_social");
                            rucpv = js.getJSONObject(i).getString("ruc_proveedor");
                            celpv = js.getJSONObject(i).getString("celular_proveedor");
                            emailpv =js.getJSONObject(i).getString("email_proveedor");
                            dirpv = js.getJSONObject(i).getString("dir_proveedor");

                        }

                        rsocial.setText(razpv);
                        ruc.setText(rucpv);
                        cel.setText(celpv);
                        email.setText(emailpv);
                        dir.setText(dirpv);

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
            String razsocial_ = rsocial.getText().toString();
            String ruc_ = ruc.getText().toString();
            String cel_ = cel.getText().toString();
            String email_=email.getText().toString();
            String dir_= dir.getText().toString();

            ActualizarProveedor(id,razsocial_,ruc_,cel_,email_,dir_);

        }
    }

    private void ActualizarProveedor(String id, String razsocial_, String ruc_, String cel_, String email_, String dir_) {
        String url = servidor + "proveedor_actualizar.php";

        RequestParams params = new RequestParams();
        params.put("id",id);
        params.put("rsocial",razsocial_);
        params.put("ruc",ruc_);
        params.put("cel",cel_);
        params.put("email",email_);
        params.put("dir",dir_);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode==200) //proceso correctamente
                {
                    String res = new String(responseBody);
                    Toast.makeText(getApplicationContext(),"Actualizado correctamente "+res,Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getApplicationContext(),ListarProveedorActivity.class);
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