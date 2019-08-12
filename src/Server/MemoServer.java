package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class MemoServer {
	
	public static void main(String[] args) throws IOException {
		int length = 0;
		int length1 = 0;
		
		ServerSocket ss1 = new ServerSocket(37799);
		System.out.println("서버가 준비되었습니다.");

		while(true) {
			System.out.println("기다리는 중..");
			Socket s1 = ss1.accept();
			System.out.println(s1.getInetAddress() + "에서 접속했습니다.");
			
			DataInputStream disstr = new DataInputStream(s1.getInputStream());
			String str = disstr.readUTF();
			
			if(str.equals("1")) {
				File file1 = new File("e:/data1.txt");
				FileInputStream fis1 = new FileInputStream(file1);
				DataInputStream dis1 = new DataInputStream(fis1);
				DataOutputStream dos1 = new DataOutputStream(s1.getOutputStream());
				
				length1 = (int)file1.length();
				dos1.writeInt(length1);
				
				byte [] byteBae = new byte[length1];
				while (dis1.read(byteBae) != -1) {
					dos1.write(byteBae);
				}
				dos1.flush(); dis1.close(); dos1.close(); 
			}else if(str.equals("2")) {
				File file1 = new File("e:/data1.txt");
				
				FileOutputStream fos1 = new FileOutputStream(file1);
				DataInputStream dis2 = new DataInputStream(s1.getInputStream());
				DataOutputStream dos2 = new DataOutputStream(fos1);
				
				length = dis2.readInt();
			     
				byte [] bytearr = new byte[length];
				while(dis2.read(bytearr) != -1) {
					dos2.write(bytearr);
				}
				dos2.flush(); dos2.close();dis2.close(); 
			}

			
		}

	}
	
	
}
