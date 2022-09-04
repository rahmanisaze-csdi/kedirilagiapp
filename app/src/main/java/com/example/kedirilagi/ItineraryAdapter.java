package com.example.kedirilagi;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
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

public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.AlamViewHolder> {

    FirebaseAuth auth;
    FirebaseUser currentuser;
    Boolean itineraryChecker = false;
    DatabaseReference itineraryRef, itineraryref, itinerary_listRef;
    ItineraryModel itineraryModel;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private Context context;
    private List<ItineraryModel> itineraryModelList;


    public ItineraryAdapter(Context context, List<ItineraryModel> itineraryModelList) {
        this.context = context;
        this.itineraryModelList = itineraryModelList;
    }

    @NonNull
    @Override
    public AlamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itinerary_item,parent,false);
        return new AlamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlamViewHolder holder, int position) {

        auth = FirebaseAuth.getInstance();
        currentuser = auth.getCurrentUser();
        String currentUserid = currentuser.getUid();
        itineraryModel = new ItineraryModel();
        itineraryref = database.getReference("itinerary");
        itinerary_listRef = database.getReference("itineraryList").child(currentUserid);

        final String postkey = itineraryModelList.get(position).getKey();

        holder.objectTitle.setText(itineraryModelList.get(position).getNama());
        holder.objectOpen.setText(itineraryModelList.get(position).getOpen());
        holder. objectDuration.setText(String.valueOf(itineraryModelList.get(position).getDuration()));
        Glide.with(context).load(itineraryModelList.get(position).getPicture()).into(holder.objectImage);

        String nama = itineraryModelList.get(position).getNama();
        String open = itineraryModelList.get(position).getOpen();
        String picture = itineraryModelList.get(position).getPicture();
        double latitude = itineraryModelList.get(position).getLatitude();
        double longitude = itineraryModelList.get(position).getLongitude();
        String userId = itineraryModelList.get(position).getUserId();

        holder.favouriteChecker(postkey);
        holder.itineraryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itineraryChecker = true;
                itineraryref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (itineraryChecker.equals(true)){
                            if (dataSnapshot.child(postkey).hasChild(currentUserid)){
                                itineraryref.child(postkey).child(currentUserid).removeValue();
                                delete(nama);
                                itineraryChecker = false;
                            }else {
                                itineraryref.child(postkey).child(currentUserid).setValue(true);
                                FragmentActivity activity = (FragmentActivity)(context);
                                FragmentManager fm = activity.getSupportFragmentManager();
                                Bundle bundle = new Bundle();
                                bundle.putString("nama", itineraryModelList.get(position).getNama());
                                DurationFragment newFragment = new DurationFragment();
                                newFragment.setArguments(bundle);
                                newFragment.show(fm,"MyFragment");
                                Calendar cdate = Calendar.getInstance();
                                SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyyy");
                                final String savedate = currentdate.format(cdate.getTime());

                                Calendar ctime = Calendar.getInstance();
                                SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
                                final String savetime = currenttime.format(ctime.getTime());

                                String time = savedate +":"+ savetime;
                                itineraryModel.setNama(nama);
                                itineraryModel.setOpen(open);
                                itineraryModel.setPicture(picture);
                                itineraryModel.setLatitude(latitude);
                                itineraryModel.setLongitude(longitude);
                                itineraryModel.setUserId(userId);
                                itineraryModel.setDuration(0);
                                itineraryModel.setItineraryKey(postkey);
                                itineraryModel.setTime(time);

                                String id = itinerary_listRef.push().getKey();
                                itinerary_listRef.child(id).setValue(itineraryModel);
                                itineraryChecker = false;
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        GetLocation getLocation = new GetLocation(context);
        Location location = getLocation.getLocation();
        if (location != null) {
            double latUser = location.getLatitude();
            double longUser = location.getLongitude();
            double latDestination = Double.valueOf(itineraryModelList.get(position).getLatitude());
            double longDestination = Double.valueOf(itineraryModelList.get(position).getLongitude());
            double distance = getDistance(latDestination, longDestination,latUser, longUser);
            holder.objectJarak.setText(String.format("%.2f KM",distance));
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

    private double Distance(double latDestination, double longDestination, double latUser, double longUser) {
        //calculate longitude difference
        double longDiff = longDestination - longUser;
        //calculate distance
        double distance = Math.sin(deg2rad(latDestination))
                * Math.sin(deg2rad(latUser))
                + Math.cos(deg2rad(latDestination))
                * Math.cos(deg2rad(latUser))
                * Math.cos(deg2rad(longDiff));
        distance = Math.acos(distance);
        //convert distance radian to degree
        distance = rad2deg(distance);
        //Distance in miles
        distance = distance * 60 * 1.1515;
        //Distances in kilometers
        distance = distance * 1.609344;
        return distance;
    }

    private double rad2deg(double distance) {
        return (distance * 180.0 / Math.PI);
    }

    private double deg2rad(double latDestination) {
        return (latDestination * Math.PI / 180.0);
    }

    @Override
    public int getItemCount() {
        return itineraryModelList.size();
    }

    public class AlamViewHolder extends RecyclerView.ViewHolder {
        ImageView objectImage;
        TextView objectTitle;
        TextView objectOpen;
        TextView objectJarak;
        ImageButton itineraryBtn;
        TextView objectDuration;
        DatabaseReference itineraryref;
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        public AlamViewHolder(@NonNull View itemView) {
            super(itemView);

            objectImage = itemView.findViewById(R.id.objectImage);
            objectTitle = itemView.findViewById(R.id.objectTitle);
            objectOpen = itemView.findViewById(R.id.objectOpen);
            objectJarak = itemView.findViewById(R.id.objectJarak);
            itineraryBtn = itemView.findViewById(R.id.itinerary_item);
            objectDuration = itemView.findViewById(R.id.objectDuration);

        }

        public void favouriteChecker(String postkey) {
            itineraryBtn = itemView.findViewById(R.id.itinerary_item);
            itineraryRef = database.getReference("itinerary");

            auth = FirebaseAuth.getInstance();
            currentuser = auth.getCurrentUser();
            String uid = currentuser.getUid();

            itineraryRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(postkey).hasChild(uid)){
                        itineraryBtn.setImageResource(R.drawable.ic_turned_in_black_24dp);
                    }else {
                        itineraryBtn.setImageResource(R.drawable.ic_turned_in_not_black_24dp);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    void delete(String nama){

        Query query = itinerary_listRef.orderByChild("nama").equalTo(nama);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    dataSnapshot1.getRef().removeValue();

                    Toast.makeText(context.getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
