package com.example.kedirilagi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class KulinerDetailActivity extends AppCompatActivity {

    ImageView imgKuliner;
    TextView txtNama,txtDesc,txtAlamat,txtHarga,txtOpen;
    Button btUpdate,btDirect;
    String AlamKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_kuliner);

        //FloatingActionButton fab = findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext(),Home.class);
                //startActivity(intent);
            //}
        //});

        //ini views
        imgKuliner = findViewById(R.id.detail_image);
        txtNama = findViewById(R.id.detail_nama);
        txtDesc = findViewById(R.id.detail_desc);
        txtAlamat = findViewById(R.id.detail_alamat);
        txtHarga = findViewById(R.id.detail_harga);
        txtOpen = findViewById(R.id.detail_open);
        btDirect = findViewById(R.id.btDirect);

        String KulinerKey = getIntent().getExtras().getString("kulinerKey");


        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //now it can get alam data

        String kulinerImage = getIntent().getExtras().getString("kulinerImage");
        Glide.with(this).load(kulinerImage).into(imgKuliner);

        String nama = getIntent().getExtras().getString("nama");
        txtNama.setText(nama);

        String desc = getIntent().getExtras().getString("description");
        txtDesc.setText(desc);

        String alamat = getIntent().getExtras().getString("alamat");
        txtAlamat.setText(alamat);

        String harga = getIntent().getExtras().getString("harga") ;
        txtHarga.setText(harga);

        String open = getIntent().getExtras().getString("open");
        txtOpen.setText(open);

        btDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = getIntent().getExtras().getString("nama");
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+nama+"");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                startActivity(mapIntent);
            }
        });




    }


}

