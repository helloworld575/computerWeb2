package gbn_method.server;

import gbn_method.Constants;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server extends Thread{

    static int N = 3;
    private byte[] recMsg = new byte[Constants.MAX_LENGTH];
    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;

    public Server(int port){
        try{

            datagramSocket = new DatagramSocket(port);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void run(){
        int i = 0;
        while(true){

            try{
                datagramPacket = new DatagramPacket(recMsg,recMsg.length);

                datagramSocket.receive(datagramPacket);

                String receStr = new String(datagramPacket.getData(),0,datagramPacket.getLength());
                System.out.println("Server receive: "+ receStr);

                int receInt = Integer.parseInt(receStr);
                if(i< Constants.WIN_SIZE-1) {
                    if (receInt == i)
                        i++;
                    else {
                        String sendStr = "" + i;
                        System.out.println("send ack:" + sendStr);
                        byte[] buf = sendStr.getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, datagramPacket.getAddress(), datagramPacket.getPort());
                        datagramSocket.send(sendPacket);
                        i = receInt + 1;
                    }
                }else{
                    System.out.println("successful finished!");
                    Thread.sleep(5000);
                    String sendStr = "" + i;
                    byte[] buf = sendStr.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, datagramPacket.getAddress(), datagramPacket.getPort());
                    datagramSocket.send(sendPacket);
                    i = 0;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
