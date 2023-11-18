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

public class ProductoEditar extends AppCompatActivity implements View.OnClickListener {

    EditText nomp,prev,prec,desc;
    Button guardar;
    final String servidor = "http://10.0.2.2/t2app/";

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_editar);

        nomp = (EditText) findViewById(R.id.txtnomPro2);
        prev = (EditText) findViewById(R.id.txtprevPro2);
        prec = (EditText) findViewById(R.id.txtprecPro2);
        desc = (EditText) findViewById(R.id.txtdesPro2);
        guardar = (Button) findViewById(R.id.btnGuardarPro2);
        guardar.setOnClickListener(this);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        Producto pro = (Producto) b.getSerializable("id");
        id = pro.getId();

        Consultar(id);
    }

    private void Consultar(String id) {
        String url = servidor + "producto_consultar.php";

        RequestParams params = new RequestParams();
        params.put("id",id);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode==200)
                {
                    String nompro="",prevpro="",precpro="",descpro="";
                    try {
                        JSONArray js = new JSONArray(new String(responseBody));
                        for (int i=0; i < js.length();i++)
                        {
                            nompro = js.getJSONObject(i).getString("nom_producto");
                            prevpro = js.getJSONObject(i).getString("preciov_producto");
                            precpro = js.getJSONObject(i).getString("precioc_producto");
                            descpro = js.getJSONObject(i).getString("desc_producto");

                        }

                        nomp.setText(nompro);
                        prev.setText(prevpro);
                        prec.setText(precpro);
                        desc.setText(descpro);

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
            String nom_ = nomp.getText().toString();
            String prev_ = prev.getText().toString();
            String prec_ = prec.getText().toString();
            String desc_ = desc.getText().toString();

            ActualizarProducto(id,nom_,prev_,prec_,desc_);

        }

    }

    private void ActualizarProducto(String id, String nom_, String prev_, String prec_, String desc_) {
        String url = servidor + "producto_actualizar.php";

        RequestParams params = new RequestParams();
        params.put("id",id);
        params.put("nom",nom_);
        params.put("prev",prev_);
        params.put("prec",prec_);
        params.put("desc",desc_);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode==200) //proceso correctamente
                {
                    String res = new String(responseBody);
                    Toast.makeText(getApplicationContext(),"Actualizado correctamente "+res,Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getApplicationContext(),ListarProductoActivity.class);
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