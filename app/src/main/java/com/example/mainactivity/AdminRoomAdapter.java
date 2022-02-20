package com.example.mainactivity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdminRoomAdapter extends RecyclerView.Adapter<AdminRoomAdapter.AdminRoomHolder> {

    private ArrayList<Room> roomList;
    private ArrayList<Room> saveList;
    private Context context;
    private OnItemClickListener listener;

    public void OriginalList() {
        roomList = saveList;
        notifyDataSetChanged();
    }

    public void FilterList(ArrayList<Room> FilterRoom){
        roomList = FilterRoom;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public AdminRoomAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(ArrayList<Room> roomList) {
        this.roomList = roomList;
        saveList = roomList;
    }

    @NonNull
    @Override
    public AdminRoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.room_admin_layout, parent, false);
        return new AdminRoomHolder(v, listener);
    }


    @Override
    public void onBindViewHolder(@NonNull AdminRoomHolder holder, int position) {
        Room room = roomList.get(position);

        holder.person.setText(room.getPersonNumber()+" Person");
        holder.cost.setText(room.getCostPerDay()+"$");
        holder.roomNumber.setText("Room Number: " + room.getRoomID());
        holder.rating.setRating(Float.parseFloat(room.getRating()));

        Glide.with(context).load(room.getImage0()).into(holder.roomImage);

        String userID = "000";
        String reserved = "Not Reserved";
        if(room.getUserID().equals("null"))
        {
            userID = "000";
            reserved = "Not Reserved";
            holder.customerID.setText(userID);
            holder.reserved.setText(reserved);
            holder.customerID.setTextColor(Color.rgb(255, 0, 0));
            holder.reserved.setTextColor(Color.rgb(255, 0, 0));
        }else
        {
            userID = room.getUserID();
            reserved = "Reserved";
            holder.customerID.setText(userID);
            holder.reserved.setText(reserved);
            holder.customerID.setTextColor(Color.rgb(0, 180, 0));
            holder.reserved.setTextColor(Color.rgb(0, 180, 0));
        }

    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }


    public class AdminRoomHolder extends RecyclerView.ViewHolder{
        private ImageView roomImage;
        private TextView person;
        private TextView reserved;
        private TextView customerID;
        private TextView cost;
        private TextView roomNumber;
        private RatingBar rating;
        private LinearLayout container;


        public AdminRoomHolder(@NonNull View itemView, OnItemClickListener listener){
            super(itemView);
            roomImage = itemView.findViewById(R.id.adminRoomLayout_iv_roomImage);
            person = itemView.findViewById(R.id.adminRoomLayout_tv_person);
            cost = itemView.findViewById(R.id.adminRoomLayout_tv_salary);
            rating = itemView.findViewById(R.id.adminRoomLayout_rb_rating);
            container = itemView.findViewById(R.id.adminRoomLayout_ll_container);
            roomNumber = itemView.findViewById(R.id.adminRoomLayout_tv_roomNumber);
            reserved = itemView.findViewById(R.id.adminRoomLayout_tv_reserved);
            customerID = itemView.findViewById(R.id.adminRoomLayout_tv_customerID);

            container.setOnClickListener(new View.OnClickListener() {
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
        }
    }

}
