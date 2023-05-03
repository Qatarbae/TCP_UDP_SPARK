package cs.spark.Param;

import cs.spark.server.SparkServer;
import spark.Request;
import spark.Response;

import java.io.IOException;

public class ParamInt {
    private boolean valid=false;
    private int value=0;
    public int getValue(){ return  value; }
    public boolean isValid(){ return valid; }
    public ParamInt(Request req, Response res, String name) throws IOException {
        String ss = req.raw().getParameter(name);

        try{
            if (ss==null){
                SparkServer.funCreateHTTPError(res, SparkServer.HTTPRequestError, "Отсутствует параметр "+name);
                return;
            }
            value  = Integer.parseInt(ss);
            valid = true;
        } catch(Exception ee){
            SparkServer.funCreateHTTPError(res, SparkServer.HTTPRequestError, "Недопустимое значение параметра "+name+":"+ss);
        }
    }

}