package Client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


class Home2 extends JFrame implements ActionListener{
    String ip;
    String id;
    Font font;
    Color color;
    String nick;
    Socket socket;
  
    int count;
    int cnt = 0;





    


	private JLabel jlTitle, jlLogin, jlCount, jlIcon;
    ImageIcon img;

    private JButton jbLogout;
    private JButton jbChat;
    private JButton jbMemo;


    private JPanel jpFlow = new JPanel();



    public Home2(String ip,String id,Font font,Color color, int count) throws IOException{
        this.ip = ip;
        this.id = id;
        this.font = font;
        this.color = color;
        this.count = count;
        

        socket = new Socket(ip, 9876);
        
        DataOutputStream dos1 = new DataOutputStream(socket.getOutputStream());
        dos1.writeUTF("1");
        System.out.println("connected..");
        InputStream in = socket.getInputStream();
        DataInputStream dis = new DataInputStream(in);
        cnt = dis.readInt();
        System.out.println("connect exit");
        dis.close();
        socket.close();
        
        draw();

        jlTitle = new JLabel("Multi Chat");
        jlLogin = new JLabel(id + "님 로그인하셨습니다.");
        jlCount = new JLabel("현재 접속중인 인원은 "+ cnt + " 명 입니다.");

        jbLogout = new JButton("Logout");
        jbLogout.addActionListener(this);

        jbChat = new JButton("Chat");
        jbMemo = new JButton("Memo");

        jlTitle.setSize(330,70);
        jlTitle.setLocation(100,30);
        jlTitle.setFont(new Font("버다나",Font.BOLD,30));

        jlLogin.setSize(320,30);
        jlLogin.setLocation(20,380);
        jlLogin.setFont(new Font("본고딕",Font.BOLD,22));

        jlCount.setSize(320,30);
        jlCount.setLocation(20,430);
        jlCount.setFont(new Font("본고딕",Font.BOLD,15));

        jlIcon.setSize(300,300);
        jlIcon.setLocation(20, 100);

        jbChat.setSize(160,40);
        jbChat.setLocation(10,515);
        jbChat.setFont(new Font("본고딕",Font.BOLD,15));
        jbChat.addActionListener(this);

        jbMemo.setSize(160,40);
        jbMemo.setLocation(183,515);
        jbMemo.setFont(new Font("본고딕",Font.BOLD,15));
        jbMemo.addActionListener(this);

        jbLogout.setSize(80,30);
        jbLogout.setLocation(250, 0);


        this.add(jlTitle);
        this.add(jbLogout);
        this.add(jbChat);
        this.add(jbMemo);


        jpFlow.add(jlLogin);
        jpFlow.add(jlCount);


        setLayout(null);
        setSize(360,600);
        setVisible(true);

        jpFlow.setSize(358,120);
        //	jp1.setBackground(Color.BLUE);
        jpFlow.setLocation(0,410);
        jpFlow.setVisible(true);
        this.add(jpFlow,"panel1");
        jpFlow.setLayout(new FlowLayout());

        Toolkit tk = Toolkit.getDefaultToolkit();
        //Toolkit의 getScreenSize() 메서드를 사용해서 스크린사이즈를 담은 Dimension 객체를 리턴받는다.
        Dimension d = tk.getScreenSize();
        int screenHeight = d.height;
        int screenWidth = d.width;
        this.setLocation((screenWidth - this.getWidth())/2, (screenHeight - this.getHeight())/2);
        this.setResizable(false);
        this.setVisible(true);

        

        


    }



    private void draw() {
        // TODO Auto-generated method stub

        img = new ImageIcon("chat.png");

        jlIcon = new JLabel(null, img, JLabel.CENTER);
        jlIcon.setVerticalTextPosition(JLabel.CENTER);
        jlIcon.setHorizontalTextPosition(JLabel.RIGHT);

        this.add(jlIcon);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

        if(e.getSource()==jbLogout) {

            int respon = JOptionPane.showConfirmDialog(null, "로그아웃 하시겠습니까?",  "로그아웃", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if(respon == 0) {
                this.setVisible(false);
                new Screen();
                
            }
            else if (respon ==1){
                //	JOptionPane.showMessageDialog(null, "취소되었습니다.");
            }
        }

        if(e.getSource() == jbChat){
            try {
                this.setVisible(false);
                new Chat(ip,id,font,color,count);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                System.out.println("sdsd??");
            }
        }

        if(e.getSource() == jbMemo){

            try {
                this.setVisible(false);
                new Memo("192.168.0.49", id, font, color, count);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

    }


}
