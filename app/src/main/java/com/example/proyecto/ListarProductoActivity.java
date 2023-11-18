package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ListarProductoActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    ListView lista;
    Button nuevo,carv,carc;

    final String servidor = "http://10.0.2.2/t2app/";

    ArrayList id_array = new ArrayList();
    ArrayList nom_array = new ArrayList();
    ArrayList pv_array = new ArrayList();
    ArrayList pc_array = new ArrayList();
    ArrayList des_array = new ArrayList();

    Producto c = null;
    static ArrayList<Carrito> carrito_ventas = new ArrayList<>();
    static ArrayList<Carrito> carrito_compras = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_producto);

        lista = (ListView) findViewById(R.id.lvListaP);
        lista.setOnItemClickListener(this);
        nuevo = (Button) findViewById(R.id.btnNuevoP);
        nuevo.setOnClickListener(this);
        carv = (Button) findViewById(R.id.btnCarritoV);
        carv.setOnClickListener(this);
        carc = (Button) findViewById(R.id.btnCarritoC);
        carc.setOnClickListener(this);

        c = new Producto();

        ListarProducto();

    }

    private void ListarProducto() {

        id_array.clear();
        nom_array.clear();
        pc_array.clear();
        pv_array.clear();
        des_array.clear();

        String url = servidor + "producto_listar.php";

        RequestParams params = new RequestParams();

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode==200) //procesado exitosamente
                {
                    try
                    {
                        JSONArray js = new JSONArray(new String(responseBody));
                        for (int i=0; i < js.length();i++)
                        {
                            id_array.add(String.valueOf(js.getJSONObject(i).getInt("id_producto")));
                            nom_array.add(js.getJSONObject(i).getString("nom_producto"));
                            pv_array.add(String.valueOf(js.getJSONObject(i).getDouble("preciov_producto")));
                            pc_array.add(String.valueOf(js.getJSONObject(i).getDouble("precioc_producto")));
                            des_array.add(js.getJSONObject(i).getString("desc_producto"));
                        }

                        lista.setAdapter(null);
                        ListViewAdapterProducto lvc = new ListViewAdapterProducto(getApplicationContext());
                        lista.setAdapter(lvc);

                    }
                    catch (JSONException e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private class ListViewAdapterProducto extends BaseAdapter
    {
        Context context;
        LayoutInflater inflater;
        TextView txt_id,txt_nom,txt_pv,txt_pc,txt_des;

        public ListViewAdapterProducto(Context context)
        {
            this.context = context;
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
            ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.producto_row,null);

            txt_id = (TextView)  itemView.findViewById(R.id.txtIdP);
            txt_nom = (TextView)  itemView.findViewById(R.id.txtNombreP);
            txt_pv = (TextView)  itemView.findViewById(R.id.txtPreciovP);
            txt_pc = (TextView)  itemView.findViewById(R.id.txtPreciocP);
            txt_des = (TextView)  itemView.findViewById(R.id.txtDesP);


            txt_id.setText(id_array.get(i).toString());
            txt_nom.setText(nom_array.get(i).toString());
            txt_pv.setText(pv_array.get(i).toString());
            txt_pc.setText(pc_array.get(i).toString());
            txt_des.setText(des_array.get(i).toString());


            return itemView;
        }
    }

    @Override
    public void onClick(View view) {
        if (view==nuevo)
        {

            //redirigirte al activity nuevo cliente
            Intent intent = new Intent(this, ProductoActivity.class);
            startActivity(intent);

        }
        else if (view==carv)
        {
            Intent intent = new Intent(this, CarritoVenta.class);
            startActivity(intent);
        }
        else if (view==carc)
        {
            Intent intent = new Intent(this, CarritoCompra.class);
            startActivity(intent);
        }


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        //Cuando presione un item de la lista
        if (adapterView == lista)
        {
            android.widget.PopupMenu popupMenu = new android.widget.PopupMenu(this,view);
            popupMenu.getMenuInflater().inflate(R.menu.opciones_producto, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    TextView tvId = (TextView) view.findViewById(R.id.txtIdP);
                    String idp = tvId.getText().toString();

                    TextView tvNom = (TextView) view.findViewById(R.id.txtNombreP);
                    String nomp = tvNom.getText().toString();

                    TextView tvPrev = (TextView) view.findViewById(R.id.txtPreciovP);
                    Double precvp = Double.parseDouble(tvPrev.getText().toString());

                    TextView tvPrec = (TextView) view.findViewById(R.id.txtPreciocP);
                    Double precvc = Double.parseDouble(tvPrec.getText().toString());


                    if (menuItem.getItemId() == R.id.opc_editar)
                    {
                        EditarProducto(idp);
                    }
                    else if (menuItem.getItemId() == R.id.opc_eliminar)
                    {
                        EliminarProducto(idp);
                    }
                    else if (menuItem.getItemId() == R.id.opc_ventas)
                    {
                        AgregarCarritoVentas(idp,nomp,precvp);
                    }
                    else if (menuItem.getItemId() == R.id.opc_compras)
                    {
                        AgregarCarritoCompras(idp,nomp,precvc);
                    }


                    return false;
                }
            });
            popupMenu.show();
        }
    }

    private void AgregarCarritoCompras(String idp, String nomp, Double precvc) {

        /*Carrito car = new Carrito();
        car.setId(Integer.parseInt(idp));
        car.setNombre(nomp);
        car.setPrecio(precvc);
        car.setCantidad(1.0);
        car.setTotal(precvc);

        carrito_compras.add(car);*/


        int n = 0;
        int p = 0;
        int pos = 0;
        for (Carrito carritoCompra : carrito_compras) {
            if(carritoCompra.getId() == Integer.parseInt(idp))
            {
                n++;
                pos = p;
            }
            p++;
        }

        if (n==0)
        {
            Carrito car = new Carrito();
            car.setId(Integer.parseInt(idp));
            car.setNombre(nomp);
            car.setPrecio(precvc);
            car.setCantidad(1.0);
            car.setTotal(precvc);

            carrito_compras.add(car);
        }
        else
        {
            Double cant_actual = carrito_compras.get(pos).getCantidad();
            Double pre_actual = carrito_compras.get(pos).getPrecio();

            carrito_compras.get(pos).setCantidad(cant_actual+1.0);
            carrito_compras.get(pos).setTotal(pre_actual*(cant_actual+1.0));
        }

        Toast.makeText(getApplicationContext(),"Se agregó a carrito de compras",Toast.LENGTH_LONG).show();
    }

    private void AgregarCarritoVentas(String idp, String nomp, Double precvp) {

        int n = 0;
        int p = 0;
        int pos = 0;
        for (Carrito carritoVenta : carrito_ventas) {
            if(carritoVenta.getId() == Integer.parseInt(idp))
            {
                n++;
                pos = p;
            }
            p++;
        }

        if (n==0)
        {
            Carrito car = new Carrito();
            car.setId(Integer.parseInt(idp));
            car.setNombre(nomp);
            car.setPrecio(precvp);
            car.setCantidad(1.0);
            car.setTotal(precvp);

            carrito_ventas.add(car);
        }
        else
        {
            Double cant_actual = carrito_ventas.get(pos).getCantidad();
            Double pre_actual = carrito_ventas.get(pos).getPrecio();

            carrito_ventas.get(pos).setCantidad(cant_actual+1.0);
            carrito_ventas.get(pos).setTotal(pre_actual*(cant_actual+1.0));
        }


        Toast.makeText(getApplicationContext(),"Se agregó a carrito de ventas",Toast.LENGTH_LONG).show();

    }


    private void EliminarProducto(String id) {

        String url = servidor + "producto_eliminar.php";

        RequestParams params = new RequestParams();
        params.put("id",id);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode==200) //procesado exitosamente
                {
                    //Respuesta que viene desde el archivo php
                    String res = new String(responseBody);
                    //Para mostrar un mensaje en pantalla de la app
                    Toast.makeText(getApplicationContext(),"Eliminado correctamente "+res,Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getApplicationContext(), ListarProductoActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(),"No se pudo eliminar ",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void EditarProducto(String id)
    {

        c.setId(id);
        Intent intent = new Intent(getApplicationContext(),ProductoEditar.class);
        Bundle b = new Bundle();
        b.putSerializable("id",c);
        intent.putExtras(b);
        startActivity(intent);

    }
}