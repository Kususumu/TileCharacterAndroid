package com.xmu.arac.draw;

import java.util.Vector;

//We Use RectangleGroup to make many Rectangle
//It has a center of the large rectangle and number of width and length
//The input of the server is 0-100 locate the red and green position
public class RectangleGroup{
    double length;
    double width;
    int lNum;
    int wNum;
    Vertex3D center;
    //Vector<Rectangle> rGroup;

    //Use to show the rectangle
    public Vector<Vertex2D> groupPointCenter;

    Vector<Integer> greenInt = new Vector<>();
    Vector<Integer> redInt = new Vector<>();
    Vector<Integer> orangeInt = new Vector<>();
    public Vector<Vertex2D> showRedPoint;
    public Vector<Vertex2D> showGreenPoint;
    public Vector<Vertex2D> showOrangePoint;

    //The plane of the large rectangle
    Plane plane;

    RectangleGroup(){

    }

    public RectangleGroup(double cx,double cy,double l,double w,int lN,int wN){
        center = new Vertex3D(cx,cy,0.0d);

        length = l;
        width = w;
        lNum = lN;
        wNum = wN;
    }

    public double getLengthOfEach(){
        return length/lNum;
    }

    public double getWidthOfEach(){
        return width/wNum;
    }

    public void init(){
        groupPointCenter = initRectangleGroup();

        LocateZero();
        //init Red From internet
        initRed(new Vector());

        updateColorIndex();

        showRedPoint = getGroupShowColorPoint(groupPointCenter,redInt);
        showGreenPoint = getGroupShowColorPoint(groupPointCenter,greenInt);
        showOrangePoint = getGroupShowColorPoint(groupPointCenter,orangeInt);

    }

    void LocateZero(){
        System.out.println("RectangleGroup::LocationZero:x "
                +groupPointCenter.elementAt(0).x +
                " y: "+groupPointCenter.elementAt(0).y);
    }

    //Abandon Message Receive from Server
    public void initRed(Vector<Integer> redGroup){
        redInt.add(0);
        redInt.add(6);
        redInt.add(18);
        redInt.add(19);
        redInt.add(20);
        redInt.add(22);
        redInt.add(23);
        redInt.add(24);
        redInt.add(25);
        redInt.add(26);
        redInt.add(28);
        redInt.add(29);
        redInt.add(30);
        redInt.add(31);
        redInt.add(32);
        redInt.add(34);
        redInt.add(35);
    }
    //init Rectangle center group
    //in this part ,our plane is on the z = 0 plane
    //so we do not need to save the value of z
    Vector initRectangleGroup(){
        Vector<Vertex2D> t = new Vector<>();
        //Get the Initial Point
        double initialX = center.x - length/2;
        double initialY = center.y - width/2 ;
        double tempX,tempY;
        for(int i=0; i<lNum; i++){
            tempX = initialX + length/lNum/2 + i*length/lNum;
            System.out.println("RectangleGroup::tempX+i"+i+" "+tempX);
            for(int j=0; j<wNum; j++){
                tempY = initialY + width/wNum/2 + j*width/wNum;

                Vertex2D v2d = new Vertex2D(tempX,tempY);
                //add the point to the Vector
                t.add(v2d);
            }
        }
        return t;
    }

    public void clickUpdate(Vertex3D p){
        Vertex2D t2d;
        t2d = findClickRectanVertex(p);
        if(t2d !=null) {
            addOrDeleteColor(findRectangleIndex(t2d), orangeInt);
            update();
        }
    }

    void update(){
        updateColorIndex();

        showRedPoint = getGroupShowColorPoint(groupPointCenter,redInt);
        showGreenPoint = getGroupShowColorPoint(groupPointCenter,greenInt);
        showOrangePoint = getGroupShowColorPoint(groupPointCenter,orangeInt);
        DrawFloor.forceUpdate = true;
    }

    void updateColorIndex(){
        //red first
        //first detect if orange is in red
        for(int i = 0; i< orangeInt.size(); i++) {
            for(int j = 0 ; j < redInt.size() ; j++){
                if(orangeInt.elementAt(i) == redInt.elementAt(j)) {
                    orangeInt.remove(i);
                }

            }
        }

        System.out.println("RectangleGroup::OrangeIntSizeInUpdate:" + orangeInt.size());
        //then detect green in orange

        //Delete the interects
        for(int i = 0; i< orangeInt.size(); i++) {
            for(int j = 0 ; j < greenInt.size() ; j++){
                if(orangeInt.elementAt(i) == greenInt.elementAt(j)) {
                    greenInt.remove(j);
                }
            }
        }

        //if green is not in orange and red , then add

        for(int i =0; i < lNum*wNum ; i++){
            boolean add = true;
            for(int z = 0 ;z < greenInt.size();z++){
                if(greenInt.elementAt(z)==i) {
                    add = false;
                    continue;
                }
            }
            for(int j = 0 ; j < redInt.size() ; j++){
                if(i == redInt.elementAt(j)) {
                    add = false;
                    continue;
                }
                else {
                    for (int w = 0; w < orangeInt.size(); w++) {
                        if (i == orangeInt.elementAt(w)) {
                            add = false;
                            continue;
                        }
                    }
                }
            }
            if(add)
                greenInt.add(i);
        }
        System.out.println("RectangleGroup::greenIntSizeInUpdate:"+greenInt.size());
    }

