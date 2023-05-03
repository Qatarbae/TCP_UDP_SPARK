package cs.spark.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server extends Thread {
    public static LinkedList<ServerSomthing> serverList = new LinkedList<>(); // список всех нитей - экземпляров
    // сервера, слушающих каждый своего клиента

    public Server(){

    }
    public void deleteSocket(ServerSomthing socket){
        System.out.println(socket);
        System.out.println(serverList.get(0));
        serverList.remove(socket);
        System.out.println("Удалили: " + serverList.size());
    }
    @Override
    public void run() {
        Socket socket;
        ServerSocket server = null;
        System.out.println("Server Started");
        try {
            server = new ServerSocket(1234);
            while (true) {
                // Блокируется до возникновения нового соединения:
                socket = server.accept();
                ServerSomthing serverSomthing = new ServerSomthing(socket, this);
                serverList.add(serverSomthing); // добавить новое соединенние в список
                System.out.println("size: " + serverList.size());

            }
        }
        catch (IOException e) {
            // Если завершится неудачей, закрывается сокет,
            // в противном случае, нить закроет его:
            try {
                server.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}