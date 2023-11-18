package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity implements View.OnClickListener {

    Button ven,com,prod,cli,prov,mp,tc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ven = (Button) findViewById(R.id.btnVentasMenu);
        ven.setOnClickListener(this);
        com = (Button) findViewById(R.id.btnComprasMenu);
        com.setOnClickListener(this);
        prod = (Button) findViewById(R.id.btnProductosMenu);
        prod.setOnClickListener(this);
        cli = (Button) findViewById(R.id.btnClientesMenu);
        cli.setOnClickListener(this);
        prov = (Button) findViewById(R.id.btnProveedoresMenu);
        prov.setOnClickListener(this);
        mp = (Button) findViewById(R.id.btnMediopagosMenu);
        mp.setOnClickListener(this);
        tc = (Button) findViewById(R.id.btnTipocomprobanteMenu);
        tc.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view==ven)
        {
            Intent intent = new Intent(this, CarritoVenta.class);
            startActivity(intent);
        }
        else if (view==com)
        {
            Intent intent = new Intent(this, CarritoCompra.class);
            startActivity(intent);
        }
        else if (view==prod)
        {
            Intent intent = new Intent(this, ListarProductoActivity.class);
            startActivity(intent);
        }
        else if (view==cli)
        {
            Intent intent = new Intent(this, ListarClienteActivity.class);
            startActivity(intent);
        }
        else if (view==prov)
        {
            Intent intent = new Intent(this, ListarProveedorActivity.class);
            startActivity(intent);
        }
        else if (view==mp)
        {
            Intent intent = new Intent(this, ListarMedioPagoActivity.class);
            startActivity(intent);
        }
        else if (view==tc)
        {
            Intent intent = new Intent(this, ListarTipoComprobanteActivity.class);
            startActivity(intent);
        }
    }
}