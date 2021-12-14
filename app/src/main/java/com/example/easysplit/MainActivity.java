package com.example.easysplit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;

import java.util.HashMap;
import java.util.Map;
import java.util.*;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText tv;
    private Button logout;
    HashMap<Integer, String> buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logout = findViewById(R.id.logout);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        buttons = new HashMap<Integer, String>();
        DocumentReference ref = FirebaseFirestore.getInstance().collection("UserData").document(userEmail);
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {


                        ArrayList<String> arr = (ArrayList<String>) doc.get("Trips");

                       if(arr!=null) {
                           Iterator iter = arr.iterator();

                           int currTop = 200;
                           while (iter.hasNext()) {
                               String currTrip = (String) iter.next();

                               Button newBt = new Button(MainActivity.this);
                               DocumentReference ref = FirebaseFirestore.getInstance().collection("Trips").document(currTrip);

                               ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                   @Override
                                   public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                       if (task.isSuccessful()) {
                                           DocumentSnapshot doc = task.getResult();
                                           if (doc.exists()) {
                                               String name = task.getResult().getString("Name");
                                               newBt.setId(newBt.generateViewId());
                                               buttons.put(newBt.getId(), currTrip);
                                               newBt.setOnClickListener(MainActivity.this);
                                               newBt.setText(name);

                                           } else {

                                               Toast.makeText(MainActivity.this, "Sorry Error Occured", Toast.LENGTH_SHORT).show();
                                           }
                                       }

                                   }
                               });


                               LinearLayout rl = (LinearLayout) findViewById(R.id.rel);
                               LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                               params.setMargins(20, 25, 20, 0);
                               rl.addView(newBt, params);
                               newBt.setBackgroundColor(Color.rgb(0, 220, 220));
                               newBt.getBackground().setAlpha(50);

                           }
                       }

                    } else {
                        Log.d("Document", "No data");
                        Toast.makeText(MainActivity.this, "No data", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, StartActivity.class));
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();


    }

    public void createTrip(View view) {
        Intent intent = new Intent(MainActivity.this, TripDetails.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int currId = v.getId();
        String clickedTrip = buttons.get(currId);
        Log.d("LOGS", clickedTrip);
        Intent intent = new Intent(MainActivity.this,EventsPage.class);
        intent.putExtra("tripId",clickedTrip);
        startActivity(intent);

    }
};




