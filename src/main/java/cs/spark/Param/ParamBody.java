package cs.spark.Param;

import com.google.gson.Gson;
import cs.spark.server.SparkServer;
import spark.Request;
import spark.Response;

import java.io.IOException;

public class ParamBody {
    private boolean valid=false;
    private Object  value=null;
    public Object getValue(){ return  value; }
    public boolean isValid(){ return valid; }
    public ParamBody(Request req, Response res, Class clazz) throws IOException {
        try{
            Gson gson = new Gson();
            value = gson.fromJson(req.body(),clazz);
            valid = value!=null;
        } catch(Exception ee){
            SparkServer.funCreateHTTPError(res, SparkServer.HTTPRequestError, "Ошибка формата класса: "+clazz.getSimpleName());
        }
    }
}
