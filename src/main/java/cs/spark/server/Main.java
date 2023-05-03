

package cs.spark.server;
import cs.spark.server.DReceiver;
import cs.spark.server.Server;
import java.io.*;


public class Main {
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
        DReceiver dReceiver = new DReceiver(8081);
        dReceiver.start();
        SparkServer sparkServer = new SparkServer(8083);
        sparkServer.SparkStart();
    }
}


//    private void handleRequest(Socket socket, ObjectOutputStream out, ObjectInputStream in) {
//        while(true){
//            try {
//                String command = (String) in.readObject();
//                //String command = "ADD";
//                System.out.println(command);
//                switch (command) {
//                    case "CLOSE":
//                        socket.close();
//                        break;
//                    case "CLEAR":
//                        figures.clear();
//                        break;
//                    case "ADD":
//                        Figure figure = (Figure) in.readObject();
//                        System.out.println("Получили фигуру");
//                        //System.out.println(figure);
//                        figures.add(figure);
//                        out.flush();
//                        break;
//                    case "GET":
//                        int index = (int) in.readObject();
//                        out.writeObject(figures.get(index));
//                        break;
//                    case "SIZE":
//                        out.writeObject(figures.size());
//                        break;
//                    case "ADD_D":
//                        out.writeObject(figures.size());
//                        for (int i = 0; i < figures.size(); i++) {
//                            out.writeObject(figures.get(i));
//                        }
//                        break;
//                    case "NAMES":
//                        List<String> names = new ArrayList<>();
//                        for (Figure figure1 : figures) {
//                            String name = figure1.getClass().getSimpleName() + ",";
//                            names.add(name);
//                        }
//                        out.writeObject(names);
//                        break;
//                    default:
//                        break;
//                }
//                Thread.sleep(2000);
//            } catch (IOException | ClassNotFoundException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//
//            System.out.println("ENDDDD");
//        }
//
//    }
