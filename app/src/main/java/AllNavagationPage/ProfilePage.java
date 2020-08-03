package AllNavagationPage;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.thomas.karakbloodbankupdate.R;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfilePage extends Fragment {


    private ProgressDialog Mprogress;
    private FirebaseAuth Mauth;
    private DatabaseReference Muserdatabase;
    private String CurrentUserID;
    private CircleImageView profileimage;
    private TextView username, emailaddress, bloodgroup;

    private EditText inputname, inputemail;
    private RelativeLayout updatebutton;

    private ImageView backbutton;
    private ImageView gotoback;

    public ProfilePage() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_page, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //   final Fragment fragment = getActivity();
        gotoback = view.findViewById(R.id.backIconID);
        gotoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);




            }
        });

        updatebutton = view.findViewById(R.id.UpdateProfile);
        inputname = view.findViewById(R.id.ProfileUserNameID);
        inputemail = view.findViewById(R.id.ProfileEmailID);

        backbutton = view.findViewById(R.id.ProfileBackButtonID);



        profileimage = view.findViewById(R.id.EditProfileImageID);
        username = view.findViewById(R.id.EditNameID);
        emailaddress = view.findViewById(R.id.EmailAdID);
        bloodgroup = view.findViewById(R.id.Blood);
        Mprogress = new ProgressDialog(getActivity());


        Mauth = FirebaseAuth.getInstance();
        CurrentUserID = Mauth.getCurrentUser().getUid();
        Muserdatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        Muserdatabase.keepSynced(true);


        readingyuseringfo();
        profilered();

        updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startingupdatedata();
            }
        });


        return view;
    }


    private void startingupdatedata(){
        String emailtext = inputemail.getText().toString();
        String usernametext = inputname.getText().toString();

        if(emailtext.isEmpty()){
            inputemail.setError("Email require");
        }
        else if(usernametext.isEmpty()){
            inputname.setError("Name require");
        }
        else {

            Mprogress.setTitle("Please wait");
            Mprogress.setMessage("We are updating your data");
            Mprogress.setCanceledOnTouchOutside(false);
            Mprogress.show();

            Map usermap = new HashMap();
            usermap.put("email", emailtext);
            usermap.put("Name", usernametext);

            Muserdatabase.child(CurrentUserID).updateChildren(usermap)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Mprogress.dismiss();
                                inputemail.setText(null);
                                inputname.setText(null);
                                Toast.makeText(getActivity(), "data is update", Toast.LENGTH_LONG).show();
                                readingyuseringfo();
                            }
                            else {
                                Mprogress.dismiss();
                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Mprogress.dismiss();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void readingyuseringfo(){
        Muserdatabase.child(CurrentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            if(dataSnapshot.hasChild("imageurl")){
                                String imageurlget = dataSnapshot.child("imageurl").getValue().toString();
                                Picasso.with(getActivity()).load(imageurlget).placeholder(R.drawable.defaultimage).into(profileimage);

                                Picasso.with(getActivity()).load(imageurlget).networkPolicy(NetworkPolicy.OFFLINE).into(profileimage, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {

                                    }
                                });
                            }
                            if(dataSnapshot.hasChild("Name")){
                                String Nameget = dataSnapshot.child("Name").getValue().toString();
                                username.setText(Nameget);
                            }
                            if(dataSnapshot.hasChild("blood")){
                                String bloodget = dataSnapshot.child("blood").getValue().toString();
                                bloodgroup.setText(bloodget);
                            }
                            if(dataSnapshot.hasChild("email")){
                                String emailget = dataSnapshot.child("email").getValue().toString();
                                emailaddress.setText(emailget);
                            }
                        }
                        else {
                            Toast.makeText(getActivity(), "No user found", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    private void profilered(){
        Muserdatabase.child(CurrentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            if(dataSnapshot.hasChild("imageurl")){
                                String imageurlget = dataSnapshot.child("imageurl").getValue().toString();
                                Picasso.with(getActivity()).load(imageurlget).placeholder(R.drawable.defaultimage).into(profileimage);

                                Picasso.with(getActivity()).load(imageurlget).networkPolicy(NetworkPolicy.OFFLINE).into(profileimage, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {

                                    }
                                });
                            }
                            if(dataSnapshot.hasChild("Name")){
                                String Nameget = dataSnapshot.child("Name").getValue().toString();
                                username.setText(Nameget);
                            }
                            if(dataSnapshot.hasChild("blood")){
                                String bloodget = dataSnapshot.child("blood").getValue().toString();
                                bloodgroup.setText(bloodget);
                            }
                            if(dataSnapshot.hasChild("email")){
                                String emailget = dataSnapshot.child("email").getValue().toString();
                                emailaddress.setText(emailget);
                            }
                        }
                        else {
                            Toast.makeText(getActivity(), "No user found", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void removeFragement(Fragment fragment){

        if(fragment != null){
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            //     transaction.setCustomAnimations(R.anim.slider_from_right    , R.anim.slide_outfrom_left);
            transaction.remove(fragment);
            transaction.commit();
        }


    }

}