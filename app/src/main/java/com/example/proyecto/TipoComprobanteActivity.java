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

public class TipoComprobanteActivity extends AppCompatActivity implements View.OnClickListener {

    EditText nomTC,descTC;
    Button guardarTC;

    final String servidor = "http://10.0.2.2/t2app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_comprobante);

        nomTC = (EditText) findViewById(R.id.txtnomTC);
        descTC = (EditText) findViewById(R.id.txtdesTC);
        guardarTC = (Button) findViewById(R.id.btnguardarTC);
        guardarTC.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if(view == guardarTC) //cuando se presione el boton Guardar
        {
            String nomTC_=nomTC.getText().toString();
            String descTC_=descTC.getText().toString();

            RegistrarTipoComprobante(nomTC_,descTC_);
        }
    }

    private void RegistrarTipoComprobante(String nomTC_, String descTC_)
    {
        String url = servidor + "tipocomprobante_registrar.php";

        RequestParams params = new RequestParams();
        params.put("nom",nomTC_);
        params.put("desc",descTC_);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {
                String res= new String(responseBody);
                Toast.makeText(getApplicationContext(),"Registrado correctamente "
                        +res, Toast.LENGTH_LONG).show();

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