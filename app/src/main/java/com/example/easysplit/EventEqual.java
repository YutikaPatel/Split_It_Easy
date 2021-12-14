package com.example.easysplit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventEqual extends AppCompatActivity {

    Event event1;

    ArrayList<String> membersList;
    String tripId;
    LinearLayout parentLL;
    ArrayList<LinearLayout> parentLLArrList = new ArrayList<LinearLayout>();
    ArrayList<Integer> memIds= new ArrayList<Integer>();
    private EditText currentEt;


    TripMemRetrival tr;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    private Button done;

    TripMemRetrival members;
    public static final String EXTRA_DATA="EXTRA_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_unequal);

        done=findViewById(R.id.done);
        Intent prev_intent = getIntent();
        tripId=prev_intent.getStringExtra("tripId");

        parentLL = findViewById(R.id.parentLL);
        event1=(Event)getIntent().getSerializableExtra(EXTRA_DATA);
        tr=new TripMemRetrival();
        membersList= event1.membersMails;


        for (int i=0; i<membersList.size(); i++){
            parentLLArrList.add( new LinearLayout(this));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,25,0,0);
            parentLLArrList.get(i).setLayoutParams(params);
            parentLLArrList.get(i).setId(i);

            parentLLArrList.get(i).setBackgroundColor(Color.rgb(0,220,220));
            parentLLArrList.get(i).getBackground().setAlpha(50);
            parentLLArrList.get(i).setOrientation(LinearLayout.HORIZONTAL);
            parentLLArrList.get(i).setWeightSum(100);
            CheckBox cb= new CheckBox(this);
            cb.setId(i+500);
            cb.setText(event1.mailIdsNames.get((membersList.get(i))));

            LinearLayout.LayoutParams params1= new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,100.0f);
            params1.setMargins(0,0,0,0);
            cb.setLayoutParams(params1);
            cb.setHorizontallyScrolling(true);
            EditText et=new EditText(this);

            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = ((CheckBox) v).isChecked();
                    // Check which checkbox was clicked
                    if (checked){
                        memIds.add(Integer.parseInt(String.valueOf(v.getId())));
                    }
                    if(!checked){
                        int memId=Integer.parseInt(String.valueOf(v.getId()));
                        memIds.remove(new Integer(memId));
                    }
                }
            });
            parentLLArrList.get(i).addView(cb);
            parentLL.addView(parentLLArrList.get(i));

        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // String txt_bill=consumed.getText().toString();

                HashMap<String,String> participants=new HashMap<String,String>();

                double sum=0;
                for(int j=0;j<memIds.size();j++){
                    String txt_amount=String.valueOf(Double.parseDouble(event1.billAmount)/memIds.size());
                    String mailId= membersList.get(memIds.get(j)-500);
                    participants.put( event1.mailIdsNames.get(mailId),txt_amount);
                    tr.updatetoPay(tripId,mailId,txt_amount);
                }

                    event1.setParticipants(participants);

                    // Iterating HashMap through for loop
                    for (Map.Entry<String, String> set :event1.paidmailIds.entrySet()) {
                        tr.updatePaid(tripId,set.getKey(),set.getValue());
                    }


                    db.collection("Trips").document(tripId).collection("eventlist").document(event1.getName()).set(event1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(EventEqual.this, "Event successfully added",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(EventEqual.this, "Couldn't add event sorry",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Intent intent = new Intent(EventEqual.this, EventsPage.class);
                    intent.putExtra("tripId",tripId);
                    startActivity(intent);


            }
        });
    }
}