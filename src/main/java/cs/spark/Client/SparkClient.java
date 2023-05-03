package cs.spark.Client;

import com.google.gson.Gson;
import cs.spark.Figure.DBRequest;
import cs.spark.Figure.Figure;
import cs.spark.Repo.RestApi;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class SparkClient {
    int PORT;
    private RestApi service = null;
    private Gson gson = new Gson();

    void createClient(){
        Retrofit retrofit=null;
        service=null;
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8083")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(RestApi.class);
    }
    public SparkClient(int PORT) throws Exception{
        this.PORT = PORT;
//        // Создаем HTTP-клиент
//        CloseableHttpClient httpclient = HttpClients.createDefault();
//
//        // Определяем URL для GET-запроса
//        HttpGet httpGet = new HttpGet("http://localhost:"+PORT+"/users/");
//
//        // Выполняем GET-запрос и получаем ответ
//        CloseableHttpResponse response = httpclient.execute(httpGet);
//
//        try {
//            // Получаем код ответа и тело ответа
//            int statusCode = response.getStatusLine().getStatusCode();
//            String responseBody = EntityUtils.toString(response.getEntity());
//
//            // Выводим результаты
//            System.out.println("Status code: " + statusCode);
//            System.out.println("Response body: " + responseBody);
//        } finally {
//            // Не забываем закрыть ответ и HTTP-клиент
//            response.close();
//            httpclient.close();
//        }

//        int num = 42; // число, которое необходимо отправить на сервер
//
//        HttpClient client = HttpClientBuilder.create().build();
//        HttpPost post = new HttpPost("http://localhost:"+PORT+"/calculate");
//
//        StringEntity input = new StringEntity(Integer.toString(num));
//        input.setContentType("application/json");
//        post.setEntity(input);
//
//        HttpResponse response = client.execute(post);
//        System.out.println(response.getStatusLine().getStatusCode());

    }
    public void addFigure(Figure figure) throws Exception{
        service.addObject("1234",new DBRequest(figure, gson)).execute();

        System.out.println(service.getObjectList("1234").execute().body());
    }
    public void delFigure(int id) throws  Exception{
        System.out.println(service.delObject("1234",id).execute());

    }

    public Figure getFigure(int id) throws Exception {
        Figure xx = (Figure) service.getObject("1234",id).execute().body().get(gson);
        System.out.println(xx);
        return xx;
    }
    public Vector<DBRequest> allFigure() throws Exception{
        return service.getObjectList("1234").execute().body();
    }
}
