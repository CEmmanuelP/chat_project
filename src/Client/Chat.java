package Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class Chat implements ActionListener{
    private Socket socket;//원통z
    private ObjectInputStream ois;//받는용
    private ObjectOutputStream oos;//출력용
    private JFrame jframe;
    private JTextField jtf;
    private JLabel jlTitle;
    private JPanel jp1, jp2;
    private String ip;
    private String id;
    private Font font;
    private Color color;
    private JButton jbSend, jbBack;
    private JTextPane jtp;
    private String saek;
    private String gulgol;
    private int count;


//생성자 ? IP(연결하려는 서버IP) 와 ID를 인자로 받는다.

    public Chat(String ip, String id,Font font,Color color,int count) throws IOException{
        this.ip = ip;
        this.id = id;
        this.font = font;
        this.color = color;
        this.count = count;

        //서버에 연결하는 소켓 객체 생성
        socket = new Socket(ip, 9876);
        System.out.println("connected..");
        DataOutputStream dos1 = new DataOutputStream(socket.getOutputStream());
        dos1.writeUTF("2");
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());

//MultiClientThread 객체를 생성하면서 자신(MultiClient)을 인자로 넘긴다.
        MultiClientThread ct = new MultiClientThread(this);
//MultiClientThread 스레드를 시작한다.
        Thread t = new Thread(ct);
        t.start();



        jframe = new JFrame("Multi Chatting");
        jtf = new JTextField(30);
        jtp = new JTextPane();
        jtp.setPreferredSize(new Dimension(100, 500));
        jbBack = new JButton(new ImageIcon("left-arrow.png"));
        jlTitle = new JLabel("          Multi Chat");
        jlTitle.setFont(new Font("본고딕",Font.BOLD,25));
        jbSend = new JButton("Send");
        jp1 = new JPanel();
        jp2 = new JPanel();
        jtp.setBackground(Color.pink);

        jp1.setLayout(new BorderLayout());
        jp2.setLayout(new BorderLayout());

        jp1.add(jbSend, BorderLayout.EAST);
        jp1.add(jtf, BorderLayout.CENTER);

        jp2.add(jbBack, BorderLayout.WEST);
        jp2.add(jlTitle,BorderLayout.CENTER);
        jframe.add(jp1, BorderLayout.SOUTH);
        jframe.add(jp2, BorderLayout.NORTH);

        JScrollPane jsp = new JScrollPane(jtp, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,  JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jframe.add(jsp, BorderLayout.CENTER);

        jtf.addActionListener(this);     //JTextField에 ActionListener연결
        jbBack.addActionListener(this);
        jbSend.addActionListener(this);  //JButton에 ActionListener 연결

        jframe.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                try{
//윈도우를 닫을 때 사용자가 종료 메세지를 보낸 것과 동일하게 문자열을 만들어 소켓에 기록한다.
                    jframe.dispose();
                    oos.writeObject(id+"#exit");
                }catch(IOException ee){
                    ee.printStackTrace();
                }
                System.exit(0);   //어플리케이션 종료
            }
            public void windowOpened(WindowEvent e){
//윈도우가 열리면 JTextField에 커서를 둔다.
                jtf.requestFocus();//키 이벤트를 받을 컴포넌트를 강제로 설정(창이열리면 글쓰는공간에 포커스맞춰짐)
            }
        });
        //jta.setEditable(false);   //JTextArea 를 읽기전용으로
        jtp.setEditable(false);     //JTextpane을 읽기전용으로
//스크린의 사이즈를 얻어오기 위해서 Toolkit 객체를 생성한다.
        Toolkit tk = Toolkit.getDefaultToolkit();
