package cs.spark.Figure;

import com.thoughtworks.xstream.XStream;

import java.awt.*;
import java.io.*;
public class RotatingText extends Figure {
    public String text; // текст строки
    transient private Font font = new Font("Arial", Font.BOLD, (int) (Math.random() * 20 + 10)); // шрифт строки


    private double angle = 0.0; // угол поворота
    public static boolean stop=false;
    transient Graphics2D g2d;

    public RotatingText(RotatingText figure) {
        super(figure);
        this.text = figure.text;
        this.font = figure.font;
        this.angle = figure.angle;
    }


    public RotatingText(int x, int y) {
        super(x, y, 0, 0);
        text=generateRandomText();
        font = new Font("Arial", Font.BOLD, (int) (Math.random() * 20 + 10));
    }
    public RotatingText(){super();}

    public void draw(Graphics g) {
        g2d = (Graphics2D)g.create();
//        if (stop==false) {
            g2d.rotate(angle, x + width / 2, y - height / 4); // повернуть текст на заданный угол

//        }
        g2d.setColor(color);
        g2d.setFont(font);
        g2d.drawString(text, x, y);
        width = g2d.getFontMetrics().stringWidth(text);
        height = g2d.getFontMetrics().getHeight();
        g2d.dispose();
    }

    public void rotate(double delta) {
        angle += delta; // увеличить угол на заданный приращение
        angle %= Math.PI * 2; // ограничить угол в пределах от 0 до 2π
    }

    private String generateRandomText() {
        StringBuilder sb = new StringBuilder();
        int length = (int) (Math.random() * 10 + 1);
        for (int i = 0; i < length; i++) {
            char c = (char) (Math.random() * 26 + 'a');
            sb.append(c);
        }
        return sb.toString();
    }
    public boolean contains(int x, int y) {//засунь сюда угол
        System.out.println("del");
        return x >= this.x && x <= this.x &&
                y <= this.y && y >= this.y - height;
    }


    @Override
    public void Save(DataOutputStream F) throws IOException {
        super.Save(F);
        F.writeDouble(angle);
        F.writeUTF(text);
        F.writeInt(font.getSize());
    }

    @Override
    public void Load(DataInputStream F) throws IOException {
        super.Load(F);
        angle=F.readDouble();
        text = F.readUTF();
        font = new Font("Arial", Font.BOLD, F.readInt());
    }

    @Override
    public void SaveText(BufferedWriter F) throws IOException {
        super.SaveText(F);
        F.write(""+angle);
        F.newLine();
        F.write(text);
        F.newLine();
        F.write(""+font.getSize());
        F.newLine();
    }

    @Override
    public void LoadText(BufferedReader F) throws IOException {
        super.LoadText(F);
        angle=Double.parseDouble(F.readLine());
        text = F.readLine();
        font = new Font("Arial", Font.BOLD, Integer.parseInt(F.readLine()));
    }
    public static void setXMLParser(XStream xstream){
        xstream.alias("RotatingText", RotatingText.class);
        xstream.useAttributeFor(RotatingText.class, "angle");
        xstream.useAttributeFor(RotatingText.class, "text");
//        xstream.useAttributeFor(RotatingText.class, "font");
    }
    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
//    public void run() {
//        while (true) {
//            synchronized (thr_text){
//                if (stop==true){
//                    try {
//                        thr_text.wait();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                canvas.repaint();
//
//                                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
////                System.out.println("text\n");
//            }
//        }
//    }
}