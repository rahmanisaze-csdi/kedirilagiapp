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

public class SearchReligiAdapter extends RecyclerView.Adapter<SearchReligiAdapter.MyViewHolder> implements Filterable {

    Context mContext;
    List<Religis> mData;
    List<Religis> mDataAll;

    public SearchReligiAdapter(Context mContext, List<Religis> mData) {
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
            List<Religis> filteredlist = new ArrayList<>();

            if (constraint.toString().isEmpty()) {
                filteredlist.addAll(mDataAll);
            } else {
                String pttrn = constraint.toString().toLowerCase().trim();
                for (Religis religis: mDataAll) {
                    if (religis.getNama().toLowerCase().contains(pttrn)) {
                        filteredlist.add(religis);
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
                    Intent religiDetailActivity = new Intent(mContext, ReligiDetailActivity.class);
                    int position = getAdapterPosition();

                    religiDetailActivity.putExtra("nama",mData.get(position).getNama());
                    religiDetailActivity.putExtra("religiImage",mData.get(position).getPicture());
                    religiDetailActivity.putExtra("description",mData.get(position).getDescription());
                    religiDetailActivity.putExtra("alamat",mData.get(position).getAlamat());
                    religiDetailActivity.putExtra("open",mData.get(position).getOpen());
                    religiDetailActivity.putExtra("highlight",mData.get(position).getHighlight());
                    religiDetailActivity.putExtra("religiKey",mData.get(position).getReligiKey());
                    mContext.startActivity(religiDetailActivity);
                }
            });

        }
    }
}

