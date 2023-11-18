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


public class MedioPagoActivity extends AppCompatActivity implements View.OnClickListener{

    EditText nom,desc;
    Button guardar;
    final String servidor = "http://10.0.2.2/t2app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medio_pago);

        nom = (EditText) findViewById(R.id.txtnomPago);
        desc = (EditText) findViewById(R.id.txtdesPago);
        guardar = (Button) findViewById(R.id.btnGuardarMPago);
        guardar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if(view == guardar) //cuando se presione el boton Guardar
        {
            String nom_=nom.getText().toString();
            String desc_=desc.getText().toString();

            RegistrarMedioPago(nom_,desc_);
        }
    }

    private void RegistrarMedioPago(String nom_, String desc_)
    {
        String url = servidor + "mediopago_registrar.php";

        RequestParams params = new RequestParams();
        params.put("nom",nom_);
        params.put("desc",desc_);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {
                if(statusCode==200) // Procesó correctamente
                {
                    String res= new String(responseBody);
                    Toast.makeText(getApplicationContext(),"Registrado correctamente "
                            +res, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
            {
                Toast.makeText(getApplicationContext(),"No se pudo " +
                        "registrar ",Toast.LENGTH_LONG).show();
            }
        });
    }
}