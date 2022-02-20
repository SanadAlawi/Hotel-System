package com.example.mainactivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReceptionistAdapter extends RecyclerView.Adapter<ReceptionistAdapter.ReceptionistHolder> {
    private ArrayList<Receptionist> receptionistList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onClickListener(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public ReceptionistAdapter(ArrayList<Receptionist> receptionistList, Context context) {
        this.receptionistList = receptionistList;
        this.context = context;
    }

    public ReceptionistAdapter(Context context) {
        this.context = context;
    }


    public void setReceptionistList(ArrayList<Receptionist> receptionistList) {
        this.receptionistList = receptionistList;
    }

    @NonNull
    @Override
    public ReceptionistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.receptionist_layout, parent, false);
        return new ReceptionistHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceptionistHolder holder, int position) {
        Receptionist receptionist = receptionistList.get(position);

        holder.receptionistID.setText(receptionist.getReceptionistID());
        holder.receptionistName.setText(receptionist.getReceptionistName());
        holder.receptionistService.setText(receptionist.getReceptionistService());
    }

    @Override
    public int getItemCount() {
        return receptionistList.size();
    }

    public class ReceptionistHolder extends RecyclerView.ViewHolder{

        private LinearLayout container;
        private TextView receptionistID;
        private TextView receptionistName;
        private TextView receptionistService;

        public ReceptionistHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            container = itemView.findViewById(R.id.receptionistActivity_ll_container);
            receptionistID = itemView.findViewById(R.id.receptionistActivity_tv_receptionistID);
            receptionistName = itemView.findViewById(R.id.receptionistActivity_tv_receptionistName);
            receptionistService = itemView.findViewById(R.id.receptionistActivity_tv_receptionistService);

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listener.onClickListener(position);
                        }
                    }
                }
            });
        }
    }
}
