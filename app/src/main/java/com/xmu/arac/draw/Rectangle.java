package com.xmu.arac.draw;

import java.util.Vector;

//Class:Rectangle
//Usage:Easy to Create Rectangle for
public class Rectangle {
    private int length;
    private int height;
    private int width;

    //The center Point of the Rectangle
    private Vertex3D center;

    public Vector points8;

    Rectangle(){
        length = 1;
        height = 1;
        width = 1;
        center = new Vertex3D(0,0,0);
    }

    Rectangle(int _length, int _height, int _width, Vertex3D _center){
        length = _length;
        height = _height;
        width = _width;
        center = new Vertex3D(_center);
    }

    //Method to get the 6 points of Rectangle
    //Retern value need to be improved
    public Vector<Vertex3D> get8Points(){
        //Up 4 Points
        //Face Points records in Vector
        //Up Face 0123
        //Down Face 4567
        //Left Face 0246
        //Right Face 1357
        //Front Face 0145
        //Back Face 2367

        Vertex3D p1 = new Vertex3D(center.x + length/2
                ,center.y + width/2,center.z + height/2);
        Vertex3D p2 = new Vertex3D(center.x - length/2
                ,center.y + width/2,center.z + height/2);
        Vertex3D p3 = new Vertex3D(center.x + length/2
                ,center.y - width/2,center.z + height/2);
        Vertex3D p4 = new Vertex3D(center.x - length/2
                ,center.y - width/2,center.z + height/2);

        //Down 4 Points
        Vertex3D p5 = new Vertex3D(center.x + length/2
                ,center.y + width/2,center.z - height/2);
        Vertex3D p6 = new Vertex3D(center.x - length/2
                ,center.y + width/2,center.z - height/2);
        Vertex3D p7 = new Vertex3D(center.x + length/2
                ,center.y - width/2,center.z - height/2);
        Vertex3D p8 = new Vertex3D(center.x - length/2
                ,center.y - width/2,center.z - height/2);

        //Store in the Vector
        Vector<Vertex3D> t = new Vector<>();
        t.add(p1);
        t.add(p2);
        t.add(p3);
        t.add(p4);
        t.add(p5);
        t.add(p6);
        t.add(p7);
        t.add(p8);

        return t;
    }

    //Return the face points of 6 faces of rectangle
    public Vector<Vertex3D> get6FacesPoints(){
        points8 = get8Points();
        //up 2 faces
        Vector<Vertex3D> t = new Vector<>();
        //This place need to be debugs
        //Up Face
        t.add((Vertex3D) points8.elementAt(0)) ;
        t.add((Vertex3D) points8.elementAt(3)) ;
        t.add((Vertex3D) points8.elementAt(1)) ;
        t.add((Vertex3D) points8.elementAt(0)) ;
        t.add((Vertex3D) points8.elementAt(3)) ;
        t.add((Vertex3D) points8.elementAt(2)) ;
        //Down face
        t.add((Vertex3D) points8.elementAt(4)) ;
        t.add((Vertex3D) points8.elementAt(7)) ;
        t.add((Vertex3D) points8.elementAt(5)) ;
        t.add((Vertex3D) points8.elementAt(4)) ;
        t.add((Vertex3D) points8.elementAt(7)) ;
        t.add((Vertex3D) points8.elementAt(6)) ;
        //Left Face
        t.add((Vertex3D) points8.elementAt(0)) ;
        t.add((Vertex3D) points8.elementAt(6)) ;
        t.add((Vertex3D) points8.elementAt(2)) ;
        t.add((Vertex3D) points8.elementAt(0)) ;
        t.add((Vertex3D) points8.elementAt(6)) ;
        t.add((Vertex3D) points8.elementAt(4)) ;
        //Right face
        t.add((Vertex3D) points8.elementAt(1)) ;
        t.add((Vertex3D) points8.elementAt(7)) ;
        t.add((Vertex3D) points8.elementAt(3)) ;
        t.add((Vertex3D) points8.elementAt(1)) ;
        t.add((Vertex3D) points8.elementAt(7)) ;
        t.add((Vertex3D) points8.elementAt(5)) ;
        //Front Face
        t.add((Vertex3D) points8.elementAt(0)) ;
        t.add((Vertex3D) points8.elementAt(5)) ;
        t.add((Vertex3D) points8.elementAt(1)) ;
        t.add((Vertex3D) points8.elementAt(0)) ;
        t.add((Vertex3D) points8.elementAt(5)) ;
        t.add((Vertex3D) points8.elementAt(4)) ;
        //Back face
        t.add((Vertex3D) points8.elementAt(2)) ;
        t.add((Vertex3D) points8.elementAt(7)) ;
        t.add((Vertex3D) points8.elementAt(3)) ;
        t.add((Vertex3D) points8.elementAt(2)) ;
        t.add((Vertex3D) points8.elementAt(7)) ;
        t.add((Vertex3D) points8.elementAt(6)) ;

        //Return 36 Vertex of the Rectangle
        return t;
    }

}
