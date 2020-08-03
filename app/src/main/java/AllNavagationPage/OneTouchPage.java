package AllNavagationPage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.card.MaterialCardView;
import com.thomas.karakbloodbankupdate.R;


public class OneTouchPage extends Fragment {

    private AdView mAdview, adtwo;

    private MaterialCardView cardone, cardtwo, cardthree;
    private InterstitialAd interstitialAd;

    public OneTouchPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.one_touch_page, container, false);

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

        cardone = view.findViewById(R.id.AdmobOneID);
        cardtwo = view.findViewById(R.id.AdmobTwo);
        cardthree = view.findViewById(R.id.AdmobThree);


        cardone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(interstitialAd.isLoaded()){
                    interstitialAd.show();
                    interstitialAd.loadAd(new AdRequest.Builder().build());
                }
                else {

                }
            }
        });

        cardtwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(interstitialAd.isLoaded()){
                    interstitialAd.show();
                    interstitialAd.loadAd(new AdRequest.Builder().build());
                }
                else {

                }
            }
        });

        cardthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(interstitialAd.isLoaded()){
                    interstitialAd.show();
                    interstitialAd.loadAd(new AdRequest.Builder().build());
                }
                else {

                }
            }
        });

        adtwo = view.findViewById(R.id.AdtwoID);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        adtwo.loadAd(adRequest1);

        mAdview = view.findViewById(R.id.BannerAd);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);

        return view;
    }
}