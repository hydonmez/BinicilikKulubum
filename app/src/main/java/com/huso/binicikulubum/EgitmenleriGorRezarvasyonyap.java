package com.huso.binicikulubum;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class EgitmenleriGorRezarvasyonyap extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    ArrayList<EgitmenlerDizisi> Egitmenidlist;
    RecyclerView EgitmenlerinRecyclerview;
    recyclerview_atlar_adapter recyclerview_atlar_adapter;
    ArrayList<String> baslangicdatearray;
    ArrayList<String> surearray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egitmenleri_gor_rezarvasyonyap);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        EgitmenlerinRecyclerview=findViewById(R.id.egitmenlerin_recyclerView);
        baslangicdatearray=new ArrayList<>();
        surearray=new ArrayList<>();

        Egitmenidlist=new ArrayList<>();
        FragmentManager manager=getSupportFragmentManager();


        Intent intent=getIntent();//isilanlari adapterdaki gonderilen degeri burada aliyoruz
        String sehiradi=intent.getStringExtra("sehiradi");
        if (sehiradi.matches("Cide")){
            firebaseFirestore.collection("CideEgitmenleri").addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@androidx.annotation.Nullable QuerySnapshot querySnapshot, @androidx.annotation.Nullable FirebaseFirestoreException error) {
                    Egitmenidlist.clear();
                    if (querySnapshot!=null) {
                        for (DocumentSnapshot snapshot : querySnapshot.getDocuments()) {
                            String advesoyad=(String) snapshot.getString("advesoyad");
                            String sure=(String) snapshot.getString("sure");
                            String cinsiyet=(String) snapshot.getString("cinsiyet");


                            EgitmenlerDizisi EgitmenlerDizisi=new EgitmenlerDizisi(advesoyad,sure,cinsiyet);
                            Egitmenidlist.add(EgitmenlerDizisi);//cekilen is ilanlarini bir liste yapisi seklinde diziye atiyor


                            EgitmenlerinRecyclerview.setLayoutManager(new LinearLayoutManager(EgitmenleriGorRezarvasyonyap.this));//recyclerview ın kaydırılması
                            recyclerview_atlar_adapter = new recyclerview_atlar_adapter(Egitmenidlist,firebaseFirestore,firebaseUser,storageReference,firebaseAuth,manager,baslangicdatearray,surearray);//messag gonderen kisi ve mesagın adını adaptera gonderiyoruz
                            EgitmenlerinRecyclerview.setAdapter(recyclerview_atlar_adapter);//burada recyclerview ile adaptera bağlıyoruz
                            recyclerview_atlar_adapter.notifyDataSetChanged();

                        }
                    }
                }
            });
        }
        if(sehiradi.matches("Gaziantep")){
            firebaseFirestore.collection("GaziantepEgitmenleri").addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@androidx.annotation.Nullable QuerySnapshot querySnapshot, @androidx.annotation.Nullable FirebaseFirestoreException error) {
                    Egitmenidlist.clear();
                    baslangicdatearray.clear();
                    if (querySnapshot!=null) {
                        for (DocumentSnapshot snapshot : querySnapshot.getDocuments()) {
                            String advesoyad=(String) snapshot.getString("advesoyad");
                            String sure=(String) snapshot.getString("sure");
                            String cinsiyet=(String) snapshot.getString("cinsiyet");


                            EgitmenlerDizisi EgitmenlerDizisi=new EgitmenlerDizisi(advesoyad,sure,cinsiyet);
                            Egitmenidlist.add(EgitmenlerDizisi);//cekilen is ilanlarini bir liste yapisi seklinde diziye atiyor

                            EgitmenlerinRecyclerview.setLayoutManager(new LinearLayoutManager(EgitmenleriGorRezarvasyonyap.this));//recyclerview ın kaydırılması
                            recyclerview_atlar_adapter = new recyclerview_atlar_adapter(Egitmenidlist,firebaseFirestore,firebaseUser,storageReference,firebaseAuth,manager,baslangicdatearray,surearray);//messag gonderen kisi ve mesagın adını adaptera gonderiyoruz
                            EgitmenlerinRecyclerview.setAdapter(recyclerview_atlar_adapter);//burada recyclerview ile adaptera bağlıyoruz
                            recyclerview_atlar_adapter.notifyDataSetChanged();



                        }
                    }
                }
            });
        }

    }
}