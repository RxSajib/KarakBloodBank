package PostPage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.card.MaterialCardView;
import com.thomas.karakbloodbankupdate.R;

import RegistationPages.RegisterPage;


public class PostIteams extends Fragment {

    private MaterialCardView donatebutton, requestbutton;
    private AdView adView;

    private ImageView bac_icon;
    private InterstitialAd interstitialAd;

    public PostIteams() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.post_iteams, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        interstitialAd = new InterstitialAd(getActivity());
        interstitialAd.setAdUnitId("ca-app-pub-8213826839161591/7247487014");
        interstitialAd.loadAd(new AdRequest.Builder().build());

        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }
        });


        adView = view.findViewById(R.id.AddID);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        bac_icon = view.findViewById(R.id.backIconID);
        bac_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        donatebutton = view.findViewById(R.id.CCone);
        requestbutton = view.findViewById(R.id.CCtwo);


        donatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(interstitialAd.isLoaded()){
                    interstitialAd.show();
                    interstitialAd.loadAd(new AdRequest.Builder().build());
                }
                else {

                }

                goto_donatepage(new DonateBloodPage());
            }
        });

        requestbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(interstitialAd.isLoaded()){
                    interstitialAd.show();
                    interstitialAd.loadAd(new AdRequest.Builder().build());
                }
                else {

                }



                goto_requestpage(new RequestBloodPage());
            }
        });

        return view;
    }

    private void goto_donatepage(Fragment fragment){

        if(fragment != null){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slider_from_right    , R.anim.slide_outfrom_left);
            transaction.replace(R.id.MainFreamContiner, fragment).addToBackStack(null);
            transaction.commit();
        }


    }

    private void goto_requestpage(Fragment fragment){

        if(fragment != null){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slider_from_right    , R.anim.slide_outfrom_left);
            transaction.replace(R.id.MainFreamContiner, fragment).addToBackStack(null);
            transaction.commit();
        }


    }
}