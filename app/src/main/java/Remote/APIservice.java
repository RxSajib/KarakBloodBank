package Remote;




import Model.Myresponce;
import Model.Sender;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by HASIB on 12/16/2017.
 */

public interface APIservice {
    @Headers(

            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAV6AAwFc:APA91bEDMbZnXqiOvry7BC_z8iRD-P6Gtx7fPpSPAiQW2KAHBPQbaf44A4TPX0qp9T18mVxD5oKO2m8SJa-BM7-WPBDC4QV5qY7h5EsWXZO2xTujEqQuwJ7w6-brLQGcEGqRZYa2Qd6S"
            }
    )
    @POST("fcm/send")
    Call<Myresponce> sendNotification(@Body Sender body);




    //Call<Myresponce>

}