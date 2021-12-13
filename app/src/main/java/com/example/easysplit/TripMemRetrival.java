package com.example.easysplit;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TripMemRetrival extends AppCompatActivity {

    TripMemRetrival() {

    }



    HashMap<String, ArrayList<Double>> members = new HashMap<String, ArrayList<Double>>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> membersList= new ArrayList<String>();

    String memName= new String();


    public void updatetoPay(String trip_id, String mailId, String toPay) {

        DocumentReference ref = FirebaseFirestore.getInstance().collection("Trips").document(trip_id).collection("memberlist").document(mailId);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    try {

                        String past_toPay = documentSnapshot.getString("toPay");
                        //Toast.makeText(EventUnequal.class,"" ,Toast.LENGTH_LONG).show();

                        double update_toPay_num= Double.parseDouble(past_toPay)+Double.parseDouble(toPay);
                        String update_toPay= String.valueOf(update_toPay_num);
                        ref.update("toPay", update_toPay);
                    }catch (Exception e){

                    }
                }

            }
        });
    }

    public void updatePaid(String trip_id, String mailId, String paid) {


        DocumentReference ref = FirebaseFirestore.getInstance().collection("Trips").document(trip_id).collection("memberlist").document(mailId);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    try {

                        String past_paid = documentSnapshot.getString("paid");
                        double update_paid_num= Double.parseDouble(past_paid)+Double.parseDouble(paid);
                        String update_paid= String.valueOf(update_paid_num);
                        ref.update("paid", update_paid);

                    }catch (Exception e){

                    }
                }

            }
        });
    }



    /*public void memberlist(String tripId) {


        DocumentReference ref = FirebaseFirestore.getInstance().collection("Trips").document(tripId);

        //List<String> mailIds = new ArrayList<String>();


        final Task<QuerySnapshot> querySnapshotTask = db.collection("Trips").document(tripId).collection("memberlist").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        membersList.add(document.getId());

                    }

                }
            }

        });



    }*/


   /* interface FirestoreCallback{
        void onCallback(List<String> list);
    }
*/


   /* public String getNames(String mailId){
        DocumentReference ref = FirebaseFirestore.getInstance().collection("UserData").document(mailId);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    try {
                       memName= documentSnapshot.getString("Name");
                    }catch (Exception e){
                    }
                }
            }
        });
     return memName;
    }*/
}

