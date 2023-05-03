package cs.spark.Figure;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class VecTexts implements Runnable {
    public Vector<RotatingText> texts = new Vector<>();
    public JPanel canvas;
    public Thread thread;
    public boolean stop=false;
    public VecTexts(){}
    @Override
    public void run() {
        while (true) {
            synchronized (thread) {
                if (stop == true) {
                    try {
                        thread.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            for (RotatingText f : texts) {
                f.rotate(0.1);

//                f.color = new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
            }
//            if(texts.size() != 0){
//
//                System.out.println(texts.get(0).getAngle());
//            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            canvas.repaint();
        }
    }
}
