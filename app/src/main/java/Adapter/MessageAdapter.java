package Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.thomas.karakbloodbankupdate.R;

import java.util.ArrayList;
import java.util.List;

import AllPogoClass.MessageHolder;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<MessageHolder> messageHolderList = new ArrayList<>();
    private Context context;
    private FirebaseAuth Mauth;
    private String CurrentUserID;
    private DatabaseReference MmessageDatabase;
    private Animation animation;

    public MessageAdapter(List<MessageHolder> messageHolderList) {
        this.messageHolderList = messageHolderList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_design, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, int position) {

        Mauth = FirebaseAuth.getInstance();
        CurrentUserID = Mauth.getCurrentUser().getUid();
        MmessageDatabase = FirebaseDatabase.getInstance().getReference().child("Message");
        MmessageDatabase.keepSynced(true);

        final  MessageHolder usermessagelist = messageHolderList.get(position);
        String MessageSenderID = usermessagelist.getFrom();
        String fromtype = usermessagelist.getType();

        animation = AnimationUtils.loadAnimation(holder.context, R.anim.from_button);

        MmessageDatabase.child(MessageSenderID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    /// todo image set
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.senderlayout.setVisibility(View.GONE);
        holder.sendermessagetext.setVisibility(View.GONE);
        holder.sendermessagetime.setVisibility(View.GONE);

        holder.recivermessagetime.setVisibility(View.GONE);
        holder.reciverlayout.setVisibility(View.GONE);
        holder.recivermessagetext.setVisibility(View.GONE);

        holder.senderimage.setVisibility(View.GONE);
        holder.senderimageLayout.setVisibility(View.GONE);
        holder.senderimageLayout.setVisibility(View.GONE);

        holder.reciverimagetime.setVisibility(View.GONE);
        holder.reciverimage.setVisibility(View.GONE);
        holder.reciverimagelayout.setVisibility(View.GONE);

        holder.sender_doc_button.setVisibility(View.GONE);
        holder.sender_doc_time.setVisibility(View.GONE);
        holder.snnderdoc_layout.setVisibility(View.GONE);
        holder.sender_doc_image.setVisibility(View.GONE);

        holder.reciver_doc_time.setVisibility(View.GONE);
        holder.reciverdoc_layout.setVisibility(View.GONE);
        holder.reciverdoc_image.setVisibility(View.GONE);
        holder.reciverdoc_button.setVisibility(View.GONE);

        if(fromtype.equals("text")){


            holder.snnderdoc_layout.setVisibility(View.GONE);
            holder.reciverdoc_layout.setVisibility(View.GONE);

            if(MessageSenderID.equals(CurrentUserID)){

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        CharSequence charSequence[]  = new CharSequence[]{
                                "Edit Message"
                        };

                        AlertDialog.Builder Mbuilder  = new AlertDialog.Builder(holder.context);
                        Mbuilder.setItems(charSequence, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 0){
                                    final DatabaseReference message = FirebaseDatabase.getInstance().getReference().child("Edit_message");
                                    message.child("message").setValue(usermessagelist.getMessage())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        message.removeValue();
                                                    }
                                                }
                                            });
                                }
                            }
                        });

                        AlertDialog alertDialog = Mbuilder.create();
                        alertDialog.show();

                        return true;
                    }
                });

                holder.sendermessagetime.setVisibility(View.VISIBLE);
                holder.sendermessagetext.setVisibility(View.VISIBLE);
                holder.senderlayout.setVisibility(View.VISIBLE);
                holder.sendermessagetext.setText(usermessagelist.getMessage());
                holder.sendermessagetime.setText(usermessagelist.getTime());
                holder.senderlayout.setVisibility(View.VISIBLE);

                holder.reciverlayout.setVisibility(View.GONE);
                holder.recivermessagetime.setVisibility(View.GONE);
                holder.recivermessagetext.setVisibility(View.GONE);

            }
            else {
                holder.recivermessagetext.setVisibility(View.VISIBLE);
                holder.reciverlayout.setVisibility(View.VISIBLE);
                holder.recivermessagetime.setVisibility(View.VISIBLE);
                holder.recivermessagetime.setText(usermessagelist.getTime());
                holder.reciverlayout.setVisibility(View.VISIBLE);
                holder.recivermessagetext.setText(usermessagelist.getMessage());

                holder.senderlayout.setVisibility(View.GONE);
                holder.sendermessagetime.setVisibility(View.GONE);
                holder.sendermessagetext.setVisibility(View.GONE);

            }
        }

        if(fromtype.equals("image")){
            holder.snnderdoc_layout.setVisibility(View.GONE);
            holder.reciverdoc_layout.setVisibility(View.GONE);
            if (MessageSenderID.equals(CurrentUserID)) {

                holder.senderimage.setVisibility(View.VISIBLE);
                holder.senderimageLayout.setVisibility(View.VISIBLE);
                holder.senderimagetime.setVisibility(View.VISIBLE);

                Picasso.with(holder.context).load(usermessagelist.getMessage()).into(holder.senderimage);
                Picasso.with(holder.context).load(usermessagelist.getMessage()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.senderimage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
                holder.senderimagetime.setText(usermessagelist.getTime());
                holder.senderimageLayout.setVisibility(View.VISIBLE);


                holder.senderimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder Mbuilder = new AlertDialog.Builder(holder.context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

                        View imageview = LayoutInflater.from(holder.context).inflate(R.layout.full_screen_image, null, false);
                        ImageView messageimage = imageview.findViewById(R.id.FullScreenImageID);

                        Mbuilder.setView(imageview);

                        Picasso.with(holder.context).load(usermessagelist.getMessage()).into(messageimage);
                        Picasso.with(holder.context).load(usermessagelist.getMessage()).networkPolicy(NetworkPolicy.OFFLINE).into(messageimage, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                            }
                        });

                        AlertDialog alertDialog = Mbuilder.create();
                        alertDialog.show();
                    }


                });

            }
            else {
                holder.reciverimagelayout.setVisibility(View.VISIBLE);
                holder.reciverimagetime.setVisibility(View.VISIBLE);
                holder.reciverimage.setVisibility(View.VISIBLE);

                Picasso.with(holder.context).load(usermessagelist.getMessage()).into(holder.reciverimage);

                Picasso.with(holder.context).load(usermessagelist.getMessage()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.reciverimage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });

                holder.reciverimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder Mbuilder  = new AlertDialog.Builder(holder.context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

                        View reciverlayout = LayoutInflater.from(holder.context).inflate(R.layout.full_screen_image, null, false);
                        ImageView imageView = reciverlayout.findViewById(R.id.FullScreenImageID);

                        Picasso.with(holder.context).load(usermessagelist.getMessage()).into(imageView);

                        Picasso.with(holder.context).load(usermessagelist.getMessage()).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                            }
                        });

                        Mbuilder.setView(reciverlayout);
                        AlertDialog alertDialog = Mbuilder.create();
                        alertDialog.show();
                    }
                });
            }
        }
        if(fromtype.equals("pdf") || fromtype.equals("doc")) {
            if (MessageSenderID.equals(CurrentUserID)) {

                holder.sender_doc_button.setVisibility(View.VISIBLE);
                holder.sender_doc_time.setVisibility(View.VISIBLE);
                holder.snnderdoc_layout.setVisibility(View.VISIBLE);
                holder.sender_doc_image.setVisibility(View.VISIBLE);
                holder.sender_doc_time.setText(usermessagelist.getTime());


                holder.sender_doc_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(usermessagelist.getMessage()));
                        holder.itemView.getContext().startActivity(intent);
                    }
                });
            }
            else {
                holder.reciver_doc_time.setVisibility(View.VISIBLE);
                holder.reciverdoc_layout.setVisibility(View.VISIBLE);
                holder.reciverdoc_image.setVisibility(View.VISIBLE);
                holder.reciverdoc_button.setVisibility(View.VISIBLE);
                holder.reciver_doc_time.setText(usermessagelist.getTime());

                holder.reciverdoc_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(usermessagelist.getMessage()));
                        holder.itemView.getContext().startActivity(intent);
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return messageHolderList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        private Context context;

        ///sender iteams
        private TextView sendermessagetext;
        private TextView sendermessagetime;
        private RelativeLayout senderlayout;
        ///sender iteams

        ///reciver iteams
        private TextView recivermessagetext;
        private TextView recivermessagetime;
        private RelativeLayout reciverlayout;
        ///reciver iteams

        ///sender image
        private ImageView senderimage;
        private TextView senderimagetime;
        private RelativeLayout senderimageLayout;
        ///sender image

        ///reciver image
        private ImageView reciverimage;
        private TextView reciverimagetime;
        private RelativeLayout reciverimagelayout;
        ///reciver image


        ///Sender document
        private RelativeLayout snnderdoc_layout;
        private ImageView sender_doc_image;
        private TextView sender_doc_time;
        private Button sender_doc_button;
        ///Sender document

        ///Reciver document
        private RelativeLayout reciverdoc_layout;
        private ImageView reciverdoc_image;
        private Button reciverdoc_button;
        private TextView reciver_doc_time;
        ///Reciver document

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            /// sender Message Text
            context = itemView.getContext();
            sendermessagetext = itemView.findViewById(R.id.SenderMessageID);
            sendermessagetime = itemView.findViewById(R.id.SenderMessageTimeID);
            senderlayout = itemView.findViewById(R.id.SenderLayoutID);
            /// sender Message Text

            ///Reciver MessageText
            reciverlayout = itemView.findViewById(R.id.ReciverLayoutID);
            recivermessagetext = itemView.findViewById(R.id.ReciverMessageID);
            recivermessagetime = itemView.findViewById(R.id.RReciverMessageTime);
            ///Reciver MessageText


            ///sender image
            senderimage = itemView.findViewById(R.id.SenderImage);
            senderimagetime = itemView.findViewById(R.id.SenderImageTime);
            senderimageLayout = itemView.findViewById(R.id.SenderImageLayout);
            ///sender image

            ///reciver image
            reciverimage = itemView.findViewById(R.id.ReciverImage);
            reciverimagelayout = itemView.findViewById(R.id.ReciverImageLayout);
            reciverimagetime = itemView.findViewById(R.id.ReciverImageTime);
            ///reciver image

            ///sender document
            sender_doc_button = itemView.findViewById(R.id.SSenderDocuntButtonID);
            sender_doc_image = itemView.findViewById(R.id.SSenderDocImage);
            sender_doc_time = itemView.findViewById(R.id.SSenderDocumentTimeID);
            snnderdoc_layout = itemView.findViewById(R.id.SSenderDocuentLayoutID);
            ///sender document

            ///reciver document
            reciver_doc_time = itemView.findViewById(R.id.ReciverDocumentTimeID);
            reciverdoc_button = itemView.findViewById(R.id.ReciverDocuntButtonID);
            reciverdoc_image = itemView.findViewById(R.id.ReciverdocImage);
            reciverdoc_layout = itemView.findViewById(R.id.ReciverDocuentLayoutID);
            ///reciver document
        }
    }
}
