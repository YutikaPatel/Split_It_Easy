package com.example.easysplit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class TripDetails extends AppCompatActivity {
    EditText tripNameView;
    Map<Object,Object> trip;
    TextView list;
    ArrayList<String> members;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        tripNameView = (EditText) findViewById(R.id.tripNameTextView);
        list = (TextView) findViewById(R.id.membersList);
        list.setText("Members Added -");
        trip = new HashMap<>();
        members = new ArrayList<String>();
        ScrollView scr = findViewById(R.id.scr);
        scr.setBackgroundColor(Color.rgb(0,220,220));
        scr.getBackground().setAlpha(50);
    }


    @Override
    public void onBackPressed(){
        finishAffinity();
    }
    public void addTrip(View view){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        members.add(userEmail);
        String tripName = tripNameView.getText().toString();
        trip.put("Name",tripName);
        trip.put("createdBy",userEmail);
        trip.put("tripEnded","false");
        //trip.put("Members",members);

        db.collection("Trips")
                .add(trip)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String newId = documentReference.getId();
                        Log.d("LOGS","Trip added!"+tripName+" "+documentReference.getId());
                        addTripForAllMembers(members,newId);
                        addMembersToTrip(newId);
                        Toast.makeText(TripDetails.this,"Trip added!",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(TripDetails.this,EventsPage.class);
                        intent.putExtra("tripId",newId);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TripDetails.this,"Error in adding trip!",Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void addTripForAllMembers( ArrayList<String> members,String newId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for (String key : members) {
            DocumentReference documentReference = db.collection("UserData").document(key);
            documentReference.update("Trips", FieldValue.arrayUnion(newId));
            Log.d("LOGS","Trip added for member "+key);
        }

    }
    public void addMembersToTrip(String newId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        for (String member : members) {
            Map<String,String> data = new HashMap<String,String>();
            DocumentReference ref= FirebaseFirestore.getInstance().collection("UserData").document(member);

            ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()){
                        DocumentSnapshot doc= task.getResult();
                        if(doc.exists()){
                            String name =  task.getResult().getString("Name");
                            data.put("Name",name);
                            data.put("paid","0");
                            data.put("toPay","0");

                            db.collection("Trips").document(newId).collection("memberlist").document(member).set(data);
                            Log.d("LOGS","Member Added fr!"+member);


                        }else{
                            Log.d("LOGS", "No data");
                            Toast.makeText(TripDetails.this,"No data" ,Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });

        }
    }
    public void addMember(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_members);
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        Button dialogButton = (Button) dialog.findViewById(R.id.okButton);
        EditText emailTextView= (EditText)dialog.findViewById(R.id.emailTextView);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String memberEmail = emailTextView.getText().toString();
                DocumentReference ref= FirebaseFirestore.getInstance().collection("UserData").document(memberEmail);
                ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()){
                            DocumentSnapshot doc= task.getResult();
                            if(doc.exists()){


                                members.add(memberEmail);
                                list.append("\n"+memberEmail);
                                Toast.makeText(TripDetails.this,"Member added!",Toast.LENGTH_LONG).show();
                                Log.d("LOGS","Member added!"+memberEmail);
                                dialog.dismiss();




                            }else{
                                Log.d("LOGS", "No data");
                                Toast.makeText(TripDetails.this,"Member does not exist. Please enter a registered email ID!" ,Toast.LENGTH_SHORT).show();
                                dialog.dismiss();


                            }
                        }

                    }
                });


            }
        });

        dialog.show();
    }

}