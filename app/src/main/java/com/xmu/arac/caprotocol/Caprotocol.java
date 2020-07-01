package com.xmu.arac.caprotocol;

public class Caprotocol {

    //fileName
    public String fileName;

    //file Length
    public long fileLength;

    //The Type of transmission
    //trans 0 is message
    //trans 1 is file
    public int transmissionType;

    //The Content of transmission
    public String content;

    //Transmission Length
    public long transLength;

    //Send or Receive
    //public int itemType = Constants.CHAT_SEND;
    public int itemType = 0;

    //0 is string, 1 is file
    public int showType = 0;

    public Caprotocol() {
        transmissionType = 0;
        content = "";
        transLength = 3;
    }

}
