package ChatPage;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.MessageAdapter;
import AllPogoClass.MessageHolder;
import Common.Common;
import Model.Myresponce;
import Model.Notification;
import Model.Sender;
import Model.Token;
import Remote.APIservice;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class ChatPage extends Fragment {


    private String ReciverID;
    private Toolbar toolbar;
    private DatabaseReference MuserDatabase;
    private TextView name;
    private CircleImageView profileimage;
    private TextView Messagetext;
    private ImageButton sendbutton;
    private String SenderID;
    private FirebaseAuth Mauth;
    private EditText message;
    private DatabaseReference Mroodref;
    private String CurrentTime, CurrentDate;

    private RecyclerView messagerecylearview;
    private List<MessageHolder> messageHolderList = new ArrayList<>();
    private DatabaseReference Messageref;
    private MessageAdapter messageAdapter;

    private DatabaseReference MMessageEdit;

    private ImageView send;
    private String SendType = "";
    private Uri imageuri;
    private StorageReference Mimagestore;

    private DatabaseReference MOnlineStatasDatabase;

    private ImageView onlineimage;
    private TextView onlinetime;
    private APIservice mService;
    private DatabaseReference MNotifactionLogo;

    private ImageView back_icon;

    private ProgressDialog SendingJob;

    public ChatPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.chat_page, container, false);

        SendingJob = new ProgressDialog(getActivity());

        back_icon = view.findViewById(R.id.backbuttonIDs);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            }
        });

        MNotifactionLogo = FirebaseDatabase.getInstance().getReference().child("Notifaction_Logo");
        mService = Common.getFCMClient();
        onlineimage = view.findViewById(R.id.OnlineImage);
        onlinetime = view.findViewById(R.id.OnlineDate);

        Mimagestore = FirebaseStorage.getInstance().getReference();
        MOnlineStatasDatabase = FirebaseDatabase.getInstance().getReference().child("Statas");

        send = view.findViewById(R.id.PlusID);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence charSequence[] = new CharSequence[]{
                        "Send Image",
                        "Send Pdf",
                        "Send Doc"
                };

                AlertDialog.Builder Mbuilder = new AlertDialog.Builder(getActivity());

                Mbuilder.setItems(charSequence, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            SendType = "image";
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "select IMAGE"), 511);
                        }
                        if (which == 1) {
                            SendType = "pdf";
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/pdf");
                            startActivityForResult(Intent.createChooser(intent, "select PDF file"), 511);
                        }
                        if (which == 2) {
                            SendType = "doc";
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/msword");
                            startActivityForResult(Intent.createChooser(intent, "select DOC file"), 511);
                        }
                    }
                });

                AlertDialog alertDialog = Mbuilder.create();
                alertDialog.show();

            }
        });

        MMessageEdit = FirebaseDatabase.getInstance().getReference().child("Edit_message");
        MMessageEdit.keepSynced(true);

        Messageref = FirebaseDatabase.getInstance().getReference();
        Messageref.keepSynced(true);
        messagerecylearview = view.findViewById(R.id.ChatListID);
        messagerecylearview.setHasFixedSize(true);
        messagerecylearview.setLayoutManager(new LinearLayoutManager(getActivity()));
        messageAdapter = new MessageAdapter(messageHolderList);
        messagerecylearview.setAdapter(messageAdapter);

        Mroodref = FirebaseDatabase.getInstance().getReference();
        Mroodref.keepSynced(true);
        Mauth = FirebaseAuth.getInstance();
        SenderID = Mauth.getCurrentUser().getUid();
        message = view.findViewById(R.id.MessageTextID);
        sendbutton = view.findViewById(R.id.MSendButtonID);


        profileimage = view.findViewById(R.id.PofileImageID);
        name = view.findViewById(R.id.usernameTID);

        Bundle bundle = getArguments();
        ReciverID = bundle.getString("KEY");

        //   ReciverID = getIntent().getStringExtra("KEY");


        toolbar = view.findViewById(R.id.ChatToolbarID);

        MuserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(ReciverID);
        MuserDatabase.keepSynced(true);
        reading_uservalue();

        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sending_Message();

            }
        });

        readingMessage();

        MMessageEdit
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            if (dataSnapshot.hasChild("message")) {
                                String messageget = dataSnapshot.child("message").getValue().toString();
                                message.setText(messageget);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        /// online stattas
        onlineStatas("online");
        readOnlineMood();
        /// online stattas


        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 511 && resultCode == RESULT_OK) {

        //    Toast.makeText(getActivity(), "image sending ...", Toast.LENGTH_SHORT).show();

            SendingJob.setTitle("Wait for a moment");
            SendingJob.setMessage("Please wait sending your file");
            SendingJob.setCanceledOnTouchOutside(false);
            SendingJob.show();

            imageuri = data.getData();
            if (SendType.equals("image")) {
                setlogogdatabase();
                StorageReference filepath = Mimagestore.child("send_image").child(imageuri.getLastPathSegment());
                filepath.putFile(imageuri)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {

                                    String imagedownloaduri = task.getResult().getDownloadUrl().toString();

                                    String message_sender_ref = "Message/" + SenderID + "/" + ReciverID;
                                    String message_reciver_ref = "Message/" + ReciverID + "/" + SenderID;

                                    DatabaseReference user_message_key = Mroodref.child("Message").child(SenderID).child(ReciverID)
                                            .push();

                                    String message_push_id = user_message_key.getKey();

                                    Calendar calendartime = Calendar.getInstance();
                                    SimpleDateFormat simpleDateFormattime = new SimpleDateFormat("HH:mm");
                                    CurrentTime = simpleDateFormattime.format(calendartime.getTime());

                                    Calendar calendardate = Calendar.getInstance();
                                    SimpleDateFormat simpleDateFormatdate = new SimpleDateFormat("yyyy-MM-dd");
                                    CurrentDate = simpleDateFormatdate.format(calendardate.getTime());

                                    Map messagemap = new HashMap();
                                    messagemap.put("message", imagedownloaduri);
                                    messagemap.put("time", CurrentTime);
                                    messagemap.put("date", CurrentDate);
                                    messagemap.put("from", SenderID);
                                    messagemap.put("type", "image");

                                    Map messagebody = new HashMap();
                                    messagebody.put(message_sender_ref + "/" + message_push_id, messagemap);
                                    messagebody.put(message_reciver_ref + "/" + message_push_id, messagemap);

                                    message.setText("");
                                    Mroodref.updateChildren(messagebody)
                                            .addOnCompleteListener(new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                    if (task.isSuccessful()) {

                                                        SendingJob.dismiss();
                                                        find_username_and_send_notification();
                                                    } else {
                                                        SendingJob.dismiss();
                                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            SendingJob.dismiss();
                                            Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                                        }
                                    });


                                } else {
                                    SendingJob.dismiss();
                                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        SendingJob.dismiss();
                        Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
            } else if (!SendType.equals("image")) {

                Toast.makeText(getActivity(), "doc file sending ...", Toast.LENGTH_SHORT).show();

                setlogogdatabase();


                StorageReference filepath = Mimagestore.child("send_document").child(imageuri.getLastPathSegment());
                filepath.putFile(imageuri)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {


                                    String imagedownloaduri = task.getResult().getDownloadUrl().toString();

                                    String message_sender_ref = "Message/" + SenderID + "/" + ReciverID;
                                    String message_reciver_ref = "Message/" + ReciverID + "/" + SenderID;

                                    DatabaseReference user_message_key = Mroodref.child("Message").child(SenderID).child(ReciverID)
                                            .push();

                                    String message_push_id = user_message_key.getKey();

                                    Calendar calendartime = Calendar.getInstance();
                                    SimpleDateFormat simpleDateFormattime = new SimpleDateFormat("HH:mm");
                                    CurrentTime = simpleDateFormattime.format(calendartime.getTime());

                                    Calendar calendardate = Calendar.getInstance();
                                    SimpleDateFormat simpleDateFormatdate = new SimpleDateFormat("yyyy-MM-dd");
                                    CurrentDate = simpleDateFormatdate.format(calendardate.getTime());

                                    Map messagemap = new HashMap();
                                    messagemap.put("message", imagedownloaduri);
                                    messagemap.put("time", CurrentTime);
                                    messagemap.put("date", CurrentDate);
                                    messagemap.put("from", SenderID);
                                    messagemap.put("type", SendType);

                                    Map messagebody = new HashMap();
                                    messagebody.put(message_sender_ref + "/" + message_push_id, messagemap);
                                    messagebody.put(message_reciver_ref + "/" + message_push_id, messagemap);

                                    message.setText("");
                                    Mroodref.updateChildren(messagebody)
                                            .addOnCompleteListener(new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                    if (task.isSuccessful()) {
                                                        SendingJob.dismiss();
                                                        find_username_and_send_notification();
                                                    } else {
                                                        SendingJob.dismiss();
                                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            SendingJob.dismiss();
                                            Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                                        }
                                    });


                                } else {
                                    SendingJob.dismiss();
                                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        SendingJob.dismiss();
                        Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });


            } else {
                SendingJob.dismiss();
                Toast.makeText(getActivity(), "Error select object", Toast.LENGTH_LONG).show();
            }
        }


    }

    /// picup image


    @Override
    public void onDestroy() {
        onlineStatas("offline");
        super.onDestroy();
    }

    @Override
    public void onStop() {
        onlineStatas("offline");
        super.onStop();
    }

    @Override
    public void onStart() {
        onlineStatas("online");
        super.onStart();
    }

    /// online stats
    private void onlineStatas(String statas) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormattime = new SimpleDateFormat("HH:mm");
        String OnlineTime = simpleDateFormattime.format(calendar.getTime());

        Map onlinemap = new HashMap();
        onlinemap.put("statas", statas);
        onlinemap.put("last_seen", OnlineTime);


        MOnlineStatasDatabase.child(SenderID).updateChildren(onlinemap)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {

                        } else {
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }


    //// send notifaction
    private void find_username_and_send_notification() {


        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {

                    String name = dataSnapshot.child("Name").getValue().toString();


                    if (!name.isEmpty()) {

                        // Log.i(TAG, "onDataChange: ");

                        sendNotification(name);


                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    //// send notifaction


    /// send notifaction to another devices
    private void sendNotification(final String sendername) {


        FirebaseDatabase.getInstance().getReference("Token").child(ReciverID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Token token = dataSnapshot.getValue(Token.class);


                String sendermessage = sendername;
                String title = message.getText().toString();




              /*  HashMap<String,String> data=new HashMap<>();
                data.put("title",title);
                data.put("body",message);*/


                Notification notification = new Notification(sendermessage, message.getText().toString());
                Sender noti = new Sender(token.getToken(), notification);


                mService.sendNotification(noti).enqueue(new Callback<Myresponce>() {
                    @Override
                    public void onResponse(Call<Myresponce> call, Response<Myresponce> response) {


                    }

                    @Override
                    public void onFailure(Call<Myresponce> call, Throwable t) {

                        Log.i("STATUS", "onResponse: FAILED ");


                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    /// send notifaction to another devices


    /// online stats

    private void readOnlineMood() {
        MOnlineStatasDatabase.child(ReciverID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            if (dataSnapshot.hasChild("statas")) {
                                String statasget = dataSnapshot.child("statas").getValue().toString();
                                if (statasget.equals("online")) {
                                    onlineimage.setImageResource(R.drawable.online_dot);
                                    onlinetime.setVisibility(View.GONE);
                                }
                                if (statasget.equals("offline")) {
                                    String last_seenget = dataSnapshot.child("last_seen").getValue().toString();
                                    onlineimage.setImageResource(R.drawable.offline_dot);
                                    onlinetime.setVisibility(View.VISIBLE);
                                }
                            }
                            if (dataSnapshot.hasChild("last_seen")) {
                                String last_seenget = dataSnapshot.child("last_seen").getValue().toString();
                                onlinetime.setText(last_seenget);
                            }
                        } else {
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    private void sending_Message() {

        String messagetext = message.getText().toString();
        if (messagetext.isEmpty()) {
            Toast.makeText(getActivity(), "Please input any message", Toast.LENGTH_LONG).show();
        } else {

            setlogogdatabase();

            String message_sender_ref = "Message/" + SenderID + "/" + ReciverID;
            String message_reciver_ref = "Message/" + ReciverID + "/" + SenderID;

            DatabaseReference user_message_key = Mroodref.child("Message").child(SenderID).child(ReciverID)
                    .push();

            String message_push_id = user_message_key.getKey();

            Calendar calendartime = Calendar.getInstance();
            SimpleDateFormat simpleDateFormattime = new SimpleDateFormat("HH:mm");
            CurrentTime = simpleDateFormattime.format(calendartime.getTime());

            Calendar calendardate = Calendar.getInstance();
            SimpleDateFormat simpleDateFormatdate = new SimpleDateFormat("yyyy-MM-dd");
            CurrentDate = simpleDateFormatdate.format(calendardate.getTime());

            Map messagemap = new HashMap();
            messagemap.put("message", messagetext);
            messagemap.put("time", CurrentTime);
            messagemap.put("date", CurrentDate);
            messagemap.put("from", SenderID);
            messagemap.put("type", "text");

            Map messagebody = new HashMap();
            messagebody.put(message_sender_ref + "/" + message_push_id, messagemap);
            messagebody.put(message_reciver_ref + "/" + message_push_id, messagemap);

            message.setText("");
            Mroodref.updateChildren(messagebody)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {

                                find_username_and_send_notification();
                            } else {
                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            });
        }

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            removeNitiiconData();
            //finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void reading_uservalue() {
        MuserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("Name")) {
                        String nameget = dataSnapshot.child("Name").getValue().toString();
                        name.setText(nameget);
                    }
                    if (dataSnapshot.hasChild("imageurl")) {
                        String imageurlget = dataSnapshot.child("imageurl").getValue().toString();
                        Glide.with(getActivity()).load(imageurlget).into(profileimage);
                    }
                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    protected void readingMessage() {
        Messageref.child("Message").child(SenderID).child(ReciverID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        MessageHolder message = dataSnapshot.getValue(MessageHolder.class);
                        messageHolderList.add(message);
                        messageAdapter.notifyDataSetChanged();
                        messagerecylearview.smoothScrollToPosition(messageAdapter.getItemCount());
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

    /// setnotifaction image
    private void setlogogdatabase() {

        Map childmap = new HashMap();
        childmap.put("ReciverID", ReciverID);

        MNotifactionLogo.updateChildren(childmap)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {

                        } else {
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
    /// setnotifaction image


    private void removeNitiiconData() {
        MNotifactionLogo.child(SenderID).removeValue();
    }

}