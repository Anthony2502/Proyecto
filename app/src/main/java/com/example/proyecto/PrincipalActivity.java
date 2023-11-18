package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
    }

    public void btnRegistrarListarTipoComprobante(View view){
        Intent btnRegistrarListarTipoComprobante = new Intent(this,ListarTipoComprobanteActivity.class);
        startActivity(btnRegistrarListarTipoComprobante);
    }
    public void btnRegistrarListarMedioPago(View view){
        Intent btnRegistrarListarMedioPago = new Intent(this,ListarMedioPagoActivity.class);
        startActivity(btnRegistrarListarMedioPago);
    }
    public void btnRegistrarListarProveedor(View view){
        Intent btnRegistrarListarProveedor = new Intent(this,ListarProveedorActivity.class);
        startActivity(btnRegistrarListarProveedor);
    }
    public void btnRegistrarListarProducto(View view){
        Intent btnRegistrarListarProducto = new Intent(this,ListarProductoActivity.class);
        startActivity(btnRegistrarListarProducto);
    }

    public void btnRegistrarListarCliente(View view){
        Intent btnRegistrarListarCliente = new Intent(this,ListarClienteActivity.class);
        startActivity(btnRegistrarListarCliente);
    }

    public void btnRegistrarCliente(View view){
        Intent btnRegistrarCliente = new Intent(this,ClienteActivity.class);
        startActivity(btnRegistrarCliente);
    }
    public void btnRegistrarProducto(View view){
        Intent btnRegistrarProducto = new Intent(this,ProductoActivity.class);
        startActivity(btnRegistrarProducto);
    }
    public void btnRegistrarProveedor(View view){
        Intent btnRegistrarProveedor = new Intent(this,ProveedorActivity.class);
        startActivity(btnRegistrarProveedor);
    }
    public void btnRegistrarMedioPago(View view){
        Intent btnRegistrarMedioPago = new Intent(this,MedioPagoActivity.class);
        startActivity(btnRegistrarMedioPago);
    }
    public void btnRegistrarTipoComprobante(View view){
        Intent btnRegistrarTipoComprobante = new Intent(this,TipoComprobanteActivity.class);
        startActivity(btnRegistrarTipoComprobante);
    }

}