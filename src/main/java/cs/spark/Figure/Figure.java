package cs.spark.Figure;

import com.thoughtworks.xstream.XStream;

import javax.swing.*;
import java.awt.*;
import java.io.*;


public abstract class Figure implements Serializable {
    public int x, y; // координаты фигуры
    public int width, height; // ширина и высота фигуры
    transient public Color color = new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));; // цвет фигуры
    transient public static JPanel canvas;
    transient public static Thread thr_text,thr_smile;
    public Figure() {}

    public Figure(Figure figure){
        x=figure.x;
        y=figure.y;
        width=figure.width;
        height=figure.height;
        color= figure.color;
    }
    public Figure(int x_, int y_, int width_, int height_) {
        x=x_;
        y=y_;
        width=width_;
        height=height_;
        //color=new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
    }
//    abstract public void run();
    abstract public void draw(Graphics g);
    public abstract boolean contains(int x, int y);

    public void Save(DataOutputStream F) throws IOException {
        F.writeInt(x);
        F.writeInt(y);
        F.writeInt(width);
        F.writeInt(height);
        F.writeInt(color.getRGB());
    }
    public void Load(DataInputStream F) throws IOException{
        x=F.readInt();
        y=F.readInt();
        width=F.readInt();
        height=F.readInt();
        color=new Color(F.readInt());
    }
    public void SaveText(BufferedWriter F) throws IOException{
        F.write(""+x); F.newLine();
        F.write(""+y); F.newLine();
        F.write(""+width);F.newLine();
        F.write(""+height);F.newLine();
        F.write(""+color.getRGB());F.newLine();
    }
    public void LoadText(BufferedReader F) throws IOException{
        String ss;
        ss=F.readLine();
        x=Integer.parseInt(ss);
        ss=F.readLine();
        y=Integer.parseInt(ss);
        ss=F.readLine();
        width=Integer.parseInt(ss);
        ss=F.readLine();
        height=Integer.parseInt(ss);
        ss=F.readLine();
        color = new Color(Integer.parseInt(ss));
    }
    public static void setXMLParser(XStream xstream){
        xstream.alias("Figure", Figure.class);
        xstream.useAttributeFor(Figure.class, "x");
        xstream.useAttributeFor(Figure.class, "y");
        xstream.useAttributeFor(Figure.class, "width");
        xstream.useAttributeFor(Figure.class, "height");
//        xstream.useAttributeFor(Figure.class, "color");
    }
}
