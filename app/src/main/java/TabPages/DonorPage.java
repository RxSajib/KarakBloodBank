package TabPages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.thomas.karakbloodbankupdate.R;

import AllPogoClass.donar_holder;
import ChatPage.ChatPage;
import de.hdodenhof.circleimageview.CircleImageView;


public class DonorPage extends Fragment {


    private RecyclerView donarlist;
    private DatabaseReference MdonarDatabase;
    private ImageView nodonarimage;
    private TextView nodonartext;
    private FirebaseAuth Mauth;
    private String CurrentUserID;
    private SearchView searchView;
    private Boolean likeboolen = false;
    private DatabaseReference MLikeDatabase;
    int value = 0;
    private Spinner searchspinner;
    private String[] filter = {"Filter", "Username", "Blood"};
    private RelativeLayout infolayout;


    public DonorPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.donor_page, container, false);


        infolayout = view.findViewById(R.id.DonorLayoutID);

        searchspinner = view.findViewById(R.id.SearchSpinnersID);
        final ArrayAdapter<String> searchadapter = new ArrayAdapter<String>(getContext(), R.layout.blood_search_, R.id.SearchIteamsID, filter);
        searchspinner.setAdapter(searchadapter);

        MdonarDatabase = FirebaseDatabase.getInstance().getReference().child("DonarPost");
        MdonarDatabase.keepSynced(true);
        MLikeDatabase = FirebaseDatabase.getInstance().getReference().child("DonarLike");
        MLikeDatabase.keepSynced(true);
        searchView = view.findViewById(R.id.DSearchID);
        Mauth = FirebaseAuth.getInstance();
        CurrentUserID = Mauth.getCurrentUser().getUid();


        donarlist = view.findViewById(R.id.DonarViewID);
        donarlist.setHasFixedSize(true);
        donarlist.setLayoutManager(new LinearLayoutManager(getActivity()));


        searchspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String searchposition = parent.getItemAtPosition(position).toString();

                if (searchposition.equals("Filter")) {
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            Toast.makeText(getContext(), "Please select your filter menu to search items", Toast.LENGTH_LONG).show();

                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            Toast.makeText(getContext(), "Please select your filter menu to search items", Toast.LENGTH_LONG).show();

                            return false;
                        }
                    });
                }

                if (searchposition.equals("Blood")) {
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            startSerchingQuryy(query);
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            startSerchingQuryy(newText);
                            return false;
                        }
                    });
                }

                if (searchposition.equals("Username")) {
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            searching_username(query);
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            searching_username(newText);
                            return false;
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        return view;
    }




    /// search by username
    private void searching_username(String sec) {
        String query = sec.toLowerCase();
        final Query firebaseQry = MdonarDatabase.orderByChild("search_username").startAt(query).endAt(query + "\uf8ff");


        FirebaseRecyclerAdapter<donar_holder, DonarHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<donar_holder, DonarHolder>(
                donar_holder.class,
                R.layout.donor_banner_update,
                DonarHolder.class,
                firebaseQry
        ) {
            @Override
            protected void populateViewHolder(final DonarHolder donarHolder, final donar_holder donar_holder, int i) {

                final String UID = getRef(i).getKey();
                MdonarDatabase.child(UID)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    infolayout.setVisibility(View.INVISIBLE);


                                    if (dataSnapshot.hasChild("display")) {
                                        String displayget = dataSnapshot.child("display").getValue().toString();

                                        if (displayget.equals("visiable")) {
                                            donarHolder.toplayout.setVisibility(View.VISIBLE);


                                            if (dataSnapshot.hasChild("DonarPost")) {
                                                String donarname = dataSnapshot.child("DonarPost").getValue().toString();
                                                donarHolder.setUsernameset(donarname);

                                            }
                                            if (dataSnapshot.hasChild("donar_number")) {
                                                String number = dataSnapshot.child("donar_number").getValue().toString();
                                                donarHolder.setmobilset(number);
                                            }
                                            if (dataSnapshot.hasChild("donar_location")) {
                                                String dlocation = dataSnapshot.child("donar_location").getValue().toString();
                                                donarHolder.setLocationset(dlocation);
                                            }
                                            if (dataSnapshot.hasChild("donar_bloodgroup")) {
                                                String bloodgroupset = dataSnapshot.child("donar_bloodgroup").getValue().toString();
                                                donarHolder.setBloodset(bloodgroupset);
                                            }
                                            if (dataSnapshot.hasChild("donar_post")) {
                                                String messgetxrt = dataSnapshot.child("donar_post").getValue().toString();
                                                donarHolder.setmessage(messgetxrt);
                                            }
                                            if (dataSnapshot.hasChild("date")) {
                                                String date = dataSnapshot.child("date").getValue().toString();
                                                donarHolder.donar_date(date);
                                            }

                                            if (dataSnapshot.hasChild("login_name")) {
                                                String name = dataSnapshot.child("login_name").getValue().toString();
                                                donarHolder.setProfileimagenamebellowset(name);

                                            }

                                            if (dataSnapshot.hasChild("donar_name")) {
                                                String name = dataSnapshot.child("donar_name").getValue().toString();

                                                donarHolder.setUsernameset(name);
                                            }


                                            if (dataSnapshot.hasChild("donar_profile_imageURL")) {
                                                String imageuri = dataSnapshot.child("donar_profile_imageURL").getValue().toString();
                                                donarHolder.setProfileimageset(imageuri);
                                            }

                                            if (dataSnapshot.hasChild("UID")) {
                                                final String uidget = dataSnapshot.child("UID").getValue().toString();
                                                if (uidget.equals(CurrentUserID)) {
                                                    donarHolder.chattext.setVisibility(View.INVISIBLE);
                                                } else {
                                                    donarHolder.chattext.setVisibility(View.VISIBLE);

                                                    donarHolder.Mview.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            goto_chatpage(new ChatPage(), uidget);
                                                        }
                                                    });


                                                }
                                            }

                                            donarHolder.LikeImage.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                /*    if (rewardedVideoAd.isLoaded()) {
                                                        rewardedVideoAd.show();
                                                    }*/


                                                    likeboolen = true;

                                                    MLikeDatabase.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                            if (likeboolen.equals(true)) {
                                                                if (dataSnapshot.child(UID).hasChild(CurrentUserID)) {
                                                                    MLikeDatabase.child(UID).child(CurrentUserID).removeValue();
                                                                    likeboolen = false;
                                                                } else {
                                                                    MLikeDatabase.child(UID).child(CurrentUserID).setValue(true);
                                                                    likeboolen = false;
                                                                }
                                                            }


                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            });


                                        } else if (displayget.equals("invisiable")) {
                                            donarHolder.toplayout.setVisibility(View.GONE);
                                        }

                                    }


                                    //      }


                                    //     }


                                } else {


                                    infolayout.setVisibility(View.VISIBLE);

                                }
                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        };


        donarlist.setAdapter(firebaseRecyclerAdapter);
    }

    /// search by username


    private void startSerchingQuryy(String sec) {
        String query = sec.toLowerCase();
        final Query firebaseQry = MdonarDatabase.orderByChild("search").startAt(query).endAt(query + "\uf8ff");


        FirebaseRecyclerAdapter<donar_holder, DonarHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<donar_holder, DonarHolder>(
                donar_holder.class,
                R.layout.donor_banner_update,
                DonarHolder.class,
                firebaseQry
        ) {
            @Override
            protected void populateViewHolder(final DonarHolder donarHolder, final donar_holder donar_holder, int i) {

                final String UID = getRef(i).getKey();
                MdonarDatabase.child(UID)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    infolayout.setVisibility(View.INVISIBLE);

                                    if (dataSnapshot.hasChild("display")) {

                                        String displayget = dataSnapshot.child("display").getValue().toString();

                                        if (displayget.equals("visiable")) {
                                            donarHolder.toplayout.setVisibility(View.VISIBLE);


                                            if (dataSnapshot.hasChild("DonarPost")) {
                                                String donarname = dataSnapshot.child("DonarPost").getValue().toString();
                                                donarHolder.setUsernameset(donarname);

                                            }
                                            if (dataSnapshot.hasChild("donar_number")) {
                                                String number = dataSnapshot.child("donar_number").getValue().toString();
                                                donarHolder.setmobilset(number);
                                            }
                                            if (dataSnapshot.hasChild("donar_location")) {
                                                String dlocation = dataSnapshot.child("donar_location").getValue().toString();
                                                donarHolder.setLocationset(dlocation);
                                            }
                                            if (dataSnapshot.hasChild("donar_bloodgroup")) {
                                                String bloodgroupset = dataSnapshot.child("donar_bloodgroup").getValue().toString();
                                                donarHolder.setBloodset(bloodgroupset);
                                            }
                                            if (dataSnapshot.hasChild("donar_post")) {
                                                String messgetxrt = dataSnapshot.child("donar_post").getValue().toString();
                                                donarHolder.setmessage(messgetxrt);
                                            }
                                            if (dataSnapshot.hasChild("date")) {
                                                String date = dataSnapshot.child("date").getValue().toString();
                                                donarHolder.donar_date(date);
                                            }


                                            if (dataSnapshot.hasChild("login_name")) {
                                                String name = dataSnapshot.child("login_name").getValue().toString();
                                                donarHolder.setUsernameset(name);
                                                donarHolder.setProfileimagenamebellowset(name);
                                            }

                                            //
                                            if (dataSnapshot.hasChild("donar_name")) {
                                                String name = dataSnapshot.child("donar_name").getValue().toString();
                                                donarHolder.setProfileimagenamebellowset(name);
                                            }


                                            if (dataSnapshot.hasChild("donar_profile_imageURL")) {
                                                String imageuri = dataSnapshot.child("donar_profile_imageURL").getValue().toString();
                                                donarHolder.setProfileimageset(imageuri);
                                            }

                                            if (dataSnapshot.hasChild("UID")) {
                                                final String uidget = dataSnapshot.child("UID").getValue().toString();
                                                if (uidget.equals(CurrentUserID)) {
                                                    donarHolder.chattext.setVisibility(View.INVISIBLE);
                                                } else {
                                                    donarHolder.chattext.setVisibility(View.VISIBLE);

                                                    donarHolder.Mview.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            goto_chatpage(new ChatPage(), uidget);
                                                        }
                                                    });

                                                }
                                            }

                                            donarHolder.LikeImage.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                 /*   if (rewardedVideoAd.isLoaded()) {
                                                        rewardedVideoAd.show();
                                                    }*/


                                                    likeboolen = true;
                                                    MLikeDatabase.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                            if (likeboolen.equals(true)) {
                                                                if (dataSnapshot.child(UID).hasChild(CurrentUserID)) {
                                                                    MLikeDatabase.child(UID).child(CurrentUserID).removeValue();
                                                                    likeboolen = false;
                                                                } else {
                                                                    MLikeDatabase.child(UID).child(CurrentUserID).setValue(true);
                                                                    likeboolen = false;
                                                                }
                                                            }


                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            });


                                        } else if (displayget.equals("invisiable")) {
                                            donarHolder.toplayout.setVisibility(View.GONE);
                                        }
                                    }


                                } else {
                                    infolayout.setVisibility(View.VISIBLE);
                                }
                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        };


        donarlist.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onStart() {



        Query firebasedes_query = MdonarDatabase.orderByChild("short");


        FirebaseRecyclerAdapter<donar_holder, DonarHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<donar_holder, DonarHolder>(
                donar_holder.class,
                R.layout.donor_banner_update,
                DonarHolder.class,
                firebasedes_query
        ) {
            @Override
            protected void populateViewHolder(final DonarHolder donarHolder, final donar_holder donar_holder, int i) {

                final String UID = getRef(i).getKey();
                MdonarDatabase.child(UID)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {


                                    infolayout.setVisibility(View.INVISIBLE);

                                    MdonarDatabase.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Long count = dataSnapshot.getChildrenCount();


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    if (dataSnapshot.hasChild("short")) {

                                    }




                                    donarHolder.doreadlike_ref(UID);


                                    if (dataSnapshot.hasChild("display")) {
                                        String displayget = dataSnapshot.child("display").getValue().toString();



                                        if (displayget.equals("visiable")) {

                                            donarHolder.toplayout.setVisibility(View.VISIBLE);


                                            if (dataSnapshot.hasChild("donar_profile_imageURL")) {
                                                String imageuri = dataSnapshot.child("donar_profile_imageURL").getValue().toString();
                                                donarHolder.setProfileimageset(imageuri);
                                            }
                                            if (dataSnapshot.hasChild("donar_number")) {
                                                String number = dataSnapshot.child("donar_number").getValue().toString();
                                                donarHolder.setmobilset(number);
                                            }
                                            if (dataSnapshot.hasChild("donar_location")) {
                                                String dlocation = dataSnapshot.child("donar_location").getValue().toString();
                                                donarHolder.setLocationset(dlocation);
                                            }
                                            if (dataSnapshot.hasChild("donar_bloodgroup")) {
                                                String bloodgroupset = dataSnapshot.child("donar_bloodgroup").getValue().toString();
                                                donarHolder.setBloodset(bloodgroupset);
                                            }
                                            if (dataSnapshot.hasChild("donar_post")) {
                                                String messgetxrt = dataSnapshot.child("donar_post").getValue().toString();
                                                donarHolder.setmessage(messgetxrt);
                                            }

                                            if (dataSnapshot.hasChild("login_name")) {
                                                String name = dataSnapshot.child("login_name").getValue().toString();
                                                donarHolder.setProfileimagenamebellowset(name);
                                            }

                                            if (dataSnapshot.hasChild("donar_name")) {
                                                String name = dataSnapshot.child("donar_name").getValue().toString();
                                                donarHolder.setUsernameset(name);
                                            }

                                            if (dataSnapshot.hasChild("date")) {
                                                String date = dataSnapshot.child("date").getValue().toString();
                                                donarHolder.donar_date(date);
                                            }

                                            if (dataSnapshot.hasChild("UID")) {
                                                final String uidget = dataSnapshot.child("UID").getValue().toString();
                                                if (uidget.equals(CurrentUserID)) {
                                                    donarHolder.chattext.setVisibility(View.INVISIBLE);
                                                } else {

                                                    donarHolder.chattext.setVisibility(View.VISIBLE);

                                                    donarHolder.Mview.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            goto_chatpage(new ChatPage(), uidget);

                                                        }
                                                    });


                                                }
                                            }


                                            donarHolder.LikeImage.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    likeboolen = true;

                                                    MLikeDatabase.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                            if (likeboolen.equals(true)) {
                                                                if (dataSnapshot.child(UID).hasChild(CurrentUserID)) {
                                                                    MLikeDatabase.child(UID).child(CurrentUserID).removeValue();
                                                                    likeboolen = false;
                                                                } else {
                                                                    MLikeDatabase.child(UID).child(CurrentUserID).setValue(true);
                                                                    likeboolen = false;
                                                                }
                                                            }


                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            });


                                        } else if (displayget.equals("invisiable")) {
                                            donarHolder.toplayout.setVisibility(View.GONE);
                                        }

                                    }


                                } else {

                                    infolayout.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        };

        donarlist.setAdapter(firebaseRecyclerAdapter);
        super.onStart();
    }


    public static class DonarHolder extends RecyclerView.ViewHolder {

        private View Mview;
        private Context context;
        private ImageView chattext;
        private TextView Username;
        private TextView Mobile;
        private TextView Location;
        private TextView Blood;
        private CircleImageView profileimage;
        private TextView profileimagenamebellow;
        private TextView donarmessage;
        private TextView postdate;

        private ImageView LikeImage;
        private DatabaseReference MLikeDatabase;
        private FirebaseAuth Mauth;
        private String CurrentuserID;
        private TextView CunterText;
        private int counter;

        private MaterialCardView toplayout;

        public DonarHolder(@NonNull View itemView) {
            super(itemView);

            Mview = itemView;
            context = Mview.getContext();

            CunterText = Mview.findViewById(R.id.LikeCounter);
            Mauth = FirebaseAuth.getInstance();
            CurrentuserID = Mauth.getCurrentUser().getUid();
            MLikeDatabase = FirebaseDatabase.getInstance().getReference().child("DonarLike");


            LikeImage = Mview.findViewById(R.id.HardIcon);
            postdate = Mview.findViewById(R.id.DonardteID);
            chattext = Mview.findViewById(R.id.ChatID);
            Username = Mview.findViewById(R.id.Usernames);
            Mobile = Mview.findViewById(R.id.DonarPhoneNumberID);
            Location = Mview.findViewById(R.id.DonarocationID);
            Blood = Mview.findViewById(R.id.DonarBlooad);
            profileimage = Mview.findViewById(R.id.profileImageCardID);
            profileimagenamebellow = Mview.findViewById(R.id.UserNameTexts);
            donarmessage = Mview.findViewById(R.id.MessageTextID);

            toplayout = Mview.findViewById(R.id.CardViewID);
        }

        private void setUsernameset(String nam) {
            Username.setText(nam);
        }

        private void setmobilset(String mob) {
            Mobile.setText(mob);
        }

        private void setLocationset(String loc) {
            Location.setText(loc);
        }

        private void setBloodset(String blood) {
            Blood.setText(blood);
        }

        private void setProfileimageset(String img) {
            Picasso.with(context).load(img).placeholder(R.drawable.defaultimage).into(profileimage);
            Picasso.with(context).load(img).placeholder(R.drawable.defaultimage).networkPolicy(NetworkPolicy.OFFLINE).into(profileimage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });
        }

        private void setProfileimagenamebellowset(String na) {
            profileimagenamebellow.setText(na);
        }

        private void setmessage(String mess) {
            donarmessage.setText(mess);
        }

        private void donar_date(String date) {
            postdate.setText(date);
        }


        private void doreadlike_ref(final String UID) {
            MLikeDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(UID).hasChild(CurrentuserID)) {
                        counter = (int) dataSnapshot.child(UID).getChildrenCount();
                        CunterText.setText(Long.toString(counter));
                        LikeImage.setImageResource(R.drawable.like);
                    } else {
                        counter = (int) dataSnapshot.child(UID).getChildrenCount();
                        CunterText.setText(Long.toString(counter));
                        LikeImage.setImageResource(R.drawable.hart_icon);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


    private void goto_chatpage(Fragment fragment, String UID){
        Bundle bundle = new Bundle();
        bundle.putString("KEY", UID);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slider_from_right    , R.anim.slide_outfrom_left);



        transaction.replace(R.id.MainFreamContiner, fragment).addToBackStack(null);
        transaction.commit();
    }

}