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

public class PasarDetailActivity extends AppCompatActivity {

    ImageView imgPasar;
    TextView txtNama,txtDesc,txtAlamat,txtOpen;
    Button btUpdate,btDirect;
    String AlamKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_pasar);

        //FloatingActionButton fab = findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext(),Home.class);
                //startActivity(intent);
            //}
        //});

        //ini views
        imgPasar = findViewById(R.id.detail_image);
        txtNama = findViewById(R.id.detail_nama);
        txtDesc = findViewById(R.id.detail_desc);
        txtAlamat = findViewById(R.id.detail_alamat);
        txtOpen = findViewById(R.id.detail_open);
        btDirect = findViewById(R.id.btDirect);

        String PasarKey = getIntent().getExtras().getString("pasarKey");


        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //now it can get alam data

        String pasarImage = getIntent().getExtras().getString("pasarImage");
        Glide.with(this).load(pasarImage).into(imgPasar);

        String nama = getIntent().getExtras().getString("nama");
        txtNama.setText(nama);

        String desc = getIntent().getExtras().getString("description");
        txtDesc.setText(desc);

        String alamat = getIntent().getExtras().getString("alamat");
        txtAlamat.setText(alamat);

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

