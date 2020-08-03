package HomePage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.thomas.karakbloodbankupdate.BuildConfig;
import com.thomas.karakbloodbankupdate.LandingPage;
import com.thomas.karakbloodbankupdate.MainActivity;
import com.thomas.karakbloodbankupdate.R;
import com.thomas.karakbloodbankupdate.WelcomePage;

import Adapter.PageAdapter;
import AllNavagationPage.OneTouchPage;
import AllNavagationPage.ProfilePage;
import AllNavagationPage.StokePage;
import FeedbackPage.FeedbackPage;
import PostPage.PostIteams;
import PostPage.RequestBloodPage;
import Profile.Setup_Profile;
import ProfileActivity.ProfilePagess;
import RegistationPages.RegisterPage;
import TabPages.DonorPage;
import TabPages.RequestPage;
import de.hdodenhof.circleimageview.CircleImageView;
import maes.tech.intentanim.CustomIntent;


public class MainHomePage extends Fragment  {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView menuicon;
    private FirebaseAuth Mauth;
    private DatabaseReference Muserdatabse;
    private String CurrentUser;
    private ImageView addbutton;
    private DatabaseReference MmoneyDatabase;

    private BottomNavigationView bottomNavigationView;
    private boolean donorpageisopen = true;
    private boolean requestpageisopen = true;
    private Context context;

    public MainHomePage() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.main_home_page, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        context = view.getContext();

        goto_donar_page(new DonorPage());

