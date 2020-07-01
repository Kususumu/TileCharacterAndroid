package com.xmu.arac.draw;

public class Vertex3D {
    public double x;
    public double y;
    public double z;

    Vertex3D(){
        x = 0;
        y = 0;
        z = 0;
    }

    public Vertex3D(int _x, int _y, int _z){
        x = _x;
        y = _y;
        z = _z;
    }

    public Vertex3D(double _x, double _y, double _z){
        x = _x;
        y = _y;
        z = _z;
    }

    public Vertex3D(Vertex3D v){
        x = v.x;
        y = v.y;
        z = v.z;
    }

    Vertex3D(Vector3 v){
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public Vector3 add(Vector3 v){
        return new Vector3(x+v.x,y+v.y,z+v.z);
    }

}
