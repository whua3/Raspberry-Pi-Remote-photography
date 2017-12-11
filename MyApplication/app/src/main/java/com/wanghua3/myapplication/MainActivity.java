package com.wanghua3.myapplication;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    EditText ip;
    TextView jduge;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);}

        ip = (EditText) findViewById(R.id.ip);
        jduge = (TextView) findViewById(R.id.judge);
        image = (ImageView) findViewById(R.id.image);

        findViewById(R.id.connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jduge.setText("连接中，请稍后...");
                connect();
            }
        });

        findViewById(R.id.takePhotos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhotos();
            }
        });
    }

    //-------------------------------------------------------

    Socket socket=null;
    BufferedWriter writer=null;
    BufferedReader reader=null;

    public void connect(){
        AsyncTask<Void,String,Boolean> read=new AsyncTask<Void, String, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... arg0) {
                try {
                    InetAddress serverAddr = InetAddress.getByName(ip.getText().toString());
                    socket = new Socket(serverAddr, 12345);
                    return true;
                } catch (UnknownHostException e1) {
                    return false;
                } catch (IOException e1) {
                    return false;
                }
            }

            protected void onPostExecute(Boolean result) {
                if(result) {
                    //------ UPDATE UI HERE
                    jduge.setText("连接成功！");
                }else{
                    jduge.setText("连接失败！");
                }
            }
        };
        read.execute();
    }

    public void  takePhotos(){
        try{
            writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write("1"+"\n");
            writer.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
        receiveFile(socket);
    }

    public void receiveFile(Socket socket) {
        byte[] inputByte = null;
        int length = 0;
        DataInputStream dis = null;
        FileOutputStream fos = null;
        try {
            try {
                dis = new DataInputStream(socket.getInputStream());
                String name=new Date().toString();
                File fileDir = new File("/sdcard/image");//仅创建路径的File对象
                if(!fileDir.exists()){
                    fileDir.mkdir();//如果路径不存在就先创建路径
                }
                File file = new File(fileDir,name+".jpg");//然后再创建路径和文件的File对象
                if(!file.exists()) {
                    file.createNewFile();
                }
                else{
                    file.delete();
                    file.createNewFile();
                }
                fos = new FileOutputStream(file);
                inputByte = new byte[100];
                    while ((length = dis.read(inputByte, 0, inputByte.length)) > 0) {
                        fos.write(inputByte, 0, length);
                        fos.flush();
                    }
            } finally {
                if (fos != null)
                    fos.close();
                if (dis != null)
                    dis.close();
                jduge.setText("拍照完成，请重新连接^_^");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
