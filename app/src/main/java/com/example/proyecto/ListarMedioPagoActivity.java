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

public class ListarMedioPagoActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    ListView listaMP;
    Button nuevoMP;

    ArrayList idMP_array = new ArrayList();
    ArrayList nomMP_array = new ArrayList();
    ArrayList descMP_array = new ArrayList();

    MedioPago mp = null;

    final String servidor = "http://10.0.2.2/t2app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_medio_pago);
        listaMP = (ListView) findViewById(R.id.lvMP);
        listaMP.setOnItemClickListener(this);
        nuevoMP = (Button) findViewById(R.id.btnNuevoMP);
        nuevoMP.setOnClickListener(this);

        mp = new MedioPago();

        ListarMedioPago();
    }

    private void ListarMedioPago()
    {
        idMP_array.clear();
        nomMP_array.clear();
        descMP_array.clear();

        String url = servidor + "mediopago_listar.php";
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
                            idMP_array.add(String.valueOf(js.getJSONObject(i).getInt("id_mediopago")));
                            nomMP_array.add(js.getJSONObject(i).getString("nom_mediopago"));
                            descMP_array.add(js.getJSONObject(i).getString("desc_mediopago"));
                        }
                        listaMP.setAdapter(null);
                        ListarMedioPagoActivity.ListViewAdapterMedioPago lvc = new ListarMedioPagoActivity.ListViewAdapterMedioPago(getApplicationContext());
                        listaMP.setAdapter(lvc);
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

    public class ListViewAdapterMedioPago extends BaseAdapter
    {
        Context context;
        LayoutInflater inflater;

        TextView txtIdMP,txtNomMP,txtDescMP;

        public ListViewAdapterMedioPago(Context applicationContext)
        {
            this.context = applicationContext;
        }

        @Override
        public int getCount() {
            return idMP_array.size();
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
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.mediopago_item,null);

            txtIdMP = (TextView) itemView.findViewById(R.id.txtidMP);
            txtNomMP = (TextView) itemView.findViewById(R.id.txtnomMP);
            txtDescMP = (TextView) itemView.findViewById(R.id.txtdescMP);


            txtIdMP.setText(idMP_array.get(i).toString());
            txtNomMP.setText(nomMP_array.get(i).toString());
            txtDescMP.setText(descMP_array.get(i).toString());

            return itemView;
        }
    }

    @Override
    public void onClick(View view)
    {
        if(view==nuevoMP) //si presionas el boton nuevo
        {
            //enviar al activiy ClienteRegistrar
            Intent intent = new Intent(this,MedioPagoActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        if (adapterView == listaMP) {
            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.getMenuInflater().inflate(R.menu.opciones, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    TextView tvId = (TextView) view.findViewById(R.id.txtidMP);
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
        String url = servidor + "mediopago_eliminar.php";

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

                    Intent intent = new Intent(getApplicationContext(),ListarMedioPagoActivity.class);
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
        mp.setId(id);

        Intent intent = new Intent(getApplicationContext(),MedioPagoEditar.class);
        Bundle b = new Bundle();
        b.putSerializable("id",mp);
        intent.putExtras(b);
        startActivity(intent);
    }


}