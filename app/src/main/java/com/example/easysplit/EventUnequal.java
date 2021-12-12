package com.example.easysplit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*public class EventUnequal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_unequal);
    }
}*/

public class EventUnequal extends AppCompatActivity  {



    Event event1;

    ArrayList<String> membersList;
    String tripId="temptrip";
    LinearLayout parentLL;
    ArrayList<LinearLayout> parentLLArrList = new ArrayList<LinearLayout>();
    ArrayList<Integer> etIds= new ArrayList<Integer>();
    private EditText currentEt;

    /*   private Button mem1 ;
       private Button mem2 ;
       private Button mem3 ;

       private EditText amount1 ;
       private EditText amount2 ;
       private EditText amount3 ;
   */
    TripMemRetrival tr;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    private Button done;

   /* private Button mem1 ;
    private Button mem2 ;
    private Button mem3 ;*/

    TripMemRetrival members;
    public static final String EXTRA_DATA="EXTRA_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_unequal);

        done=findViewById(R.id.done);


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
            parentLLArrList.get(i).setBackgroundColor(Color.YELLOW);
            parentLLArrList.get(i).setOrientation(LinearLayout.HORIZONTAL);
            parentLLArrList.get(i).setWeightSum(100);
            CheckBox cb= new CheckBox(this);
            cb.setId(i+500);
            cb.setText(event1.mailIdsNames.get((membersList.get(i))));
            cb.setBackgroundColor(Color.CYAN);
            LinearLayout.LayoutParams params1= new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,66.0f);
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
                        Toast.makeText(EventUnequal.this,"just AAth", Toast.LENGTH_SHORT).show();
                        et.setId(Integer.parseInt(String.valueOf(v.getId()))+500);
                        et.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        LinearLayout.LayoutParams params2= new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,34.0f);
                        params2.setMarginEnd(0);
                        et.setLayoutParams(params2);
                        et.setBackgroundColor(Color.RED);
                        et.setHint("amount");
                        et.setGravity(Gravity.CENTER);
                        et.setHorizontallyScrolling(true);
                       // et.addTextChangedListener(textWatcher);
                        parentLLArrList.get(Integer.parseInt(String.valueOf(v.getId()))-500).addView(et);
                        Toast.makeText(EventUnequal.this,String.valueOf(Integer.parseInt(String.valueOf(v.getId()))-500), Toast.LENGTH_SHORT).show();
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





        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // String txt_bill=consumed.getText().toString();

                HashMap<String,String> participants=new HashMap<String,String>();

                double sum=0;
                for(int j=0;j<etIds.size();j++){
                    String mailId= membersList.get(etIds.get(j)-1000);
                    currentEt=findViewById(etIds.get(j));
                    String txt_amount= currentEt.getText().toString();
                    if(txt_amount.equals("") || txt_amount==null){
                        txt_amount="0";
                    }
                    sum+=Double.parseDouble(txt_amount);
                }
                if(sum==Double.parseDouble(event1.billAmount)){

                    for(int i=0;i<etIds.size();i++){
                        String mailId= membersList.get(etIds.get(i)-1000);
                        currentEt=findViewById(etIds.get(i));
                        String txt_amount= currentEt.getText().toString();
                        if(txt_amount.equals("") || txt_amount==null){
                            txt_amount="0";
                        }
                        sum+=Double.parseDouble(txt_amount);
                        participants.put( event1.mailIdsNames.get(mailId),txt_amount);
                        tr.updatetoPay(tripId,mailId,txt_amount);
                    }
                    event1.setParticipants(participants);

                    // Iterating HashMap through for loop
                    for (Map.Entry<String, String> set :event1.paidBy.entrySet()) {
                        tr.updatePaid(tripId,set.getKey(),set.getValue());
                    }



                    db.collection("Trips").document(tripId).collection("eventlist").document(event1.getName()).set(event1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(EventUnequal.this, "Event successfully added",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(EventUnequal.this, "Couldn't add event sorry",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    startActivity(new Intent(EventUnequal.this, EventsPage.class));

                }else{
                    Toast.makeText(EventUnequal.this, "Total consumed amount does not match the total paid amount ₹"+event1.billAmount+"",Toast.LENGTH_SHORT).show();
                }




            }
        });
    }



}


