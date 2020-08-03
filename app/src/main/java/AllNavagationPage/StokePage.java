package AllNavagationPage;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thomas.karakbloodbankupdate.R;

import StokeFream.DonorStoke;
import StokeFream.RequestFream;


public class StokePage extends Fragment {



    private BottomNavigationView bottomNavigationView;

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


    public StokePage() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.stoke_page, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        totoaldonar = view.findViewById(R.id.TotalDonorID);
        totoalreciver = view.findViewById(R.id.TotalReciverID);
        relativeLayout = view.findViewById(R.id.TopLayout);
        MdonarDatabase = FirebaseDatabase.getInstance().getReference().child("DonarPost");
        MdonarDatabase.keepSynced(true);
        MRequesterDatabase = FirebaseDatabase.getInstance().getReference().child("Request_post");
        MRequesterDatabase.keepSynced(true);

        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.from_button);
        //     relativeLayout = view.findViewById(R.id.TopLayout);
            relativeLayout.setAnimation(animation);


        backbutton = view.findViewById(R.id.BcakButtonID);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            }
        });



        MdonarDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                long donner =  dataSnapshot.getChildrenCount();
                totoaldonar.setText("Total Donors: "+donner);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        MRequesterDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                long counter = dataSnapshot.getChildrenCount();
                totoalreciver.setText("Total Request: "+counter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        bottomNavigationView = view.findViewById(R.id.BottomnavID);

        goto_donorpage(new DonorStoke());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.DonarID){
                    goto_donorpage(new DonorStoke());
                }

                if(item.getItemId() == R.id.RequestuserID){
                    goto_requestpage(new RequestFream());
                }

                return true;
            }
        });


        return view;
    }


    private void goto_donorpage(Fragment fragment){

        if(fragment != null){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.StokeFream, fragment);
            transaction.commit();
        }


    }

    private void goto_requestpage(Fragment fragment){

        if(fragment != null){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.StokeFream, fragment);
            transaction.commit();
        }


    }

}