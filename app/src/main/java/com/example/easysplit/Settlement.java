package com.example.easysplit;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
//import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Payer{
    String name;
    double amount;

    Payer(String nm, double amt){
        this.name = nm;
        this.amount = amt;
    }
}

class Receiver{
    String name;
    double amount;

    Receiver(String nm, double amt){
        this.name = nm;
        this.amount = amt;
    }
}

class PayerSorterDes implements Comparator<Payer>{
    public int compare(Payer o1, Payer o2) {
        int i=1,j=-1,k=0;
        if(o2.amount>o1.amount) { return i;}
        else if(o2.amount<o1.amount) { return j;}
        else return k;
    }
}

class PayerSorter implements Comparator<Payer>{
    public int compare(Payer o1, Payer o2) {
        int i=1,j=-1,k=0;
        if(o1.amount>o2.amount) { return i;}
        else if(o1.amount<o2.amount) { return j;}
        else return k;
    }
}

class ReceiverSorter implements Comparator<Receiver> {
    public int compare(Receiver o1, Receiver o2) {
        int i=1,j=-1,k=0;
        if(o2.amount>o1.amount) { return i;}
        else if(o2.amount<o1.amount) { return j;}
        else return k;
    }
}

public class Settlement extends AppCompatActivity {

    List<String> mailIds = new ArrayList<String>();
    ArrayList<String> str = new ArrayList<String>();
    String tripId;

    HashMap<String, ArrayList<Double>> members = new HashMap<String, ArrayList<Double>>();
    ArrayList<Receiver> receivers= new ArrayList<Receiver>();
    ArrayList<Payer> payers= new ArrayList<Payer>();
    LinearLayout parentLL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement);
        Intent prev_intent = getIntent();
       tripId=prev_intent.getStringExtra("tripId");


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        parentLL = findViewById(R.id.parentLL);

        String memName= new String();

            DocumentReference ref = FirebaseFirestore.getInstance().collection("Trips").document(tripId);



        db.collection("Trips").document(tripId).collection("memberlist").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        mailIds.add(document.getId());

                    }

                    for (int i = 0; i < mailIds.size(); i++) {

                        DocumentReference ref2 = FirebaseFirestore.getInstance().collection("Trips").document(tripId).collection("memberlist").document(mailIds.get(i));
                        String currentMailId = mailIds.get(i);
                        ref2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                if (documentSnapshot.exists()) {
                                    try {


                                        String paid = documentSnapshot.getString("paid");
                                        String toPay = documentSnapshot.getString("toPay");
                                        String Name=documentSnapshot.getString("Name");
                                        members.put(Name, new ArrayList<Double>());
                                        ArrayList<Double> pp = members.get(Name);
                                        pp.add(Double.parseDouble(paid));
                                        pp.add(Double.parseDouble(toPay));
                                        members.put(Name, pp);

                                    } catch (Exception e) {
                                    }

                                } else {

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                    }

                } else {

                }
                ;
            }

        });



        CountDownTimer cTimer=null;

        cTimer = new CountDownTimer(3000, 70) {
            public void onTick(long millisUntilFinished) {


            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onFinish() {

                int idTv=0;
                TextView b;
                LinearLayout.LayoutParams params1;

                for (Map.Entry<String,ArrayList<Double>> entry : members.entrySet()){

                    String name=entry.getKey();
                    ArrayList<Double> pp=entry.getValue();
                    double paid=pp.get(0);
                    double toPay=pp.get(1);
                    double diff= paid-toPay;
                    if(diff>0){
                        //receiver
                        Receiver receiver= new Receiver(name,diff);
                        receivers.add(receiver);
                    }else if(diff<0){
                        diff=diff*-1;
                        Payer payer= new Payer(name,diff);
                        payers.add(payer);
                    }

                }

                receivers.sort(new ReceiverSorter());
                payers.sort(new PayerSorterDes());


                for(int i=0;i<payers.size();i++) {
                    if(payers.get(i).amount<receivers.get(0).amount) {
                        receivers.get(0).amount -= payers.get(i).amount;
                        str.add(payers.get(i).name+" owes ₹"+payers.get(i).amount+" to "+receivers.get(0).name);
                        receivers.sort(new ReceiverSorter());
                        payers.remove(i);
                        i--;
                    }
                    else if(payers.get(i).amount==receivers.get(0).amount) {
                        str.add(payers.get(i).name+" owes ₹"+payers.get(i).amount+" to "+receivers.get(0).name);
                        payers.remove(i);
                        i--;
                        receivers.remove(0);
                    }
                    else {
                        continue;
                    }
                }
                if(payers.size()!=0 && receivers.size()!=0) {
                    receivers.sort(new ReceiverSorter());
                    payers.sort(new PayerSorter());
                    while(payers.size()!=0&& receivers.size()!=0) {
                        if(payers.get(0).amount<receivers.get(0).amount) {
                            str.add(payers.get(0).name+" owes ₹"+payers.get(0).amount+" to "+receivers.get(0).name);
                            receivers.get(0).amount -= payers.get(0).amount;
                            payers.remove(0);
                        }
                        else if(payers.get(0).amount==receivers.get(0).amount) {
                            str.add(payers.get(0).name+" owes ₹"+payers.get(0).amount+" to "+receivers.get(0).name);
                            payers.remove(0);
                            receivers.remove(0);
                        }
                        else {
                            str.add(payers.get(0).name+" owes ₹"+receivers.get(0).amount+" to "+receivers.get(0).name);
                            payers.get(0).amount -= receivers.get(0).amount;
                            receivers.remove(0);
                        }
                    }

                }

                for(int r=0;r<str.size();r++){

                    b= new TextView(Settlement.this);
                    b.setId(idTv);
                    params1= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    params1.setMargins(30,20,30,20);
                    b.setLayoutParams(params1);
                    b.setGravity(Gravity.FILL);
                    b.setPadding(17,60,17,60);
                    b.setText(str.get(r));
                    b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                    b.setTypeface(null, Typeface.BOLD);;
                    b.setBackgroundColor(Color.rgb(0,220,220));
                    b.getBackground().setAlpha(50);
                    parentLL.addView(b);



                }


            }
        };

        cTimer.start();


    }

    @Override
    public void onBackPressed(){
        Log.d("LOGS","Going back");
        Intent intent = new Intent(Settlement.this,MainActivity.class);
        startActivity(intent);
    }
}