        bottomNavigationView = view.findViewById(R.id.BottomBavViewID);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.DonarID){

                    if(donorpageisopen){
                        goto_donar_page(new DonorPage());
                        donorpageisopen = false;
                        requestpageisopen = true;
                    }

                }

                if(item.getItemId() == R.id.RequestuserID){

                    if(requestpageisopen){
                        goto_requestpage(new RequestPage());
                        requestpageisopen = false;
                        donorpageisopen = true;
                    }

                }

                return true;
            }
        });

   //     BottomPageContiner

        MmoneyDatabase = FirebaseDatabase.getInstance().getReference().child("Money");
        addbutton = view.findViewById(R.id.AddButtonID);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goto_addpage(new PostIteams());
            }
        });

        Mauth = FirebaseAuth.getInstance();
        CurrentUser = Mauth.getCurrentUser().getUid();
        menuicon = view.findViewById(R.id.ac);
        drawerLayout = view.findViewById(R.id.MainDrawerID);
        navigationView = view.findViewById(R.id.MainNavID);




        /*tabLayout = view.findViewById(R.id.TabLayoutID);
        viewPager = view.findViewById(R.id.ViewpagerID);
        pagerAdapter = new PageAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);*/

        Muserdatabse = FirebaseDatabase.getInstance().getReference().child("Users");
        Muserdatabse.keepSynced(true);


        ///chaeck user exists or not

        FirebaseUser Muser  = Mauth.getCurrentUser();
        if(Muser == null){

        }
        else {
            Muserdatabse.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(!dataSnapshot.hasChild(CurrentUser)){

                        if(getActivity() != null){
                            goto_profile_page(new Setup_Profile());
                        }


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        ///chaeck user exists or not



        menuicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.ProfileID){
                    item.setChecked(true);
                    item.setCheckable(true);
                    drawerLayout.closeDrawer(Gravity.LEFT);

                    goto_profile(new ProfilePage());

                   /* Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);*/
                }
                if(item.getItemId() == R.id.LogOutID){
                    Mauth.signOut();
                  item.setCheckable(true);
                  item.setChecked(true);
                  drawerLayout.closeDrawer(Gravity.LEFT);

                  Mauth.signOut();

                  goto_welcomepage(new LandingPage());
                }

                if(item.getItemId() == R.id.ShareID){
                    item.setCheckable(true);
                    item.setChecked(true);
                    drawerLayout.closeDrawer(Gravity.LEFT);


                    int counter = 0;
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");

                    String shareMessage = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";

                    String sharebody = shareMessage;
                    String sharesubject = "“A bottle of blood saved my life, was it yours.” “Every blood donor is a life saver.”\n" +
                            "download the link below share with your all friends To Get REAL blessing " + "\n\n"+sharebody;
                    intent.putExtra(Intent.EXTRA_TEXT, sharesubject);
                    //  intent.putExtra(Intent.EXTRA_SUBJECT, sharebody);
                    startActivity(Intent.createChooser(intent, "share with"));
                }

                if(item.getItemId() == R.id.StokeID){
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    item.setCheckable(true);
                    item.setChecked(true);

                    goto_stokepage(new StokePage());
                   /* Intent intent = new Intent(getApplicationContext(), StokeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);*/
                }
                if (item.getItemId() == R.id.FeedbackID){
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    item.setCheckable(true);
                    item.setChecked(true);

                   /* Intent intent = new Intent(getApplicationContext(), FeedbackActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);*/

                   goto_feedback_page(new FeedbackPage());
                }

                if(item.getItemId() == R.id.SupportUsID){
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    item.setCheckable(true);
                    item.setChecked(true);

                    goto_onetouch(new OneTouchPage());

                   /* Intent intent = new Intent(getApplicationContext(), OneTouchActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);*/
                }


                return true;
            }
        });


        View mainview = navigationView.inflateHeaderView(R.layout.header_layout);
        final TextView username = mainview.findViewById(R.id.Username);
        final TextView userblood = mainview.findViewById(R.id.UserBlood);
        final CircleImageView image = mainview.findViewById(R.id.ImageViewID);
        final TextView moneytext = mainview.findViewById(R.id.CounterMoneyID);


        MmoneyDatabase.child(CurrentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Long val = dataSnapshot.getChildrenCount();
                    moneytext.setText("Donated Rs."+Long.toString(val));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Muserdatabse.child(CurrentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    if(dataSnapshot.hasChild("Name")){
                        String Nameget = dataSnapshot.child("Name").getValue().toString();
                        username.setText(Nameget);
                    }
                    if(dataSnapshot.hasChild("blood")){
                        String bloodget = dataSnapshot.child("blood").getValue().toString();
                        userblood.setText(bloodget);
                    }
                    if(dataSnapshot.hasChild("imageurl")){
                        String imageurlget = dataSnapshot.child("imageurl").getValue().toString();

                        Picasso.with(getActivity()).load(imageurlget).placeholder(R.drawable.defaultimage).into(image);

                        Picasso.with(getActivity()).load(imageurlget).networkPolicy(NetworkPolicy.OFFLINE)
                                .into(image, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {

                                    }
                                });
                    }
                }
                else {
              //      Toast.makeText(getContext(), "no user found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return view;
    }








    private void goto_profile_page(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slider_from_right    , R.anim.slide_outfrom_left);
        transaction.replace(R.id.MainFreamContiner, fragment).addToBackStack(null);
        transaction.commit();

     /* Intent intent = new Intent(getActivity(), ProfilePagess.class);
      startActivity(intent);*/
    }



    private void goto_addpage(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slider_from_right    , R.anim.slide_outfrom_left);
        transaction.replace(R.id.MainFreamContiner, fragment).addToBackStack(null);
        transaction.commit();
    }

    private void goto_profile(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slider_from_right    , R.anim.slide_outfrom_left);
        transaction.replace(R.id.MainFreamContiner, fragment).addToBackStack(null);
        transaction.commit();
    }

    private void goto_stokepage(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slider_from_right    , R.anim.slide_outfrom_left);
        transaction.replace(R.id.MainFreamContiner, fragment).addToBackStack(null);
        transaction.commit();
    }

    private void goto_onetouch(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slider_from_right    , R.anim.slide_outfrom_left);
        transaction.replace(R.id.MainFreamContiner, fragment).addToBackStack(null);
        transaction.commit();
    }

    private void goto_donar_page(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.BottomPageContiner, fragment);
        transaction.commit();
    }

    private void goto_requestpage(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.BottomPageContiner, fragment);
        transaction.commit();
    }

    private void goto_welcomepage(Fragment fragment){
        /*Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        CustomIntent.customType(getActivity(), "left-to-right");
        startActivity(intent);*/

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slider_from_right    , R.anim.slide_outfrom_left);
        transaction.replace(R.id.WelcomeContiner, fragment);
        transaction.commit();

    }

    private void goto_feedback_page(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slider_from_right    , R.anim.slide_outfrom_left);
        transaction.replace(R.id.WelcomeContiner, fragment).addToBackStack(null);
        transaction.commit();
    }

}