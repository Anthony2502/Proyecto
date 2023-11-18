package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class ClienteActivity extends AppCompatActivity implements View.OnClickListener{

    EditText nom,ape,dni,cel,email,dir;
    Button guardar;
    final String servidor = "http://10.0.2.2/t2app/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        nom = (EditText) findViewById(R.id.txtRazSocial);
        ape = (EditText) findViewById(R.id.txtRuc);
        dni = (EditText) findViewById(R.id.txtDniC);
        cel = (EditText) findViewById(R.id.txtCelP);
        email = (EditText) findViewById(R.id.txtEmailP);
        dir = (EditText) findViewById(R.id.txtDirP);
        guardar = (Button) findViewById(R.id.btnGuardarP);
        guardar.setOnClickListener(this);

    }

    @Override
    public void onClick(View view)
    {
        if(view==guardar) //cuando se presione el boton Grabar
        {
            String nom_ = nom.getText().toString();
            String ape_ = ape.getText().toString();
            String dni_ = dni.getText().toString();
            String cel_ = cel.getText().toString();
            String email_ = email.getText().toString();
            String dir_ = dir.getText().toString();

            RegistrarCliente(nom_,ape_,dni_,cel_,email_,dir_);

        }
    }

    private void RegistrarCliente(String nom_, String ape_, String dni_, String cel_, String email_, String dir_)
    {
        String url = servidor + "cliente_registrar.php";

        RequestParams params = new RequestParams();
        params.put("nom",nom_);
        params.put("ape",ape_);
        params.put("dni",dni_);
        params.put("cel",cel_);
        params.put("email",email_);
        params.put("dir",dir_);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {
                if(statusCode==200) //proceso correctamente
                {
                    String res = new String(responseBody);
                    Toast.makeText(getApplicationContext(),"Registrado correctamente "+res,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
            {
                Toast.makeText(getApplicationContext(),"No se pudo registrar ",Toast.LENGTH_LONG).show();

            }
        });
    }
}