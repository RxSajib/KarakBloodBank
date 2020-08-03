package com.thomas.karakbloodbankupdate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import HomePage.HomeContiner;
import RegistationPages.LoginPages;
import RegistationPages.RegisterPage;


public class LandingPage extends Fragment {

    private RelativeLayout loginbutton, registerbutton;
    private FirebaseAuth Mauth;

    public LandingPage() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.landing_page, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Mauth = FirebaseAuth.getInstance();
        loginbutton = view.findViewById(R.id.LoginButtonIDID);
        registerbutton = view.findViewById(R.id.SigUpButtonID);


        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goto_loginpage(new LoginPages());
            }
        });


        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goto_registerpage(new RegisterPage());
            }
        });


        return view;
    }

    private void goto_loginpage(Fragment fragment){

        if(fragment != null){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slider_from_right    , R.anim.slide_outfrom_left);
            transaction.replace(R.id.WelcomeContiner, fragment).addToBackStack(null);
            transaction.commit();
        }


    }

    private void goto_registerpage(Fragment fragment){

        if(fragment != null){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slider_from_right    , R.anim.slide_outfrom_left);
            transaction.replace(R.id.WelcomeContiner, fragment).addToBackStack(null);
            transaction.commit();
        }


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


    @Override
    public void onStop() {
        super.onStop();

        FirebaseUser Muser = Mauth.getCurrentUser();
        if(Muser != null){

            gotohomecontiner(new HomeContiner());

        }
    }

    private void gotohomecontiner(Fragment fragment){

        if(fragment != null){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slider_from_right    , R.anim.slide_outfrom_left);
            transaction.replace(R.id.MainFreamContiner, fragment);
            transaction.commit();
        }


    }
}