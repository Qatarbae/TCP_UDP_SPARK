package cs.spark.server;
import cs.spark.Figure.Figure;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
//public class DReceiver extends Thread{
//
//    public void connect() throws Exception {
//        DatagramSocket ds = new DatagramSocket(3000);
//
//
//    }
//
//    @Override
//    public void run() {
//        DatagramSocket ds = null;
//        try {
//            ds = new DatagramSocket(8081);
//            DatagramPacket dp;
//            System.out.println("Server UDP started");
//            while (true) {
//                byte[] buf = new byte[1024];
//                dp = new DatagramPacket(buf, 1024);
//                ds.receive(dp);
//                String str = new String(dp.getData(), 0, dp.getLength());
//                System.out.println(str);
//
//            }
//
//        }
//        catch (IOException e) {
//            ds.close();
//        }
//    }
//}

public class DReceiver extends Thread {
    private DatagramSocket socketin;
    private DatagramSocket socketout;
    private int port;
    private boolean running;
    private byte[] buffer = new byte[1024];
    private byte[] buffer1 = new byte[1024];
    private Vector<Figure> figures = new Vector<>();
    public DReceiver(int port) throws SocketException {
        socketin = new DatagramSocket(port);
        socketout = new DatagramSocket();
        this.port = port;
    }


    public void sendCommand(String command, DatagramPacket packet) {
        try {
            InetAddress s;
            InetAddress ip = InetAddress.getByName("127.0.0.1");
            ByteArrayInputStream in;
            ByteArrayOutputStream out;
            ObjectInputStream objectInput;
            ObjectOutputStream objectOutput;
            byte[] bytes = new byte[1024];
            byte[] data;
            switch (command) {
                case "CLOSE": // Закрытие соединения
                    socketin.close();
                    break;
                case "CLEAR": // Очистка вектора объектов
                    figures.clear();
                    break;
                case "ADD": // передача объектов
                    packet = new DatagramPacket(bytes, bytes.length);

                    socketin.receive(packet);
                    s = packet.getAddress();
                    data = packet.getData();
                    in = new ByteArrayInputStream(data);
                    objectInput = new ObjectInputStream(in);
                    Figure figure = (Figure) objectInput.readObject();
                    figures.add(figure);
                    System.out.println("Получили фигуру");
                    System.out.println(figures.size());
                    break;
                case "ADD_D": // Из сервера взять

                    out = new ByteArrayOutputStream();
                    objectOutput = new ObjectOutputStream(out);
                    objectOutput.writeObject(figures);
                    data = out.toByteArray();
                    packet = new DatagramPacket(data, data.length, ip, port+1);
                    socketout.send(packet);
                    break;
                case "SIZE": // запрос размера векторов.
                    out = new ByteArrayOutputStream();
                    objectOutput = new ObjectOutputStream(out);
                    objectOutput.writeObject(figures.size());
                    data = out.toByteArray();
                    packet = new DatagramPacket(data, data.length, ip, port+1);
                    socketout.send(packet);
                    break;
                case "NAMES": // передача списка имен
                    List<String> names = new ArrayList<>();
                    for (Figure figure1 : figures) {
                        String name = figure1.getClass().getSimpleName() + ","+ figure1.color;
                        names.add(name);
                    }
                    out = new ByteArrayOutputStream();
                    objectOutput = new ObjectOutputStream(out);
                    objectOutput.writeObject(names);
                    data = out.toByteArray();
                    packet = new DatagramPacket(data, data.length, ip, port+1);
                    socketout.send(packet);
                    break;
                case "GET": // запрос объекта с заданным номером вектора
                    packet = new DatagramPacket(bytes, bytes.length);
                    socketin.receive(packet);
                    data = packet.getData();
                    in = new ByteArrayInputStream(data);
                    objectInput = new ObjectInputStream(in);
                    String str = (String) objectInput.readObject();
                    int index = Integer.parseInt(str);
                    if(index < figures.size() && index >= 0){
                        out = new ByteArrayOutputStream();
                        objectOutput = new ObjectOutputStream(out);
                        objectOutput.writeObject(figures.get(index));
                        data = out.toByteArray();
                        packet = new DatagramPacket(data, data.length, ip, port+1);
                        socketout.send(packet);
                    }
                    break;
                case "?": // ответ на запрос размера вектора
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {

        running = true;
        System.out.println("UDP Server listening on port " + socketin.getLocalPort() + "...");
        while (running) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                socketin.receive(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
//            DatagramPacket packet1 = new DatagramPacket(buffer1, buffer1.length);
//            socket.receive(packet1);
            byte[] data = packet.getData();
            String request = new String(data, 0, packet.getLength());
//            byte[] data1 = packet.getData();
//            String request1 = new String(data1, 0, packet1.getLength());
            System.out.println(request);
//            System.out.println(request1);
            sendCommand(request, packet);
        }
    }
}