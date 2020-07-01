package com.xmu.arac.caprotocol;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

//A Thread diff from the main thread to avoid exception
public class ClientThread extends Thread {

    Caprotocol capro = new Caprotocol();
    int[] x ={1,2,2,2,5};

    PrintWriter clientPrintWriter;
    BufferedReader clientBufferedReader;

    Handler mainHandler;
    Handler clientHandler;

    private Socket s;
    private Gson gson;

    boolean createFile = true;

    public ClientThread(Handler handler) {
        mainHandler = handler;
        gson = new Gson();
    }

    @Override
    public void run() {
        super.run();

        try {
            //创建socket
            s = new Socket(Constants.HOST, Constants.PORT);
            //获取到读写对象
            clientPrintWriter = new PrintWriter(s.getOutputStream());
            clientBufferedReader = new BufferedReader(new InputStreamReader(s.getInputStream()));

            //Test the Print Writer
            //clientPrintWriter.write("ClientThread::This is the Android foreground");
            //clientPrintWriter.flush();

            //A New Thread to read message and send
            //Read and handle the message to main thread
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    String content = null;
                    FileOutputStream fos = null;
                    try {
                        //Use this Thread to read buffer
                        while ((content = clientBufferedReader.readLine()) != null) {
                            Caprotocol transCapro = gson.fromJson(content, Caprotocol.class);
                            switch(transCapro.transmissionType){
                                //Constants.RECEIVE_MASSAGE
                                case 0:
                                    //Transmission trans = mGson.fromJson(content, Transmission.class);
                                    //if (trans.transmissionType == Constants.TRANSFER_STR) {
                                    Message msg = new Message();
                                    msg.what = Constants.RECEIVE_MASSAGE;
                                    msg.obj = content;
                                    //Debug information
                                    System.out.println("ClientThread::Receive Message from Server:"
                                            + content);
                                    //This can use to divide to string and the file
                                    mainHandler.sendMessage(msg);
                                    break;

                                //Constants.RECEIVE_MASSAGE
                                case 1:
                                    long fileLength = transCapro.fileLength;
                                    long transLength = transCapro.transLength;
                                    if (createFile) {
                                        createFile = false;
                                        fos = new FileOutputStream(new File("d:/" + transCapro.fileName));
                                    }
                                    byte[] b = Base64Utils.decode(transCapro.content.getBytes());
                                    fos.write(b, 0, b.length);
                                    System.out.println("Receiving Percentage:" + 100 * transLength / fileLength + "%...");
                                    if (transLength == fileLength) {
                                        createFile = true;

                                        fos.flush();
                                        fos.close();
                                    }
                                    break;
                            }

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

            //当前线程创建 handler
            Looper.prepare();
            clientHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == Constants.SEND_MSG) {

                        System.out.println("ClientThread::In the sending of Message to Server!");
                        //try {

                        //Send the json
                        clientPrintWriter.write(gson.toJson(capro));
                        clientPrintWriter.flush();

                        //clientPrintWriter.write("ClientThread:Caprotocol2Json(capro):"+gson.toJson(capro));

                        //clientPrintWriter = new PrintWriter(s.getOutputStream());
                        //clientPrintWriter.write("ClientThread::This is the Handler in ClientThread");
                        //clientPrintWriter.flush();
                        /*}catch(Exception e){
                            e.printStackTrace();
                        }*/
                        //clientPrintWriter.write(msg.obj.toString() + "\r\n");
                        //clientPrintWriter.flush();
                        //If you want to break,you can push this
                        //clientPrintWriter.write("esc");
                        //clientPrintWriter.flush();
                    }
                    //else if (msg.what == Constants.SEND_FILE) {//传输文件
                        //定义标记判定是字符串还是文件
                        //sendFile(msg.obj.toString());
                    //}
                }
            };
            Looper.loop();
        } catch (IOException e) {
            e.printStackTrace();
            //Exception to Close
            try {
                if (clientPrintWriter != null) {
                    clientPrintWriter.close();
                }
                if (clientBufferedReader != null) {
                    clientBufferedReader.close();
                }
                //s is socket
                if (s != null) {
                    s.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    public Handler getClientHandler() {
        return clientHandler;
    }

    public void setClientHandler(Handler handler) {
        clientHandler = handler;
    }

}
