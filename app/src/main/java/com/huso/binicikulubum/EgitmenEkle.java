package com.huso.binicikulubum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class EgitmenEkle extends AppCompatActivity {
    private AutoCompleteTextView cinsiyetspinner,sehirspinner;
    private TextInputEditText advesoyadedittext,tecrubeedittext;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egitmen_ekle);

        cinsiyetspinner=findViewById(R.id.cinsiyetegitmen_spinner);
        sehirspinner=findViewById(R.id.sehir_spinner);
        advesoyadedittext=findViewById(R.id.advesoyad_egitmen_edittext);
        tecrubeedittext=findViewById(R.id.tecrube_egitmen_edittext);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        ArrayAdapter<String> cinsiyetadapter=new ArrayAdapter<String>(EgitmenEkle.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.cinsiyet));
        cinsiyetadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cinsiyetspinner.setAdapter(cinsiyetadapter);

        ArrayAdapter<String> sehiradapter=new ArrayAdapter<String>(EgitmenEkle.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.sehir));
        sehiradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sehirspinner.setAdapter(sehiradapter);
    }

    public void Egitmenkaydetbutonu(View view){
        String advesoyad=advesoyadedittext.getText().toString();
        String tecrube=tecrubeedittext.getText().toString();
        String cinsiyet=cinsiyetspinner.getText().toString();
        String sehir=sehirspinner.getText().toString();

        if (advesoyad.matches("") || tecrube.matches("")){
            Toast.makeText(EgitmenEkle.this,"Boş alan mevcuttur...",Toast.LENGTH_LONG).show();

        }else {
            if (sehir.matches("Cide")){
                HashMap<String,Object> EgitmenEkledata=new HashMap<>();
                EgitmenEkledata.put("advesoyad",advesoyad);
                EgitmenEkledata.put("sure",tecrube);
                EgitmenEkledata.put("cinsiyet",cinsiyet);
                EgitmenEkledata.put("sehir",sehir);
                firebaseFirestore.collection("CideEgitmenleri").add(EgitmenEkledata).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(EgitmenEkle.this,"Eğitmen başarıyla eklenmiştir...",Toast.LENGTH_LONG).show();
                        advesoyadedittext.setText("");
                        tecrubeedittext.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EgitmenEkle.this,"Eğitmen eklenemedi...",Toast.LENGTH_LONG).show();
                    }
                });

            }else {
                HashMap<String,Object> EgitmenEkledata=new HashMap<>();
                EgitmenEkledata.put("advesoyad",advesoyad);
                EgitmenEkledata.put("sure",tecrube);
                EgitmenEkledata.put("cinsiyet",cinsiyet);
                EgitmenEkledata.put("sehir",sehir);
                firebaseFirestore.collection("GaziantepEgitmenleri").add(EgitmenEkledata).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(EgitmenEkle.this,"Eğitmen başarıyla eklenmiştir...",Toast.LENGTH_LONG).show();
                        advesoyadedittext.setText("");
                        tecrubeedittext.setText("");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EgitmenEkle.this,"Eğitmen eklenemedi...",Toast.LENGTH_LONG).show();
                    }
                });
            }

        }

    }
}