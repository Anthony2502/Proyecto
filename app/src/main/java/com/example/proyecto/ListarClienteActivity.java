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

public class ListarClienteActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    ListView listaC;
    Button nuevo;

    ArrayList id_array = new ArrayList();
    ArrayList nom_array = new ArrayList();
    ArrayList ape_array = new ArrayList();
    ArrayList dni_array = new ArrayList();
    ArrayList cel_array = new ArrayList();
    ArrayList email_array = new ArrayList();
    ArrayList dir_array = new ArrayList();

    Cliente c = null;
    final String servidor = "http://10.0.2.2/t2app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_cliente);

        listaC = (ListView) findViewById(R.id.lvListaC);
        listaC.setOnItemClickListener(this);
        nuevo = (Button) findViewById(R.id.btnNuevoC);
        nuevo.setOnClickListener(this);

        c = new Cliente();

        ListarCliente();
    }

    private void ListarCliente()
    {
        id_array.clear();
        nom_array.clear();
        ape_array.clear();
        dni_array.clear();
        cel_array.clear();
        email_array.clear();
        dir_array.clear();

        String url = servidor + "cliente_listar.php";

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
                            id_array.add(String.valueOf(js.getJSONObject(i).getInt("id_cliente")));
                            nom_array.add(js.getJSONObject(i).getString("nom_cliente"));
                            ape_array.add(js.getJSONObject(i).getString("ape_cliente"));
                            dni_array.add(js.getJSONObject(i).getString("dni_cliente"));
                            cel_array.add(js.getJSONObject(i).getString("cel_cliente"));
                            email_array.add(js.getJSONObject(i).getString("email_cliente"));
                            dir_array.add(js.getJSONObject(i).getString("dir_cliente"));
                        }

                        listaC.setAdapter(null);
                        ListViewAdapterCliente lvc = new ListViewAdapterCliente(getApplicationContext());
                        listaC.setAdapter(lvc);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(),"No se pudo registrar ",Toast.LENGTH_LONG).show();
            }
        });
    }

    private class ListViewAdapterCliente extends BaseAdapter {
        Context context;
        LayoutInflater inflater;

        TextView txtId,txtNom,txtApe,txtDni,txtCel,txtEmail,txtDir;
        public ListViewAdapterCliente(Context applicationContext) {
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
            ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.clientes_item,null);

            txtId = (TextView) itemView.findViewById(R.id.txtidC);
            txtNom = (TextView) itemView.findViewById(R.id.txtNomC);
            txtApe = (TextView) itemView.findViewById(R.id.txtApeC);
            txtDni = (TextView) itemView.findViewById(R.id.txtdniC);
            txtCel = (TextView) itemView.findViewById(R.id.txtcelC);
            txtEmail = (TextView) itemView.findViewById(R.id.txtemailC);
            txtDir = (TextView) itemView.findViewById(R.id.txtdirC);

            txtId.setText(id_array.get(i).toString());
            txtNom.setText(nom_array.get(i).toString());
            txtApe.setText(ape_array.get(i).toString());
            txtDni.setText(dni_array.get(i).toString());
            txtCel.setText(cel_array.get(i).toString());
            txtEmail.setText(email_array.get(i).toString());
            txtDir.setText(dir_array.get(i).toString());

            return itemView;
        }
    }

    @Override
    public void onClick(View view)
    {
        if(view==nuevo) //si presionas el boton nuevo
        {
            //enviar al activiy ClienteRegistrar
            Intent intent = new Intent(this,ClienteActivity.class);
            startActivity(intent);

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == listaC)
        {
            PopupMenu popupMenu = new PopupMenu(this,view);
            popupMenu.getMenuInflater().inflate(R.menu.opciones, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    TextView tvId = (TextView) view.findViewById(R.id.txtidC);
                    String id = tvId.getText().toString();

                    if(menuItem.getItemId() == R.id.opc_editar)
                    {
                        Editar(id);
                    }
                    else if(menuItem.getItemId() == R.id.opc_eliminar)
                    {
                        Eliminar(id);
                    }


                    return false;
                }
            });
            popupMenu.show();
        }
    }

    private void Eliminar(String id) {
        String url = servidor + "cliente_eliminar.php";

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

                    Intent intent = new Intent(getApplicationContext(),ListarClienteActivity.class);
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
        c.setId(id);

        Intent intent = new Intent(getApplicationContext(),ClienteEditar.class);
        Bundle b = new Bundle();
        b.putSerializable("id",c);
        intent.putExtras(b);
        startActivity(intent);
    }


}