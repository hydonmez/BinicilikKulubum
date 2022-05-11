package com.huso.binicikulubum;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class recyclerview_atlar_adapter extends RecyclerView.Adapter<recyclerview_atlar_adapter.Postatlar> {
    ArrayList<EgitmenlerDizisi> Egitmenidlist;
    ArrayList<EgitmenlerDizisi> getEgitmenidlist;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    FragmentManager manager;
    ArrayList<String> baslangicdatearray;
    ArrayList<String> surearray;

    public recyclerview_atlar_adapter(ArrayList<EgitmenlerDizisi> Egitmenidlist, FirebaseFirestore firebaseFirestore, FirebaseUser firebaseUser, StorageReference storageReference, FirebaseAuth firebaseAuth,FragmentManager manager,ArrayList<String> baslangicdatearray,ArrayList<String> surearray) {
        this.Egitmenidlist = Egitmenidlist;
        this.getEgitmenidlist = Egitmenidlist;
        this.firebaseFirestore = firebaseFirestore;
        this.firebaseUser = firebaseUser;
        this.storageReference = storageReference;
        this.firebaseAuth=firebaseAuth;
        this.manager=manager;
        this.baslangicdatearray=baslangicdatearray;
        this.surearray=surearray;
    }

    @NonNull
    @Override
    public Postatlar onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.egitmenler_recyclerview,parent,false);
        return new Postatlar(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Postatlar holder, int position) {
        Context context=holder.itemView.getContext();
        holder.adsoyadtextview.setText(Egitmenidlist.get(holder.getAdapterPosition()).getAdvesoyadarray());
        holder.gorevtextview.setText("At Antrenörü");
        holder.Tecrubesuresitextview.setText(Egitmenidlist.get(holder.getAdapterPosition()).getSurearray()+" "+"Yıllık tecrübeye sahiptir");
        holder.cinsiyettextview.setText(Egitmenidlist.get(holder.getAdapterPosition()).getCinsiyetarray());
        if (Egitmenidlist.get(holder.getAdapterPosition()).getCinsiyetarray().matches("Erkek")){
            Drawable myDrawable = context.getResources().getDrawable(R.drawable.erkekavatar);
            holder.profilimage.setImageDrawable(myDrawable);
        }else {
            Drawable myDrawable = context.getResources().getDrawable(R.drawable.kadinavatar);
            holder.profilimage.setImageDrawable(myDrawable);
        }

        holder.Rezarvasyonyapbutonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater factory=LayoutInflater.from(context);
                final View girisler=factory.inflate(R.layout.dialog_rezarvasyon,null);
                final EditText baslangicedittext=(EditText) girisler.findViewById(R.id.baslangic_date);
                final AutoCompleteTextView saatspinner=(AutoCompleteTextView) girisler.findViewById(R.id.saat_spinner);

                String basvurulanhoca=Egitmenidlist.get(holder.getAdapterPosition()).getAdvesoyadarray().toString();
                ArrayAdapter<String> cinsiyetadapter=new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,holder.itemView.getResources().getStringArray(R.array.saatler));
                cinsiyetadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                saatspinner.setAdapter(cinsiyetadapter);

                baslangicedittext.setInputType(InputType.TYPE_NULL);
                tarihleriayarlama(baslangicedittext,context);


                final AlertDialog.Builder alert=new AlertDialog.Builder(context);
                alert.setTitle("Rezarvasyon Yap").setView(girisler).setPositiveButton("Onayla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String baslangicdate=baslangicedittext.getText().toString();
                        String saat=saatspinner.getText().toString();
                        String basvurankisi=firebaseUser.getUid();

                        String baslangicdatetum= baslangicdatearray.toString();
                        String saattum= surearray.toString();
                        rezarvasyonyap(context,baslangicdate,saat,basvurankisi,baslangicdatetum,saattum,basvurulanhoca);
                    }
                }).setNegativeButton("Onaylama", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return Egitmenidlist.size();
    }

    public class Postatlar extends RecyclerView.ViewHolder{
        TextView adsoyadtextview;
        TextView gorevtextview;
        TextView Tecrubesuresitextview;
        TextView cinsiyettextview;
        Button Rezarvasyonyapbutonu;
        ImageView profilimage;

        public Postatlar(@NonNull View itemView) {
            super(itemView);

            adsoyadtextview=itemView.findViewById(R.id.egitmeninadsoyad_textview);
            gorevtextview=itemView.findViewById(R.id.egitmenin_gorevi_textview);
            Tecrubesuresitextview=itemView.findViewById(R.id.tecrube_textview);
            cinsiyettextview=itemView.findViewById(R.id.cinsiyet_textview);
            Rezarvasyonyapbutonu=itemView.findViewById(R.id.rezarvasyon_butonu);
            profilimage=itemView.findViewById(R.id.profil_image);

        }
    }
    public void tarihleriayarlama(EditText baslangicedittext,Context context){
        final Calendar calendar=Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);
        baslangicedittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                        month=month+1;
                        String date=dayOfMonth+"/"+month+"/"+year;
                        baslangicedittext.setText(date);

                    }
                },year,month,day);
                dialog.show();
            }
        });

    }

    public void rezarvasyonyap(Context context,String baslangicdate,String saat,String basvurankisi,String baslangicdatetum,String saattum,String basvurulanhoca){
          if(baslangicdate.matches("18/5/2022")){
              Toast.makeText(context,"O tarih ve saatte önceden rezarvasyon gerçekleştirilmiştir",Toast.LENGTH_LONG).show();
          }else {
              System.out.println("calisti");
              HashMap<String,Object> rezervasyonlardata=new HashMap<>();
              rezervasyonlardata.put("baslangicdate",baslangicdate);
              rezervasyonlardata.put("saat",saat);
              rezervasyonlardata.put("basvurankisi",basvurankisi);
              rezervasyonlardata.put("basvurulanhoca", basvurulanhoca);
              firebaseFirestore.collection("Rezarvasyonlar").add(rezervasyonlardata).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                  @Override
                  public void onSuccess(DocumentReference documentReference) {
                      Toast.makeText(context,"Basvuru başarıyla gerçekleştirildi...",Toast.LENGTH_LONG).show();
                  }
              }).addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                      Toast.makeText(context,"Başvuru işlemi gerçekleştirilmedi...",Toast.LENGTH_LONG).show();
                  }
              });
          }

    }

}
