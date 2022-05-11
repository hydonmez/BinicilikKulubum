package com.huso.binicikulubum;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class recyclerview_sehir_adapter extends RecyclerView.Adapter<recyclerview_sehir_adapter.Postsehir> {
    ArrayList<String> sehiradiarray;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    StorageReference storageReference;

    public recyclerview_sehir_adapter(ArrayList<String> sehiradiarray, FirebaseFirestore firebaseFirestore, FirebaseUser firebaseUser, StorageReference storageReference) {
        this.sehiradiarray = sehiradiarray;
        this.firebaseFirestore = firebaseFirestore;
        this.firebaseUser = firebaseUser;
        this.storageReference = storageReference;
    }

    @NonNull
    @Override
    public Postsehir onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.sehirler_recyclerview,parent,false);
        return new Postsehir(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Postsehir holder, int position) {
        holder.sehiraditextview.setText(sehiradiarray.get(holder.getAdapterPosition()));
        Context context=holder.itemView.getContext();
        StorageReference newreference=storageReference.child("Sehirler/" +sehiradiarray.get(holder.getAdapterPosition())+".jpg");
        newreference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String downloadurl=uri.toString();
                Picasso.get().load(downloadurl).into(holder.sehirimageview);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(e.getLocalizedMessage());
            }
        });

        holder.egitmenlerigorbutonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,EgitmenleriGorRezarvasyonyap.class);
                intent.putExtra("sehiradi",sehiradiarray.get(holder.getAdapterPosition()));
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return sehiradiarray.size();
    }

    public class Postsehir extends RecyclerView.ViewHolder{
        TextView sehiraditextview;
        ImageView sehirimageview;
        Button egitmenlerigorbutonu;

        public Postsehir(@NonNull View itemView) {
            super(itemView);
            sehiraditextview=itemView.findViewById(R.id.sehiradi_textview);
            sehirimageview=itemView.findViewById(R.id.sehir_imageview);
            egitmenlerigorbutonu=itemView.findViewById(R.id.atlarigor_butonu);
        }
    }
}
