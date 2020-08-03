package Offline;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class KarakBloodBankUpdate extends Application {


    @Override
    public void onCreate() {
        super.onCreate();



        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso build = builder.build();
        build.setIndicatorsEnabled(false);
        build.setLoggingEnabled(true);
        Picasso.setSingletonInstance(build);


    }




    public void DisplayOnlineStatas(String stats){

        ///date
        Calendar calendardate = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy");
        String CurrentDate = simpleDateFormat.format(calendardate.getTime());


        ///time
        Calendar calendartime = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm a");
        String CurrentTime = simpleDateFormat1.format(calendartime.getTime());

        Map statasmap = new HashMap();
        statasmap.put("time", CurrentTime);
        statasmap.put("date", CurrentDate);
        statasmap.put("statas", stats);


    }



}
