package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ListarTipoComprobanteActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    ListView listaTC;
    Button nuevoTC;
    ArrayList idTC_array = new ArrayList();
    ArrayList nomTC_array = new ArrayList();
    ArrayList descTC_array = new ArrayList();

    TipoComprobante tc = null;

    final String servidor = "http://10.0.2.2/t2app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_tipo_comprobante);

        listaTC = (ListView) findViewById(R.id.lvComprobante);
        listaTC.setOnItemClickListener(this);
        nuevoTC = (Button) findViewById(R.id.btnNuevoComprobante);
        nuevoTC.setOnClickListener(this);

        tc = new TipoComprobante();

        ListarTipoComprobante();
    }

    private void ListarTipoComprobante()
    {
        idTC_array.clear();
        nomTC_array.clear();
        descTC_array.clear();

        String url = servidor + "tipocomprobante_listar.php";
        RequestParams params = new RequestParams();
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {
                if(statusCode==200) //proceso correctamente
                {
                    try {
                        JSONArray js = new JSONArray(new String(responseBody));
                        for (int i=0; i < js.length();i++)
                        {
                            idTC_array.add(String.valueOf(js.getJSONObject(i).getInt("id_tipocomprobante")));
                            nomTC_array.add(js.getJSONObject(i).getString("nom_tipocomprobante"));
                            descTC_array.add(js.getJSONObject(i).getString("desc_tipocomprobante"));
                        }
                        listaTC.setAdapter(null);
                        ListarTipoComprobanteActivity.ListViewAdapterTipoComprobante lvc = new ListarTipoComprobanteActivity.ListViewAdapterTipoComprobante(getApplicationContext());
                        listaTC.setAdapter(lvc);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
            {
                Toast.makeText(getApplicationContext(),"No se pudo registrar ",Toast.LENGTH_LONG).show();
            }
        });
    }

    public class ListViewAdapterTipoComprobante extends BaseAdapter {
        Context context;
        LayoutInflater inflater;

        TextView txtIdTC,txtNomTC,txtDescTC;
        public ListViewAdapterTipoComprobante(Context applicationContext)
        {
            this.context = applicationContext;
        }


        @Override
        public int getCount() {
            return idTC_array.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.tipocomprobante_item,null);

            txtIdTC = (TextView) itemView.findViewById(R.id.txtidTC);
            txtNomTC = (TextView) itemView.findViewById(R.id.txtnomComprobante);
            txtDescTC = (TextView) itemView.findViewById(R.id.txtdescTC);

            txtIdTC.setText(idTC_array.get(i).toString());
            txtNomTC.setText(nomTC_array.get(i).toString());
            txtDescTC.setText(descTC_array.get(i).toString());

            return itemView;
        }
    }

    @Override
    public void onClick(View view) {
        if(view==nuevoTC) //si presionas el boton nuevo
        {
            //enviar al activiy ClienteRegistrar
            Intent intent = new Intent(this,TipoComprobanteActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == listaTC) {
            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.getMenuInflater().inflate(R.menu.opciones, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    TextView tvId = (TextView) view.findViewById(R.id.txtidTC);
                    String id = tvId.getText().toString();

                    if (menuItem.getItemId() == R.id.opc_editar) {
                        Editar(id);
                    } else if (menuItem.getItemId() == R.id.opc_eliminar) {
                        Eliminar(id);
                    }


                    return false;
                }
            });
            popupMenu.show();
    }


}

    private void Eliminar(String id) {
        String url = servidor + "tipocomprobante_eliminar.php";

        RequestParams params = new RequestParams();
        params.put("id",id);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode==200) //proceso correctamente
                {
                    String res = new String(responseBody);
                    Toast.makeText(getApplicationContext(),"Eliminado correctamente "+res,Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getApplicationContext(),ListarTipoComprobanteActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(),"No se pudo eliminar ",Toast.LENGTH_LONG).show();

            }
        });
    }

    private void Editar(String id) {
        tc.setId(id);

        Intent intent = new Intent(getApplicationContext(),TipoComprobanteEditar.class);
        Bundle b = new Bundle();
        b.putSerializable("id",tc);
        intent.putExtras(b);
        startActivity(intent);
    }
}