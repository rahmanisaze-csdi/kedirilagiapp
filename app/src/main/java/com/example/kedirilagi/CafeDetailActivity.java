package com.example.kedirilagi;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class CafeDetailActivity extends AppCompatActivity {

    ImageView imgCafe,imgInstagram;
    TextView txtNama,txtDesc,txtAlamat,txtHarga,txtInstagram,txtOpen;
    Button btUpdate,btDirect;
    String AlamKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_cafe);

        //FloatingActionButton fab = findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext(),Home.class);
                //startActivity(intent);
            //}
        //});

        //ini views
        imgCafe = findViewById(R.id.detail_image);
        imgInstagram = findViewById(R.id.imgInstagram);
        txtNama = findViewById(R.id.detail_nama);
        txtDesc = findViewById(R.id.detail_desc);
        txtAlamat = findViewById(R.id.detail_alamat);
        txtHarga = findViewById(R.id.detail_harga);
        txtOpen = findViewById(R.id.detail_open);
        txtInstagram = findViewById(R.id.detail_instagram);
        btDirect = findViewById(R.id.btDirect);

        String CafeKey = getIntent().getExtras().getString("cafeKey");


        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //now it can get alam data

        String cafeImage = getIntent().getExtras().getString("cafeImage");
        Glide.with(this).load(cafeImage).into(imgCafe);

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

        String instagram = getIntent().getExtras().getString("instagram");
        txtInstagram.setText(instagram);

        txtInstagram.setPaintFlags(txtInstagram.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        btDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = getIntent().getExtras().getString("nama");
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+nama+"");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                startActivity(mapIntent);
            }
        });

        txtInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sApplink = getIntent().getExtras().getString("igLink");
                if (sApplink!=null){
                    String sPackage = "com.instagram.android";
                    openLink(sApplink,sPackage,sApplink);
                }else {
                    Toast.makeText(CafeDetailActivity.this, "Instagram not available", Toast.LENGTH_SHORT).show();
                }
            }

            private void openLink(String sApplink, String sPackage, String sApplink1) {
                try {
                    Uri uri = Uri.parse(sApplink);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    intent.setPackage(sPackage);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }catch (ActivityNotFoundException activityNotFoundException){
                    Uri uri = Uri.parse(sApplink);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });



    }


}

