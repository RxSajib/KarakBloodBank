package StokeFream;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thomas.karakbloodbankupdate.R;


public class DonorStoke extends Fragment {


    private MaterialTextView aplsu, aminus, bplus, bminus;
    private MaterialTextView oplus, ominus, abplus, abminus;
    private DatabaseReference MuserDatabase;
    private Animation animation;

    private int ApositiveCounter,AnegtiveCounter,BpositiveCounter, BnegCounter, OpositiveCounter;
    private int OnegtiveCounter, ABpositiveCounter, ABnegtiveCounter;

    private DatabaseReference MdonarDatabase, MRequesterDatabase;

    private MaterialTextView totoaldonar, totoalreciver;

    private ImageView backbutton;
    private RelativeLayout relativeLayout;

    public DonorStoke() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.donor_stoke, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


           /*adView = findViewById(R.id.StokeAdID);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);*/

      /*  backbutton = view.findViewById(R.id.BcakButtonID);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               *//* Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*//*
                //   finish();
            }
        });*/

    /*    totoaldonar = view.findViewById(R.id.TotalDonorID);
        totoalreciver = view.findViewById(R.id.TotalReciverID);
*/
     //   animation = AnimationUtils.loadAnimation(getActivity(), R.anim.from_button);
        MuserDatabase = FirebaseDatabase.getInstance().getReference().child("DonarPost");

   //     relativeLayout = view.findViewById(R.id.TopLayout);
    //    relativeLayout.setAnimation(animation);

        aplsu = view.findViewById(R.id.AplusText);
        aminus = view.findViewById(R.id.Aminustext);
        bplus = view.findViewById(R.id.bplustext);
        bminus = view.findViewById(R.id.bminustext);
        oplus = view.findViewById(R.id.oplustext);
        ominus = view.findViewById(R.id.ominustext);
        abplus = view.findViewById(R.id.abplustext);
        abminus = view.findViewById(R.id.abminustext);



        readingAllBloodGroup();

        MdonarDatabase = FirebaseDatabase.getInstance().getReference().child("DonarPost");
        MdonarDatabase.keepSynced(true);
        MRequesterDatabase = FirebaseDatabase.getInstance().getReference().child("Request_post");
        MRequesterDatabase.keepSynced(true);

        MdonarDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                long donner =  dataSnapshot.getChildrenCount();
//                totoaldonar.setText("Total Donors: "+donner);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        MRequesterDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                long counter = dataSnapshot.getChildrenCount();
//                totoalreciver.setText("Total Request: "+counter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        return view;
    }


    private void readingAllBloodGroup(){

        MuserDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    String value = ds.getValue().toString();

                    if(value.equals("A+")){
                        ApositiveCounter++;
                        aplsu.setText("Total Bag "+ApositiveCounter);
                    }
                    if(value.equals("A-")){
                        AnegtiveCounter++;
                        aminus.setText("Total Bag "+AnegtiveCounter);
                    }
                    if(value.equals("B+")){
                        BpositiveCounter++;
                        bplus.setText("Total Bag "+BpositiveCounter);
                    }
                    if(value.equals("B-")){
                        BnegCounter++;
                        bminus.setText("Total Bag "+BnegCounter);
                    }
                    if(value.equals("O+")){
                        OpositiveCounter++;
                        oplus.setText("Total Bag "+OpositiveCounter);
                    }
                    if(value.equals("O-")){
                        OnegtiveCounter++;
                        ominus.setText("Total Bag "+OnegtiveCounter);
                    }
                    if(value.equals("AB+")){
                        ABpositiveCounter++;
                        abplus.setText("Total Bag "+ABpositiveCounter);
                    }
                    if(value.equals("AB-")){
                        ABnegtiveCounter++;
                        abminus.setText("Total Bag "+ABnegtiveCounter);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}