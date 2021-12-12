package com.example.easysplit;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    ArrayList<LinearLayout> parentLLArrList = new ArrayList<LinearLayout>();
    ArrayList<Integer> etIds= new ArrayList<Integer>();

    LinearLayout parentLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_page);

        FirebaseFirestore db= FirebaseFirestore.getInstance();
        List<String> list=new ArrayList<String>();
        List<String> memlist=new ArrayList<String>();
        parentLL = findViewById(R.id.parentLL);

        toEventDetails=findViewById(R.id.toEventDetails);

        DocumentReference ref= FirebaseFirestore.getInstance().collection("Trips").document(tripId);

        db.collection("Trips").document(tripId).collection("eventlist").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                       // Toast.makeText(EventsPage.this,"in for" ,Toast.LENGTH_LONG).show();
                    }

                    String size=String.valueOf(list.size());
                    Toast.makeText(EventsPage.this,size, Toast.LENGTH_LONG).show();

                    for (int i=0; i<list.size(); i++){
                        // Toast.makeText(EventDetails.this,"aahe aahe", Toast.LENGTH_LONG).show();
                        parentLLArrList.add( new LinearLayout(EventsPage.this));
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0,25,0,0);
                        parentLLArrList.get(i).setLayoutParams(params);
                        parentLLArrList.get(i).setId(i);
                        parentLLArrList.get(i).setGravity(Gravity.CENTER);
                        //parentLLArrList.get(i).setBackgroundColor(Color.YELLOW);
                        parentLLArrList.get(i).setOrientation(LinearLayout.HORIZONTAL);
                       // parentLLArrList.get(i).setWeightSum(100);
                        Button b= new Button(EventsPage.this);
                        b.setId(i+500);
                        b.setText(list.get(i));
                        b.setBackgroundColor(Color.CYAN);
                        LinearLayout.LayoutParams params1= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                        params1.setMargins(30,0,30,0);
                        b.setLayoutParams(params1);
                       // b.setGravity(Gravity.CENTER);
                        int j=i;
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                    Intent intent= new Intent(EventsPage.this, PastEventDetails.class);
                                    //intent.putExtra(EventEqual.EXTRA_DATA,list.get(i));
                                    intent.putExtra("eventName", list.get(j));
                                    startActivity(intent);

                            }
                        });
                        parentLLArrList.get(i).addView(b);
                        parentLL.addView(parentLLArrList.get(i));

                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        toEventDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EventsPage.this, EventDetails.class));
            }
        });
    }
}