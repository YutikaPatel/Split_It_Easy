package com.example.easysplit;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.List;

public class EventsPage extends AppCompatActivity {

    String tripId="temptrip";
    String eventId="";
    private Button toEventDetails ;
    private Button addEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_page);

        FirebaseFirestore db= FirebaseFirestore.getInstance();
        List<String> list=new ArrayList<String>();

        toEventDetails=findViewById(R.id.toEventDetails);

        DocumentReference ref= FirebaseFirestore.getInstance().collection("Trips").document(tripId);

        db.collection("Trips").document(tripId).collection("eventlist").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    //list = new ArrayList<>();
                    String eventsid="";
                    int i=0;
                    Toast.makeText(EventsPage.this,"before for" ,Toast.LENGTH_SHORT).show();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                        eventsid=eventsid+list.get(i);
                        Toast.makeText(EventsPage.this,"in for" ,Toast.LENGTH_LONG).show();
                        i++;
                    }
                    Toast.makeText(EventsPage.this,"after for" ,Toast.LENGTH_LONG).show();
                  //  Toast.makeText(EventsPage.this,eventsid ,Toast.LENGTH_LONG).show();
                   // eventId=list.get(0);
                    String heh= list.get(0)+list.get(1)+list.get(2);
                    Log.d(TAG, list.toString());
                  //  Toast.makeText(EventsPage.this,heh ,Toast.LENGTH_LONG).show();
                   // Toast.makeText(EventsPage.this,list.get(0) ,Toast.LENGTH_LONG).show();



                    DocumentReference ref2= FirebaseFirestore.getInstance().collection("Trips").document(tripId).collection("eventlist").document(list.get(list.size()-1));

                    ref2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                try{
                                    EvenTester event1 = documentSnapshot.toObject(EvenTester.class);
                                    //Toast.makeText(EventsPage.this,list.get(0) ,Toast.LENGTH_LONG).show();
                                    Toast.makeText(EventsPage.this, "Name: " + event1.getName()+"bill"+event1.getBill()+ "\n" , Toast.LENGTH_SHORT).show();
                                    //+ "cb: " + event1.getBill()+ "pb: "+event1.paidBy.get("Yutika")
                                    //+ "pb: "+event1.paidBy.get("Yutika").toString()

                                }catch (Exception e){
                                    Toast.makeText(EventsPage.this, "NAy banla object", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(EventsPage.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EventsPage.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        /*Toast toast=Toast.makeText(EventsPage.this, "Document does not exist", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP,0,250);
        toast.show();*/
      //  eventId=list.get(0);
       // String petr="Petrol";

      /*  ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc= task.getResult();
                    if(doc.exists()) {
                        String trip_name =  task.getResult().getString("Name");


                    }

                }
            }
        });*/

        toEventDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EventsPage.this, EventDetails.class));
            }
        });
    }
}