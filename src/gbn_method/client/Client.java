package gbn_method.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import gbn_method.Constants;

import javax.swing.*;

public class Client extends Thread{

    private InetAddress address = null;
    private int port;

    private DatagramSocket datagramSocket;
    static Timer timer;
    static int timeout = 3000;

    public Client(int port){
        String netAddress = Constants.NET_ADDRESS;
        try{
            this.address =  InetAddress.getByName(netAddress);
            this.port = port;

            datagramSocket = new DatagramSocket();

            System.out.println("start send msg:");
            sendNum(0,true);
            timer = new Timer(timeout,new DelayActionListener(datagramSocket));

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                byte[] recebuf = new byte[Constants.MAX_LENGTH];
                DatagramPacket recePacket = new DatagramPacket(recebuf, recebuf.length);
                datagramSocket.receive(recePacket);

                String receStr = new String(recePacket.getData(), 0, recePacket.getLength());
                System.out.println("Client receive:" + receStr);
                int receInt = Integer.parseInt(receStr);

                if (receInt == Constants.WIN_SIZE - 1) {
                    System.out.println("-------  Server数据接收完毕!  -------");
                    timer.stop();
                    break;
                } else {
                    timer.stop();
                    Client.timer = new Timer(timeout, new DelayActionListener(datagramSocket));
                    Client.timer.start();
                    System.out.println("-------  Client重新发送数据包：" + receInt + "  -------");
                    this.sendNum(receInt, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendNum(int num,boolean lose){
        try{
            for(int i = num;i<Constants.WIN_SIZE;i++){
                if(!(lose&&i==Constants.LOSE)){
                    System.out.println("send msg "+i);
                    String sendStr = ""+i;
                    byte[] buf = sendStr.getBytes();
                    DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, address, port);
                    datagramSocket.send(datagramPacket);
                    System.out.println("msg "+ i + " end");
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

class DelayActionListener implements ActionListener {

    private DatagramSocket socket;

    DelayActionListener(DatagramSocket clientSocket){
        this.socket = clientSocket;
    }

    @Override
    public void actionPerformed(ActionEvent e){

        Client.timer.stop();
        Client.timer = new Timer(Client.timeout,new DelayActionListener(socket));
        Client.timer.start();

        System.out.println("-------  Client准备重传数据  -------");

        try{
            for(int i = 0;i<Constants.WIN_SIZE;i++){
                System.out.println("send msg "+i);
                String sendStr = ""+i;
                byte[] buf = sendStr.getBytes();
                DatagramPacket datagramPacket = new DatagramPacket(buf,buf.length,InetAddress.getByName("127.0.0.1"),1234);
                socket.send(datagramPacket);
                System.out.println("-------  Client发送数据包 " + i +"  -------");
            }
        }catch (Exception err){
            err.printStackTrace();
        }

    }
}
