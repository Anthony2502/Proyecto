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

public class ProductoActivity extends AppCompatActivity implements View.OnClickListener{

    EditText nom,prev,prec,desc;
    Button guardar;
    final String servidor = "http://10.0.2.2/t2app/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        nom = (EditText) findViewById(R.id.txtnomPago);
        prev = (EditText) findViewById(R.id.txtprevPro);
        prec = (EditText) findViewById(R.id.txtprecPro);
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
            float prev_= Float.parseFloat(prev.getText().toString());
            float prec_= Float.parseFloat(prec.getText().toString());
            String desc_=desc.getText().toString();

            RegistrarProducto(nom_,prev_,prec_,desc_);
        }
    }

    private void RegistrarProducto(String nom_, float prev_, float prec_,String desc_)
    {
        String url = servidor + "producto_registrar.php";

        RequestParams params = new RequestParams();
        params.put("nom",nom_);
        params.put("prev",prev_);
        params.put("prec",prec_);
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