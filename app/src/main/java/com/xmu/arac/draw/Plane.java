package com.xmu.arac.draw;

//Plane is a class which will describe a plane of base and normal
//Usage:bellow the function you can see the name
public class Plane
{

    public Vertex3D base;
    public Vector3 normal;

    public Plane()
    {
        base = new Vertex3D();
        normal = new Vector3();
    }

    public Plane(Vertex3D _base, Vector3 _normal)
    {
        base = _base;
        normal = _normal;
        normal.normalize();
    }

    public Plane(Vertex3D v0, Vertex3D v1, Vertex3D v2)
    {
        Vector3 vec0 = new Vector3(v0, v1);
        Vector3 vec1 = new Vector3(v0, v2);
        normal = vec0.crossProduct(vec1);
        base = v0;
        normal.normalize();
    }

    public static Plane getNormalSurface(Vertex3D prev_vertex,
                                         Vertex3D center_vertex, Vertex3D next_vertex)
    {
        Vector3 vector0 = new Vector3(prev_vertex, center_vertex);
        Vector3 vector1 = new Vector3(center_vertex, next_vertex);
        vector0.normalize();
        vector1.normalize();
        return new Plane(center_vertex, vector0.add(vector1));
    }

    public double distance(Vertex3D v)
    {
        Vector3 vector = new Vector3(base, v);
        return Math.abs(normal.dotProduct(vector));
    }

    public double signedDistance(Vertex3D v)
    {
        Vector3 vector = new Vector3(base, v);
        return normal.dotProduct(vector);
    }

    public double angle(Vector3 v)
    {
        double angle = normal.getRelativeAngle(v);
        return Math.abs(angle - 1.5707963267948966D);
    }

    //If you want to use the function of crossPoint
    //the normalize of the direction is in need
    public Vertex3D crossPoint(Line line) {
        double planeToLinebase = signedDistance(line.base);
        double cos = -normal.cos(line.direction);
        return new Vertex3D(line.base.add(line.direction
                .multiple(planeToLinebase / cos)));
    }

    public  Vertex3D crossPoint(Ray r) {
        double planeToLinebase = signedDistance(r.base);
        System.out.println("Plane::planeToLinebase::"+planeToLinebase);
        double cos = -normal.cos(r.direction);
        return new Vertex3D(r.base.add(r.direction
                .multiple(planeToLinebase / cos)));
    }
}

