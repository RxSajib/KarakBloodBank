package HomePage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.thomas.karakbloodbankupdate.R;

import Model.Token;


public class HomeContiner extends Fragment {



    public HomeContiner() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_continer, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);



        fcm_token();
        goto_mainpage(new MainHomePage());

        return view;
    }

    private void goto_mainpage(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.MainFreamContiner, fragment);
        transaction.commit();
    }


    private void fcm_token() {



        String refreshedToken = FirebaseInstanceId.getInstance().getToken();


        //  if (refreshedToken!="")



        Log.i("TAG", "fcm_token: "+refreshedToken);


        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference referance=db.getReference("Token");
        Token token=new Token(refreshedToken,true);
        referance.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);


    }
}