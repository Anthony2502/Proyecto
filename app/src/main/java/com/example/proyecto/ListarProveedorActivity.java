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

public class ListarProveedorActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    ListView listaProv;
    Button nuevoProv;

    ArrayList id_array = new ArrayList();
    ArrayList rsocial_array = new ArrayList();
    ArrayList ruc_array = new ArrayList();    
    ArrayList cel_array = new ArrayList();
    ArrayList email_array = new ArrayList();
    ArrayList dir_array = new ArrayList();

    Proveedor prov = null;
    final String servidor = "http://10.0.2.2/t2app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_proveedor);

        listaProv = (ListView) findViewById(R.id.lvProveedor);
        listaProv.setOnItemClickListener(this);
        nuevoProv = (Button) findViewById(R.id.btnNuevoProv);
        nuevoProv.setOnClickListener(this);

        prov = new Proveedor();

        ListarProveedor();
    }

    private void ListarProveedor()
    {
        id_array.clear();
        rsocial_array.clear();
        ruc_array.clear();
        cel_array.clear();
        email_array.clear();
        dir_array.clear();

        String url = servidor + "proveedor_listar.php";

        RequestParams params = new RequestParams();
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {
                if(statusCode==200) //proceso correctamente
                {
                    try
                    {
                        JSONArray js = new JSONArray(new String(responseBody));
                        for (int i=0; i < js.length();i++)
                        {
                            id_array.add(String.valueOf(js.getJSONObject(i).getInt("id_proveedor")));
                            rsocial_array.add(js.getJSONObject(i).getString("razon_social"));
                            ruc_array.add(js.getJSONObject(i).getString("ruc_proveedor"));
                            cel_array.add(js.getJSONObject(i).getString("celular_proveedor"));
                            email_array.add(js.getJSONObject(i).getString("email_proveedor"));
                            dir_array.add(js.getJSONObject(i).getString("dir_proveedor"));
                        }
                        listaProv.setAdapter(null);
                        ListarProveedorActivity.ListViewAdapterProveedor lvc = new ListarProveedorActivity.ListViewAdapterProveedor(getApplicationContext());
                        listaProv.setAdapter(lvc);
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

    public class ListViewAdapterProveedor extends BaseAdapter {
        Context context;
        LayoutInflater inflater;

        TextView txtId,txtRazon,txtRuc,txtCel,txtEmail,txtDir;
        public ListViewAdapterProveedor(Context applicationContext)
        {
            this.context = applicationContext;
        }

        @Override
        public int getCount() {
            return id_array.size();
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
            ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.proveedor_item,null);

            txtId = (TextView) itemView.findViewById(R.id.txtidProv);
            txtRazon = (TextView) itemView.findViewById(R.id.txtrazonProv);
            txtRuc = (TextView) itemView.findViewById(R.id.txtrucProv);
            txtCel = (TextView) itemView.findViewById(R.id.txtcelProv);
            txtEmail = (TextView) itemView.findViewById(R.id.txtemailProv);
            txtDir = (TextView) itemView.findViewById(R.id.txtdirProv);

            txtId.setText(id_array.get(i).toString());
            txtRazon.setText(rsocial_array.get(i).toString());
            txtRuc.setText(ruc_array.get(i).toString());
            txtCel.setText(cel_array.get(i).toString());
            txtEmail.setText(email_array.get(i).toString());
            txtDir.setText(dir_array.get(i).toString());
            return itemView;
        }
    }

    @Override
    public void onClick(View view)
    {
        if(view==nuevoProv) //si presionas el boton nuevo
        {
            //enviar al activiy ClienteRegistrar
            Intent intent = new Intent(this,ProveedorActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == listaProv) {
            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.getMenuInflater().inflate(R.menu.opciones, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    TextView tvId = (TextView) view.findViewById(R.id.txtidProv);
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
        String url = servidor + "proveedor_eliminar.php";

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

                    Intent intent = new Intent(getApplicationContext(),ListarProveedorActivity.class);
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
        prov.setId(id);

        Intent intent = new Intent(getApplicationContext(),ProveedorEditar.class);
        Bundle b = new Bundle();
        b.putSerializable("id",prov);
        intent.putExtras(b);
        startActivity(intent);
    }


}