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
    String tripId="temptrip";
    private TextView name;
    private TextView createdBy;
    private TextView billAmount;
    private TextView distributionType;

    //ArrayList<LinearLayout> parentLLArrList = new ArrayList<LinearLayout>();
    ArrayList<Integer> etIds= new ArrayList<Integer>();
    LinearLayout parentLL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_event_details);
        String eventName = getIntent().getExtras().getString("eventName");
        Toast.makeText(PastEventDetails.this,eventName, Toast.LENGTH_LONG).show();


        name=findViewById(R.id.name  );
        createdBy=findViewById(R.id.createdBy );
        billAmount=findViewById(R.id.billAmount  );
        distributionType=findViewById(R.id.distributionType );

        parentLL = findViewById(R.id.parentLL);

        LinearLayout linearLayout=findViewById(R.id.linearlayout);
        linearLayout.setBackgroundColor(Color.rgb(0,220,220));
        linearLayout.getBackground().setAlpha(50);


        DocumentReference ref2= FirebaseFirestore.getInstance().collection("Trips").document(tripId).collection("eventlist").document(eventName);

        ref2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    try{
                        Event event1 = documentSnapshot.toObject(Event.class);

                        name.setText("Name: "+event1.getName());
                        createdBy.setText("Created By: "+event1.getCreatedBy());
                        billAmount.setText("Total Bill Amount: "+ event1.getBillAmount());
                        distributionType.setText("Distribution Type: "+event1.getDistributionType());

                        String size=String.valueOf(event1.paidBy.size());
                        Toast.makeText(PastEventDetails.this,size, Toast.LENGTH_LONG).show();

                        int i=0;
                        for (Map.Entry<String, String> entry : event1.paidBy.entrySet()){
                            String key = entry.getKey();
                            Object value = entry.getValue();

                            TextView b= new TextView(PastEventDetails.this);
                            b.setId(i+500);
                            b.setText(key+ " : ₹"+ value);
                            b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                            //b.setBackgroundColor(Color.CYAN);
                            LinearLayout.LayoutParams params1= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                            params1.setMargins(150,5,30,0);
                            b.setLayoutParams(params1);
                            // b.setGravity(Gravity.CENTER);

                           // parentLLArrList.get(i).addView(b);
                            parentLL.addView(b);

                            i++;
                        }


                       // parentLLArrList=new ArrayList<LinearLayout>();

                        int id=100000;
                        TextView participants= new TextView(PastEventDetails.this);
                        participants.setId(id);
                        LinearLayout.LayoutParams params1= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                        params1.setMargins(40,50,0,45);
                        participants.setLayoutParams(params1);
                        participants.setGravity(Gravity.FILL);
                       // participants.setPadding(,17,0,17);
                        participants.setText("Participants ->");
                        participants.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                        participants.setTypeface(null, Typeface.BOLD);;
                        parentLL.addView(participants);
                      //  parentLL.addView(parentLLArrList.get(i));


                        int z=0;
                        for (Map.Entry<String, String> entry : event1.participants.entrySet()){
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            // Toast.makeText(EventDetails.this,"aahe aahe", Toast.LENGTH_LONG).show();


                            //parentLLArrList.get(i).setBackgroundColor(Color.YELLOW);

                            // parentLLArrList.get(i).setWeightSum(100);
                            TextView b= new TextView(PastEventDetails.this);
                            b.setId(z+500);
                            b.setText(key+ " : ₹"+ value);
                            b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                            //b.setBackgroundColor(Color.CYAN);
                            LinearLayout.LayoutParams params2= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                            params2.setMargins(150,5,30,0);
                            b.setLayoutParams(params2);
                            // b.setGravity(Gravity.CENTER);


                            parentLL.addView(b);

                            z++;
                        }


                    }catch (Exception e){
                        Toast.makeText(PastEventDetails.this, "NAy banla object", Toast.LENGTH_SHORT).show();
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