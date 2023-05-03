package cs.spark.server;

import com.google.gson.Gson;
import cs.spark.Figure.DBRequest;
import cs.spark.Figure.Figure;
import cs.spark.Figure.JEmpty;
import cs.spark.Param.ParamBody;
import cs.spark.Param.ParamInt;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.util.ArrayList;
import java.util.Vector;

import static spark.Spark.*;
import static spark.Spark.delete;

public class SparkServer {

    int PORT;

    public final static int HTTPRequestError = 400;
    private Vector<Figure> figures = new Vector<>();
    public SparkServer(int PORT) {
        this.PORT = PORT;


    }
    public void SparkStart(){
        spark.Spark.port(PORT);
//        // Определяем маршрут для GET-запроса на /hello
//        spark.Spark.get("/hello", (req, res) -> "<h1>Hello World</h1>");
//
//        // Определяем маршрут для POST-запроса на /hello
//        spark.Spark.post("/hello", (req, res) -> "Hello " + req.body());
//
//        // Определяем маршрут для GET-запроса на /users/:name
//        spark.Spark.get("/users/:name", (req, res) -> "Hello " + req.params(":name"));
//
//        // Определяем маршрут для PUT-запроса на /users/:name
//        spark.Spark.put("/users/:name", (req, res) -> {
//            // Обработка PUT-запроса на /users/:name
//            return "User " + req.params(":name") + " updated!";
//        });
//
//        // Определяем маршрут для DELETE-запроса на /users/:name
//        spark.Spark.delete("/users/:name", (req, res) -> {
//            // Обработка DELETE-запроса на /users/:name
//            return "User " + req.params(":name") + " deleted!";
//        });
        Spark.get("/vector",routeObjectList);
        Spark.post("/add", routeObjectAdd);
        Spark.get("/get", routeObjectGet);
        Spark.post("/delete", routeObjectDel);
    }

    Route routeObjectList = new Route() {
        @Override
        public Object handle(Request req, Response res) throws Exception {
            Vector<DBRequest> out = new Vector<>();
            Gson gson = new Gson();
            for(Figure oo : figures){
                out.add(new DBRequest(oo,gson));
            }
            return new Gson().toJson(out);
        }
    };

    Route routeObjectGet = new Route() {
        @Override
        public Object handle(Request req, Response res) throws Exception {
            ParamInt qq = new ParamInt(req, res, "id");
            if (!qq.isValid()) return null;
            int id = qq.getValue();
            if (id>=figures.size() || id<0){
                SparkServer.funCreateHTTPError(res,SparkServer.HTTPRequestError, "Недопустимое значение параметра "+id);
                return null;
            }
            Gson gson = new Gson();
            return new Gson().toJson(new DBRequest(figures.get(id),gson));
        }
    };
    Route routeObjectAdd = new Route() {
        @Override
        public Object handle(Request req, Response res) throws Exception {
            ParamBody qq = new ParamBody(req, res, DBRequest.class);
            if (!qq.isValid()) return null;
            DBRequest rr = (DBRequest) qq.getValue();
            Figure oo = rr.get(new Gson());
            figures.add(oo);
            return new Gson().toJson(new JEmpty());
        }
    };

    Route routeObjectDel = new Route() {
        @Override
        public Object handle(Request req, Response res) throws Exception {
            ParamInt qq = new ParamInt(req, res, "id");
            if (!qq.isValid()) return null;
            int id = qq.getValue();
            if (id>=figures.size() || id<0){
                SparkServer.funCreateHTTPError(res,SparkServer.HTTPRequestError, "Недопустимое значение параметра "+id);
                return null;
            }
            figures.remove(id);
            return new Gson().toJson(new JEmpty());
        }
    };

    public static void  funCreateHTTPError(Response res, int code, String mes){
        res.status(code);
        res.raw().setCharacterEncoding("utf-8");
        res.body(mes);
        System.out.println("HTTP: "+code+" "+mes);
    }

    public void createHTTPError(Response res, int code, String mes){
        funCreateHTTPError(res,code,mes);
    }
}
