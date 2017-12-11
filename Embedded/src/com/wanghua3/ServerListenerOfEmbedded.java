package com.wanghua3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class ServerListenerOfEmbedded extends Thread {
	
	@Override
	public void run() {
		try {
			ServerSocket serverSocket=new ServerSocket(12345);
			System.out.println("服务器已经启动......");
			while (true) {
				//阻塞
				Socket socket=serverSocket.accept();
				//建立连接
				JOptionPane.showMessageDialog(null,"有客户端连接到了本机的12345端口");
				System.out.println("来啦！");
				//将socket传递给新的线程
				new SeparateSocket(socket).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
