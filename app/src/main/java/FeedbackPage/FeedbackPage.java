package FeedbackPage;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hsalf.smilerating.SmileRating;
import com.thomas.karakbloodbankupdate.R;

import GmailApi.Post;
import GmailApi.Postt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FeedbackPage extends Fragment {


    private EditText phonenumber, comments;
    private RelativeLayout submitbutton;
    private SmileRating rating;
    private String RatingText = "";
    private ProgressDialog Mprogress;

    private AdView adView;

    public FeedbackPage() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.feedback_page, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        Mprogress = new ProgressDialog(getActivity());

        adView = view.findViewById(R.id.mAdviewID);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        phonenumber = view.findViewById(R.id.MobileNumberID);
        comments = view.findViewById(R.id.CommentsID);
        submitbutton = view.findViewById(R.id.SubmitButtonID);
        rating = view.findViewById(R.id.smile_rating);


        rating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(int smiley, boolean reselected) {

                switch (smiley){
                    case SmileRating.TERRIBLE:
                        RatingText = "Terrible";
                        break;

                    case SmileRating.BAD:
                        RatingText = "Bad";
                        break;

                    case SmileRating.OKAY:
                        RatingText = "Okay";
                        break;

                    case SmileRating.GOOD:
                        RatingText = "Good";
                        break;

                    case SmileRating.GREAT:
                        RatingText = "Great";
                        break;
                }
            }
        });


        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startposting();
            }
        });


        return view;
    }


    private void startposting(){

        final String phone = phonenumber.getText().toString().trim();
        String message = comments.getText().toString();

        if(phone.isEmpty()){
            phonenumber.setError("Number require");
        }
        else if(message.isEmpty()){
            comments.setError("Comments require");
        }
        else {

            Mprogress.setMessage("Sending ...");
            Mprogress.setTitle("Please wait");
            Mprogress.setCanceledOnTouchOutside(false);
            Mprogress.show();

            String MESSAGE  = "Phone Number :"+phone+"\n"+"Message :"+message+"\n"+"Rating :"+RatingText+"\n";


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://hsmailapi.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            /////send email

            Post sendMailAPIClint = retrofit.create(Post.class);


            // password   Mangrovehotel2019
            //emailaddress tomastechinfo@gmail.com

            Postt postt = new Postt("souraveroy47@gmail.com", "009988776655", "sajibroy206@gmail.com", "FeedBack From KarakBloodbank", MESSAGE);

            Call<Post> call = sendMailAPIClint.createPost(postt);

            call.enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {

                    if(!response.isSuccessful()){
                        Mprogress.dismiss();
                        phonenumber.setText("");
                        comments.setText("");
                    }
                    else {
                        Mprogress.dismiss();
                        phonenumber.setText("");
                        comments.setText("");
                    }
                }

                @Override
                public void onFailure(Call<Post> call, Throwable t) {
                    Mprogress.dismiss();
                    phonenumber.setText("");
                    comments.setText("");
                }
            });

        }
    }
}