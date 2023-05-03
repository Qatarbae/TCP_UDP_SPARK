package cs.spark.server;

import cs.spark.Figure.Figure;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

public class ServerSomthing extends Thread {
    private static Vector<Figure> figures = new Vector<>();

    private Socket socket; // сокет, через который сервер общается с клиентом,
    // кроме него - клиент и сервер никак не связаны

    private ObjectOutputStream output;
    ObjectOutputStream out;
    ObjectInputStream in;
    Server server;

    private static int count = 0;
    public static LinkedList<String> serverList = new LinkedList<String>(); // список всех нитей - экземпляров
    public static TreeSet<String> identificators = new TreeSet<>();
    

    public ServerSomthing(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        // если потоку ввода/вывода приведут к генерированию искдючения, оно проброситься дальше
        //output = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
        // сооюбщений новому поключению
        count++;
        start(); // вызываем run()
    }

    @Override
    public void run() {

        while (!socket.isClosed()) {
            try {
                String command = (String) in.readObject();
                //String command = "ADD";
                System.out.println(command);
                switch (command) {
                    case "CLOSE": // Закрытие соединения
                        socket.close();
                        break;
                    case "CLEAR": // Очистка вектора объектов
                        figures.clear();
                        break;
                    case "ADD": // передача объектов
                        Figure figure = (Figure) in.readObject();
                        System.out.println("Получили фигуру");
                        //System.out.println(figure);
                        figures.add(figure);
                        out.flush();
                        break;
                    case "ADD_D": // запрос на передачу объекта
                        out.writeObject(figures.size());
                        for (int i = 0; i < figures.size(); i++) {
                            out.writeObject(figures.get(i));
                        }
                        System.out.println(figures);
                        break;
                    case "NAMES": // передача списка имен
                        List<String> names = new ArrayList<>();
                        for (Figure figure1 : figures) {
                            String name = figure1.getClass().getSimpleName() + ","+ figure1.color;
                            names.add(name);
                        }
                        out.writeObject(names);
                        break;
                    case "SIZE": // запрос размера векторов.
                        out.writeObject(figures.size());
                        break;
                    case "GET": // запрос объекта с заданным номером вектора
                        String str = (String) in.readObject();
                        int index = Integer.parseInt(str);
                        if(index < figures.size() && index >= 0){
                            out.writeObject(figures.get(index));
                        }

                        break;
                    case "ALL": // Все объекты
                        figures = (Vector<Figure>) in.readObject();
                        System.out.println("Приняли все объекты");
                        break;
                    default:
                        break;
                }
            } catch (IOException | ClassNotFoundException ee) {
                try {
                    server.deleteSocket(this);
                    socket.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

        }

    }
}