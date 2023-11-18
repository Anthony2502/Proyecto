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

public class ClienteEditar extends AppCompatActivity implements View.OnClickListener{

    EditText nom,ape,dni,cel,email,dir;
    Button grabar;
    final String servidor = "http://10.0.2.2/t2app/";
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_editar);

        nom = (EditText) findViewById(R.id.txtNombre2);
        ape = (EditText) findViewById(R.id.txtApellido2);
        dni = (EditText) findViewById(R.id.txtDniC2);
        cel = (EditText) findViewById(R.id.txtCel2);
        email = (EditText) findViewById(R.id.txtEmail2);
        dir = (EditText) findViewById(R.id.txtDir2);
        grabar = (Button) findViewById(R.id.btnActualizar2);
        grabar.setOnClickListener(this);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        Cliente c = (Cliente) b.getSerializable("id");
        id = c.getId();

        Consultar(id);
    }

    private void Consultar(String id) {
        String url = servidor + "cliente_consultar.php";

        RequestParams params = new RequestParams();
        params.put("id",id);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode==200) //proceso correctamente
                {
                    try {
                        String nom1="",ape1="",dni1="",cel1="",email1="",dir1="";

                        JSONArray js = new JSONArray(new String(responseBody));
                        for (int i=0; i < js.length();i++)
                        {
                            nom1 = js.getJSONObject(i).getString("nom_cliente");
                            ape1 = js.getJSONObject(i).getString("ape_cliente");
                            dni1 = js.getJSONObject(i).getString("dni_cliente");
                            cel1 = js.getJSONObject(i).getString("cel_cliente");
                            email1 = js.getJSONObject(i).getString("email_cliente");
                            dir1 = js.getJSONObject(i).getString("dir_cliente");
                        }

                        nom.setText(nom1);
                        ape.setText(ape1);
                        dni.setText(dni1);
                        cel.setText(cel1);
                        email.setText(email1);
                        dir.setText(dir1);

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
        if(view==grabar) //cuando se presione el boton Grabar
        {
            String nom_ = nom.getText().toString();
            String ape_ = ape.getText().toString();
            String dni_ = dni.getText().toString();
            String cel_ = cel.getText().toString();
            String email_ = email.getText().toString();
            String dir_ = dir.getText().toString();

            ActualizarCliente(id,nom_,ape_,dni_,cel_,email_,dir_);

        }
    }

    private void ActualizarCliente(String id, String nom_, String ape_, String dni_, String cel_, String email_, String dir_) {
        String url = servidor + "cliente_actualizar.php";

        RequestParams params = new RequestParams();
        params.put("id",id);
        params.put("nom",nom_);
        params.put("ape",ape_);
        params.put("dni",dni_);
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

                    Intent intent = new Intent(getApplicationContext(),ListarClienteActivity.class);
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