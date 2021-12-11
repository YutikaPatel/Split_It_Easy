package com.example.easysplit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class EventEqual extends AppCompatActivity {

    private Button done;

    private Button mem1 ;
    private Button mem2 ;
    private Button mem3 ;
    Event event1;
    FirebaseFirestore db;
    TripMemRetrival members;
    public static final String EXTRA_DATA="EXTRA_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_equal);

        done=findViewById(R.id.done);
        mem1 =findViewById(R.id.mem1 );
        mem2 =findViewById(R.id.mem2 );
        mem3 =findViewById(R.id.mem3 );

        event1=(Event)getIntent().getSerializableExtra(EXTRA_DATA);

        HashMap<String,String> participants = new HashMap<String,String>();
        db= FirebaseFirestore.getInstance();

        mem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                participants.put("Rutuja","0");
            }
        });

        mem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                participants.put("Yutika","0");
            }
        });

        mem3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        event1.setParticipants(participants);
                        event1.setDistributionType("Equal");

                        members= new TripMemRetrival();
                        members.updatetoPay("temptrip","rutujaudhane@gmail.com","1");
                        members.updatetoPay("temptrip","yutikapatel@gmail.com","2");

                        db.collection("Trips").document("temptrip").collection("eventlist").document(event1.getName()).set(event1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(EventEqual.this, "event added",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(EventEqual.this, "Couldn't add event sorry",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });
    }
}