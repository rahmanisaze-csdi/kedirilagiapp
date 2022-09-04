package com.example.kedirilagi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ItineraryListAdapterNearby extends RecyclerView.Adapter<ItineraryListAdapterNearby.MyViewHolder> {

    private Context context;
    private List<ItineraryModel> itineraryModelList;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean check = false;
    int currentPosition = -1;
    int positions = 0;
    private int mCounter = 0;

    public ItineraryListAdapterNearby(Context context, List<ItineraryModel> itineraryModelList) {
        this.context = context;
        this.itineraryModelList = itineraryModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itinerary_list,parent,false);
        return new MyViewHolder(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        sharedPreferences = context.getSharedPreferences("pref",0);
        editor = sharedPreferences.edit();

        String nama = itineraryModelList.get(position).getNama();

        holder.objectTitle.setText(itineraryModelList.get(position).getNama());
        holder.objectOpen.setText(itineraryModelList.get(position).getOpen());
        holder.duration.setText(String.valueOf(itineraryModelList.get(position).getDuration()));
        Glide.with(context).load(itineraryModelList.get(position).getPicture()).into(holder.objectImage);
        //if (mCounter<itineraryModelList.size()){
        // mCounter +=1;
        //holder.number.setText(mCounter + "");
        //}

        holder.number.setText(""+ (position+1));

        holder.number.setTag(1);

        holder.directBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+nama+"");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                context.startActivity(mapIntent);
            }


        });

        //Hitung jarak
        GetLocation getLocation = new GetLocation(context);
        Location location = getLocation.getLocation();
        if (location != null) {
            double latUser = location.getLatitude();
            double longUser = location.getLongitude();
            double latDestination = Double.valueOf(itineraryModelList.get(position).getLatitude());
            double longDestination = Double.valueOf(itineraryModelList.get(position).getLongitude());
            double distance = getDistance(latDestination, longDestination,latUser, longUser);
            holder.objectJarak.setText(String.format("%.2f KM",distance));

            //Hitung waktu untuk Mobil
            final int kecepatan = 60 * 1000;
            double jarakTempuh = distance * 1000;

            // Hitung waktu yang dibutuhkan
            double waktu       = jarakTempuh / ((double) kecepatan / 3600);
            int ubahWaktu  = (int) waktu; // jadikan int

            int modWaktu    = ubahWaktu % 3600; // cari sisa bagi
            int totalJam    = (ubahWaktu - modWaktu) / 3600; // hitung untuk jam
            int totalMenit  = (modWaktu - modWaktu % 60) / 60; // hitung untuk menit

            if (totalJam!=0){
                holder.waktuMobil.setText(String.format("%d jam %d menit", totalJam, totalMenit));
            }else {
                holder.waktuMobil.setText(String.format("%d menit", totalMenit));
            }

            //Hitung waktu untuk Motor
            final int kecepatanMotor = 40 * 1000;
            double jarakTempuhMotor = distance * 1000;

            // Hitung waktu yang dibutuhkan
            double waktuMotor       = jarakTempuhMotor / ((double) kecepatanMotor / 3600);
            int ubahWaktuMotor  = (int) waktuMotor; // jadikan int

            int modWaktuMotor    = ubahWaktuMotor % 3600; // cari sisa bagi
            int totalJamMotor    = (ubahWaktuMotor - modWaktuMotor) / 3600; // hitung untuk jam
            int totalMenitMotor  = (modWaktuMotor - modWaktuMotor % 60) / 60; // hitung untuk menit

            if (totalJam!=0){
                holder.waktuMotor.setText(String.format("%d jam %d menit", totalJamMotor, totalMenitMotor));
            }else {
                holder.waktuMotor.setText(String.format("%d menit", totalMenitMotor));
            }


        }
    }

    private double getDistance(double latDestination, double longDestination, double latUser, double longUser) {

        Double lat1 = latDestination;
        Double lon1 = longDestination;
        Double lat2 = latUser;
        Double lon2 = longUser;
        final int R = 6371;

        Double latRad1 = lat1 * (Math.PI / 180);
        Double latRad2 = lat2 * (Math.PI / 180);
        Double deltaLatRad = (lat2 - lat1) * (Math.PI / 180);
        Double deltaLonRad = (lon2 - lon1) * (Math.PI / 180);
        //rumus haversine//
        Double a = Math.sin(deltaLatRad / 2)
                * Math.sin(deltaLatRad / 2)
                + Math.cos(latRad1)
                * Math.cos(latRad2)
                * Math.sin(deltaLonRad / 2)
                * Math.sin(deltaLonRad / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        Double s = R * c;
        return s;

    }

    @Override
    public int getItemCount() {
        return itineraryModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView objectImage;
        TextView objectTitle;
        TextView objectOpen;
        TextView objectJarak;
        TextView waktuMobil;
        TextView waktuMotor;
        Button directBtn;
        TextView number;
        TextView duration;
        ImageView check;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            objectImage = itemView.findViewById(R.id.objectImage);
            objectTitle = itemView.findViewById(R.id.objectTitle);
            objectOpen = itemView.findViewById(R.id.objectOpen);
            objectJarak = itemView.findViewById(R.id.objectJarak);
            waktuMobil = itemView.findViewById(R.id.waktuMobil);
            waktuMotor = itemView.findViewById(R.id.waktuMotor);
            directBtn = itemView.findViewById(R.id.btDirection);
            number = itemView.findViewById(R.id.number);
            duration = itemView.findViewById(R.id.durasi);
            check = itemView.findViewById(R.id.check);

        }

        public void check() {

        }
    }
}
