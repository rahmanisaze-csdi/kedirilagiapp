package com.example.kedirilagi;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements RecyclerViewClickListener{

    RecyclerView homeRV,suggestedRV, rekomendasiRV;
    RecyclerView.Adapter adapter;
    ArrayList<SuggestedModel> suggestedModels = new ArrayList<>();
    ProgressBar progressBar, progressBar2;
    TextView replaceTitle, tvSearch, tvNodata;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    AlamAdapter alamAdapter;
    SejarahAdapter sejarahAdapter;
    ReligiAdapter religiAdapter;
    KulinerAdapter kulinerAdapter;
    PasarAdapter pasarAdapter;
    CafeAdapter cafeAdapter;
    RecAlamAdapter recAlamAdapter;
    RecSejarahAdapter recSejarahAdapter;
    RecReligiAdapter recReligiAdapter;
    RecKulinerAdapter recKulinerAdapter;
    RecCafeAdapter recCafeAdapter;
    List<Alams> alamList;
    List<Sejarahs> sejarahList;
    List<Religis> religiList;
    List<Kuliners> kulinerList;
    List<Pasars> pasarList;
    List<Cafes> cafeList;

    public HomeFragment() {
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvSearch = view.findViewById(R.id.search);

        final NavController navController = Navigation.findNavController(view);

        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_HomeFragment_to_alamFragment);
                suggestedModels.clear();
            }
        });

        suggestedRV = view.findViewById(R.id.suggested_recyclerview);

        homeRV = view.findViewById(R.id.recyclerview);
        //homeRV.setHasFixedSize(true);
        homeRV.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL,false));

        rekomendasiRV = view.findViewById(R.id.rekomendasiRV);
        //rekomendasiRV.setHasFixedSize(true);
        rekomendasiRV.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));

        suggestedRV = view.findViewById(R.id.suggested_recyclerview);
        progressBar = view.findViewById(R.id.progress_bar);
        progressBar2 = view.findViewById(R.id.progress_bar2);
        replaceTitle = view.findViewById(R.id.objectTitle);

        tvNodata = view.findViewById(R.id.empty_view);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Alam");

        alamItems();

        suggestedItems();

        rekomendasiAlam();

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tambahan,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.saran:
                Intent i = new Intent(this.getActivity(), SaranActivity.class);
                startActivity(i);
                return true;
            case R.id.bantuan:
                Intent intent = new Intent(this.getActivity(), BantuanActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void alamItems() {

        //Get list Alam from the database

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                progressBar.setVisibility(View.INVISIBLE);
                alamList = new ArrayList<>();
                for (DataSnapshot alamsnap: dataSnapshot.getChildren()){

                    Alams alams = alamsnap.getValue(Alams.class);
                    alams.setAlamKey(alamsnap.getKey());
                    GetLocation getLocation = new GetLocation(getContext());
                    Location location = getLocation.getLocation();
                    if (location != null) {
                        double latUser = location.getLatitude();
                        double longUser = location.getLongitude();
                        double latDestination = Double.valueOf(alams.getLatitude());
                        double longDestination = Double.valueOf(alams.getLongitude());
                        alams.setDistance(distanceFunc(latDestination, longDestination, latUser, longUser));
                    }
                    alamList.add(alams);

                }

                alamAdapter = new AlamAdapter(getContext(),alamList);
                homeRV.setAdapter(alamAdapter);

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

    private void sejarahItems() {
        progressBar.setVisibility(View.INVISIBLE);

        //Get list Alam from the database

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sejarahList = new ArrayList<>();
                for (DataSnapshot sejarahsnap: dataSnapshot.getChildren()){

                    Sejarahs sejarahs = sejarahsnap.getValue(Sejarahs.class);
                    sejarahs.setSejarahKey(sejarahsnap.getKey());
                    GetLocation getLocation = new GetLocation(getContext());
                    Location location = getLocation.getLocation();
                    if (location != null) {
                        double latUser = location.getLatitude();
                        double longUser = location.getLongitude();
                        double latDestination = Double.valueOf(sejarahs.getLatitude());
                        double longDestination = Double.valueOf(sejarahs.getLongitude());
                        sejarahs.setDistance(distanceFunc(latDestination, longDestination, latUser, longUser));
                    }
                    sejarahList.add(sejarahs);

                }

                sejarahAdapter = new SejarahAdapter(getContext(),sejarahList);
                homeRV.setAdapter(sejarahAdapter);

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

    private void religiItems() {
        progressBar.setVisibility(View.INVISIBLE);

        //Get list Alam from the database

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                religiList = new ArrayList<>();
                for (DataSnapshot religisnap: dataSnapshot.getChildren()){

                    Religis religis = religisnap.getValue(Religis.class);
                    religis.setReligiKey(religisnap.getKey());
                    GetLocation getLocation = new GetLocation(getContext());
                    Location location = getLocation.getLocation();
                    if (location != null) {
                        double latUser = location.getLatitude();
                        double longUser = location.getLongitude();
                        double latDestination = Double.valueOf(religis.getLatitude());
                        double longDestination = Double.valueOf(religis.getLongitude());
                        religis.setDistance(distanceFunc(latDestination, longDestination, latUser, longUser));
                    }
                    religiList.add(religis);

                }

                religiAdapter = new ReligiAdapter(getContext(),religiList);
                homeRV.setAdapter(religiAdapter);

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

    private void kulinerItems() {
        progressBar.setVisibility(View.INVISIBLE);

        //Get list Alam from the database

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                kulinerList = new ArrayList<>();
                for (DataSnapshot kulinersnap: dataSnapshot.getChildren()){

                    Kuliners kuliners = kulinersnap.getValue(Kuliners.class);
                    kuliners.setKulinerKey(kulinersnap.getKey());
                    GetLocation getLocation = new GetLocation(getContext());
                    Location location = getLocation.getLocation();
                    if (location != null) {
                        double latUser = location.getLatitude();
                        double longUser = location.getLongitude();
                        double latDestination = Double.valueOf(kuliners.getLatitude());
                        double longDestination = Double.valueOf(kuliners.getLongitude());
                        kuliners.setDistance(distanceFunc(latDestination, longDestination, latUser, longUser));
                    }
                    kulinerList.add(kuliners);

                }

                kulinerAdapter = new KulinerAdapter(getContext(),kulinerList);
                homeRV.setAdapter(kulinerAdapter);

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

    private void pasarItems() {
        progressBar.setVisibility(View.INVISIBLE);

        //Get list Alam from the database

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pasarList = new ArrayList<>();
                for (DataSnapshot pasarsnap: dataSnapshot.getChildren()){

                    Pasars pasars = pasarsnap.getValue(Pasars.class);
                    pasars.setPasarKey(pasarsnap.getKey());
                    GetLocation getLocation = new GetLocation(getContext());
                    Location location = getLocation.getLocation();
                    if (location != null) {
                        double latUser = location.getLatitude();
                        double longUser = location.getLongitude();
                        double latDestination = Double.valueOf(pasars.getLatitude());
                        double longDestination = Double.valueOf(pasars.getLongitude());
                        pasars.setDistance(distanceFunc(latDestination, longDestination, latUser, longUser));
                    }
                    pasarList.add(pasars);

                }

                pasarAdapter = new PasarAdapter(getContext(),pasarList);
                homeRV.setAdapter(pasarAdapter);

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

    private void cafeItems() {
        progressBar.setVisibility(View.INVISIBLE);

        //Get list Alam from the database

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cafeList = new ArrayList<>();
                for (DataSnapshot cafesnap: dataSnapshot.getChildren()){

                    Cafes cafes = cafesnap.getValue(Cafes.class);
                    cafes.setCafeKey(cafesnap.getKey());
                    GetLocation getLocation = new GetLocation(getContext());
                    Location location = getLocation.getLocation();
                    if (location != null) {
                        double latUser = location.getLatitude();
                        double longUser = location.getLongitude();
                        double latDestination = Double.valueOf(cafes.getLatitude());
                        double longDestination = Double.valueOf(cafes.getLongitude());
                        cafes.setDistance(distanceFunc(latDestination, longDestination, latUser, longUser));
                    }
                    cafeList.add(cafes);

                }

                cafeAdapter = new CafeAdapter(getContext(),cafeList);
                homeRV.setAdapter(cafeAdapter);

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

    private void rekomendasiAlam() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("RekomendasiAlam");

        //Get list Alam from the database

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar2.setVisibility(View.INVISIBLE);
                alamList = new ArrayList<>();
                for (DataSnapshot alamsnap: dataSnapshot.getChildren()){

                    Alams alams = alamsnap.getValue(Alams.class);
                    alams.setAlamKey(alamsnap.getKey());
                    GetLocation getLocation = new GetLocation(getContext());
                    Location location = getLocation.getLocation();
                    if (location != null) {
                        double latUser = location.getLatitude();
                        double longUser = location.getLongitude();
                        double latDestination = Double.valueOf(alams.getLatitude());
                        double longDestination = Double.valueOf(alams.getLongitude());
                        alams.setDistance(distanceFunc(latDestination, longDestination, latUser, longUser));
                    }
                    alamList.add(alams);

                }

                recAlamAdapter = new RecAlamAdapter(getContext(),alamList);
                rekomendasiRV.setAdapter(recAlamAdapter);

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

    private void rekomendasiSejarah() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("RekomendasiSejarah");

        //Get list Alam from the database

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar2.setVisibility(View.INVISIBLE);
                sejarahList = new ArrayList<>();
                for (DataSnapshot sejarahsnap: dataSnapshot.getChildren()){

                    Sejarahs sejarahs = sejarahsnap.getValue(Sejarahs.class);
                    sejarahs.setSejarahKey(sejarahsnap.getKey());
                    GetLocation getLocation = new GetLocation(getContext());
                    Location location = getLocation.getLocation();
                    if (location != null) {
                        double latUser = location.getLatitude();
                        double longUser = location.getLongitude();
                        double latDestination = Double.valueOf(sejarahs.getLatitude());
                        double longDestination = Double.valueOf(sejarahs.getLongitude());
                        sejarahs.setDistance(distanceFunc(latDestination, longDestination, latUser, longUser));
                    }
                    sejarahList.add(sejarahs);

                }

                recSejarahAdapter = new RecSejarahAdapter(getContext(),sejarahList);
                rekomendasiRV.setAdapter(recSejarahAdapter);

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

    private void rekomendasiReligi() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("RekomendasiReligi");

        if (religiAdapter.getItemCount() == 0){
            rekomendasiRV.setVisibility(View.GONE);
            tvNodata.setVisibility(View.VISIBLE);
        }else {
            rekomendasiRV.setVisibility(View.VISIBLE);
            tvNodata.setVisibility(View.GONE);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressBar2.setVisibility(View.INVISIBLE);
                    religiList = new ArrayList<>();
                    for (DataSnapshot religisnap: dataSnapshot.getChildren()){

                        Religis religis = religisnap.getValue(Religis.class);
                        religis.setReligiKey(religisnap.getKey());
                        religiList.add(religis);

                    }

                    recReligiAdapter = new RecReligiAdapter(getContext(),religiList);
                    rekomendasiRV.setAdapter(recReligiAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });
        }
    }

    private void rekomendasiKuliner() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("RekomendasiKuliner");

        //Get list Alam from the database

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar2.setVisibility(View.INVISIBLE);
                kulinerList = new ArrayList<>();
                for (DataSnapshot kulinersnap: dataSnapshot.getChildren()){

                    Kuliners kuliners = kulinersnap.getValue(Kuliners.class);
                    kuliners.setKulinerKey(kulinersnap.getKey());
                    GetLocation getLocation = new GetLocation(getContext());
                    Location location = getLocation.getLocation();
                    if (location != null) {
                        double latUser = location.getLatitude();
                        double longUser = location.getLongitude();
                        double latDestination = Double.valueOf(kuliners.getLatitude());
                        double longDestination = Double.valueOf(kuliners.getLongitude());
                        kuliners.setDistance(distanceFunc(latDestination, longDestination, latUser, longUser));
                    }
                    kulinerList.add(kuliners);

                }

                recKulinerAdapter = new RecKulinerAdapter(getContext(),kulinerList);
                rekomendasiRV.setAdapter(recKulinerAdapter);

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

    private void rekomendasiCafe() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("RekomendasiCafe");

        //Get list Alam from the database

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar2.setVisibility(View.INVISIBLE);
                cafeList = new ArrayList<>();
                for (DataSnapshot cafesnap: dataSnapshot.getChildren()){

                    Cafes cafes = cafesnap.getValue(Cafes.class);
                    cafes.setCafeKey(cafesnap.getKey());
                    GetLocation getLocation = new GetLocation(getContext());
                    Location location = getLocation.getLocation();
                    if (location != null) {
                        double latUser = location.getLatitude();
                        double longUser = location.getLongitude();
                        double latDestination = Double.valueOf(cafes.getLatitude());
                        double longDestination = Double.valueOf(cafes.getLongitude());
                        cafes.setDistance(distanceFunc(latDestination, longDestination, latUser, longUser));
                    }
                    cafeList.add(cafes);

                }

                recCafeAdapter = new RecCafeAdapter(getContext(),cafeList);
                rekomendasiRV.setAdapter(recCafeAdapter);

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

    private void suggestedItems() {

        //suggestedRV.setHasFixedSize(true);
        suggestedRV.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL,false));

        suggestedModels.add(new SuggestedModel(R.drawable.alam2, "Alam/Buatan"));
        suggestedModels.add(new SuggestedModel(R.drawable.sejarah, "Sejarah"));
        suggestedModels.add(new SuggestedModel(R.drawable.religi2, "Religi"));
        suggestedModels.add(new SuggestedModel(R.drawable.kuliner2, "Kuliner"));
        suggestedModels.add(new SuggestedModel(R.drawable.mall2, "Pasar/Mall"));
        suggestedModels.add(new SuggestedModel(R.drawable.cafe2, "Cafe"));

        adapter = new SuggestedAdapter(suggestedModels,HomeFragment.this);
        suggestedRV.setAdapter(adapter);

    }

    @Override
    public void onItemClick(int position) {

        progressBar.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.VISIBLE);
        if (position == 0){
            replaceTitle.setText("Alam/Buatan");
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Alam");
            alamItems();
            rekomendasiAlam();
            tvSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(v).navigate(R.id.action_HomeFragment_to_alamFragment);
                    suggestedModels.clear();
                }
            });
            tvNodata.setVisibility(View.GONE);
            rekomendasiRV.setVisibility(View.VISIBLE);
            progressBar2.setVisibility(View.VISIBLE);
        }else if (position == 1){
            replaceTitle.setText("Sejarah");
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Sejarah");
            sejarahItems();
            rekomendasiSejarah();
            tvSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(v).navigate(R.id.action_HomeFragment_to_sejarahFragment2);
                    suggestedModels.clear();
                }
            });
            tvNodata.setVisibility(View.GONE);
            rekomendasiRV.setVisibility(View.VISIBLE);
            progressBar2.setVisibility(View.VISIBLE);
        }else if (position == 2){
            replaceTitle.setText("Religi");
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Religi");
            religiItems();
            tvSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(v).navigate(R.id.action_HomeFragment_to_religiFragment);
                    suggestedModels.clear();
                }
            });
            tvNodata.setVisibility(View.VISIBLE);
            rekomendasiRV.setVisibility(View.GONE);
            progressBar2.setVisibility(View.GONE);
        }else if (position == 3){
            replaceTitle.setText("Kuliner");
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Kuliner");
            kulinerItems();
            rekomendasiKuliner();
            tvSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(v).navigate(R.id.action_HomeFragment_to_kulinerFragment);
                    suggestedModels.clear();
                }
            });
            tvNodata.setVisibility(View.GONE);
            rekomendasiRV.setVisibility(View.VISIBLE);
            progressBar2.setVisibility(View.VISIBLE);
        }else if (position == 4){
            replaceTitle.setText("Pasar/Mall");
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Pasar");
            pasarItems();
            tvSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(v).navigate(R.id.action_HomeFragment_to_pasarFragment);
                    suggestedModels.clear();
                }
            });
            tvNodata.setVisibility(View.VISIBLE);
            rekomendasiRV.setVisibility(View.GONE);
            progressBar2.setVisibility(View.GONE);
        }else {
            replaceTitle.setText("Cafe");
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Cafe");
            cafeItems();
            rekomendasiCafe();
            tvSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(v).navigate(R.id.action_HomeFragment_to_cafeFragment);
                    suggestedModels.clear();
                }
            });
            tvNodata.setVisibility(View.GONE);
            rekomendasiRV.setVisibility(View.VISIBLE);
            progressBar2.setVisibility(View.VISIBLE);
        }
    }
}
