package PostPage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thomas.karakbloodbankupdate.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class DonateBloodPage extends Fragment {

    private ImageView back_icon;

    private ImageView updatebutton;
    private Toolbar toolbar;
    private Spinner agespinner;
    private String[] ageArray = {"18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
            "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44",
            "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60",
            "61", "62", "63", "64", "65", "66", "67", "68", "69", "70"};

    private EditText name, mobilenumber, location, post;
    private RelativeLayout femailbutton, malebutton;
    private ImageView maleicon, femailicon;
    private int countpost;

    private RelativeLayout aplusbutton, aminusbutton, bplusbutton, bminusbutton, oplusbutton, ominusbutton;
    private RelativeLayout abplusbutton, abminusbutton;

    private ImageView aplusicon, aminusicon, bplusicon, bminusicon, oplusicon, ominusicon;
    private ImageView abplusicon, abminusicon;

    private CircleImageView profileimage;
    private Uri profileimageuri;
    private String Downloaduri;
    private FirebaseAuth Mauth;
    private String CurrentUserID;
    private StorageReference Mprofilestores;
    private String AgeString="";
    private String Gendertext="";
    private String Bloodtext = "";
    private String CurrentTime, CurrentDate;
    private DatabaseReference DonarpostRef;
    private ProgressDialog Mprogress;

    private DatabaseReference Muserdatabase;
    private String CurrentLoginuserName;
    private Long counter ;
    private String random_time;
    private int convertnegative;
    private String addsearchname;


    private ProgressDialog Mloding;

    private AdView mAdview;

    public DonateBloodPage() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.donate_blood_page, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mAdview = view.findViewById(R.id.AdtwoID);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);

        back_icon = view.findViewById(R.id.backIconID);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            }
        });

        updatebutton  = view.findViewById(R.id.UpdateButtonID);
        Mloding = new ProgressDialog(getActivity());

        Mprogress = new ProgressDialog(getActivity());
        profileimage = view.findViewById(R.id.DonarPostprofileImageID);
        Mauth = FirebaseAuth.getInstance();
        CurrentUserID = Mauth.getCurrentUser().getUid();
        Mprofilestores = FirebaseStorage.getInstance().getReference();

        Muserdatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        Muserdatabase.keepSynced(true);
        Muserdatabase.child(CurrentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    CurrentLoginuserName = dataSnapshot.child("Name").getValue().toString();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        DonarpostRef = FirebaseDatabase.getInstance().getReference().child("DonarPost");
        DonarpostRef.keepSynced(true);

        /// blood group button
        aplusbutton = view.findViewById(R.id.DAbplusButtonID);
        aminusbutton = view.findViewById(R.id.DAminusButtonID);
        bplusbutton = view.findViewById(R.id.DBplusButtonID);
        bminusbutton = view.findViewById(R.id.DBminusButtonID);
        oplusbutton = view.findViewById(R.id.DOplusButtonID);
        ominusbutton = view.findViewById(R.id.DOminusButtonID);
        abplusbutton = view.findViewById(R.id.DAAbplusButtonID);
        abminusbutton = view.findViewById(R.id.DAbminusButtonID);
        /// blood group button

        ///blood icon image
        aplusicon = view.findViewById(R.id.DAplusIcon);
        aminusicon = view.findViewById(R.id.DAminusicon);
        bplusicon = view.findViewById(R.id.DBplusicon);
        bminusicon = view.findViewById(R.id.DBminusicon);
        oplusicon = view.findViewById(R.id.DOplusicon);
        ominusicon = view.findViewById(R.id.DOminusicon);
        abplusicon = view.findViewById(R.id.DAbplusicon);
        abminusicon = view.findViewById(R.id.DAbminusicon);
        ///blood icon image

        aplusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bloodtext = "A+";
                aplusicon.setBackgroundResource(R.drawable.cheackbox);
                aminusicon.setBackgroundResource(R.drawable.clircle_box);
                bplusicon.setBackgroundResource(R.drawable.clircle_box);
                bminusicon.setBackgroundResource(R.drawable.clircle_box);
                oplusicon.setBackgroundResource(R.drawable.clircle_box);
                ominusicon.setBackgroundResource(R.drawable.clircle_box);
                abplusicon.setBackgroundResource(R.drawable.clircle_box);
                abminusicon.setBackgroundResource(R.drawable.clircle_box);

                aplusbutton.setBackgroundResource(R.drawable.genderstoke);
                aminusbutton.setBackgroundResource(R.drawable.uncheack);
                bplusbutton.setBackgroundResource(R.drawable.uncheack);
                bminusbutton.setBackgroundResource(R.drawable.uncheack);
                oplusbutton.setBackgroundResource(R.drawable.uncheack);
                ominusbutton.setBackgroundResource(R.drawable.uncheack);
                abplusbutton.setBackgroundResource(R.drawable.uncheack);
                abminusbutton.setBackgroundResource(R.drawable.uncheack);
            }
        });

        aminusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bloodtext = "A-";
                aplusicon.setBackgroundResource(R.drawable.clircle_box);
                aminusicon.setBackgroundResource(R.drawable.cheackbox);
                bplusicon.setBackgroundResource(R.drawable.clircle_box);
                bminusicon.setBackgroundResource(R.drawable.clircle_box);
                oplusicon.setBackgroundResource(R.drawable.clircle_box);
                ominusicon.setBackgroundResource(R.drawable.clircle_box);
                abplusicon.setBackgroundResource(R.drawable.clircle_box);
                abminusicon.setBackgroundResource(R.drawable.clircle_box);

                aplusbutton.setBackgroundResource(R.drawable.uncheack);
                aminusbutton.setBackgroundResource(R.drawable.genderstoke);
                bplusbutton.setBackgroundResource(R.drawable.uncheack);
                bminusbutton.setBackgroundResource(R.drawable.uncheack);
                oplusbutton.setBackgroundResource(R.drawable.uncheack);
                ominusbutton.setBackgroundResource(R.drawable.uncheack);
                abplusbutton.setBackgroundResource(R.drawable.uncheack);
                abminusbutton.setBackgroundResource(R.drawable.uncheack);
            }
        });

        bplusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bloodtext = "B+";
                aplusicon.setBackgroundResource(R.drawable.clircle_box);
                aminusicon.setBackgroundResource(R.drawable.clircle_box);
                bplusicon.setBackgroundResource(R.drawable.cheackbox);
                bminusicon.setBackgroundResource(R.drawable.clircle_box);
                oplusicon.setBackgroundResource(R.drawable.clircle_box);
                ominusicon.setBackgroundResource(R.drawable.clircle_box);
                abplusicon.setBackgroundResource(R.drawable.clircle_box);
                abminusicon.setBackgroundResource(R.drawable.clircle_box);

                aplusbutton.setBackgroundResource(R.drawable.uncheack);
                aminusbutton.setBackgroundResource(R.drawable.uncheack);
                bplusbutton.setBackgroundResource(R.drawable.genderstoke);
                bminusbutton.setBackgroundResource(R.drawable.uncheack);
                oplusbutton.setBackgroundResource(R.drawable.uncheack);
                ominusbutton.setBackgroundResource(R.drawable.uncheack);
                abplusbutton.setBackgroundResource(R.drawable.uncheack);
                abminusbutton.setBackgroundResource(R.drawable.uncheack);
            }
        });

        bminusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bloodtext = "B-";
                aplusicon.setBackgroundResource(R.drawable.clircle_box);
                aminusicon.setBackgroundResource(R.drawable.clircle_box);
                bplusicon.setBackgroundResource(R.drawable.clircle_box);
                bminusicon.setBackgroundResource(R.drawable.cheackbox);
                oplusicon.setBackgroundResource(R.drawable.clircle_box);
                ominusicon.setBackgroundResource(R.drawable.clircle_box);
                abplusicon.setBackgroundResource(R.drawable.clircle_box);
                abminusicon.setBackgroundResource(R.drawable.clircle_box);


                aplusbutton.setBackgroundResource(R.drawable.uncheack);
                aminusbutton.setBackgroundResource(R.drawable.uncheack);
                bplusbutton.setBackgroundResource(R.drawable.uncheack);
                bminusbutton.setBackgroundResource(R.drawable.genderstoke);
                oplusbutton.setBackgroundResource(R.drawable.uncheack);
                ominusbutton.setBackgroundResource(R.drawable.uncheack);
                abplusbutton.setBackgroundResource(R.drawable.uncheack);
                abminusbutton.setBackgroundResource(R.drawable.uncheack);
            }
        });

        oplusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bloodtext = "O+";
                aplusicon.setBackgroundResource(R.drawable.clircle_box);
                aminusicon.setBackgroundResource(R.drawable.clircle_box);
                bplusicon.setBackgroundResource(R.drawable.clircle_box);
                bminusicon.setBackgroundResource(R.drawable.clircle_box);
                oplusicon.setBackgroundResource(R.drawable.cheackbox);
                ominusicon.setBackgroundResource(R.drawable.clircle_box);
                abplusicon.setBackgroundResource(R.drawable.clircle_box);
                abminusicon.setBackgroundResource(R.drawable.clircle_box);


                aplusbutton.setBackgroundResource(R.drawable.uncheack);
                aminusbutton.setBackgroundResource(R.drawable.uncheack);
                bplusbutton.setBackgroundResource(R.drawable.uncheack);
                bminusbutton.setBackgroundResource(R.drawable.uncheack);
                oplusbutton.setBackgroundResource(R.drawable.genderstoke);
                ominusbutton.setBackgroundResource(R.drawable.uncheack);
                abplusbutton.setBackgroundResource(R.drawable.uncheack);
                abminusbutton.setBackgroundResource(R.drawable.uncheack);
            }
        });

        ominusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bloodtext = "O-";
                aplusicon.setBackgroundResource(R.drawable.clircle_box);
                aminusicon.setBackgroundResource(R.drawable.clircle_box);
                bplusicon.setBackgroundResource(R.drawable.clircle_box);
                bminusicon.setBackgroundResource(R.drawable.clircle_box);
                oplusicon.setBackgroundResource(R.drawable.clircle_box);
                ominusicon.setBackgroundResource(R.drawable.cheackbox);
                abplusicon.setBackgroundResource(R.drawable.clircle_box);
                abminusicon.setBackgroundResource(R.drawable.clircle_box);


                aplusbutton.setBackgroundResource(R.drawable.uncheack);
                aminusbutton.setBackgroundResource(R.drawable.uncheack);
                bplusbutton.setBackgroundResource(R.drawable.uncheack);
                bminusbutton.setBackgroundResource(R.drawable.uncheack);
                oplusbutton.setBackgroundResource(R.drawable.uncheack);
                ominusbutton.setBackgroundResource(R.drawable.genderstoke);
                abplusbutton.setBackgroundResource(R.drawable.uncheack);
                abminusbutton.setBackgroundResource(R.drawable.uncheack);
            }
        });

        abplusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bloodtext = "AB+";
                aplusicon.setBackgroundResource(R.drawable.clircle_box);
                aminusicon.setBackgroundResource(R.drawable.clircle_box);
                bplusicon.setBackgroundResource(R.drawable.clircle_box);
                bminusicon.setBackgroundResource(R.drawable.clircle_box);
                oplusicon.setBackgroundResource(R.drawable.clircle_box);
                ominusicon.setBackgroundResource(R.drawable.clircle_box);
                abplusicon.setBackgroundResource(R.drawable.cheackbox);
                abminusicon.setBackgroundResource(R.drawable.clircle_box);

                aplusbutton.setBackgroundResource(R.drawable.uncheack);
                aminusbutton.setBackgroundResource(R.drawable.uncheack);
                bplusbutton.setBackgroundResource(R.drawable.uncheack);
                bminusbutton.setBackgroundResource(R.drawable.uncheack);
                oplusbutton.setBackgroundResource(R.drawable.uncheack);
                ominusbutton.setBackgroundResource(R.drawable.uncheack);
                abplusbutton.setBackgroundResource(R.drawable.genderstoke);
                abminusbutton.setBackgroundResource(R.drawable.uncheack);
            }
        });

        abminusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bloodtext = "AB-";
                aplusicon.setBackgroundResource(R.drawable.clircle_box);
                aminusicon.setBackgroundResource(R.drawable.clircle_box);
                bplusicon.setBackgroundResource(R.drawable.clircle_box);
                bminusicon.setBackgroundResource(R.drawable.clircle_box);
                oplusicon.setBackgroundResource(R.drawable.clircle_box);
                ominusicon.setBackgroundResource(R.drawable.clircle_box);
                abplusicon.setBackgroundResource(R.drawable.clircle_box);
                abminusicon.setBackgroundResource(R.drawable.cheackbox);


                aplusbutton.setBackgroundResource(R.drawable.uncheack);
                aminusbutton.setBackgroundResource(R.drawable.uncheack);
                bplusbutton.setBackgroundResource(R.drawable.uncheack);
                bminusbutton.setBackgroundResource(R.drawable.uncheack);
                oplusbutton.setBackgroundResource(R.drawable.uncheack);
                ominusbutton.setBackgroundResource(R.drawable.uncheack);
                abplusbutton.setBackgroundResource(R.drawable.uncheack);
                abminusbutton.setBackgroundResource(R.drawable.genderstoke);
            }
        });

        name = view.findViewById(R.id.DnameID);
        mobilenumber = view.findViewById(R.id.DMobileNumber);
        location = view.findViewById(R.id.DLocatonID);
        post = view.findViewById(R.id.DMessageID);


        femailbutton = view.findViewById(R.id.DFemailLayoutID);
        malebutton = view.findViewById(R.id.DMaleLayoutID);
        maleicon = view.findViewById(R.id.DMale);
        femailicon = view.findViewById(R.id.DFemaleIcon);

        femailbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gendertext = "Female";
                femailbutton.setBackgroundResource(R.drawable.genderstoke);
                malebutton.setBackgroundResource(R.drawable.uncheack);

                femailicon.setBackgroundResource(R.drawable.cheackbox);
                maleicon.setBackgroundResource(R.drawable.clircle_box);
            }
        });
        malebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gendertext = "Male";
                femailicon.setBackgroundResource(R.drawable.clircle_box);
                maleicon.setBackgroundResource(R.drawable.cheackbox);

                femailbutton.setBackgroundResource(R.drawable.uncheack);
                malebutton.setBackgroundResource(R.drawable.genderstoke);
            }
        });




        agespinner = view.findViewById(R.id.SpinnerID);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.age_spinner, R.id.SampleAgeID, ageArray);
        agespinner.setAdapter(adapter);


        ///age spinner
        agespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AgeString = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ///age spinner




        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 511);
            }
        });


        DonarpostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    countpost = (int) dataSnapshot.getChildrenCount();
                    convertnegative = (~(countpost - 1));
                }
                else {
                    convertnegative = 0;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        _set_des_orderval();





        updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updating_addpost();
            }
        });

        return view;
    }




    private void _set_des_orderval(){
        DonarpostRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    String key =   ds.getValue().toString();
                    Log.d("TAG", key);
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



// updating_addpost();
    /*
     * note of des order
     * 0-1-2
     * 2-1-0
     * 0 1
     * */




    private void updating_addpost(){




        final String nametext = name.getText().toString();
        //AgeString
        String mobilenumbertext = mobilenumber.getText().toString();
        String locationtext = location.getText().toString();
        final String posttext = post.getText().toString();

        if(nametext.isEmpty()){
            name.setError("Name require");
        }
        else if(mobilenumbertext.isEmpty()){
            mobilenumber.setError("Mobile Number require");
        }
        else if(locationtext.isEmpty()){
            location.setError("Location require");
        }
        else if(posttext.isEmpty()){
            post.setError("post require");
        }
        else if (Gendertext == "") {
            Toast.makeText(getActivity(), "please enter your gender", Toast.LENGTH_LONG).show();
        } else if (Bloodtext == "") {
            Toast.makeText(getActivity(), "please enter your blood group", Toast.LENGTH_LONG).show();
        }
      /*  else if(AgeString == ""){
            Toast.makeText(getApplicationContext(), "please enter your age", Toast.LENGTH_LONG).show();
        }*/
        else {






            Mprogress.setTitle("Please wait ...");
            Mprogress.setMessage("Donor Request is posting wait for a moment");
            Mprogress.setCanceledOnTouchOutside(false);
            Mprogress.show();

            Calendar calendartime = Calendar.getInstance();
            SimpleDateFormat simpleDateFormattime = new SimpleDateFormat("HH:mm:ss");
            CurrentTime = simpleDateFormattime.format(calendartime.getTime());

            Calendar calendardate = Calendar.getInstance();
            SimpleDateFormat simpleDateFormatdate = new SimpleDateFormat("yyyy-mm-dd");
            CurrentDate = simpleDateFormatdate.format(calendardate.getTime());


            random_time = CurrentTime+CurrentDate;



            Map donarmap = new HashMap();
            donarmap.put("donar_name", nametext);
            donarmap.put("donar_age", AgeString);
            donarmap.put("donar_gender", Gendertext);
            donarmap.put("donar_number", mobilenumbertext);
            donarmap.put("donar_bloodgroup", Bloodtext);
            donarmap.put("donar_location", locationtext);
            donarmap.put("donar_post", posttext);
            donarmap.put("UID", CurrentUserID);
            donarmap.put("donar_profile_imageURL", Downloaduri);
            donarmap.put("time", CurrentTime);
            donarmap.put("date", CurrentDate);
            donarmap.put("short", convertnegative);
            donarmap.put("login_name", CurrentLoginuserName);
            donarmap.put("search", Bloodtext.toLowerCase());
            donarmap.put("counter", counter);
            donarmap.put("Randoomuid", random_time);
            donarmap.put("display", "visiable");
            donarmap.put("search_username", CurrentLoginuserName.toLowerCase());

            DonarpostRef.child(random_time).updateChildren(donarmap)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Mprogress.dismiss();

                                name.setText(null);
                                mobilenumber.setText(null);
                                location.setText(null);
                                post.setText(null);
                              /*  Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();*/

                                Toast.makeText(getActivity(), "post upload success", Toast.LENGTH_LONG).show();
                                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                              /*  DonarpostRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            countpost = dataSnapshot.getChildrenCount()-1;
                                        }
                                        else {
                                            countpost = 0;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });*/

                            }
                            else {
                                Mprogress.dismiss();
                                Toast.makeText(getActivity(), task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
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



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 511 && resultCode == RESULT_OK) {


            Mloding.setTitle("Wait for a moment ...");
            Mloding.setMessage("Please wait uploading image");
            Mloding.setCanceledOnTouchOutside(false);
            Mloding.show();

            profileimageuri = data.getData();
            profileimage.setImageURI(profileimageuri);


            StorageReference filepath = Mprofilestores.child("donar_image").child(profileimageuri.getLastPathSegment());
            filepath.putFile(profileimageuri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                Downloaduri = task.getResult().getDownloadUrl().toString();
                                Mloding.dismiss();
                            }
                            else {
                                Mloding.dismiss();
                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Mloding.dismiss();
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

        }
    }


    /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                profileimage.setImageURI(uri);

                StorageReference filepath = Mprofilestores.child("donar_image").child(uri.getLastPathSegment());
                filepath.putFile(uri)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if(task.isSuccessful()){
                                    Downloaduri = task.getResult().getDownloadUrl().toString();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }*/



}