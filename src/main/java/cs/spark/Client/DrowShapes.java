package cs.spark.Client;


import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import cs.spark.Figure.*;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.List;
import java.util.Vector;


public class DrowShapes extends JFrame{
    private Vector<Figure> figures = new Vector<>();
    JRadioButton rb_select_text=new JRadioButton("Строка",true);
    JRadioButton rb_select_smiley=new JRadioButton("Смайл");
    JRadioButton connect=new JRadioButton("Подключение");
    JRadioButton disconnect=new JRadioButton("Отключение", true);

    JMenuBar jMenuBar = new JMenuBar();
    JMenu jMenu = new JMenu("lab1");
    JMenuItem btn_stop_text = new JMenuItem();
    JMenuItem btn_notify_text = new JMenuItem();
    JMenuItem btn_stop_smile = new JMenuItem();
    JMenuItem btn_notify_smile = new JMenuItem();
    JMenuItem btn_load_bin = new JMenuItem();
    JMenuItem btn_save_bin = new JMenuItem();
    JMenuItem btn_load_text = new JMenuItem();
    JMenuItem btn_save_text = new JMenuItem();
    JMenuItem btn_load_serial = new JMenuItem();
    JMenuItem btn_save_serial = new JMenuItem();
    JMenuItem btn_load_xml = new JMenuItem();
    JMenuItem btn_save_xml = new JMenuItem();
    JMenuItem btn_load_json = new JMenuItem();
    JMenuItem btn_save_json = new JMenuItem();
    NumericTextField FieldIndex_tcp = new NumericTextField();
    NumericTextField FieldIndex_udp = new NumericTextField();

    JPanel tcp_panel = new JPanel();
    JPanel udp_panel = new JPanel();
    JPanel api_panel = new JPanel();

    JTabbedPane tab = new JTabbedPane();

    JButton TCP = new JButton("TCP");
    JButton tcp_all_figures = new JButton("Запросить фигуры");
    JButton btn_clear_tcp = new JButton("Clearserv");
    JButton btn_names_tcp = new JButton("Names");
    JButton btn_size_tcp = new JButton("размер вектора");
    JButton btn_clear_udp = new JButton("Clearserv");
    JButton btn_names_udp = new JButton("Names");
    JButton btn_size_udp = new JButton("размер вектора");
    //TODO lab-3
    JButton btn_udp = new JButton("все объекты udp");

    JButton btn_names_api = new JButton("Names");
    NumericTextField field_api = new NumericTextField();
    JButton btn_del_api = new JButton("delete");
    JButton btn_get_api = new JButton("get");
    JButton btn_all_api = new JButton("add All Figures");

    UpPanel up = new UpPanel();
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    ButtonGroup  group = new ButtonGroup ();
    ButtonGroup  groupServ = new ButtonGroup ();
    VecSmiles vec_smiles=new VecSmiles();
    VecTexts vec_texts=new VecTexts();
    Thread tr_text=new Thread(vec_texts);
    final Thread tr_smile=new Thread(vec_smiles);

