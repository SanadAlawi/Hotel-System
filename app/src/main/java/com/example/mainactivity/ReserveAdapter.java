package com.example.mainactivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class ReserveAdapter extends RecyclerView.Adapter<ReserveAdapter.ReserveHolder> {

    private ArrayList<Reserve> reserveList;
    private Context context;
    private OnItemClickListener listener;


    public interface OnItemClickListener{
        void onItemClick(int position);
        void onRemoveClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public ReserveAdapter(ArrayList<Reserve> reserveList, Context context) {
        this.reserveList = reserveList;
        this.context = context;
    }

    public ReserveAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<Reserve> getReserveList() {
        return reserveList;
    }

    public void setReserveList(ArrayList<Reserve> reserveList) {
        this.reserveList = reserveList;
    }

    @NonNull
    @Override
    public ReserveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reserve_layout, parent, false);
        return new ReserveHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ReserveHolder holder, int position) {
        Reserve reserve = reserveList.get(position);

        holder.dateStart.setText("From: " + reserve.getDateStart());
        holder.dateEnd.setText("To: " + reserve.getDateEnd());
        holder.cost.setText("Cost: " + reserve.getCost()+"$");
        holder.rating.setRating((float) reserve.getRating());
        Glide.with(context).load(reserve.getImage0()).into(holder.roomImage);
    }

    @Override
    public int getItemCount() {
        return reserveList.size();
    }

    protected class ReserveHolder extends RecyclerView.ViewHolder{
        private LinearLayout container;
        private ImageView roomImage;
        private ImageView removeImage;
        private TextView dateStart;
        private TextView dateEnd;
        private TextView cost;
        private RatingBar rating;
        private Button service;
        public ReserveHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            container = itemView.findViewById(R.id.reserveLayout_ll_container);
            roomImage = itemView.findViewById(R.id.reserveLayout_iv_roomImage);
            removeImage = itemView.findViewById(R.id.reserveLayout_iv_removeReserved);
            dateStart = itemView.findViewById(R.id.reserveLayout_tv_dateStart);
            dateEnd = itemView.findViewById(R.id.reserveLayout_tv_dateEnd);
            cost = itemView.findViewById(R.id.reserveLayout_tv_cost);
            rating = itemView.findViewById(R.id.reserveLayout_rb_rating);

            service = itemView.findViewById(R.id.reserveLayout_btn_service);

            service.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            removeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                            listener.onRemoveClick(position);
                    }
                }
            });


        }
    }
}
