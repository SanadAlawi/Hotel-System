package com.example.mainactivity;

import android.annotation.SuppressLint;
import android.content.Context;
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

public class CustomerRoomAdapter extends RecyclerView.Adapter<CustomerRoomAdapter.CustomerRoomHolder> {

    private ArrayList<Room> roomList;
    private ArrayList<Room> saveRoomList;
    private Context context;
    private OnItemClickListener listener;



    public interface OnItemClickListener{
        void onItemClick(int position);
        void onFavoriteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public CustomerRoomAdapter(ArrayList<Room> roomList, Context context) {
        this.roomList = roomList;
        this.context = context;
    }

    public CustomerRoomAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(ArrayList<Room> roomList) {

        this.roomList = roomList;
        saveRoomList = roomList;
    }

    @NonNull
    @Override
    public CustomerRoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.room_customer_layout, parent, false);
        return new CustomerRoomHolder(v, listener);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull CustomerRoomHolder holder, int position) {
        Room room = roomList.get(position);

        holder.person.setText(room.getPersonNumber()+" Person");
        holder.cost.setText(room.getCostPerDay()+"$");
        holder.roomNumber.setText("Room Number: " + room.getRoomID());
        holder.rating.setRating(Float.parseFloat(room.getRating()));
        Glide.with(context).load(room.getImage0()).into(holder.roomImage);

        if(room.getLove())
        {
            holder.favoriteImage.setImageResource(R.drawable.ic_favorite);
        }else
        {
            holder.favoriteImage.setImageResource(R.drawable.ic_favorite_border);
        }

    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public void FilterList(ArrayList<Room> FilterRoom){
        roomList = FilterRoom;
        notifyDataSetChanged();
    }

    public void OriginalList(){
        roomList = saveRoomList;
        notifyDataSetChanged();
    }

    public class CustomerRoomHolder extends RecyclerView.ViewHolder{
        private ImageView roomImage;
        private ImageView favoriteImage;
        private TextView person;
        private TextView cost;
        private TextView roomNumber;
        private RatingBar rating;
        private LinearLayout container;

//        private LinearLayout adminProperties;
//        private TextView isReserved;
//        private TextView userID;
        public CustomerRoomHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            roomImage = itemView.findViewById(R.id.customerRoomLayout_iv_roomImage);
            favoriteImage = itemView.findViewById(R.id.customerRoomLayout_iv_favorite);
            person = itemView.findViewById(R.id.customerRoomLayout_tv_person);
            cost = itemView.findViewById(R.id.customerRoomLayout_tv_salary);
            rating = itemView.findViewById(R.id.customerRoomLayout_rb_rating);
            container = itemView.findViewById(R.id.customerRoomLayout_ll_container);
            roomNumber = itemView.findViewById(R.id.customerRoomLayout_tv_roomNumber);

//            adminProperties = itemView.findViewById(R.id.roomLayout_ll_adminProperties);
//            isReserved = itemView.findViewById(R.id.roomLayout_tv_isReserved);
//            userID = itemView.findViewById(R.id.roomLayout_tv_userID);

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

            favoriteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listener.onFavoriteClick(position);
                        }
                    }
                }
            });

        }
    }
}
