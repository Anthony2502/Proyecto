package com.example.proyecto;

import static com.example.proyecto.ListarProductoActivity.carrito_compras;
import static com.example.proyecto.ListarProductoActivity.carrito_ventas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class CarritoCompra extends AppCompatActivity {
    final String servidor = "http://10.0.2.2/t2app/";
    Spinner prov,mp_compra,tc_compra;
    ListView lista_compra;
    TextView tot_compra;
    Button guardar,cancelar;

    ArrayList idprov_array = new ArrayList();
    ArrayList razprov_array = new ArrayList();
    ArrayList idmp_array = new ArrayList();
    ArrayList nomp_array = new ArrayList();
    ArrayList idtc_array = new ArrayList();
    ArrayList nomtc_array = new ArrayList();

    //carrito_row → arrays
    ArrayList id_car_array = new ArrayList();
    ArrayList nom_car_array = new ArrayList();
    ArrayList pre_car_array = new ArrayList();
    ArrayList cant_car_array = new ArrayList();
    ArrayList tot_car_array = new ArrayList();
    public Double total = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito_compra);

        prov = (Spinner) findViewById(R.id.spProvC);
        mp_compra = (Spinner) findViewById(R.id.spMP_PvC);
        tc_compra = (Spinner) findViewById(R.id.spTP_PvC);
        lista_compra = (ListView) findViewById(R.id.lvCarritoC);
        tot_compra = (TextView) findViewById(R.id.txtTotalC);
        guardar = (Button) findViewById(R.id.btnGuardarC);
        cancelar = (Button) findViewById(R.id.btnCancelarC);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        SpinnerProveedor();
        SpinnerMedioPago();
        SpinnerTipoComprobante();
        ListarCarritoCompra();

    }


    private void SpinnerTipoComprobante() {
        idtc_array.clear();
        nomtc_array.clear();
        String url = servidor + "tipocomprobante_listar.php";
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
                            idtc_array.add(String.valueOf(js.getJSONObject(i).getInt("id_tipocomprobante")));
                            nomtc_array.add(js.getJSONObject(i).getString("nom_tipocomprobante"));
                        }

                        tc_compra.setAdapter(null);
                        SpinnerAdapterTipoComprobante lv = new SpinnerAdapterTipoComprobante(getApplicationContext());
                        tc_compra.setAdapter(lv);

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



    private class SpinnerAdapterTipoComprobante extends BaseAdapter {
        Context context;
        LayoutInflater inflater;
        TextView txt_idtc,txt_nomtc;

        public SpinnerAdapterTipoComprobante(Context context)
        {
            this.context = context;
        }

        @Override
        public int getCount() {
            return idtc_array.size();
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
            ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.spinner_row,null);

            txt_idtc = (TextView)  itemView.findViewById(R.id.txtIdSpinner);
            txt_nomtc = (TextView)  itemView.findViewById(R.id.txtNomSpinner);

            txt_idtc.setText(idtc_array.get(i).toString());
            txt_nomtc.setText(nomtc_array.get(i).toString());


            return itemView;
        }
    }

    private void SpinnerMedioPago() {
        idmp_array.clear();
        nomp_array.clear();
        String url = servidor + "mediopago_listar.php";

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
                            idmp_array.add(String.valueOf(js.getJSONObject(i).getInt("id_mediopago")));
                            nomp_array.add(js.getJSONObject(i).getString("nom_mediopago"));
                        }

                        mp_compra.setAdapter(null);
                        SpinnerAdapterMedioPago lv = new SpinnerAdapterMedioPago(getApplicationContext());
                        mp_compra.setAdapter(lv);

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

    private class SpinnerAdapterMedioPago extends BaseAdapter{
        Context context;
        LayoutInflater inflater;
        TextView txt_idmp,txt_nomp;

        public SpinnerAdapterMedioPago(Context context)
        {
            this.context = context;
        }

        @Override
        public int getCount() {
            return idmp_array.size();
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
            ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.spinner_row,null);

            txt_idmp = (TextView)  itemView.findViewById(R.id.txtIdSpinner);
            txt_nomp = (TextView)  itemView.findViewById(R.id.txtNomSpinner);

            txt_idmp.setText(idmp_array.get(i).toString());
            txt_nomp.setText(nomp_array.get(i).toString());


            return itemView;
        }
    }


    private void SpinnerProveedor() {
        idprov_array.clear();
        razprov_array.clear();

        String url = servidor + "proveedor_listar.php";

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
                            idprov_array.add(String.valueOf(js.getJSONObject(i).getInt("id_proveedor")));
                            razprov_array.add(js.getJSONObject(i).getString("razon_social"));
                        }

                        prov.setAdapter(null);
                        SpinnerAdapterProveedor lvc = new SpinnerAdapterProveedor(getApplicationContext());
                        prov.setAdapter(lvc);

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

    private class SpinnerAdapterProveedor extends BaseAdapter {
        Context context;
        LayoutInflater inflater;
        TextView txt_id,txt_raz;

        public SpinnerAdapterProveedor(Context context)
        {
            this.context = context;
        }

        @Override
        public int getCount() {
            return idprov_array.size();
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
            ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.spinner_row,null);

            txt_id = (TextView)  itemView.findViewById(R.id.txtIdSpinner);
            txt_raz = (TextView)  itemView.findViewById(R.id.txtNomSpinner);

            txt_id.setText(idprov_array.get(i).toString());
            txt_raz.setText(razprov_array.get(i).toString());


            return itemView;
        }

    }
    private void ListarCarritoCompra() {
        id_car_array.clear();
        nom_car_array.clear();
        pre_car_array.clear();
        cant_car_array.clear();
        tot_car_array.clear();

        for (Carrito carritoCompra : carrito_compras) {
            id_car_array.add(String.valueOf(carritoCompra.getId()));
            nom_car_array.add(carritoCompra.getNombre());
            pre_car_array.add(String.valueOf(carritoCompra.getPrecio()));
            cant_car_array.add(String.valueOf(carritoCompra.getCantidad()));
            tot_car_array.add(String.valueOf(carritoCompra.getTotal()));
            total+=carritoCompra.getTotal();
        }

        tot_compra.setText(String.valueOf(total));

        lista_compra.setAdapter(null);
        ListViewAdapterCarrito lvc = new ListViewAdapterCarrito(getApplicationContext());
        lista_compra.setAdapter(lvc);
    }


    private class ListViewAdapterCarrito extends BaseAdapter{
        Context context;
        LayoutInflater inflater;
        TextView txt_id, txt_nom, txt_pre, txt_cant, txt_tot;

        public ListViewAdapterCarrito(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return id_car_array.size();
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
            ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.carrito_row, null);

            txt_id = (TextView) itemView.findViewById(R.id.txtIdCar);
            txt_nom = (TextView) itemView.findViewById(R.id.txtNombreCar);
            txt_pre = (TextView) itemView.findViewById(R.id.txtPrecioCar);
            txt_cant = (TextView) itemView.findViewById(R.id.txtCantidadCar);
            txt_tot = (TextView) itemView.findViewById(R.id.txtTotalCar);

            txt_id.setText(id_car_array.get(i).toString());
            txt_nom.setText(nom_car_array.get(i).toString());
            txt_pre.setText(pre_car_array.get(i).toString());
            txt_cant.setText(cant_car_array.get(i).toString());
            txt_tot.setText(tot_car_array.get(i).toString());

            return itemView;
        }
    }



}