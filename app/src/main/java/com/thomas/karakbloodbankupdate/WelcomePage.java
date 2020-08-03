package com.thomas.karakbloodbankupdate;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;


public class WelcomePage extends Fragment {



    public WelcomePage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.welcome_page, container, false);




        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    goto_landing_page(new LandingPage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });
        thread.start();

        return view;
    }

    void goto_landing_page(Fragment fragment){

        if(fragment != null){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slider_from_right    , R.anim.slide_outfrom_left);
            transaction.replace(R.id.WelcomeContiner, fragment);
            transaction.commit();
        }


    }
}