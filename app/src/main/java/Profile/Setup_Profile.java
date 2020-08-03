package Profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.thomas.karakbloodbankupdate.R;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;
import java.util.PrimitiveIterator;

import HomePage.MainHomePage;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class Setup_Profile extends Fragment {

    private RelativeLayout male, femail;
    private ImageView maleicon, femailicon;
    private EditText fullname;
    private CircleImageView profileimage;

    private RelativeLayout setupbutton;
    private DatabaseReference MuserDatabase;
    private FirebaseAuth Mauth;
    private String CurrentUserID;
    private String DownloadImage;

    private ProgressDialog Mprogress;
    private Uri imageuri = null;
    private StorageReference Mprofileimagestor;
    private String gender;


    public Setup_Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.setup__profile, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);



        Mprogress = new ProgressDialog(getActivity());
        Mauth = FirebaseAuth.getInstance();
        CurrentUserID = Mauth.getCurrentUser().getUid();
        MuserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        MuserDatabase.keepSynced(true);

        fullname = view.findViewById(R.id.FullnameInputTextID);
        male = view.findViewById(R.id.MaleLayoutID);
        femail = view.findViewById(R.id.FemailLayoutID);
        maleicon = view.findViewById(R.id.Male);
        femailicon = view.findViewById(R.id.FemaleIcon);
        fullname = view.findViewById(R.id.FullnameInputTextID);

        setupbutton = view.findViewById(R.id.RegisterButtID);
        profileimage = view.findViewById(R.id.ProfileImageIDID);
        Mprofileimagestor = FirebaseStorage.getInstance().getReference().child("profileImage");


        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "Male";
                maleicon.setBackgroundResource(R.drawable.cheackbox);
                femailicon.setBackgroundResource(R.drawable.clircle_box);
                male.setBackgroundResource(R.drawable.genderstoke);
                femail.setBackgroundResource(R.drawable.uncheack);
            }
        });

        femail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "Female";
                femailicon.setBackgroundResource(R.drawable.cheackbox);
                maleicon.setBackgroundResource(R.drawable.clircle_box);
                femail.setBackgroundResource(R.drawable.genderstoke);
                male.setBackgroundResource(R.drawable.uncheack);
            }
        });


        setupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = fullname.getText().toString();
                if (name.isEmpty()) {
                    fullname.setError("Name require");
                } else if (gender.isEmpty()) {
                    Toast.makeText(getActivity(), "Gender is empty", Toast.LENGTH_SHORT).show();
                } else {
                    Mprogress.setTitle("Wait for a second ...");
                    Mprogress.setMessage("please wait we are setup your profile");
                    Mprogress.setCanceledOnTouchOutside(false);
                    Mprogress.show();
                    Map<String, Object> usermap = new HashMap<String, Object>();
                    usermap.put("Name", name);
                    usermap.put("sex", gender);
                    usermap.put("imageurl", DownloadImage);


                    MuserDatabase.child(CurrentUserID)
                            .updateChildren(usermap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Mprogress.dismiss();

                                        goto_homecontiner(new MainHomePage());
                                    } else {
                                        Mprogress.dismiss();
                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Mprogress.dismiss();
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        });

        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 511);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 511 && resultCode == RESULT_OK) {

            Mprogress.setTitle("Wait for a second ...");
            Mprogress.setMessage("please wait we are setup your profile");
            Mprogress.setCanceledOnTouchOutside(false);
            Mprogress.show();

            Uri imageuri = data.getData();
            profileimage.setImageURI(imageuri);

            StorageReference filepath = Mprofileimagestor.child(imageuri.getLastPathSegment());
            filepath.putFile(imageuri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                Mprogress.dismiss();
                                DownloadImage = task.getResult().getDownloadUrl().toString();


                            } else {
                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void goto_homecontiner(Fragment fragment){

        if(fragment != null){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slider_from_right    , R.anim.slide_outfrom_left);
            transaction.replace(R.id.MainFreamContiner, fragment);
            transaction.commit();
        }

    }
}