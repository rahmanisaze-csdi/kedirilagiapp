package com.example.kedirilagi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class SearchSejarahAdapter extends RecyclerView.Adapter<SearchSejarahAdapter.MyViewHolder> implements Filterable {

    Context mContext;
    List<Sejarahs> mData;
    List<Sejarahs> mDataAll;

    public SearchSejarahAdapter(Context mContext, List<Sejarahs> mData) {
        this.mContext = mContext;
        this.mData = mData;
        this.mDataAll = new ArrayList<>(mData);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.search_item,parent,false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvNama.setText(mData.get(position).getNama());
        Glide.with(mContext).load(mData.get(position).getPicture()).into(holder.image);


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        //run on background thread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Sejarahs> filteredlist = new ArrayList<>();

            if (constraint.toString().isEmpty()) {
                filteredlist.addAll(mDataAll);
            } else {
                String pttrn = constraint.toString().toLowerCase().trim();
                for (Sejarahs sejarahs: mDataAll) {
                    if (sejarahs.getNama().toLowerCase().contains(pttrn)) {
                        filteredlist.add(sejarahs);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredlist;
            return filterResults;
        }
        //run on ui thread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mData.clear();
            mData.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder{

        //Button btDetail;
        TextView tvNama;
        TextView tvOpen;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNama = itemView.findViewById(R.id.tvNama);
            image = itemView.findViewById(R.id.img);
            //btDetail = itemView.findViewById(R.id.btDetail);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sejarahDetailActivity = new Intent(mContext, SejarahDetailActivity.class);
                    int position = getAdapterPosition();

                    sejarahDetailActivity.putExtra("nama",mData.get(position).getNama());
                    sejarahDetailActivity.putExtra("sejarahImage",mData.get(position).getPicture());
                    sejarahDetailActivity.putExtra("description",mData.get(position).getDescription());
                    sejarahDetailActivity.putExtra("alamat",mData.get(position).getAlamat());
                    sejarahDetailActivity.putExtra("htm",mData.get(position).getHtm());
                    sejarahDetailActivity.putExtra("open",mData.get(position).getOpen());
                    sejarahDetailActivity.putExtra("highlight",mData.get(position).getHighlight());
                    sejarahDetailActivity.putExtra("sejarahKey",mData.get(position).getSejarahKey());
                    mContext.startActivity(sejarahDetailActivity);
                }
            });

        }
    }
}