package cs.spark.Repo;

import cs.spark.Figure.DBRequest;
import cs.spark.Figure.Figure;
import cs.spark.Figure.JEmpty;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.ArrayList;
import java.util.Vector;

public interface RestApi {
    @GET("/vector")
    Call<Vector<DBRequest>> getObjectList(@Header("SessionToken") String token);
    @POST("/add")
    Call<JEmpty> addObject(@Header("SessionToken") String token, @Body DBRequest data);
    @GET("/get")
    Call<DBRequest> getObject(@Header("SessionToken") String token, @Query("id") int id);
    @POST("/delete")
    Call<JEmpty> delObject(@Header("SessionToken") String token, @Query("id") int id);
}
