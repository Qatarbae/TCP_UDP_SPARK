package cs.spark.Figure;

import com.google.gson.Gson;

public class DBRequest {
    private String className="";

    private String jsonObject="";
    public DBRequest(String className, String jsonObject) {
        this.className = className;
        this.jsonObject = jsonObject;
    }
    public DBRequest(){}
    public DBRequest(Figure ent, Gson gson){
        put(ent,gson);
    }
    public void put(Figure ent, Gson gson){
        className = ent.getClass().getName();
        System.out.println(className);
        jsonObject = gson.toJson(ent);
    }
    public Figure get(Gson gson) throws Exception{
        Class cc = Class.forName(className);
        if (cc==null)
            throw new Exception("Illegal class "+className);
        Figure ent = (Figure) gson.fromJson(jsonObject, cc);
        return ent;
    }
    public String toString(){
        try {
            return get(new Gson()).toString();
        } catch (Exception e) { return "????"; }
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(String jsonObject) {
        this.jsonObject = jsonObject;
    }
}
