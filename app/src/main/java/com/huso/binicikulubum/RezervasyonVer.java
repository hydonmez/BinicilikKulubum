package com.huso.binicikulubum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.EventListener;

public class RezervasyonVer extends AppCompatActivity {
    RecyclerView sehirlerrcyclerview;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    recyclerview_sehir_adapter recyclerview_sehir_adapter;
    ArrayList<String> sehiradiarray;
    ArrayList<String> baslangicdatearray;
    ArrayList<String> surearray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezervasyon_ver);

        sehirlerrcyclerview=findViewById(R.id.sehirler_recyclerView);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();

        sehiradiarray=new ArrayList<>();
        baslangicdatearray=new ArrayList<>();
        surearray=new ArrayList<>();
        sehirler();


    }
    public void sehirler(){
        firebaseFirestore.collection("Sehirler").addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable QuerySnapshot querySnapshot, @androidx.annotation.Nullable FirebaseFirestoreException error) {
                if (querySnapshot!=null) {
                    for (DocumentSnapshot snapshot : querySnapshot.getDocuments()) {
                        String sehiradi=(String) snapshot.getString("Sehiradi");
                        String sayfadakikisi=firebaseUser.getUid();

                        sehiradiarray.add(sehiradi);

                        sehirlerrcyclerview.setLayoutManager(new LinearLayoutManager(RezervasyonVer.this));//recyclerview ın kaydırılması
                        recyclerview_sehir_adapter = new recyclerview_sehir_adapter(sehiradiarray,firebaseFirestore,firebaseUser,storageReference);//messag gonderen kisi ve mesagın adını adaptera gonderiyoruz
                        sehirlerrcyclerview.setAdapter(recyclerview_sehir_adapter);//burada recyclerview ile adaptera bağlıyoruz
                        recyclerview_sehir_adapter.notifyDataSetChanged();


                    }
                }
            }
        });

    }
}