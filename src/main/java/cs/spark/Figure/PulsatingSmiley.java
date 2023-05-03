package cs.spark.Figure;

import com.thoughtworks.xstream.XStream;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.*;

public class PulsatingSmiley extends Figure {
     private double phase = 0.0; // фаза пульсации
     private double amplitude = 10.0; // амплитуда пульсации
     private double frequency = 0.1; // частота пульсации
//    private int size;
transient public static boolean stop=false;

    public PulsatingSmiley(PulsatingSmiley figure) {
        super(figure);
    }

    public PulsatingSmiley(int x, int y, int size) {
        super(x, y, size,size);
//        this.size = size;
    }
    public PulsatingSmiley() {
        super();
    }

    public void draw(Graphics g) {
        int pulsatingSize = (int)(width + amplitude * Math.sin(phase)); // вычислить размер с учетом пульсации
        g.setColor(color);

        g.fillOval(x, y, pulsatingSize, pulsatingSize);
        g.setColor(Color.BLACK);
        g.drawOval(x, y, pulsatingSize, pulsatingSize);
//        if(!stop)
//        pulse();

        drawEyes(g, pulsatingSize);
        drawMouth(g, pulsatingSize);
    }
    private void drawEyes(Graphics g, int size) {
        int eyeSize = size / 5;
        int leftEyeX = x + size / 3 - eyeSize / 2;
        int rightEyeX = x + 2 * size / 3 - eyeSize / 2;
        int eyeY = y + size / 3;
        g.fillOval(leftEyeX, eyeY, eyeSize, eyeSize);
        g.fillOval(rightEyeX, eyeY, eyeSize, eyeSize);
    }
    private void drawMouth(Graphics g, int size) {
        int mouthSize = size / 2;
        int mouthX = x + size / 4;
        int mouthY = y + 3 * size / 4 - mouthSize / 2;
        int mouthAngle = (int) (30 * Math.sin(phase)); // угол поворота рта
        g.fillArc(mouthX, mouthY, mouthSize, mouthSize, mouthAngle, 180 - 2 * mouthAngle);
    }

    public void pulse() {
        phase += frequency; // увеличить фазу на частоту
    }
    public boolean contains(int x, int y) {
        Ellipse2D.Double circle = new Ellipse2D.Double(this.x, this.y, width, height);
        return circle.contains(x, y);
    }

    @Override
    public void Save(DataOutputStream F) throws IOException {
        super.Save(F);
//        F.writeDouble(phase);
    }

    @Override
    public void Load(DataInputStream F) throws IOException {
        super.Load(F);
    }

    @Override
    public void SaveText(BufferedWriter F) throws IOException {
        super.SaveText(F);
    }

    @Override
    public void LoadText(BufferedReader F) throws IOException {
        super.LoadText(F);
    }

    public static void setXMLParser(XStream xstream){
        xstream.alias("PulsatingSmiley", PulsatingSmiley.class);
//        xstream.useAttributeFor(PulsatingSmiley.class, "angle");
//        xstream.useAttributeFor(PulsatingSmiley.class, "text");
//        xstream.useAttributeFor(PulsatingSmiley.class, "font");
    }
//    public void run() {
//        while (true) {
//            synchronized (thr_smile){
//                if (stop==true){
//                    try {
//                        thr_smile.wait();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                canvas.repaint();
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
////                System.out.println("smile\n);
//            }
//        }
//    }
}