    public JPanel canvas = new JPanel() {
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            for (Figure f : figures) {
                f.draw(g);
            }
        }
    };

    public void window(){
        group.add(rb_select_text);
        group.add(rb_select_smiley);
        groupServ.add(connect);
        groupServ.add(disconnect);
        btn_stop_text.setText("Остановить строки");
        jMenu.add(btn_stop_text);
        btn_notify_text.setText("Запустить строки");
        jMenu.add(btn_notify_text);
        btn_stop_smile.setText("остановить Смайлики");
        jMenu.add(btn_stop_smile);
        btn_notify_smile.setText("Запустить смайлики");
        jMenu.add(btn_notify_smile);
        btn_load_bin.setText("загрузить bin");
        jMenu.add(btn_load_bin);
        btn_save_bin.setText("сохранить bin");
        jMenu.add(btn_save_bin);
        btn_load_text.setText("загрузить тект");
        jMenu.add(btn_load_text);
        btn_save_text.setText("сохранить текст");
        jMenu.add(btn_save_text);
        btn_load_serial.setText("загрузить serial");
        jMenu.add(btn_load_serial);
        btn_save_serial.setText("сохранить serial");
        jMenu.add(btn_save_serial);
        btn_load_xml.setText("загрузить xml");
        jMenu.add(btn_load_xml);
        btn_save_xml.setText("сохранить xml");
        jMenu.add(btn_save_xml);
        btn_load_json.setText("загрузить json");
        jMenu.add(btn_load_json);
        btn_save_json.setText("сохранить json");
        jMenu.add(btn_save_json);

        //canvas.add(jMenu);
        canvas.add(tab);
        tab.add("TCP",tcp_panel);
        tab.add("UDP",udp_panel);
        tab.add("RestAPI", api_panel);
        canvas.add(rb_select_text);
        canvas.add(rb_select_smiley);

//        canvas.add(connect);
//        canvas.add(disconnect);
//        canvas.add(TCP);
//        canvas.add(tcp_all_figures);
//        canvas.add(btn_clear_tcp);
//        canvas.add(btn_names_tcp);
//        canvas.add(btn_size_tcp);
//        FieldIndex_tcp.setToolTipText("Введите число");
//        canvas.add(FieldIndex_tcp);

        tcp_panel.add(connect);
        tcp_panel.add(disconnect);
        tcp_panel.add(TCP);
        tcp_panel.add(tcp_all_figures);
        tcp_panel.add(btn_clear_tcp);
        tcp_panel.add(btn_names_tcp);
        tcp_panel.add(btn_size_tcp);
        FieldIndex_tcp.setToolTipText("Введите число");
        tcp_panel.add(FieldIndex_tcp);


        udp_panel.add(btn_clear_udp);
        udp_panel.add(btn_names_udp);
        udp_panel.add(btn_size_udp);
        FieldIndex_udp.setToolTipText("Введите число");
        udp_panel.add(FieldIndex_udp);
        udp_panel.add(btn_udp);

        api_panel.add(btn_all_api);
        api_panel.add(btn_names_api);
        api_panel.add(field_api);
        api_panel.add(btn_del_api);
        api_panel.add(btn_get_api);
        //canvas.add(label, BorderLayout.SOUTH);
//        canvas.add(tcp_panel);
        add(up, BorderLayout.SOUTH);
        btn_notify_text.setEnabled(false);
        btn_notify_smile.setEnabled(false);

        TCP.setEnabled(false);
        tcp_all_figures.setEnabled(false);
        btn_clear_tcp.setEnabled(false);
        btn_names_tcp.setEnabled(false);
        btn_size_tcp.setEnabled(false);
        FieldIndex_tcp.setEnabled(false);
    }
    public DrowShapes() {
        vec_smiles.thread=tr_smile;
        vec_texts.thread=tr_text;
        tr_smile.start();
        tr_text.start();

        vec_texts.canvas=canvas;
        vec_smiles.canvas=canvas;
        setTitle("Draw Shapes");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 900);
        setLocationRelativeTo(null);

        window();

        // TODO lab_3
        DSender dSender;
        SparkClient sparkClient;
        try {
            sparkClient = new SparkClient(8083);
            sparkClient.createClient();
            dSender = new DSender();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // TODO
        connect.addActionListener(e ->{
            if (socket!=null&& !socket.isClosed())
                return;
            try {
                clientTCP("localhost", 1234);
                if (figures.size()>0) {
                    out.writeObject("ALL");
                    out.writeObject(figures);
                }
                TCP.setEnabled(true);
                tcp_all_figures.setEnabled(true);
                btn_clear_tcp.setEnabled(true);
                btn_names_tcp.setEnabled(true);
                btn_size_tcp.setEnabled(true);
                FieldIndex_tcp.setEnabled(true);
            }
            catch (IOException ex) {
                connect.setSelected(false);
                throw new RuntimeException(ex);
            }
//            catch (Exception ex) {
//                connect.setSelected(false);
//                throw new RuntimeException(ex);
//            }
        });
        disconnect.addActionListener(e -> {
            if(socket==null || socket.isClosed())
                return;
            TCP.setEnabled(false);
            tcp_all_figures.setEnabled(false);
            btn_clear_tcp.setEnabled(false);
            btn_names_tcp.setEnabled(false);
            btn_size_tcp.setEnabled(false);
            FieldIndex_tcp.setEnabled(false);
            try {
                socket.close();
                out.close();
                in.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();

                if (e.getButton() == MouseEvent.BUTTON1) { // левая кнопка мыши - создать новую фигуру
                    Figure f;
                    if (rb_select_text.isSelected()) {
                        f = new RotatingText(p.x, p.y);
                        vec_texts.texts.add((RotatingText) f);
                        // TODO На Сервер
                        try {
                            try {
                                sparkClient.addFigure(f);
                                dSender.sendCommand("ADD", f);
                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                            if(connect.isSelected()){
                                System.out.println("ADD");
                                out.writeObject("ADD");
                                out.writeObject(f);
                                out.flush();
                            }
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        int size = (int) (Math.random() * 50 + 20);
                        f = new PulsatingSmiley(p.x, p.y, size);
                        vec_smiles.smiles.add((PulsatingSmiley) f);
                        // TODO На Сервер
                        try {
                            try {
                                sparkClient.addFigure(f);
                                dSender.sendCommand("ADD", f);
                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                            if(connect.isSelected()) {
                                out.writeObject("ADD");
                                out.writeObject(f);
                                out.flush();
                            }
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    figures.add(f);
                } else if (e.getButton() == MouseEvent.BUTTON3) { // правая кнопка мыши - удалить фигуру
                    for (Figure f : figures) {
                        if (f.contains(p.x, p.y)) {
                            try {
                                sparkClient.delFigure(figures.indexOf(f));
                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                            figures.remove(f);

                            break;
                        }
                    }
                }
            }
        });
        btn_stop_text.addActionListener(e -> {
            vec_texts.stop=true;
            btn_stop_text.setEnabled(false);
            btn_notify_text.setEnabled(true);
        });
        btn_notify_text.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RotatingText.stop=false;
                vec_texts.stop=false;
                btn_stop_text.setEnabled(true);
                btn_notify_text.setEnabled(false);
                synchronized (tr_text){tr_text.notify();}
            }});
        btn_stop_smile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PulsatingSmiley.stop=true;
                vec_smiles.stop=true;
                btn_stop_smile.setEnabled(false);
                btn_notify_smile.setEnabled(true);
            }});
        btn_notify_smile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PulsatingSmiley.stop=false;
                vec_smiles.stop=false;
                btn_stop_smile.setEnabled(true);
                btn_notify_smile.setEnabled(false);
                synchronized (tr_smile){tr_smile.notify();}
            }});



        btn_load_bin.addActionListener(e -> {
            FileDialog dlg=new FileDialog((Frame) null,"Открыть файл",FileDialog.LOAD);
            dlg.setFile("*.dat");
            dlg.show();
            String path=dlg.getDirectory()+dlg.getFile();
            try	{
                DataInputStream si=new DataInputStream(new FileInputStream(path));
                int n=si.readInt();
                Figure pp;
                while (n--!=0){
                    String className=si.readUTF();
                    Class cc=Class.forName(className);
                    pp=(Figure)cc.newInstance();
                    pp.Load(si);
                    figures.add(pp);
                    // todo
                    if (className.equals("cs.spark.Figure.RotatingText")){
                        vec_texts.texts.add((RotatingText) pp);
                    }
                    else
                        vec_smiles.smiles.add((PulsatingSmiley) pp);
                }
                si.close();
            }
            catch (Exception ignored) { }
//                }

        });
        btn_save_bin.addActionListener(e -> {
            FileDialog dlg=new FileDialog((Frame) null,"Сохранить файл",FileDialog.SAVE);
            dlg.setFile("*.dat");
            dlg.show();
            String path=dlg.getDirectory()+dlg.getFile();
            try	{
                DataOutputStream si=new DataOutputStream(new FileOutputStream(path));
                int n=figures.size();
                si.writeInt(n);
                for (Figure figure : figures) {
                    si.writeUTF(((Figure) figure).getClass().getName());
                    ((Figure) figure).Save(si);
                }
                si.close();
            }
            catch (Exception ignored) {}
        });
        btn_load_text.addActionListener(e -> {
            FileDialog dlg=new FileDialog((Frame) null,"Открыть файл",FileDialog.LOAD);
            dlg.setFile("*.txt");
            dlg.show();
            String path=dlg.getDirectory()+dlg.getFile();
            try	{
                BufferedReader si=new BufferedReader(new InputStreamReader(new FileInputStream(path),"Cp1251"));
                String ss=si.readLine();
                int n=Integer.parseInt(ss);
                Figure pp;
                while (n--!=0){
                    String className=si.readLine();
                    Class<?> cc= Class.forName(className);
                    pp=(Figure) cc.newInstance();
                    pp.LoadText(si);
                    figures.add(pp);
                    //Todo
                    if (className.equals("cs.spark.Figure.RotatingText")){
                        vec_texts.texts.add((RotatingText) pp);
                    }
                    else
                        vec_smiles.smiles.add((PulsatingSmiley) pp);
                }
                si.close();
            }
            catch (Exception ee) { System.out.println(""+ee);}
//                }

        });
        btn_save_text.addActionListener(e -> {
            FileDialog dlg=new FileDialog((Frame) null,"Сохранить файл",FileDialog.SAVE);
            dlg.setFile("*.txt");
            dlg.show();
            String path=dlg.getDirectory()+dlg.getFile();
            try	{
                BufferedWriter si=new BufferedWriter(new OutputStreamWriter(new FileOutputStream("jf.txt"),"Cp1251"));
                int n=figures.size();
                si.write(""+n);
                si.newLine();
                for (int i=0;i<n;i++){
                    Figure pp=(Figure) figures.get(i);
                    si.write(pp.getClass().getName());
                    si.newLine();
                    pp.SaveText(si);
                }
                si.close();
            }
            catch (Exception ee) {}
        });
        btn_load_xml.addActionListener(e -> {
            FileDialog dlg=new FileDialog((Frame) null,"Открыть файл",FileDialog.LOAD);
            dlg.setFile("*.xml");
            dlg.show();
            String path=dlg.getDirectory()+dlg.getFile();
            try	{
                BufferedReader si=new BufferedReader(new InputStreamReader(new FileInputStream(path),"Cp1251"));
                XStream xstream = new XStream();
                xstream.allowTypes(new Class[]{PulsatingSmiley.class, RotatingText.class});
                RotatingText.setXMLParser(xstream);
                PulsatingSmiley.setXMLParser(xstream);
                Figure.setXMLParser(xstream);
                Vector<Figure> xx=(Vector)
                        xstream.fromXML(si);
                si.close();
                for(int i=0;i<xx.size();i++){
                    Figure pp=xx.get(i);
                    figures.add(pp);
                    // Todo
                    if(pp.getClass().getName().equals("cs.spark.Figure.RotatingText"))
                        vec_texts.texts.add((RotatingText) pp);
                    else
                        vec_smiles.smiles.add((PulsatingSmiley) pp);
                }
            }
            catch (Exception ee) { System.out.println(""+ee);}

        });
        btn_save_xml.addActionListener(e -> {
            FileDialog dlg=new FileDialog((Frame) null,"Сохранить файл",FileDialog.SAVE);
            dlg.setFile("*.xml");
            dlg.show();
            String path=dlg.getDirectory()+dlg.getFile();
            try {
                OutputStreamWriter si=new OutputStreamWriter(new FileOutputStream(path),"Cp1251");
                XStream xstream = new XStream();
                RotatingText.setXMLParser(xstream);
                PulsatingSmiley.setXMLParser(xstream);
                Figure.setXMLParser(xstream);
                xstream.toXML(figures,si);
                si.flush();
                si.close();
            }
            catch (Exception ee) {System.out.println(""+ee);}
        });
        btn_load_serial.addActionListener(e -> {
            FileDialog dlg=new FileDialog((Frame) null,"Открыть файл",FileDialog.LOAD);
            dlg.setFile("*.srl");
            dlg.show();
            String path=dlg.getDirectory()+dlg.getFile();
            try	{
                ObjectInputStream si=new ObjectInputStream(new FileInputStream(path));
                int n=si.readInt();
                Figure pp;
                while (n--!=0){
                    pp=(Figure) si.readObject();
                    figures.add(pp);
                    // TODO
                    if(pp.getClass().getName().equals("cs.spark.Figure.RotatingText"))
                        vec_texts.texts.add((RotatingText) pp);
                    else
                        vec_smiles.smiles.add((PulsatingSmiley) pp);
                }
                si.close();
            }
            catch (Exception ee) { System.out.println(""+ee);}

        });
        btn_save_serial.addActionListener(e -> {
            FileDialog dlg=new FileDialog((Frame) null,"Сохранить файл",FileDialog.SAVE);
            dlg.setFile("*.srl");
            dlg.show();
            String path=dlg.getDirectory()+dlg.getFile();
            try	{
                ObjectOutputStream si=new ObjectOutputStream(new FileOutputStream(path));
                int n=figures.size();
                si.writeInt(n);
                for (int i=0;i<n;i++){
                    Figure pp=(Figure) figures.get(i);
                    si.writeObject(pp);
                }
                si.close();
            }
            catch (Exception ee) {System.out.println(""+ee);}
        });
        btn_load_json.addActionListener(e -> {
            FileDialog dlg=new FileDialog((Frame) null,"Открыть файл",FileDialog.LOAD);
            dlg.setFile("*.json");
            dlg.show();
            String path=dlg.getDirectory()+dlg.getFile();
            try {
                Gson gson = new Gson();
                BufferedReader si=new BufferedReader(new InputStreamReader(new FileInputStream(path),"Cp1251"));
                String ss=si.readLine();
                int n=Integer.parseInt(ss);
                Figure pp;
                while (n--!=0){
                    Class cc=Class.forName(si.readLine());
                    pp=(Figure) gson.fromJson(si.readLine(),cc);
                    figures.add(pp);
                    //TODO
                    if(pp.getClass().getName().equals("cs.spark.Figure.RotatingText"))
                        vec_texts.texts.add((RotatingText) pp);
                    else
                        vec_smiles.smiles.add((PulsatingSmiley) pp);
                }
                si.close();
            }
            catch (Exception ee) { System.out.println(""+ee);}

        });
        btn_save_json.addActionListener(e -> {
            FileDialog dlg=new FileDialog((Frame) null,"Сохранить файл",FileDialog.SAVE);
            dlg.setFile("*.json");
            dlg.show();
            String path=dlg.getDirectory()+dlg.getFile();
            try {
                BufferedWriter si= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path),"Cp1251"));
                Gson gson = new Gson();
                int n=figures.size();
                si.write(""+n);
                si.newLine();
                for (int i=0;i<n;i++){
                    Figure pp=(Figure) figures.get(i);
                    si.write(pp.getClass().getName());
                    si.newLine();
                    si.write(gson.toJson(pp));
                    si.newLine();
                }
                si.flush();
                si.close();
            }
            catch (Exception ee) {System.out.println(""+ee);}
        });
        // TODO КНОПКИ
        TCP.addActionListener(e -> {
            if(connect.isSelected()) {
                sendCommand("ADD");
            }
        });
        tcp_all_figures.addActionListener(e -> {
            if(connect.isSelected()) {
                sendCommand("ADD_D");
            }
        });

        FieldIndex_tcp.addActionListener((ActionListener) e -> {
            // Отображение введенного текста
            if(connect.isSelected()) {
                sendCommand("GET");
            }
            // TODO
//            dSender.TextF(FieldIndex_tcp.getText());
//            dSender.sendCommand("GET", null);
//            if(dSender.isAnswer())
//                addFigures(dSender.figure);
        });
        FieldIndex_udp.addActionListener((ActionListener) e -> {
            // Отображение введенного текста
//            if(connect.isSelected()) {
//                sendCommand("GET");
//            }
            // TODO
            dSender.TextF(FieldIndex_udp.getText());
            dSender.sendCommand("GET", null);
            if(dSender.isAnswer())
                addFigures(dSender.figure);
        });
        btn_clear_tcp.addActionListener(e ->{
            if(connect.isSelected()) {
                sendCommand("CLEAR");
            }
//            dSender.sendCommand("CLEAR", null);
        });
        btn_clear_udp.addActionListener(e ->{
//            if(connect.isSelected()) {
//                sendCommand("CLEAR");
//            }
            dSender.sendCommand("CLEAR", null);
        });
        btn_names_tcp.addActionListener(e -> {
            if(connect.isSelected()) {
                sendCommand("NAMES");
            }
            // TODO
//            dSender.sendCommand("NAMES", null);
//            namesUDP(dSender.getNames());
        });
        btn_names_udp.addActionListener(e -> {
//            if(connect.isSelected()) {
//                sendCommand("NAMES");
//            }
            // TODO
            dSender.sendCommand("NAMES", null);
            namesUDP(dSender.getNames());
        });
        btn_size_tcp.addActionListener(e -> {
            if(connect.isSelected()) {
                sendCommand("SIZE");
            }
            // TODO
//            dSender.sendCommand("SIZE", null);
//            up.getLabelSize(dSender.getSize());
        });
        btn_size_udp.addActionListener(e -> {
//            if(connect.isSelected()) {
//                sendCommand("SIZE");
//            }
            // TODO
            dSender.sendCommand("SIZE", null);
            up.getLabelSize(dSender.getSize());
        });

        btn_udp.addActionListener(e -> {
            // TODO
            dSender.sendCommand("ADD_D", null);
            for(Figure figure: dSender.getFigures()){
                addFigures(figure);
            }
        });

        btn_all_api.addActionListener(e -> {
            try {
                Vector<DBRequest> dbRequests = sparkClient.allFigure();
                Figure pp;
                Gson gson = new Gson();
                for(DBRequest f: dbRequests){
                    Class cc=Class.forName(f.getClassName());
                    pp=(Figure) gson.fromJson(f.getJsonObject(),cc);
                    //pp.color = new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
                    figures.add(pp);

                    //TODO

                    System.out.println(pp.getClass().getName());
                    if(pp.getClass().getName().equals("cs.spark.Figure.RotatingText"))
                        vec_texts.texts.add((RotatingText) pp);
                    else
                        vec_smiles.smiles.add((PulsatingSmiley) pp);
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        btn_names_api.addActionListener(e -> {
            up.textArea.setText("");
            Vector<DBRequest> dbRequests = null;
            try {
                dbRequests = sparkClient.allFigure();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            StringBuilder stringBuilder = new StringBuilder();
            Figure pp;
            Gson gson = new Gson();
            for(DBRequest f:dbRequests){
                stringBuilder.append(f.getClassName()+f.getJsonObject()).append("\n");
            }
            up.east(stringBuilder);
        });
        btn_del_api.addActionListener(e -> {
            if(!field_api.getText().equals("")){
                try {
                    sparkClient.delFigure(Integer.parseInt(field_api.getText()));
                    Figure figure =figures.get(Integer.parseInt(field_api.getText()));
                    figures.remove(Integer.parseInt(field_api.getText()));
                    if(figure.getClass().getName().equals("cs.spark.Figure.RotatingText")){
                        //RotatingText rotatingText = new RotatingText((RotatingText) figure);
                        vec_texts.texts.remove( figure);
                    }
                    else{
                        //PulsatingSmiley pulsatingSmiley = new PulsatingSmiley((PulsatingSmiley) figure);
                        vec_smiles.smiles.remove(figure);
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        btn_get_api.addActionListener(e->{
            if(!field_api.getText().equals("")){
                try {
                    Figure figure = sparkClient.getFigure(Integer.parseInt(field_api.getText()));
                    addFigures(figure);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        add(canvas);

        jMenuBar.add(jMenu);
        setJMenuBar(jMenuBar);

        setVisible(true);
    }
    public void addFigures(Figure figure){
        if(figure.getClass().getName().equals("cs.spark.Figure.RotatingText")){
            RotatingText rotatingText = new RotatingText((RotatingText) figure);
            vec_texts.texts.add( rotatingText);
            figures.add(rotatingText);
        }
        else{
            PulsatingSmiley pulsatingSmiley = new PulsatingSmiley((PulsatingSmiley) figure);
            vec_smiles.smiles.add(pulsatingSmiley);
            figures.add(pulsatingSmiley);
        }
    }
    public void namesUDP(List<String> names){
        int i = 0;
        up.textArea.setText("");
        StringBuilder stringBuilder = new StringBuilder();
        for(String s:names){
            stringBuilder.append(s).append("\n");
            System.out.println(s);
            i++;
        }
        up.east(stringBuilder);
        //up.clear();
    }
    public void sendCommand(String command) {
        try {
            out.writeObject(command);
            switch (command) {
                case "CLOSE": // Закрытие соединения
                    out.writeObject(command);
                case "CLEAR": // Очистка вектора объектов
                    out.writeObject(command);
                    break;
                case "ADD": // передача объектов
                    Figure figure = figures.get(0);
                    out.writeObject(figure);
                    break;
                case "ADD_D": // Из сервера взять
//                    figures.clear();
//                    vec_texts.texts.clear();
//                    vec_smiles.smiles.clear();
                    int size = (Integer) in.readObject();
                    System.out.println(size);
                    for(int i = 0; i < size; i++){
                        Figure figure1 = (Figure) in.readObject();
                        figure1.x += 10;
                        addFigures(figure1);
                    }
                    break;
                case "SIZE": // запрос размера векторов.
                    int getLabelSize = (Integer) in.readObject();
                    System.out.println(getLabelSize);
                    up.getLabelSize(getLabelSize);
                    break;
                case "NAMES": // передача списка имен
                    List<String> names = (List<String>) in.readObject();
                    int i = 0;
                    up.textArea.setText("");
                    StringBuilder stringBuilder = new StringBuilder();
                    for(String s:names){
                        stringBuilder.append(s).append("\n");
                        System.out.println(s);
                        i++;
                    }
                    up.east(stringBuilder);
                    up.clear();
                    break;

                case "GET": // запрос объекта с заданным номером вектора
                    out.writeObject(FieldIndex_tcp.getText());
                    if((boolean)in.readObject()) {
                        Figure figure1 = (Figure) in.readObject();
                        addFigures(figure1);
                    }
                    break;
                case "?": // ответ на запрос размера вектора
                    break;
                default:
                    break;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void clientTCP(String host, int port) {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class UpPanel extends JPanel {
    public JTextArea textArea;
    public JScrollPane scrollPane;
    UpPanel() {
        textArea = new JTextArea(12, 50);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

    }
    void east(StringBuilder s){
        textArea.append(s.toString());
    }

    public void clear(){
    }
    public void getLabelSize(int s){
        textArea.setText("Размер вектора: "+s);
    }
}

class NumericTextField extends JTextField {
    public NumericTextField() {
        super(10);
        setDocument(new NumericDocument());
    }

    private static class NumericDocument extends PlainDocument {
        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (str == null) {
                return;
            }

            char[] chars = str.toCharArray();
            boolean isNumeric = true;

            for (char aChar : chars) {
                if (!Character.isDigit(aChar)) {
                    isNumeric = false;
                    break;
                }
            }

            if (isNumeric) {
                super.insertString(offs, new String(chars), a);
            }
        }
    }
}