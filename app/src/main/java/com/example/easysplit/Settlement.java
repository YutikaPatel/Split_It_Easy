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
import android.widget.Toast;

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
       // ArrayList<String> membermails= new ArrayList<String>();
        parentLL = findViewById(R.id.parentLL);

        String memName= new String();

            DocumentReference ref = FirebaseFirestore.getInstance().collection("Trips").document(tripId);



        db.collection("Trips").document(tripId).collection("memberlist").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                  //  Toast.makeText(Settlement.this,"before for  "+mailIds.size()+"" ,Toast.LENGTH_SHORT).show();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        mailIds.add(document.getId());

                       // Toast.makeText(Settlement.this,"in for  " +mailIds.size()+"",Toast.LENGTH_SHORT).show();

                    }
                    //Toast.makeText(Settlement.this,"after for  "+mailIds.size()+"" ,Toast.LENGTH_SHORT).show();



                   // Toast.makeText(Settlement.this,"Navinnnnnnnnn for  "+mailIds.size()+"" ,Toast.LENGTH_SHORT).show();

                    for (int i = 0; i < mailIds.size(); i++) {

                        DocumentReference ref2 = FirebaseFirestore.getInstance().collection("Trips").document(tripId).collection("memberlist").document(mailIds.get(i));
                        String currentMailId = mailIds.get(i);
                        ref2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                if (documentSnapshot.exists()) {
                                    try {

                                        //EvenTester event1 = documentSnapshot.toObject(EvenTester.class);
                                        Toast.makeText(Settlement.this,"memeber size"+members.size() ,Toast.LENGTH_LONG).show();
                                        //  Toast.makeText(EventsPage.this, "Name: " + event1.getName()+"bill"+event1.getBill()+ "\n" , Toast.LENGTH_SHORT).show();
                                        //+ "cb: " + event1.getBill()+ "pb: "+event1.paidBy.get("Yutika")
                                        //+ "pb: "+event1.paidBy.get("Yutika").toString()
                                        String paid = documentSnapshot.getString("paid");
                                        String toPay = documentSnapshot.getString("toPay");
                                        String Name=documentSnapshot.getString("Name");
                                        members.put(Name, new ArrayList<Double>());
                                        ArrayList<Double> pp = members.get(Name);
                                        pp.add(Double.parseDouble(paid));
                                        pp.add(Double.parseDouble(toPay));
                                        members.put(Name, pp);

                                    } catch (Exception e) {
                                        // Toast.makeText(EventsPage.this, "NAy banla object", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    //Toast.makeText(EventsPage.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Toast.makeText(EventsPage.this, "Error!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
                ;
            }

        });



        CountDownTimer cTimer=null;

        cTimer = new CountDownTimer(5000, 100) {
            public void onTick(long millisUntilFinished) {
                Toast.makeText(Settlement.this, "wait", Toast.LENGTH_SHORT).show();
                // Toast.makeText(EventDetails.this,"aahe aahe", Toast.LENGTH_SHORT).show();

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onFinish() {

                int idTv=0;
                TextView b;
                LinearLayout.LayoutParams params1;

                for (Map.Entry<String,ArrayList<Double>> entry : members.entrySet()){
                   //  entry.getKey() entry.getValue());
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

                //System.out.println();
                payers.sort(new PayerSorterDes());


                for(int i=0;i<payers.size();i++) {
                    if(payers.get(i).amount<receivers.get(0).amount) {
                        receivers.get(0).amount -= payers.get(i).amount;
                        str.add(payers.get(i).name+" owes Rs"+payers.get(i).amount+" to "+receivers.get(0).name);
                        receivers.sort(new ReceiverSorter());
                        payers.remove(i);
                        i--;
                    }
                    else if(payers.get(i).amount==receivers.get(0).amount) {
                        str.add(payers.get(i).name+" owes Rs"+payers.get(i).amount+" to "+receivers.get(0).name);
                        payers.remove(i);
                        i--;
                        receivers.remove(0);
                    }
                    else {
                        continue;
                    }
                }
                if(payers.size()!=0) {
                    receivers.sort(new ReceiverSorter());
                    payers.sort(new PayerSorter());
                    while(payers.size()!=0) {
                        if(payers.get(0).amount<receivers.get(0).amount) {
                            str.add(payers.get(0).name+" owes Rs"+payers.get(0).amount+" to "+receivers.get(0).name);
                            receivers.get(0).amount -= payers.get(0).amount;
                            payers.remove(0);
                        }
                        else if(payers.get(0).amount==receivers.get(0).amount) {
                            str.add(payers.get(0).name+" owes Rs"+payers.get(0).amount+" to "+receivers.get(0).name);
                            payers.remove(0);
                            receivers.remove(0);
                        }
                        else {
                            str.add(payers.get(0).name+" owes Rs"+receivers.get(0).amount+" to "+receivers.get(0).name);
                            payers.get(0).amount -= receivers.get(0).amount;
                            receivers.remove(0);
                        }
                    }

                }



                for(int r=0;r<str.size();r++){

                    b= new TextView(Settlement.this);
                    b.setId(idTv);
                    params1= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    params1.setMargins(40,50,0,45);
                    b.setLayoutParams(params1);
                    b.setGravity(Gravity.FILL);
                    // participants.setPadding(,17,0,17);
                    b.setText(str.get(r));
                    b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                    b.setTypeface(null, Typeface.BOLD);;
                    b.setBackgroundColor(Color.rgb(0,220,220));
                    b.getBackground().setAlpha(50);
                    parentLL.addView(b);
                    // parentLLArrList.get(i).addView(b);


                }



                Toast.makeText(Settlement.this,members.size()+"  "+members.get("haha")+" "+mailIds.size(), Toast.LENGTH_SHORT).show();

            }
        };

        cTimer.start();




    }
}