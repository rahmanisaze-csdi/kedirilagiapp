package com.example.kedirilagi;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import java.util.List;

public class SaranAdapter extends RecyclerView.Adapter<SaranAdapter.MyViewHolder> {

    Context mContext;
    List<SaranModel> mData;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference saran,suka;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public SaranAdapter(Context mContext, List<SaranModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public SaranAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.saran_item,parent,false);
        return new SaranAdapter.MyViewHolder(row);

    }

    @Override
    public void onBindViewHolder(@NonNull SaranAdapter.MyViewHolder holder, int position) {

        saran = database.getReference("Saran");
        suka = database.getReference("Suka").child(mData.get(position).getSaranKey());

        holder.username.setText(mData.get(position).getUsername());
        holder.saran.setText(mData.get(position).getSaran());
        holder.waktu.setText(mData.get(position).getWaktu());
        Glide.with(mContext).load(mData.get(position).getPicture()).into(holder.imgUser);

        isLike(mData.get(position).getSaranKey(), holder.imageView);
        nrLikes(holder.likes, mData.get(position).getSaranKey());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.imageView.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("Suka").child(mData.get(position).getSaranKey())
                            .child(firebaseUser.getUid()).setValue(true);
                }else {
                    FirebaseDatabase.getInstance().getReference().child("Suka").child(mData.get(position).getSaranKey())
                            .child(firebaseUser.getUid()).removeValue();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        ImageView imgUser;
        ImageView imageView;
        TextView likes;
        TextView username;
        TextView saran;
        TextView waktu;
        RelativeLayout relativeLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgUser = itemView.findViewById(R.id.imageUser);
            username = itemView.findViewById(R.id.username);
            saran = itemView.findViewById(R.id.saran);
            waktu = itemView.findViewById(R.id.waktu);
            imageView = itemView.findViewById(R.id.suka);
            likes = itemView.findViewById(R.id.jumlahLike);
            relativeLayout = itemView.findViewById(R.id.rvLayout);
            relativeLayout.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            int position = getAdapterPosition();
            String item = String.valueOf(mData.get(position).getUsername());
            if (item.equals(user.getDisplayName()))
                menu.add(getAdapterPosition(),101,0,"Hapus");
        }
    }

    private void isLike(String saranKey,final ImageView imageView) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Suka")
                .child(saranKey);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.ic_favorite_black_24dp);
                    imageView.setTag("liked");
                }else {
                    imageView.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void nrLikes(TextView likes, String saranKey){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Suka")
                .child(saranKey);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likes.setText(dataSnapshot.getChildrenCount()+" suka");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void removeItem(int position){
        String isiSaran = mData.get(position).getSaran();
        Query query = saran.orderByChild("saran").equalTo(isiSaran);
        suka.removeValue();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    dataSnapshot1.getRef().removeValue();
                    Toast.makeText(mContext.getApplicationContext(), "saran dihapus", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
