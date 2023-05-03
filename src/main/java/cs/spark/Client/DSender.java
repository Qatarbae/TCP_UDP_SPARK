package cs.spark.Client;

import cs.spark.Figure.Figure;

import java.io.*;
import java.net.*;
import java.util.*;

public class DSender{
    private DatagramSocket socketin;
    private DatagramSocket socketout;
    private Vector<Figure> figures = new Vector<Figure>();
    private int port = 8081;
    private InetAddress ip;
    Figure figure;
    private int size;
    private List<String> names;
    private byte[] buffer = new byte[10000];
    private String textField;
    public boolean isAnswer() {
        return answer;
    }

    boolean answer=true;

    DSender() throws Exception {
        socketin = new DatagramSocket();
        socketout = new DatagramSocket(port+1);
        ip = InetAddress.getByName("127.0.0.1");

    }

    public Figure setFigure(Figure figure) throws Exception {
        this.figure = figure;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutput = new ObjectOutputStream(byteStream);
        objectOutput.writeObject(figure);
        byte[] byteArray = byteStream.toByteArray();

        DatagramSocket ds = new DatagramSocket();
        String str = "Welcome java";

        InetAddress ip = InetAddress.getByName("127.0.0.1");
        DatagramPacket dp = new DatagramPacket(byteArray, byteArray.length, ip, 8081);
        ds.send(dp);
        Thread.sleep(2000);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        ds.receive(packet);
        byte[] data = packet.getData();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream objectInput = new ObjectInputStream(in);
        Figure figure1 = (Figure) objectInput.readObject();
        ds.close();
        return  figure1;
    }

    public void sendCommand(String command, Figure figure) {
        try {
            ByteArrayInputStream in;
            ByteArrayOutputStream out;
            ObjectInputStream objectInput;
            ObjectOutputStream objectOutput;
            byte[] bytes = command.getBytes();
            byte[] data;
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, ip, 8081);
            socketin.send(packet);
            switch (command) {
                case "CLOSE": // Закрытие соединения

                    break;
                case "CLEAR": // Очистка вектора объектов

                    break;
                case "ADD": // передача объектов
                    out = new ByteArrayOutputStream();
                    objectOutput = new ObjectOutputStream(out);
                    objectOutput.writeObject(figure);
                    bytes = out.toByteArray();
                    packet = new DatagramPacket(bytes, bytes.length, ip, port);
                    socketin.send(packet);
                    System.out.println("Отправили объект");
                    break;
                case "ADD_D": // Из сервера взять
                    packet = new DatagramPacket(buffer, buffer.length);
                    socketout.receive(packet);
                    data = packet.getData();
                    in = new ByteArrayInputStream(data);
                    objectInput = new ObjectInputStream(in);
                    figures = (Vector<Figure>) objectInput.readObject();

                    //setFigures(figure1);
                    break;
                case "SIZE": // запрос размера векторов.
                    packet = new DatagramPacket(buffer, buffer.length);
                    socketout.receive(packet);
                    data = packet.getData();
                    in = new ByteArrayInputStream(data);
                    objectInput = new ObjectInputStream(in);
                    size = (Integer) objectInput.readObject();
                    break;
                case "NAMES": // передача списка имен
                    packet = new DatagramPacket(buffer, buffer.length);
                    socketout.receive(packet);
                    data = packet.getData();
                    in = new ByteArrayInputStream(data);
                    objectInput = new ObjectInputStream(in);
                    names = (List<String>)objectInput.readObject();
                    break;
                case "GET": // запрос объекта с заданным номером вектора
                    out = new ByteArrayOutputStream();
                    objectOutput = new ObjectOutputStream(out);
                    objectOutput.writeObject(textField);
                    bytes = out.toByteArray();
                    packet = new DatagramPacket(bytes, bytes.length, ip, port);
                    socketin.send(packet);
                    //взять с сервера
                    packet = new DatagramPacket(buffer, buffer.length);
                    socketout.receive(packet);
                    data = packet.getData();
                    in = new ByteArrayInputStream(data);
                    objectInput = new ObjectInputStream(in);
                    this.figure = (Figure) objectInput.readObject();

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
    public void TextF(String textField){
        this.textField = textField;
    }
    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Figure getFigure() {
        return figure;
    }

    public Vector<Figure> getFigures() {
        return figures;
    }

    public void setFigures(Vector<Figure> figures) {
        this.figures = figures;
    }
    //    @Override
//    public void run(){
//        DatagramSocket ds = null;
//        try {
//            ds = new DatagramSocket(8082);
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
}
