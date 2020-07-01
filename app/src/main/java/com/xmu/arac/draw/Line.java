package com.xmu.arac.draw;

//ClassLine:Line has a base point and a direction
public class Line
{

    public Vertex3D base;
    public Vector3 direction;

    Line()
    {
        base = new Vertex3D();
        direction = new Vector3();
    }

    public Line(Vertex3D _base, Vector3 _direction)
    {
        base = _base;
        direction = _direction;
        direction.normalize();
    }

    public double distance(Vertex3D v)
    {
        return Vector3.crossProduct(direction, new Vector3(base, v)).length();
    }
}

