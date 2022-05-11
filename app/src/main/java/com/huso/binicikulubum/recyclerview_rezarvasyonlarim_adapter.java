package com.huso.binicikulubum;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class recyclerview_rezarvasyonlarim_adapter extends RecyclerView.Adapter<recyclerview_rezarvasyonlarim_adapter.Postrezarvasyonlarim> {
    ArrayList<rezarvasyonlardizisi> rezarvasyonidlist;
    ArrayList<rezarvasyonlardizisi> getrezarvasyonidlist;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;



    public recyclerview_rezarvasyonlarim_adapter(ArrayList<rezarvasyonlardizisi> rezarvasyonidlist, FirebaseFirestore firebaseFirestore, FirebaseUser firebaseUser, FirebaseAuth firebaseAuth) {
        this.rezarvasyonidlist=rezarvasyonidlist;
        this.getrezarvasyonidlist=rezarvasyonidlist;
        this.firebaseFirestore = firebaseFirestore;
        this.firebaseUser = firebaseUser;
        this.firebaseAuth = firebaseAuth;
    }

    @NonNull
    @Override
    public Postrezarvasyonlarim onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.rezarvasyonlarim_recyclerview,parent,false);
        return new recyclerview_rezarvasyonlarim_adapter.Postrezarvasyonlarim(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Postrezarvasyonlarim holder, int position) {
        Context context=holder.itemView.getContext();
        holder.basvurulanhocaadsoyadtextview.setText(rezarvasyonidlist.get(holder.getAdapterPosition()).getBasvurulanhocaarray());
        holder.baslangicvebitistarihtextview.setText(rezarvasyonidlist.get(holder.getAdapterPosition()).getBaslangicdatearray());
        holder.basvurulansaattextview.setText(rezarvasyonidlist.get(holder.getAdapterPosition()).getSaatarray());
        firebaseFirestore.collection("Profiller").document(rezarvasyonidlist.get(position).getBasvurankisiarray()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null) {
                    String advesoyad = documentSnapshot.getString("advesoyad");
                    holder.basvurankisiadsoyadtextview.setText(advesoyad);

                }
            }
        });

        holder.basvuruyuiptaletimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Rezarvasyonu İptal Et");
                builder.setMessage("İptal Etmek İstermisiniz ?");
                builder.setNegativeButton("HAYIR",null);
                builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//cikis islemlerini gerceklestirir
                        firebaseFirestore.collection("Rezarvasyonlar").document(rezarvasyonidlist.get(holder.getAdapterPosition()).getRezarvasyonidarray()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context,"Başarıyla iptal edildi",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                builder.show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return rezarvasyonidlist.size();
    }

    class Postrezarvasyonlarim extends RecyclerView.ViewHolder{
        TextView basvurankisiadsoyadtextview;
        TextView basvurulanhocaadsoyadtextview;
        TextView baslangicvebitistarihtextview;
        TextView basvurulansaattextview;
        Button basvuruyuiptaletimage;
        public Postrezarvasyonlarim(@NonNull View itemView) {
            super(itemView);

            basvurankisiadsoyadtextview=itemView.findViewById(R.id.basvurankisininadvesoyad_textview);
            basvurulanhocaadsoyadtextview=itemView.findViewById(R.id.basvurulanhoca_textview);
            baslangicvebitistarihtextview=itemView.findViewById(R.id.baslangicvebitistarih_textview);
            basvurulansaattextview=itemView.findViewById(R.id.basvurulansaat_textview);
            basvuruyuiptaletimage=itemView.findViewById(R.id.basvuruyuiptaletmebutonu_imageview);
        }
    }
}
