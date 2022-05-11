package com.huso.binicikulubum;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Anasayfa extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    TextView rezarvasyonverTextview,Rezarvasyonlartextview,profilyazitextview;
    ImageView rezarvasyonverimageview,profilresimimageview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        final String sayfadakikisi=firebaseUser.getUid();
        rezarvasyonverTextview=findViewById(R.id.Rezarvasyon_ver_textview);
        Rezarvasyonlartextview=findViewById(R.id.Rezarvasyonlar_textview);
        rezarvasyonverimageview=findViewById(R.id.rezarvasyon_ver_imageview);
        profilyazitextview=findViewById(R.id.profil_yazi_textview);
        profilresimimageview=findViewById(R.id.profilresmi_imageview);


        if (sayfadakikisi.matches("jjQp7YV7OFgiyT4My8NnzpEFFo13")){
            rezarvasyonverTextview.setText("Eğitmen Ekle");
            profilyazitextview.setText("Çiftlik Ekle");
            Rezarvasyonlartextview.setText("Rezervasyonlar");
            Drawable myDrawable = getResources().getDrawable(R.drawable.ic_baseline_person_add_alt_1_24);
            rezarvasyonverimageview.setImageDrawable(myDrawable);
            Drawable myDrawable2 = getResources().getDrawable(R.drawable.ic_baseline_castle_24);
            profilresimimageview.setImageDrawable(myDrawable2);

        }

    }

    //Asagidaki butonlarda hangi sayfalara yonlendirilecegini gosterir
    public void cvduzenlebutonu(View view){
        final String sayfadakikisi=firebaseUser.getUid();
        if (sayfadakikisi.matches("jjQp7YV7OFgiyT4My8NnzpEFFo13")) {
            Intent intent=new Intent(Anasayfa.this,CiftlikEkle.class);
            startActivity(intent);
        }else {
            Intent intent=new Intent(Anasayfa.this,CvSayfasi.class);
            startActivity(intent);
        }


    }
    public void Rezervasyonverbutonu(View view){
        final String sayfadakikisi=firebaseUser.getUid();
        if (sayfadakikisi.matches("jjQp7YV7OFgiyT4My8NnzpEFFo13")) {
            Intent intent=new Intent(Anasayfa.this,EgitmenEkle.class);
            startActivity(intent);
        }else {
            Intent intent=new Intent(Anasayfa.this,RezervasyonVer.class);
            startActivity(intent);
        }

    }
    public void Rezarvasyonlarimbutonu(View view){
        Intent intent=new Intent(Anasayfa.this,Rezarvasyonlarim.class);
        startActivity(intent);

    }

    public void cikisbutonu(View view){
        AlertDialog.Builder builder=new AlertDialog.Builder(Anasayfa.this);
        builder.setTitle("ÇIKIŞ YAP");
        builder.setMessage("Çıkış Yapmak İstermisiniz ?");
        builder.setNegativeButton("HAYIR",null);
        builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {//cikis islemlerini gerceklestirir
                firebaseAuth.signOut();
                Intent intenttosignup=new Intent(Anasayfa.this,KullaniciGirisSayfasi.class);
                startActivity(intenttosignup);
                finish();
            }
        });
        builder.show();
    }

}