//Toolkit의 getScreenSize() 메서드를 사용해서 스크린사이즈를 담은 Dimension 객체를 리턴받는다.
        Dimension d = tk.getScreenSize();
        int screenHeight = d.height;
        int screenWidth = d.width;
        jframe.pack();   //JFrame의 사이즈를 서브콤포넌트들에 맞게 자동으로 조정해준다.
        jframe.setLocation((screenWidth - jframe.getWidth())/2, (screenHeight - jframe.getHeight())/2);
        jframe.setResizable(false);
        jframe.setVisible(true);
        ////////////////////////////////////////////////////디자인끝~~~~~~~~~~///////////////////////////////////////////////////////////////////
    }




    //ActionListener 인터페이스가 강제하는 메서드
    public void actionPerformed(ActionEvent e){
//ActionEvent의 getSource() 메서드는 이벤트를 발생시킨 객체 자체를 리턴시켜 준다.
        Object obj = e.getSource();
        String msg = jtf.getText();  //JTextField에서 값을 읽어오는 getText() 메서드

//ActionListener에 연결한 객체가 두 개 이상이므로 IF문을 사용해 구분하였다.
//JTextField의 요청일 경우
        if(obj == jtf || obj == jbSend){
            if(msg == null || msg.length()==0){   //메세지 내용이 없을 경우
//Alert창 : JOptionPane.showMessageDialog(소속되는 상위객체, 메세지객체, 메세지창제목, 메세지창종류)
                JOptionPane.showMessageDialog(jframe, "글을 쓰세요", "경고", JOptionPane.WARNING_MESSAGE);
            }else{   //메세지 내용이 있을 경우
                try{
                    saek=color.toString();
                    gulgol = font.toString();
                    oos.writeObject(id+"#"+msg+"#"+saek+"#"+gulgol);   //메세지를 직렬화 하여 소켓에 기록      //id#msg//////////////////갑자기 toString()메소드가 자꾸 에러뜸
                } catch(IOException ee){
                    System.out.println("Teafdsfsdfsdf" + ee.getMessage());
                }

                jtf.setText("");   //기록한 후에는 입력창을 지우고 다시 입력받을 준비를 한다.
            }
//JButton의 요청일 경우 (종료버튼)
        }
        else if(obj == jbBack){
            try {
                jframe.setVisible(false);
                oos.writeObject(id + "#back");
                jframe.dispose();
                new Home2(ip, id, font, color, --count);
            }catch(IOException e1) {
                e1.printStackTrace();
            }

        }
    }

    public void exit(){
        System.exit(0);
    }
    
    


    public ObjectInputStream getOis(){
        return ois;
    }

    public JTextPane getJtp() {
        return jtp;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public String getId(){
        return id;
    }

    public void append(String s,String colors, String fonts) {
          String[] fontArr = fonts.split(",");
          String font2 = fontArr[1].substring(5,fontArr[1].length());
       
        String[] colorArr = colors.split(","); //colors를 잘라서 RGB정보추출
        int r =  Integer.parseInt(colorArr[0].substring(17,colorArr[0].length()));

        int g = Integer.parseInt(colorArr[1].substring(2,colorArr[1].length()));

        int b = Integer.parseInt(colorArr[2].substring(2,colorArr[2].length()-1));

        Color color2 = new Color(r,g,b);

        SimpleAttributeSet style = new SimpleAttributeSet();  // 스타일 객체 생성
        StyleConstants.setFontFamily(style, font2);      // style 객체에 'font2'으로 추출한 글꼴 적용
        StyleConstants.setFontSize(style, 15);      // style 객체에 글자 크기 적용
        StyleConstants.setBold(style, true);   // style 객체에 굵게 효과 적용
        StyleConstants.setForeground(style, color2);   // style 객체에 'color2'로 받아온 색깔 적용

        try {
            Document doc = jtp.getDocument();
            doc.insertString(doc.getLength(), s, style);
        }catch(BadLocationException exc) {
            exc.printStackTrace();
        }
    }

    public void append1(String s){

        try {
            Document doc = jtp.getDocument();
            doc.insertString(doc.getLength(), s,null);
        } catch (BadLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    private static class MultiClientThread extends Thread{
        private Chat c;

        public MultiClientThread(Chat c) {
            this.c = c;
        }

        public void run() {
            String message = null;
            String[] receivedMsg = null;
            boolean isStop = false;

            while(!isStop) {
                try {
                    message = (String)c.getOis().readObject();
                    receivedMsg = message.split("#");//eugene#/vote you
                    //  wmessage = receivedMsg[1].split(" ");// /vote you
                }catch(Exception e) {
                    e.printStackTrace();
                    isStop = true;
                }

                System.out.println(receivedMsg[0] + " : " + receivedMsg[1]);//eugene : hi

                if(receivedMsg[1].equals("exit")) {
                    if(receivedMsg[0].equals(c.getId())) {
                        c.exit();
                    }
                }else{
                    c.append(receivedMsg[0] + " : " + receivedMsg[1] + System.getProperty("line.separator"),receivedMsg[2], receivedMsg[3]);
                    c.getJtp().setCaretPosition(c.getJtp().getDocument().getLength());

                }
            }
        }
    }
}
