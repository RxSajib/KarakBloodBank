package RegistationPages;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.thomas.karakbloodbankupdate.R;

import HomePage.HomeContiner;

public class RegisterPage extends Fragment {

    private EditText email, password, retypepassword;
    private RelativeLayout signuobutton;
    private FirebaseAuth Mauth;
    private ProgressDialog Mprogress;

    private MaterialTextView gotologinpage;

    public RegisterPage() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.register_page, container, false);

        gotologinpage = view.findViewById(R.id.HaveAccountID);
        gotologinpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goto_loginpage(new LoginPages());
            }
        });

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Mprogress = new ProgressDialog(getActivity());
        Mauth = FirebaseAuth.getInstance();
        email = view.findViewById(R.id.REMailAddressID);
        password = view.findViewById(R.id.RRPasswordID);
        retypepassword = view.findViewById(R.id.RCPasswordID);

        signuobutton = view.findViewById(R.id.RegisterButtonID);

        signuobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailtext = email.getText().toString();
                String passwordtext = password.getText().toString();
                String rpasswordtext = retypepassword.getText().toString();

                if(emailtext.isEmpty()){
                    email.setError("Email require");
                }
                else if(passwordtext.isEmpty()){
                    password.setError("Password require");
                }
                else if(rpasswordtext.isEmpty()){
                    retypepassword.setError("Password require");
                }

                else if(!passwordtext.equals(rpasswordtext)){
                    email.setError(null);
                    password.setError("Password not match");
                    retypepassword.setError("Password not match");
                }

                else if(passwordtext.length() <= 5){
                    Toast.makeText(getActivity(), "Password need 8 char", Toast.LENGTH_SHORT).show();
                }
                else {

                    Mprogress.setTitle("Wait for a moment ...");
                    Mprogress.setMessage("Please wait we are creating your account");
                    Mprogress.setCanceledOnTouchOutside(false);
                    Mprogress.show();

                    Mauth.createUserWithEmailAndPassword(emailtext, passwordtext)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        goto_homepgecontiner(new HomeContiner());
                                        Mprogress.dismiss();
                                    }
                                    else {
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

        return view;
    }



    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser Muser = Mauth.getCurrentUser();
        if(Muser != null){
            goto_homepgecontiner(new HomeContiner());
        }
    }



    private void goto_homepgecontiner(Fragment fragment){

        if(fragment != null){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slider_from_right    , R.anim.slide_outfrom_left);
            transaction.replace(R.id.WelcomeContiner, fragment);
            transaction.commit();
        }


    }

    private void goto_loginpage(Fragment fragment){
        if(fragment != null){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slider_from_right    , R.anim.slide_outfrom_left);
            transaction.replace(R.id.WelcomeContiner, fragment ).addToBackStack(null);
            transaction.commit();
        }
    }
}