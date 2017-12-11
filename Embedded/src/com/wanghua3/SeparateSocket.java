package com.wanghua3;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SeparateSocket extends Thread {
	Socket socket;
    byte[] inputByte = null;
    int length = 0;
    long i=0;
    DataInputStream dis = null;
    FileOutputStream fos = null;
	public SeparateSocket(Socket s) {
		this.socket = s;
	}

	public void in() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
			socket.getInputStream()));
			String msg = null;
			while ((msg = reader.readLine()) != null) {
				receiveFile();
				System.out.println(msg);
			}
		} catch (IOException e) {
			
		}
	}

	public void receiveFile() {
		try {
            try {
                dis = new DataInputStream(socket.getInputStream());
                File fileDir = new File("E:/data");//仅创建路径的File对象
                if(!fileDir.exists()){
                    fileDir.mkdir();//如果路径不存在就先创建路径
                }
                File file = new File(fileDir,"data"+i+".txt");//然后再创建路径和文件的File对象
                if(!file.exists()) {
                    file.createNewFile();
                }
                else{
                    file.delete();
                    file.createNewFile();
                }
                fos = new FileOutputStream(file);
                //inputByte = new byte[100];
                String str="\n";
                    while ((length = dis.read(inputByte, 0, inputByte.length)) > 0) {
                        fos.write(inputByte, 0, length);
                        fos.write(str.getBytes(),0,length);
                        fos.flush();
                    }
            } finally {
                if (fos != null)
                    fos.close();
                if (dis != null)
                    dis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	public void run() {
		in();
	}

}