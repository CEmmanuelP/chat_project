package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import javax.swing.*;
import java.awt.*;



class Screen extends JFrame implements ActionListener, ItemListener, Runnable{
    private JLabel jlTitle, jlNickname, jlFont, jlColor, jlPreview;
    private JComboBox jcbFont;
    private JButton jbPalette, jbLogin;

    public static JTextField jtfNickname= new JTextField("닉네임 입력");
    private Font font1 = new Font("굴림", Font.BOLD, 20);
    private Color color1 = Color.BLACK;
    private int[] lengthchk = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private int count = 0;


    Home2 link1;
    String ip, id;

    String[] font = {"글꼴을 고르세요", "굴림", "궁서", "바탕", "돋움", "맑은 고딕"};



    JPanel jpFlow = new JPanel();

    public Screen(){
    	
    
        Thread th1 = new Thread(this);
        th1.start();
    	
    	
        jlTitle = new JLabel("Multi Chat");
        jlNickname = new JLabel("Nickname : ");
        jlFont = new JLabel("Font : ");
        jlColor = new JLabel("Color : ");

        jlPreview = new JLabel(jtfNickname.getText());
        jlPreview.setFont(new Font("굴림", Font.BOLD, 25));
        jcbFont = new JComboBox(font);
        jcbFont.addItemListener(this);
        jbPalette = new JButton("색을 골라주세요");
        jbLogin = new JButton("LogIn");

        jbPalette.addActionListener(this);
        jbLogin.addActionListener(this);

        jlTitle.setSize(330, 70);
        jlTitle.setLocation(100, 30);
        jlTitle.setFont(new Font("버다나", Font.BOLD, 30));

        jlNickname.setSize(120, 30);
        jlNickname.setLocation(30, 180);
        jlNickname.setFont(new Font("본고딕", Font.BOLD, 19));

        jlFont.setSize(120, 30);
        jlFont.setLocation(30, 250);
        jlFont.setFont(new Font("본고딕", Font.BOLD, 19));

        jlColor.setSize(120, 30);
        jlColor.setLocation(30, 315);
        jlColor.setFont(new Font("본고딕", Font.BOLD, 19));

        jtfNickname.setSize(154, 40);
        jtfNickname.setLocation(150, 175);
        jtfNickname.setFont(new Font("굴림", Font.BOLD, 15));

        jcbFont.setSize(150, 40);
        jcbFont.setLocation(150,245);
        jcbFont.setFont(new Font("본고딕", Font.BOLD, 15));

        jbPalette.setSize(150, 40);
        jbPalette.setLocation(150,315);
        jbPalette.setFont(new Font("본고딕", Font.BOLD, 15));

        jbLogin.setSize(320, 40);
        jbLogin.setLocation(10,515);
        jbLogin.setFont(new Font("본고딕", Font.BOLD, 15));


        this.add(jlTitle);
        this.add(jlNickname);
        this.add(jlFont);
        this.add(jlColor);
        this.add(jtfNickname);
        this.add(jcbFont);
        this.add(jbPalette);
        this.add(jbLogin);

        setLayout(null);
        setSize(360, 600);
        setVisible(true);

        jpFlow.setSize(360, 60);
        jpFlow.setLocation(0, 420);
        jpFlow.setVisible(true);
        this.add(jpFlow,"panel1");
        jpFlow.setLayout(new FlowLayout());
        jpFlow.add(jlPreview);

        Toolkit tk = Toolkit.getDefaultToolkit();
        //Toolkit의 getScreenSize() 메서드를 사용해서 스크린사이즈를 담은 Dimension 객체를 리턴받는다.
        Dimension d = tk.getScreenSize();
        int screenHeight = d.height;
        int screenWidth = d.width;
        this.setLocation((screenWidth - this.getWidth())/2, (screenHeight - this.getHeight())/2);
        this.setResizable(false);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {


        if(e.getSource() == jbPalette){
            color1 = JColorChooser.showDialog(null, "나를 표현할 색을 골라보세요.", Color.CYAN);
            if(color1 != null){
                jlPreview.setForeground(color1);
            }
        }

        if(e.getSource() ==jbLogin) {

            if(jlPreview.getText().length() == 0){
                JOptionPane.showMessageDialog(jtfNickname, "닉네임을 입력하세요.");
                jtfNickname.setText("닉네임 입력");
            }
            else {
                int respon  = JOptionPane.showConfirmDialog(jbLogin, "위와 같은 설정내용으로 \n  채탱방에 입장하시겠습니까?", "로그인", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(respon == 0) {
                    try {
                        this.setVisible(false);
                        count++;
                        new Home2("192.168.0.49",jtfNickname.getText(),font1,color1,count);
                    }catch(IOException e1) {
                        e1.printStackTrace();
                    }

                } else if (respon == 1) {
                    //	JOptionPane.showMessageDialog(jbLogin, "취소되었습니다.");
                }
            }
        }
    }

    public void itemStateChanged(ItemEvent e){
        if(e.getItem().equals("궁서")){
            jlPreview.setFont(new Font("궁서", Font.BOLD, 25));
            font1 = new Font("궁서", Font.BOLD, 25);
        };

        if(e.getItem().equals("바탕")){
            jlPreview.setFont(new Font("바탕", Font.BOLD, 25));
            font1 = new Font("바탕", Font.BOLD, 25);
        };

        if(e.getItem().equals("굴림")){
            jlPreview.setFont(new Font("굴림", Font.BOLD, 25));
            font1 = new Font("굴림", Font.BOLD, 25);
        };

        if(e.getItem().equals("돋움")){
            jlPreview.setFont(new Font("돋움", Font.BOLD, 25));
            font1 = new Font("돋움", Font.BOLD, 25);
        };
        if(e.getItem().equals("맑은 고딕")){
            jlPreview.setFont(new Font("맑은 고딕", Font.BOLD, 25));
            font1 = new Font("맑은 고딕", Font.BOLD, 25);
        };

    }

    public void run(){
        while(jlPreview != null){
            jlPreview.setText(jtfNickname.getText());
            String nick = jlPreview.getText();
            char[] name = new char[10];
            name = nick.toCharArray();

            if(name.length > 7){
                JOptionPane.showMessageDialog(jtfNickname, "닉네임은 8글자 이하로 설정 가능합니다");
                jtfNickname.setText("닉네임 입력");
            }

        }
    }
}




public class Login {
    public static void main(String[] args){
        Screen sc = new Screen();
        
        Thread th1 = new Thread(sc);
        th1.start();
    }
}
