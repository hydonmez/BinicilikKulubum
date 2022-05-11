package com.huso.binicikulubum;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class CiftlikEkle extends AppCompatActivity {
    EditText sehirekleedittext;
    ImageView sehirimageview;
    private ActivityResultLauncher<Intent> activityResultLauncherciftlik;
    private ActivityResultLauncher<String> permisionLauncherciftlik;
    private Bitmap secilenfotociftlik;
    private Uri imagedataciftlik;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ciftlik_ekle);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();

        sehirekleedittext=findViewById(R.id.sehir_ekle_edittext);
        sehirimageview=findViewById(R.id.sehirresmi_imageview);
        registerLauncher();

    }

    public void AtCiftliginiKaydetbutonu(View view){
        resmistrogeaktarma();
        String sehiradi=sehirekleedittext.getText().toString();
        HashMap<String,Object> CiftlikEkledata=new HashMap<>();
        CiftlikEkledata.put("Sehiradi",sehiradi);
        firebaseFirestore.collection("Sehirler").add(CiftlikEkledata).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(CiftlikEkle.this,"Çiftlik başarıyla eklenmiştir...",Toast.LENGTH_LONG).show();
                sehirekleedittext.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CiftlikEkle.this,"Eğitmen eklenemedi...",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void sehirekleimageview(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Galerinize gidip fotoraf secmek icin izin veriyormusunuz?",Snackbar.LENGTH_INDEFINITE).setAction("İzin veriyorum", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        permisionLauncherciftlik.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

                    }
                }).show();
            }else{
                permisionLauncherciftlik.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }else {
            Intent intenttogallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncherciftlik.launch(intenttogallery);

        }
    }

    private void registerLauncher(){
        //secilen fotorafin imageview a ayarlanmasi saglanir
        activityResultLauncherciftlik = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK){
                    Intent intentFromResult=result.getData();
                    if (intentFromResult !=null){
                        imagedataciftlik=intentFromResult.getData();

                        try {
                            if (Build.VERSION.SDK_INT >=28) {//versiyonlarin cesitligine gore islem gerceklestirir
                                ImageDecoder.Source source = ImageDecoder.createSource(CiftlikEkle.this.getContentResolver(), imagedataciftlik);
                                secilenfotociftlik = ImageDecoder.decodeBitmap(source);
                                if (secilenfotociftlik==null){
                                    Drawable myDrawable = getResources().getDrawable(R.drawable.fotoimage);
                                    sehirimageview.setImageDrawable(myDrawable);
                                }else {
                                    sehirimageview.setImageBitmap(secilenfotociftlik);
                                }


                            }else {
                                secilenfotociftlik= MediaStore.Images.Media.getBitmap(CiftlikEkle.this.getContentResolver(),imagedataciftlik);
                                if (secilenfotociftlik==null){
                                    Drawable myDrawable = getResources().getDrawable(R.drawable.fotoimage);
                                    sehirimageview.setImageDrawable(myDrawable);
                                }else {
                                    sehirimageview.setImageBitmap(secilenfotociftlik);
                                }

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        //izin islemlerinin gerceklestirmesi yapilir
        permisionLauncherciftlik=registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result) {
                    Intent intenttogallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncherciftlik.launch(intenttogallery);
                }else {
                    Toast.makeText(CiftlikEkle.this,"İzine İhtiyac Var!",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void resmistrogeaktarma(){//Girilen fotorafi veritabanindaki storage aktarilir
        String sehiradi=sehirekleedittext.getText().toString();
        if(imagedataciftlik!=null) {
            storageReference.child("Sehirler/" + sehiradi + ".jpg").putFile(imagedataciftlik).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CiftlikEkle.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

    }

}