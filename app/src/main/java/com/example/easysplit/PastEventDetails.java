package com.example.easysplit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class PastEventDetails extends AppCompatActivity {

    public static final String EXTRA_DATA="EXTRA_DATA";
    String tripId;
    private TextView name;
    private TextView createdBy;
    private TextView billAmount;
    private TextView distributionType;
    private TextView paidBy;

    //ArrayList<LinearLayout> parentLLArrList = new ArrayList<LinearLayout>();
    ArrayList<Integer> etIds= new ArrayList<Integer>();
    LinearLayout parentLL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_event_details);
        String eventName = getIntent().getExtras().getString("eventName");
        Intent prev_intent = getIntent();
        tripId=prev_intent.getStringExtra("tripId");

        name=findViewById(R.id.name  );
        createdBy=findViewById(R.id.createdBy );
        billAmount=findViewById(R.id.billAmount  );
        distributionType=findViewById(R.id.distributionType );
        paidBy=findViewById(R.id.paidBy);
        paidBy.setBackgroundColor(Color.rgb(0,220,220));
        paidBy.getBackground().setAlpha(50);
        parentLL = findViewById(R.id.parentLL);


        DocumentReference ref2= FirebaseFirestore.getInstance().collection("Trips").document(tripId).collection("eventlist").document(eventName);

        ref2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    try{
                        Event event1 = documentSnapshot.toObject(Event.class);

                        name.setText("Name: "+event1.getName());
                        name.setBackgroundColor(Color.rgb(0,220,220));
                        name.getBackground().setAlpha(50);
                        createdBy.setText("Created By: "+event1.getCreatedBy());
                        createdBy.setBackgroundColor(Color.rgb(0,220,220));
                        createdBy.getBackground().setAlpha(50);
                        billAmount.setText("Total Bill Amount: "+ event1.getBillAmount());
                        billAmount.setBackgroundColor(Color.rgb(0,220,220));
                        billAmount.getBackground().setAlpha(50);
                        distributionType.setText("Distribution Type: "+event1.getDistributionType());
                        distributionType.setBackgroundColor(Color.rgb(0,220,220));
                        distributionType.getBackground().setAlpha(50);

                        String size=String.valueOf(event1.paidBy.size());

                        int i=0;
                        for (Map.Entry<String, String> entry : event1.paidBy.entrySet()){
                            String key = entry.getKey();
                            Object value = entry.getValue();

                            TextView b= new TextView(PastEventDetails.this);
                            b.setId(i+500);
                            b.setText(key+ " : ₹"+ value);
                            b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                            LinearLayout.LayoutParams params1= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                            params1.setMargins(150,5,30,0);
                            b.setLayoutParams(params1);
                            parentLL.addView(b);

                            i++;
                        }


                        int id=100000;
                        TextView participants= new TextView(PastEventDetails.this);
                        participants.setId(id);
                        LinearLayout.LayoutParams params1= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                        params1.setMargins(0,50,0,45);
                        participants.setLayoutParams(params1);
                        participants.setGravity(Gravity.FILL);
                        participants.setText("Participants ->");
                        participants.setTextColor(Color.BLACK);
                        participants.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                        participants.setTypeface(null, Typeface.BOLD);;
                        participants.setBackgroundColor(Color.rgb(0,220,220));
                        participants.getBackground().setAlpha(50);
                        participants.setPadding(40,31,31,30);
                        parentLL.addView(participants);

                        int z=0;
                        for (Map.Entry<String, String> entry : event1.participants.entrySet()){
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            TextView b= new TextView(PastEventDetails.this);
                            b.setId(z+500);
                            b.setText(key+ " : ₹"+ value);
                            b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);

                            LinearLayout.LayoutParams params2= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                            params2.setMargins(150,5,30,0);
                            b.setLayoutParams(params2);
                            parentLL.addView(b);

                            z++;
                        }


                    }catch (Exception e){

                    }

                } else {
                    Toast.makeText(PastEventDetails.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PastEventDetails.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}