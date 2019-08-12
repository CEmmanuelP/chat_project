package Server;

import java.io.*;
import java.net.*;
import java.util.*;

public class MultiServer {
    private List<MultiServerThread> list;
    private Socket s1;


    public MultiServer() throws IOException {

        list = new ArrayList<>();//사용자 목록 객

        ServerSocket serverSocket = new ServerSocket(9876);//서버(소켓) 생성
        System.out.println("서버가 준비되었습니다.");


        boolean isStop = false;
        while (!isStop) {//true
            System.out.println("Server ready...");
            s1 = serverSocket.accept();
            System.out.println(s1.getInetAddress() + "에서 접속했습니다.");
                
            DataInputStream disstr = new DataInputStream(s1.getInputStream());
         String str = disstr.readUTF();
         
         
         if(str.equals("1")) {
            OutputStream out = s1.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);
            InputStream in = s1.getInputStream();
            DataInputStream dis = new DataInputStream(in);
            dos.writeInt(list.size());
            System.out.println("값 전송되었습니다.");
            dos.close(); s1.close();
         }else if(str.equals("2")) {
                System.out.println("start");
                MultiServerThread mst = new MultiServerThread(this);
                list.add(mst);
                Thread t = new Thread(mst);
                t.start();
            }
        }
    }

    public List<MultiServerThread> getList() {
        return list;
    }

    public Socket getSocket() {
        return s1;
    }

    public static void main(String arg[]) throws IOException {
        new MultiServer();
    }

    private static class MultiServerThread implements Runnable {
        private Socket socket, socket2;
        private MultiServer ms;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;

        public MultiServerThread(MultiServer ms) {
            this.ms = ms;//서버 정보 받아옴
        }


        public synchronized void run() {
            boolean isStop = false;
            try {
                socket = ms.getSocket();
                ois = new ObjectInputStream(socket.getInputStream());
                oos = new ObjectOutputStream(socket.getOutputStream());//i/o stream 객체생성

                String message = null;

                while (!isStop) {
                    message = (String) ois.readObject();
                    String[] str = message.split("#");
                    if(str[1].equals("exit")) {
                        ms.getList().remove(this);
                        System.out.println(socket.getInetAddress() + " 정상적으로 종료하셨습니다");
                        System.out.println("list size : " + ms.getList().size());
                        break;
                    }else if(str[1].equals("back")) {
                        ms.getList().remove(this);
                        System.out.println(socket.getInetAddress() + " 정상적으로 종료하셨습니다");
                        System.out.println("list size : " + ms.getList().size());
                        break;
                    }else {
                        broadCasting(message);
                    }

                }



            } catch (Exception e) {
                ms.getList().remove(this);
                System.out.println(socket.getInetAddress() + " 비정상적으로 종료하셨습니다");
                System.out.println("list size : " + ms.getList().size());
            }
        }

        public void broadCasting(String message) throws IOException {//모두에게 뿌려주는
            for (MultiServerThread ct : ms.getList()) {
                ct.send(message);
            }
        }

        public void send(String message) throws IOException {//출력용
            oos.writeObject(message);
        }

    }
}
