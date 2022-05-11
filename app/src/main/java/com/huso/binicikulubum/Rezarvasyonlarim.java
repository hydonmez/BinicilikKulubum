package com.huso.binicikulubum;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class Rezarvasyonlarim extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    RecyclerView rezarvasyonlarimrecyclerview;
    recyclerview_rezarvasyonlarim_adapter recyclerview_rezarvasyonlarim_adapter;
    ArrayList<rezarvasyonlardizisi> rezarvasyonidlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezarvasyonlarim);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        rezarvasyonlarimrecyclerview=findViewById(R.id.rezarvasyonlarim_recyclerView);

        rezarvasyonidlist=new ArrayList<>();
        rezarvasyonlarim();
    }

    public void rezarvasyonlarim(){
        firebaseFirestore.collection("Rezarvasyonlar").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {
                rezarvasyonidlist.clear();
                if (querySnapshot!=null) {
                    for (DocumentSnapshot snapshot : querySnapshot.getDocuments()) {
                        String baslangicdate=(String) snapshot.getString("baslangicdate");
                        String bitisdate=(String) snapshot.getString("bitisdate");
                        String saat=(String) snapshot.getString("saat");
                        String basvurankisi=(String) snapshot.getString("basvurankisi");
                        String basvurulanhoca=(String) snapshot.getString("basvurulanhoca");
                        String rezarvasyonid=snapshot.getId();
                        String Sayfadakikisi=firebaseUser.getUid();

                        if (Sayfadakikisi.matches(basvurankisi) || Sayfadakikisi.matches("jjQp7YV7OFgiyT4My8NnzpEFFo13")){

                            rezarvasyonlardizisi rezarvasyonlardizis=new rezarvasyonlardizisi(baslangicdate,bitisdate,saat,basvurankisi,basvurulanhoca,rezarvasyonid);
                            rezarvasyonidlist.add(rezarvasyonlardizis);//cekilen is ilanlarini bir liste yapisi seklinde diziye atiyor

                            rezarvasyonlarimrecyclerview.setLayoutManager(new LinearLayoutManager(Rezarvasyonlarim.this));//recyclerview ın kaydırılması
                            recyclerview_rezarvasyonlarim_adapter = new recyclerview_rezarvasyonlarim_adapter(rezarvasyonidlist,firebaseFirestore,firebaseUser,firebaseAuth);//messag gonderen kisi ve mesagın adını adaptera gonderiyoruz
                            rezarvasyonlarimrecyclerview.setAdapter(recyclerview_rezarvasyonlarim_adapter);//burada recyclerview ile adaptera bağlıyoruz
                            recyclerview_rezarvasyonlarim_adapter.notifyDataSetChanged();

                        }

                    }
                }
            }
        });

    }
}