    //if exist then remove,if not ,add
    //can be use in every Vector<Integer>
    public void addOrDeleteColor(int index,Vector<Integer> color){
        //if find then return
        for(int i = 0; i< color.size(); i++){
            if(color.elementAt(i) == index)
                color.remove(i);
        }
        color.add(index);
        update();
    }

    //use the vertex2D to find an rectangle index
    public int findRectangleIndex(Vertex2D p){
        double initialX = center.x - length/2;
        double initialY = center.y - width/2 ;
        int i = (int)(Math.round((p.x - initialX - length/lNum/2)/(length/lNum)));
        int j = (int)(Math.round((p.y - initialY - width/wNum/2)/(width/wNum)));
        int index = lNum*i+j;
        System.out.println("RectangleGroup::TheIndexI" + i);
        System.out.println("RectangleGroup::TheIndexJ" + j);
        System.out.println("RectangleGroup::TheIndexIs" + index);
        return index;
    }

    public Vertex2D findClickRectanVertex(Vertex3D p){
        Vertex2D d = new Vertex2D(p.x,p.z);
        for(int i = 0;i < groupPointCenter.size();i++){
            Vertex2D t2d = groupPointCenter.elementAt(i);
            if(getLengthOfEach() >= getWidthOfEach()) {
                if (t2d.distance(d) < (getWidthOfEach()/2)) {
                    System.out.println("RectangleGroup::distance:"+t2d.distance(d));
                    System.out.println("RectangleGroup::wantDistance:"+getWidthOfEach()/9);
                    System.out.println("RectangleGroup::PointWeGet:x "+t2d.x+" y: "+t2d.y);
                    return d;
                }
            }else{
                if (t2d.distance(d) < (getLengthOfEach()/2)) {
                    return d;
                }
            }
        }
        return null;
    }

    //The function use to get the red Rectangle Center Point
    /*Vector getGroupShowRedPoint(Vector<Vertex2D> groupPC) {
        Vector<Vertex2D> t = new Vector<>();

        Vertex2D t2d;
        for (int i = 0; i < groupPC.size(); i++) {
            //Compare the Location value with the Red Record
            for (int w = 0; w < redInt.size(); w++) {
                if (i == redInt.elementAt(w)) {
                    t2d = groupPC.elementAt(i);
                    //add the point to the Vector
                    t.add(t2d);
                }
            }
        }
        return t;
    }*/

    //The function can be use in all the color
    Vector getGroupShowColorPoint(Vector<Vertex2D> groupPC,Vector<Integer> groupColor) {
        Vector<Vertex2D> t = new Vector<>();

        Vertex2D t2d;
        for (int i = 0; i < groupPC.size(); i++) {
            for (int w = 0; w < groupColor.size(); w++) {
                //here is different to Red
                if (i == groupColor.elementAt(w)) {
                    t2d = groupPC.elementAt(i);
                    //add the point to the Vector
                    t.add(t2d);
                }
            }
        }
        return t;
    }

    //The function use the
    Vector getGroupShowRectanglePoint(Vector<Vertex2D> groupPC) {
        Vector<Vertex2D> t = new Vector<>();
        Vertex2D t2d;
        for (int i = 0; i < groupPC.size(); i++) {
            //Compare the Location value with the Red Record
            t2d = groupPC.elementAt(i);

            Vertex2D temp1 = new Vertex2D(t2d.x - length / lNum / 2, t2d.y - width / wNum / 2);
            Vertex2D temp2 = new Vertex2D(t2d.x - length / lNum / 2, t2d.y + width / wNum / 2);
            Vertex2D temp3 = new Vertex2D(t2d.x + length / lNum / 2, t2d.y + width / wNum / 2);
            Vertex2D temp4 = new Vertex2D(t2d.x + length / lNum / 2, t2d.y - width / wNum / 2);

            //add the point to the Vector
            t.add(temp1);
            t.add(temp2);
            t.add(temp3);
            t.add(temp4);
        }
        return t;
    }


}
