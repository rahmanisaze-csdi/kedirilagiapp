package com.example.kedirilagi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItineraryNearby extends Fragment {

    RecyclerView rvItinerarySystem;
    ItineraryListAdapterNearby itineraryListAdapter;
    List<ItineraryModel> itineraryModelList;
    List<ItineraryModel> itinerarySystemList;
    RadioGroup rgMenit,rgKendaraan,rgKategori;
    RadioButton rbMenit1,rbMenit2,rbMenit3,rbKendaraan1,rbKendaraan2,rbKategori1,rbKategori2,rbKategori3;
    TextView tvTotal;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    int v = 0;
    int totalWaktu = 0;
    double r = 0;

    public ItineraryNearby() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_itinerary_system, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = this.getContext().getSharedPreferences("pref",0);
        editor = sharedPreferences.edit();

        rgMenit = view.findViewById(R.id.rgMenit);
        rgKendaraan = view.findViewById(R.id.rgKendaraan);
        rgKategori = view.findViewById(R.id.rgKategori);
        rbMenit1 = view.findViewById(R.id.rbMenit1);
        rbMenit2 = view.findViewById(R.id.rbMenit2);
        rbMenit3 = view.findViewById(R.id.rbMenit3);
        rbKendaraan1 = view.findViewById(R.id.rbKendaraan1);
        rbKendaraan2 = view.findViewById(R.id.rbKendaraan2);
        rbKategori1 = view.findViewById(R.id.rbKategori1);
        rbKategori2 = view.findViewById(R.id.rbKategori2);
        rbKategori3 = view.findViewById(R.id.rbKategori3);
        tvTotal = view.findViewById(R.id.tvTotal);
        progressBar = view.findViewById(R.id.progressBar3);
        rvItinerarySystem = view.findViewById(R.id.rvItinerarySystem);
        rvItinerarySystem.setHasFixedSize(true);
        rvItinerarySystem.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        cekrgMenit();
        cekrgKendaraan();
        cekrgKategori();

        rgMenit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbMenit1){
                    totalWaktu = 45;
                    editor.putInt("menit",2);
                }else if (checkedId == R.id.rbMenit2){
                    totalWaktu = 60;
                    editor.putInt("menit",1);
                }else if (checkedId == R.id.rbMenit3){
                    totalWaktu = 120;
                    editor.putInt("menit",0);
                }
                editor.commit();
                itineraryList();
            }
        });

        rgKendaraan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbKendaraan1){
                    v = 60;
                    editor.putInt("kendaraan",1);
                }else if (checkedId == R.id.rbKendaraan2){
                    v = 40;
                    editor.putInt("kendaraan",0);
                }
                editor.commit();
                itineraryList();
            }
        });

        rgKategori.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbKategori1){
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference("Alam");
                    r = 2.8;
                    editor.putInt("kategori",2);
                }else if (checkedId == R.id.rbKategori2){
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference("Sejarah");
                    r = 5.0;
                    editor.putInt("kategori",1);
                }else if (checkedId == R.id.rbKategori3){
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference("Religi");
                    r = 5.0;
                    editor.putInt("kategori",0);
                }
                editor.commit();
                itineraryList();
            }

        });




    }

    private void total() {
        if (itineraryModelList!=null){
            tvTotal.setText(""+itineraryModelList.size());
        }else {
            tvTotal.setText("0");
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
            builder.setTitle("Tidak dapat menemukan objek terdekat dari Anda");
            builder.setMessage("Pastikan Anda berada di Kediri");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }

    private void cekrgMenit() {
        int menit = sharedPreferences.getInt("menit",4);
        if (menit==2){
            totalWaktu = 45;
            rbMenit1.setChecked(true);
        }else if (menit==1){
            totalWaktu = 60;
            rbMenit2.setChecked(true);
        }else if (menit==0){
            totalWaktu = 120;
            rbMenit3.setChecked(true);
        }
    }

    private void cekrgKendaraan() {
        int kendaraan = sharedPreferences.getInt("kendaraan",3);
        if (kendaraan==1){
            v = 60;
            rbKendaraan1.setChecked(true);
        }else if (kendaraan==0){
            v = 40;
            rbKendaraan2.setChecked(true);
        }
    }

    private void cekrgKategori() {
        int kategori = sharedPreferences.getInt("kategori",4);
        if (kategori==2){
            rbKategori1.setChecked(true);
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Alam");
            r = 2.8;
            itineraryList();
        }else if (kategori==1){
            rbKategori2.setChecked(true);
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Sejarah");
            r = 5.0;
            itineraryList();
        }else if (kategori==0){
            rbKategori3.setChecked(true);
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Religi");
            r = 5.0;
            itineraryList();
        }
    }

    private void itineraryList() {
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.INVISIBLE);
                itineraryModelList = new ArrayList<>();
                itinerarySystemList = new ArrayList<>();
                Integer perjalanan = 0;
                Integer duration = 0;
                Integer time = 0;
                double distance = 0;
                for (DataSnapshot itinerarylist: dataSnapshot.getChildren()){

                    ItineraryModel itineraryModel = itinerarylist.getValue(ItineraryModel.class);
                    itineraryModel.setKey(itinerarylist.getKey());
                    GetLocation getLocation = new GetLocation(getContext());
                    Location location = getLocation.getLocation();
                    if (location != null) {
                        double latUser = location.getLatitude();
                        double longUser = location.getLongitude();
                        double latDestination = Double.valueOf(itineraryModel.getLatitude());
                        double longDestination = Double.valueOf(itineraryModel.getLongitude());
                        itineraryModel.setDistance(distanceFunc(latDestination, longDestination, latUser, longUser));
                    }
                    //itineraryModelList.add(itineraryModel);

                    //List<ItineraryModel> synset = Collections.synchronizedSortedSet(itineraryModelList.add(itineraryModel));
                    //synchronized (itineraryModelList) {
                      //  itineraryModelList.forEach((e) -> {
                        //    itinerarySystemList.add(e.getDuration());
                        //});
                    //}

                    //if (perjalanan <= totalWaktu) {
                      //  itineraryModelList.add(itineraryModel);
                    //}

                    //Hitung waktu untuk Mobil

                    if (Double.valueOf(itineraryModel.getDistance()) < r){
                            final int kecepatan = v * 1000;
                            double jarakTempuh = itineraryModel.getDistance() * 1000;

                            // Hitung waktu yang dibutuhkan
                            double waktu       = jarakTempuh / ((double) kecepatan / 3600);
                            int ubahWaktu  = (int) waktu; // jadikan int

                            int modWaktu    = ubahWaktu % 3600; // cari sisa bagi
                            //int totalJam    = (ubahWaktu - modWaktu) / 3600; // hitung untuk jam
                            int totalMenit  = (modWaktu - modWaktu % 60) / 60; // hitung untuk menit
                            itineraryModel.setWaktu(totalMenit);
                            Integer time1 = Integer.valueOf(itineraryModel.getWaktu());
                            Integer timeCost = Integer.valueOf(itineraryModel.getDuration());
                            duration = duration + timeCost;
                            time = time + time1;
                            perjalanan = time + duration;
                            if (perjalanan<=totalWaktu){
                                itineraryModelList.add(itineraryModel);
                            }else {
                                itinerarySystemList.add(itineraryModel);
                            }
                    }else{
                            itinerarySystemList.add(itineraryModel);
                    }

                }


                Collections.sort(itineraryModelList,(itineraryModel1, t1) -> Double.compare(itineraryModel1.getDistance(),t1.getDistance()));

               // if (perjalanan<=10){
                 //   Collections.sort(itineraryModelList,(itineraryModel1, t1) -> Double.compare(itineraryModel1.getDistance(),t1.getDistance()));
                //}

                total();
                itineraryListAdapter = new ItineraryListAdapterNearby(getContext(),itineraryModelList);
                rvItinerarySystem.setAdapter(itineraryListAdapter);

            }

            private double distanceFunc(double latDestination, double longDestination, double latUser, double longUser) {
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
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
