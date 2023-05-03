package cs.spark.Figure;

import javax.swing.*;
import java.util.Vector;

public class VecSmiles implements Runnable {
    public Vector<PulsatingSmiley> smiles = new Vector<>();
    public JPanel canvas;
    public Thread thread;
    public boolean stop=false;
    public VecSmiles(){}
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
            for (PulsatingSmiley f : smiles) {
                f.pulse();
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            canvas.repaint();
        }
    }
}