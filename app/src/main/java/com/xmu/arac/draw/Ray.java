package com.xmu.arac.draw;

public class Ray {
    public Vertex3D base;
    public Vector3 direction;

    Ray(){ }

    public Ray(Vertex3D v,Vector3 d){
        base = new Vertex3D(v);
        direction = new Vector3(d);
        direction.normalize();
    }

}
