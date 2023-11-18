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

public class ProveedorActivity extends AppCompatActivity implements View.OnClickListener{

    EditText rsocial,ruc,cel,email,dir;
    Button guardar;
    final String servidor = "http://10.0.2.2/t2app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedor);

        rsocial = (EditText) findViewById(R.id.txtRazSocial);
        ruc = (EditText) findViewById(R.id.txtRuc);
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
            String rsocial_ = rsocial.getText().toString();
            String ruc_ = ruc.getText().toString();
            String cel_ = cel.getText().toString();
            String email_ = email.getText().toString();
            String dir_ = dir.getText().toString();


            RegistrarProveedor(rsocial_,ruc_,cel_,email_,dir_);

        }

    }

    private void RegistrarProveedor(String rsocial_, String ruc_, String cel_, String email_, String dir_)
    {
        String url = servidor + "proveedor_registrar.php";

        RequestParams params = new RequestParams();
        params.put("rsocial",rsocial_);
        params.put("ruc",ruc_);
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