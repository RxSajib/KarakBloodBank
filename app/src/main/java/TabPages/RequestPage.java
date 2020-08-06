package TabPages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.google.firebase.database.ChildEventListener;
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


public class RequestPage extends Fragment {

    private FirebaseAuth Mauth;
    private String CurrentuserID;
    private DatabaseReference MRequestDatabase;
    private RecyclerView MList;
    private DatabaseReference MLikeRef;
    private Boolean likestate = false;
    private RelativeLayout datainfo;
    private SearchView searchView;
    private Spinner spinneriteams;
    private String[] filter = {"Filter", "Username", "Blood"};


    public RequestPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.request_page, container, false);


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        datainfo = view.findViewById(R.id.RequestLayoutID);

        spinneriteams = view.findViewById(R.id.SearchSpinnerID);
        ArrayAdapter<String> spinneradapter = new ArrayAdapter<String>(getContext(), R.layout.blood_search_, R.id.SearchIteamsID, filter);
        spinneriteams.setAdapter(spinneradapter);

        searchView = view.findViewById(R.id.RequesterSearchID);

        MLikeRef = FirebaseDatabase.getInstance().getReference().child("Request_Like");
        MLikeRef.keepSynced(true);
        MList = view.findViewById(R.id.UserRecylearViewID);
        MList.setHasFixedSize(true);
        MList.setLayoutManager(new LinearLayoutManager(getContext()));
        MRequestDatabase = FirebaseDatabase.getInstance().getReference().child("Request_post");
        MRequestDatabase.keepSynced(true);
        Mauth = FirebaseAuth.getInstance();
        CurrentuserID = Mauth.getCurrentUser().getUid();

        ///come

        StratReadingVie();

        spinneriteams.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String cicktext = parent.getItemAtPosition(position).toString();
                if (cicktext.equals("Filter")) {
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
                if (cicktext.equals("Blood")) {
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            start_searching(query);
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            start_searching(newText);
                            return false;
                        }
                    });
                }
                if (cicktext.equals("Username")) {
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {

                            start_searchingusername(query);

                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            start_searchingusername(newText);
                            return false;
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        desening_order_data();


        return view;
    }


    private void desening_order_data() {

    }

    // searching by username
    private void start_searchingusername(String sec) {

        String query = sec.toLowerCase();
        final Query firebaseQry = MRequestDatabase.orderByChild("search_username").startAt(query).endAt(query + "\uf8ff");


        FirebaseRecyclerAdapter<donar_holder, DonarHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<donar_holder, DonarHolder>(
                donar_holder.class,
                R.layout.request_user_bannerupdate,
                DonarHolder.class,
                firebaseQry
        ) {
            @Override
            protected void populateViewHolder(final DonarHolder donarHolder, final donar_holder donar_holder, int i) {

                final String UID = getRef(i).getKey();


                MRequestDatabase.child(UID)
                        .addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    String count = ds.getValue().toString();
                                    Log.d("TAG", count);
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


                MRequestDatabase.child(UID)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    donarHolder.onreading_likedata(UID);

                                    datainfo.setVisibility(View.INVISIBLE);


                                    if (dataSnapshot.hasChild("display")) {

                                        String displayget = dataSnapshot.child("display").getValue().toString();

                                        if (displayget.equals("visiable")) {
                                            donarHolder.toplayout.setVisibility(View.VISIBLE);

                                            if (dataSnapshot.hasChild("blood_group")) {
                                                String geoup = dataSnapshot.child("blood_group").getValue().toString();
                                                donarHolder.setBloodgroupset(geoup);
                                            }

                                            if (dataSnapshot.hasChild("date")) {
                                                String datstring = dataSnapshot.child("date").getValue().toString();
                                                donarHolder.setDatetextset(datstring);
                                            }
                                            if (dataSnapshot.hasChild("location")) {
                                                String locationget = dataSnapshot.child("location").getValue().toString();
                                                donarHolder.setLocationset(locationget);
                                            }
                                            if (dataSnapshot.hasChild("patentname")) {
                                                String nam = dataSnapshot.child("patentname").getValue().toString();

                                                donarHolder.setUsernameset(nam);

                                            }

                                            if (dataSnapshot.hasChild("loginusername")) {
                                                String nam = dataSnapshot.child("loginusername").getValue().toString();

                                                donarHolder.setProfileimagebellow_nameset(nam);
                                            }

                                            if (dataSnapshot.hasChild("Image_downloadurl")) {
                                                String uri = dataSnapshot.child("Image_downloadurl").getValue().toString();
                                                donarHolder.setProfileimageset(uri);
                                            }

                                            if (dataSnapshot.hasChild("message")) {
                                                String messageget = dataSnapshot.child("message").getValue().toString();
                                                donarHolder.setMessageset(messageget);
                                            }
                                            if (dataSnapshot.hasChild("mobilenumber")) {
                                                String mobile = dataSnapshot.child("mobilenumber").getValue().toString();
                                                donarHolder.setMobilenumberset(mobile);
                                            }

                                            if (dataSnapshot.hasChild("userid")) {
                                                final String uid_get = dataSnapshot.child("userid").getValue().toString();

                                                if (uid_get.equals(CurrentuserID)) {
                                                    donarHolder.chaaticon.setVisibility(View.INVISIBLE);
                                                } else {
                                                    donarHolder.chaaticon.setVisibility(View.VISIBLE);
                                                }

                                                donarHolder.Mview.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        if (uid_get.equals(CurrentuserID)) {
                                                        } else {
                                                            goto_chatpage(new ChatPage(), uid_get);
                                                        }
                                                    }
                                                });
                                            }

                                            donarHolder.LikeImage.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    likestate = true;
                                                    MLikeRef.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                            if (likestate.equals(true)) {
                                                                if (dataSnapshot.child(UID).hasChild(CurrentuserID)) {
                                                                    MLikeRef.child(UID).child(CurrentuserID).removeValue();
                                                                    likestate = false;
                                                                } else {
                                                                    MLikeRef.child(UID).child(CurrentuserID).setValue(true);
                                                                    likestate = false;
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
                                    datainfo.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        };

        MList.setAdapter(firebaseRecyclerAdapter);
    }
    // searching by username


    private void start_searching(String sec) {

        String query = sec.toLowerCase();
        final Query firebaseQry = MRequestDatabase.orderByChild("search").startAt(query).endAt(query + "\uf8ff");


        FirebaseRecyclerAdapter<donar_holder, DonarHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<donar_holder, DonarHolder>(
                donar_holder.class,
                R.layout.request_user_bannerupdate,
                DonarHolder.class,
                firebaseQry
        ) {
            @Override
            protected void populateViewHolder(final DonarHolder donarHolder, final donar_holder donar_holder, int i) {

                final String UID = getRef(i).getKey();


                MRequestDatabase.child(UID)
                        .addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    String count = ds.getValue().toString();
                                    Log.d("TAG", count);
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


                MRequestDatabase.child(UID)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    donarHolder.onreading_likedata(UID);

                                    datainfo.setVisibility(View.INVISIBLE);

                                    if (dataSnapshot.hasChild("display")) {

                                        String displayget = dataSnapshot.child("display").getValue().toString();

                                        if (displayget.equals("visiable")) {

                                            donarHolder.toplayout.setVisibility(View.VISIBLE);

                                            if (dataSnapshot.hasChild("blood_group")) {
                                                String geoup = dataSnapshot.child("blood_group").getValue().toString();
                                                donarHolder.setBloodgroupset(geoup);
                                            }

                                            if (dataSnapshot.hasChild("date")) {
                                                String datstring = dataSnapshot.child("date").getValue().toString();
                                                donarHolder.setDatetextset(datstring);
                                            }
                                            if (dataSnapshot.hasChild("location")) {
                                                String locationget = dataSnapshot.child("location").getValue().toString();
                                                donarHolder.setLocationset(locationget);
                                            }
                                            if (dataSnapshot.hasChild("patentname")) {
                                                String nam = dataSnapshot.child("patentname").getValue().toString();

                                                donarHolder.setUsernameset(nam);

                                            }

                                            if (dataSnapshot.hasChild("loginusername")) {
                                                String nam = dataSnapshot.child("loginusername").getValue().toString();

                                                donarHolder.setProfileimagebellow_nameset(nam);
                                            }

                                            if (dataSnapshot.hasChild("Image_downloadurl")) {
                                                String uri = dataSnapshot.child("Image_downloadurl").getValue().toString();
                                                donarHolder.setProfileimageset(uri);
                                            }

                                            if (dataSnapshot.hasChild("message")) {
                                                String messageget = dataSnapshot.child("message").getValue().toString();
                                                donarHolder.setMessageset(messageget);
                                            }
                                            if (dataSnapshot.hasChild("mobilenumber")) {
                                                String mobile = dataSnapshot.child("mobilenumber").getValue().toString();
                                                donarHolder.setMobilenumberset(mobile);
                                            }

                                            if (dataSnapshot.hasChild("userid")) {
                                                final String uid_get = dataSnapshot.child("userid").getValue().toString();

                                                if (uid_get.equals(CurrentuserID)) {
                                                    donarHolder.chaaticon.setVisibility(View.INVISIBLE);
                                                } else {
                                                    donarHolder.chaaticon.setVisibility(View.VISIBLE);
                                                }

                                                donarHolder.Mview.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        if (uid_get.equals(CurrentuserID)) {
                                                        } else {
                                                            goto_chatpage(new ChatPage(), uid_get);
                                                        }
                                                    }
                                                });
                                            }

                                            donarHolder.LikeImage.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    likestate = true;
                                                    MLikeRef.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                            if (likestate.equals(true)) {
                                                                if (dataSnapshot.child(UID).hasChild(CurrentuserID)) {
                                                                    MLikeRef.child(UID).child(CurrentuserID).removeValue();
                                                                    likestate = false;
                                                                } else {
                                                                    MLikeRef.child(UID).child(CurrentuserID).setValue(true);
                                                                    likestate = false;
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

                                    datainfo.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        };

        MList.setAdapter(firebaseRecyclerAdapter);
    }


    public void StratReadingVie() {
        final Query firebasedes_query = MRequestDatabase.orderByChild("counter");
        FirebaseRecyclerAdapter<donar_holder, DonarHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<donar_holder, DonarHolder>(
                donar_holder.class,
                R.layout.request_user_bannerupdate,
                DonarHolder.class,
                firebasedes_query
        ) {
            @Override
            protected void populateViewHolder(final DonarHolder donarHolder, final donar_holder donar_holder, int i) {

                final String UID = getRef(i).getKey();
                MRequestDatabase.child(UID)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    datainfo.setVisibility(View.INVISIBLE);

                                    if (dataSnapshot.hasChild("counter")) {
                                        MRequestDatabase
                                                .addChildEventListener(new ChildEventListener() {
                                                    @Override
                                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                        for (DataSnapshot db : dataSnapshot.getChildren()) {


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

                                    donarHolder.onreading_likedata(UID);




                                    if (dataSnapshot.hasChild("display")) {

                                        String displayget = dataSnapshot.child("display").getValue().toString();

                                        if (displayget.equals("visiable")) {
                                            donarHolder.toplayout.setVisibility(View.VISIBLE);


                                            if (dataSnapshot.hasChild("blood_group")) {
                                                String geoup = dataSnapshot.child("blood_group").getValue().toString();
                                                donarHolder.setBloodgroupset(geoup);
                                            }
                                            if (dataSnapshot.hasChild("date")) {
                                                String datstring = dataSnapshot.child("date").getValue().toString();
                                                donarHolder.setDatetextset(datstring);
                                            }
                                            if (dataSnapshot.hasChild("Image_downloadurl")) {
                                                String uri = dataSnapshot.child("Image_downloadurl").getValue().toString();
                                                donarHolder.setProfileimageset(uri);
                                            }
                                            if (dataSnapshot.hasChild("location")) {
                                                String locationget = dataSnapshot.child("location").getValue().toString();
                                                donarHolder.setLocationset(locationget);
                                            }
                                            if (dataSnapshot.hasChild("patentname")) {
                                                String nam = dataSnapshot.child("patentname").getValue().toString();

                                                donarHolder.setUsernameset(nam);
                                            }
                                            //
                                            if (dataSnapshot.hasChild("loginusername")) {
                                                String nam = dataSnapshot.child("loginusername").getValue().toString();

                                                donarHolder.setProfileimagebellow_nameset(nam);
                                            }

                                            if (dataSnapshot.hasChild("message")) {
                                                String messageget = dataSnapshot.child("message").getValue().toString();
                                                donarHolder.setMessageset(messageget);
                                            }
                                            if (dataSnapshot.hasChild("mobilenumber")) {
                                                String mobile = dataSnapshot.child("mobilenumber").getValue().toString();
                                                donarHolder.setMobilenumberset(mobile);
                                            }

                                            if (dataSnapshot.hasChild("userid")) {
                                                final String uid_get = dataSnapshot.child("userid").getValue().toString();

                                                if (uid_get.equals(CurrentuserID)) {
                                                    donarHolder.chaaticon.setVisibility(View.INVISIBLE);
                                                } else {
                                                    donarHolder.chaaticon.setVisibility(View.VISIBLE);
                                                }

                                                donarHolder.Mview.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        if (uid_get.equals(CurrentuserID)) {
                                                        } else {

                                                            goto_chatpage(new ChatPage(), uid_get);
                                                        }
                                                    }
                                                });
                                            }

                                            donarHolder.LikeImage.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    likestate = true;
                                                    MLikeRef.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                            if (likestate.equals(true)) {
                                                                if (dataSnapshot.child(UID).hasChild(CurrentuserID)) {
                                                                    MLikeRef.child(UID).child(CurrentuserID).removeValue();
                                                                    likestate = false;
                                                                } else {
                                                                    MLikeRef.child(UID).child(CurrentuserID).setValue(true);
                                                                    likestate = false;
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
                                    datainfo.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        };

        MList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class DonarHolder extends RecyclerView.ViewHolder {

        private View Mview;
        private CircleImageView profileimage;
        private TextView profileimagebellow_name;
        private TextView username;
        private TextView Mobilenumber;
        private TextView Location;
        private TextView Bloodgroup;
        private TextView Message;
        private Context context;
        private TextView time;
        private ImageView LikeImage;

        private DatabaseReference MLikedatabase;
        private FirebaseAuth Mauth;
        private String CurrentUserID;
        private int counter;
        private TextView Counttext;
        private ImageView chaaticon;
        private TextView datetext;

        private MaterialCardView toplayout;

        public DonarHolder(@NonNull View itemView) {
            super(itemView);

            Mview = itemView;
            chaaticon = Mview.findViewById(R.id.DChatID);
            context = Mview.getContext();
            profileimage = Mview.findViewById(R.id.RprofileImageCardID);
            profileimagebellow_name = Mview.findViewById(R.id.CurrentUserName);
            username = Mview.findViewById(R.id.RUsernames);
            Mobilenumber = Mview.findViewById(R.id.RDonarPhoneNumberID);
            Location = Mview.findViewById(R.id.RDonarocationID);
            Bloodgroup = Mview.findViewById(R.id.RDonarBlooad);
            Message = Mview.findViewById(R.id.RMessageTextID);
            time = Mview.findViewById(R.id.DonardteID);
            LikeImage = Mview.findViewById(R.id.DHardIcon);
            datetext = Mview.findViewById(R.id.FDonardteID);

            Counttext = Mview.findViewById(R.id.DLikeCounter);
            MLikedatabase = FirebaseDatabase.getInstance().getReference().child("Request_Like");
            Mauth = FirebaseAuth.getInstance();
            CurrentUserID = Mauth.getCurrentUser().getUid();

            toplayout = Mview.findViewById(R.id.CardlayoutID);
        }

        private void setProfileimageset(String img) {
            Picasso.with(context).load(img).placeholder(R.drawable.defaultimage).into(profileimage);

            Picasso.with(context).load(img).placeholder(R.drawable.defaultimage).networkPolicy(NetworkPolicy.OFFLINE)
                    .into(profileimage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });
        }

        private void setUsernameset(String nam) {
            username.setText(nam);
        }

        private void setMobilenumberset(String mobile) {
            Mobilenumber.setText(mobile);
        }

        private void setLocationset(String loc) {
            Location.setText(loc);
        }

        private void setMessageset(String mess) {
            Message.setText(mess);
        }

        private void setTimeset(String tim) {
            time.setText(tim);
        }

        private void setBloodgroupset(String blood) {
            Bloodgroup.setText(blood);
        }

        private void setProfileimagebellow_nameset(String nam) {
            profileimagebellow_name.setText(nam);
        }

        private void setDatetextset(String dat) {
            datetext.setText(dat);
        }

        private void onreading_likedata(final String UID) {
            MLikedatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(UID).hasChild(CurrentUserID)) {
                        counter = (int) dataSnapshot.child(UID).getChildrenCount();
                        LikeImage.setImageResource(R.drawable.like);
                        Counttext.setText(Long.toString(counter));
                    } else {
                        counter = (int) dataSnapshot.child(UID).getChildrenCount();
                        Counttext.setText(Long.toString(counter));
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

        if(fragment != null || UID != null){
            Bundle bundle = new Bundle();
            bundle.putString("KEY", UID);
            fragment.setArguments(bundle);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slider_from_right    , R.anim.slide_outfrom_left);
            transaction.replace(R.id.MainFreamContiner, fragment).addToBackStack(null);
            transaction.commit();
        }


    }

}