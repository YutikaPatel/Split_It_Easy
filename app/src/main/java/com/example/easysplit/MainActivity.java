package com.example.easysplit;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button logout;
    private Button eventspage;
    private Button addEvent;

   // private EditText bill;
   // private EditText name;

    String txt_name,txt_bill;
    int num_bill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logout= findViewById(R.id.logout);
        eventspage=findViewById(R.id.eventspage);
        //addEvent=findViewById(R.id.addEvent);

        //billAmount=findViewById(R.id.bill);
        //name= findViewById(R.id.name);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this,"Logged Out", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(MainActivity.this, StartActivity.class));
            }
        });
        eventspage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, EventsPage.class));
            }
        });

        //FirebaseFirestore db= FirebaseFirestore.getInstance();

/*  Add to database with input doc name

        Map<String,Object> user= new HashMap<>();
        user.put("username","yutikapatel@gmail.com");
        user.put("Name","Yutika");
        user.put("toPay",45);
        user.put("tobePayed",55);

        db.collection("UserData").document("User").set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "values added",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Couldn't add value",Toast.LENGTH_SHORT).show();
                }
            }
        });

*/



        /* add extra key value to existing document

        Map<String,Object> data= new HashMap<>();
        data.put("clearDept","false");
        db.collection("UserData").document("User").set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "extra values added",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Couldn't add the extra value",Toast.LENGTH_SHORT).show();
                }
            }
        });
   /*  Add to database with unique id generated

        Map<String,Object> user= new HashMap<>();
        user.put("username","radhikawadekar@gmail.com");
        user.put("Name","Radhika");
        user.put("toPay",45);
        user.put("tobePayed",55);
        db.collection("UserData").add(user).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "values added",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Couldn't add value",Toast.LENGTH_SHORT).show();
                }
            }
        });

         */

/*
Update existing doc
 DocumentReference ref= FirebaseFirestore.getInstance().collection("UserData").document("User");
        ref.update("clearDept", true);
 */

        /*
        Get data from database
    DocumentReference ref= FirebaseFirestore.getInstance().collection("UserData").document("User");
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc= task.getResult();
                    if(doc.exists()){
                        Log.d("Document", doc.getData().toString());     //ouput in console
                        String res= doc.getData().toString();
                        Toast.makeText(MainActivity.this,res ,Toast.LENGTH_SHORT).show();
                    }else{
                        Log.d("Document", "No data");
                        Toast.makeText(MainActivity.this,"No data" ,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
         */

      //  DocumentReference ref= FirebaseFirestore.getInstance().collection("Trips").document("temptrip").collection("eventlist").document("Petrol");

         /*  Event event=new Event("Food","james","radhika");
       //EventList el=new EventList();
       //el.eventList.add(event);

        db.collection("Trips").document("temptrip").collection("eventlist").document("Food").set(event).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "values added",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Couldn't add value",Toast.LENGTH_SHORT).show();
                }
            }
        });
*/


        //EventList el=new EventList();
        //el.eventList.add(event);
   /*     addEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                txt_name= name.getText().toString();
                txt_bill=bill.getText().toString();
                //num_bill = Integer.parseInt(bill.getText().toString());

                num_bill+=1;

                EvenTester event=new EvenTester(txt_name,txt_bill);

                db.collection("Trips").document("temptrip").collection("eventlist").document(txt_name).set(event).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "event added",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this, "Couldn't add event sorry",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        */




/*

        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                Event utravlela = documentSnapshot.toObject(Event.class);

                                String nm = utravlela.getName();
                                String cb = utravlela.getCreatedBy();
                                String pb = utravlela.getPaidBy();

                                Toast.makeText(MainActivity.this, "Name: " + utravlela.name+ "\n" + "cb: " + utravlela.createdBy+ "pb: "+pb, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(MainActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    });
*/
       //DocumentReference ref2= FirebaseFirestore.getInstance().collection("Trips").document("temptrip").collection("eventlist");

   /*    db.collection("Trips").document("temptrip").collection("eventlist").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                        Toast.makeText(MainActivity.this,document.getId() ,Toast.LENGTH_SHORT).show();
                    }
                    Log.d(TAG, list.toString());

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

*/

/*get selected value from database

    FirebaseFirestore.getInstance().collection("UserData").whereEqualTo("toPay",45).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot doc:task.getResult()){
                Log.d("Document",doc.getId()+" " +doc.getData());  //ouput in console

                }
            }
            }
        });


*/


    }
}