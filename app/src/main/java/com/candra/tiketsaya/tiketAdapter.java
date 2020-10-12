package com.candra.tiketsaya;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class tiketAdapter extends RecyclerView.Adapter<tiketAdapter.MyViewHolder> {

    Context context;
    ArrayList<myTiket> myTikets;

    public tiketAdapter(Context c, ArrayList<myTiket> a){
        context = c;
        myTikets = a;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_mytiket,
                        viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.nama_destinasi.setText(myTikets.get(i).getNama_wisata());
        myViewHolder.namakota.setText(myTikets.get(i).getLokasi());
        myViewHolder.total_tiket.setText(myTikets.get(i).getJumlah_tiket() + " Ticket");

        final String getNamaWisata = myTikets.get(i).getNama_wisata();

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MyTiketDetails.class);
                intent.putExtra("nama_wisata", getNamaWisata);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myTikets.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nama_destinasi, namakota, total_tiket;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nama_destinasi = itemView.findViewById(R.id.nama_destinasi);
            namakota = itemView.findViewById(R.id.namakota);
            total_tiket = itemView.findViewById(R.id.total_tiket);
        }
    }
}
