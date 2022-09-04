package com.example.kedirilagi;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ItineraryListAdapterManual extends RecyclerView.Adapter<ItineraryListAdapterManual.MyViewHolder> {

    private Context context;
    private List<ItineraryModel> itineraryModelList;
    FirebaseAuth auth;
    FirebaseUser currentuser;
    DatabaseReference itineraryref, itinerary_listRef;
    ItineraryModel itineraryModel;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private long mEndTime;

    public ItineraryListAdapterManual(Context context, List<ItineraryModel> itineraryModelList) {
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

        auth = FirebaseAuth.getInstance();
        currentuser = auth.getCurrentUser();
        String currentUserid = currentuser.getUid();
        itineraryref = database.getReference("itinerary");
        itinerary_listRef = database.getReference("itineraryList").child(currentUserid);

        String nama = itineraryModelList.get(position).getNama();
        String key = itineraryModelList.get(position).getItineraryKey();

        holder.objectTitle.setText(itineraryModelList.get(position).getNama());
        holder.objectOpen.setText(itineraryModelList.get(position).getOpen());
        holder.duration.setText(String.valueOf(itineraryModelList.get(position).getDuration()));
        Glide.with(context).load(itineraryModelList.get(position).getPicture()).into(holder.objectImage);

        holder.number.setText(""+ (position+1));

        holder.number.setTag(1);

        holder.directBtn.setVisibility(View.INVISIBLE);

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
        if (itineraryModelList.get(position).getLatitude() != 0.0 && itineraryModelList.get(position).getLongitude() != 0.0) {
            if (position == 0){
                holder.directBtn.setVisibility(View.VISIBLE);
                double latUser = location.getLatitude();
                double longUser = location.getLongitude();
                double latDestination = Double.valueOf(itineraryModelList.get(position).getLatitude());
                double longDestination = Double.valueOf(itineraryModelList.get(position).getLongitude());
                double distance = getDistance(latDestination, longDestination,latUser, longUser);
                holder.objectJarak.setText(String.format("%.2f KM",distance));

                if (distance <= 0.12){
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("itineraryList").child(currentUserid);
                    Calendar cdate = Calendar.getInstance();
                    SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyyy");
                    final String savedate = currentdate.format(cdate.getTime());

                    Calendar ctime = Calendar.getInstance();
                    SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
                    final String savetime = currenttime.format(ctime.getTime());

                    String time = savedate +":"+ savetime;

                    Query query = myRef.orderByChild("nama").equalTo(nama);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds: dataSnapshot.getChildren()){
                                ds.getRef().child("latitude").setValue(0.0);
                                ds.getRef().child("longitude").setValue(0.0);
                                ds.getRef().child("time").setValue(time);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }


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
            }else {
                if (itineraryModelList.get(position-1).getLatitude() == 0.0 && itineraryModelList.get(position-1).getLongitude() == 0.0){
                    holder.directBtn.setVisibility(View.VISIBLE);
                    double latUser = location.getLatitude();
                    double longUser = location.getLongitude();
                    double latDestination = Double.valueOf(itineraryModelList.get(position).getLatitude());
                    double longDestination = Double.valueOf(itineraryModelList.get(position).getLongitude());
                    double distance = getDistance(latDestination, longDestination,latUser, longUser);
                    holder.objectJarak.setText(String.format("%.2f KM",distance));

                    if (distance <= 0.03){
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("itineraryList").child(currentUserid);
                        Calendar cdate = Calendar.getInstance();
                        SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyyy");
                        final String savedate = currentdate.format(cdate.getTime());

                        Calendar ctime = Calendar.getInstance();
                        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
                        final String savetime = currenttime.format(ctime.getTime());

                        String time = savedate +":"+ savetime;

                        Query query = myRef.orderByChild("nama").equalTo(nama);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds: dataSnapshot.getChildren()){
                                    ds.getRef().child("latitude").setValue(0.0);
                                    ds.getRef().child("longitude").setValue(0.0);
                                    ds.getRef().child("time").setValue(time);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }


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
                }else {
                    double latUser = Double.valueOf(itineraryModelList.get(position-1).getLatitude());
                    double longUser = Double.valueOf(itineraryModelList.get(position-1).getLongitude());
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
        }else {
            holder.check.setVisibility(View.VISIBLE);
            holder.number.setVisibility(View.INVISIBLE);
            holder.directBtn.setVisibility(View.GONE);
            holder.alert.setVisibility(View.GONE);
            holder.time.setVisibility(View.VISIBLE);
            holder.time.setText(itineraryModelList.get(position).getTime());
            holder.objectJarak.setText(String.format("0 KM"));
            holder.waktuMotor.setText(String.format("0 Menit"));
            holder.waktuMobil.setText(String.format("0 Menit"));
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
        TextView alert;
        TextView time;
        CountDownTimer countDownTimer;
        private long mTimerRunning;

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
            alert = itemView.findViewById(R.id.alert);
            time = itemView.findViewById(R.id.times);

        }
    }
}
