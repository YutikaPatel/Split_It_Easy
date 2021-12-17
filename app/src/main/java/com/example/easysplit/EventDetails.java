package com.example.easysplit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class EventDetails extends AppCompatActivity  {

    private Button toEventEqual;
    private Button toEventUnequal;
    private TextView billAmount;
    Event event1;
    private EditText eventName;

    String tripId;
    LinearLayout parentLL;
    ArrayList<LinearLayout> parentLLArrList = new ArrayList<LinearLayout>();
    ArrayList<Integer> etIds= new ArrayList<Integer>();
    ArrayList<String> membersList= new ArrayList<String>();
    List<String> membersListname= new ArrayList<String>();
    private EditText currentEt;

    TripMemRetrival tr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        FirebaseFirestore db= FirebaseFirestore.getInstance();
        Intent prev_intent = getIntent();
        tripId=prev_intent.getStringExtra("tripId");

        eventName= findViewById(R.id.eventName);
        parentLL = findViewById(R.id.parentLL);
        billAmount=findViewById(R.id.billAmount);
        toEventEqual=findViewById(R.id.toEventEqual);
        toEventUnequal=findViewById(R.id.toEventUnequal);
        HashMap<String,String> here_mailIdsNames = new HashMap<String,String>();

        event1=new Event();
        tr=new TripMemRetrival();

        DocumentReference ref = FirebaseFirestore.getInstance().collection("Trips").document(tripId);


        db.collection("Trips").document(tripId).collection("memberlist").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        membersList.add(document.getId());

                    }
                    for(int i=0;i<membersList.size();i++){
                        int j=i;
                        DocumentReference ref2 = FirebaseFirestore.getInstance().collection("UserData").document(membersList.get(i));
                        ref2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    try {
                                        here_mailIdsNames.put(membersList.get(j),documentSnapshot.getString("Name") );
                                    }catch (Exception e){
                                    }

                                }
                            }
                        });
                    }


                }
            }

        });


        CountDownTimer cTimer = null;

            cTimer = new CountDownTimer(4000, 80) {
                public void onTick(long millisUntilFinished) {

                }
                public void onFinish() {

                    event1.mailIdsNames=here_mailIdsNames;
                    event1.membersMails=membersList;
                    String size=String.valueOf(membersList.size());


                    TextWatcher textWatcher = new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            try {
                                double result = 0;
                                for (int h = 0; h < etIds.size(); h++) {

                                    currentEt = findViewById(etIds.get(h));
                                    String txt_amount = currentEt.getText().toString();
                                    if(txt_amount.equals("") || txt_amount==null){
                                        continue;
                                    }
                                    result += Double.parseDouble(txt_amount);
                                }
                                billAmount.setText(String.valueOf(result));
                                billAmount.setTextSize(20);                                    /////////////////////
                            }catch(Exception e){

                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    };

                    for (int i=0; i<membersList.size(); i++){
                       // Toast.makeText(EventDetails.this,"aahe aahe", Toast.LENGTH_LONG).show();
                        parentLLArrList.add( new LinearLayout(EventDetails.this));
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0,25,0,0);
                        parentLLArrList.get(i).setLayoutParams(params);
                        parentLLArrList.get(i).setId(i);
                        parentLLArrList.get(i).setBackgroundColor(Color.rgb(0,220,220));
                        parentLLArrList.get(i).getBackground().setAlpha(50);
                        parentLLArrList.get(i).setOrientation(LinearLayout.HORIZONTAL);
                        parentLLArrList.get(i).setWeightSum(100);
                        CheckBox cb= new CheckBox(EventDetails.this);
                        cb.setId(i+500);
                        cb.setText(here_mailIdsNames.get(membersList.get(i)));
                        cb.setPadding(0,0,0,7);
                        cb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                        LinearLayout.LayoutParams params1= new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,66.0f);
                        params1.setMargins(0,0,0,0);
                        cb.setLayoutParams(params1);
                        cb.setHorizontallyScrolling(true);
                        EditText et=new EditText(EventDetails.this);
                        cb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boolean checked = ((CheckBox) v).isChecked();
                                if (checked){

                                    et.setId(Integer.parseInt(String.valueOf(v.getId()))+500);
                                    et.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                    LinearLayout.LayoutParams params2= new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,34.0f);
                                    params2.setMarginEnd(0);
                                    et.setLayoutParams(params2);
                                    et.setHint("amount");
                                    et.setGravity(Gravity.CENTER);
                                    et.setHorizontallyScrolling(true);
                                    et.addTextChangedListener(textWatcher);
                                    et.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                                    parentLLArrList.get(Integer.parseInt(String.valueOf(v.getId()))-500).addView(et);
                                    etIds.add(Integer.parseInt(String.valueOf(v.getId()))+500);
                                }
                                if(!checked){
                                    int etId=Integer.parseInt(String.valueOf(v.getId()))+500;
                                    EditText remove = findViewById(etId);
                                    String remove_txt= remove.getText().toString();
                                    remove.setText("0");

                                    parentLLArrList.get(Integer.parseInt(String.valueOf(v.getId()))-500).removeView(et);

                                    etIds.remove(new Integer(etId));


                                }
                            }
                        });
                        parentLLArrList.get(i).addView(cb);
                        parentLL.addView(parentLLArrList.get(i));


                    }


                }
            };
            cTimer.start();


        toEventEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String txt_eventName= eventName.getText().toString();
                String txt_bill=billAmount.getText().toString();

                HashMap<String,String> paidBy=new HashMap<String,String>();
                HashMap<String,String> paidmailIds= new HashMap<String,String>();
                for(int i=0;i<etIds.size();i++){
                    String mailId= membersList.get(etIds.get(i)-1000);
                    currentEt=findViewById(etIds.get(i));
                    String txt_amount= currentEt.getText().toString();
                    if(txt_amount.equals("") || txt_amount==null){
                        txt_amount="0";
                    }
                    paidBy.put( here_mailIdsNames.get(mailId),txt_amount);
                    paidmailIds.put(mailId,txt_amount);

                }


                event1.setName(txt_eventName);
                event1.setBillAmount(txt_bill);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                event1.setCreatedBy(here_mailIdsNames.get(user.getEmail()));
                event1.setPaidBy(paidBy);
                event1.setDistributionType("Equal");
                event1.paidmailIds=paidmailIds;
                Intent i= new Intent(EventDetails.this, EventEqual.class);
                i.putExtra(EventEqual.EXTRA_DATA,event1);
                i.putExtra("tripId",tripId);
                startActivity(i);
            }
        });


        toEventUnequal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String txt_eventName= eventName.getText().toString();
                String txt_bill=billAmount.getText().toString();

                HashMap<String,String> paidBy=new HashMap<String,String>();
                HashMap<String,String> paidmailIds= new HashMap<String,String>();

                for(int i=0;i<etIds.size();i++){
                    String mailId= membersList.get(etIds.get(i)-1000);
                    currentEt=findViewById(etIds.get(i));
                    String txt_amount= currentEt.getText().toString();
                    paidBy.put( here_mailIdsNames.get(mailId),txt_amount);
                    paidmailIds.put(mailId,txt_amount);

                }

                event1.setName(txt_eventName);
                event1.setBillAmount(txt_bill);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                event1.setCreatedBy(here_mailIdsNames.get(user.getEmail()));
                event1.setPaidBy(paidBy);
                event1.paidmailIds=paidmailIds;
                event1.setDistributionType("Unequal");
                Intent i= new Intent(EventDetails.this, EventUnequal.class);
                i.putExtra(EventEqual.EXTRA_DATA,event1);
                i.putExtra("tripId",tripId);
                startActivity(i);
            }
        });



    }



}