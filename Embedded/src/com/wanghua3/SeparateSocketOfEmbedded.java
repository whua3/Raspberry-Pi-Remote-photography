package com.wanghua3;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SeparateSocketOfEmbedded extends Thread {
	Socket socket;
	int length = 0;
	byte[] sendBytes = null;
	DataOutputStream dos = null;
	FileInputStream fis = null;
	public SeparateSocketOfEmbedded(Socket s) {
		this.socket = s;
	}

	public void in() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
			socket.getInputStream()));
			String msg = null;
			while ((msg = reader.readLine()) != null) {
				//takeAPhoto();
				System.out.println(msg);
			}
		} catch (IOException e) {
			
		}
	}

	public void takeAPhoto() {
		try {
			try {
				dos = new DataOutputStream(socket.getOutputStream());
				File fileDir = new File("D:");
				if (!fileDir.exists()) {
					fileDir.mkdir();
				}
				File file = new File(fileDir, "image.jpg");
				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} 
				fis = new FileInputStream(file);
				sendBytes = new byte[1024];
				while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
					dos.write(sendBytes, 0, length);
					dos.flush();
				}
				System.out.println("the photo has been sent out!");
			} finally {
				if (dos != null)
					dos.close();//socket closed,or the photo can not be sent finished
				if (fis != null)
					fis.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		in();
	}

}