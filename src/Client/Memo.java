package Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
////////////////////////////////////////////////////////////


/////////////////////////////////////////////////////////////////////////////

public class Memo implements ActionListener{

    private String ip;
    private String id;
    private Font font;
    private Color color;
    private String string;
    private int count;
    int length;
    private MemoInput mi;
    private MemoOutput mo;

    private Socket socket;

    private JFrame jframe;
    private JButton jbBack,jbSave,jbRefresh;
    private JLabel jlTitle; //수정날짜 아직 못만듦(jl2)
    //JTextArea ta;
    private JScrollPane jsp;
    private JTextPane jtp;

    private JPanel jp1,jp2;

    public Memo(String ip,String id, Font font, Color color, int count) throws IOException{
        this.ip = ip;
        this.id = id;
        this.font = font;
        this.color = color;
        this.count = count;
        
        
        
	    
		/*
		 * FileOutputStream fos1 = new FileOutputStream("e:/data2.txt"); DataInputStream
		 * dis1 = new DataInputStream(s1.getInputStream()); DataOutputStream dos1 = new
		 * DataOutputStream(fos1);
		 * 
		 * length = dis1.readInt();
		 * 
		 * byte [] bytearr = new byte[length]; System.out.println("byte -> file화 중...");
		 * 
		 * while(dis1.read(bytearr) != -1) { dos1.write(bytearr); String str = new
		 * String(bytearr); memo = str; } fos1.close(); dis1.close(); s1.close();
		 * System.out.println("끝.");
		 */
        mi = new MemoInput(this);
        string = mi.getMemo();
        
        
     


        jframe = new JFrame("메모장");
        jtp = new JTextPane();
        jtp.setPreferredSize(new Dimension(100, 500));
        jtp.setText(string);
        jbBack = new JButton(new ImageIcon("left-arrow.png"));
        jbSave = new JButton("save");
        jbRefresh = new JButton(new ImageIcon("update-arrows.png")); //임시
        jlTitle = new JLabel("       Multi Chat");
        jlTitle.setFont(new Font("본고딕",Font.BOLD,25));

        jp1 = new JPanel();
        jp2 = new JPanel();

        jp1.setLayout(new BorderLayout());
        jp2.setLayout(new BorderLayout());

        jp1.add(jbRefresh, BorderLayout.EAST);

        jp2.add(jbBack, BorderLayout.WEST);
        jp2.add(jlTitle,BorderLayout.CENTER);
        jp2.add(jbSave, BorderLayout.EAST);

        jframe.add(jp1, BorderLayout.SOUTH);
        jframe.add(jp2, BorderLayout.NORTH);

        JScrollPane jsp = new JScrollPane(jtp, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,  JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jframe.add(jsp, BorderLayout.CENTER);

        jbBack.addActionListener(this);
        jbSave.addActionListener(this);
        jbRefresh.addActionListener(this);

        jframe.setSize(360,600);
        jframe.setVisible(true);

        Toolkit tk = Toolkit.getDefaultToolkit();
        //Toolkit의 getScreenSize() 메서드를 사용해서 스크린사이즈를 담은 Dimension 객체를 리턴받는다.
        Dimension d = tk.getScreenSize();
        int screenHeight = d.height;
        int screenWidth = d.width;
        jframe.setLocation((screenWidth - jframe.getWidth())/2, (screenHeight - jframe.getHeight())/2);
        jframe.setResizable(false);
        jframe.setVisible(true);

    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jbBack) {/////////////////////////back누르면 다시 홈화면으로
            try {
                jframe.setVisible(false);
                new Home2(ip,id,font,color, --count);
            }catch(IOException e1) {
                e1.printStackTrace();
            }

        }
        else if(e.getSource() == jbSave) {//////////////////////저장누르면 ThreadSend2쓰레드 실행
            try {
                mo = new MemoOutput(this);
                jframe.setVisible(false);
                new Home2(ip,id,font,color, --count);
            }catch(IOException e1) {
                e1.printStackTrace();
            }



        }
        else if(e.getSource() == jbRefresh) {//////////////////샤로고침누르면 다시 메모클라스 재실행
        	
        	
            int respon  = JOptionPane.showConfirmDialog(jbRefresh, "새로고침 하시곘습니까?", "새로고침 경고장", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respon == 0) {
                jframe.setVisible(false);
                try {
                    new Memo(ip, id, font, color, count);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } else if (respon == 1) {
                //	JOptionPane.showMessageDialog(jbLogin, "취소되었습니다.");
            }
        	
        }

    }

    public Socket getSocket() {
        return socket;
    }
    public String getIp() {
        return ip;
    }
    public JTextPane getJtp() {
        return jtp;
    }

}

class MemoInput{
	int length;
	String memo;
	Socket s1;
	Memo m1;
	
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public MemoInput(Memo m1) throws UnknownHostException, IOException {
		this.m1= m1;
		System.out.println("111");
		s1 = new Socket("192.168.0.49", 37799);
	    
		FileOutputStream fos1 = new FileOutputStream("e:/data2.txt");
		DataInputStream dis1 = new DataInputStream(s1.getInputStream());
		DataOutputStream dos1 = new DataOutputStream(fos1);
		DataOutputStream dos2 = new DataOutputStream(s1.getOutputStream());
		dos2.writeUTF("1");
		length = dis1.readInt();
		System.out.println(length);
		byte [] bytearr = new byte[length];
		while(dis1.read(bytearr) != -1) {
			dos1.write(bytearr);
			String str = new String(bytearr);
			memo = str;
			//////////////
			System.out.println(str);
		}
		fos1.close(); dis1.close();
		System.out.println("끝.");
		
    }
}

class MemoOutput {
	int length;
	String memo;
	Socket s1;
	Memo m;
	
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public MemoOutput(Memo m) throws IOException {
		this.m = m;
		
		s1 = new Socket("192.168.0.49", 37799);
		FileOutputStream fos = new FileOutputStream("e:/data2.txt");
		DataOutputStream dos2 = new DataOutputStream(fos);
		
		
		byte [] byteBae = m.getJtp().getText().getBytes();
		dos2.write(byteBae);
		
		File file1 = new File("e:/data2.txt");
		FileInputStream fis1 = new FileInputStream(file1); 
		DataInputStream dis1 = new DataInputStream(fis1);
		DataOutputStream dos1 = new DataOutputStream(s1.getOutputStream());
		
		dos1.writeUTF("2");
		
		length = byteBae.length;

		dos1.writeInt(length);
	
		
		
		while (dis1.read(byteBae) != -1) {
			dos1.write(byteBae);
		}
		dos1.flush(); dis1.close(); dos1.close();
	}
}
